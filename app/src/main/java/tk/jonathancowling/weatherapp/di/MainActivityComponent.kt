package tk.jonathancowling.weatherapp.di

import android.content.res.Resources
import tk.jonathancowling.weatherapp.ui.activity.main.MainActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent
interface MainActivityComponent {
  fun inject(main: MainActivity)

  @Subcomponent.Builder
  interface Builder {
    @BindsInstance
    fun resources(resources: Resources): Builder

    fun build(): MainActivityComponent
  }
}
