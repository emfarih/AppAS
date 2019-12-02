package ml.farih.appas.ui.activity

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.service.voice.VoiceInteractionService
import androidx.appcompat.app.AppCompatActivity
import androidx.slice.SliceManager
import com.google.firebase.appindexing.Action
import com.google.firebase.appindexing.FirebaseUserActions
import com.google.firebase.appindexing.builders.AssistActionBuilder
import kotlinx.android.synthetic.main.activity_main.*
import ml.farih.appas.R


class MainActivity : AppCompatActivity() {
    @Suppress("PrivatePropertyName")
    private val SLICE_AUTHORITY = "ml.farih.appas"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getIntentData()
        grantSlicePermissions()
    }

    private fun getIntentData() {
        val action = intent.action
        val data = intent.data
        if (action == Intent.ACTION_VIEW && data != null) {
            when (data.lastPathSegment) {
                "main" -> notifyActionStatus(Action.Builder.STATUS_TYPE_COMPLETED)
                else -> tv_data.text = data.lastPathSegment
            }
        }
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

    private val actionTokenExtra = "actions.fulfillment.extra.ACTION_TOKEN"

    private fun notifyActionStatus(status: String) {
        val actionToken = intent.getStringExtra(actionTokenExtra)
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val action =
            AssistActionBuilder()
                .setActionToken(actionToken)
                .setActionStatus(status)
                .build()
        FirebaseUserActions.getInstance().end(action)
    }
}
