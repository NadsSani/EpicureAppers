package com.nads.epicureapp.model.repositories


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.nads.epicureapp.model.apiservice.Epicureservices
import com.nads.epicureapp.model.apiservice.datamodels.AddRecipeModel
import com.nads.epicureapp.model.apiservice.datamodels.LoggedUser
import com.nads.epicureapp.model.apiservice.datamodels.ResultModel
import com.nads.epicureapp.model.apiservice.datamodels.SplashData
import com.nads.epicureapp.ui.homepage.model.*
import com.nads.epicureapp.ui.homepage.profiledir.models.ResultWFoodId
import com.nads.epicureapp.ui.loginui.loginmodels.LoginModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpicureRepository @Inject constructor(private val epicureservices: Epicureservices) {

    val epicuresplashvid: LiveData<SplashData> = liveData {
        withContext(Dispatchers.IO) {
            val data = epicureservices.getsplashvid()
            emit(data)
            Log.e("EnteredRepSplashvid", data.toString())
        }
    }
    suspend fun submitratings(rating:Float,username:String,foodid: String): ResultModel {
        return epicureservices.submitrating(rating,username,foodid)
    }
    suspend fun submitlike(like:Int,username:String,foodid: String): ResultModel {
        return epicureservices.submitlike(like,username,foodid)
    }
    suspend fun signuper(username: String, password: String):ResultModel{
        return epicureservices.signup(username, password)
    }
    suspend fun topcooks():TopCooks{
        return epicureservices.gettopratingcooks()
    }
    suspend fun getfoods(pagenumber:Int): Getfoods {
      return epicureservices.gettopratingfoods(pagenumber)

    }
    suspend fun serchfoods(foodstitle: String?,pagenumber: Int): SearchFoodModel {
      return  epicureservices.serchfoods(foodstitle,pagenumber)
    }
    suspend fun serchcooks(username: String?,pagenumber: Int):SearchCooksModel{
       return epicureservices.serchcooks(username,pagenumber)
    }
    suspend fun getcooksfoodlist(username:String?,pagenumber: Int):SearchFoodModel{
        return epicureservices.getcooksfoodlist(username,pagenumber)
    }
    suspend fun getVegfoods(pagenumber:Int): Getfoods {
        return epicureservices.gettopratingVegfoods(pagenumber)
    }
    suspend fun logier(username: String, password: String): LoggedUser{
        return epicureservices.login(username, password)
    }


    suspend fun sendconfirm(emailid:String,password:String,otp:String): ResultModel {
        return epicureservices.sendconfirmotp(emailid,password,otp)
    }

    suspend fun senddetailsofuser(emailaddresspref:String,userid:String,username:String,name:String,phonenumber:String,address:String,aboutyou:String,proimage:String): ResultModel? {
        return epicureservices.adddetails(emailaddresspref,userid,username,name,phonenumber,address,aboutyou,proimage)
    }

    suspend fun addrecipesrep(username:String,foodtitle:String,category:String,image:String,description:String): AddRecipeModel{
        return epicureservices.adduserrecipe(username,foodtitle,category,image,description)
    }

     suspend fun getmyrecipe(username: String,nextpagenumber:Int): Getfoods {
         return epicureservices.getmyrecipes(username,nextpagenumber)
     }

    suspend fun getfooddetails(username: String,foodid:String,currentusername:String): EpicFoodDetails {
        return epicureservices.getfooddetails(username,foodid,currentusername)
    }

    suspend fun deleterecipe(category: String?,username: String?,foodid:String?): ResultWFoodId {
        return epicureservices.deleterecipe(category,username,foodid)
    }

    suspend fun updaterecipe(username: String,foodtitle: String
                             ,category: String,image: String,
                             ingredients: String,
                             description: String,
                             foodid: String,newcategory: String
                             ,likes: String,rating:String): ResultWFoodId {
        return epicureservices.updaterecipe(username,foodtitle,category, image, ingredients
            , description, foodid, newcategory, likes, rating)
    }


   suspend fun googlelogin(idtoken: String?): LoginModel {
        return epicureservices.googlelogin(idtoken)
    }
    suspend fun checkingversion(versioncode:Int):ResultModel{
        return epicureservices.checkingversion(versioncode)
    }
    suspend fun getdataforupdatingprofile(username:String?):ProfilUpdate{
        return epicureservices.getdataforupdatingprofile(username)
    }
    suspend fun updatingprofile(emailid:String,username:String,name:String?,
                                phone:String?,
                                locations:String?,aboutyou:String?
                                ,profilepic:String?, facebookid:String?
                                ,instaid:String?):ResultModel{
        return epicureservices.updatingprofile(emailid,username,name,phone,
            locations,aboutyou,profilepic, facebookid,instaid)
    }

}









