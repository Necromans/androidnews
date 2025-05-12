package com.example.newsapp

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.newsapp.dataBase.WeatherApi
import com.example.newsapp.databinding.ActivityWeatherFragmentBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class WeatherFragment : Fragment(R.layout.activity_weather_fragment), CoroutineScope {

    private lateinit var binding: ActivityWeatherFragmentBinding
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Main + job

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = ActivityWeatherFragmentBinding.bind(view)

        //Настройка Spinner для выбора города
        val cities = resources.getStringArray(R.array.cities_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.citySpinner.adapter = adapter

        binding.citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val selectedCity = parentView.getItemAtPosition(position).toString()
                fetchWeatherData(selectedCity)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                //Обработка случая, если ничего не выбрано
                fetchWeatherData("Almaty")
            }
        }

        // значальная загрузка погоды для первого города (например, Almaty)
        fetchWeatherData("Almaty")
    }

    //Функция для загрузки погоды для выбранного города
    private fun fetchWeatherData(city: String) {
        launch {
            try {
                val weather = withContext(IO) {
                    WeatherApi.api.getCurrentWeather(
                        apiKey = "52572d0299764691b54144923252304",
                        city = city
                    )
                }

                binding.textCity.text = city
                binding.textTemperature.text = "${weather.current.temp_c}°C"
                binding.textDescription.text = weather.current.condition.text
                binding.textHumidity.text = "Humidity: ${weather.current.humidity}%"


            } catch (e: Exception) {
                e.printStackTrace()
                binding.textTemperature.text = "Error loading weather"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel() //Останавливаем корутину при уничтожении фрагмента
    }
}
