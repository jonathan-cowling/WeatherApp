package tk.jonathancowling.weatherapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tk.jonathancowling.weatherapp.R
import kotlinx.android.synthetic.main.fragment_retry.*

class RetryFragment : DialogFragment(), ObservableFragment {
  override fun interact(listener: ObservableFragment.OnFragmentInteractionListener) {
    listener.onFragmentInteraction(RetryFragmentMessage)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    dialog.setCanceledOnTouchOutside(false)
    return inflater.inflate(R.layout.fragment_retry, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    retry_text.text =
      String.format(getString(R.string.retry_template), "Manchester")

    retry_button.setOnClickListener { interact(context as ObservableFragment.OnFragmentInteractionListener) }
  }

  override fun onAttach(context: Context?) {
    if (context !is ObservableFragment.OnFragmentInteractionListener) {
      throw ClassCastException(
        """context $context must implement ${ObservableFragment.OnFragmentInteractionListener::class.java.name}""")
    }
    super.onAttach(context)
  }

  object RetryFragmentMessage : ObservableFragment.Message
}
