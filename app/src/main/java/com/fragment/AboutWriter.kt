package com.fragment

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.connectionlesson_5_firebase_storage.App
import com.example.connectionlesson_5_firebase_storage.R
import com.example.connectionlesson_5_firebase_storage.databinding.WriterFragmentBinding
import com.google.android.material.appbar.AppBarLayout
import com.like.LikeButton
import com.like.OnLikeListener
import com.room.database.WriterDatabase
import com.room.entity.Writer
import com.room.entity.WriterFavourite
import com.utils.CreateDB


class AboutWriter : Fragment(R.layout.writer_fragment) {
    private val binding: WriterFragmentBinding by viewBinding()
    var searchText = true
    val db = CreateDB.db
    lateinit var writer: Writer
    var like: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadWriterData()
        process()
        if (writer.favourite == 1) {
            binding.bookmarkBtn.isLiked = true
            getBookmark()
            like = true
        }
        binding.searchBtn.setOnClickListener {
            searchText = false
            visibility(searchText)
        }
        binding.close.setOnClickListener {
            closeBtn()
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.bookmarkBtn.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                like = true
                binding.bookmarkView.setBackgroundResource(R.drawable.background_bookmark_writer_green)
                binding.bookmarkBtn.setLikeDrawableRes(R.drawable.art_bookmark)
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
                like = true
            }

            override fun unLiked(likeButton: LikeButton?) {
                binding.bookmarkView.setBackgroundResource(R.drawable.background_bookmark_writer)
                writer.favourite = 0
                val writerFavourite = db.writerFavouriteDao().getWriterById(writer.id!!)
                db.writerDao().updateWriter(writer)
                db.writerFavouriteDao().deleteWriter(writerFavourite)
                like = false
            }
        })


    }

    private fun getBookmark() {
        binding.bookmarkView.setBackgroundResource(R.drawable.background_bookmark_writer_green)
        binding.bookmarkBtn.setLikeDrawableRes(R.drawable.art_bookmark)
    }

    private fun loadWriterData() {
        val writerId = arguments?.getInt("writerId")
        writer = db.writerDao().getWriterById(writerId!!)
        binding.writerBirthDied.text = "(${writer.yearBirth} - ${writer.yearDied})"
        binding.collapsingToolBar.title = writer.fullName
        binding.writerBio.text = writer.fullInformation
        Glide.with(App.instance).load(writer.image).placeholder(R.drawable.writer_place_holder)
            .into(binding.writerImg)
    }

    private fun closeBtn() {
        if (binding.searchEt.text.isNotEmpty()) {
            val str = binding.searchEt.text.toString()
            textHighLight(str, R.color.lightInk)
            binding.searchEt.setText("")
        } else {
            searchText = true
            visibility(searchText)
            binding.collapsingToolBar.title = writer.fullName
        }
    }

    private fun visibility(visibility: Boolean) {
        with(binding)
        {
            searchBtn.isVisible = searchText
            backBtn.isVisible = searchText
            bookmarkLayout.isVisible = searchText
            searchLayout.isVisible = !searchText
        }

    }

    private fun process() {
        binding.toolbar.elevation = 0F
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.collapsingToolBar.setCollapsedTitleTypeface(resources.getFont(R.font.lato_regular))
            binding.collapsingToolBar.setExpandedTitleTypeface(resources.getFont(R.font.lato_regular))
            // binding.collapsingToolBar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
            //binding.collapsingToolBar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        }
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val str = binding.searchEt.text.toString()
                if (str.length == 1) {
                    textHighLight(str, R.color.lightInk)
                } else {
                    textHighLight(str, R.color.textHighLight)
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (kotlin.math.abs(verticalOffset) == appBarLayout.totalScrollRange) {
                // CLOSE
                if (!searchText) {
                    binding.collapsingToolBar.title = ""
                }
                binding.bookmarkView.setBackgroundResource(R.drawable.background_bookmark_writer)
                binding.bookmarkBtn.setLikeDrawableRes(R.drawable.art_bookmark_green)

            } else {
                // OPEN
                if (like) {
                    getBookmark()
                }

                binding.collapsingToolBar.title = writer.fullName
            }
        })
    }

    private fun textHighLight(str: String, color: Int) {
        binding.writerBio.setTextToHighlight("$str")
        binding.writerBio.setTextHighlightColor(color);
        binding.writerBio.highlight();
    }
}

