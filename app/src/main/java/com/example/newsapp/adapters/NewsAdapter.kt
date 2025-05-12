package com.example.newsapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.ArticleDetailsActivity
import com.example.newsapp.R
import com.example.newsapp.models.Article

class NewsAdapter(private var articles: List<Article>) :
    RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageArticle: ImageView = itemView.findViewById(R.id.imageArticle)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textDate: TextView = itemView.findViewById(R.id.textDate)
    }

    //Метод для обновления списка
    fun updateArticles(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged() //Уведомляем адаптер, что данные обновились
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.textTitle.text = article.title
        holder.textDate.text = article.date.toString()
        //Используем контекст элемента itemView
        Glide.with(holder.itemView.context)
            .load(article.imageUrl) // URLизображения
            .into(holder.imageArticle) //Вставка изображения в ImageView

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ArticleDetailsActivity::class.java).apply {
                putExtra("title", article.title)
                putExtra("content", article.fullContent)
                putExtra("imageUrl", article.imageUrl)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = articles.size


}
