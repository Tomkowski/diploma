package com.tomitive.avia.model

import com.google.gson.annotations.SerializedName

data class CancellationRequest(
    @SerializedName("requester") val requester: Credentials,
    @SerializedName("classId") val classId: Long
)