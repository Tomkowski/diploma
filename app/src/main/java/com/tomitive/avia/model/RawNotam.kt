package com.tomitive.avia.model

import com.google.gson.JsonObject

data class RawNotam(val total: Int, val rows: List<JsonObject>)