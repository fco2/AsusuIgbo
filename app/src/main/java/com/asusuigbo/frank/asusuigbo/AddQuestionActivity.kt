package com.asusuigbo.frank.asusuigbo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.TooltipCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asusuigbo.frank.asusuigbo.adapters.OptionInfoAdapter
import com.asusuigbo.frank.asusuigbo.models.OptionInfo
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddQuestionActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var questionTypeSpinner: Spinner
    private lateinit var recordAudioBtn: ImageView
    private lateinit var optionRecyclerView: RecyclerView
    private var questionTypeText = ""
    private var optionList: MutableList<OptionInfo> = mutableListOf()
    private lateinit var fab: FloatingActionButton
    private lateinit var optionAdapter: OptionInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)
        questionTypeSpinner = findViewById(R.id.question_type_spinner_id)
        recordAudioBtn = findViewById(R.id.record_audio_button_id)
        fab = findViewById(R.id.add_option_btn_id)
        initializeSpinner()
        questionTypeSpinner.onItemSelectedListener = this
        TooltipCompat.setTooltipText(recordAudioBtn, "Record Audio")
        createOptionFirstEntry()
        val manager = LinearLayoutManager(this)
        optionAdapter = OptionInfoAdapter(optionList)
        optionRecyclerView = findViewById<RecyclerView>(R.id.option_recycler_view_id).apply{
            setHasFixedSize(true)
            layoutManager = manager
            adapter = optionAdapter
        }
        fab.setOnClickListener(addOptionClickListener)
    }

    private  val addOptionClickListener = View.OnClickListener {
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

    private fun createOptionFirstEntry(){
        this.optionList.add(OptionInfo("", ""))
    }

}
