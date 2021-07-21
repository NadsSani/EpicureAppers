package com.nads.epicureapp.ui.loginui


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.lifecycle.*
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.nads.epicureapp.model.apiservice.datamodels.LoggedUser
import com.nads.epicureapp.model.repositories.EpicureRepository
import com.nads.epicureapp.ui.loginui.loginmodels.LoginModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class LoginViewModel @ViewModelInject constructor( private val epicureRepository: EpicureRepository,
                                                   @Assisted private val savedStateHandle: SavedStateHandle
):ViewModel()
{
    var epicurelogins = MutableLiveData<LoggedUser>()
    var epicuresignuper = MutableLiveData<Boolean>()
    var epicureconfirm = MutableLiveData<Boolean>()
    var epicureadddetails = MutableLiveData<Boolean>()
    var epicuresignupbutton = MutableLiveData<Boolean>()
    var epicgooglelogin = MutableLiveData<LoginModel>()
    init{
        Log.i("SplashViewModel", "SplashViewModel created!")
    }
    //for login
     fun getloginuser(username:String, password:String) {
         viewModelScope.launch{
             val data = epicureRepository.logier(username, password)
             epicurelogins.value = data
         }
     }
//for going to signup page
    fun gettosignuppage(){
    epicuresignupbutton.value = true

     }




    //send Details of user
    fun senddetailsofuser(emailaddresspref:String,userid:String,username:String,name:String,phonenumber:String,address:String,aboutyou:String,profileimage:Drawable){
        viewModelScope.launch{
            val bitmap = profileimage.toBitmap()
            val bytearrystream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytearrystream)
            val bb = bytearrystream.toByteArray()
            val proimage = Base64.encodeToString(bb, 0)

            val data = epicureRepository.senddetailsofuser(emailaddresspref,userid,username,name,
                phonenumber,address,aboutyou,proimage)
            epicureadddetails.value = data?.status!!
        }
    }
    fun googlelogin(idtoken: String?){
        viewModelScope.launch {
       epicgooglelogin.value =  epicureRepository.googlelogin(idtoken)
    }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }
}