package com.nads.epicureapp.ui.homepage

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.nads.epicureapp.R
import com.nads.epicureapp.databinding.FragmentFoodDetailsBinding
import com.nads.epicureapp.model.apiservice.datamodels.ResultModel
import com.nads.epicureapp.ui.homepage.cookspages.CooksPageDirections
import com.nads.epicureapp.ui.homepage.model.EpicFoodDetails


class FoodDetails : Fragment() {

    val args: FoodDetailsArgs by navArgs()

    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.userprimary_navigation) { defaultViewModelProviderFactory }
    lateinit var textView: TextView
    lateinit var ingredients:TextView
    lateinit var description:TextView
    lateinit var imagerepicfooddetails :ImageView
    lateinit var ratingbarfood:TextView
    lateinit var likecount :TextView
    lateinit var likebutton:ImageButton
    lateinit var ratingbutton:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding:FragmentFoodDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_food_details, container, false)
        binding.homeviewmodel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val userid = args.userid
        val foodid = args.foodid
        val username = args.username
        val opencooksbutton = args.opencooksbutton
        if (opencooksbutton=="opencooksbutton"){
           val cooksopenpage = binding.root.findViewById<TextView>(R.id.cookspagebutton)
            cooksopenpage.visibility = View.VISIBLE
            cooksopenpage.setOnClickListener(View.OnClickListener {
                it->
               val action = FoodDetailsDirections.actionFoodDetailsToCooksPage(userid,foodid,username)
                it.findNavController().navigate(action)
            })
        }
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.epicureapp_file_key), Context.MODE_PRIVATE)
        val currentusername = sharedPref?.getString("username", null)

        if (currentusername != null) {
            homeViewModel.getfooddetails(username.toString(), foodid,currentusername)
        }
        initviews(binding)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

        }
        return binding.root
    }

fun initviews(binding: com.nads.epicureapp.databinding.FragmentFoodDetailsBinding){
    val title = binding.root.findViewById<TextView>(R.id.titleepic)
    val ingredients = binding.root.findViewById<TextView>(R.id.ingredientsepic)
    val description = binding.root.findViewById<TextView>(R.id.descriptionepic)
    val imagerepicfooddetails = binding.root.findViewById<ImageView>(R.id.epicfooddetailimage)
    val ratingbarfood = binding.root.findViewById<TextView>(R.id.ratingbarforfooddetails)
    val likecount = binding.root.findViewById<TextView>(R.id.likecount)
    val likebutton = binding.root.findViewById<ImageButton>(R.id.likebuttonepic)
    val ratingbutton = binding.root.findViewById<ImageButton>(R.id.ratingbarimage)
    val phonebutton = binding.root.findViewById<ImageButton>(R.id.phoneimg)
    val instagrambutton = binding.root.findViewById<ImageButton>(R.id.instagramimg)
    val facebookbutton = binding.root.findViewById<ImageButton>(R.id.facebookimg)
    val epicfooddetails = Observer<EpicFoodDetails> { newName ->
        title.text = newName.datavalues.get(0).title
        ingredients.text = newName.datavalues.get(0).ingredients
        description.text = newName.datavalues.get(0).descrip
        val foodlistimage = Base64.decode(newName.datavalues.get(0).imageurl, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(foodlistimage, 0, foodlistimage.size)
        Glide.with(this.requireActivity()).load(decodedImage).fitCenter().into(
            imagerepicfooddetails
        )
        ratingbarfood.text = newName.avrating.toString()
        likecount.text = newName.likecount.toString()
        clickphone(binding, phonebutton, newName.datavaluesforcooks.get(0).contact)
        clickfacebook(binding, facebookbutton, newName.datavaluesforcooks.get(0).facebook)
        clickinstagram(binding, instagrambutton, newName.datavaluesforcooks.get(0).instagram)
        openratingpage(ratingbutton,newName.datavalues.get(0).username,newName.datavalues.get(0).foodid)
        if(newName.dataforlikerating?.get(0)?.likedbutton == 1){
            likebutton.setImageResource(R.mipmap.baseline_thumb_up_alt_black_18dp)
            homeViewModel.epicliketruefalse.value = true

        }
        else{
            likebutton.setImageResource(R.mipmap.outline_thumb_up_alt_black_18dp)
            homeViewModel.epicliketruefalse.value = false
        }
        //Like button clicked
        likebutton.setOnClickListener(View.OnClickListener {
            likebuttoned(likebutton,newName.datavalues.get(0).username,newName.datavalues.get(0).foodid )
            })

    }

    homeViewModel.epicFoodDetails.observe(viewLifecycleOwner, epicfooddetails)





}


    fun clickinstagram(
        binding: FragmentFoodDetailsBinding,
        instabutton: ImageButton,
        instalink: String
    ){
    instabutton.setOnClickListener(View.OnClickListener {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(instalink)
        }
        startActivity(intent)

    })

    }


    fun clickfacebook(
        binding: FragmentFoodDetailsBinding,
        facebookbutton: ImageButton,
        facebooklink: String
    ){

       facebookbutton.setOnClickListener(View.OnClickListener {
           val intent = Intent(Intent.ACTION_VIEW).apply {
               data = Uri.parse(facebooklink)
           }
           startActivity(intent)

       })

    }


    fun clickphone(
        binding: FragmentFoodDetailsBinding,
        phonebutton: ImageButton,
        phonenumber: String
    )
    {
          phonebutton.setOnClickListener(View.OnClickListener {
              val intent = Intent(Intent.ACTION_DIAL).apply {
                  data = Uri.parse("tel:$phonenumber")
              }
              startActivity(intent)
          })

    }

    fun openratingpage(ratingButton: ImageButton, username: String, foodid: String){
        ratingButton.setOnClickListener(
            View.OnClickListener { it ->
                val builder: AlertDialog.Builder? = activity?.let {
                    AlertDialog.Builder(it)
                }
                val mView = View.inflate(requireActivity(), R.layout.popup_fragment_rating, null)
                builder?.setView(mView)
                val dialog: AlertDialog? = builder?.create()
                dialog?.show()
                val ratingBarreview = mView.findViewById<RatingBar>(R.id.ratingBarreview)
                val cancelratingbutton = mView.findViewById<Button>(R.id.cancelratingbutton)
                val submitrating: Button = mView.findViewById(R.id.submitrating)
                submitrating.setOnClickListener(View.OnClickListener {
                    homeViewModel.submitrating(ratingBarreview.rating,  username, foodid)
                    dialog?.cancel()
                })
            }
        )
        val submissionforrating = Observer<ResultModel> {newvale->
            if (newvale.status == true){
                Toast.makeText(requireActivity(),"Thanks For Rating Us",Toast.LENGTH_LONG).show()

            }
            else{
                Toast.makeText(requireActivity(),"There is error in the submission",Toast.LENGTH_LONG).show()
            }

        }
        homeViewModel.epicsubmitrating.observe(viewLifecycleOwner,submissionforrating)

    }
    fun likebuttoned(likebutton: ImageButton,username: String, foodid: String){
        if(homeViewModel.epicliketruefalse.value == false){
            homeViewModel.epicliketruefalse.value = true
            likebutton.setImageResource(R.mipmap.baseline_thumb_up_alt_black_18dp)
            homeViewModel.submitlike(1,username,foodid)
        }
        else{
            homeViewModel.epicliketruefalse.value = false
            likebutton.setImageResource(R.mipmap.outline_thumb_up_alt_black_18dp)
            homeViewModel.submitlike(0,username,foodid)
        }

    }
}