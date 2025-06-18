package com.greentag.app.data

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.FileProvider
import com.greentag.app.AppDatabase
import com.greentag.app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AdminActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CupHistoryAdapter
    private lateinit var buttonExportCsv: Button
    private lateinit var buttonShareCsv: Button
    private lateinit var buttonResetDb: Button
    private lateinit var adminLayout: LinearLayout
    private lateinit var textTotalCount: TextView // ✅ 추가

    private val correctPassword = "1234"
    private var exportedCsvFile: File? = null // 공유용 파일 캐싱

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        recyclerView = findViewById(R.id.recyclerViewHistory)
        buttonExportCsv = findViewById(R.id.buttonExportCsv)
        buttonShareCsv = findViewById(R.id.buttonShareCsv)
        buttonResetDb = findViewById(R.id.buttonDBreset)
        adminLayout = findViewById(R.id.adminLayout)
        textTotalCount = findViewById(R.id.textViewTotal)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CupHistoryAdapter()
        recyclerView.adapter = adapter

        buttonExportCsv.setOnClickListener {
            exportCsv()
        }

        buttonShareCsv.setOnClickListener {
            shareCsv()
        }

        buttonResetDb.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("DB 초기화")
                .setMessage("정말로 모든 반납 데이터를 삭제하시겠습니까?")
                .setPositiveButton("삭제") { _, _ ->
                    resetDatabase()
                }
                .setNegativeButton("취소", null)
                .show()
        }


        findViewById<Button>(R.id.buttonGoHome).setOnClickListener {
            finish() }

        loadData()
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val history = AppDatabase.getDatabase(this@AdminActivity).cupReturnDao().getAll()
            runOnUiThread {
                adapter.setData(history)
                textTotalCount.text = "총 반납 수량: ${history.size}개" // ✅ 갱신
            }
        }
    }

    private fun resetDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase(this@AdminActivity).cupReturnDao().deleteAll()
            runOnUiThread {
                adapter.setData(emptyList())
                textTotalCount.text = "총 반납 수량: 0개" // ✅ 초기화 시 갱신
                Toast.makeText(this@AdminActivity, "초기화 완료", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun exportCsv() {
        CoroutineScope(Dispatchers.IO).launch {
            val data = AppDatabase.getDatabase(this@AdminActivity).cupReturnDao().getAll()
            if (data.isEmpty()) {
                runOnUiThread {
                    Toast.makeText(this@AdminActivity, "저장할 데이터가 없습니다", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "greentag_$timeStamp.csv"
            val exportDir = File(getExternalFilesDir(null), "exports")
            if (!exportDir.exists()) exportDir.mkdirs()

            val file = File(exportDir, fileName)
            try {
                FileWriter(file).use { writer ->
                    writer.appendLine("uid,alias,customer,location,status,size,timestamp")
                    for (item in data) {
                        writer.appendLine("${item.uid},${item.alias},${item.customer},${item.location},${item.status},${item.size},${item.timestamp}")
                    }
                }
                exportedCsvFile = file
                runOnUiThread {
                    Toast.makeText(this@AdminActivity, "CSV 저장 완료: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@AdminActivity, "CSV 저장 실패: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun shareCsv() {
        if (exportedCsvFile == null || !exportedCsvFile!!.exists()) {
            // CSV가 없으면 먼저 생성 시도
            CoroutineScope(Dispatchers.IO).launch {
                val data = AppDatabase.getDatabase(this@AdminActivity).cupReturnDao().getAll()
                if (data.isEmpty()) {
                    runOnUiThread {
                        Toast.makeText(this@AdminActivity, "공유할 데이터가 없습니다", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "greentag_$timeStamp.csv"
                val exportDir = File(getExternalFilesDir(null), "exports")
                if (!exportDir.exists()) exportDir.mkdirs()

                val file = File(exportDir, fileName)
                try {
                    FileWriter(file).use { writer ->
                        writer.appendLine("uid,alias,customer,location,status,size,timestamp")
                        for (item in data) {
                            writer.appendLine("${item.uid},${item.alias},${item.customer},${item.location},${item.status},${item.size},${item.timestamp}")
                        }
                    }
                    exportedCsvFile = file

                    runOnUiThread {
                        shareCsvFile(file)
                    }
                } catch (e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@AdminActivity, "CSV 저장 실패: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            shareCsvFile(exportedCsvFile!!)
        }
    }

    private fun shareCsvFile(file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            this,
            "$packageName.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            startActivity(Intent.createChooser(intent, "CSV 파일 공유"))
        } catch (e: Exception) {
            Toast.makeText(this, "공유 실패: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

}
