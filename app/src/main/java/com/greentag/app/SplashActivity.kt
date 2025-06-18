package com.greentag.app

import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.acs.smartcard.Reader
import com.acs.smartcard.ReaderException
import com.google.android.material.button.MaterialButton
import com.greentag.app.data.AdminActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {

    private lateinit var reader: Reader
    private lateinit var usbManager: UsbManager

    private lateinit var progressBar: ProgressBar
    private lateinit var textViewSummary: TextView
    private lateinit var textViewCo2: TextView
    private lateinit var textViewTree: TextView
    private lateinit var textViewMessage: TextView
    private lateinit var buttonAdmin: MaterialButton

    private val tag = "ENTRY"
    private var isReaderOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_splash)

        usbManager = getSystemService(USB_SERVICE) as UsbManager
        reader = Reader(usbManager)

        progressBar = findViewById(R.id.progressBar)
        textViewSummary = findViewById(R.id.textViewSummary)
        textViewCo2 = findViewById(R.id.textViewCo2)
        textViewTree = findViewById(R.id.textViewTree)
        textViewMessage = findViewById(R.id.textViewMessage)
        buttonAdmin = findViewById(R.id.buttonAdmin)

        buttonAdmin.setOnClickListener {
            // 비밀번호 입력 다이얼로그
            val builder = AlertDialog.Builder(this)
            builder.setTitle("관리자 비밀번호 입력")

            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            builder.setView(input)

            builder.setPositiveButton("확인") { dialog, _ ->
                val enteredPassword = input.text.toString()
                if (enteredPassword == "1234") {
                    startActivity(Intent(this, AdminActivity::class.java))
                } else {
                    Toast.makeText(this, "비밀번호가 올바르지 않습니다", Toast.LENGTH_SHORT).show()
                }
            }

            builder.setNegativeButton("취소") { dialog, _ -> dialog.cancel() }
            builder.show()
        }


        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@SplashActivity)
            val dao = db.cupReturnDao()
            val count = dao.getCount()
            withContext(Dispatchers.Main) {
                updateUI(count)
                showMessage("✅ 앱 시작됨 - 기존 반납 수 불러옴")
            }
        }

        startNfcLoop()
    }

    private fun startNfcLoop() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                try {
                    val device: UsbDevice? = usbManager.deviceList.values.find {
                        reader.isSupported(it)
                    }

                    if (device == null) {
                        showMessage("❌ USB 리더기를 찾을 수 없습니다")
                        Log.e(tag, "❌ USB 리더기 없음")
                        delay(2000)
                        continue
                    }

                    if (!isReaderOpened) {
                        try {
                            reader.open(device)
                            isReaderOpened = true
                            showMessage("✅ 리더기 연결 성공")
                            Log.d(tag, "✅ reader.open 성공")
                        } catch (e: Exception) {
                            showMessage("❌ 리더기 열기 실패: ${e.message}")
                            Log.e(tag, "❌ reader.open 실패: ${e.message}")
                            delay(2000)
                            continue
                        }
                    }

                    val slot = 0
                    reader.power(slot, Reader.CARD_COLD_RESET)
                    delay(300)
                    reader.setProtocol(slot, Reader.PROTOCOL_T1)

                    val state = reader.getState(slot)
                    if (state != Reader.CARD_SPECIFIC) {
                        showMessage("⏳ 카드가 제대로 삽입되지 않았습니다 (state=$state)")
                        Log.d(tag, "❌ 카드 SPECIFIC 상태 아님 → 현재 state=$state")
                        delay(1000)
                        continue
                    }

                    val command = byteArrayOf(0xFF.toByte(), 0xCA.toByte(), 0x00, 0x00, 0x00)
                    val response = ByteArray(256)
                    val result = reader.transmit(slot, command, command.size, response, response.size)

                    if (result < 2) {
                        showMessage("⚠️ 카드 응답 오류 (길이: $result)")
                        Log.e(tag, "⚠️ 응답 너무 짧음: $result bytes")
                        delay(1000)
                        continue
                    }

                    val uidBytes = response.copyOf(result - 2)
                    val uid = uidBytes.joinToString("") { "%02X".format(it) }

                    if (uid.length !in 8..24) {
                        showMessage("⚠️ UID 오류: $uid")
                        Log.e(tag, "⚠️ UID 길이 이상: $uid")
                        delay(1000)
                        continue
                    }

                    Log.d(tag, "✅ UID 읽힘: $uid")
                    showMessage("✅ 태그 인식됨: $uid")

                    val db = AppDatabase.getDatabase(this@SplashActivity)
                    val dao = db.cupReturnDao()
                    val exists = dao.getAll().any { it.uid == uid }

                    if (!exists) {
                        dao.insert(
                            CupReturn(
                                uid = uid,
                                alias = "중원대컵$uid",
                                customer = "중원대학교",
                                location = "정문 부스",
                                status = "returned",
                                size = "M",
                                timestamp = System.currentTimeMillis()
                            )
                        )
                        Log.d(tag, "✅ RoomDB 저장 완료: $uid")
                        showMessage("✅ 새로운 컵 반납 기록 완료")
                    } else {
                        Log.d(tag, "⚠️ 이미 등록된 UID: $uid")
                        showMessage("⚠️ 이미 반납된 컵입니다")
                    }

                    val count = dao.getCount()
                    withContext(Dispatchers.Main) {
                        updateUI(count)
                    }

                    delay(1500)

                } catch (e: ReaderException) {
                    Log.e(tag, "❌ Reader 예외: ${e.message}")
                    showMessage("⏳ 컵 반납 대기 중입니다...")  // ✅ 사용자에게 부드러운 안내
                    delay(1000)
                } catch (e: Exception) {
                    Log.e(tag, "❌ 일반 예외: ${e.message}")
                    showMessage("⏳ 컵 반납 대기 중입니다...")  // ✅ 예외 메시지 숨기고 사용자 안내
                    delay(1000)
                }
            }
        }
    }

    private suspend fun showMessage(msg: String) {
        withContext(Dispatchers.Main) {
            textViewMessage.text = msg
        }
    }

    private fun updateUI(count: Int) {
        textViewSummary.text = "지금까지 접수한 컵 $count 개"
        val co2Saved = count * 0.025
        textViewCo2.text = "↓ %.2f kg".format(co2Saved)
        val trees = count / 200
        textViewTree.text = "모두가 힘을 합쳐 $trees 그루의 나무를 심었어요!"
        progressBar.max = 3000
        progressBar.progress = count.coerceAtMost(3000)
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@SplashActivity)
            val dao = db.cupReturnDao()
            val count = dao.getCount()
            withContext(Dispatchers.Main) {
                updateUI(count)
                Log.d(tag, "🔁 onResume: DB 최신 반납 수 = $count")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (isReaderOpened) {
                reader.close()
                isReaderOpened = false
            }
        } catch (_: Exception) {}
    }
}
