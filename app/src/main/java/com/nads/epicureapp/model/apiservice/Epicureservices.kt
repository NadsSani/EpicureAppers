package com.nads.epicureapp.model.apiservice

import com.nads.epicureapp.model.apiservice.datamodels.AddRecipeModel
import com.nads.epicureapp.model.apiservice.datamodels.LoggedUser
import com.nads.epicureapp.model.apiservice.datamodels.ResultModel
import com.nads.epicureapp.model.apiservice.datamodels.SplashData
import com.nads.epicureapp.ui.homepage.model.*
import com.nads.epicureapp.ui.homepage.profiledir.models.ResultWFoodId
import com.nads.epicureapp.ui.loginui.loginmodels.LoginModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface Epicureservices {

//==================Unused
   //Gets the splash video url
    @GET("getsplashvidurl")
    suspend fun getsplashvid():SplashData


    //Gets the login done
    @FormUrlEncoded
    @POST("login")
    suspend fun login(@Field("username")username:String,@Field("password")password:String):LoggedUser

    //Gets the signup done
    @FormUrlEncoded
    @POST("signup")
    suspend fun signup(@Field("username")username:String,@Field("password")password:String): ResultModel

    @FormUrlEncoded
    @POST("confirmotp")
    suspend fun sendconfirmotp(@Field("username")emailid:String,@Field("password")password:String
                               ,@Field("otp")otp:String):ResultModel
//==========================Unused


    @FormUrlEncoded
    @POST("addusercomp")
    suspend fun adddetails(@Field("emailid")emailid:String,@Field("userid")userid:String
                           ,@Field("username")username:String,@Field("name")name:String
                           ,@Field("phonenumber")phonenumber:String,@Field("address")address:String
                           ,@Field("aboutyou")aboutyou:String
                           ,@Field("profilepic")profilepic:String):ResultModel

    //Gets the topcooks
    @POST("gettopratingcooksoftheweek")
    suspend fun gettopratingcooks():TopCooks

    //Gets toprating foods
    @FormUrlEncoded
    @POST("gettopratingfoods")
    suspend fun gettopratingfoods(@Field("pagenumber")pagenumber:Int): Getfoods

    //gets toprating Vegfoods
    @FormUrlEncoded
    @POST("gettopratingVegfoods")
    suspend fun gettopratingVegfoods(@Field("pagenumber")pagenumber:Int):Getfoods

     @FormUrlEncoded
     @POST("getserchedcookstoprating")
     suspend fun serchcooks(@Field("username")username:String?,@Field("pagenumber")pagenumber:Int):SearchCooksModel

     @FormUrlEncoded
     @POST("getserchedfoodstoprating")
     suspend fun serchfoods(@Field("foodstitle")foodstitle:String?,@Field("pagenumber")pagenumber:Int):SearchFoodModel

     @FormUrlEncoded
     @POST("adduserrecipe")
     suspend fun adduserrecipe(@Field("username") username :String,@Field("foodtitle")foodtitle:String
                               ,@Field("category")category:String,@Field("image")image:String
                               ,@Field("description")description:String):AddRecipeModel

    @FormUrlEncoded
    @POST("getmyrecipes")
    suspend fun getmyrecipes(@Field("username")username:String,@Field("pagenumber")pagenumber:Int):Getfoods

    @FormUrlEncoded
    @POST("getcooksfoodlist")
    suspend fun getcooksfoodlist(@Field("username")username:String?,@Field("pagenumber")pagenumber:Int):SearchFoodModel

    @FormUrlEncoded
    @POST("getfooddetails")
    suspend fun getfooddetails(@Field("username")username:String?,@Field("foodid")foodid:String,
                               @Field("currentusername")currentusername:String):EpicFoodDetails

    @FormUrlEncoded
    @POST("submitrating")
    suspend fun submitrating(@Field("rating")rating:Float,@Field("username")username:String,
                             @Field("foodid")foodid:String):ResultModel

     @FormUrlEncoded
     @POST("submitlike")
     suspend fun submitlike(@Field("like")like:Int,@Field("username")username:String,
                            @Field("foodid")foodid:String):ResultModel

    @FormUrlEncoded
    @POST("deleterecipe")
    suspend fun deleterecipe(@Field("category")category: String?,@Field("username")username:String?,
                             @Field("foodid")foodid:String?):ResultWFoodId

   @FormUrlEncoded
   @POST("updaterecipe")
   suspend fun updaterecipe(@Field("username")username:String,@Field("foodtitle")foodtitle:String
                            ,@Field("category")category: String,@Field("image")image:String,
                            @Field("ingredients")ingredients:String,
                            @Field("description")description: String,@Field("foodid")foodid:String,
                            @Field("newcategory")newcategory:String,@Field("likes")likes:String,
                            @Field("rating")rating:String):ResultWFoodId

   @FormUrlEncoded
   @POST("googlelogin")
   suspend fun googlelogin(@Field("idtoken") idtoken: String?): LoginModel

   @FormUrlEncoded
   @POST("checkingversion")
   suspend fun checkingversion(@Field("versioncode") versioncode:Int?): ResultModel
    @FormUrlEncoded
    @POST("getdataforupdatingprofile")
    suspend fun getdataforupdatingprofile(@Field("username") username:String?):ProfilUpdate
    @FormUrlEncoded
    @POST("updatingprofile")
    suspend fun updatingprofile(@Field("emailid") emailid:String?
                                ,@Field("username") username:String?
                                ,@Field("name") name:String?
                                ,@Field("phone") phone:String?
                                ,@Field("locations") locations:String?
                                ,@Field("aboutyou") aboutyou:String?
                                ,@Field("profilepic") profilepic:String?
                                ,@Field("facebookid") facebookid:String?,
                                @Field("instaid") instaid:String?):ResultModel

}