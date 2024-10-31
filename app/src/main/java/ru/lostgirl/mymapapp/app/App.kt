package ru.lostgirl.mymapapp.app

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import ru.lostgirl.mymapapp.BuildConfig

class App: Application() {
    override fun onCreate() {
        MapKitFactory.setApiKey(BuildConfig.API_KEY)
        super.onCreate()
    }
}