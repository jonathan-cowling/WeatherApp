package tk.jonathancowling.weatherapp.config

import tk.jonathancowling.weatherapp.data.TemperatureUnit
import org.threeten.bp.Duration
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.TemporalAmount
import javax.inject.Inject

class DefaultConfig @Inject constructor() {
  val temperatureUnitToSwitchTo: TemperatureUnit = TemperatureUnit.FAHRENHEIT
  val getWeatherThrottle: TemporalAmount = Duration.of(1, ChronoUnit.MINUTES)
}

