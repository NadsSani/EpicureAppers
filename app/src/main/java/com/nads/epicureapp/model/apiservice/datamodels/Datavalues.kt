package com.nads.epicureapp.model.apiservice.datamodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Datavalues(@Expose
                      @SerializedName("favoritefoods")
                      val favoritefoods: String = "",
                      @Expose
                      @SerializedName("favoritecooks")
                      val favoritecooks: String = "",
                      @Expose
                      @SerializedName("pass")
                      val pass: String = "",
                      @Expose
                      @SerializedName("profilepic")
                      val profilepic: String = "",
                      @Expose
                      @SerializedName("rating")
                      val rating: String = "",
                      @Expose
                      @SerializedName("emailid")
                      val emailid: String = "",
                      @Expose
                      @SerializedName("locations")
                      val locations: String = "",
                      @Expose
                      @SerializedName("id")
                      val id: String = "",
                      @Expose
                      @SerializedName("userid")
                      val username: String = "")