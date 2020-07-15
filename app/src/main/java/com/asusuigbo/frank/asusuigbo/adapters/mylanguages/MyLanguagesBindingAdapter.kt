package com.asusuigbo.frank.asusuigbo.adapters.mylanguages

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.DataInfo

@BindingAdapter("dataText")
fun TextView.setDataText(dataInfo: DataInfo){
    this.text = dataInfo.dataText
    if(dataInfo.additionalInfo == "Add Language")
        this.setTextColor(ContextCompat.getColor(context, R.color.lighterGrey))
}

@BindingAdapter("active")
fun TextView.setActive(dataInfo: DataInfo){
    this.visibility = if(dataInfo.isActive) View.VISIBLE else View.GONE
}

@BindingAdapter("additionalInfo")
fun ConstraintLayout.setEnabled(dataInfo: DataInfo){
    this.isEnabled = dataInfo.additionalInfo == "Add Language"
}