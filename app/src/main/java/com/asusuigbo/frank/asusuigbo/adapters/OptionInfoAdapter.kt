package com.asusuigbo.frank.asusuigbo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.OptionInfo

class OptionInfoAdapter(private var dataList: MutableList<OptionInfo>)
    : RecyclerView.Adapter<OptionInfoAdapter.CustomViewHolder>() {

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var option: TextView? = null
        var audio: ImageView? = null
        var additionalInfo: TextView? = null

        init{
            option = itemView.findViewById(R.id.option_add_option_id)
            audio = itemView.findViewById(R.id.option_record_audio_button_id)
            additionalInfo = itemView.findViewById(R.id.option_additional_info_id)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.component_option_info, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int = this.dataList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.option!!.text = this.dataList[position].Option
        //holder.audio!!.text = this
        holder.additionalInfo!!.text = this.dataList[position].AdditionalInfo
    }

    fun addOption(option: OptionInfo){
        dataList.add(option)
        notifyDataSetChanged()
    }
}