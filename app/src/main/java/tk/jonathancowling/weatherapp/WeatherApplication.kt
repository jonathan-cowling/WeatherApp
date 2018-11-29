package tk.jonathancowling.weatherapp

import android.app.Application
import tk.jonathancowling.weatherapp.di.ApplicationComponent
import tk.jonathancowling.weatherapp.di.DaggerApplicationComponent

class WeatherApplication : Application() {
  var component: ApplicationComponent = DaggerApplicationComponent.builder().application(this).build()
}
