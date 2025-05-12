package com.example.newsapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentMyArticlesBinding
import com.example.newsapp.models.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyArticlesFragment : Fragment() {
    private lateinit var binding: FragmentMyArticlesBinding
    private lateinit var newsAdapter: NewsAdapter
    private var articles: List<Article> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //Инициализация адаптера с пустым списком
        newsAdapter = NewsAdapter(articles)
        recyclerView.adapter = newsAdapter

        val sharedPref = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val id = sharedPref.getInt("id", -1)

        //CoroutineScope для асинхронных операций
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = Retrofit.api.getArticlesByAuthor(id)
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

        return view
    }
}