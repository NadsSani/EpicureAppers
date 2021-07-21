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
import com.nads.epicureapp.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private val loginViewModel:LoginViewModel by navGraphViewModels(R.id.user_login_navigation){defaultViewModelProviderFactory}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding : FragmentSignupBinding =
            DataBindingUtil.inflate(inflater,R.layout.fragment_signup, container, false)
        var getlues = Observer<Boolean> { newName ->
            // Update the UI, in this case, a TextView.
            var ger:Boolean = newName
            if (ger == true){

                val signupemailiddress: EditText = binding.root!!.findViewById(R.id.sign_up_emailid)
                val signuppassword: EditText = binding.root!!.findViewById(R.id.sign_up_password)
                //Need To secure this data by  https://developer.android.com/topic/security/data
                val sharedPref = this.requireActivity().getSharedPreferences(
                    getString(R.string.epicureapp_file_key), Context.MODE_PRIVATE)

                    with (sharedPref.edit()) {
                        putString("emailid", signupemailiddress.text.toString())
                        putString("password",signuppassword.text.toString())

                        commit()
                }

//                val signupemailaddress = signupemailiddress.text.toString()
//                val actiontoconfirm = SignupFragmentDirections.signUpToConfirm(signupemailaddress)
                binding.root.findNavController().navigate(R.id.sign_up_to_confirm)
            }
            else{
                Toast.makeText(this.requireActivity(),"Error Invalid Email id",Toast.LENGTH_LONG).show()
            }
        }

        loginViewModel.epicuresignuper.observe(viewLifecycleOwner,getlues )
        val signupemailaddress: EditText = binding.root!!.findViewById(R.id.sign_up_emailid)
        val signuppasword: EditText = binding.root!!.findViewById(R.id.sign_up_password)
        val signupbutton: Button = binding.root!!.findViewById(R.id.signupbutton)
        val loadbar: ProgressBar = binding.root!!.findViewById(R.id.signupprogressBar)
        signupbutton.setOnClickListener(View.OnClickListener {

            loadbar.isVisible=true
            loadbar.progress

//            loginViewModel.getsignupconfirmation(signupemailaddress.text.toString(),signuppasword.text.toString())

        })
        binding.loginviewmodel= loginViewModel
        binding.lifecycleOwner= viewLifecycleOwner



        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

        //Dont know why i need to use FindNavController().popBackStack() two times
           binding.root.findNavController().popBackStack()
            binding.root.findNavController().popBackStack()
            binding.root.findNavController().navigate(R.id.loginFragment)
        }


        return binding.root

    }

}