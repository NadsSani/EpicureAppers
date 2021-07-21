package com.nads.epicureapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.nads.epicureapp.model.apiservice.datamodels.ResultModel
import com.nads.epicureapp.model.repositories.EpicureRepository
import com.nads.epicureapp.splashscreen.SplashViewModel
import com.nads.epicureapp.ui.homepage.HomeFragment
import com.nads.epicureapp.ui.loginui.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreen : AppCompatActivity(){
    private lateinit var binding: com.nads.epicureapp.databinding.ActivitySplashScreenBinding
    private val SPLASH_TIME_OUT:Long = 5000
    private val loginViewModel:LoginViewModel by viewModels()
    @Inject lateinit var epicureRepository: EpicureRepository

    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = com.nads.epicureapp.databinding.ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    //======================================================================
        //Checking version of the Application
        try {
            val pInfo: PackageInfo =
                getPackageManager().getPackageInfo(getPackageName(), 0)
            val version: String = pInfo.versionName
            val versioncode: Int = pInfo.versionCode
            Log.e("Code",version +"\n"+ versioncode )
            splashViewModel.checkversion(versioncode)
            val checkversionio = Observer<ResultModel> {
                it->
                if (it.status == true){
                    Toast.makeText(this,"Newest Version",Toast.LENGTH_LONG).show()
                    startintro(binding)
                }
                else{
                    val builder = AlertDialog.Builder(this)
                    //set title for alert dialog
                    builder.setTitle("OLD VERSION")
                    //set message for alert dialog
                    builder.setMessage("PLEASE UPDATE TO NEW VERSION ")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                    //performing cancel action
                    builder.setNeutralButton("OK"){dialogInterface , which ->
                        finish()
                    }
                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
            }
            splashViewModel.checkingdata.observe(this,checkversionio)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


    }



//=========================================================================


    fun startintro(binding:com.nads.epicureapp.databinding.ActivitySplashScreenBinding){

        val account = GoogleSignIn.getLastSignedInAccount(this)

        val sharedPref = getSharedPreferences(
            getString(R.string.epicureapp_file_key), Context.MODE_PRIVATE
        )
        val emailaddresspref = sharedPref?.getString("emailid", null)
        val userid = sharedPref.getString("userid", null)
        val username = sharedPref.getString("username", null)

        if (emailaddresspref != null && userid != null && username != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else {
            Handler().postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, SPLASH_TIME_OUT)
       }
    }


}


