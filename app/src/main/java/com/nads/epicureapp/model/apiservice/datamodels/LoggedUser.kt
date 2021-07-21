package com.nads.epicureapp.model.apiservice.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoggedUser(@Expose
                      @SerializedName("datavalues")
                      val datavalues: Datavalues,
                      @Expose
                      @SerializedName("status")
                      val status: Boolean = false)