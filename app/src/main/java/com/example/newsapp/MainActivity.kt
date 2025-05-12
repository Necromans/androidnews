package com.example.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Пример текста на экране
        binding.tvWelcome.text = "Добро пожаловать в NewsApp!"

        //Инициализация BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        //Проверка на savedInstanceState, чтобы не повторно загружать фрагмент
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()) //Загружаем фрагмент на старте
                .commit()
        }

        //Обработчик кликов по пунктам меню
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> openFragment(HomeFragment()) //Загружаем фрагмент при выборе "Главная"
                R.id.nav_weather -> openFragment(WeatherFragment())
                R.id.nav_add -> openFragment(AddArticleFragment())
                R.id.nav_profile -> openFragment(UserProfileFragment())
                R.id.nav_articles -> openFragment(MyArticlesFragment())
                //Другие пункты меню можно добавить сюда
            }
            true
        }
    }

    //Функция для загрузки фрагмента
    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
