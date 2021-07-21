package com.nads.epicureapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.nads.epicureapp.ui.loginui.LoginFragment
import dagger.hilt.android.AndroidEntryPoint


//EpicureApp Get your backend server's OAuth 2.0 client ID
//731246938497-afh3tp1nh0coo6kqlpqt4unonotq1b1i.apps.googleusercontent.com
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    override fun onStart() {
        super.onStart()
//        val action: String? = intent?.action
//        val data: Uri? = intent?.data
//        val emailid = data?.getQueryParameter("emailid")
//
//        if(data?.getQueryParameter("emailid") != null){
//            Log.e("deeplinkintent",data.getQueryParameters("emailid").toString())
//        //findNavController(R.id.user_login_navigation).navigate(R.id.confirmationFragment)
//
//    }
    }
}