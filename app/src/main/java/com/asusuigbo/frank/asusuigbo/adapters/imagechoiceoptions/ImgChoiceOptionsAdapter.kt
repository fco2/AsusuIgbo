package com.asusuigbo.frank.asusuigbo.adapters.imagechoiceoptions

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.databinding.ComponentImgChoiceOptionBinding
import com.asusuigbo.frank.asusuigbo.models.OptionInfo


class ImgChoiceOptionsAdapter(private val clickListener: ImgOptionClickListener):
    ListAdapter<OptionInfo, ImgChoiceOptionsAdapter.CustomViewHolder>(ImgChoiceDiffUtil()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder.from(parent)
    }

    override fun getItemId(position: Int): Long {
        super.getItemId(position)
        return position.toLong()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val optionInfo = getItem(position)
        holder.bind(optionInfo, clickListener)
    }

    class CustomViewHolder(private val binding: ComponentImgChoiceOptionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(optionInfo: OptionInfo, clickListener: ImgOptionClickListener){
            binding.optionInfo = optionInfo
            binding.imgChoiceClickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : CustomViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = ComponentImgChoiceOptionBinding.inflate(inflater, parent, false)
                return CustomViewHolder(binding)
            }
        }
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

class ImgOptionClickListener(var clickListener: (option: String, audio: String, view: View) -> Unit){
    fun onClick(optionInfo: OptionInfo, view: View) = (clickListener(optionInfo.Option, optionInfo.Audio, view))
}