package tk.jonathancowling.weatherapp.ui.fragment

interface ObservableFragment {

  fun interact(listener: OnFragmentInteractionListener)

  interface OnFragmentInteractionListener {
    fun onFragmentInteraction(msg: Message)
  }

  interface Message
}
