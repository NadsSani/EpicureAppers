package com.nads.epicureapp.ui.homepage.searchfoodsdir

import android.graphics.BitmapFactory
import android.util.Base64
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

import com.nads.epicureapp.ui.homepage.model.DatavalueXX
import com.nads.epicureapp.ui.homepage.searchcooksdir.SearchCooksDirections
import kotlinx.android.synthetic.main.card_list_foods_search.view.*


class SearchFoodsAdapter(diffCallback: DiffUtil.ItemCallback<DatavalueXX>) :PagingDataAdapter<DatavalueXX, SearchFoodsAdapter.ViewHolder> (diffCallback) {


    class ViewHolder(view: com.nads.epicureapp.databinding.CardListFoodsSearchBinding) : RecyclerView.ViewHolder(view.root) {
        val cardView: CardView

        init {
            cardView = view.root.findViewById(R.id.card_for_foods_search)
        }

    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val context =holder.cardView.context
         val img = holder.cardView.findViewById<ImageView>(R.id.foodlistpic)
        val title = holder.cardView.findViewById<TextView>(R.id.footitle)
        val likertext = holder.cardView.likesforfoodsearch
        val ratingtext = holder.cardView.ratingforfoodsearch

        val foodlistimage = Base64.decode(item?.imageurl, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(foodlistimage, 0, foodlistimage.size)
        Glide.with(context).load(decodedImage).fitCenter().into(img)
        title.text = item?.title
        likertext.text = "Likes : "+item?.likes.toString()
        ratingtext.text = "Rating : "+item?.rating.toString()

        holder.cardView.setOnClickListener {
                view ->
            val action = SearchFoodsDirections.actionSearchFoodsToFoodDetails(item?.userid.toString(),item?.foodid.toString(),"opencooksbutton",item?.username.toString())
            view.findNavController().navigate(action)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.getContext())
//            .inflate(R.layout.cards_for_list, parent, false)
        val cardsForListBinding = com.nads.epicureapp.databinding.CardListFoodsSearchBinding.inflate(view,parent,false)


        return ViewHolder(cardsForListBinding)
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