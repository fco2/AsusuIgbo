package com.asusuigbo.frank.asusuigbo.adapters.newsinfo

import androidx.recyclerview.widget.DiffUtil
import com.asusuigbo.frank.asusuigbo.models.NewsInfo

class NewsInfoDiffUtil: DiffUtil.ItemCallback<NewsInfo>(){
    override fun areItemsTheSame(oldItem: NewsInfo, newItem: NewsInfo): Boolean {
        return oldItem.title == newItem.title && oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: NewsInfo, newItem: NewsInfo): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}