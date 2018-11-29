package tk.jonathancowling.weatherapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import tk.jonathancowling.weatherapp.R
import tk.jonathancowling.weatherapp.data.*
import kotlinx.android.synthetic.main.fragment_weather.*
import org.threeten.bp.Clock
import org.threeten.bp.LocalDate

class WeatherFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? =
    inflater.inflate(R.layout.fragment_weather, container, false)

  private var weather: Weather? = null
  private var city: City? = null
  private var forecast: Forecast? = null

  fun setState(weather: Weather, city: City, forecast: Forecast? = null) {
    this.weather = weather
    this.city = city
    this.forecast = forecast
  }

  private fun updateUI() {
    weather_description_text.text =
      String.format(getString(R.string.weather_description_template, weather!!.description))
    temperature_text.text =
      String.format(getString(R.string.temperature_template, weather!!.temperature))
    val temperatureUnitValue = when (weather!!.unit) {
      TemperatureUnit.CELSIUS -> "C"
      TemperatureUnit.FAHRENHEIT -> "F"
      TemperatureUnit.KELVIN -> "K"
    }
    temperature_unit_text.text =
      String.format(getString(R.string.temperature_unit_template, temperatureUnitValue))
    weather_image
      .setImageDrawable(resources.getDrawable(weather!!.code.icon, null))
    city_text.text =
      String.format(getString(R.string.city_text_template, city!!.name))

    forecast?. let {
      forecast_list_container.apply {
        setHasFixedSize(true)
        adapter = ForecastAdapter(it.weatherList.let{
          if(it.size >= 6) {
             it//.drop(1).take(5)
          } else {
            it.take(5)
          }
        })
        layoutManager = GridLayoutManager(fragmentContext, 5)
      }
    }
  }

  lateinit var fragmentContext: Context

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    if (context == null) {
      throw IllegalStateException("context shouldn't be null")
    }
    this.fragmentContext = context
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    if ((weather == null) or (city == null)) {
      throw IllegalStateException("set Weather must be set before adding to view")
    }
    updateUI()
  }

  inner class ForecastAdapter(private val data: List<Weather>) : RecyclerView.Adapter<ForecastViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ForecastViewHolder =
      ForecastViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.forecast_list_item, p0, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(viewHolder: ForecastViewHolder, index: Int) {
      viewHolder.apply {
        image.setImageDrawable(fragmentContext.getDrawable(data[index].code.icon))
        temperature.text = String.format(getString(R.string.forecast_list_item_temperature_template), data[index].temperature)
        unit.text = String.format(getString(R.string.forecast_list_item_temperature_unit_template), when(data[index].unit){
          TemperatureUnit.CELSIUS -> "C"
          TemperatureUnit.KELVIN -> "K"
          TemperatureUnit.FAHRENHEIT -> "F"
        })
        val correspondingDate = LocalDate.now(Clock.systemUTC()).plusDays(index.toLong()).dayOfMonth
        dayName.text = String.format(getString(R.string.forecast_list_item_day_text_template), correspondingDate, DateOrdinal.get(correspondingDate))
      }
    }
  }

  data class ForecastViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val image: ImageView = view.findViewById(R.id.forecast_list_item_image)
    val temperature: TextView = view.findViewById(R.id.forecast_list_item_temperature_text)
    val unit: TextView = view.findViewById(R.id.forecast_list_item_temperature_unit_text)
    val dayName: TextView = view.findViewById(R.id.forecast_list_item_day_text)
  }
}
