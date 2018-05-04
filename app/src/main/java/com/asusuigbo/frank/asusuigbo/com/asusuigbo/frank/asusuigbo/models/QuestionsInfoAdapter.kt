package com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.R

class QuestionsInfoAdapter(var dataList: ArrayList<Option>, var activity: Activity): RecyclerView.Adapter<QuestionsInfoAdapter.CustomVH>() {

    var selectedItem: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomVH {
        val view: View = LayoutInflater.from(parent!!.context).inflate(R.layout.question_options, parent, false)
        return CustomVH(view)
    }

    override fun getItemCount(): Int = this.dataList.size

    override fun onBindViewHolder(holder: CustomVH?, position: Int) {
        holder!!.radioButton!!.isChecked = (position == selectedItem)
        holder.optionsText!!.text = this.dataList[position].OptionText
    }

    inner class CustomVH(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        var radioButton: RadioButton? = null
        var optionsText: TextView? = null

        init{
            this.radioButton = itemView!!.findViewById(R.id.radio_button_id)
            this.optionsText = itemView.findViewById(R.id.option_text_id)

            var customOnclickListener = View.OnClickListener {
                selectedItem = adapterPosition
                notifyDataSetChanged()
                //Set selected variable
                SharedData.SelectedAnswerIndex = selectedItem
                //set button to active
                val button = activity.findViewById<Button>(R.id.check_answer_button_id)
                button.isEnabled = true
            }

            this.radioButton!!.setOnClickListener(customOnclickListener)
            this.optionsText!!.setOnClickListener(customOnclickListener)
        }

    }
}