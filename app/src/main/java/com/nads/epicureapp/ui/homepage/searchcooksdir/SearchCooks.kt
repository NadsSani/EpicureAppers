package com.nads.epicureapp.ui.homepage.searchcooksdir

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nads.epicureapp.R
import com.nads.epicureapp.databinding.FragmentSearchCooksBinding
import com.nads.epicureapp.ui.homepage.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchCooks : Fragment() {
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
        val binding:FragmentSearchCooksBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_cooks, container, false)
        binding.homeviewmodel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        serchforcooks(binding)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().findNavController(R.id.fragment_to).popBackStack()
            requireActivity().findNavController(R.id.fragment_to).popBackStack()
            requireActivity().findNavController(R.id.fragment_to).navigate(R.id.homeFragment3)
        }



        return binding.root
    }

       fun serchforcooks(binding: FragmentSearchCooksBinding){

           val serchviewcooks = binding.root.findViewById<SearchView>(R.id.serchcookstext)


           serchviewcooks.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
               override fun onQueryTextSubmit(query: String?): Boolean {
                   val viewAdapter = SearchCooksAdapter(SearchCooksAdapter.UserComparator)
                   lifecycleScope.launch {
                       homeViewModel.getcooksSearchResult(query).collectLatest { pagingData ->
                           viewAdapter.submitData(pagingData)
                       }
                   }


                   recyclerView = binding.root.findViewById<RecyclerView>(R.id.searchcookslist).apply {
                       layoutManager = LinearLayoutManager(requireActivity())
                       adapter = viewAdapter
                   }
                   return true
               }

               override fun onQueryTextChange(newText: String?): Boolean {

                   val viewAdapter = SearchCooksAdapter(SearchCooksAdapter.UserComparator)
                   lifecycleScope.launch {
                       homeViewModel.getcooksSearchResult(newText).collectLatest { pagingData ->
                           viewAdapter.submitData(pagingData)
                       }
                   }

                   recyclerView = binding.root.findViewById<RecyclerView>(R.id.searchcookslist).apply {
                       layoutManager = LinearLayoutManager(requireActivity())
                       adapter = viewAdapter
                   }
                   return true
               }


           })



       }

}