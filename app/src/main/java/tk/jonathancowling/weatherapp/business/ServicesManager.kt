package tk.jonathancowling.weatherapp.business

import tk.jonathancowling.keys.KeyManager
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServicesManager @Inject constructor(val keys: KeyManager) {
  fun defaultRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

  data class WeatherServiceConfig (
    val BASE_URL: String = "http://api.openweathermap.org/data/2.5/",
    val networkScheduler: Scheduler = Schedulers.io(),
    val uiScheduler : Scheduler = AndroidSchedulers.mainThread()
  )

  val weatherServiceConfig = WeatherServiceConfig()
}
