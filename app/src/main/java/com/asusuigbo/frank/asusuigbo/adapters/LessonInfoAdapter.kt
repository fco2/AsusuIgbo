package com.asusuigbo.frank.asusuigbo.adapters

import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.LessonInfo

/**
 * Created by Frank on 3/27/2018.
 */
class LessonInfoAdapter(var lessonList: List<LessonInfo>) : RecyclerView.Adapter<LessonInfoAdapter.CustomViewHolder>(){

    class CustomViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView? = null
        var imageIndexview: ImageView? = null
        var titleTextview: TextView? = null
        init {
            cardView = itemView!!.findViewById(R.id.card_view_id)
            imageIndexview = itemView.findViewById(R.id.lessonImageId)
            titleTextview = itemView.findViewById(R.id.lessonNameId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.card_view_layout, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int = this.lessonList.size

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        //TODO: change to image view
        holder!!.imageIndexview!!.setImageResource(this.lessonList[position].imageDrawableIndex)
        holder.titleTextview!!.text = this.lessonList[position].lessonKey
        holder.cardView?.setOnClickListener { v ->

            val intent = Intent(v.context, LessonActivity::class.java)
            intent.putExtra("LESSON_NAME", this.lessonList[position].lessonKey)
            // You need this if starting activity outside an activity context
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            v.context.startActivity(intent)
        }
    }
}