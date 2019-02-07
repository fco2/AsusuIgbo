package com.asusuigbo.frank.asusuigbo.adapters

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
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
class LessonInfoAdapter(var lessonList: List<LessonInfo>, var context: Context) : RecyclerView.Adapter<LessonInfoAdapter.CustomViewHolder>(){

    class CustomViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView? = null
        var imageIndexView: ImageView? = null
        var titleTextView: TextView? = null

        init {
            cardView = itemView!!.findViewById(R.id.card_view_id)
            imageIndexView = itemView.findViewById(R.id.lessonImageId)
            titleTextView = itemView.findViewById(R.id.lessonNameId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.card_view_layout, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int = this.lessonList.size

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        holder!!.imageIndexView!!.setImageResource(this.lessonList[position].imageDrawableIndex)
        holder.titleTextView!!.text = this.lessonList[position].lessonKey
        holder.cardView?.setOnClickListener { v ->

            val intent = Intent(v.context, LessonActivity::class.java)
            intent.putExtra("LESSON_NAME", this.lessonList[position].lessonKey)

            var nextLesson = 0
            if(itemCount != (position + 1))
                nextLesson = position + 1
            intent.putExtra("NEXT_LESSON", "$nextLesson")
            // You need this if starting activity outside an activity context
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            v.context.startActivity(intent)
        }

        if(this.lessonList[position].lessonComplete == "FALSE"){
            holder.cardView?.isEnabled = false
            val color: Int = ContextCompat.getColor(context, R.color.inactiveCardColor)
            holder.cardView?.cardBackgroundColor = ColorStateList.valueOf(color)
            holder.cardView?.elevation = 0.0f
        }
    }
}