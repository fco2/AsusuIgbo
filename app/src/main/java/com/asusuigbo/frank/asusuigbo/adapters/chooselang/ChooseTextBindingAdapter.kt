package com.asusuigbo.frank.asusuigbo.adapters.chooselang

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.asusuigbo.frank.asusuigbo.models.DataInfo


@BindingAdapter("dataText")
fun TextView.setDataText(dataInfo: DataInfo){
    this.text = dataInfo.dataText
}