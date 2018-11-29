package tk.jonathancowling.weatherapp.di

import android.content.Context
import tk.jonathancowling.keys.KeyManager
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
  fun activityComponentBuilder(): MainActivityComponent.Builder

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(applicationContext: Context): Builder

    fun build(): ApplicationComponent
  }
}

@Module(subcomponents = [MainActivityComponent::class])
class ApplicationModule {
  @Provides
  @Singleton
  fun provideKeyManager() = KeyManager()
}
