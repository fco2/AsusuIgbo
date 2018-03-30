package com.asusuigbo.frank.asusuigbo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class LessonActivity : AppCompatActivity() {

    var textItem: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        textItem = findViewById(R.id.lesson_activity_text_id)
        val text = intent.extras["LESSON_NAME"]
        textItem!!.text = text.toString()
    }
}
