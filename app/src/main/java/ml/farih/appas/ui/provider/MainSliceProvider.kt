package ml.farih.appas.ui.provider

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat.createWithBitmap
import androidx.core.graphics.drawable.IconCompat.createWithResource
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.*
import androidx.slice.builders.ListBuilder.*
import com.bumptech.glide.Glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ml.farih.appas.R
import ml.farih.appas.data.ApiResponse
import ml.farih.appas.data.Repository
import ml.farih.appas.di.DaggerAppComponent
import ml.farih.appas.ui.activity.MainActivity
import javax.inject.Inject

@SuppressLint("Slices")
class MainSliceProvider : SliceProvider() {
    private lateinit var contextNonNull: Context

    @Inject
    lateinit var repository: Repository
    var data = listOf(ApiResponse(), ApiResponse(), ApiResponse())


    override fun onCreateSliceProvider(): Boolean {
        DaggerAppComponent.builder().build().inject(this)
        contextNonNull = context ?: return false
        return true
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        Log.e("SLICE", sliceUri.query.toString())
        return when (sliceUri.path) {
            "/account" -> createAccountSlice(sliceUri)
            "/order" -> createOrderSlice(sliceUri)
            "/pay_invoice" -> createPayInvoiceSlice(sliceUri)
            "/get_invoice" -> createGetInvoiceSlice(sliceUri)
            else -> null
        }
    }

    private fun createGetInvoiceSlice(sliceUri: Uri): Slice? {
        val mainPendingIntent = PendingIntent.getActivity(
            contextNonNull,
            sliceUri.hashCode(),
            Intent(contextNonNull, MainActivity::class.java),
            0
        )
        return list(contextNonNull, sliceUri, INFINITY) {
            setAccentColor(ContextCompat.getColor(contextNonNull, R.color.colorAccent))
            if (sliceUri.getQueryParameter("serviceName") != null) {
                header {
                    title = "Tagihan ${sliceUri.getQueryParameter("serviceName")}"
                    subtitle = "IDR 299.999"
                }
            } else {
                header {
                    title = "Tagihan Listrik"
                    subtitle = "IDR 299.999"

                    // Must be included to show the next row
                    primaryAction = SliceAction.create(
                        mainPendingIntent,
                        createWithResource(contextNonNull, R.drawable.card_search),
                        ICON_IMAGE,
                        "SEARCH"
                    )
                }
                row {
                    title = "Tagihan Internet"
                    subtitle = "IDR 199.999"
                }
                row {
                    title = "Tagihan Air"
                    subtitle = "IDR 249.999"
                }
            }
//            addAction(
//                SliceAction.create(
//                    mainPendingIntent,
//                    createWithResource(contextNonNull, R.drawable.open_in_new),
//                    ICON_IMAGE,
//                    ""
//                )
//            )
        }
    }

    private fun createPayInvoiceSlice(sliceUri: Uri): Slice {
        val mainPendingIntent = PendingIntent.getActivity(
            contextNonNull,
            sliceUri.hashCode(),
            Intent(contextNonNull, MainActivity::class.java),
            0
        )
        if (data[2].name == "Loading...") getDataFromNetwork(sliceUri)
        val amountCurrency =
            if (sliceUri.getQueryParameter("amountCurrency") != null) sliceUri.getQueryParameter("amountCurrency")
            else "IDR"
        val amountValue =
            if (sliceUri.getQueryParameter("amountValue") != null) sliceUri.getQueryParameter("amountValue")
            else "299.999"
        return list(contextNonNull, sliceUri, INFINITY) {
            setAccentColor(ContextCompat.getColor(contextNonNull, R.color.colorAccent))
            header {
                title = "Bayar Tagihan ${sliceUri.getQueryParameter("serviceName")}"
                subtitle = "Total : $amountCurrency $amountValue"
            }
            addAction(
                SliceAction.create(
                    mainPendingIntent,
                    createWithResource(contextNonNull, R.drawable.credit_card_outline),
                    ICON_IMAGE,
                    ""
                )
            )
        }
    }

    private fun createOrderSlice(sliceUri: Uri): Slice? {
        val mainPendingIntent = PendingIntent.getActivity(
            contextNonNull,
            sliceUri.hashCode(),
            Intent(contextNonNull, MainActivity::class.java),
            0
        )

        if (data[1].name == "Loading...") getDataFromNetwork(sliceUri)
        return list(contextNonNull, sliceUri, INFINITY) {
            setAccentColor(ContextCompat.getColor(contextNonNull, R.color.colorAccent))
            header {
                title = data[1].name
                subtitle = data[1].subtitle

                // Must be included to show the gridRow
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
                    addImage(createWithBitmap(bitmap), LARGE_IMAGE)
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