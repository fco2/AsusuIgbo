package com.asusuigbo.frank.asusuigbo.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusuigbo.frank.asusuigbo.R
import com.asusuigbo.frank.asusuigbo.adapters.chooselang.ChooseLangAdapter
import com.asusuigbo.frank.asusuigbo.adapters.chooselang.ChooseLangClickListener
import com.asusuigbo.frank.asusuigbo.auth.signup.SignUpActivity
import com.asusuigbo.frank.asusuigbo.databinding.ActivityChooseLangBinding
import com.asusuigbo.frank.asusuigbo.helpers.ItemOffsetDecoration
import com.asusuigbo.frank.asusuigbo.models.LanguageInfo
import com.google.android.material.snackbar.Snackbar

class ChooseLangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseLangBinding
    private var langList = ArrayList<LanguageInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarMain.setNavigationIcon(R.mipmap.baseline_arrow_back_white_18dp)
        binding.toolbarMain.setNavigationOnClickListener {
            val intent = Intent(this, ChooseLangPromptActivity::class.java)
            startActivityForResult(intent, 0)
            finish()
        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView(){
        populateList()
        val manager = LinearLayoutManager(this)
        val dividerItemOffsetDecoration = DividerItemDecoration(this, manager.orientation)
        val chooseLangAdapter = ChooseLangAdapter(ChooseLangClickListener {
            //Snackbar.make(binding.root, "Clicked $it", Snackbar.LENGTH_SHORT).show()
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra("LANGUAGE", it)
            startActivity(intent)
        })
        binding.chooseLangRecyclerView.apply {
            layoutManager = manager
            hasFixedSize()
            adapter = chooseLangAdapter
            addItemDecoration(dividerItemOffsetDecoration)
            chooseLangAdapter.submitList(langList)
        }
    }

    private fun populateList(){
        langList.add(LanguageInfo("Igbo"))
        langList.add(LanguageInfo("Oza"))
        langList.add(LanguageInfo("Yoruba"))
    }
}
