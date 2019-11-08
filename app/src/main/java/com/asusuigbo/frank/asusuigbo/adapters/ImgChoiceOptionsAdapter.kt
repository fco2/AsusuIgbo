package com.asusuigbo.frank.asusuigbo.adapters

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.asusuigbo.frank.asusuigbo.models.UserButton


class ImgChoiceOptionsAdapter(private val optionList: MutableList<OptionInfo>,
                              private val lessonActivity: LessonActivity):
            Adapter<ImgChoiceOptionsAdapter.CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_img_choice_options, parent, false)
        //measuredHeight returns pixels (px) but units are measured in dp.
        val params = view.layoutParams as GridLayoutManager.LayoutParams
        params.height = parent.measuredHeight / 2 - 68
        view.layoutParams = params
        return CustomViewHolder(view)
    }

    /*fun convertFromPxToDp(): Int{
        val y = 68
        return y.toDp(lessonActivity.applicationContext)
    }
    private fun Int.toDp(context: Context): Int = (this / context.resources.displayMetrics.density).toInt()
    fun Int.toPx(context: Context): Int = (this * context.resources.displayMetrics.density).toInt() */

    override fun getItemCount(): Int = optionList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.imgChoiceText.text = optionList[position].Option
        val resourceId: Int = getResourceId(position)
        holder.imgChoiceImg.setImageResource(resourceId)
        holder.imgChoiceCardView.setOnClickListener {
            //remove color filter for all items
            for(i in 0 until lessonActivity.imgChoiceView.recyclerView.childCount){
                val view: View = lessonActivity.imgChoiceView.recyclerView.getChildAt(i)
                view.background.clearColorFilter()
                val image = view.findViewById<ImageView>(R.id.img_choice_img)
                image.clearColorFilter()
            }
            //add color filter for selected item, both RelativeLayout and imageView
            val overlayColor = ContextCompat.getColor(lessonActivity.applicationContext,
                R.color.selectedImgChoiceOption)
            val filter = PorterDuffColorFilter(overlayColor, PorterDuff.Mode.MULTIPLY)
            holder.imgChoiceCardView.background.colorFilter = filter
            holder.imgChoiceImg.colorFilter = filter
            //enable button click and set buttonState
            lessonActivity.button!!.isEnabled = true
            lessonActivity.buttonState = UserButton.AnswerSelected
            lessonActivity.selectedAnswer = optionList[position].Option
        }
    }

    private fun getResourceId(position: Int): Int {
        val imgName = optionList[position].AdditionalInfo
        val context = lessonActivity.applicationContext
        return context.resources.getIdentifier(imgName, "mipmap", context.packageName)
    }

    inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imgChoiceCardView: RelativeLayout = itemView.findViewById(R.id.img_choice_card_view)
        var imgChoiceText: TextView = itemView.findViewById(R.id.img_choice_text)
        var imgChoiceImg: ImageView = itemView.findViewById(R.id.img_choice_img)
    }
}