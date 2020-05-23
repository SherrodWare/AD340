package com.example.zipcode

interface AppNavigator {
    fun navigateToCurrentForecast(zipcode: String)
    fun navigateToLocationEntry()
}