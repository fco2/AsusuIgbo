package com.asusuigbo.frank.asusuigbo.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusuigbo.frank.asusuigbo.adapters.chooselang.ChooseLangAdapter
import com.asusuigbo.frank.asusuigbo.adapters.chooselang.ChooseLangClickListener
import com.asusuigbo.frank.asusuigbo.databinding.ActivityChooseLangBinding
import com.asusuigbo.frank.asusuigbo.models.LanguageInfo
import com.google.android.material.snackbar.Snackbar

class ChooseLangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseLangBinding
    private var langList = ArrayList<LanguageInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView(){
        populateList()
        val manager = LinearLayoutManager(this)
        binding.chooseLangRecyclerView.layoutManager = manager
        binding.chooseLangRecyclerView.hasFixedSize()
        val adapter = ChooseLangAdapter(ChooseLangClickListener {
            //TODO: implement going to sign up screen
            Snackbar.make(binding.root, "Clicked $it", Snackbar.LENGTH_SHORT).show()
        })
        binding.chooseLangRecyclerView.adapter = adapter
        adapter.submitList(langList)
    }

    private fun populateList(){
        langList.add(LanguageInfo("Igbo"))
        langList.add(LanguageInfo("Oza"))
        langList.add(LanguageInfo("Yoruba"))
    }
}
