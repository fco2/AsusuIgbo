package com.asusuigbo.frank.asusuigbo.adapters.optionInfo

import android.annotation.SuppressLint
import android.media.MediaRecorder
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.AddQuestionFragment
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

class OptionInfoAdapter(private var dataList: MutableList<OptionInfo>, private var addQuestionsFragment: AddQuestionFragment)
    : RecyclerView.Adapter<OptionInfoAdapter.CustomViewHolder>() {

    private lateinit var mediaRecorder: MediaRecorder
    private var filePath = ""
    private var fileName = ""

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
        return CustomViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = this.dataList.size

    override fun getItemId(position: Int): Long {
        super.getItemId(position)
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return position
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.option!!.text = this.dataList[position].Option
        //holder.audio!!.text = this
        holder.additionalInfo!!.text = this.dataList[position].AdditionalInfo
        //set tag to hold position of view
        holder.audio!!.tag = position.toString()
        //set option textChangeListener
        addOptionTextChangeListener(holder.option!!, position)
        addOptionTextChangeListener(holder.additionalInfo!!, position, false)
        holder.audio!!.setOnTouchListener(recordButtonTouchListener)
    }

    private fun addOptionTextChangeListener(
        textView: TextView,
        position: Int,
        isSetOption: Boolean = true
    ) {
        textView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(isSetOption)
                    dataList[position].Option = s.toString()
                else
                    dataList[position].AdditionalInfo = s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private var recordButtonTouchListener = View.OnTouchListener{ view: View, motionEvent: MotionEvent ->
        if(motionEvent.action == MotionEvent.ACTION_DOWN) {
            setFileNameAndAudio(view)
            startRecording()
            Snackbar.make(view.rootView, "Started recording..", Snackbar.LENGTH_SHORT).show()
            addQuestionsFragment.vibrateForAudio()
        }
        else
        if(motionEvent.action == MotionEvent.ACTION_UP) {
            stopRecording()
            Snackbar.make(view.rootView, "Finished recording!", Snackbar.LENGTH_SHORT).show()
            addQuestionsFragment.vibrateForAudio()
        }
        true
    }

    private fun setFileNameAndAudio(view: View) {
        val position = view.tag.toString().toInt()
        fileName = replaceSpaceWithUnderscore(this.dataList[position].Option)
        fileName += ".3gp"
        filePath = addQuestionsFragment.requireContext().getExternalFilesDir(null)!!.absolutePath
        filePath += "/$fileName"
    }

    private fun startRecording(){
        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setOutputFile(filePath)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        try {
            mediaRecorder.prepare()
            mediaRecorder.start()
        }catch(e: IllegalStateException){
            e.printStackTrace()
        }catch(e: IOException){
            e.printStackTrace()
        }
    }

    private fun stopRecording(){
        mediaRecorder.stop()
        //mediaRecorder.reset()
        mediaRecorder.release()
    }

    fun addOption(option: OptionInfo){
        dataList.add(option)
        notifyDataSetChanged()
    }

    private fun replaceSpaceWithUnderscore(s: String): String{
        return s.trim().replace(" ", "_")
    }
}