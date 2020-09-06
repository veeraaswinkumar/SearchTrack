package com.searchtracks.webservice

import com.searchtracks.model.*
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody


object ApiServiceCall {
    fun accessApi( webApiService: ApiServiceInterface,values:String): Observable<SearchData> {
        return webApiService.GetTrackList(values)
    }
}