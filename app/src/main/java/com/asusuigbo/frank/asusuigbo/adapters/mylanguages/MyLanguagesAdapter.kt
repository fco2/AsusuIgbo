package com.asusuigbo.frank.asusuigbo.adapters.mylanguages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.adapters.ChooseTextClickListener
import com.asusuigbo.frank.asusuigbo.adapters.ChooseTextDiffUtil
import com.asusuigbo.frank.asusuigbo.databinding.ComponentMyLanguagesBinding
import com.asusuigbo.frank.asusuigbo.models.DataInfo

class MyLanguagesAdapter(private val clickListener: ChooseTextClickListener) : ListAdapter<DataInfo, MyLanguagesAdapter.ViewHolder>(ChooseTextDiffUtil()) {
    class ViewHolder(var binding: ComponentMyLanguagesBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dataInfo: DataInfo, clickListener: ChooseTextClickListener) {
            binding.dataInfo = dataInfo
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = ComponentMyLanguagesBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataInfo = getItem(position)
        holder.bind(dataInfo, clickListener)
    }
}

