package com.hye.sesac.klangpj

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hye.sesac.klangpj.databinding.ActivityMainBinding
import com.hye.sesac.klangpj.ui.viewmodel.SharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) { //Bundle 이건 -> UI관련된 것 //onSavedInstance
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView.rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            val typedValue = TypedValue()
            theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true)
            Log.d("ThemeCheck", "colorBackground: ${typedValue.data.toString(16)}")

            // 네비게이션 영역도 bottomMargin 대신 padding 사용 권장
            binding.bottomNavView.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }



        with(binding) {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)  // 타이틀 숨기기
            }

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            findViewById<BottomNavigationView>(R.id.bottom_nav_view).setupWithNavController(
                navController
            )
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.homeFragment,
                    R.id.wordGameFragment,
                    R.id.myPageFragment
                )
            )
            setupWithNavController(toolbar, navController, appBarConfiguration)

            //navigation 변경시 타이틀 숨기기
            navController.addOnDestinationChangedListener { _, _, _ ->
                supportActionBar?.apply {
                    setDisplayShowTitleEnabled(false)
                    setDisplayHomeAsUpEnabled(true)
                    toolbar.title = ""

                }

            }


        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main_top_app_bar, menu)

        //발자국 아이콘 찾기
        val pawIcon = menu.findItem(R.id.paw)
        val pawView = pawIcon.actionView
        val pawText: TextView = pawView?.findViewById<TextView>(R.id.paw_count) ?: return false

        //물고기 아이콘 찾기
        val targetIcon = menu.findItem(R.id.crown)
        val targetView = targetIcon.actionView
        val targetText: TextView =
            targetView?.findViewById<TextView>(R.id.reward_count) ?: return false



        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    sharedViewModel.pawPoint.collectLatest {
                        pawText.text = it.toString()
                    }
                }
                launch {
                    sharedViewModel.targetPoint.collectLatest {
                        targetText.text = it.toString()
                    }
                }
            }
        }
        return true

    }


}