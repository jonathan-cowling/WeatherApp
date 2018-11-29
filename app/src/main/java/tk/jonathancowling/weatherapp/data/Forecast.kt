package tk.jonathancowling.weatherapp.data

import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.temporal.ChronoUnit

data class Forecast(
  val weatherList: List<Weather>
){

  data class WeatherAndTime(val weather: Weather, val time: Instant) {
    companion object {
      fun aggregate(list: List<WeatherAndTime>): WeatherAndTime {
          list.forEach {
            if (LocalDateTime.ofInstant(it.time, ZoneId.of("UTC")).hour == 12) {
              return it
            }
          }
        return list.firstOrNull()?: throw IllegalArgumentException("list shouldn't be empty")
      }
    }
  }

  companion object {
    fun fromResponse(response: ForecastWeatherApiResponse): Forecast {
      return Forecast(response.weatherList
        .map { WeatherAndTime(Weather.fromResponse(it), Instant.ofEpochSecond(it.timestamp)) }
        .groupBy { Duration.between(Instant.EPOCH, it.time.truncatedTo(ChronoUnit.DAYS)).toDays() }
        .map { it.value }
        .map { WeatherAndTime.aggregate(it) }
        .map { it.weather }
      )
    }
  }
}
