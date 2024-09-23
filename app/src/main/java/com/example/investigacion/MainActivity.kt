package com.example.investigacion

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {
    val urlBase = "https://jsonplaceholder.typicode.com/"
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById(R.id.rv_posts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PostAdapter(arrayListOf())
        recyclerView.adapter = adapter


        val refreshButton: Button = findViewById(R.id.btn_refresh)
        refreshButton.setOnClickListener {
            fetchAndDisplayPosts()
        }


        fetchAndDisplayPosts()
    }

    private fun fetchAndDisplayPosts() {
        val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(PostApiService::class.java)

        lifecycleScope.launch {
            runCatching {
                val response = service.getUserPost()
                if (response.isEmpty()) {
                    throw Exception("La respuesta está vacía.")
                }
                response
            }.onSuccess { posts ->
                val shuffledPosts = posts.shuffled()
                adapter.updatePosts(shuffledPosts)

                Toast.makeText(this@MainActivity, "Solicitud GET exitosa", Toast.LENGTH_SHORT).show()
            }.onFailure { e ->
                val message = when (e) {
                    is IOException -> "Ha ocurrido un error de red: verifica tu conexxion a internet"
                    is HttpException -> "Error del servidor: ${e.code()}"
                    else -> "Ha ocurrido un error: ${e.message}"
                }

                Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
            }
        }
    }

}

