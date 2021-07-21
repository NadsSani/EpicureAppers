package com.nads.epicureapp.ui.homepage.cookspages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nads.epicureapp.R
import com.nads.epicureapp.databinding.FragmentCooksPageBinding
import com.nads.epicureapp.ui.homepage.FoodDetailsArgs
import com.nads.epicureapp.ui.homepage.HomeFragmentArgs
import com.nads.epicureapp.ui.homepage.HomeViewModel
import com.nads.epicureapp.ui.homepage.adapters.HomeAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CooksPage : Fragment() {
    val args: CooksPageArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
   // private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.userprimary_navigation) { defaultViewModelProviderFactory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        val binding:FragmentCooksPageBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_cooks_page,container,false)
        binding.homeviewmodel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val userid = args.userid
        val username = args.username

        if (username != null){

            homeViewModel.epiccorouselusername = username
            findNavController().currentDestination?.label = username
        }

       createfoodlist(binding)







        return binding.root
    }
     fun createfoodlist(binding:FragmentCooksPageBinding){
         viewManager = LinearLayoutManager(requireActivity())
         val viewAdapter = CooksPageAdapter(CooksPageAdapter.UserComparator)
         recyclerView = binding.root.findViewById<RecyclerView>(R.id.cookspagerecyclerview).apply {
             layoutManager = viewManager
             adapter = viewAdapter
         }
         lifecycleScope.launch {
             homeViewModel.getcooksfoodlist().collectLatest { pagingData ->
                 viewAdapter.submitData(pagingData)
             }
         }
     }

}