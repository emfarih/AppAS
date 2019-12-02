package ml.farih.appas.data

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("name")
    val name: String = "Loading...",

    @SerializedName("subtitle")
    val subtitle: String = "",

    @SerializedName("image_uri")
    val imageUri: String = ""
)