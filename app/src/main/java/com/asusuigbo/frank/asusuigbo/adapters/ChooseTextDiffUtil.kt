package com.asusuigbo.frank.asusuigbo.adapters

import androidx.recyclerview.widget.DiffUtil
import com.asusuigbo.frank.asusuigbo.models.DataInfo

class ChooseTextDiffUtil : DiffUtil.ItemCallback<DataInfo>(){
    override fun areItemsTheSame(oldItem: DataInfo, newItem: DataInfo): Boolean {
        return oldItem.dataText == newItem.dataText
    }

    override fun areContentsTheSame(oldItem: DataInfo, newItem: DataInfo): Boolean {
        return oldItem == newItem
    }
}

class ChooseTextClickListener(var clickListener: (dataInfo: String) -> Unit){
    fun onClick(dataInfo: DataInfo) = clickListener(dataInfo.dataText)
}