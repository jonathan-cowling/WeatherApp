package tk.jonathancowling.weatherapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import tk.jonathancowling.keys.KeyManager
import tk.jonathancowling.weatherapp.R
import tk.jonathancowling.weatherapp.WeatherApplication
import tk.jonathancowling.weatherapp.business.WeatherService
import tk.jonathancowling.weatherapp.data.*
import tk.jonathancowling.weatherapp.di.ApplicationComponent
import tk.jonathancowling.weatherapp.ui.activity.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import javax.inject.Singleton

class WeatherFragmentTest {

  val weatherService: WeatherService = Mockito.mock(WeatherService::class.java)
  val testModule = TestModule()

  @Singleton
  @Component(modules = [TestModule::class])
  interface TestComponent : ApplicationComponent {
    @Component.Builder
    interface Builder {
      @BindsInstance
      fun application(applicationContext: Context): Builder

      fun testModule(module: TestModule): Builder
      fun build(): ApplicationComponent
    }
  }

  @Module
  inner class TestModule {
    @Provides
    fun provideKeyManager() = KeyManager()

    @Provides
    fun provideWeatherService() = weatherService
  }


  @JvmField
  @Rule
  val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java, false, false)

  @Before
  fun setup() {
    Mockito.reset(weatherService)
    (InstrumentationRegistry.getTargetContext().applicationContext as WeatherApplication)
      .component = DaggerWeatherFragmentTest_TestComponent.builder().testModule(testModule)
      .application(InstrumentationRegistry.getTargetContext().applicationContext).build()
    Mockito.`when`(weatherService.getWeather(3333169))
      .thenReturn(Single.just(Result.response(Response.success(Weather(0f, "it's manchester, it's always raining!",
        TemperatureUnit.KELVIN, WeatherCode.BROKEN_CLOUDS)))))
    Mockito.`when`(weatherService.getForecast(ArgumentMatchers.anyInt())).thenReturn(
      Single.just(Result.response(Response.success(
        Forecast(listOf(Weather(0f, "", TemperatureUnit.KELVIN, WeatherCode.BROKEN_CLOUDS)))
      )))
    )

    activityTestRule.launchActivity(Intent())
  }

  @Test
  fun setWeather() {
    activityTestRule.activity.setWeather(
      Weather(21f,
        "it's manchester, it's always raining!",
        TemperatureUnit.CELSIUS, WeatherCode.BROKEN_CLOUDS), City(0, ""))

    onView(withId(R.id.temperature_unit_text)).check(matches(withText("˚C")))
    onView(withId(R.id.temperature_text)).check(matches(withText("21.0")))
    onView(withId(R.id.weather_description_text)).check(matches(withText("it's manchester, it's always raining!")))
  }
}
