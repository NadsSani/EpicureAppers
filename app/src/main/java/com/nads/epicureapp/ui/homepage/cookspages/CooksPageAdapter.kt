package com.nads.epicureapp.ui.homepage.cookspages

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nads.epicureapp.R

import com.nads.epicureapp.databinding.CooksPageRecycleviewCardslistBinding
import com.nads.epicureapp.ui.homepage.model.DatavalueXX


class CooksPageAdapter  (diffCallback: DiffUtil.ItemCallback<DatavalueXX>) :PagingDataAdapter<DatavalueXX, CooksPageAdapter.ViewHolder>(diffCallback) {


    class ViewHolder(view: CooksPageRecycleviewCardslistBinding) : RecyclerView.ViewHolder(view.root){
        val cardView: CardView
        init {
            cardView = view.root.findViewById(R.id.cards_cooks_page_recyclerview)
        }
    }
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val view = LayoutInflater.from(parent.getContext())

         val cooksPageRecycleviewCardslistBinding  = CooksPageRecycleviewCardslistBinding.inflate(view,parent,false)
         return ViewHolder(cooksPageRecycleviewCardslistBinding)
     }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //had an error here because i forgot to add getItem here to get the count and display the full list
        //so the count was only 5 but i needed 9 and the error was solved after adding getItem
        val item = getItem(position)
        val img = holder.cardView.findViewById<ImageView>(R.id.imageprofileepicurefoodlist)
        val context =holder.cardView.context

        val foodlistimage = Base64.decode(item?.imageurl,Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(foodlistimage, 0, foodlistimage.size)
        holder.cardView.findViewById<TextView>(R.id.titlecooksfood).text = item?.title
        holder.cardView.findViewById<ImageView>(R.id.imageprofileepicurefoodlist).setImageBitmap(decodedImage)
        holder.cardView.findViewById<TextView>(R.id.epicurecooksfoodlistrating).text = "Rating : "+item?.rating
        holder.cardView.findViewById<TextView>(R.id.likesforcooksrecipes).text = "Likes : "+item?.likes
        Glide.with(context).load(decodedImage).fitCenter().into(img)
        holder.cardView.setOnClickListener {
                view ->
         val action = CooksPageDirections.actionCooksPageToFoodDetails(item?.userid.toString(),item?.foodid.toString(),"",item?.username.toString())
            view.findNavController().navigate(action)


        }

    }




    object UserComparator : DiffUtil.ItemCallback<DatavalueXX>() {
        override fun areItemsTheSame(oldItem: DatavalueXX, newItem: DatavalueXX): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: DatavalueXX, newItem: DatavalueXX): Boolean {
            return oldItem == newItem
        }
    }
}