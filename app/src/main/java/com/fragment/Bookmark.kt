package com.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.WriterFavouriteAdapter
import com.example.connectionlesson_5_firebase_storage.R
import com.example.connectionlesson_5_firebase_storage.databinding.BookmarkFragmentBinding
import com.room.entity.Writer
import com.room.entity.WriterFavourite
import com.utils.CreateDB
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.function.Consumer

class Bookmark : Fragment(R.layout.bookmark_fragment) {
    private val binding: BookmarkFragmentBinding by viewBinding()
    val db = CreateDB.db
    private val writerFavouriteAdapter = WriterFavouriteAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.searchBtn.setOnClickListener {
            findNavController().navigate(R.id.search)
        }
        db.writerFavouriteDao().getWritersFlowable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(@SuppressLint("NewApi")
            object : Consumer<List<WriterFavourite>>,
                io.reactivex.rxjava3.functions.Consumer<List<WriterFavourite>> {
                override fun accept(t: List<WriterFavourite>) {
                    writerFavouriteAdapter.submitList(t)
                }
            },
                @SuppressLint("NewApi")
                object : Consumer<Throwable>,
                    io.reactivex.rxjava3.functions.Consumer<Throwable> {
                    override fun accept(t: Throwable) {

                    }

                })

        binding.writersRV.adapter = writerFavouriteAdapter
        adapterClicks()
    }

    private fun adapterClicks() {
        writerFavouriteAdapter.setOnItemClickListener { id ->
            val bundleOf = bundleOf("writerId" to id)
            findNavController().navigate(R.id.aboutWriter, bundleOf)
        }
        writerFavouriteAdapter.setOnFavouriteClickListener { favourite, writer ->
            if (favourite == 0) {
                val writerById = db.writerDao().getWriterById(writer.id!!)
                writerById.favourite = 0
                db.writerDao().updateWriter(writerById)
                db.writerFavouriteDao().deleteWriter(writer)
            }

        }
    }
}