package com.asusuigbo.frank.asusuigbo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.Option
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.QuestionGroup
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.QuestionsInfoAdapter
import com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models.SharedData

class LessonActivity : AppCompatActivity() {

    var textItem: TextView? = null
    var recyclerView: RecyclerView? = null
    var dataList: ArrayList<QuestionGroup> = ArrayList()
    var optionList: ArrayList<Option> = ArrayList()
    var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)

        this.button = findViewById(R.id.check_answer_button_id)
        recyclerView = findViewById(R.id.question_recycler_view_id)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        this.populateList()

        recyclerView!!.adapter = QuestionsInfoAdapter(this.optionList)
        this.button!!.setOnClickListener(buttonClickListener)

    }

    private val buttonClickListener = View.OnClickListener {
        Toast.makeText(applicationContext, SharedData.SelectedAnswerIndex.toString(), Toast.LENGTH_LONG).show()
    }

    private fun populateList(){
        this.populateOptions()
        val qg = QuestionGroup("Question 1", this.optionList, 2,
                -1)
        this.dataList.add(qg)
    }

    private fun populateOptions(){
        val a = Option(0, "A option")
        val b = Option(1, "B option")
        val c = Option(2, "C option")
        val d = Option(3, "D option")
        this.optionList.add(a)
        this.optionList.add(b)
        this.optionList.add(c)
        this.optionList.add(d)
    }
}
