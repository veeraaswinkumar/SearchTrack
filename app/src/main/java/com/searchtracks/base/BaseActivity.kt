package com.searchtracks.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.gcain.permissionhelper.PermissionChecker
import com.searchtracks.MyApplication
import com.searchtracks.room.SearchDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


abstract class BaseActivity : AppCompatActivity() {

    lateinit var myApplication: MyApplication

    lateinit var permissionChecker: PermissionChecker

    @Inject
    lateinit var searchDatabase: SearchDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermissionChecker()
        myApplication = applicationContext as MyApplication
        myApplication.appComponent.inject(this)
    }

    private fun initPermissionChecker() {
        permissionChecker = PermissionChecker(this@BaseActivity)
    }


    interface OnDialogClickListener {
        fun onPositiveClick(dialog: Dialog)
        fun onNegativeClick(dialog: Dialog)
    }


}