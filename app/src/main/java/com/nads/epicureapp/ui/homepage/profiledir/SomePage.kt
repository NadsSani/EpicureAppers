package com.nads.epicureapp.ui.homepage.profiledir

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nads.epicureapp.R
import com.nads.epicureapp.databinding.FragmentSomePageBinding
import com.nads.epicureapp.ui.homepage.HomeViewModel
import com.nads.epicureapp.ui.homepage.adapters.YourRecipesAdapter
import com.nads.epicureapp.ui.homepage.searchcooksdir.SearchCooksAdapter
import kotlinx.android.synthetic.main.fragment_some_page.view.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll


class SomePage : Fragment() {
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.userprimary_navigation) { defaultViewModelProviderFactory }
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        val binding:FragmentSomePageBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_some_page, container, false)
        binding.homeviewmodel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclemyrecipe)
        recyclerView.isVisible = false
        val progressbar = binding.root.findViewById<ProgressBar>(R.id.yourrecipprogress)
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.epicureapp_file_key), Context.MODE_PRIVATE)
        val username = sharedPref?.getString("username", null)

        if (username != null) {

            getmyrecipes(username.toString(),binding,progressbar,recyclerView)
        }
        return binding.root
    }
    


    fun getmyrecipes(username:String,binding:FragmentSomePageBinding,progressbar:ProgressBar,recyclerView: RecyclerView){
        val viewAdapter = YourRecipesAdapter(YourRecipesAdapter.UserComparator,this.requireActivity())

        lifecycleScope.launch {
            homeViewModel.getmyrecipeforlist(username).collectLatest { pagingData ->
                viewAdapter.submitData(pagingData)

            }

        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = viewAdapter
            isVisible=true
        }
        progressbar.isVisible = false

    }

}