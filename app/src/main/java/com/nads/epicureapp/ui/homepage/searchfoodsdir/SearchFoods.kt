package com.nads.epicureapp.ui.homepage.searchfoodsdir

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nads.epicureapp.R
import com.nads.epicureapp.databinding.FragmentSearchFoodsBinding
import com.nads.epicureapp.ui.homepage.HomeViewModel
import com.nads.epicureapp.ui.homepage.adapters.HomeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFoods : Fragment() {
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.userprimary_navigation) { defaultViewModelProviderFactory }
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val binding:FragmentSearchFoodsBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_search_foods,container,false)
        binding.homeviewmodel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        serchforfoods(binding)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
            findNavController().popBackStack()
            findNavController().navigate(R.id.homeFragment3)

        }

        return binding.root
    }


    fun serchforfoods(binding: FragmentSearchFoodsBinding){

        val serchviewcooks = binding.root.findViewById<SearchView>(R.id.serchcooksfoods)

        serchviewcooks.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                 val viewAdapter = SearchFoodsAdapter(SearchFoodsAdapter.UserComparator)

                lifecycleScope.launch {
                    homeViewModel.getfoodsSearchResult(query).collectLatest { pagingData ->
                        viewAdapter.submitData(pagingData)
                    }
                }
                recyclerView = binding.root.findViewById<RecyclerView>(R.id.searchfoodslist).apply {
                    layoutManager = LinearLayoutManager(requireActivity())
                    adapter = viewAdapter
                }


                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val viewAdapter = SearchFoodsAdapter(SearchFoodsAdapter.UserComparator)

                lifecycleScope.launch {
                    homeViewModel.getfoodsSearchResult(newText.toString()).collectLatest { pagingData ->
                        viewAdapter.submitData(pagingData)
                    }
                }
                recyclerView = binding.root.findViewById<RecyclerView>(R.id.searchfoodslist).apply {
                    layoutManager = LinearLayoutManager(requireActivity())
                    adapter = viewAdapter
                }


                return true
            }


        })



    }
}