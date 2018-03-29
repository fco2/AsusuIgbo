package com.asusuigbo.frank.asusuigbo

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.zip.Inflater

/**
 * Created by Frank on 3/27/2018.
 */
class DataInfoAdapter(var dataList: List<DataInfo>) : RecyclerView.Adapter<DataInfoAdapter.CustomViewHolder>(){

    class CustomViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView? = null
        var indexTextview: TextView? = null
        var titleTextview: TextView? = null
        init {
            cardView = itemView!!.findViewById(R.id.card_view_id)
            indexTextview = itemView.findViewById(R.id.textView2)
            titleTextview = itemView.findViewById(R.id.textView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.card_view_layout, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int = this.dataList.size

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        holder!!.indexTextview!!.text = this.dataList[position].Index.toString()
        holder.titleTextview!!.text = this.dataList[position].Name
    }


}