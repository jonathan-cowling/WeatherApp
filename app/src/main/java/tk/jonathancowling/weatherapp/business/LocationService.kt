package tk.jonathancowling.weatherapp.business

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import tk.jonathancowling.weatherapp.R
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationService @Inject constructor(manager: ServicesManager, context: Context) {

  val gson = Gson()
  val cities: List<City> = gson.fromJson(JsonReader(InputStreamReader(
    context.applicationContext.resources.openRawResource(R.raw.city_list)
  )), object : TypeToken<List<City>>() {}.type)

  class City {
    var name: String? = null
    var id: Int? = null
  }

  fun getCityId(name: String) = cities.filter { it.name == name }.let {
    if (it.isNotEmpty()) {
      it[0].id!!
    } else {
      null
    }
  }

}
