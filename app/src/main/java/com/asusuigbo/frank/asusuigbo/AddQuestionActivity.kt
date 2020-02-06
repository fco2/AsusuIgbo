package com.asusuigbo.frank.asusuigbo

import android.annotation.SuppressLint
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.adapters.OptionInfoAdapter
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.models.QuestionInfo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException

class AddQuestionActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var questionTypeSpinner: Spinner
    private lateinit var recordAudioBtn: ImageView
    private lateinit var optionRecyclerView: RecyclerView
    private var questionTypeText = ""
    private var optionList: MutableList<OptionInfo> = mutableListOf()
    private lateinit var fab: FloatingActionButton
    private lateinit var optionAdapter: OptionInfoAdapter
    private lateinit var questionEditText: TextInputEditText
    lateinit var lessonNameEditText: TextInputEditText
    private lateinit var correctAnswerEditText: TextInputEditText
    private lateinit var mediaRecorder: MediaRecorder
    private var filePath = ""
    private var fileName = ""
    private var optionFilePath = ""
    private var optionFileName = ""
    private lateinit var saveBtn: Button
    private lateinit var questionGroup: QuestionGroup

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
        val manager = LinearLayoutManager(this)
        optionAdapter = OptionInfoAdapter(optionList, this)
        optionRecyclerView = findViewById<RecyclerView>(R.id.option_recycler_view_id).apply{
            setHasFixedSize(true)
            layoutManager = manager
            adapter = optionAdapter
        }
        fab.setOnClickListener(addOptionBtnClickListener)
        recordAudioBtn.setOnTouchListener(recordAudioOnTouchListener)
        saveBtn.setOnClickListener(saveQuestionClickListener)
        setUpSwipeToDelete()
    }

    private val saveQuestionClickListener = View.OnClickListener{
        populateQuestionGroup()
        saveQuestionGroupToFireBase()
    }

    private fun populateQuestionGroup(){
        val file = File(filePath)
        val audio = if(file.exists())
            "Audio/${lessonNameEditText.text}/$fileName"
        else
            ""
        val qi = QuestionInfo(questionEditText.text.toString(), audio)
        this.questionGroup = QuestionGroup(qi,
            optionList, correctAnswerEditText.text.toString(), questionTypeText)
    }

    private fun saveQuestionGroupToFireBase(){
        val dbRef = FirebaseDatabase.getInstance().reference

        //we will first get size of lesson then increment it by 1 and save.
        dbRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                val lastQuestionIndex=
                    p0.child("Lessons/${lessonNameEditText.text}").children.last().key!!.toInt() + 1
                saveLessonData(lastQuestionIndex)
            }
        })
    }

    private fun saveLessonData(indexToUpdate: Int){
        val dbRef = FirebaseDatabase.getInstance().reference
        //set option audio value
        optionList.forEach {
            optionFilePath = getOptionFilePath(it)

            val file = File(optionFilePath)
            it.Audio = if(file.exists())
                "Audio/${lessonNameEditText.text}/$optionFileName"
            else
                ""
        }
        dbRef.child("Lessons/${lessonNameEditText.text}")
            .child("$indexToUpdate").setValue(questionGroup)
        //save audio for question
        if(File(filePath).exists())
            saveRecordingToFireBase(filePath, questionGroup.QuestionInfo.Audio)
        //save audio for options
        optionList.forEach {
            if(it.Audio.trim() != "") {
                optionFilePath = getOptionFilePath(it)
                saveRecordingToFireBase(optionFilePath, it.Audio)
            }
        }
        Toast.makeText(this, "Saved data successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun getOptionFilePath(it: OptionInfo): String {
        optionFileName = replaceSpaceWithUnderscore(it.Option)
        optionFileName += ".3gp"
        optionFilePath = Environment.getExternalStorageDirectory().absolutePath
        optionFilePath += "/$optionFileName"
        return optionFilePath
    }

    private fun saveRecordingToFireBase(fileToSave: String, fireBaseIdentifier: String) {
        var storageRef = FirebaseStorage.getInstance().reference
        storageRef = storageRef.child(fireBaseIdentifier) // Audio/test_audio.3gp
        val uri = Uri.fromFile(File(fileToSave)) //filePath
        storageRef.putFile(uri).addOnSuccessListener {
            File(fileToSave).delete()
        }
    }

    private val recordAudioOnTouchListener = View.OnTouchListener{ _: View, motionEvent: MotionEvent ->
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
        fileName += ".3gp"
        filePath += "/$fileName"
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
        mediaRecorder.reset()
        mediaRecorder.release()
    }

    private  val addOptionBtnClickListener = View.OnClickListener {
        optionAdapter.addOption(OptionInfo("", ""))
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
            .replace("'", "_")
            .replace("?", "")
    }

    private fun setUpSwipeToDelete(){
        val itemTouchHelperCallback =
            object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    optionList.remove(optionList[viewHolder.adapterPosition])
                    optionAdapter.notifyDataSetChanged()
                    Toast.makeText(applicationContext, "Deleted Item!", Toast.LENGTH_SHORT).show()
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(optionRecyclerView)
    }
}
