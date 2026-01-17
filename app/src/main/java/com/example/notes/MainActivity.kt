package com.example.notes

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ApiService
class MainActivity : AppCompatActivity() {

    // Deklarasi apiService di luar agar bisa dipakai di loadNotes()
    private lateinit var apiService: ApiService
    private lateinit var tvNotesList: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val btnSave = findViewById<Button>(R.id.btnSave)
        tvNotesList = findViewById<TextView>(R.id.tvNotesList)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.4/notes/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Panggil data saat pertama kali aplikasi dibuka
        loadNotes()

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Isi semua bidang!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            apiService.addNote(title, content).enqueue(object : Callback<NoteResponse> {
                override fun onResponse(call: Call<NoteResponse>, response: Response<NoteResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Berhasil simpan!", Toast.LENGTH_SHORT).show()
                        etTitle.text.clear()
                        etContent.text.clear()
                        loadNotes() // Refresh daftar setelah simpan
                    }
                }

                override fun onFailure(call: Call<NoteResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun loadNotes() {
        apiService.getNotes().enqueue(object : Callback<List<NoteResponse>> {
            override fun onResponse(call: Call<List<NoteResponse>>, response: Response<List<NoteResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    val notes = response.body()!!
                    val stringBuilder = StringBuilder()

                    // Susun data menjadi teks memanjang ke bawah
                    for (note in notes) {
                        stringBuilder.append("ID: ${note.id}\n")
                        stringBuilder.append("Judul: ${note.title}\n")
                        stringBuilder.append("Isi: ${note.content}\n")
                        stringBuilder.append("---------------------------\n")
                    }
                    tvNotesList.text = if (notes.isEmpty()) "Tidak ada catatan." else stringBuilder.toString()
                }
            }

            override fun onFailure(call: Call<List<NoteResponse>>, t: Throwable) {
                tvNotesList.text = "Gagal memuat data: ${t.message}"
            }
        })
    }
}