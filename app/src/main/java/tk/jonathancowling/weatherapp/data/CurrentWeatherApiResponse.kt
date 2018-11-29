package tk.jonathancowling.weatherapp.data

import com.google.gson.annotations.SerializedName

data class CurrentWeatherApiResponse(
  @SerializedName("cod") val code: Int,
  @SerializedName("weather") val weatherDescription: List<WeatherDescription>,
  @SerializedName("coord") val coOrdinate: CoOrdinate,
  @SerializedName("main") val mainWeather: MainWeather,
  val wind: Wind,
  val clouds: Clouds,
  val rain: Rain,
  val snow: Snow,
  val dt: Double
  // ignore: base, id, sys, name
  ) {

  data class CoOrdinate (
    @SerializedName("lat") var lattitude: Float,
    @SerializedName("lon") var longitude: Float
  )

  data class MainWeather (
    @SerializedName("temp") val temperature: Float,
    var humidity: Float,
    var pressure: Float
    // ignore: temp_min, temp_max, sea_level, grnd_level
  )

  data class WeatherDescription (
    val main: String,
    val description: String,
    val id: Int
  )

  //ignore individual phenomena
  class Wind
  class Clouds
  class Rain
  class Snow
}
