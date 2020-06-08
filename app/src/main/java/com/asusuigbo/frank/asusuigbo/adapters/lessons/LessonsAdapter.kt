package com.asusuigbo.frank.asusuigbo.adapters.lessons

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.databinding.LessonItemBinding
import com.asusuigbo.frank.asusuigbo.models.UserLesson

/**
 * Created by Frank on 3/27/2018.
 */
class LessonsAdapter(private var clickListener: LessonsClickListener) : ListAdapter<UserLesson, LessonsAdapter.ViewHolder>(LessonsDiffUtil()){

    class ViewHolder(private val binding: LessonItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserLesson, clickListener: LessonsClickListener) {
            binding.userLesson = item
            binding.clickListener = clickListener
            binding.executePendingBindings()

            if(item.Unlocked == "False"){
                val color: Int = ContextCompat.getColor(binding.root.context, R.color.inactiveCardColor)
                binding.cardViewId.setCardBackgroundColor(ColorStateList.valueOf(color))
                binding.cardViewId.elevation = 0.0f

                //set color filter to make image appear inactive
                val matrix = ColorMatrix()
                matrix.setSaturation(0.0f) //default is 1.0f
                val filter = ColorMatrixColorFilter(matrix)
                binding.lessonImage.colorFilter = filter

                val shakeAnimation = AnimationUtils.loadAnimation(binding.root.context, R.anim.shake_animation)
                //this replaces default onclick for cardView
                binding.cardViewId.setOnClickListener{v ->
                    v.startAnimation(shakeAnimation)
                }
            }
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

    override fun getItemId(position: Int): Long {
         super.getItemId(position)
         return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}

class LessonsDiffUtil: DiffUtil.ItemCallback<UserLesson>(){
    override fun areItemsTheSame(oldItem: UserLesson, newItem: UserLesson): Boolean {
        return oldItem.LessonName == newItem.LessonName
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: UserLesson, newItem: UserLesson): Boolean {
        return oldItem == newItem
    }
}

//clickListener
class LessonsClickListener(val clickListener: (lessonName: String) -> Unit){
    fun onClick(userLesson: UserLesson) = clickListener(userLesson.LessonName)
}