package com.m.appas.ui.provider

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.header
import androidx.slice.builders.list
import com.m.appas.R
import com.m.appas.data.ApiResponse
import com.m.appas.data.Repository
import com.m.appas.di.DaggerAppComponent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class MainSliceProvider : SliceProvider() {
    private lateinit var contextNonNull: Context

    @Inject
    lateinit var repository: Repository
    var data = ApiResponse()

    override fun onCreateSliceProvider(): Boolean {
        DaggerAppComponent.builder().build().inject(this)
        contextNonNull = context ?: return false
        return true
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        when(sliceUri.path){
            "/account" -> return createMainSlice(sliceUri)
        }
        return null
    }

    private fun createMainSlice(sliceUri: Uri): Slice {
        if(data.name=="Loading...") getDataFromNetwork(sliceUri)
        return list(contextNonNull, sliceUri, ListBuilder.INFINITY) {
            setAccentColor(ContextCompat.getColor(contextNonNull,
                R.color.colorAccent
            ))
            header {
                title = data.name
                subtitle = data.imageUri
            }
        }
    }

    private fun getDataFromNetwork(sliceUri: Uri) {
        GlobalScope.launch {
            try {
                data = repository.getData()[0]
                contextNonNull.contentResolver.notifyChange(sliceUri,null)
            }catch (e: Exception){
                Log.e("E",e.toString())
            }
        }
    }
}