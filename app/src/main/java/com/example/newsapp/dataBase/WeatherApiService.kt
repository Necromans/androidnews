package com.example.newsapp.dataBase

import com.example.newsapp.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String,
        @Query("aqi") aqi: String = "no"
    ): WeatherResponse
}