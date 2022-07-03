package com.example.connectionlesson_5_firebase_storage

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.connectionlesson_5_firebase_storage.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.room.database.WriterDatabase
import com.room.entity.Writer
import com.utils.CreateDB
import com.utils.LoadDataFromFireStore
import com.utils.NetworkHelper
import com.utils.SharedPreference


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding()
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    val db = CreateDB.db
    var networkHelper = NetworkHelper(App.instance)
    private lateinit var handler: Handler
    var connect = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler(Looper.getMainLooper())
        if (db.writerDao().getAllWriters().isEmpty()) {
            handler.postDelayed(runnable, 0)
        }
        navControllerUI()
        SharedPreference.init(this)
        permissions()
        loadMode()
    }

    private fun navControllerUI() {
        navController = findNavController(R.id.my_nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.writers,
            R.id.bookmark,
            R.id.addWriter,
            R.id.search,
            R.id.setting,
            R.id.aboutWriter,
            R.id.typeOfLiteratures))
        //setupActionBarWithNavController(navController, appBarConfiguration)
        setupSmoothBottomMenu()
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.bottomNav.isVisible = destination.id !in destinationWithoutBottomNav
        }

    }

    private fun loadMode() {
        val mode = SharedPreference.mode
        if (mode == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private val destinationWithoutBottomNav = listOf(R.id.search, R.id.addWriter, R.id.aboutWriter)

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.nav_menu)
        val menu = popupMenu.menu
        binding.bottomNav.setupWithNavController(menu, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun permissions() {
        Dexter.withContext(this@MainActivity)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?,
                ) {

                }
            }).check()
    }

    private var runnable = object : Runnable {
        override fun run() {
            if (networkHelper.isNetworkConnected() && connect) {
                LoadDataFromFireStore.loadData(App.instance)
                connect = false
            }
            if (db.writerDao().getAllWriters().isNotEmpty()) {
                handler.removeCallbacks(this)
            }
            handler.postDelayed(this, 100)
        }

    }

}