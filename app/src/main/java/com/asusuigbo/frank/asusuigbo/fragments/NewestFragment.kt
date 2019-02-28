package com.asusuigbo.frank.asusuigbo.fragments


import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.asusuigbo.frank.asusuigbo.FlipAnimation
import com.asusuigbo.frank.asusuigbo.R
import com.google.firebase.database.*

class NewestFragment : Fragment() {

    private lateinit var backCardView: CardView
    private lateinit var frontCardView: CardView
    private lateinit var parentLayout: RelativeLayout
    private lateinit var button: Button
    private lateinit var englishText: TextView
    private lateinit var translatedText: TextView
    private lateinit var soundsLikeText: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_newest, container, false)

        backCardView = view.findViewById(R.id.translated_text_card_view)
        frontCardView = view.findViewById(R.id.english_text_card_view)
        parentLayout = view.findViewById(R.id.fragment_newest_layout_id)
        button = view!!.findViewById(R.id.test_button_id)
        englishText = view.findViewById(R.id.english_text_id)
        translatedText = view.findViewById(R.id.translated_text_id)
        soundsLikeText = view.findViewById(R.id.sounds_like_text_id)
        progressBar = view.findViewById(R.id.loading_new_words_id)
        this.loadDataFromFireBase()
        button.setOnClickListener(clickListener)
        return view
    }

    private val clickListener = View.OnClickListener {
        val flipAnimation = FlipAnimation(frontCardView, backCardView)

        if(frontCardView.visibility == View.GONE){
            flipAnimation.reverse()
            button.text = activity.getText(R.string.show_translation)
        }else{
            button.text = activity.getText(R.string.hide_translation)
        }
        parentLayout.startAnimation(flipAnimation)
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
