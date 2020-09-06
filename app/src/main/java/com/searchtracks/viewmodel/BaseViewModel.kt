package com.searchtracks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.searchtracks.MyApplication
import com.searchtracks.webservice.ApiServiceInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


open class BaseViewModel(var app: Application) : AndroidViewModel(app) {

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var webApiService: ApiServiceInterface

    init {
        val myApplication = app as MyApplication
        myApplication.appComponent.inject(this)
    }


    fun convertToJson(T: Any): String {
        return gson.toJson(T)
    }


    fun loadRequestBodyData(value: String): RequestBody {
        return ("" + value
                ).toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }

    fun loadMultipartImage(imageFile: File?, parmName: String = "image"): MultipartBody.Part? {
        // create RequestBody instance from file
        return if (imageFile != null) {
            val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part.createFormData(parmName, imageFile.name, requestFile)
        } else {
            null
        }
    }

}