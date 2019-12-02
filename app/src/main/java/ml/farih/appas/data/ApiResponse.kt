package ml.farih.appas.data

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("name")
    val name: String = "Loading...",

    @SerializedName("image_uri")
    val imageUri: String = ""
)