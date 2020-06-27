package com.asusuigbo.frank.asusuigbo

import android.annotation.SuppressLint
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.adapters.OptionInfoAdapter
import com.asusuigbo.frank.asusuigbo.databinding.ActivityAddQuestionBinding
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.models.QuestionInfo
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException

class AddQuestionActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityAddQuestionBinding
    private var questionTypeText = ""
    private var optionList: MutableList<OptionInfo> = mutableListOf()
    private lateinit var mediaRecorder: MediaRecorder
    private var filePath = ""
    private var fileName = ""
    private var optionFilePath = ""
    private var optionFileName = ""
    private lateinit var optionAdapter: OptionInfoAdapter
    private lateinit var questionGroup: QuestionGroup
    private lateinit var vibrator: Vibrator

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeQuestionFormatSpinner()
        binding.questionTypeSpinnerId.onItemSelectedListener = this
        val manager = LinearLayoutManager(this)
        optionAdapter = OptionInfoAdapter(optionList, this)
        binding.optionRecyclerViewId.apply{
            setHasFixedSize(true)
            layoutManager = manager
            adapter = optionAdapter
        }
        binding.fab.setOnClickListener(addOptionBtnClickListener)
        binding.recordAudioButtonId.setOnTouchListener(recordAudioOnTouchListener)
        binding.saveQuestionBtn.setOnClickListener(saveQuestionClickListener)
        setUpSwipeToDeleteOption()
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    private val saveQuestionClickListener = View.OnClickListener{
        populateQuestionGroup()
        saveQuestionGroupToFireBase()
    }

    private fun populateQuestionGroup(){
        val file = File(filePath)
        val audio = if(file.exists())
            "Audio/${binding.lessonNameEditText.text}/$fileName"
        else
            ""
        val qi = QuestionInfo(binding.questionEditText.text.toString(), audio)
        this.questionGroup = QuestionGroup(qi, optionList, binding.correctAnswerEditText.text.toString(), questionTypeText)
    }

    private fun saveQuestionGroupToFireBase(){
        val dbRef = FirebaseDatabase.getInstance().reference
        //we will first get size of lesson then increment it by 1 and save.
        dbRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {
                val lastQuestionIndex= if(p0.child("Lessons/${binding.lessonNameEditText.text}").children.count() == 0)
                    0
                else
                    p0.child("Lessons/${binding.lessonNameEditText.text}").children.last().key!!.toInt() + 1
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
                "Audio/${binding.lessonNameEditText.text}/$optionFileName"
            else
                ""
        }
        dbRef.child("Lessons/${binding.lessonNameEditText.text}")
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
        Snackbar.make(binding.root, "Saved data successfully!", Snackbar.LENGTH_SHORT).show()
    }

    private fun getOptionFilePath(it: OptionInfo): String {
        optionFileName = replaceSpaceWithUnderscore(it.Option)
        optionFileName += ".3gp"
        optionFilePath = applicationContext.getExternalFilesDir(null)!!.absolutePath
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

    private val recordAudioOnTouchListener = View.OnTouchListener{ view: View, motionEvent: MotionEvent ->
        if(motionEvent.action == MotionEvent.ACTION_DOWN){
            Snackbar.make(binding.root, "Started recording..", Snackbar.LENGTH_SHORT).show()
            startRecording()
            vibrateForAudio()
        }else
        if(motionEvent.action == MotionEvent.ACTION_UP){
            stopRecording()
            vibrateForAudio()
            Snackbar.make(binding.root, "Finished recording!", Snackbar.LENGTH_SHORT).show()
        }
        true
    }

    fun vibrateForAudio() {
        if (Build.VERSION.SDK_INT >= 26)
            vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        else
            vibrator.vibrate(150)
    }

    private fun startRecording(){
        mediaRecorder = MediaRecorder()
        filePath = applicationContext.getExternalFilesDir(null)!!.absolutePath
        fileName = replaceSpaceWithUnderscore(binding.questionEditText.text.toString()) + ".3gp"
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

    private fun initializeQuestionFormatSpinner(){
        ArrayAdapter.createFromResource(
            this,
            R.array.question_type,
            android.R.layout.simple_list_item_1
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.questionTypeSpinnerId.adapter = adapter
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

    private fun setUpSwipeToDeleteOption(){
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
                    Snackbar.make(binding.root, "Deleted Item!", Snackbar.LENGTH_SHORT).show()
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.optionRecyclerViewId)
    }
}
