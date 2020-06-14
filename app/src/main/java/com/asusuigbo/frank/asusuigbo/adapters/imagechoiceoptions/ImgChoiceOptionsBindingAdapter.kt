package com.asusuigbo.frank.asusuigbo.adapters.imagechoiceoptions


import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.asusuigbo.frank.asusuigbo.models.OptionInfo

@BindingAdapter("Option")
fun TextView.setOption(optionInfo: OptionInfo){
    text = optionInfo.Option
}

@BindingAdapter("AdditionalInfo")
fun ImageView.setAdditionalInfo(optionInfo: OptionInfo){
    val imgName = optionInfo.AdditionalInfo
    val resId = context!!.resources.getIdentifier(imgName, "mipmap", context!!.packageName)
    // imgName != "0" && imgName != "1" && imgName != "2" && imgName != "3"
    if(resId.toString() != imgName){ // this check is to filter out word pair questions whose AdditionalInfo is index of its pair, they return their exact same index
        setImageResource(resId)
    }
}