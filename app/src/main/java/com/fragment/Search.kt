package com.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.WriterAdapter
import com.example.connectionlesson_5_firebase_storage.MainActivity
import com.example.connectionlesson_5_firebase_storage.R
import com.example.connectionlesson_5_firebase_storage.databinding.SearchFragmentBinding
import com.room.entity.Writer
import com.room.entity.WriterFavourite
import com.utils.CreateDB
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.function.Consumer


class Search : Fragment(R.layout.search_fragment) {
    private val binding: SearchFragmentBinding by viewBinding()
    val db = CreateDB.db
    val writerAdapter = WriterAdapter()
    val list = db.writerDao().getAllWriters()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.close.setOnClickListener {
            findNavController().popBackStack()
        }
//        db.writerDao().getWritersFlowable()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(@SuppressLint("NewApi")
//            object : Consumer<List<Writer>>,
//                io.reactivex.rxjava3.functions.Consumer<List<Writer>> {
//                override fun accept(t: List<Writer>) {
//                    writerAdapter.submitList(t)
//                }
//            })
        writerAdapter.submitList(list)
        binding.writersRV.adapter = writerAdapter
        textChanged()
        adapterClicks()
    }

    private fun adapterClicks() {
        writerAdapter.setOnItemClickListener { id ->
            val bundleOf = bundleOf("writerId" to id)
            findNavController().navigate(R.id.aboutWriter, bundleOf)
        }
        writerAdapter.setOnFavouriteClickListener { favourite, writer ->
            if (favourite == 1) {
                writer.favourite = 1
                var writerFavourite = WriterFavourite(writer.id,
                    writer.fullName,
                    writer.yearBirth,
                    writer.yearDied,
                    writer.typeOfLiterature,
                    writer.fullInformation,
                    writer.image,
                    writer.favourite)
                db.writerDao().updateWriter(writer)
                db.writerFavouriteDao().insertWriter(writerFavourite)
            } else {
                writer.favourite = 0
                val writerFavourite = db.writerFavouriteDao().getWriterById(writer.id!!)
                db.writerDao().updateWriter(writer)
                db.writerFavouriteDao().deleteWriter(writerFavourite)
            }
        }

    }


    private fun textChanged() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = binding.searchEt.text.toString()
                if (str.length == 1) {
                    //textHighLight(str, R.color.lightInk)
                } else {
                    //textHighLight(str, R.color.textHighLight)
                }
            }

            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })
    }

    fun filter(text: String) {
        val filteredList = ArrayList<Writer>()
        val writersList = db.writerDao().getAllWriters()
        for (writer in writersList) {
            if (writer.fullName?.lowercase()?.contains(text.lowercase()) == true) {
                filteredList.add(writer)
            }
        }
        writerAdapter.submitList(filteredList)
    }

}
