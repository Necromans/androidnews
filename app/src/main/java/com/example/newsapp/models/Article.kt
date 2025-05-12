package com.example.newsapp.models



data class Article(
    val id: Int = 0,
    val title: String,
    val fullContent: String,
    val imageUrl: String,
    val content: String,
    val date: String,
    val authorId: Int
)
