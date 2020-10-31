package com.asusuigbo.frank.asusuigbo.adapters.newsinfo

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.asusuigbo.frank.asusuigbo.models.NewsInfo
import com.squareup.picasso.Picasso

@BindingAdapter("title")
fun setTitle(view: TextView, newsInfo: NewsInfo){
    view.text = newsInfo.title
}

@BindingAdapter("newsSource")
fun setNewsSource(view: TextView, newsInfo: NewsInfo){
    view.text = newsInfo.newsSource
}

@BindingAdapter("date")
fun setDate(view: TextView, newsInfo: NewsInfo){
    view.text = newsInfo.date
}

@BindingAdapter("content")
fun setContent(view: TextView, newsInfo: NewsInfo){
    view.text = newsInfo.content
}

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, newsInfo: NewsInfo){
    Picasso.with(view.context!!).load(newsInfo.imageUrl).into(view)
}