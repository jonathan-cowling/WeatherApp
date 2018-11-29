package tk.jonathancowling.weatherapp.data

import tk.jonathancowling.weatherapp.R

// https://openweathermap.org/weather-conditions
enum class WeatherCode(val code: Int, val icon: Int) {

  // Group 2xx: Thunderstorm
  THUNDERSTORM_WITH_LIGHT_RAIN(200, R.drawable.ic_thunder),
  THUNDERSTORM_WITH_RAIN(201, R.drawable.ic_thunder),
  THUNDERSTORM_WITH_HEAVY_RAIN(202, R.drawable.ic_thunder),
  LIGHT_THUNDERSTORM(210, R.drawable.ic_thunder),
  THUNDERSTORM(211, R.drawable.ic_thunder),
  HEAVY_THUNDERSTORM(212, R.drawable.ic_thunder),
  RAGGED_THUNDERSTORM(221, R.drawable.ic_thunder),
  THUNDERSTORM_WITH_LIGHT_DRIZZLE(230, R.drawable.ic_thunder),
  THUNDERSTORM_WITH_DRIZZLE(231, R.drawable.ic_thunder),
  THUNDERSTORM_WITH_HEAVY_DRIZZLE(232, R.drawable.ic_thunder),

  // Group 3xx: Drizzle
  LIGHT_INTENSITY_DRIZZLE(300, R.drawable.ic_rainy),
  DRIZZLE(301, R.drawable.ic_rainy),
  HEAVY_INTENSITY_DRIZZLE(302, R.drawable.ic_rainy),
  LIGHT_INTENSITY_DRIZZLE_RAIN(310, R.drawable.ic_rainy),
  DRIZZLE_RAIN(311, R.drawable.ic_rainy),
  HEAVY_INTENSITY_DRIZZLE_RAIN(312, R.drawable.ic_rainy),
  SHOWER_RAIN_AND_DRIZZLE(313, R.drawable.ic_rainy),
  HEAVY_SHOWER_RAIN_AND_DRIZZLE(314, R.drawable.ic_rainy),
  SHOWER_DRIZZLE(321, R.drawable.ic_rainy),

  // Group 5xx: Rain
  LIGHT_RAIN(500, R.drawable.ic_rainy),
  MODERATE_RAIN(501, R.drawable.ic_rainy),
  HEAVY_INTENSITY_RAIN(502, R.drawable.ic_rainy),
  VERY_HEAVY_RAIN(503, R.drawable.ic_rainy),
  EXTREME_RAIN(504, R.drawable.ic_rainy),
  FREEZING_RAIN(511, R.drawable.ic_rainy),
  LIGHT_INTENSITY_SHOWER_RAIN(520, R.drawable.ic_rainy),
  SHOWER_RAIN(521, R.drawable.ic_rainy),
  HEAVY_INTENSITY_SHOWER_RAIN(522, R.drawable.ic_rainy),
  RAGGED_SHOWER_RAIN(531, R.drawable.ic_rainy),

  // Group 6xx: Snow
  LIGHT_SNOW(600, R.drawable.ic_cloudy_snowy),
  SNOW(601, R.drawable.ic_cloudy_snowy),
  HEAVY_SNOW(602, R.drawable.ic_cloudy_snowy),
  SLEET(611, R.drawable.ic_cloudy_snowy),
  SHOWER_SLEET(612, R.drawable.ic_cloudy_snowy),
  LIGHT_RAIN_AND_SNOW(615, R.drawable.ic_cloudy_snowy),
  RAIN_AND_SNOW(616, R.drawable.ic_cloudy_snowy),
  LIGHT_SHOWER_SNOW(620, R.drawable.ic_cloudy_snowy),
  SHOWER_SNOW(621, R.drawable.ic_cloudy_snowy),
  HEAVY_SHOWER_SNOW(622, R.drawable.ic_cloudy_snowy),

  // Group 7xx: Atmosphere
  MIST(701, R.drawable.ic_cloudy_foggy),
  SMOKE(711, R.drawable.ic_warning),
  HAZE(721, R.drawable.ic_warning),
  SAND_DUST_WHIRLS(731, R.drawable.ic_warning),
  FOG(741, R.drawable.ic_cloudy_foggy),
  SAND(751, R.drawable.ic_warning),
  DUST(761, R.drawable.ic_warning),
  VOLCANIC_ASH(762, R.drawable.ic_warning),
  SQUALLS(771, R.drawable.ic_warning),
  TORNADO(781, R.drawable.ic_warning),

  // Group 800: Clear
  CLEAR_SKY(800, R.drawable.ic_sunny),

  // Group 80x: Clouds
  FEW_CLOUDS(801, R.drawable.ic_cloudy),
  SCATTERED_CLOUDS(802, R.drawable.ic_cloudy),
  BROKEN_CLOUDS(803, R.drawable.ic_cloudy),
  OVERCAST_CLOUDS(804, R.drawable.ic_cloudy)
}
