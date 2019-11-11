package com.asusuigbo.frank.asusuigbo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.asusuigbo.frank.asusuigbo.fragments.ImgChoiceFragment
import com.asusuigbo.frank.asusuigbo.fragments.SingleSelectFragment

class TestDictActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private var currentFragment = "A"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_dict)

        progressBar = findViewById(R.id.progress_bar_test_id)
        progressBar.visibility = View.VISIBLE

        //initialize fragment here
        /*val singleSelectFragment = SingleSelectFragment(this)
        val fragmentManager = supportFragmentManager
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.frame_layout_id, singleSelectFragment)
        ft.commit()
        progressBar.visibility = View.GONE */
    }

    fun toggleFragment(givenFragment: String){
        val fragmentManager = supportFragmentManager
        val ft = fragmentManager.beginTransaction()

       /* if(givenFragment == "B"){
            val imgChoiceFragment = ImgChoiceFragment(this)
            ft.replace(R.id.frame_layout_id, imgChoiceFragment)
        }else{
            val singleSelectFragment = SingleSelectFragment(this)
            ft.replace(R.id.frame_layout_id, singleSelectFragment)
        }
        ft.commit() */
    }
}
