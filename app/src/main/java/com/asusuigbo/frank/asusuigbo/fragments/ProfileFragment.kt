package com.asusuigbo.frank.asusuigbo.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.asusuigbo.frank.asusuigbo.AddQuestionActivity
import com.asusuigbo.frank.asusuigbo.LoginActivity
import com.asusuigbo.frank.asusuigbo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException
import java.lang.IllegalStateException


class ProfileFragment : Fragment() {

    private lateinit var signOutBtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var username: TextView
    private lateinit var lessonsCompleted: TextView
    private lateinit var wordsLearned: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var recordButton: ImageView
    private lateinit var mediaRecorder: MediaRecorder
    private var filePath = ""
    private lateinit var recordNotificationText: TextView
    private var counter = 0

    private lateinit var playButton: Button
    private lateinit var addQuestionButton: Button

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        signOutBtn = view.findViewById(R.id.sign_out_id)
        username = view.findViewById(R.id.username_id)
        lessonsCompleted = view.findViewById(R.id.lessons_completed_value)
        wordsLearned = view.findViewById(R.id.words_learned_value)
        progressBar = view.findViewById(R.id.progress_bar_profile_id)
        recordButton = view.findViewById(R.id.record_audio_button_id)
        recordNotificationText = view.findViewById(R.id.record_notification_id)
        playButton = view.findViewById(R.id.play_button)
        addQuestionButton = view.findViewById(R.id.add_question_id)
        progressBar.visibility = View.VISIBLE

        auth = FirebaseAuth.getInstance()
        setUserName()
        signOutBtn.setOnClickListener(signOutClickListener)
        recordButton.setOnTouchListener(recordButtonTouchListener)
        playButton.setOnClickListener(playOnClickListener)
        addQuestionButton.setOnClickListener(addQuestionClickListener)
        return view
    }

    private val addQuestionClickListener = View.OnClickListener{
        startActivity(Intent(activity!!.applicationContext, AddQuestionActivity::class.java))
    }

    private val playOnClickListener = View.OnClickListener{
        val storageRef = FirebaseStorage.getInstance().reference
        storageRef.child("Audio/test_audio.3gp").downloadUrl.addOnSuccessListener {
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(it.toString())
            mediaPlayer.setOnPreparedListener{player ->
                player.start()
            }
            mediaPlayer.prepareAsync()
        }
    }

    private var recordButtonTouchListener = View.OnTouchListener{ _: View, motionEvent: MotionEvent ->
        if(motionEvent.action == MotionEvent.ACTION_DOWN)
            startRecording()
        else
        if(motionEvent.action == MotionEvent.ACTION_UP)
            stopRecording()
        true
    }

    private fun startRecording(){
        mediaRecorder = MediaRecorder()
        recordNotificationText.text = getString(R.string.started_recording)
        filePath = Environment.getExternalStorageDirectory().absolutePath
        filePath += "/test_audio_$counter.3gp"
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
        counter += 1
    }

    private fun stopRecording(){
        mediaRecorder.stop()
        mediaRecorder.release()
        recordNotificationText.text = getString(R.string.stopped_recording)
        saveRecordingToFireBase()
    }

    private val signOutClickListener = View.OnClickListener {

       /* if(auth.currentUser != null){
            auth.signOut()
            startActivity(Intent(activity!!.applicationContext, LoginActivity::class.java))
        }*/
    }

    private fun setUserName(){
        val dbReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        dbReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                username.text = p0.child("Users").child(auth.currentUser!!.uid).child("Username").value.toString()
                wordsLearned.text = p0.child("Users").child(auth.currentUser!!.uid)
                        .child("WordsLearned").value.toString()
                lessonsCompleted.text = p0.child("Users").child(auth.currentUser!!.uid)
                        .child("LessonsCompleted").value.toString()

                progressBar.visibility = View.GONE
            }
        })
    }

    private fun saveRecordingToFireBase() {
        var storageRef = FirebaseStorage.getInstance().reference
        storageRef = storageRef.child("Audio/test_audio.3gp")
        val uri = Uri.fromFile(File(filePath))
        storageRef.putFile(uri).addOnSuccessListener {
            //val x = it.uploadSessionUri
            Log.d("MY_TAG", "filepath: $filePath")
            Log.d("MY_TAG", "II: ${it.uploadSessionUri.toString()}")
        }
    }
}// Required empty public constructor

// D/MY_TAG: II: https://firebasestorage.googleapis.com/v0/b/asusu-igbo.appspot.com/o?name=Audio%2Ftest_audio.3gp&uploadType=resumable&upload_id=AEnB2Uqhn8HSPeNI80rloFE6VnU1PodZFBH9rj3MnjBhgPBUJta-5UWEDhTJ7EjgOIhYyU2mrPwlxdbICPqnE0ARa2uMoHcTLg&upload_protocol=resumable
