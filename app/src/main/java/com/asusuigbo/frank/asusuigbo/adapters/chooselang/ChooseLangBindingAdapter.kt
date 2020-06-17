package com.asusuigbo.frank.asusuigbo.adapters.chooselang

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.asusuigbo.frank.asusuigbo.models.LanguageInfo


@BindingAdapter("Language")
fun TextView.setLanguage(languageInfo: LanguageInfo){
    this.text = languageInfo.language
}