package com.tokopedia.test.appintentnslice

import android.content.Context
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.header
import androidx.slice.builders.list

class MainSliceProvider : SliceProvider() {
    private lateinit var contextNonNull: Context

    override fun onCreateSliceProvider(): Boolean {
        contextNonNull = context ?: return false
        return true
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        when(sliceUri.path){
            "/main" -> return createMainSlice(sliceUri)
        }
        return null
    }

    private fun createMainSlice(sliceUri: Uri): Slice {
        return list(contextNonNull, sliceUri, ListBuilder.INFINITY) {
            setAccentColor(ContextCompat.getColor(contextNonNull,
                R.color.colorAccent
            ))
            header {
                title = "UMRAH PLUS DUBAI"
                subtitle = "Rp20.750.000"
            }
        }
    }

}