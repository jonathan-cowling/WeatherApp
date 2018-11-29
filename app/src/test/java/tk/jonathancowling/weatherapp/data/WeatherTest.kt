package tk.jonathancowling.weatherapp.data

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test

import org.junit.Assert.*

class WeatherTest {

  @Test
  fun changeUnitK2C() {
    assertThat("Change unit not accurate", Weather(0f, "", TemperatureUnit.KELVIN, WeatherCode.BROKEN_CLOUDS)
      .changeUnit(TemperatureUnit.CELSIUS),
      equalTo(Weather(-273.15f, "", TemperatureUnit.CELSIUS, WeatherCode.BROKEN_CLOUDS)))
  }

  @Test
  fun changeUnitK2F() {
    assertThat("Change unit not accurate", Weather(0f, "", TemperatureUnit.KELVIN, WeatherCode.BROKEN_CLOUDS)
      .changeUnit(TemperatureUnit.FAHRENHEIT),
      equalTo(Weather(-459.67f, "", TemperatureUnit.FAHRENHEIT, WeatherCode.BROKEN_CLOUDS)))
  }

  @Test
  fun changeUnitC2F() {
    assertThat("Change unit not accurate", Weather(0f, "", TemperatureUnit.CELSIUS, WeatherCode.BROKEN_CLOUDS)
      .changeUnit(TemperatureUnit.FAHRENHEIT),
      equalTo(Weather(31.99997f, "", TemperatureUnit.FAHRENHEIT, WeatherCode.BROKEN_CLOUDS)))
  }

  @Test
  fun changeUnitF2C() {
    assertThat("Change unit not accurate", Weather(0f, "", TemperatureUnit.FAHRENHEIT, WeatherCode.BROKEN_CLOUDS)
      .changeUnit(TemperatureUnit.CELSIUS),
      equalTo(Weather(-17.777756f, "", TemperatureUnit.CELSIUS, WeatherCode.BROKEN_CLOUDS)))
  }

  @Test
  fun changeUnitCeqF() {
    assertThat("Change unit not accurate", Weather(-40f, "", TemperatureUnit.CELSIUS, WeatherCode.BROKEN_CLOUDS)
      .changeUnit(TemperatureUnit.FAHRENHEIT),
      equalTo(Weather(-40.00003f, "", TemperatureUnit.FAHRENHEIT, WeatherCode.BROKEN_CLOUDS)))
  }

  @Test
  fun fromResponse() {
    val response = CurrentWeatherApiResponse(200,
      mutableListOf(CurrentWeatherApiResponse.WeatherDescription("main", "desc", WeatherCode.BROKEN_CLOUDS.code)),
      CurrentWeatherApiResponse.CoOrdinate(0f, 0f),
      CurrentWeatherApiResponse.MainWeather(0f, 0f, 0f),
      CurrentWeatherApiResponse.Wind(),
      CurrentWeatherApiResponse.Clouds(),
      CurrentWeatherApiResponse.Rain(),
      CurrentWeatherApiResponse.Snow(),
      0.0)
    assertThat("Weather not as expected from response", Weather.fromResponse(response),
      equalTo(Weather(0f, "desc", TemperatureUnit.KELVIN, WeatherCode.BROKEN_CLOUDS)))
  }

  @Test
  fun fromForecastResponse() {
    val response = ForecastWeatherApiResponse.Weather(
      0,
      ForecastWeatherApiResponse.MainWeather(0f, 0f, 0f),
      listOf(ForecastWeatherApiResponse.WeatherDescription(800, "desc")),
      ForecastWeatherApiResponse.Wind(),
      ForecastWeatherApiResponse.Clouds(),
      ForecastWeatherApiResponse.Rain(),
      ForecastWeatherApiResponse.Snow()
    )

    assertThat("Weather not as expected from forecast response", Weather.fromResponse(response),
      equalTo(Weather(0f, "desc", TemperatureUnit.KELVIN, WeatherCode.CLEAR_SKY)))
  }
}
