package tk.jonathancowling.keys

import android.util.Base64

open class KeyManager {
    external fun weatherApi64(): String
    fun weatherApi() = String(Base64.decode(weatherApi64(), Base64.NO_WRAP))

    companion object {
        init {
            System.loadLibrary("keys")
        }
    }
}
