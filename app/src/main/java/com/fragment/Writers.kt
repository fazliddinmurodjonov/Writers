package com.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.ViewPagerAdapter
import com.example.connectionlesson_5_firebase_storage.App
import com.example.connectionlesson_5_firebase_storage.R
import com.example.connectionlesson_5_firebase_storage.databinding.ItemTabLayoutBinding
import com.example.connectionlesson_5_firebase_storage.databinding.WritersFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.utils.CreateDB
import com.utils.LoadDataFromFireStore

class Writers : Fragment(R.layout.writers_fragment) {
    private val binding: WritersFragmentBinding by viewBinding()
    lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var typeList: ArrayList<String>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(binding)
        {
            typeList = ArrayList(listOf(*resources.getStringArray(R.array.literatures)))
            viewPagerAdapter = ViewPagerAdapter(requireActivity())
            binding.literatureViewPager.adapter = viewPagerAdapter
            TabLayoutMediator(literatureTabLayout, literatureViewPager) { tab, position ->
            }.attach()
            setTab()
            searchBtn.setOnClickListener {
                findNavController().navigate(R.id.search)
            }
        }

    }

    private fun setTab() {
        val tabCount = binding.literatureTabLayout.tabCount
        for (i in 0 until tabCount) {
            val itemTabLayout =
                ItemTabLayoutBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            val tabAt = binding.literatureTabLayout.getTabAt(i)
            tabAt?.customView = itemTabLayout.root
            itemTabLayout.typeOfLiterature.text = typeList[i]

        }
    }
}
