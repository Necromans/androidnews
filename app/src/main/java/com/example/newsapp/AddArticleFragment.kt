package com.example.newsapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.newsapp.databinding.FragmentAddArticleBinding
import com.example.newsapp.models.Article
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale


class AddArticleFragment : Fragment() {

    private lateinit var binding: FragmentAddArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddArticleBinding.inflate(inflater, container, false)

        binding.btnAddArticle.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val content = binding.editContent.text.toString()
            val fullContent = binding.editfullContent.text.toString()
            val imageUrl = binding.editImageUrl.text.toString()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val formattedDate = dateFormat.format(Date())
            val date = formattedDate;
            Log.d("AddArticleFragmentAPI", "Title: $title\nContent: $content\nfullContent: $fullContent\nImageUrl: $imageUrl\nDate: $date")


            val sharedPref = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
            val authorId = sharedPref.getInt("id", -1)
            Log.d("AddArticle", "$authorId")
            //Проверяем, что все поля заполнены
            if (title.isNotEmpty() && content.isNotEmpty() && fullContent.isNotEmpty() && imageUrl.isNotEmpty() && date.toString().isNotEmpty()) {
                val article = Article(
                    id = 0,
                    title = title,
                    fullContent = fullContent,
                    imageUrl = imageUrl,
                    content = content,
                    date = date,
                    authorId = authorId
                )


                //Отправляем данные на сервер
                sendArticleToServer(article)
                val newFragment = HomeFragment()  //Это новый фрагмент, который ты хочешь открыть
                val transaction = parentFragmentManager.beginTransaction()
                //Заменяем текущий фрагмент на новый
                transaction.replace(R.id.fragment_container, newFragment) //R.id.fragment_container — это контейнер, в который помещаются фрагменты
                //Можно добавить транзакцию в стек, чтобы можно было вернуться назад
                transaction.addToBackStack(null)

                //Выполняем транзакцию
                transaction.commit()

            } else {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun sendArticleToServer(article: Article) {
        val articleJson = JSONObject()
        articleJson.put("title", article.title)
        articleJson.put("content", article.content)
        articleJson.put("fullContent", article.fullContent)
        articleJson.put("imageUrl", article.imageUrl)
        articleJson.put("date", article.date)
        articleJson.put("authorId", article.authorId)

        Log.d("AddArticleFragmentAPI", "Sending Article: $articleJson")
        //Запускаем асинхронный запрос на сервер
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = Retrofit.api.addArticle(article)
                if (response.isSuccessful) {
                    Toast.makeText(context, "Article added successfully", Toast.LENGTH_SHORT).show()

                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(context, "Failed to add article", Toast.LENGTH_SHORT).show()
                    Log.d("AddArticleFragmentAPI", "$errorBody")
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("AddArticleFragmentAPI", "Error: ${e.message}")
            }
        }
    }
}
