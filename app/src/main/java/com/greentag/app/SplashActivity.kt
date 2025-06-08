package com.greentag.app

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.acs.smartcard.Reader
import com.greentag.app.data.AppDatabase
import com.greentag.app.data.CupReturn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {

    private lateinit var usbManager: UsbManager
    private lateinit var reader: Reader
    private var device: UsbDevice? = null

    private lateinit var textViewSummary: TextView
    private lateinit var textViewCo2: TextView
    private lateinit var textViewTree: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var buttonReset: Button

    private val goal = 3000
    private val co2PerCup = 0.02
    private val treePer3000 = 1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_splash)

        textViewSummary = findViewById(R.id.textViewSummary)
        textViewCo2 = findViewById(R.id.textViewCo2)
        textViewTree = findViewById(R.id.textViewTree)
        progressBar = findViewById(R.id.progressBar)
        buttonReset = findViewById(R.id.buttonReset)

        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        reader = Reader(usbManager)

        // ✅ 앱 실행 시 저장된 컵 수 표시
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@SplashActivity)
            val count = db.cupReturnDao().getCount()

            withContext(Dispatchers.Main) {
                updateUI(count)
            }
        }

        // ✅ 초기화 버튼
        buttonReset.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(this@SplashActivity)
                db.cupReturnDao().deleteAll()
                val count = db.cupReturnDao().getCount()

                withContext(Dispatchers.Main) {
                    updateUI(count)
                }

                Log.d("ENTRY", "🧹 RoomDB 초기화 완료")
            }
        }

        connectToUsbDevice()
    }

    private fun connectToUsbDevice() {
        for (d in usbManager.deviceList.values) {
            if (reader.isSupported(d)) {
                device = d
                break
            }
        }

        if (device == null) {
            Log.e("ENTRY", "❌ USB 리더기 없음")
            return
        }

        try {
            reader.open(device)
            readLoop()
        } catch (e: Exception) {
            Log.e("ENTRY", "❌ 리더기 연결 실패: ${e.message}")
        }
    }

    private fun readLoop() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                try {
                    val slot = 0
                    reader.power(slot, Reader.CARD_COLD_RESET)
                    delay(300)
                    reader.setProtocol(slot, Reader.PROTOCOL_T1)

                    val state = reader.getState(slot)
                    if (state != Reader.CARD_SPECIFIC) {
                        Log.e("ENTRY", "❌ 카드 미감지 상태 (state=$state)")
                        delay(1000)
                        continue
                    }

                    val command = byteArrayOf(0xFF.toByte(), 0xCA.toByte(), 0x00, 0x00, 0x00)
                    val response = ByteArray(256)
                    val responseLength = 256

                    val result = reader.transmit(slot, command, command.size, response, responseLength)
                    val actualLength = responseLength.coerceIn(4, 12)
                    val uid = response.copyOf(actualLength).joinToString("") { "%02X".format(it) }

                    if (uid.length !in 8..24) continue

                    Log.d("ENTRY", "✅ UID 읽힘: $uid")

                    val db = AppDatabase.getDatabase(this@SplashActivity)
                    val dao = db.cupReturnDao()

                    val exists = dao.getAll().any { it.uid == uid }
                    if (exists) {
                        Log.d("ENTRY", "⚠️ 이미 등록된 UID: $uid")
                    } else {
                        dao.insert(CupReturn(uid, System.currentTimeMillis()))
                        Log.d("ENTRY", "✅ Room 저장됨: $uid")
                    }

                    val count = dao.getCount()
                    withContext(Dispatchers.Main) {
                        updateUI(count)
                    }

                    delay(2000)

                } catch (e: Exception) {
                    Log.e("ENTRY", "❌ 예외 발생: ${e.message}")
                    delay(1000)
                }
            }
        }
    }

    private fun updateUI(count: Int) {
        textViewSummary.text = "지금까지 접수한 컵 $count 개"
        val co2 = count * co2PerCup
        val tree = count * (treePer3000 / goal)
        textViewCo2.text = "↓ %.2f kg".format(co2)
        textViewTree.text = "모두가 힘을 합쳐 %.2f 그루의 나무를 심었어요!".format(tree)
        progressBar.max = goal
        progressBar.progress = count.coerceAtMost(goal)
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            reader.close()
        } catch (_: Exception) {}
    }
}
