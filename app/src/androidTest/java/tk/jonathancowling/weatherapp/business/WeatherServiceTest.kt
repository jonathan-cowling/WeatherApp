package tk.jonathancowling.weatherapp.business

import tk.jonathancowling.keys.KeyManager
import tk.jonathancowling.weatherapp.data.CurrentWeatherApiResponse
import tk.jonathancowling.weatherapp.data.Forecast
import tk.jonathancowling.weatherapp.data.ForecastWeatherApiResponse
import tk.jonathancowling.weatherapp.data.Weather
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.Result
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

@RunWith(MockitoJUnitRunner::class)
class WeatherServiceTest {

  @Mock
  lateinit var manager: ServicesManager

  @Mock
  lateinit var keys: KeyManager

  @Test
  fun getWeatherSuccess() {
    val expectedResponse = CurrentWeatherApiResponse(0,
      mutableListOf(CurrentWeatherApiResponse.WeatherDescription("main", "desc", 800)),
      CurrentWeatherApiResponse.CoOrdinate(0f, 0f),
      CurrentWeatherApiResponse.MainWeather(0f, 0f, 0f),
      CurrentWeatherApiResponse.Wind(),
      CurrentWeatherApiResponse.Clouds(),
      CurrentWeatherApiResponse.Rain(),
      CurrentWeatherApiResponse.Snow(),
      0.0)

    val retrofitBuilder = Retrofit.Builder()
      .addConverterFactory(object : Converter.Factory() {
        override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
          return Converter<ResponseBody, CurrentWeatherApiResponse> { expectedResponse }
        }
      })
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(OkHttpClient.Builder()
        .addInterceptor {
          Response.Builder()
            .code(200)
            .request(it.request())
            .protocol(Protocol.HTTP_1_0)
            .message("message")
            .body(ResponseBody.create(MediaType.parse("application.json"), ";ekw"))
            .build()
        }
        .build()
      )

    val currentThread = Schedulers.from { it.run() }

    `when`(manager.keys).thenReturn(keys)
    `when`(manager.defaultRetrofitBuilder()).thenReturn(retrofitBuilder)
    `when`(manager.weatherServiceConfig).thenReturn(
      ServicesManager.WeatherServiceConfig("https://localhost:9999", currentThread, currentThread))
    WeatherService(manager).getWeather(0).subscribe(object : SingleObserver<Result<Weather>> {
      override fun onSuccess(t: Result<Weather>) {
        assertThat("null response", t.response(), notNullValue())
        assertThat("null body", t.response()!!.body(), notNullValue())
        assertThat("weather not as expected", t.response()!!.body()!!,
          equalTo(Weather.fromResponse(expectedResponse)))
      }

      override fun onSubscribe(d: Disposable) {}
      override fun onError(e: Throwable) {
        fail("error in response " + e.toString())
      }
    })
  }

  @Test
  fun getWeatherError() {
    // response has invalid weatherDescription.id
    val expectedResponse = CurrentWeatherApiResponse(0,
      mutableListOf(CurrentWeatherApiResponse.WeatherDescription("main", "desc", 0)),
      CurrentWeatherApiResponse.CoOrdinate(0f, 0f),
      CurrentWeatherApiResponse.MainWeather(0f, 0f, 0f),
      CurrentWeatherApiResponse.Wind(),
      CurrentWeatherApiResponse.Clouds(),
      CurrentWeatherApiResponse.Rain(),
      CurrentWeatherApiResponse.Snow(),
      0.0)

    val retrofitBuilder = Retrofit.Builder()
      .addConverterFactory(object : Converter.Factory() {
        override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
          return Converter<ResponseBody, CurrentWeatherApiResponse> { expectedResponse }
        }
      })
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(OkHttpClient.Builder()
        .addInterceptor {
          Response.Builder()
            .code(200)
            .request(it.request())
            .protocol(Protocol.HTTP_1_0)
            .message("message")
            .body(ResponseBody.create(MediaType.parse("application.json"), ";ekw"))
            .build()
        }
        .build()
      )

    val currentThread = Schedulers.from { it.run() }

    `when`(manager.keys).thenReturn(keys)
    `when`(manager.defaultRetrofitBuilder()).thenReturn(retrofitBuilder)
    `when`(manager.weatherServiceConfig).thenReturn(
      ServicesManager.WeatherServiceConfig("https://localhost:9999", currentThread, currentThread))
    WeatherService(manager).getWeather(0).subscribe(object : SingleObserver<Result<Weather>> {
      override fun onSuccess(t: Result<Weather>) {
        fail("response succeeded unexpectedly")
      }

      override fun onSubscribe(d: Disposable) {}
      override fun onError(e: Throwable) {
        if (e !is IllegalArgumentException) {
          fail(e.toString())
        }
        assertThat("message not as expected", e.message, equalTo("weatherDescription code must be a valid WeatherCode.code"))
      }
    })
  }

  @Test
  fun getWeatherFailed() {
    val expectedResponse = CurrentWeatherApiResponse(0,
      mutableListOf(CurrentWeatherApiResponse.WeatherDescription("main", "desc", 800)),
      CurrentWeatherApiResponse.CoOrdinate(0f, 0f),
      CurrentWeatherApiResponse.MainWeather(0f, 0f, 0f),
      CurrentWeatherApiResponse.Wind(),
      CurrentWeatherApiResponse.Clouds(),
      CurrentWeatherApiResponse.Rain(),
      CurrentWeatherApiResponse.Snow(),
      0.0)

    val retrofitBuilder = Retrofit.Builder()
      .addConverterFactory(object : Converter.Factory() {
        override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
          return Converter<ResponseBody, CurrentWeatherApiResponse> { expectedResponse }
        }
      })
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(OkHttpClient.Builder()
        .addInterceptor {
          Response.Builder()
            .code(500)
            .request(it.request())
            .protocol(Protocol.HTTP_1_0)
            .message("Internal Server Error")
            .body(ResponseBody.create(MediaType.parse("application.json"), ";ekw"))
            .build()
        }
        .build()
      )

    val currentThread = Schedulers.from { it.run() }

    `when`(manager.keys).thenReturn(keys)
    `when`(manager.defaultRetrofitBuilder()).thenReturn(retrofitBuilder)
    `when`(manager.weatherServiceConfig).thenReturn(
      ServicesManager.WeatherServiceConfig("https://localhost:9999", currentThread, currentThread))
    WeatherService(manager).getWeather(0).subscribe(object : SingleObserver<Result<Weather>> {
      override fun onSuccess(t: Result<Weather>) {
        assertThat("response is null", t.response(), notNullValue())
        assertThat("error code isn't correct", t.response()!!.code(), equalTo(500))
      }

      override fun onSubscribe(d: Disposable) {}
      override fun onError(e: Throwable) {
        fail(e.toString())
      }
    })
  }

  @Test
  fun getForecastSuccess() {
    val expectedResponse = ForecastWeatherApiResponse(listOf(ForecastWeatherApiResponse.Weather(
      LocalDateTime.of(2018, 1, 1, 12, 0).toEpochSecond(ZoneOffset.UTC),
      ForecastWeatherApiResponse.MainWeather(0f, 0f, 0f),
      listOf(ForecastWeatherApiResponse.WeatherDescription(800, "desc")),
      ForecastWeatherApiResponse.Wind(),
      ForecastWeatherApiResponse.Clouds(),
      ForecastWeatherApiResponse.Rain(),
      ForecastWeatherApiResponse.Snow()
    )))

    val retrofitBuilder = Retrofit.Builder()
      .addConverterFactory(object : Converter.Factory() {
        override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
          return Converter<ResponseBody, ForecastWeatherApiResponse> { expectedResponse }
        }
      })
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(OkHttpClient.Builder()
        .addInterceptor {
          Response.Builder()
            .code(200)
            .request(it.request())
            .protocol(Protocol.HTTP_1_0)
            .message("ok")
            .body(ResponseBody.create(MediaType.parse("application.json"), ""))
            .build()
        }
        .build()
      )

    val currentThread = Schedulers.from { it.run() }

    `when`(manager.keys).thenReturn(keys)
    `when`(manager.defaultRetrofitBuilder()).thenReturn(retrofitBuilder)
    `when`(manager.weatherServiceConfig).thenReturn(
      ServicesManager.WeatherServiceConfig("https://localhost:9999", currentThread, currentThread))

    WeatherService(manager).getForecast(0).subscribe(object: SingleObserver<Result<Forecast>> {
      override fun onSuccess(t: Result<Forecast>) {
        assertThat("null response", t.response(), notNullValue())
        assertThat("null body", t.response()!!.body(), notNullValue())
        assertThat("weather not as expected", t.response()!!.body()!!,
          equalTo(Forecast.fromResponse(expectedResponse)))
      }

      override fun onSubscribe(d: Disposable) {}
      override fun onError(e: Throwable) { fail("request error " + e.toString()) }

    })
  }

  @Test
  fun getForecastError() {
    val expectedResponse = ForecastWeatherApiResponse(listOf(ForecastWeatherApiResponse.Weather(
      LocalDateTime.of(2018, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC),
      ForecastWeatherApiResponse.MainWeather(0f, 0f, 0f),
      listOf(ForecastWeatherApiResponse.WeatherDescription(0, "desc")), // one of the weathers is invalid
      ForecastWeatherApiResponse.Wind(),
      ForecastWeatherApiResponse.Clouds(),
      ForecastWeatherApiResponse.Rain(),
      ForecastWeatherApiResponse.Snow()
    )))

    val retrofitBuilder = Retrofit.Builder()
      .addConverterFactory(object : Converter.Factory() {
        override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
          return Converter<ResponseBody, ForecastWeatherApiResponse> { expectedResponse }
        }
      })
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(OkHttpClient.Builder()
        .addInterceptor {
          Response.Builder()
            .code(200)
            .request(it.request())
            .protocol(Protocol.HTTP_1_0)
            .message("ok")
            .body(ResponseBody.create(MediaType.parse("application.json"), ""))
            .build()
        }
        .build()
      )

    val currentThread = Schedulers.from { it.run() }

    `when`(manager.keys).thenReturn(keys)
    `when`(manager.defaultRetrofitBuilder()).thenReturn(retrofitBuilder)
    `when`(manager.weatherServiceConfig).thenReturn(
      ServicesManager.WeatherServiceConfig("https://localhost:9999", currentThread, currentThread))

    WeatherService(manager).getForecast(0).subscribe(object: SingleObserver<Result<Forecast>> {
      override fun onSuccess(t: Result<Forecast>) {
        fail("response succeeded unexpectedly")
      }

      override fun onSubscribe(d: Disposable) {}
      override fun onError(e: Throwable) {
        if (e !is IllegalArgumentException) {
          fail(e.toString())
        }
        assertThat("message not as expected", e.message, equalTo("weatherDescription code must be a valid WeatherCode.code"))
      }

    })
  }

  @Test
  fun getForecastFailure() {
    val expectedResponse = ForecastWeatherApiResponse(listOf(ForecastWeatherApiResponse.Weather(
      LocalDateTime.of(2018, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC),
      ForecastWeatherApiResponse.MainWeather(0f, 0f, 0f),
      listOf(ForecastWeatherApiResponse.WeatherDescription(800, "desc")),
      ForecastWeatherApiResponse.Wind(),
      ForecastWeatherApiResponse.Clouds(),
      ForecastWeatherApiResponse.Rain(),
      ForecastWeatherApiResponse.Snow()
    )))

    val retrofitBuilder = Retrofit.Builder()
      .addConverterFactory(object : Converter.Factory() {
        override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
          return Converter<ResponseBody, ForecastWeatherApiResponse> { expectedResponse }
        }
      })
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(OkHttpClient.Builder()
        .addInterceptor {
          Response.Builder()
            .code(500)
            .request(it.request())
            .protocol(Protocol.HTTP_1_0)
            .message("Internal Server Error")
            .body(ResponseBody.create(MediaType.parse("application.json"), ";ekw"))
            .build()
        }
        .build()
      )

    val currentThread = Schedulers.from { it.run() }

    `when`(manager.keys).thenReturn(keys)
    `when`(manager.defaultRetrofitBuilder()).thenReturn(retrofitBuilder)
    `when`(manager.weatherServiceConfig).thenReturn(
      ServicesManager.WeatherServiceConfig("https://localhost:9999", currentThread, currentThread))
    WeatherService(manager).getForecast(0).subscribe(object : SingleObserver<Result<Forecast>> {
      override fun onSuccess(t: Result<Forecast>) {
        assertThat("response is null", t.response(), notNullValue())
        assertThat("error code isn't correct", t.response()!!.code(), equalTo(500))
      }

      override fun onSubscribe(d: Disposable) {}
      override fun onError(e: Throwable) {
        fail(e.toString())
      }
    })
  }

}
