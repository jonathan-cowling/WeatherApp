package tk.jonathancowling.weatherapp.ui.activity.main

import tk.jonathancowling.weatherapp.data.City
import tk.jonathancowling.weatherapp.data.Forecast
import tk.jonathancowling.weatherapp.data.TemperatureUnit
import tk.jonathancowling.weatherapp.data.Weather
import tk.jonathancowling.weatherapp.util.LifeCycleListener

interface MainContract {
  fun setWeather(weather: Weather, city: City)
  fun setWeather(weather: Weather, city: City, forecast: Forecast)
  fun setError(e: Throwable)
  fun setLoading()
  fun setSwitchUnitMenuItem(unit: TemperatureUnit)
  fun weatherAlreadyUpToDate()

  var getWeatherListener: () -> Unit
  var switchUnitListener: () -> Unit
  var lifecycleListener: LifeCycleListener
}
