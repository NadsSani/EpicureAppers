package com.nads.epicureapp.ui.homepage.searchcooksdir

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nads.epicureapp.R
import com.nads.epicureapp.databinding.CardForLinearlistBinding
import com.nads.epicureapp.ui.homepage.cookspages.CooksPageDirections
import com.nads.epicureapp.ui.homepage.model.DatavalueXXX
import kotlinx.android.synthetic.main.card_for_linearlist.view.*

class SearchCooksAdapter(diffCallback: DiffUtil.ItemCallback<DatavalueXXX>) :PagingDataAdapter<DatavalueXXX,SearchCooksAdapter.ViewHolder> (diffCallback){



    class ViewHolder(view:CardForLinearlistBinding ) : RecyclerView.ViewHolder(view.root){
        val cardView: CardView
        init {
            cardView = view.root.findViewById(R.id.card_for_linear_listed)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val context =holder.cardView.context
        val insta = holder.cardView.findViewById<ImageButton>(R.id.instaincookssearch)
        if(item?.instagram != null){
            insta.visibility = View.VISIBLE
        }
        val facebook = holder.cardView.findViewById<ImageButton>(R.id.facebookincookssearch)
        if(item?.facebook != null){
            facebook.visibility = View.VISIBLE
        }

        val phone = holder.cardView.findViewById<ImageButton>(R.id.callincookssearch)
        if(item?.contact != null){
            phone.visibility = View.VISIBLE
        }

        val title = holder.cardView.foodstitle3
        val profileimage = holder.cardView.findViewById<ImageView>(R.id.imageprofileepicurefoodlist)
        val foodlistimage = Base64.decode(item?.profilepic, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(foodlistimage, 0, foodlistimage.size)
        Glide.with(context).load(decodedImage).fitCenter().into(profileimage)
        title.text= item?.username.toString()
        insta.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(item?.instagram)
            }
            context.startActivity(intent)

        })
        facebook.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(item?.facebook)
            }
            context.startActivity(intent)

        })

        phone.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${item?.contact}")
            }
            context.startActivity(intent)
        })

        //holder.cardView.findViewById<TextView>(R.id.foodstitle3).text = item?.userid
        holder.cardView.setOnClickListener {
                view ->
            val action = SearchCooksDirections.actionSearchCooksToCooksPage(item?.userid.toString(),item?.username.toString())
            view.findNavController().navigate(action)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        val view = LayoutInflater.from(parent.getContext())
//            .inflate(R.layout.cards_for_list, parent, false)
        val cardForLinearlistBinding = CardForLinearlistBinding.inflate(view,parent,false)


        return ViewHolder(cardForLinearlistBinding)
    }





    object UserComparator : DiffUtil.ItemCallback<DatavalueXXX>() {
        override fun areItemsTheSame(oldItem: DatavalueXXX, newItem: DatavalueXXX): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DatavalueXXX, newItem: DatavalueXXX): Boolean {
            return oldItem == newItem
        }
    }



}