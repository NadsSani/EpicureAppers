package com.nads.epicureapp.ui.homepage.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nads.epicureapp.R
import com.nads.epicureapp.databinding.CardsForMyrecipeBinding
import com.nads.epicureapp.ui.homepage.HomeViewModel
import com.nads.epicureapp.ui.homepage.model.DatavalueX
import com.nads.epicureapp.ui.homepage.profiledir.SomePageDirections


class YourRecipesAdapter(diffCallback: DiffUtil.ItemCallback<DatavalueX>,context: Context) :
    PagingDataAdapter<DatavalueX, YourRecipesAdapter.ViewHolder>(diffCallback){
    private var context1: Context? = context


    private var homeViewModel: HomeViewModel? = null

    class ViewHolder(view: CardsForMyrecipeBinding) : RecyclerView.ViewHolder(view.root){
        val cardView: CardView
        init {
            cardView = view.root.findViewById(R.id.card_for_myrecipelisted)
        }

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        homeViewModel = ViewModelProviders.of(((context1 as FragmentActivity?)!!)).get(HomeViewModel::class.java)

        val img = holder.cardView.findViewById<ImageView>(R.id.imageprofilemyrecipe)
        val context =holder.cardView.context
        holder.cardView.findViewById<TextView>(R.id.foodstitlemyrecipe).text = item?.foodtitle
        holder.cardView.findViewById<TextView>(R.id.ratingvaluemyrecipe).text = item?.rating
        holder.cardView.findViewById<TextView>(R.id.like_count_yourrecipevalue).text = item?.likes.toString()
        val foodlistimage = Base64.decode(item?.foodpic, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(foodlistimage, 0, foodlistimage.size)
        val buttondel = holder.cardView.findViewById<Button>(R.id.deletebutton)
        val buttonedit = holder.cardView.findViewById<Button>(R.id.edit_button)
        Glide.with(context).load(decodedImage).fitCenter().into(img)

        buttondel.setOnClickListener(View.OnClickListener { it ->
            homeViewModel!!.delrecipe(item?.category,item?.username,item?.foodid)
            refreshView(position)

        })
        buttonedit.setOnClickListener { it ->
              val action = SomePageDirections.actionSomePage2ToEditRecipes(item?.username.toString(),item?.foodid.toString(),item?.username.toString())
              it.findNavController().navigate(action)
        }
        holder.cardView.setOnClickListener { it ->
               val action = SomePageDirections.actionSomePage2ToFoodDetails(
                   item?.userid.toString(),
                   item?.foodid.toString(),
                   "opencooksbutton",
                   item?.username.toString()
               )
                it.findNavController().navigate(action)
        }
    }
    fun refreshView(position: Int) {
        notifyItemChanged(position)
        refresh()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.getContext())
        val cardsForMyrecipeBinding = CardsForMyrecipeBinding.inflate(view, parent, false)
        return ViewHolder(cardsForMyrecipeBinding)
    }




    object UserComparator : DiffUtil.ItemCallback<DatavalueX>() {
        override fun areItemsTheSame(oldItem: DatavalueX, newItem: DatavalueX): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DatavalueX, newItem: DatavalueX): Boolean {
            return oldItem == newItem
        }
    }



}