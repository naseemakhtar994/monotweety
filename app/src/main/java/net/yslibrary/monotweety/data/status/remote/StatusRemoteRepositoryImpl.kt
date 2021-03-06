package net.yslibrary.monotweety.data.status.remote

import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import net.yslibrary.monotweety.base.di.UserScope
import rx.Emitter
import rx.Observable
import rx.Single
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/09/30.
 */
@UserScope
class StatusRemoteRepositoryImpl @Inject constructor(private val statusesService: UpdateStatusService) : StatusRemoteRepository {

  override fun update(status: String, inReplyToStatusId: Long?): Single<Tweet> {
    return Observable.fromEmitter<Tweet>({ emitter ->
      val call = statusesService.update(encodeStatus(status), inReplyToStatusId, null, null, null, null, null, null, null)
      call.enqueue(object : Callback<Tweet>() {
        override fun success(result: Result<Tweet>) {
          emitter.onNext(result.data)
          emitter.onCompleted()
        }

        override fun failure(exception: TwitterException) {
          emitter.onError(exception)
        }
      })
      emitter.setCancellation { call.cancel() }
    }, Emitter.BackpressureMode.BUFFER).toSingle()
  }

  /**
   * encode status and replace `*` with `%2A`
   * https://github.com/twitter/twitter-kit-android/issues/68
   */
  fun encodeStatus(status: String): String {
    var encoded = status

    try {
      encoded = URLEncoder.encode(status, "UTF-8")
    } catch (e: UnsupportedEncodingException) {
      // no-op
    }
    encoded = encoded.replace("*", "%2A")

    return encoded
  }
}