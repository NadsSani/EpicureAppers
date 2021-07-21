package com.nads.epicureapp.model.apiservice.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResultModel(@Expose
                       @SerializedName("status")
                       val status: Boolean? = false)