package com.nads.epicureapp.ui.loginui

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.nads.epicureapp.R
import com.nads.epicureapp.databinding.LoginFragmentBinding
import com.nads.epicureapp.ui.loginui.loginmodels.LoginModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment:Fragment(){

    private val loginViewModel:LoginViewModel by navGraphViewModels(R.id.user_login_navigation){defaultViewModelProviderFactory}
   lateinit var loginprogress:ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding : LoginFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )
        binding.loginViewModel= loginViewModel
        binding.lifecycleOwner= viewLifecycleOwner
        loginprogress = binding.root.findViewById<ProgressBar>(R.id.loginprogressBar)
        logininit(binding)


// The methods to login using google login the viewmodel livedata is called and when changed this is called
        val googleloginvalue = Observer<LoginModel> {
            it->
           if (it.status==true && it.userid!=null && it.username != null && it.emailid != null){
               val sharedPref = this.requireActivity().getSharedPreferences(
                   getString(R.string.epicureapp_file_key), Context.MODE_PRIVATE)

               with (sharedPref.edit()) {
                   putString("emailid", it.emailid.toString())
                   putString("username",it.username.toString())
                   putString("userid",it.userid.toString())
                   commit()
               }
               loginprogress.isVisible = false
             findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
           }
           else if(it.status==false && it.userid != null && it.emailid != null){
               val sharedPref = this.requireActivity().getSharedPreferences(
                   getString(R.string.epicureapp_file_key), Context.MODE_PRIVATE)

               with (sharedPref.edit()) {
                   putString("emailid", it.emailid)
                   putString("userid",it.userid)
                   commit()
               }
               loginprogress.isVisible = false
               findNavController().navigate(R.id.action_loginFragment_to_adddetailsFrag)
               Log.e("GOOGLEE",it.username + " "+ it.emailid)
           }
            else{
               Toast.makeText(this.requireActivity(),"Error Internal",Toast.LENGTH_LONG).show()
               loginprogress.isVisible = false
               requireActivity().finish()

           }
        }
        loginViewModel.epicgooglelogin.observe(viewLifecycleOwner,googleloginvalue)



        return binding.root
    }
//=======================================================================================



    fun logininit(binding: LoginFragmentBinding){

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .requestId()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso)
         val signinbut = binding.root.findViewById<SignInButton>(R.id.sign_in_button)
        signinbut.setOnClickListener(View.OnClickListener { it ->
            loginprogress.isVisible = true
            loginprogress.progress
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            Log.e("LOGINID",account?.idToken.toString())
            loginViewModel.googlelogin(account?.idToken)

        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    companion object {
        private val RC_SIGN_IN = 1212;
    }
}