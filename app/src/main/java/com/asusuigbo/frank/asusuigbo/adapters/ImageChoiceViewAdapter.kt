package com.asusuigbo.frank.asusuigbo.adapters

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.asusuigbo.frank.asusuigbo.LessonActivity
import com.asusuigbo.frank.asusuigbo.R

class ImageChoiceViewAdapter(private val lessonActivity: LessonActivity) {
    //TODO: set background for selected cardview....
    var imgChoiceLayout: RelativeLayout = lessonActivity.activity.findViewById(R.id.image_choice_layout_id)
    var imgChoiceQuestion: TextView = lessonActivity.activity.findViewById(R.id.img_choice_question_id)
    private var cardViewTopLeft: CardView = lessonActivity.activity.findViewById(R.id.img_choice_card_view_tl)
    private var cardViewTopRight: CardView = lessonActivity.activity.findViewById(R.id.img_choice_card_view_tr)
    private var cardViewBottomLeft: CardView = lessonActivity.activity.findViewById(R.id.img_choice_card_view_bl)
    private var cardViewBottomRight: CardView = lessonActivity.activity.findViewById(R.id.img_choice_card_view_br)
    private var translatedWordTopLeft: TextView = lessonActivity.activity.findViewById(R.id.img_choice_text_top_left)
    private var translatedWordTopRight: TextView = lessonActivity.activity.findViewById(R.id.img_choice_text_top_right)
    private var translatedWordBottomLeft: TextView = lessonActivity.activity.findViewById(R.id.img_choice_text_bottom_left)
    private var translatedWordBottomRight: TextView = lessonActivity.activity.findViewById(R.id.img_choice_text_bottom_right)
    private var imgViewTopLeft: TextView = lessonActivity.activity.findViewById(R.id.img_choice_img_top_left)
    private var imgViewTopRight: TextView = lessonActivity.activity.findViewById(R.id.img_choice_img_top_right)
    private var imgViewBottomLeft: TextView = lessonActivity.activity.findViewById(R.id.img_choice_img_bottom_left)
    private var imgViewBottomRight: TextView = lessonActivity.activity.findViewById(R.id.img_choice_img_bottom_right)

    init{
        cardViewTopLeft.setOnClickListener { v ->
            //TODO: set click listener action
        }

        cardViewTopRight.setOnClickListener { v ->
            //TODO: set click listener action
        }

        cardViewBottomLeft.setOnClickListener { v ->
            //TODO: set click listener action
        }

        cardViewBottomRight.setOnClickListener { v ->
            //TODO: set click listener action
        }
    }

}
















