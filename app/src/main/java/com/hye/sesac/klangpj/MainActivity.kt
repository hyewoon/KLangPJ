package com.hye.sesac.klangpj

import androidx.lifecycle.lifecycleScope
import android.os.Bundle
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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


        lifecycleScope.launch(Dispatchers.IO) {
            uploadExcelToFirestore()
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

    private suspend fun uploadExcelToFirestore() {
        val excelToFireStore = ExcelToFireStore(this)
        excelToFireStore.uploadExcelToFirestoreWithBatch()

    }



}