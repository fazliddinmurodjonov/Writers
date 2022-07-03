package com.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.connectionlesson_5_firebase_storage.App
import com.example.connectionlesson_5_firebase_storage.R
import com.example.connectionlesson_5_firebase_storage.databinding.DialogAboutProgramBinding
import com.example.connectionlesson_5_firebase_storage.databinding.SettingFragmentBinding
import com.utils.CreateDB
import com.utils.LoadDataFromFireStore
import com.utils.SharedPreference

class Setting : Fragment(R.layout.setting_fragment) {
    private val binding: SettingFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SharedPreference.init(requireContext())
        binding.switchTheme.isChecked = SharedPreference.mode == 1
        with(binding)
        {
            switchTheme.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    SharedPreference.mode = 1
                } else {
                    SharedPreference.mode = 0
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
            share.setOnClickListener {
                shareLink()
            }
            addWriter.setOnClickListener {
                findNavController().navigate(R.id.addWriter)
            }
            aboutApp.setOnClickListener {
                aboutAppDialog()
            }
        }
    }


    private fun aboutAppDialog() {
        val dialog = Dialog(requireActivity())
        val dialogView =
            DialogAboutProgramBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        dialog.setContentView(dialogView.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun shareLink() {
        val appLink = ""
        val share = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "$appLink")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(share, "Share via")
        startActivity(shareIntent)
    }

    override fun onResume() {
        super.onResume()
        val size = CreateDB.db.writerDao().getAllWriters().size
        LoadDataFromFireStore.loadDataForNew(App.instance, size)
    }
}