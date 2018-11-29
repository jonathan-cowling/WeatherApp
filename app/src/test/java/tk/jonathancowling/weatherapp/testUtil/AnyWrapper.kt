package tk.jonathancowling.weatherapp.testUtil

import org.mockito.Mockito

object AnyWrapper {
  fun <T> any(): T = Mockito.any<T>()
}
