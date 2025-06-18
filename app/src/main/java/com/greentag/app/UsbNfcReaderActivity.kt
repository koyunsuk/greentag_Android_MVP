// ✅ 1. 전체 재작성 버전 (기존 흐름 유지, reader 충돌 해결)
package com.greentag.app

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.acs.smartcard.Reader
import kotlinx.coroutines.*

class UsbNfcReaderActivity : AppCompatActivity() {

    private lateinit var usbManager: UsbManager
    private lateinit var reader: Reader
    private var device: UsbDevice? = null
    private val TAG = "ENTRY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        reader = Reader(usbManager)

        Log.d(TAG, "🧾 USB 연결 대기 중")
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
            Log.e(TAG, "❌ USB 리더기 없음")
            finish()
            return
        }

        try {
            try {
                reader.close()
            } catch (_: Exception) {}
            reader.open(device)
            Log.d(TAG, "✅ reader.open 성공")
            readCard()
        } catch (e: Exception) {
            Log.e(TAG, "❌ reader.open 실패: ${e.message}")
            finish()
        }
    }

    private fun readCard() {
        Thread {
            try {
                val slot = 0
                reader.power(slot, Reader.CARD_COLD_RESET)
                Thread.sleep(300)

                try {
                    reader.setProtocol(slot, Reader.PROTOCOL_T1)
                } catch (e: Exception) {
                    Log.w(TAG, "⚠️ setProtocol 예외: ${e.message}")
                }

                var state = reader.getState(slot)
                var retry = 0
                while (state != Reader.CARD_SPECIFIC && retry < 5) {
                    Log.d(TAG, "⏳ 카드 상태 polling 중... state=$state / 재시도=$retry")
                    Thread.sleep(200)
                    state = reader.getState(slot)
                    retry++
                }

                if (state != Reader.CARD_SPECIFIC) {
                    Log.e(TAG, "❌ SPECIFIC 상태 아님 → transmit 중단됨")
                    return@Thread
                }

                val command = byteArrayOf(0xFF.toByte(), 0xCA.toByte(), 0x00, 0x00, 0x00)
                val response = ByteArray(256)
                val result = reader.transmit(slot, command, command.size, response, 256)

                val uid = response.copyOf(result).joinToString("") { "%02X".format(it) }

                if (uid.length !in 8..24) {
                    Log.e(TAG, "❌ UID 길이 비정상 ($uid)")
                    return@Thread
                }

                Log.d(TAG, "✅ UID 읽힘: $uid")

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db = AppDatabase.getDatabase(this@UsbNfcReaderActivity)
                        val newCup = CupReturn(
                            uid = uid,
                            alias = "중원대컵$uid",
                            status = "returned",
                            location = "중원대 컵반납",
                            size = "M",
                            customer = "중원대",
                            timestamp = System.currentTimeMillis()
                        )
                        db.cupReturnDao().insert(newCup)
                        Log.d(TAG, "✅ RoomDB에 저장됨: $uid")
                    } catch (e: Exception) {
                        Log.e(TAG, "❌ 저장 실패: ${e.message}")
                        withContext(Dispatchers.Main) { finish() }
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "❌ UID 읽기 실패: ${e.message}")
                runOnUiThread { finish() }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            reader.close()
            Log.d(TAG, "🔌 reader 정상 종료됨")
        } catch (_: Exception) {}
    }
}
