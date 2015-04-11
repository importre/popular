package io.github.importre.popular.api

import com.squareup.okhttp.OkHttpClient
import io.github.importre.popular.BuildConfig
import retrofit.RequestInterceptor
import retrofit.RestAdapter
import retrofit.client.OkClient

public class Popular {

    companion object {

        public val CLIENT_ID: String = ""
        public val restAdapter: RestAdapter
        public val service: PopularService

        init {
            val client = OkHttpClient()
            val logLevel = getLogLevel()
            val endPoint = "https://api.instagram.com"

            val interceptor = RequestInterceptor { request ->
                request.addQueryParam("client_id", CLIENT_ID)
            }

            restAdapter = RestAdapter.Builder()
                    .setEndpoint(endPoint)
                    .setLogLevel(logLevel)
                    .setRequestInterceptor(interceptor)
                    .setClient(OkClient(client)).build()

            service = restAdapter.create<PopularService>(javaClass<PopularService>())
        }

        private fun getLogLevel(): RestAdapter.LogLevel {
            return if (BuildConfig.DEBUG) {
                RestAdapter.LogLevel.FULL
            } else {
                RestAdapter.LogLevel.NONE
            }
        }
    }
}
