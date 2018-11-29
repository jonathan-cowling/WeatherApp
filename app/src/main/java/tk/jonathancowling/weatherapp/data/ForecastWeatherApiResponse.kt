package tk.jonathancowling.weatherapp.data

import com.google.gson.annotations.SerializedName

data class ForecastWeatherApiResponse(
  @SerializedName("list") val weatherList: List<Weather>
) {

  data class Weather(@SerializedName("dt") val timestamp: Long,
                     @SerializedName("main") val mainWeather: ForecastWeatherApiResponse.MainWeather,
                     @SerializedName("weather") val weatherDescription: List<ForecastWeatherApiResponse.WeatherDescription>,
                     val wind: Wind,
                     val clouds: Clouds,
                     val rain: Rain,
                     val snow: Snow)

  data class MainWeather(@SerializedName("temp") val temperature: Float,
                         val pressure: Float,
                         val humidity: Float)

  data class WeatherDescription(val id: Int, val description: String)

  //ignore individual phenomena
  class Wind
  class Clouds
  class Rain
  class Snow

  // ignore city and coord and more

}
