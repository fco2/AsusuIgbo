package com.asusuigbo.frank.asusuigbo.adapters.imagechoiceoptions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.fragments.ImgChoiceFragment
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.asusuigbo.frank.asusuigbo.models.UserButton


@Suppress("SameParameterValue")
class ImgChoiceOptionsAdapter(private val optionList: MutableList<OptionInfo>,
                              private val fragment: ImgChoiceFragment):
            ListAdapter<OptionInfo, ImgChoiceOptionsAdapter.CustomViewHolder>(ImgChoiceDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_img_choice_option, parent, false)
        return CustomViewHolder(view)
    }

    //override fun getItemCount(): Int = optionList.size

   /* override fun getItemId(position: Int): Long {
        super.getItemId(position)
        return position.toLong()
    }*/

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.imgChoiceText.text = optionList[position].Option
        val resourceId: Int = getResourceId(position)
        holder.imgChoiceImg.setImageResource(resourceId)
        holder.imgChoiceCardView.setOnClickListener {
            //play audio here
            if(optionList[position].Audio != "")
                fragment.playAudio(optionList[position].Audio)
            //remove color filter for all items
            for(i in 0 until fragment.binding.imgChoiceRecyclerViewId.childCount){
                val view: View = fragment.binding.imgChoiceRecyclerViewId.getChildAt(i)
                view.background.clearColorFilter()
                val image = view.findViewById<ImageView>(R.id.img_choice_img)
                image.clearColorFilter()
            }
            //add color filter for selected item, both RelativeLayout and imageView
            val overlayColor = ContextCompat.getColor(fragment.currentLessonActivity.applicationContext,
                R.color.selectedImgChoiceOption)
            val filter = PorterDuffColorFilter(overlayColor, PorterDuff.Mode.MULTIPLY)
            holder.imgChoiceCardView.background.colorFilter = filter
            holder.imgChoiceImg.colorFilter = filter
            //enable button click and set buttonState
            fragment.setUpButtonStateAndText(UserButton.AnswerSelected, R.string.answer_button_state)
            fragment.currentLessonActivity.currentLessonViewModel.setSelectedAnswer(optionList[position].Option)
        }
    }

    private fun getResourceId(position: Int): Int {
        val imgName = optionList[position].AdditionalInfo
        val context = fragment.currentLessonActivity.applicationContext
        return context.resources.getIdentifier(imgName, "mipmap", context.packageName)
    }

    inner class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imgChoiceCardView: ConstraintLayout = itemView.findViewById(R.id.img_choice_card_view)
        var imgChoiceText: TextView = itemView.findViewById(R.id.img_choice_text)
        var imgChoiceImg: ImageView = itemView.findViewById(R.id.img_choice_img)
    }
}

class ImgChoiceDiffUtil : DiffUtil.ItemCallback<OptionInfo>(){
    override fun areItemsTheSame(oldItem: OptionInfo, newItem: OptionInfo): Boolean {
        return oldItem.Option == newItem.Option
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: OptionInfo, newItem: OptionInfo): Boolean {
        return oldItem == newItem
    }
}

class ImgOptionClickListener(var clickListener: (option: String) -> Unit){
    fun onClick(optionInfo: OptionInfo) = (clickListener(optionInfo.Option))
}