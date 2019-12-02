package ml.farih.appas.ui.provider

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.IconCompat.createWithResource
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.*
import androidx.slice.builders.ListBuilder.*
import com.bumptech.glide.Glide
import ml.farih.appas.data.ApiResponse
import ml.farih.appas.data.Repository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ml.farih.appas.R
import ml.farih.appas.di.DaggerAppComponent
import ml.farih.appas.ui.activity.MainActivity
import java.lang.Exception
import javax.inject.Inject

@SuppressLint("Slices")
class MainSliceProvider : SliceProvider() {
    private lateinit var contextNonNull: Context

    @Inject
    lateinit var repository: Repository
    var data = listOf(ApiResponse(), ApiResponse())


    override fun onCreateSliceProvider(): Boolean {
        DaggerAppComponent.builder().build().inject(this)
        contextNonNull = context ?: return false
        return true
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        when (sliceUri.path) {
            "/account" -> return createAccountSlice(sliceUri)
            "/order" -> return createOrderSlice(sliceUri)
        }
        return null
    }

    private fun createOrderSlice(sliceUri: Uri): Slice? {
        val mainPendingIntent = PendingIntent.getActivity(
            contextNonNull,
            sliceUri.hashCode(),
            Intent(contextNonNull,MainActivity::class.java),
            0
        )

        if (data[1].name == "Loading...") getDataFromNetwork(sliceUri)
        return list(contextNonNull, sliceUri, INFINITY) {
            setAccentColor(ContextCompat.getColor(contextNonNull, R.color.colorAccent))
            header {
                title = data[1].name
                subtitle = data[1].subtitle
                primaryAction = SliceAction.create(
                    mainPendingIntent,
                    createWithResource(contextNonNull, R.drawable.card_search),
                    ICON_IMAGE,
                    "SEARCH"
                )
            }
            gridRow {
                cell {
                    val futureTarget = Glide.with(contextNonNull)
                        .asBitmap()
                        .load("https://www.batamnews.co.id/foto_berita/83kuda.jpg")
                        .submit()
                    val bitmap = futureTarget.get()
                    addImage(IconCompat.createWithBitmap(bitmap), LARGE_IMAGE)
                }
            }
        }
    }

    private fun createAccountSlice(sliceUri: Uri): Slice {
        if (data[0].name == "Loading...") getDataFromNetwork(sliceUri)
        return list(contextNonNull, sliceUri, INFINITY) {
            setAccentColor(
                ContextCompat.getColor(
                    contextNonNull,
                    R.color.colorAccent
                )
            )
            header {
                title = data[0].name
                subtitle = data[0].subtitle
            }
        }
    }

    private fun getDataFromNetwork(sliceUri: Uri) {
        GlobalScope.launch {
            try {
                data = repository.getData()
                contextNonNull.contentResolver.notifyChange(sliceUri, null)
            } catch (e: Exception) {
                Log.e("E", e.toString())
            }
        }
    }
}