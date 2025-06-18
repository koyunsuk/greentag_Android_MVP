package com.greentag.app

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.greentag.app.data.AppDatabase
import com.greentag.app.util.CsvExporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class AdminActivity : AppCompatActivity() {

    private lateinit var buttonCsv: Button
    private lateinit var buttonExcel: Button
    private lateinit var buttonReset: Button
    private lateinit var buttonBack: Button
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // ✅ 버튼과 리스트뷰 연결
        buttonCsv = findViewById(R.id.buttonExportCsv)
        buttonExcel = findViewById(R.id.buttonExportExcel)
        buttonReset = findViewById(R.id.buttonResetDatabase)
        buttonBack = findViewById(R.id.buttonBackToMain)
        listView = findViewById(R.id.listViewCups)

        updateListView()

        // ✅ CSV 내보내기
        buttonCsv.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val data = AppDatabase.getDatabase(this@AdminActivity).cupReturnDao().getAll()
                val file: File? = CsvExporter.export(this@AdminActivity, data)
                launch(Dispatchers.Main) {
                    if (file != null) {
                        Toast.makeText(
                            this@AdminActivity,
                            "✅ CSV 내보내기 완료: ${file.absolutePath}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this@AdminActivity, "❌ CSV 저장 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // ⏳ Excel (준비 중)
        buttonExcel.setOnClickListener {
            Toast.makeText(this, "📦 엑셀 저장 기능은 추후 업데이트 예정입니다.", Toast.LENGTH_SHORT).show()
        }

        // 🔄 DB 초기화
        buttonReset.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("초기화")
                .setMessage("모든 반납 데이터를 삭제하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    lifecycleScope.launch(Dispatchers.IO) {
                        AppDatabase.getDatabase(this@AdminActivity).cupReturnDao().deleteAll()
                        launch(Dispatchers.Main) {
                            updateListView()
                            Toast.makeText(this@AdminActivity, "✅ 데이터 초기화 완료", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("아니오", null)
                .show()
        }

        // 🔙 메인화면으로 돌아가기
        buttonBack.setOnClickListener {
            finish() // SplashActivity로 돌아감
        }
    }

    // 🔄 리스트뷰 업데이트
    private fun updateListView() {
        lifecycleScope.launch(Dispatchers.IO) {
            val data = AppDatabase.getDatabase(this@AdminActivity).cupReturnDao().getAll()
            val adapter = CSVPreviewAdapter(this@AdminActivity, data)
            launch(Dispatchers.Main) {
                listView.adapter = adapter
            }
        }
    }
}
