package com.redridgeapps.remoteforqbittorrent.api

import com.redridgeapps.remoteforqbittorrent.repo.PreferenceRepository
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QBittorrentInterceptor @Inject constructor(
        private val prefRepo: PreferenceRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val origRequest = chain.request()

        // Make request
        val origResponse = chain.proceed(origRequest.withAuth())

        // If request failed with Auth error
        if (origResponse.code() == ERROR_403_FORBIDDEN) {

            // Re-login
            val loginSucceeded = login(chain)

            // If login succeeds re-execute original request
            if (loginSucceeded) return chain.proceed(origRequest.withAuth())
        }

        return origResponse
    }

    private fun Request.withAuth(): Request {
        return newBuilder()
                .addHeader(HEADER_LABEL_COOKIE, prefRepo.sid)
                .build()
    }

    private fun login(chain: Interceptor.Chain): Boolean {

        val formBody = FormBody.Builder()
                .add("username", prefRepo.username)
                .add("password", prefRepo.password)
                .build()

        val request = Request.Builder()
                .url(QBittorrentService.buildURL(prefRepo.baseUrl, QBittorrentService.AUTH_LOGIN))
                .post(formBody)
                .build()

        val response = chain.proceed(request)

        val sid = QBittorrentService.extractSID(response)

        sid?.let { prefRepo.sid = it }

        return !sid.isNullOrBlank()
    }
}

private const val ERROR_403_FORBIDDEN = 403
private const val HEADER_LABEL_COOKIE = "Cookie"
