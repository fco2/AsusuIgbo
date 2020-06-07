package com.asusuigbo.frank.asusuigbo.adapters.lessons

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.databinding.LessonItemBinding
import com.asusuigbo.frank.asusuigbo.models.LessonInfo

/**
 * Created by Frank on 3/27/2018.
 */
class LessonsAdapter(private var clickListener: LessonsClickListener) : ListAdapter<LessonInfo, LessonsAdapter.ViewHolder>(LessonsDiffUtil()){

    class ViewHolder(private val binding: LessonItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LessonInfo, clickListener: LessonsClickListener) {
            binding.lessonInfo = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = LessonItemBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

   /* override fun getItemId(position: Int): Long {
         super.getItemId(position)
         return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return position
    }*/

    //override fun getItemCount(): Int = this.lessonList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)


        //holder.imageIndexView!!.setImageResource(this.lessonList[position].imageDrawableIndex)
        //holder.titleTextView!!.text = this.lessonList[position].lessonKey

        //TODO: up next
        holder.cardView?.setOnClickListener { v ->
           //------
        }

        //
        if(position > this.viewableLessons ){
            val color: Int = ContextCompat.getColor(context, R.color.inactiveCardColor)
            holder.cardView?.setCardBackgroundColor(ColorStateList.valueOf(color))
            holder.cardView?.elevation = 0.0f

            //set color filter to make image appear inactive
            val matrix = ColorMatrix()
            matrix.setSaturation(0.0f) //default is 1.0f
            val filter = ColorMatrixColorFilter(matrix)
            holder.imageIndexView?.colorFilter = filter

            val shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake_animation)
            //this replaces default onclick for cardView
            holder.cardView?.setOnClickListener{v ->
                v.startAnimation(shakeAnimation)
            }
        }
    }
}

class LessonsDiffUtil: DiffUtil.ItemCallback<LessonInfo>(){
    override fun areItemsTheSame(oldItem: LessonInfo, newItem: LessonInfo): Boolean {
        return oldItem.lessonKey == newItem.lessonKey
    }

    override fun areContentsTheSame(oldItem: LessonInfo, newItem: LessonInfo): Boolean {
        return oldItem == newItem
    }
}

//clickListener
class LessonsClickListener(val clickListener: (lessonKey: String) -> Unit){
    fun onClick(lessonInfo: LessonInfo) = clickListener(lessonInfo.lessonKey)
}