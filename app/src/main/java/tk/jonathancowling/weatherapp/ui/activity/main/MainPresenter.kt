package tk.jonathancowling.weatherapp.ui.activity.main

import android.content.res.Resources
import tk.jonathancowling.weatherapp.config.DefaultConfig
import tk.jonathancowling.weatherapp.data.City
import tk.jonathancowling.weatherapp.data.Forecast
import tk.jonathancowling.weatherapp.data.TemperatureUnit
import tk.jonathancowling.weatherapp.util.LifeCycleListener
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import org.threeten.bp.Instant
import retrofit2.adapter.rxjava2.Result
import java.io.IOException

class MainPresenter(private val contract: MainContract,
                    private val resources: Resources,
                    private val mainInteractor: MainInteractor,
                    private val defaults: DefaultConfig) {

  private var model = MainModel(city = City(3333169, "Manchester"))
  private val disposables: MutableList<Disposable> = mutableListOf()

  private fun shouldUpdateWeather() = (model.weather == null)
    || (model.lastUpdate == null)
    || Instant.now().isAfter(model.lastUpdate!!.plus(defaults.getWeatherThrottle))

  init {
    contract.getWeatherListener = {
      if (shouldUpdateWeather()) {
        getWeather()
      } else {
        contract.weatherAlreadyUpToDate()
      }
    }
    contract.switchUnitListener = {
      var newSwitchUnitMenuItem = defaults.temperatureUnitToSwitchTo

      model.weather = model.weather!!.changeUnit(model.weather!!.unit.let {
        if (it == TemperatureUnit.CELSIUS) {
          newSwitchUnitMenuItem = TemperatureUnit.CELSIUS
          return@let TemperatureUnit.FAHRENHEIT
        } else { // the user doesn't get kelvin
          newSwitchUnitMenuItem = TemperatureUnit.FAHRENHEIT
          return@let TemperatureUnit.CELSIUS
        }
      })
      model.forecast = Forecast(model.forecast!!.weatherList.map {
        it.changeUnit(it.unit.let {
          if (it == TemperatureUnit.CELSIUS) {
            return@let TemperatureUnit.FAHRENHEIT
          } else { // the user doesn't get kelvin
            return@let TemperatureUnit.CELSIUS
          }
        })
      })

      contract.setWeather(model.weather!!, model.city!!, model.forecast!!)
      contract.setSwitchUnitMenuItem(newSwitchUnitMenuItem)
    }
    contract.lifecycleListener = object : LifeCycleListener {
      override fun onCreate() {}
      override fun onPostCreate() {}
      override fun onPause() {
        disposables.forEach {
          if (!it.isDisposed) {
            it.dispose()
          }
        }
      }

      override fun onResume() {
        if (shouldUpdateWeather()) {
          getWeather()
        } else {
          contract.setWeather(model.weather!!, model.city!!)
        }
      }

      override fun onDestroy() {}
    }
  }

  fun getWeather() {
    contract.setLoading()
    mainInteractor.getWeather(model.city!!.id)
      .flatMap {
        if (!it.response()!!.isSuccessful) {
          throw IOException(it.response()!!.errorBody().toString())
        }
        model.lastUpdate = org.threeten.bp.Instant.now()
        model.weather = it.response()!!.body()!!.changeUnit(TemperatureUnit.CELSIUS)
        contract.setWeather(model.weather!!, model.city!!)
        mainInteractor.getForecast(model.city!!.id)
      }
      .subscribe(object : SingleObserver<Result<Forecast>> {

        override fun onSubscribe(d: Disposable) {
          disposables.add(d)
        }

        override fun onSuccess(t: Result<Forecast>) {
          if (!t.response()!!.isSuccessful) {
            onError(IOException(t.response()!!.errorBody().toString()))
          }
          model.lastUpdate = org.threeten.bp.Instant.now()
          model.forecast =
            Forecast(t.response()!!.body()!!.weatherList.map { it.changeUnit(TemperatureUnit.CELSIUS) })
          contract.setWeather(model.weather!!, model.city!!, model.forecast!!)
        }

        override fun onError(e: Throwable) {
          contract.setError(e)
        }

      })
  }
}
