package com.nads.epicureapp.ui.homepage.profiledir

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.nads.epicureapp.R
import com.nads.epicureapp.databinding.FragmentSettingsBinding
import com.nads.epicureapp.model.apiservice.datamodels.ResultModel
import com.nads.epicureapp.ui.homepage.HomeViewModel
import com.nads.epicureapp.ui.homepage.model.ProfilUpdate
import kotlinx.android.synthetic.main.fragment_settings.view.*
import java.io.ByteArrayOutputStream


class Settings : Fragment() {
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.userprimary_navigation) { defaultViewModelProviderFactory }
    lateinit var named:EditText
    lateinit var phone:EditText
    lateinit var postalcode:EditText
    lateinit var aboutyou:EditText
    lateinit var facebookid:EditText
    lateinit var instaid:EditText
    lateinit var profileimag:ImageView
    lateinit var proimagebutton:Button
    lateinit var submitbutton:Button
    lateinit var loadprogress:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.homeviewmodel = homeViewModel
        binding.lifecycleOwner= viewLifecycleOwner
        intiviews(binding)
        submitviews(binding)
        homeViewModel.epicureupdateprofile.value = ResultModel(null)

     val getdata = Observer<ResultModel> {
         it->
         if(it.status == true){
             loadprogress.isVisible = false
             binding.root.findNavController().navigate(R.id.action_settings_dest_to_homeFragment3)
             Toast.makeText(requireActivity(),"Submitted Successfully",Toast.LENGTH_LONG).show()
         }
         else if (it.status == false){
             loadprogress.isVisible = false
             binding.root.findNavController().navigate(R.id.action_settings_dest_to_homeFragment3)
             Toast.makeText(requireActivity(),"Error In Data",Toast.LENGTH_LONG).show()
         }
     }
     homeViewModel.epicureupdateprofile.observe(viewLifecycleOwner,getdata)
        return binding.root
    }





    fun intiviews(binding:FragmentSettingsBinding){
        named = binding.root.findViewById(R.id.editnamed)
        phone = binding.root.findViewById(R.id.editphoned)
        postalcode = binding.root.findViewById(R.id.updatepostalcode)
        aboutyou = binding.root.findViewById(R.id.updateaboutyou)
        facebookid = binding.root.findViewById(R.id.updatefacebookid)
        instaid = binding.root.findViewById(R.id.updateinstagramid)
        profileimag = binding.root.findViewById(R.id.updateprofileimage)
        proimagebutton = binding.root.findViewById(R.id.editprofileimageupdate)
        loadprogress = binding.root.detailsprogressBareditdetails
        submitbutton  = binding.root.findViewById(R.id.updateprosubmit)
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.epicureapp_file_key), Context.MODE_PRIVATE)
        val username = sharedPref?.getString("username", null)
        val getdataupdating = Observer<ProfilUpdate>{
                it->
            named.setText(it.name.toString())
            phone.setText( it.phone.toString())
            postalcode.setText(it.locations.toString())
            aboutyou.setText(it.aboutyou.toString())
            facebookid.setText(it.facebook.toString())
            instaid.setText(it.instagram.toString())
            loadprogress.isVisible = false
            val foodlistimage = Base64.decode(it.profilepic, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(foodlistimage, 0, foodlistimage.size)
            Glide.with(this.requireActivity()).load(decodedImage).fitCenter().into(profileimag)
        }
        homeViewModel.getdataforupdatingprofile(username)
        loadprogress.isVisible = true
        loadprogress.progress
        homeViewModel.epicuregetdataupdatingprofile.observe(viewLifecycleOwner,getdataupdating)


    }
    //==================================================
    fun submitviews(binding:FragmentSettingsBinding){
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.epicureapp_file_key), Context.MODE_PRIVATE)
        val username = sharedPref?.getString("username", null)
        val emailid = sharedPref?.getString("emailid", null)
        initimagebutton(proimagebutton)
        submitbutton.setOnClickListener(View.OnClickListener {
            it->
            loadprogress.isVisible = true
            loadprogress.progress
            val bitmap = profileimag.drawable.toBitmap()
            val bytearrystream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytearrystream)
            val bb = bytearrystream.toByteArray()
            val imaglength = bb.size
            val usernamereg = Regex("^[A-Za-z]\\w{5,20}$")
            val nameb = Regex("^[A-Za-z\\s0-9]*$")
            val facebookvalidater = Regex("^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
                    "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
                    "([).!';/?:,][[:blank:]])?$")
            val phonnumberpattern = Regex("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$")
            val reg = Regex("^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$")
            if (username.toString().matches(usernamereg)){
                if (postalcode.text.toString().length in 4..100) {
                    if(phone.text.toString().length in 5..20){
                        if (named.text.toString().matches(nameb)) {
                            if (aboutyou.text.toString().isNotEmpty()) {
                                if(facebookid.text.toString().matches(facebookvalidater)|| facebookid.text.toString().isEmpty()){
                                    if (instaid.text.toString().matches(facebookvalidater)|| instaid.text.toString().isEmpty()) {
                                        if (imaglength < 100000){
                                        homeViewModel.updatingprofile(
                                            emailid.toString(),
                                            username.toString(),
                                            named.text.toString(),
                                            phone.text.toString(),
                                            postalcode.text.toString(),
                                            aboutyou.text.toString(),
                                            profileimag.drawable,
                                            facebookid.text.toString(),
                                            instaid.text.toString()
                                        )}
                                        else{
                                            Toast.makeText(requireActivity(),"Image Size should be under .1 Mb and a valid image",Toast.LENGTH_LONG).show()
                                            loadprogress.isVisible = false
                                        }
                                    }else{
                                        Toast.makeText(requireActivity(), "The content is invalid it should be valid instaid", Toast.LENGTH_LONG).show()
                                        loadprogress.isVisible = false
                                    }
                                }else{
                                    Toast.makeText(requireActivity(), "The content is invalid it should be valid facebookid", Toast.LENGTH_LONG).show()
                                    loadprogress.isVisible = false
                                }
                            }
                            else{
                                Toast.makeText(requireActivity(), "The content is invalid it should be under 100 words", Toast.LENGTH_LONG).show()
                                loadprogress.isVisible = false
                            }

                        }
                        else{
                            Toast.makeText(requireActivity(), "The username is invalid letters should be between 6 to 20 only use valid letters and numbers"
                                , Toast.LENGTH_LONG).show()
                            loadprogress.isVisible = false
                        }

                    }
                    else{
                        Toast.makeText(requireActivity(), "Invalid phone number", Toast.LENGTH_LONG).show()
                        loadprogress.isVisible = false
                    }
                }
                else{
                    Toast.makeText(requireActivity(), "Invalid pincode", Toast.LENGTH_LONG).show()
                    loadprogress.isVisible = false
                }
            }
            else {
                val usernamefortoast = username.toString()
                Toast.makeText(requireActivity(),
                    "The username $usernamefortoast is invalid letters should be between 6 to 20 only use valid letters "
                    , Toast.LENGTH_LONG).show()
                loadprogress.isVisible = false
            }



        })


    }
    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1212;
        //Permission code
        private val PERMISSION_CODE = 1213;
    }
    fun initimagebutton(imagebutton:Button){
        imagebutton.setOnClickListener {
            //check runtime permission
            if (this.requireActivity().checkSelfPermission
                    (Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
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
            Glide.with(requireActivity()).load(data?.data).into(profileimag)
        }
    }

}