package com.nads.epicureapp.model.apiservice.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddRecipeModel(@Expose
                           @SerializedName("status")
                          val status: Boolean? = false,
                          @Expose
                           @SerializedName("pay")
                          val pay:String)
