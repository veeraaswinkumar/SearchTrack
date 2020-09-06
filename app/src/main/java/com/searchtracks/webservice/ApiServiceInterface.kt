package com.searchtracks.webservice

import com.searchtracks.model.*
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.*

interface ApiServiceInterface {
    //declare apis here
    @GET("search")
    fun GetTrackList(@Query("term") term: String):
            Observable<SearchData>
}