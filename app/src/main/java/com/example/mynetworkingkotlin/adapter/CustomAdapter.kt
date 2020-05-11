package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.model.Player
import com.example.mynetworkingkotlin.R
import java.lang.reflect.Member


//class CustomAdapter(val members: ArrayList<Member>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperAdapter{ //Paging  add method val listener: OnBottomReachedListener
class CustomAdapter(private val context: Context, private val members: List<Player>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //Paging  add method val listener: OnBottomReachedListener

    override fun getItemCount(): Int {
        return members.size
    }

    // -------------------- Hohlagan turdagi listni hosil qiladi-------------------------//
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_custom_layout, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = members.get(position)


        if (holder is CustomViewHolder) {
            holder.tennis_name.setText(item.name)
            holder.tennis_location.setText(item.country + ", " + item.city)
            Glide.with(context).load(item.imgUrl).into(holder.tennis_image)

        }
    }

    class CustomViewHolder(iteView: View) : RecyclerView.ViewHolder(iteView) {
        var tennis_image = itemView.findViewById<ImageView>(R.id.tennis_image)
        var tennis_name = itemView.findViewById<TextView>(R.id.tennis_name)
        var tennis_location = itemView.findViewById<TextView>(R.id.tennis_location)
        /*var view_background = itemView.findViewById<TextView>(R.id.view_background)*/
    }


}






