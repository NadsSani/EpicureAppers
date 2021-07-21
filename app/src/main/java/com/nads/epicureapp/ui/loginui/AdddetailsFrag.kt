package com.nads.epicureapp.ui.loginui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.nads.epicureapp.R
import com.nads.epicureapp.databinding.FragmentAdddetailsBinding
import com.nads.epicureapp.databinding.FragmentConfirmationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class AdddetailsFrag : Fragment() {
    private val loginViewModel:LoginViewModel by navGraphViewModels(R.id.user_login_navigation){defaultViewModelProviderFactory}
    lateinit var profileimage:ImageView
    lateinit var imge: Drawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val binding : FragmentAdddetailsBinding =
            DataBindingUtil.inflate(inflater,R.layout.fragment_adddetails, container, false)
        binding.loginviewmodel= loginViewModel
        binding.lifecycleOwner= viewLifecycleOwner
        //Need To secure this data by  https://developer.android.com/topic/security/data
        val sharedPref = this.requireActivity().getSharedPreferences(
            getString(R.string.epicureapp_file_key), Context.MODE_PRIVATE)
        val emailaddresspref = sharedPref.getString("emailid", "")
        val userid = sharedPref.getString("userid", "")
        val username: EditText = binding.root.findViewById(R.id.add_details_username)
        val name: EditText = binding.root.findViewById(R.id.adddetails_nametext)
        val phonenumber: EditText = binding.root.findViewById(R.id.add_details_phone)
        val address: EditText = binding.root.findViewById(R.id.add_details_postal_address)
        val aboutyou: EditText = binding.root.findViewById(R.id.add_details_about_you)
        profileimage = binding.root.findViewById(R.id.add_details_profile_pic)
        val submitbutton: Button = binding.root!!.findViewById(R.id.submit_add_details_button)
      val loadbar: ProgressBar = binding.root!!.findViewById(R.id.detailsprogressBar)
        val imagebutton:Button = binding.root!!.findViewById(R.id.add_details_profile_pic_upload)

        var getlues = Observer<Boolean> { newName ->

            var ger:Boolean = newName
            if (ger == true){
                val sharedPref = this.requireActivity().getSharedPreferences(
                    getString(R.string.epicureapp_file_key), Context.MODE_PRIVATE)
               loadbar.isVisible = false
                with (sharedPref.edit()) {
                    putString("username",username.text.toString() )
                    commit()
                }
                binding.root.findNavController().navigate(R.id.action_adddetailsFrag_to_mainActivity)
            }
            else{

                requireActivity().finish()
            }
        }
        loginViewModel.epicureadddetails.observe(viewLifecycleOwner,getlues)
        //pictureuploadbutton
        imagebutton.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (this.requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }

       
        submitbutton.setOnClickListener(View.OnClickListener{
            it->
            val bitmap = profileimage.drawable.toBitmap()
            val bytearrystream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytearrystream)
            val bb = bytearrystream.toByteArray()
            val imaglength = bb.size
            val usernamereg = Regex("^[A-Za-z]\\w{5,20}$")
            val phonnumberpattern = Regex("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$")
            val reg = Regex("^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$")
            loadbar.isVisible = true
            if (username.text.toString().matches(usernamereg)){
                if (address.text.toString().length in 4..100) {
                    if(phonenumber.text.toString().length in 8..30){
                        if (name.text.toString().matches(usernamereg)) {
                            if (aboutyou.text.toString().isNotEmpty()) {
                                if (imaglength < 100000){
                                Log.e("GETHERE","GOTHERE")
                                senddetails(emailaddresspref.toString(),userid.toString(),
                                    username.text.toString(),name.text.toString(),phonenumber.text.toString(),
                                    address.text.toString(),aboutyou.text.toString(),profileimage.drawable,loadbar)
                            }
                                else{
                                    Toast.makeText(requireActivity(),"Image Size should be under .1 Mb and a valid image",Toast.LENGTH_LONG).show()
                                    loadbar.isVisible = false
                                }
                            }
                            else{
                                Toast.makeText(requireActivity(), "The content is invalid it should be under 100 words", Toast.LENGTH_LONG).show()
                                loadbar.isVisible = false
                            }

                        }
                        else{
                            Toast.makeText(requireActivity(), "The username is invalid letters should be between 6 to 20 only use valid letters and numbers"
                                , Toast.LENGTH_LONG).show()
                            loadbar.isVisible = false
                        }

                    }
                    else{
                        Toast.makeText(requireActivity(), "Invalid phone number", Toast.LENGTH_LONG).show()
                        loadbar.isVisible = false
                    }
                }
                else{
                    Toast.makeText(requireActivity(), "Invalid pincode", Toast.LENGTH_LONG).show()
                    loadbar.isVisible = false
                }
            }
            else {
                val usernamefortoast = username.text.toString()
                Toast.makeText(requireActivity(),
                    "The username $usernamefortoast is invalid letters should be between 6 to 20 only use valid letters "
                    , Toast.LENGTH_LONG).show()
                loadbar.isVisible = false
            }

    })
        return binding.root
    }






    fun senddetails(email:String,userid:String,username:String,name:String,phonenumber:String
                    ,address:String,about:String,profilimag:Drawable,loadbar:ProgressBar){
        loadbar.isVisible=true
       loadbar.progress
        if(email.isEmpty() || userid.isEmpty() || username.isEmpty() ||
            phonenumber.isEmpty() || address.isEmpty())
        {
           loadbar.isVisible = false

        }else{
            Log.e("ErrorGotten",about+address+phonenumber)
        loginViewModel.senddetailsofuser(email,userid,
            username,name,phonenumber,
            address,about,profilimag)
       }
    }








    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1212;
        //Permission code
        private val PERMISSION_CODE = 1213;
    }


    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this.requireActivity(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            Glide.with(requireActivity()).load(data?.data).into(profileimage)
        }
    }
}
