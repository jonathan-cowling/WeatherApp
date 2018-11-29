package tk.jonathancowling.weatherapp.ui.activity.help

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import tk.jonathancowling.weatherapp.R
import kotlinx.android.synthetic.main.toolbar.*

class HelpActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_help)
    setSupportActionBar(toolbar)
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
  }

}
