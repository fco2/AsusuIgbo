package com.asusuigbo.frank.asusuigbo

import android.annotation.SuppressLint
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.widget.TooltipCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.adapters.OptionInfoAdapter
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.io.IOException
import java.lang.IllegalStateException

class AddQuestionActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var questionTypeSpinner: Spinner
    private lateinit var recordAudioBtn: ImageView
    private lateinit var optionRecyclerView: RecyclerView
    private var questionTypeText = ""
    private var optionList: MutableList<OptionInfo> = mutableListOf()
    private lateinit var fab: FloatingActionButton
    private lateinit var optionAdapter: OptionInfoAdapter
    private lateinit var questionEditText: TextInputEditText
    private lateinit var lessonNameEditText: TextInputEditText
    private lateinit var correctAnswerEditText: TextInputEditText
    private lateinit var mediaRecorder: MediaRecorder
    private var filePath = ""
    private var fileName = ""
    private lateinit var saveBtn: Button

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)
        questionTypeSpinner = findViewById(R.id.question_type_spinner_id)
        recordAudioBtn = findViewById(R.id.record_audio_button_id)
        fab = findViewById(R.id.add_option_btn_id)
        questionEditText = findViewById(R.id.add_question_id)
        lessonNameEditText = findViewById(R.id.lesson_name_id)
        correctAnswerEditText = findViewById(R.id.correct_answer_id)
        saveBtn = findViewById(R.id.save_question)
        initializeSpinner()
        questionTypeSpinner.onItemSelectedListener = this
        //TODO: line below not working
        TooltipCompat.setTooltipText(recordAudioBtn, "Record Audio")
        val manager = LinearLayoutManager(this)
        optionAdapter = OptionInfoAdapter(optionList, this)
        optionRecyclerView = findViewById<RecyclerView>(R.id.option_recycler_view_id).apply{
            setHasFixedSize(true)
            layoutManager = manager
            adapter = optionAdapter
        }
        fab.setOnClickListener(addOptionBtnClickListener)
        recordAudioBtn.setOnTouchListener(recordAudioOnTouchListener)
    }

    private val recordAudioOnTouchListener = View.OnTouchListener{ view: View, motionEvent: MotionEvent ->
        if(motionEvent.action == MotionEvent.ACTION_DOWN){
            Toast.makeText(this, "Started recording..", Toast.LENGTH_SHORT).show()
            startRecording()
        }else
        if(motionEvent.action == MotionEvent.ACTION_UP){
            stopRecording()
            Toast.makeText(this, "Finished recording!", Toast.LENGTH_SHORT).show()
        }
        true
    }

    private fun startRecording(){
        mediaRecorder = MediaRecorder()
        filePath = Environment.getExternalStorageDirectory().absolutePath
        fileName = replaceSpaceWithUnderscore(questionEditText.text.toString())
        filePath += "/$fileName.3gp"
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
        mediaRecorder.release()
    }

    private  val addOptionBtnClickListener = View.OnClickListener {
        optionAdapter.addOption(OptionInfo("", ""))

        //Log.d("MY_TAG", "-> ${questionEditText.text}")
    }

    private fun initializeSpinner(){
        ArrayAdapter.createFromResource(
            this,
            R.array.question_type,
            android.R.layout.simple_list_item_1
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            questionTypeSpinner.adapter = adapter
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        questionTypeText = parent!!.getItemAtPosition(position).toString()
    }

    private fun replaceSpaceWithUnderscore(s: String): String{
        return s.trim().replace(" ", "_")
    }
}
