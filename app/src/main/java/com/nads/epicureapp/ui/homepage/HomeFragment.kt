package com.nads.epicureapp.ui.homepage

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.nads.epicureapp.R
import com.nads.epicureapp.databinding.FragmentHomeBinding
import com.nads.epicureapp.model.apiservice.datamodels.AddRecipeModel
import com.nads.epicureapp.ui.homepage.adapters.HomeAdapter
import com.nads.epicureapp.ui.homepage.model.TopCooks
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var recyclerView2: RecyclerView
    private lateinit var viewManager2: RecyclerView.LayoutManager
    private lateinit var viewPager: ViewPager2
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.userprimary_navigation) { defaultViewModelProviderFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.homeviewmodel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initserchbuttonforcooksandfoods(binding)
        forcorouselview(binding)
        recyclerviewforvegfoods(binding)
        recyclerviewfornonveg(binding)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        }
        return binding.root
    }

    fun initserchbuttonforcooksandfoods(binding: FragmentHomeBinding){
        val buttonforcooks = Observer<Boolean> { newName ->
            if(newName){
                findNavController().navigate(R.id.action_homeFragment3_to_searchCooks)
            }
        }
        val buttonforfoods = Observer<Boolean> { newName ->
            if(newName){
                findNavController().navigate(R.id.action_homeFragment3_to_searchFoods)
            }
        }
        homeViewModel.epicureserchcooks.observe(viewLifecycleOwner, buttonforcooks)
        homeViewModel.epicureserchfoods.observe(viewLifecycleOwner, buttonforfoods)
    }


// for corousel view function calls "gettopratingcooksoftheweek" in service

    fun forcorouselview(binding: FragmentHomeBinding){
        val imagearray:MutableList<String> = ArrayList()
        val imageListener: ImageListener = object : ImageListener {

            override fun setImageForPosition(position: Int, imageView: ImageView) {

                homeViewModel.topcooks()
                homeViewModel.epicuretopcooks.observe(
                    viewLifecycleOwner,
                    Observer<TopCooks> { item ->
                        // Update the UI
                        var i = 0
                        for (itemm in item.datavalues) {
                            imagearray.add(itemm.profilepic)
                        }
                        val decodedByte: ByteArray = Base64.decode(imagearray[position], Base64.DEFAULT)
                        val bitmap =
                            BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
                        Glide.with(requireActivity()).load(bitmap).fitCenter().into(
                            imageView
                        );
                        imageView.setOnClickListener(View.OnClickListener {
                       val action = HomeFragmentDirections.actionHomeFragment3ToCooksPage(item.datavalues.get(0).userid,item.datavalues.get(0).username.toString())
                           it.findNavController().navigate(action)
                        })

                    })


            }
        }
        val carouselView = binding.root.findViewById(R.id.corouselView) as CarouselView;
        carouselView.setPageCount(5);
        carouselView.setImageListener(imageListener)

    }

    fun recyclerviewforvegfoods(binding:FragmentHomeBinding){
        viewManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val viewAdapter = HomeAdapter(HomeAdapter.UserComparator)
        recyclerView = binding.root.findViewById<RecyclerView>(R.id.list_trendfoods).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        lifecycleScope.launch {
            homeViewModel.flower.collectLatest { pagingData ->
                viewAdapter.submitData(pagingData)
            }
        }

    }

    fun recyclerviewfornonveg(binding: FragmentHomeBinding){

        viewManager2 = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val viewAdapter2 = HomeAdapter(HomeAdapter.UserComparator)
        recyclerView2 = binding.root.findViewById<RecyclerView>(R.id.list_trendfoods_veg).apply {
            layoutManager = viewManager2
            adapter = viewAdapter2
        }
        lifecycleScope.launch {
            homeViewModel.flower2.collectLatest { pagingData ->
                viewAdapter2.submitData(pagingData)
            }
        }
    }



}


