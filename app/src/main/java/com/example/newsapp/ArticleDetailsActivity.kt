package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ActivityArticleDetailsBinding

class ArticleDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Получаем данные из Intent
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val imageUrl = intent.getStringExtra("imageUrl")


        //Устанавливаем вьюшки
        binding.textTitle.text = title
        binding.textDescription.text = content

        //Загрузка изображения по URL
        Glide.with(this)  //Используем activity как контекст
            .load(imageUrl)  //URL изображения
            .into(binding.imageView)  //Вставка изображения в ImageView

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.textDescription.apply {
            movementMethod = ScrollingMovementMethod()  //Включаем прокрутку
            text = content  //Устанавливаем текст
        }

    }


}
