package com.hye.sesac.klangpj

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hye.sesac.klangpj.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView.rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

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
                    R.id.rankFragment,
                    R.id.myPageFragment
                )
            )
            setupWithNavController(toolbar, navController, appBarConfiguration)


        }


    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        try {
            // 예외를 발생시킬 수 있는 코드
            if (hasFocus) {
                // 창이 포커스를 얻을 때 수행할 작업
            } else {
                // 창이 포커스를 잃을 때 수행할 작업
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "onWindowFocusChanged에서 예외 발생", e)
            // 예외를 정상적으로 처리
        }

    }


}