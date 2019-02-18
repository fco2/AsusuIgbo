package com.asusuigbo.frank.asusuigbo.fragments


import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.R
import com.google.firebase.database.*

class NewestFragment : Fragment() {

    private lateinit var cardView: CardView
    private lateinit var button: Button
    private var isViewShown = false
    private lateinit var englishText: TextView
    private lateinit var translatedText: TextView
    private lateinit var soundsLikeText: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_newest, container, false)

        cardView = view.findViewById(R.id.new_word_text_id)
        button = view.findViewById(R.id.test_button_id)
        englishText = view.findViewById(R.id.english_text_id)
        translatedText = view.findViewById(R.id.translated_text_id)
        soundsLikeText = view.findViewById(R.id.sounds_like_text_id)
        progressBar = view.findViewById(R.id.loading_new_words_id)
        progressBar.visibility = View.VISIBLE
        this.loadDataFromFireBase()

        cardView.visibility = View.INVISIBLE
        button.setOnClickListener(clickListener)
        return view
    }

    private val clickListener = View.OnClickListener {
        when (this.isViewShown) {
            true -> {
                slideUp(this.cardView)
            }
            else -> {
                slideDown(this.cardView)
            }
        }
    }

    private fun slideDown(view: CardView?){
        view!!.visibility = View.VISIBLE
        this.slide(view, 0f, view.height.toFloat() + 15f)
        this.isViewShown = true
        this.button.text = getString(R.string.hide_translation)
    }

    private fun slideUp(view: CardView?){
        this.slide(view, view!!.height.toFloat() + 15f,0f )
        this.isViewShown = false
        this.button.text = getString(R.string.show_translation)
    }

    private fun slide(view: CardView?, fromYDelta: Float, toYDelta: Float){
        val animate = TranslateAnimation(0f, 0f,
                fromYDelta, toYDelta)
        animate.duration = 500
        animate.fillAfter = true
        view!!.startAnimation(animate)
    }

    private fun loadDataFromFireBase(){
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val dbReference: DatabaseReference = database.getReference("DailyWords")

        dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                translatedText.text = p0.child("TranslatedWord").value.toString()
                englishText.text = p0.child("EnglishWords").value.toString()
                soundsLikeText.text = p0.child("SoundsLike").value.toString()
                progressBar.visibility = View.GONE
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}// Required empty public constructor
