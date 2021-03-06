package net.yslibrary.monotweety.data

import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterSession
import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.data.license.LicenseModule
import net.yslibrary.monotweety.data.local.LocalModule
import net.yslibrary.monotweety.data.session.SessionModule
import net.yslibrary.monotweety.data.setting.SettingModule

/**
 * Created by yshrsmz on 2016/09/27.
 */
@Module(
    includes = arrayOf(
        LicenseModule::class,
        LocalModule::class,
        SessionModule::class,
        SettingModule::class))
class DataModule {

  @Provides
  fun provideSessionManager(): SessionManager<TwitterSession> {
    return TwitterCore.getInstance().sessionManager
  }
}