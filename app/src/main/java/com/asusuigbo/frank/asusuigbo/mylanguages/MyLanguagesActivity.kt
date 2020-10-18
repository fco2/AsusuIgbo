package com.asusuigbo.frank.asusuigbo.mylanguages

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusuigbo.frank.asusuigbo.adapters.ChooseTextClickListener
import com.asusuigbo.frank.asusuigbo.adapters.mylanguages.MyLanguagesAdapter
import com.asusuigbo.frank.asusuigbo.databinding.ActivityMyLanguagesBinding
import com.asusuigbo.frank.asusuigbo.models.DataInfo
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyLanguagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyLanguagesBinding
    private val viewModel: MyLanguagesViewModel by viewModels()
    private var isItemDecorationSet = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyLanguagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getAllLanguagesData().observe(this, {
            viewModel.setListData(it)
        })
        viewModel.dataList.observe(this, {
            setUpRecyclerView(it)
        })
    }

    private fun openAlertDialog(){
        var chosenLanguage: String
        val list = arrayOf("Igbo", "Oza", "Yoruba")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Languages")
        builder.setItems(list) { _, which ->
            chosenLanguage = when (which) {
                0 -> "Igbo"
                1 -> "Oza"
                2 -> "Yoruba"
                else -> "Igbo"
            }
            binding.progressBar.visibility = View.VISIBLE
            viewModel.setNewActiveLanguage(chosenLanguage)
            Snackbar.make(binding.root, "$chosenLanguage is now active!", Snackbar.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
        }.create().show()
    }

    private fun setUpRecyclerView(it: List<DataInfo>) {
        val manager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, manager.orientation)
        val myLanguagesAdapter = MyLanguagesAdapter(ChooseTextClickListener {
            if(it == "+ Add Language") {
                openAlertDialog()
            }else{
                viewModel.setNewActiveLanguage(it)
            }
        })
        binding.languagesRecyclerView.apply {
            layoutManager = manager
            hasFixedSize()
            if(!isItemDecorationSet){
                addItemDecoration(itemDecoration)
                isItemDecorationSet = true
            }

            adapter = myLanguagesAdapter
            myLanguagesAdapter.submitList(it)
        }
    }
}
