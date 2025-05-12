package com.example.newsapp

import Retrofit
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.models.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var newsAdapter: NewsAdapter
    private var articles: List<Article> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Инициализация binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val recyclerView = binding.recyclerView
        val searchView = binding.searchView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //Инициализация адаптера с пустым списком
        newsAdapter = NewsAdapter(articles)
        recyclerView.adapter = newsAdapter

        //CoroutineScope для асинхронных операций
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = Retrofit.api.getArticles()
                withContext(Dispatchers.Main) {
                    if (response.isNotEmpty()) {
                        Log.d("API_Response", "Received ${response.size} articles")
                        articles = response
                        newsAdapter.updateArticles(articles) // Обновление адаптера
                    } else {
                        Log.d("API_Response", "No articles found")
                    }
                }
            } catch (e: Exception) {
                Log.e("API_Error", "Error: ${e.message}")
            }
        }

        //Слушатель для поиска
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = articles.filter {
                    it.title.contains(newText ?: "", ignoreCase = true) ||
                            it.content.contains(newText ?: "", ignoreCase = true)
                }
                newsAdapter.updateArticles(filtered)
                return true
            }
        })

        return view
    }
}

