package com.asusuigbo.frank.asusuigbo.adapters.newsinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.databinding.ComponentNewsInfoBinding
import com.asusuigbo.frank.asusuigbo.models.NewsInfo

class NewsInfoAdapter(var clickListener: NewsInfoClickListener) : ListAdapter<NewsInfo, NewsInfoViewHolder>(NewsInfoDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsInfoViewHolder {
        return NewsInfoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NewsInfoViewHolder, position: Int) {
        val newsInfo = getItem(position)
        holder.bind(newsInfo, clickListener)
    }
}

class NewsInfoViewHolder(var binding: ComponentNewsInfoBinding) : RecyclerView.ViewHolder(binding.root){

    companion object{
        fun from(parent: ViewGroup): NewsInfoViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context!!)
            val binding = ComponentNewsInfoBinding.inflate(layoutInflater, parent, false)
            return NewsInfoViewHolder(binding)
        }
    }

    fun bind(newsInfo: NewsInfo, clickListener: NewsInfoClickListener) {
        binding.newsInfo = newsInfo
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
}

class NewsInfoClickListener(var clickListener: (title: String) -> Unit){
    fun onClick(newsInfo: NewsInfo){
        return clickListener(newsInfo.title)
    }
}