package com.m.appas.ui.activity

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.service.voice.VoiceInteractionService
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.slice.SliceManager
import com.m.appas.R
import com.m.appas.data.Repository
import com.m.appas.di.DaggerAppComponent
import com.m.appas.ui.provider.MainSliceProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Suppress("PrivatePropertyName")
    private val SLICE_AUTHORITY = "com.m.appas"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        grantSlicePermissions()
    }

    private fun grantSlicePermissions() {
        val context: Context = applicationContext
        val sliceProviderUri: Uri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(SLICE_AUTHORITY)
            .build()

        val assistantPackage: String = getAssistantPackage(context) ?: return
        SliceManager.getInstance(context)
            .grantSlicePermission(assistantPackage, sliceProviderUri)
    }

    private fun getAssistantPackage(context: Context): String? {
        val packageManager = context.packageManager
        val resolveInfoList =
            packageManager.queryIntentServices(
                Intent(VoiceInteractionService.SERVICE_INTERFACE), 0
            )
        return if (resolveInfoList.isEmpty()) {
            null
        } else resolveInfoList[0].serviceInfo.packageName
    }
}
