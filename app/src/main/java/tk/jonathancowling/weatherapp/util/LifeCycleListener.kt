package tk.jonathancowling.weatherapp.util

interface LifeCycleListener {

  fun onCreate()
  fun onPostCreate()
  fun onPause()
  fun onResume()
  fun onDestroy()
}
