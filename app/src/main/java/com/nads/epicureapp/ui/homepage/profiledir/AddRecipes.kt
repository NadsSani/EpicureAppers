package com.nads.epicureapp.ui.homepage.profiledir

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.nads.epicureapp.R
import com.nads.epicureapp.databinding.FragmentAddRecipesBinding
import com.nads.epicureapp.model.apiservice.datamodels.AddRecipeModel
import com.nads.epicureapp.ui.homepage.HomeViewModel
import kotlinx.android.synthetic.main.fragment_add_recipes.view.*
import java.io.ByteArrayOutputStream


class AddRecipes : Fragment() {

    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.userprimary_navigation) { defaultViewModelProviderFactory }

    lateinit var foodimage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val binding: FragmentAddRecipesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_recipes, container, false)
        binding.homeviewmodel = homeViewModel
        binding.lifecycleOwner= viewLifecycleOwner
        initspinnervegornonveg(binding)
        homeViewModel.epicuresubmitreciperesult.value = AddRecipeModel(null,"")
        val foodtitle = binding.root.findViewById<EditText>(R.id.editfoodtitle)
        val typeoffood = binding.root.findViewById<Spinner>(R.id.editvegornonveg)
        foodimage = binding.root.findViewById<ImageView>(R.id.imageView3)
        val description=binding.root.findViewById<EditText>(R.id.editdescription)
        val imagebutton=binding.root.findViewById<Button>(R.id.imagebuttonrecipe)
        val submitbutton = binding.root.findViewById<Button>(R.id.addrecipesbutton)
        val progressBar = binding.root.submitrecipeprogressbar
        submitbutton.setOnClickListener {
            progressBar.isVisible= true
            progressBar.progress
            submitvalues(binding,foodtitle,typeoffood,foodimage,description,progressBar)
        }
        initimagebutton(imagebutton)

        val submitresult = Observer<AddRecipeModel?>{ newvale ->
            if (newvale?.status != null) {
                if (newvale.status!!){
                    progressBar.isVisible = false
                    Toast.makeText(requireActivity(),"The Recipe Have Been Submitted",Toast.LENGTH_LONG).show()

                    binding.root.findNavController().navigate(R.id.action_addRecipes_to_homeFragment3)

                } else if (newvale?.pay == "needpayment"){

                } else if (newvale?.pay == "exist"){

                } else if (newvale?.pay == "limit") {

                } else{
                    Toast.makeText(requireActivity(),"There Recepie already exists with this name Or Have Reached the limits",Toast.LENGTH_LONG).show()
                    progressBar.isVisible = false
                }
            }

        }

        homeViewModel.epicuresubmitreciperesult.observe(viewLifecycleOwner,submitresult)


        return binding.root
    }


fun submitvalues(
    binding:FragmentAddRecipesBinding,foodtitle:EditText,typeffood:Spinner,foodimage:ImageView,description:EditText,progressBar: ProgressBar)
{
    val bitmap = foodimage.drawable.toBitmap()
    val bytearrystream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytearrystream)
    val bb = bytearrystream.toByteArray()
    val imaglength = bb.size
    val foodtitlereg = Regex("^[A-Za-z\\s0-9]*$")
    val titlefoodie = foodtitle.text.toString()
    val descripfoodie = description.text.toString()
       if (/*titlefoodie.matches(foodtitlereg) &&*/
           titlefoodie.length in 6..29 && titlefoodie.isNotEmpty()){
           if (typeffood.selectedItem.toString() != "Select Category"){
               if(/*descripfoodie.matches(foodtitlereg) &&*/
                   descripfoodie.length in 11..99 &&
                   descripfoodie.isNotEmpty()){
                       if (imaglength <  100000){
                   //==============================================
                   val sharedPref = activity?.getSharedPreferences(
                       getString(R.string.epicureapp_file_key), Context.MODE_PRIVATE)
                   val username = sharedPref?.getString("username", null)
                   if (username != null) {

                       homeViewModel.submitvaluesforrecipes(
                           username,
                           foodtitle.text.toString(),
                           typeffood.selectedItem.toString(),
                           foodimage.drawable,
                           description.text.toString()
                       )
                   }

                   else{
                       progressBar.isVisible = false
                       Toast.makeText(requireActivity(),"Should Logout and login again",Toast.LENGTH_LONG).show()
                   }
                       }else{
                           Toast.makeText(requireActivity(),"Image Size should be under .1 Mb and a valid image",Toast.LENGTH_LONG).show()
                           progressBar.isVisible = false
                       }
                   //========================================================
                   }
               else{
                   progressBar.isVisible = false
                   Toast.makeText(requireActivity(),
                       "valid description Should be filled words and spaces only allowed and 11 to 99 characters required"
                       ,Toast.LENGTH_LONG).show()
                   }
           }
           else{
               progressBar.isVisible = false
               Toast.makeText(requireActivity(),"Category Should be selected",Toast.LENGTH_LONG).show()
           }
       }else{
           progressBar.isVisible = false
         Toast.makeText(requireActivity(),"Valid Title Should be filled",Toast.LENGTH_LONG).show()

       }

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
                requestPermissions(permissions,PERMISSION_CODE);
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
        startActivityForResult(intent,IMAGE_PICK_CODE)
    }
    fun initspinnervegornonveg(binding:FragmentAddRecipesBinding){
        val s = binding.root.findViewById(R.id.editvegornonveg) as Spinner
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.Typeoffd,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            s.adapter = adapter
        }
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
            Glide.with(requireActivity()).load(data?.data).into(foodimage)
        }
    }




}