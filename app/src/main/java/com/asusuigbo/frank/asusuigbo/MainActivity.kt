package com.asusuigbo.frank.asusuigbo

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.asusuigbo.frank.asusuigbo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

//Hilt Fragments must be attached to an @AndroidEntryPoint Activity.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.setOnNavigationItemReselectedListener { /* NO-OP */ }

        navController.addOnDestinationChangedListener{_, destination, _ ->
            when(destination.id){
                R.id.allLessonsFragment, R.id.profileFragment,
                    R.id.weeklyNewsFragment -> {
                    //Make toolbar and bottom nav visible
                    bottomNavigationView.visibility = View.VISIBLE
                    binding.layoutToolbar.toolbarMain.visibility = View.VISIBLE
                    //Set toolbar text here
                }
                else -> {
                    bottomNavigationView.visibility = View.GONE
                    binding.layoutToolbar.toolbarMain.visibility = View.GONE
                }
            }

            when(destination.id){
                R.id.allLessonsFragment ->{
                    setToolBarTexts(getString(R.string.lessons_text), true)
                    //set active language here since it should be authenticated by here
                    viewModel.activeLanguage.observe(this, {
                        it?.let{
                            binding.layoutToolbar.currentLanguage.text = it
                        }
                    })
                }
                R.id.profileFragment -> { setToolBarTexts(getString(R.string.profile_text), false)}
                R.id.weeklyNewsFragment -> {setToolBarTexts(getString(R.string.weekly_news), false)}
                R.id.myLanguagesFragment -> {
                    binding.layoutToolbar.toolbarMain.visibility = View.VISIBLE
                    setToolBarTexts(getString(R.string.my_languages), false)
                }
            }
        }
    }

    private fun setToolBarTexts(text: String, isLangVisible: Boolean){
        binding.layoutToolbar.toolbarText.text = text
        binding.layoutToolbar.currentLanguage.visibility = if(isLangVisible) View.VISIBLE else View.GONE
    }

}
