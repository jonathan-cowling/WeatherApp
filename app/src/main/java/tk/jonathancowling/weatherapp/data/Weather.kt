package tk.jonathancowling.weatherapp.data

data class Weather(val temperature: Float,
                   val description: String,
                   val unit: TemperatureUnit,
                   val code: WeatherCode) {

  fun changeUnit(to: TemperatureUnit) = toKelvin().kelvinTo(to)

  private fun kelvinTo(newUnit: TemperatureUnit): Weather {
    if (unit != TemperatureUnit.KELVIN) {
      throw IllegalStateException("temperature unit isn't kelvin")
    }
    return Weather(kelvinTo(temperature, newUnit), description, newUnit, code)
  }

  private fun toKelvin() = Weather(toKelvin(temperature, unit), description, TemperatureUnit.KELVIN, code)

  private fun kelvinTo(kelvin: Float, newUnit: TemperatureUnit): Float = when (newUnit) {
    TemperatureUnit.CELSIUS -> {
      kelvin - 273.15f
    }
    TemperatureUnit.FAHRENHEIT -> {
      kelvin * 9f / 5 - 459.67f
    }
    TemperatureUnit.KELVIN -> {
      kelvin
    }
  }

  private fun toKelvin(temp: Float, from: TemperatureUnit): Float = when (from) {
    TemperatureUnit.CELSIUS -> {
      temp + 273.15f
    }
    TemperatureUnit.FAHRENHEIT -> {
      (temp + 459.67f) * 5f / 9
    }
    TemperatureUnit.KELVIN -> {
      temp
    }
  }

  companion object {
    fun fromResponse(currentWeatherResponse: CurrentWeatherApiResponse): Weather {
      return Weather(currentWeatherResponse.mainWeather.temperature,
        currentWeatherResponse.weatherDescription.firstOrNull()?.description
          ?: throw IllegalArgumentException("weatherDescription must not be empty"),
        TemperatureUnit.KELVIN,
        WeatherCode.values().firstOrNull {
          it.code == currentWeatherResponse.weatherDescription.first().id
        }
          ?: throw IllegalArgumentException("weatherDescription code must be a valid WeatherCode.code"))
    }

    fun fromResponse(weatherInForecast: ForecastWeatherApiResponse.Weather): Weather {
      return Weather(weatherInForecast.mainWeather.temperature,
        weatherInForecast.weatherDescription.firstOrNull()?.description
          ?: throw IllegalArgumentException("weatherDescription must not be empty"),
        TemperatureUnit.KELVIN,
        WeatherCode.values().firstOrNull {
          it.code == weatherInForecast.weatherDescription.first().id
        }
          ?: throw IllegalArgumentException("weatherDescription code must be a valid WeatherCode.code"))
    }
  }
}
