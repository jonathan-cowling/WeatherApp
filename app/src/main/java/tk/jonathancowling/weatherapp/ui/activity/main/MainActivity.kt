package tk.jonathancowling.weatherapp.ui.activity.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import tk.jonathancowling.weatherapp.R
import tk.jonathancowling.weatherapp.WeatherApplication
import tk.jonathancowling.weatherapp.business.LocationService
import tk.jonathancowling.weatherapp.business.WeatherService
import tk.jonathancowling.weatherapp.config.DefaultConfig
import tk.jonathancowling.weatherapp.data.City
import tk.jonathancowling.weatherapp.data.Forecast
import tk.jonathancowling.weatherapp.data.TemperatureUnit
import tk.jonathancowling.weatherapp.data.Weather
import tk.jonathancowling.weatherapp.di.MainActivityComponent
import tk.jonathancowling.weatherapp.ui.activity.help.HelpActivity
import tk.jonathancowling.weatherapp.ui.fragment.LoadingFragment
import tk.jonathancowling.weatherapp.ui.fragment.ObservableFragment
import tk.jonathancowling.weatherapp.ui.fragment.RetryFragment
import tk.jonathancowling.weatherapp.ui.fragment.WeatherFragment
import tk.jonathancowling.weatherapp.util.LifeCycleListener
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract, ObservableFragment.OnFragmentInteractionListener {

  override fun weatherAlreadyUpToDate() {
    Snackbar.make(window.decorView.rootView.findViewById(android.R.id.content),
      R.string.weather_up_to_date, Snackbar.LENGTH_SHORT).show()
  }

  override fun onFragmentInteraction(msg: ObservableFragment.Message) {
    when (msg) {
      is RetryFragment.RetryFragmentMessage -> {
        getWeatherListener()
      }
    }
  }

  lateinit var component: MainActivityComponent
  @Inject
  lateinit var weatherService: WeatherService
  @Inject
  lateinit var locationService: LocationService
  @Inject
  lateinit var defaults: DefaultConfig

  override lateinit var lifecycleListener: LifeCycleListener // presenter init
  override lateinit var getWeatherListener: () -> Unit // presenterInit
  override lateinit var switchUnitListener: () -> Unit //presenterInit

  var switchUnitMenuItem: MenuItem? = null

  override fun setSwitchUnitMenuItem(unit: TemperatureUnit) {
    switchUnitMenuItem?.let {
      val unitText = when (unit) {
        TemperatureUnit.KELVIN -> "K"
        TemperatureUnit.CELSIUS -> "C"
        TemperatureUnit.FAHRENHEIT -> "F"
      }

      it.title =
        String.format(getString(R.string.toolbar_menu_switch_unit_template, unitText))
    }
  }


  override fun setError(e: Throwable) {
    supportFragmentManager.findFragmentByTag(LOADING_FRAGMENT_TAG)?.let {
      (it as LoadingFragment).dismiss()
    }
    supportFragmentManager.findFragmentByTag(ERROR_FRAGMENT_TAG)?:let {
      RetryFragment().show(supportFragmentManager, ERROR_FRAGMENT_TAG)
    }
  }

  override fun setLoading() {
    supportFragmentManager.findFragmentByTag(ERROR_FRAGMENT_TAG)?.let {
      (it as RetryFragment).dismiss()
    }
    supportFragmentManager.findFragmentByTag(LOADING_FRAGMENT_TAG)?:let {
      LoadingFragment().show(supportFragmentManager, LOADING_FRAGMENT_TAG)
    }
  }

  override fun setWeather(weather: Weather, city: City) {
    val wFrag = WeatherFragment()
    wFrag.setState(weather, city)
    supportFragmentManager.findFragmentByTag(LOADING_FRAGMENT_TAG)?.let {
      (it as LoadingFragment).dismiss()
    }
    supportFragmentManager.beginTransaction()
      .replace(R.id.weather_fragment_container, wFrag, WEATHER_FRAGMENT_TAG)
      .commit()

  }

  override fun setWeather(weather: Weather, city: City, forecast: Forecast) {
    val wFrag = WeatherFragment()
    wFrag.setState(weather, city, forecast)
    supportFragmentManager.findFragmentByTag(LOADING_FRAGMENT_TAG)?.let {
      (it as LoadingFragment).dismiss()
    }
    supportFragmentManager.beginTransaction()
      .replace(R.id.weather_fragment_container, wFrag, WEATHER_FRAGMENT_TAG)
      .commit()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)
    component = (application as WeatherApplication).component
      .activityComponentBuilder()
      .resources(resources)
      .build()

    component.inject(this)
    MainPresenter(this, resources, MainInteractor(weatherService, locationService), defaults)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.toolbar_menu, menu)
    switchUnitMenuItem = menu?.findItem(R.id.toolbar_menu_switch_unit)
    setSwitchUnitMenuItem(defaults.temperatureUnitToSwitchTo)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.toolbar_menu_help -> {
        startActivity(Intent(this, HelpActivity::class.java))
        return true
      }
      R.id.toolbar_menu_refresh -> {
        getWeatherListener()
        return true
      }
      R.id.toolbar_menu_switch_unit -> {
        switchUnitListener()
        return true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onPause() {
    super.onPause()
    supportFragmentManager.findFragmentByTag(ERROR_FRAGMENT_TAG)?.let {
      supportFragmentManager.beginTransaction().remove(it).commit()
    }
    lifecycleListener.onPause()
  }

  override fun onResume() {
    super.onResume()
    lifecycleListener.onResume()
  }

  companion object {
    const val LOADING_FRAGMENT_TAG = "loading_fragment"
    const val ERROR_FRAGMENT_TAG = "error_fragment"
    const val WEATHER_FRAGMENT_TAG = "weather_fragment"
  }
}
