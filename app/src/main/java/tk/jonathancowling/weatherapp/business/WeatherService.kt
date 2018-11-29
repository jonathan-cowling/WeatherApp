package tk.jonathancowling.weatherapp.business

import tk.jonathancowling.weatherapp.data.CurrentWeatherApiResponse
import tk.jonathancowling.weatherapp.data.Forecast
import tk.jonathancowling.weatherapp.data.ForecastWeatherApiResponse
import tk.jonathancowling.weatherapp.data.Weather
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherService @Inject constructor(private val manager: ServicesManager) {

  fun getWeather(id: Int): Single<Result<Weather>> {
    return retrofit.getWeather(id, manager.keys.weatherApi())
      .subscribeOn(manager.weatherServiceConfig.networkScheduler)
      .map {
        if (it.isError
          or (it.response() == null)) {
          return@map Result.error<Weather>(it.error()?: IOException())
        }

        if (!it.response()!!.isSuccessful or (it.response()!!.body() == null)){
          return@map Result.response(
            Response.error<Weather>(it.response()!!.code(),
            it.response()!!.errorBody()?: ResponseBody.create(
              MediaType.parse("text"), "")))
        }

        return@map Result.response(
          Response.success(Weather.fromResponse(it.response()!!.body()!!)))

      }
      .observeOn(manager.weatherServiceConfig.uiScheduler)
  }

  fun getForecast(id: Int): Single<Result<Forecast>> {
    return retrofit.getForecast(id, manager.keys.weatherApi())
      .subscribeOn(manager.weatherServiceConfig.networkScheduler)
      .map {
        if (it.isError
          or (it.response() == null)) {
          return@map Result.error<Forecast>(it.error()?: IOException())
        }

        if (!it.response()!!.isSuccessful or (it.response()!!.body() == null)){
          return@map Result.response(
            Response.error<Forecast>(it.response()!!.code(),
              it.response()!!.errorBody()?: ResponseBody.create(
                MediaType.parse("text"), "")))
        }

        return@map Result.response(
          Response.success(Forecast.fromResponse(it.response()!!.body()!!)))

      }
      .observeOn(manager.weatherServiceConfig.uiScheduler)
  }

  private val retrofit = manager.defaultRetrofitBuilder()
    .baseUrl(manager.weatherServiceConfig.BASE_URL)
    .build()
    .create(Endpoints::class.java)

  interface Endpoints {
    @GET("weather")
    fun getWeather(@Query("id") id: Int,
                   @Query("APPID") key: String): Single<Result<CurrentWeatherApiResponse>>
    @GET("forecast")
    fun getForecast(@Query("id") id: Int,
                   @Query("APPID") key: String): Single<Result<ForecastWeatherApiResponse>>
  }
}
