package tk.jonathancowling.weatherapp.ui.activity.main

import tk.jonathancowling.weatherapp.business.LocationService
import tk.jonathancowling.weatherapp.business.WeatherService

class MainInteractor(private val weatherService: WeatherService,
                     private val locationService: LocationService) {
  fun getWeather(id: Int) = weatherService.getWeather(id)

  fun getForecast(id: Int) = weatherService.getForecast(id)

  // TODO: wrap in Single and compose with getWeather and make it get a whole city
  fun getCityId(name: String) = locationService.getCityId(name)
}
