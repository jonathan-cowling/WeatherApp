package tk.jonathancowling.weatherapp.androidTestUtil

import org.mockito.Mockito

object AnyWrapper {
  fun <T> any(): T = Mockito.any<T>()
}
