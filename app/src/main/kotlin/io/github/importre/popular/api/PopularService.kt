package io.github.importre.popular.api

import retrofit.http.GET
import rx.Observable

public trait PopularService {

    GET("/v1/media/popular")
    fun getPopular(): Observable<PopularResult>
}
