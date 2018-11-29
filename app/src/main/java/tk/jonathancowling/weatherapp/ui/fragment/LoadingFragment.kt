package tk.jonathancowling.weatherapp.ui.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tk.jonathancowling.weatherapp.R
import kotlinx.android.synthetic.main.fragment_loading.*

class LoadingFragment : DialogFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    dialog.setCanceledOnTouchOutside(false)
    return inflater.inflate(R.layout.fragment_loading, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    loading_message_detailed.text =
      String.format(getString(R.string.getting_weather_template), "Manchester")
  }
}
