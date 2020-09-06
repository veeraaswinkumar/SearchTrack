package com.app.gcain.permissionhelper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import java.util.ArrayList

/**
 * Created by nextbrain on 18/4/18.
 */

class PermissionChecker {
    lateinit var activity: Activity
    lateinit var fragment: Fragment
    private var permissions: ArrayList<String>? = null
    private var successListener: Runnable? = null
    private var deniedListener: Runnable? = null
    private var neverAskAgainListener: Runnable? = null
    private var dialogBeforeRunBuilder: AlertDialog.Builder? = null
    private var dialogBeforeAskPositiveButton: Int = 0
    private var dialogBeforeAskPositiveButtonColor = DIALOG_WITHOUT_CUSTOM_COLOR

    /**
     * This method return context, depending on what you use: activity or fragment
     *
     * @return context
     */
    private val context: Context?
        get() = activity ?: fragment.context


    /**
     * This method check listeners for null
     *
     * @return true if you realized method onSuccess and onDenied
     */
    private val isListenersCorrect: Boolean
        get() = successListener != null && deniedListener != null


    /**
     * This method ckeck api version
     *
     * @return true if API >=23
     */
    private val isNeedToAskPermissions: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M


    /**
     * @return Array of permissions, that will be request
     */
    private val permissionsForRequest: Array<String>
        get() {
            val permissionsForRequest = ArrayList<String>()
            for (permission in permissions!!) {
                if (isPermissionNotGranted(permission)) {
                    permissionsForRequest.add(permission)
                }
            }
            return permissionsForRequest.toTypedArray()
        }


    /**
     * Default activity constructor
     *
     * @param activity is activity instance. Use it only in activities. Don't use in fragments!
     */
    constructor(activity: Activity) {
        this.activity = activity
    }


    /**
     * Default fragment constructor
     *
     * @param fragment is fragment instance. Use it only in fragments
     */
    constructor(fragment: Fragment) {
        this.fragment = fragment
    }


    /**
     * @param permission is single permission, which you want to ask
     * @return current object
     */
    fun check(permission: String): PermissionChecker {
        this.permissions = ArrayList()
        permissions!!.add(permission)
        return this
    }


    /**
     * @param permissions is array of permissions, which you want to ask
     * @return current object
     */
    fun check(permissions: ArrayList<String>): PermissionChecker {
        this.permissions = permissions
        return this
    }


    /**
     * Setup success callback
     *
     * @param listener called when user success permission
     * @return current object
     */
    fun onSuccess(listener: Runnable): PermissionChecker {
        this.successListener = listener
        return this
    }

    /**
     * Setup denied callback
     *
     * @param listener called when user deny permission
     * @return current object
     */
    fun onDenied(listener: Runnable): PermissionChecker {
        this.deniedListener = listener
        return this
    }


    @Deprecated("replaced by  {@link #onDenied(Runnable)} ()")
    fun onFailure(listener: Runnable): PermissionChecker {
        this.deniedListener = listener
        return this
    }

    /**
     * This method setup never ask again callback
     *
     * @param listener called when permission in status "never ask again"
     * @return current object
     */
    fun onNeverAskAgain(listener: Runnable): PermissionChecker {
        this.neverAskAgainListener = listener
        return this
    }


    /**
     * This method setup custom dialog before permissions will be asked.
     * Dialog will be shown only if permissions not granted.
     *
     * @param titleRes          dialog title string resource
     * @param messageRes        dialog message string resource
     * @param positiveButtonRes dialog positive button string resource
     * @return current object
     */

    fun withDialogBeforeRun(
        @StringRes titleRes: Int,
        @StringRes messageRes: Int,
        @StringRes positiveButtonRes: Int
    ): PermissionChecker {

        this.dialogBeforeAskPositiveButton = positiveButtonRes
        dialogBeforeRunBuilder = getDialogBuilder(titleRes, messageRes)
        return this
    }


    /**
     * This method setup custom dialog positive button color
     *
     * @param colorRes dialog positive button string resource
     * @return current object
     */
    fun setDialogPositiveButtonColor(@ColorRes colorRes: Int): PermissionChecker {
        this.dialogBeforeAskPositiveButtonColor = ContextCompat.getColor(context!!, colorRes)
        return this
    }


    /**
     * This method return dialog builder with default settings.
     * It is created for the future customization
     *
     * @param titleRes   dialog title string resource
     * @param messageRes dialog message string resource
     * @return new dialog builder object
     */
    private fun getDialogBuilder(@StringRes titleRes: Int, @StringRes messageRes: Int): AlertDialog.Builder {
        val context = context
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle(context!!.getString(titleRes))
        dialogBuilder.setMessage(context.getString(messageRes))
        dialogBuilder.setCancelable(false)
        return dialogBuilder
    }


    /**
     * This method check API-version and listeners
     *
     * @throws RuntimeException if isListenersCorrect return false
     */
    fun run() {
        if (isListenersCorrect) {
            runSuccessOrAskPermissions()
        } else {
            throw RuntimeException("permissionSuccessListener or permissionDeniedListener have null reference. You must realize onSuccess and onDenied methods")
        }
    }


    /**
     * This method run successListener if all permissions granted,
     * and run method c[.checkPermissions], if [.isNeedToAskPermissions] return false
     */
    private fun runSuccessOrAskPermissions() {
        if (isNeedToAskPermissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermissions()
            }
        } else {
            successListener!!.run()
        }
    }


    /**
     * This method request only those permissions that are not granted.
     * If all are granted, success callback called
     * otherwise [.checkDialogAndAskPermissions] will called
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkPermissions() {
        val permissionsForRequest = permissionsForRequest
        if (permissionsForRequest.size > 0) {
            checkDialogAndAskPermissions(permissionsForRequest)
        } else {
            successListener!!.run()
        }
    }

    /**
     * This method check your dialog
     * If you set it, [.withDialogBeforeRun], that dialog will be show before system permission dialog
     * otherwise [.askPermissions] will be called
     * Note, custom dialog will show only if permissions not granted.
     *
     * @param permissionsForRequest = permissions, when currently not granted and will be asked
     */
    @SuppressLint("NewApi")
    private fun checkDialogAndAskPermissions(permissionsForRequest: Array<String>) {
        if (dialogBeforeRunBuilder != null && isNotContainsNeverAskAgain(permissionsForRequest)) {
            showDialogBeforeRun(permissionsForRequest)
        } else {
            askPermissions(permissionsForRequest)
        }
    }

    /**
     * This method check permissions for never again.
     *
     * @param permissionsForRequest = permissions, when currently not granted and will be asked
     * @return false if one of them never ask gain
     */
    private fun isNotContainsNeverAskAgain(permissionsForRequest: Array<String>): Boolean {
        for (permissions in permissionsForRequest) {
            if (isNeverAskAgain(permissions)) {
                return false
            }
        }
        return true
    }

    /**
     * This method set positive button and custom color to your dialog
     * method [.askPermissions] called when positive button clicked
     *
     * @param permissionsForRequest = permissions, when currently not granted and will be asked
     */
    private fun showDialogBeforeRun(permissionsForRequest: Array<String>) {
        dialogBeforeRunBuilder!!.setPositiveButton(dialogBeforeAskPositiveButton) { dialogInterface, i ->
            askPermissions(
                permissionsForRequest
            )
        }
        val dialogBeforeRun = dialogBeforeRunBuilder!!.create()

        dialogBeforeRun.show()
        if (dialogBeforeAskPositiveButtonColor != DIALOG_WITHOUT_CUSTOM_COLOR) {
            dialogBeforeRun.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(dialogBeforeAskPositiveButtonColor)
        }
    }

    /**
     * This method ask permission
     *
     * @param permissionsForRequest array of permissions which you want to ask
     */
    @SuppressLint("NewApi")
    private fun askPermissions(permissionsForRequest: Array<String>) {
        if (activity != null) {
            activity.requestPermissions(permissionsForRequest, PERMISSION_REQUEST_CODE)
        } else {
            fragment.requestPermissions(permissionsForRequest, PERMISSION_REQUEST_CODE)
        }
    }


    /**
     * if permission not granted, check neverAskAgain, else call failure
     * if permission grander, call success
     *
     * @param grantResults Permissions, which granted
     * @param permissions  Permissions, which you asked
     * @param requestCode  requestCode of out request
     */
    @SuppressLint("NewApi")
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (permission in permissions) {
                if (isPermissionNotGranted(permission)) {
                    runDeniedOrNeverAskAgain(permission)
                    return
                }
            }
        }
        successListener!!.run()
        unbind()
    }

    /**
     * This method run denied or neverAskAgain callbacks
     *
     * @param permission Permissions, which granted
     */

    @SuppressLint("NewApi")
    private fun runDeniedOrNeverAskAgain(permission: String) {
        if (isNeverAskAgain(permission)) {
            runNeverAskAgain()
        } else {
            deniedListener!!.run()
        }
        unbind()
    }


    /**
     * This method run neverAskAgain callback if neverAskAgainListener not null
     */
    private fun runNeverAskAgain() {
        if (neverAskAgainListener != null) {
            neverAskAgainListener!!.run()
        }
    }


    /**
     * @param permission for check
     * @return true if permission granted and false if permission not granted
     */
    private fun isPermissionNotGranted(permission: String): Boolean {
        return if (activity != null) {
            ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
        } else {
            ActivityCompat.checkSelfPermission(fragment.context!!, permission) != PackageManager.PERMISSION_GRANTED
        }
    }


    /**
     * @param permission for check neverAskAgain
     * @return true if user checked "Never Ask Again"
     */
    @SuppressLint("NewApi")
    private fun isNeverAskAgain(permission: String): Boolean {
        return if (activity != null) {
            !activity.shouldShowRequestPermissionRationale(permission)
        } else {
            !fragment.shouldShowRequestPermissionRationale(permission)
        }
    }


    /**
     * This method start application settings activity
     * Note: is not possible to open at once screen with application permissions.
     */
    fun startApplicationSettingsActivity() {
        val context = context
        val intent = Intent()
        val uri = Uri.fromParts("package", context!!.packageName, null)
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = uri
        context.startActivity(intent)

    }


    @Deprecated(
        "no necessary to use begin the version 1.1.2, it's work automatically\n" +
                "      after each permissions request.\n" +
                "      Don't forget setup callbacks again."
    )
    fun unsubscribe() {
        unbind()
    }

    /**
     * This method change listeners reference to avoid memory leaks
     * Don't forget setup callbacks and your settings again!
     */
    private fun unbind() {
        deniedListener = null
        successListener = null
        if (dialogBeforeRunBuilder != null) {
            dialogBeforeRunBuilder = null
            dialogBeforeAskPositiveButton = DIALOG_WITHOUT_CUSTOM_COLOR
        }
        if (neverAskAgainListener != null) {
            neverAskAgainListener = null
        }
    }

    companion object {

        private val PERMISSION_REQUEST_CODE = 98
        private val DIALOG_WITHOUT_CUSTOM_COLOR = 0
    }
}

