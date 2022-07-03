package com.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.example.connectionlesson_5_firebase_storage.App
import com.example.connectionlesson_5_firebase_storage.R
import com.example.connectionlesson_5_firebase_storage.databinding.ItemSpinnerBinding

class SpinnerAdapter(var list: List<String>, var context: Context) : BaseAdapter() {

    override fun isEnabled(position: Int): Boolean = position != 0

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): String = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return super.getDropDownView(position, convertView, parent)
    }

    @SuppressLint("ViewHolder", "ResourceAsColor")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding =
            ItemSpinnerBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        binding.categoryName.text = list[position]
        if (position == 0) {
            binding.categoryName.setTextColor(ContextCompat.getColor(context,
                R.color.hintColor))
        }
        var itemView: View
        itemView = binding.root
        isEnabled(position)
        return itemView
    }

}