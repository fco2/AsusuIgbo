package com.asusuigbo.frank.asusuigbo.adapters.chooselang

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.adapters.ChooseTextClickListener
import com.asusuigbo.frank.asusuigbo.adapters.ChooseTextDiffUtil
import com.asusuigbo.frank.asusuigbo.databinding.ItemDataInfoBinding
import com.asusuigbo.frank.asusuigbo.models.DataInfo

class ChooseTextAdapter(private val clickListener: ChooseTextClickListener) : ListAdapter<DataInfo, ChooseTextAdapter.ViewHolder>(
    ChooseTextDiffUtil()
){
    class ViewHolder(private var binding: ItemDataInfoBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(dataInfo: DataInfo, clickListener: ChooseTextClickListener){
            binding.dataInfo = dataInfo
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemDataInfoBinding.inflate(inflater, parent, false)
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