package com.nads.epicureapp.ui.loginui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.nads.epicureapp.R
import com.nads.epicureapp.databinding.FragmentConfirmationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmationFragment : Fragment() {
    private val loginViewModel:LoginViewModel by navGraphViewModels(R.id.user_login_navigation){defaultViewModelProviderFactory}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentConfirmationBinding =
            DataBindingUtil.inflate(inflater,R.layout.fragment_confirmation, container, false)
        binding.loginviewmodel= loginViewModel
        binding.lifecycleOwner= viewLifecycleOwner

        //viewmodel accessing
        var getlues = Observer<Boolean> { newName ->

            var ger:Boolean = newName
            if (ger == true){
               binding.root.findNavController().navigate(R.id.action_confirmationFragment_to_adddetailsFrag)
            }
            else{
                Toast.makeText(this.requireActivity(),"Error Invalid OTP ", Toast.LENGTH_LONG).show()
            }
        }
        loginViewModel.epicureconfirm.observe(viewLifecycleOwner,getlues )

        //Need To secure this data by  https://developer.android.com/topic/security/data
        val sharedPref = this.requireActivity().getSharedPreferences(
            getString(R.string.epicureapp_file_key), Context.MODE_PRIVATE)
        val emailaddresspref = sharedPref.getString("emailid", "emailaddress")
        val passwordpref = sharedPref.getString("password", "somepassword")
        val otpcode: EditText = binding.root!!.findViewById(R.id.otpcode)
        val confirmbutton: Button = binding.root!!.findViewById(R.id.confirm_button)
        val loadbar: ProgressBar = binding.root!!.findViewById(R.id.confirmprogressBar)





        //Back button
       requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        binding.root.findNavController().popBackStack()
        binding.root.findNavController().popBackStack()
        binding.root.findNavController().popBackStack()
        binding.root.findNavController().navigate(R.id.signupFragment)
        }

        return binding.root
    }


}