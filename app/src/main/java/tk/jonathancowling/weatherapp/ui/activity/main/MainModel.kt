package tk.jonathancowling.weatherapp.ui.activity.main

import tk.jonathancowling.weatherapp.data.City
import tk.jonathancowling.weatherapp.data.Forecast
import tk.jonathancowling.weatherapp.data.Weather
import org.threeten.bp.Instant

class MainModel(var weather: Weather? = null,
                var city: City? = null,
                var lastUpdate: Instant? = null,
                var forecast: Forecast? = null)
