package com.greentag.app

import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.acs.smartcard.Reader
import com.greentag.app.data.AppDatabase
import com.greentag.app.data.CupReturn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsbNfcReaderActivity : AppCompatActivity() {

    private lateinit var usbManager: UsbManager
    private lateinit var reader: Reader
    private var device: UsbDevice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        reader = Reader(usbManager)

        Log.d("ENTRY", "🧾 USB 연결 대기 중")
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
            finish()
            return
        }

        try {
            reader.open(device)
            readCard()
        } catch (e: Exception) {
            Log.e("ENTRY", "❌ 리더기 연결 실패: ${e.message}")
            finish()
        }
    }

    private fun readCard() {
        Thread {
            try {
                val slot = 0

                Log.d("ENTRY", "reader.power 실행 (COLD_RESET)")
                reader.power(slot, Reader.CARD_COLD_RESET)
                Thread.sleep(300)

                try {
                    Log.d("ENTRY", "reader.setProtocol(PROTOCOL_T1) 실행")
                    reader.setProtocol(slot, Reader.PROTOCOL_T1)
                } catch (e: Exception) {
                    Log.w("ENTRY", "⚠️ setProtocol 예외: ${e.message}")
                }

                var state = reader.getState(slot)
                var retry = 0
                while (state != Reader.CARD_SPECIFIC && retry < 5) {
                    Log.d("ENTRY", "⏳ 카드 상태 polling 중... state=$state / 재시도=$retry")
                    Thread.sleep(200)
                    state = reader.getState(slot)
                    retry++
                }

                if (state != Reader.CARD_SPECIFIC) {
                    Log.e("ENTRY", "❌ SPECIFIC 상태 아님 → transmit 중단됨")
                    return@Thread
                }

                val command = byteArrayOf(0xFF.toByte(), 0xCA.toByte(), 0x00, 0x00, 0x00)
                val response = ByteArray(256)
                val responseLength = 256

                val result = reader.transmit(slot, command, command.size, response, responseLength)
                Log.d("ENTRY", "📡 transmit result: $result")

                val actualLength = responseLength.coerceIn(4, 12)
                val uidBytes = response.copyOf(actualLength)
                val uid = uidBytes.joinToString("") { "%02X".format(it) }

                if (uid.length !in 8..24) {
                    Log.e("ENTRY", "❌ UID 길이 비정상 ($uid)")
                    return@Thread
                }

                Log.d("ENTRY", "✅ UID 읽힘: $uid")

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db = AppDatabase.getDatabase(this@UsbNfcReaderActivity)
                        db.cupReturnDao().insert(CupReturn(uid, System.currentTimeMillis()))
                        Log.d("ENTRY", "✅ RoomDB에 저장됨: $uid")

//                        val intent = Intent(this@UsbNfcReaderActivity, SuccessActivity::class.java)
//                        intent.putExtra("uid", uid)
//                        startActivity(intent)
//                        Log.d("ENTRY", "🎉 SuccessActivity 진입 시도됨")
//                        finish()
                    } catch (e: Exception) {
                        Log.e("ENTRY", "❌ 저장 후 화면 전환 실패: ${e.message}")
                        withContext(Dispatchers.Main) {
                            finish()
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("ENTRY", "❌ UID 읽기 실패: ${e.message}")
                runOnUiThread {
                    finish()
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            reader.close()
        } catch (_: Exception) {}
    }
}
