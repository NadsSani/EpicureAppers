package com.nads.epicureapp.ui.homepage.profiledir

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
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
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.nads.epicureapp.R
import com.nads.epicureapp.databinding.FragmentEditRecipesBinding
import com.nads.epicureapp.ui.homepage.FoodDetailsArgs
import com.nads.epicureapp.ui.homepage.HomeViewModel
import com.nads.epicureapp.ui.homepage.model.EpicFoodDetails
import com.nads.epicureapp.ui.homepage.profiledir.models.ResultWFoodId
import kotlinx.android.synthetic.main.fragment_edit_recipes.view.*
import java.io.ByteArrayOutputStream


class EditRecipes : Fragment() {

    lateinit var foodimage: ImageView
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.userprimary_navigation) { defaultViewModelProviderFactory }
    val args: FoodDetailsArgs by navArgs()
    lateinit var foodtitle:EditText
    lateinit var typeoffood:Spinner
    lateinit var ingredients:EditText
    lateinit var description:EditText
    lateinit var imagebutton:Button
    lateinit var submitbutton:Button
    lateinit var newcategory:String
    lateinit var likes:String
    lateinit var rating:String
    lateinit var editreciprogressbar:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentEditRecipesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_recipes, container, false)
        binding.homeviewmodel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val userid = args.userid
        val foodid = args.foodid
        val username =args.username
        initspinnervegornonveg(binding)
        initviews(binding)
       homeViewModel.epicureeditrecipe.value = ResultWFoodId(null,null)
        foodtitle = binding.root.findViewById<EditText>(R.id.updatefoodtitle)
        typeoffood = binding.root.findViewById<Spinner>(R.id.updatevegornonveg)
        foodimage = binding.root.findViewById<ImageView>(R.id.updateimageView3)
       // ingredients=binding.root.findViewById<EditText>(R.id.updateingredients)
        description=binding.root.findViewById<EditText>(R.id.updatedescription)
        imagebutton=binding.root.findViewById<Button>(R.id.updateimagebuttonrecipe)
        submitbutton = binding.root.findViewById<Button>(R.id.updaterecipesbutton)
        editreciprogressbar = binding.root.editrecipeprogressbar
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.epicureapp_file_key), Context.MODE_PRIVATE)
        val currentusername = sharedPref?.getString("username", null)
        if (username != null && currentusername!=null) {
            homeViewModel.getfooddetails(username,foodid,currentusername)
            editreciprogressbar.isVisible = true
            editreciprogressbar.progress
        }
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


        val getdetails = Observer<EpicFoodDetails> { newName ->
            val newval = newName.datavalues.get(0)
            foodtitle.setText(newval.title)
            newcategory = newval.TypeVegorNon.toString()
            if (newval.TypeVegorNon.toString() == "Vegetarian"){
               typeoffood.setSelection(1)
            }else{
                typeoffood.setSelection(2)
            }
            likes = newval.likes.toString()
            rating = newval.rating.toString()
           // ingredients.setText(newval.ingredients)
            description.setText(newval.descrip)
            val foodlistimage = Base64.decode(newval.imageurl, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(foodlistimage, 0, foodlistimage.size)
            Glide.with(this.requireActivity()).load(decodedImage).fitCenter().into(foodimage)
            editreciprogressbar.isVisible = false
            if (username != null) {
                submition(binding ,username,foodid)
            }

        }
        val getvalues = Observer<ResultWFoodId> {
            it->
            if (it.status == true){
                Toast.makeText(this.requireActivity(),"Values Submitted Successfully",Toast.LENGTH_LONG).show()
               binding.root.findNavController().navigate(R.id.action_editRecipes_to_somePage2)
            }
            else if (it.status == false){
                Toast.makeText(this.requireActivity(),"Recipe With This Name Already Exists",Toast.LENGTH_LONG).show()
                binding.root.findNavController().navigate(R.id.action_editRecipes_to_somePage2)
            }
        }

        homeViewModel.epicFoodDetails.observe(viewLifecycleOwner,getdetails)
       homeViewModel.epicureeditrecipe.observe(viewLifecycleOwner,getvalues)




        return binding.root
    }



  fun initviews(binding:FragmentEditRecipesBinding){


  }



 fun submition(binding: FragmentEditRecipesBinding,username:String,foodid:String){

   submitbutton.setOnClickListener {
       editreciprogressbar.isVisible = true
       editreciprogressbar.progress
       val foodtitlete = foodtitle.text.toString()
       val typeofveg = typeoffood.selectedItem.toString()
       val ingredien = ""//ingredients.text.toString()
       val descrip = description.text.toString()
       val foodtitlereg = Regex("^[A-Za-z\\s0-9]*$")
       val bitmap = foodimage.drawable.toBitmap()
       val bytearrystream = ByteArrayOutputStream()
       bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytearrystream)
       val bb = bytearrystream.toByteArray()
       val imaglength = bb.size
       if (/*foodtitlete.matches(foodtitlereg) && */foodtitlete.length in 6..29){
           if (typeofveg != "Select Category"){
               if(/*descrip.matches(foodtitlereg) &&*/descrip.length in 11..99 ){
                       if (imaglength < 100000){
                   //==============================================
                       homeViewModel.updaterecipe(username,foodtitlete,
                           typeofveg,foodimage.drawable,ingredien,descrip,foodid,
                           newcategory,likes,rating)
                   //========================================================
               }
                       else{
                           Toast.makeText(requireActivity(),"Image Size should be under .1 Mb and a valid image",Toast.LENGTH_LONG).show()
                           editreciprogressbar.isVisible = false
                       }
               }
               else{
                 //  progressBar.isVisible = false
                   Toast.makeText(requireActivity(),
                       "valid description Should be filled words and spaces only allowed"
                       ,Toast.LENGTH_LONG).show()
                   editreciprogressbar.isVisible = false
               }
           }
           else{
              // progressBar.isVisible = false
               Toast.makeText(requireActivity(),"Category Should be selected",Toast.LENGTH_LONG).show()
               editreciprogressbar.isVisible = false
           }
       }else{
          // progressBar.isVisible = false
           Toast.makeText(requireActivity(),"Valid Title Should be filled",Toast.LENGTH_LONG).show()
           editreciprogressbar.isVisible = false
       }

       // homeViewModel.updaterecipe(username,foodtitlete,typeofveg,foodimage.drawable,ingredien,descrip,foodid,newcategory,likes,rating)
     }
 }


    fun initspinnervegornonveg(binding: FragmentEditRecipesBinding){
        val s = binding.root.findViewById(R.id.updatevegornonveg) as Spinner
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.Typeoffd,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            s.adapter = adapter
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
            Glide.with(requireActivity()).load(data?.data).into(foodimage)
            //foodimage.setImageURI(data?.data)
        }
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1212;
        //Permission code
        private val PERMISSION_CODE = 1213;

    }
}