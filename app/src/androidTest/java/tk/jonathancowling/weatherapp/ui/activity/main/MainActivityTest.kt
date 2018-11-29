package tk.jonathancowling.weatherapp.ui.activity.main

import android.content.Context
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.widget.ImageView
import tk.jonathancowling.keys.KeyManager
import tk.jonathancowling.weatherapp.R
import tk.jonathancowling.weatherapp.WeatherApplication
import tk.jonathancowling.weatherapp.business.WeatherService
import tk.jonathancowling.weatherapp.data.*
import tk.jonathancowling.weatherapp.di.ApplicationComponent
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.Single
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import javax.inject.Singleton

class MainActivityTest {

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
      .component = DaggerMainActivityTest_TestComponent.builder().testModule(testModule)
      .application(InstrumentationRegistry.getTargetContext().applicationContext).build()
    `when`(weatherService.getWeather(anyInt())).thenReturn(
      Single.just(Result.response(Response.success(
        Weather(0f, "", TemperatureUnit.KELVIN, WeatherCode.BROKEN_CLOUDS)
      )))
    )
    `when`(weatherService.getForecast(anyInt())).thenReturn(
      Single.just(Result.response(Response.success(
        Forecast(listOf(Weather(0f, "", TemperatureUnit.KELVIN, WeatherCode.BROKEN_CLOUDS)))
      )))
    )

    activityTestRule.launchActivity(Intent())
  }

  @Test
  fun setWeatherShouldDismissLoadingAndSetWeatherFragment() {
    activityTestRule.activity.setLoading()
    onView(withText("Please Wait")).check { it, _ -> if (it == null) fail("loading fragment not appeared") }

    activityTestRule.activity.setWeather(Weather(0f, "", TemperatureUnit.KELVIN, WeatherCode.BROKEN_CLOUDS),
      City(0, ""))
    onView(withText("Please Wait")).check { _, it -> if (it == null) fail("loading fragment isn't gone") }
    onView(withId(R.id.temperature_text)).check(ViewAssertions.matches(withText("0.0")))
    onView(withId(R.id.temperature_unit_text)).check(ViewAssertions.matches(withText("˚K")))
    onView(withId(R.id.weather_image)).check{ v, e ->
      if (e != null) { fail("view not found")}
      assertThat("drawable not correct", (v as ImageView).drawable.constantState,
        equalTo(activityTestRule.activity.resources.getDrawable(R.drawable.ic_cloudy, null)
          .constantState))
    }
  }

  @Test
  fun setErrorShouldShowRetry() {
    activityTestRule.activity.setError(RuntimeException())

    onView(withText(String.format(activityTestRule.activity.resources.getString(R.string.retry_template), "Manchester")))
      .check { it, _ -> if (it == null) fail("retry fragment not appeared") }
  }

  @Test
  fun setErrorShouldDismissLoadingAndShowRetry() {
    activityTestRule.activity.setLoading()
    onView(withText("Please Wait")).check { it, _ -> if (it == null) fail("loading fragment not appeared") }

    activityTestRule.activity.setError(RuntimeException())
    onView(withText(String.format(activityTestRule.activity.resources.getString(R.string.retry_template), "Manchester")))
      .check { it, _ -> if (it == null) fail("retry fragment not appeared") }
    onView(withText("Please Wait")).check { _, it -> if (it == null) fail("retry fragment isn't gone") }
  }

  @Test
  fun setLoadingShouldDismissRetry() {
    val retryText = String.format(activityTestRule.activity.resources.getString(R.string.retry_template), "Manchester")
    activityTestRule.activity.setError(RuntimeException())
    onView(withText(retryText))
      .check { it, _ -> if (it == null) fail("retry fragment not appeared") }

    activityTestRule.activity.setLoading()
    onView(withText("Please Wait")).check { it, _ -> if (it == null) fail("loading fragment not appeared") }
    onView(withText(retryText)).check { _, it -> if (it == null) fail("retry fragment isn't gone") }
  }

  @Test
  fun testChangingUnit() {
    val weather = Weather(0f, "desc", TemperatureUnit.CELSIUS, WeatherCode.BROKEN_CLOUDS)

    // not testing presenter so change listener to setWeather directly
    activityTestRule.activity.switchUnitListener = {
      activityTestRule.activity.setWeather(weather.changeUnit(TemperatureUnit.FAHRENHEIT), City(0, ""))
      activityTestRule.activity.setSwitchUnitMenuItem(TemperatureUnit.CELSIUS)
    }

    activityTestRule.activity.setWeather(weather, City(0, ""))

    onView(withId(R.id.temperature_text)).check(ViewAssertions.matches(withText("0.0")))
    onView(withId(R.id.temperature_unit_text)).check(ViewAssertions.matches(withText("˚C")))

    openActionBarOverflowOrOptionsMenu(activityTestRule.activity)
    onView(withText(String.format(activityTestRule.activity.getString(R.string.toolbar_menu_switch_unit_template, "F"))))
      .perform(click())

    onView(withId(R.id.temperature_text)).check(ViewAssertions.matches(withText("32.0")))
    onView(withId(R.id.temperature_unit_text)).check(ViewAssertions.matches(withText("˚F")))

    openActionBarOverflowOrOptionsMenu(activityTestRule.activity)
    onView(withText(String.format(activityTestRule.activity.getString(R.string.toolbar_menu_switch_unit_template, "C"))))
      .check(ViewAssertions.matches(isDisplayed()))
  }

}
