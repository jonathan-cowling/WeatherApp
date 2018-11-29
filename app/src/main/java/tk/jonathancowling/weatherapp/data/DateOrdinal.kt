package tk.jonathancowling.weatherapp.data

object DateOrdinal {
  private val values = mapOf(
    0 to "th",
    1 to "st",
    2 to "nd",
    3 to "rd",
    4 to "th",
    5 to "th",
    6 to "th",
    7 to "th",
    8 to "th",
    9 to "th",
    10 to "th",
    11 to "th",
    12 to "th",
    13 to "th",
    14 to "th",
    15 to "th",
    16 to "th",
    17 to "th",
    18 to "th",
    19 to "th",
    20 to "th",
    21 to "st",
    22 to "nd",
    23 to "rd",
    24 to "th",
    25 to "th",
    26 to "th",
    27 to "th",
    28 to "th",
    29 to "th",
    30 to "th",
    31 to "st"
  )
  fun get(cardinal: Int): String = values[cardinal]
    ?: throw IllegalArgumentException("date ordinals must be between 1 and 31 inclusive")
}
