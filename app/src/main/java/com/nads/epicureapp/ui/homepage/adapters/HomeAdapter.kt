package com.nads.epicureapp.ui.homepage.adapters

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
import com.nads.epicureapp.databinding.CardsForListBinding
import com.nads.epicureapp.ui.homepage.HomeFragmentDirections
import com.nads.epicureapp.ui.homepage.cookspages.CooksPageDirections
import com.nads.epicureapp.ui.homepage.model.DatavalueX

class HomeAdapter(diffCallback: DiffUtil.ItemCallback<DatavalueX>):
    PagingDataAdapter<DatavalueX, HomeAdapter.ViewHolder> (diffCallback){


    class ViewHolder(view: CardsForListBinding) : RecyclerView.ViewHolder(view.root){
        val cardView:CardView
        init {
            cardView = view.root.findViewById(R.id.card_for_listed)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.getContext())
//            .inflate(R.layout.cards_for_list, parent, false)
      val cardsForListBinding = CardsForListBinding.inflate(view,parent,false)


        return ViewHolder(cardsForListBinding)
    }
//Need To IMplement the intent to go to choosen cards cooks
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     val item = getItem(position)
    val img = holder.cardView.findViewById<ImageView>(R.id.imageView2)
    val context =holder.cardView.context
    val title = holder.cardView.findViewById<TextView>(R.id.foodstitle)
    val rating = holder.cardView.findViewById<TextView>(R.id.ratedvalue)
    val likes = holder.cardView.findViewById<TextView>(R.id.likecount)

    val foodlistimage = Base64.decode(item?.foodpic, Base64.DEFAULT)
    val decodedImage = BitmapFactory.decodeByteArray(foodlistimage, 0, foodlistimage.size)
    Glide.with(context).load(decodedImage).fitCenter().into(img)
    rating.text= item?.rating
    likes.text=item?.likes.toString()
    title.text = item?.foodtitle.toString()

             holder.cardView.findViewById<TextView>(R.id.foodstitle).text = item?.foodtitle

     Log.e("errorfooddetails",item?.username.toString() + " " +item?.foodid.toString())
    holder.cardView.setOnClickListener {view->

        val action = HomeFragmentDirections.actionHomeFragment3ToFoodDetails(item?.userid.toString(),item?.foodid.toString(),"opencooksbutton",item?.username.toString())
        view.findNavController().navigate(action)
                 }
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
