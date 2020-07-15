package com.asusuigbo.frank.asusuigbo.mylanguages

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusuigbo.frank.asusuigbo.adapters.ChooseTextClickListener
import com.asusuigbo.frank.asusuigbo.adapters.mylanguages.MyLanguagesAdapter
import com.asusuigbo.frank.asusuigbo.databinding.ActivityMyLanguagesBinding
import com.asusuigbo.frank.asusuigbo.models.DataInfo
import com.google.android.material.snackbar.Snackbar


class MyLanguagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyLanguagesBinding
    private lateinit var viewModel: MyLanguagesViewModel
    private lateinit var factory: MyLanguagesViewModelFactory
    private var isItemDecorationSet = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyLanguagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = MyLanguagesViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(MyLanguagesViewModel::class.java)
        viewModel.getAllLanguagesData().observe(this, Observer {
            viewModel.setListData(it)
        })
        viewModel.dataList.observe(this, Observer{
            setUpRecyclerView(it)
            Snackbar.make(binding.root, "List updated", Snackbar.LENGTH_SHORT).show()
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
            openAlertDialog()
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
