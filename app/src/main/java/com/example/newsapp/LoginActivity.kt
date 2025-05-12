package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivityLoginBinding
import com.example.newsapp.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Инициализация ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Обработка нажатия кнопки "Войти"
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(0, "", email, password)
                CoroutineScope(Dispatchers.IO).launch {
                    val response = Retrofit.api.login(user)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val loginResponse  = response.body()
                            val loggedInUser = loginResponse?.user
                            Log.d("LoginActivity", "Response body: $loggedInUser")

                            //Логирование данных пользователя
                            Log.d("LoginActivity", "Logged in user ID: ${loggedInUser?.id}")
                            Log.d("LoginActivity", "Logged in user email: ${loggedInUser?.email}")
                            Log.d("LoginActivity", "Logged in user name: ${loggedInUser?.name}")
                            val sharedPref = getSharedPreferences("UserData", MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("email", loggedInUser?.email)
                                putString("name", loggedInUser?.name ?: "")
                                putInt("id", loggedInUser?.id ?: -1)
                                apply()
                            }

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
