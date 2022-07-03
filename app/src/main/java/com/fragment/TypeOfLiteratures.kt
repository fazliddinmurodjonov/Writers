package com.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.WriterAdapter
import com.example.connectionlesson_5_firebase_storage.R
import com.example.connectionlesson_5_firebase_storage.databinding.FragmentTypeOfLiteraturesBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.room.database.WriterDatabase
import com.room.entity.Writer
import com.room.entity.WriterFavourite
import com.utils.CreateDB
import com.utils.WriterFirebase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.function.Consumer

private const val arg = "position"

class TypeOfLiteratures : Fragment(R.layout.fragment_type_of_literatures) {
    private var position: Int? = null
    val binding: FragmentTypeOfLiteraturesBinding by viewBinding()
    private val writerAdapter = WriterAdapter()
    val db = CreateDB.db
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            position = it.getInt(arg)
        }
        db.writerDao().getWritersByCategoryFlowable(position!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(@SuppressLint("NewApi")
            object : Consumer<List<Writer>>,
                io.reactivex.rxjava3.functions.Consumer<List<Writer>> {
                override fun accept(t: List<Writer>) {
                    writerAdapter.submitList(t)
                }
            },
                @SuppressLint("NewApi")
                object : Consumer<Throwable>,
                    io.reactivex.rxjava3.functions.Consumer<Throwable> {
                    override fun accept(t: Throwable) {

                    }

                })
        binding.writersRV.adapter = writerAdapter
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


    companion object {
        @JvmStatic
        fun getPosition(position: Int) =
            TypeOfLiteratures().apply {
                arguments = Bundle().apply {
                    putInt(arg, position)
                }
            }
    }
}