package com.asusuigbo.frank.asusuigbo

import android.app.Fragment
import android.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpBottomNavigation()
    }

    private fun setUpBottomNavigation(){
        bottomNavigationView = findViewById(R.id.bottom_nav_view_id)

        if(bottomNavigationView != null){

            val menu: Menu = bottomNavigationView!!.menu
            selectNavMenuItem(menu.getItem(0))

            bottomNavigationView?.setOnNavigationItemSelectedListener { item ->
                selectNavMenuItem(item)
                false
            }
        }
    }

    private fun selectNavMenuItem(menuItem: MenuItem){
        menuItem.isChecked = true

        when(menuItem.itemId){
            R.id.home_icon_id -> initializeFragment(HomeFragment())
            R.id.newest_icon_id -> initializeFragment(NewestFragment())
            R.id.profile_icon_id -> initializeFragment(ProfileFragment())
            else -> initializeFragment(HomeFragment())
        }
    }

    private fun initializeFragment(fragment: Fragment){
        val fm: FragmentManager = fragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.frame_layout_id, fragment)
        ft.commit()
    }
}
