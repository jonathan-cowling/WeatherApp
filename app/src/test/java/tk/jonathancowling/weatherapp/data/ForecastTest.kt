package tk.jonathancowling.weatherapp.data

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.fail
import org.junit.Test
import org.threeten.bp.*

class ForecastTest {

  @Test
  fun fromEmptyResponse() {
    val response = ForecastWeatherApiResponse(listOf())

    assertThat("forecast is not correct", Forecast.fromResponse(response),
      equalTo(Forecast(listOf())))
  }

  @Test
  fun fromResponse() {
    val response = ForecastWeatherApiResponse(listOf(ForecastWeatherApiResponse.Weather(
      LocalDateTime.of(2018, 1, 1, 12, 0).toEpochSecond(ZoneOffset.UTC),
      ForecastWeatherApiResponse.MainWeather(0f, 0f, 0f),
      listOf(ForecastWeatherApiResponse.WeatherDescription(800, "desc")),
      ForecastWeatherApiResponse.Wind(),
      ForecastWeatherApiResponse.Clouds(),
      ForecastWeatherApiResponse.Rain(),
      ForecastWeatherApiResponse.Snow()
    )))

    assertThat("forecast is not correct", Forecast.fromResponse(response),
      equalTo(Forecast(listOf(Weather(0f, "desc", TemperatureUnit.KELVIN, WeatherCode.CLEAR_SKY)))))
  }

  @Test
  fun dateAggregationOfEmptyShouldFail() {
    try {
      Forecast.WeatherAndTime.aggregate(listOf())
      fail("Weather Aggregation failed")
    } catch (e: IllegalArgumentException) {}
  }

  @Test
  fun dateAggregationShouldTakeMidday() {
    val expected = Forecast.WeatherAndTime(Weather(0f, "", TemperatureUnit.KELVIN, WeatherCode.CLEAR_SKY),
      Instant.ofEpochSecond(LocalDateTime.of(2018, 1, 1, 12, 59).toEpochSecond(ZoneOffset.UTC)))

    assertThat("didn't take weather at 12", Forecast.WeatherAndTime.aggregate(listOf(
      Forecast.WeatherAndTime(Weather(0f, "", TemperatureUnit.KELVIN, WeatherCode.BROKEN_CLOUDS),
        Instant.ofEpochSecond(LocalDateTime.of(2018, 1, 1, 11, 59).toEpochSecond(ZoneOffset.UTC))),
      expected,
      Forecast.WeatherAndTime(Weather(0f, "", TemperatureUnit.KELVIN, WeatherCode.LIGHT_RAIN),
        Instant.ofEpochSecond(LocalDateTime.of(2018, 1, 1, 13, 0).toEpochSecond(ZoneOffset.UTC)))
    )), `is`(expected))
  }

  @Test
  fun dateAggregationShouldTakeFirstMatch() {
    val expected = Forecast.WeatherAndTime(Weather(0f, "", TemperatureUnit.KELVIN, WeatherCode.CLEAR_SKY),
      Instant.ofEpochSecond(LocalDateTime.of(2018, 1, 1, 12, 59).toEpochSecond(ZoneOffset.UTC)))

    assertThat("didn't take first weather at 12", Forecast.WeatherAndTime.aggregate(listOf(
      Forecast.WeatherAndTime(Weather(0f, "", TemperatureUnit.KELVIN, WeatherCode.BROKEN_CLOUDS),
        Instant.ofEpochSecond(LocalDateTime.of(2018, 1, 1, 1, 0).toEpochSecond(ZoneOffset.UTC))),
      expected,
      Forecast.WeatherAndTime(Weather(0f, "", TemperatureUnit.KELVIN, WeatherCode.LIGHT_RAIN),
        Instant.ofEpochSecond(LocalDateTime.of(2018, 1, 1, 12, 0).toEpochSecond(ZoneOffset.UTC)))
    )), `is`(expected))
  }
}
