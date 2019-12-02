package ml.farih.appas.ui.provider

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.header
import androidx.slice.builders.list
import ml.farih.appas.data.ApiResponse
import ml.farih.appas.data.Repository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ml.farih.appas.R
import ml.farih.appas.di.DaggerAppComponent
import java.lang.Exception
import javax.inject.Inject

@SuppressLint("Slices")
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
        when (sliceUri.path) {
            "/account" -> return createMainSlice(sliceUri)
        }
        return null
    }

    private fun createMainSlice(sliceUri: Uri): Slice {
        if (data.name == "Loading...") getDataFromNetwork(sliceUri)
        return list(contextNonNull, sliceUri, ListBuilder.INFINITY) {
            setAccentColor(
                ContextCompat.getColor(
                    contextNonNull,
                    R.color.colorAccent
                )
            )
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
                contextNonNull.contentResolver.notifyChange(sliceUri, null)
            } catch (e: Exception) {
                Log.e("E", e.toString())
            }
        }
    }
}