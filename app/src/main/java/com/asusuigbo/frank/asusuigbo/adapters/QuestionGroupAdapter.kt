package com.asusuigbo.frank.asusuigbo.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.SharedData
import com.asusuigbo.frank.asusuigbo.models.UserButton

class QuestionGroupAdapter(var dataList: ArrayList<String>, var activity: Activity):
        RecyclerView.Adapter<QuestionGroupAdapter.CustomVH>() {

    var selectedItem: Int = -1
    val checkAnswerButton = this.activity.findViewById<Button>(R.id.check_answer_button_id)!!

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomVH {
        val view: View = LayoutInflater
                .from(parent!!.context)
                .inflate(R.layout.question_options, parent, false)
        return CustomVH(view)
    }

    override fun getItemCount(): Int = this.dataList.size

    override fun onBindViewHolder(holder: CustomVH?, position: Int) {
        holder!!.radioButton!!.isChecked = (position == selectedItem)
        holder.optionsText!!.text = this.dataList[position]
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
                SharedData.SelectedAnswerIndex = selectedItem
                SharedData.ButtonState = UserButton.AnswerSelected
                checkAnswerButton.isEnabled = true
            }
            this.radioButton!!.setOnClickListener(customOnclickListener)
            this.optionsText!!.setOnClickListener(customOnclickListener)
        }
    }
}