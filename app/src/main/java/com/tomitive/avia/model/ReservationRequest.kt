package com.tomitive.avia.model

import com.google.gson.annotations.SerializedName

data class ReservationRequest(
    @SerializedName("requester")val requester: Credentials,
    @SerializedName("classId")val classId: Long,
    @SerializedName("title")val title: String,
    @SerializedName("beginDate")val beginDate: Long,
    @SerializedName("endDate")val endDate: Long
)
