package tk.jonathancowling.weatherapp.ui.activity.main

import android.content.res.Resources
import tk.jonathancowling.weatherapp.config.DefaultConfig
import tk.jonathancowling.weatherapp.data.TemperatureUnit
import tk.jonathancowling.weatherapp.data.Weather
import tk.jonathancowling.weatherapp.data.WeatherCode
import tk.jonathancowling.weatherapp.testUtil.AnyWrapper
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

  lateinit var presenter: MainPresenter

  @Mock
  lateinit var contract: MainContract

  @Mock
  lateinit var resources: Resources

  @Mock
  lateinit var interactor: MainInteractor

  @Before
  fun setup() {
    presenter = MainPresenter(contract, resources, interactor, DefaultConfig())
    reset(contract, resources, interactor)
  }

  @Test
  fun init() {
    MainPresenter(contract, resources, interactor, DefaultConfig())

    verifyZeroInteractions(resources, interactor)
    verify(contract, times(1)).switchUnitListener = AnyWrapper.any()
    verify(contract, times(1)).getWeatherListener = AnyWrapper.any()
    verify(contract, times(1)).lifecycleListener = AnyWrapper.any()
  }

  @Test
  fun presenterSendsRequestSuccess() {
    val id = 3333169
    `when`(interactor.getWeather(id)).thenReturn(Single.just(Result.response(Response.success(
      Weather(0f, "", TemperatureUnit.KELVIN, WeatherCode.BROKEN_CLOUDS)))))
    presenter.getWeather()

    verify(interactor, times(1)).getWeather(id)
    //verify() // TODO: contract set weather and unit
  }

  @Test
  fun presenterSendsRequestFailure() {
    val id = 3333169
    val exception = RuntimeException("TESTING ERROR")
    `when`(interactor.getWeather(id)).thenReturn(Single.error(exception))
    presenter.getWeather()

    verify(interactor, times(1)).getWeather(id)
    verify(contract, times(1)).setError(exception)
  }


}
