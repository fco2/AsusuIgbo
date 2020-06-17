package com.asusuigbo.frank.asusuigbo.adapters.chooselang

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.databinding.ItemLanguageBinding
import com.asusuigbo.frank.asusuigbo.models.LanguageInfo

class ChooseLangAdapter(private val clickListener: ChooseLangClickListener) : ListAdapter<LanguageInfo, ChooseLangAdapter.ViewHolder>(ChooseLangDiffUtil()){
    class ViewHolder(private var binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(languageInfo: LanguageInfo, clickListener: ChooseLangClickListener){
            binding.languageInfo = languageInfo
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemLanguageBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}

class ChooseLangDiffUtil : DiffUtil.ItemCallback<LanguageInfo>(){
    override fun areItemsTheSame(oldItem: LanguageInfo, newItem: LanguageInfo): Boolean {
        return oldItem.language == newItem.language
    }

    override fun areContentsTheSame(oldItem: LanguageInfo, newItem: LanguageInfo): Boolean {
        return oldItem == newItem
    }
}

class ChooseLangClickListener(var clickListener: (language: String) -> Unit){
    fun onClick(languageInfo: LanguageInfo) = clickListener(languageInfo.language)
}