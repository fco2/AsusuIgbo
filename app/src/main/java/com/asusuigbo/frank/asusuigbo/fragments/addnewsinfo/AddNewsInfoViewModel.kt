package com.asusuigbo.frank.asusuigbo.fragments.addnewsinfo

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddNewsInfoViewModel @ViewModelInject constructor() : ViewModel() {

    private val _title = MutableLiveData<String>()
    val title : LiveData<String>
        get() = _title

    private val _newsSource= MutableLiveData<String>()
    val newsSource : LiveData<String>
        get() = _newsSource

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl : LiveData<String>
        get() = _imageUrl

    private val _content = MutableLiveData<String>()
    val content : LiveData<String>
        get() = _content

    private val _language = MutableLiveData<String>()
    val language : LiveData<String>
        get() = _language

    private val _date = MutableLiveData<String>()
    val date : LiveData<String>
        get() = _date

    private val _imgUri = MutableLiveData<Uri>()
    val imgUri : LiveData<Uri>
        get() = _imgUri

    fun setImgUri(uri: Uri){
        _imgUri.value = uri
    }

    fun setTitle(t: String){
        _title.value = t
    }

    fun setContent(t: String){
        _content.value = t
    }

    fun setDate(t: String){
        _date.value = t
    }

    fun setLanguage(t: String){
        _language.value = t
    }

    fun setNewsSource(t: String){
        _newsSource.value = t
    }
}