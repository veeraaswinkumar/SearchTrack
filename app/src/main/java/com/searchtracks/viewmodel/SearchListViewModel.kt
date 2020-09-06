package com.searchtracks.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.searchtracks.model.LoginOutput
import com.searchtracks.model.SearchData
import com.searchtracks.webservice.ApiServiceCall
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException

class SearchListViewModel(App:Application) : BaseViewModel(App) {

    fun accessApi(values:String):
            MutableLiveData<SearchData> {
        val imageResults = MutableLiveData<SearchData>()
        ApiServiceCall.accessApi( webApiService,values)
                .subscribeOn(Schedulers.newThread()) // creates a new thread for each unit of work scheduled
                .observeOn(AndroidSchedulers.mainThread()) // observes result in main thread
                .subscribe(     //subscribing results
                        { response ->
                            imageResults.postValue(response)
                        },
                        { error ->
                            try {
                                val errorBody = (error as HttpException).response()?.errorBody()?.string()
                                if (errorBody != null) {
                                    val searchData = SearchData()
                                    imageResults.postValue(searchData)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                imageResults.postValue(null)
                            }
                        }
                )
        return imageResults
    }


}