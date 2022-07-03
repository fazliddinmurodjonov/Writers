package com.adapter

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.connectionlesson_5_firebase_storage.App
import com.example.connectionlesson_5_firebase_storage.R
import com.example.connectionlesson_5_firebase_storage.databinding.ItemWriterBinding
import com.like.LikeButton
import com.like.OnLikeListener
import com.room.entity.Writer

class SearchWriterAdapter() : ListAdapter<Writer, SearchWriterAdapter.ViewHolder>(MyDiffUtil()) {
    lateinit var itemClick: OnItemClickListener
    lateinit var favouriteClick: OnFavouriteClickListener

    fun interface OnFavouriteClickListener {
        fun onClick(favourite: Int, writer: Writer)
    }

    fun interface OnItemClickListener {
        fun onClick(id: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }

    fun setOnFavouriteClickListener(listener: OnFavouriteClickListener) {
        favouriteClick = listener
    }

    inner class ViewHolder(var binding: ItemWriterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(writer: Writer) {
            binding.writerName.text = writer.fullName
            binding.bookmarkBtn.isLiked = false
            binding.writerDateOfBornDied.text = "${writer.yearBirth}-${writer.yearDied}"
            Glide.with(App.instance).load(writer.image).into(binding.writerImg)
            if (writer.favourite == 1) {
                binding.bookmarkBtn.isLiked = true
                binding.bookmarkBack.setBackgroundResource(R.drawable.background_bookmark_green)
            } else {
                binding.bookmarkBtn.isLiked = false
                binding.bookmarkBack.setBackgroundResource(R.drawable.background_bookmark_white)
            }
            binding.cardView.setOnClickListener {
                itemClick.onClick(writer.id!!)
            }
            binding.bookmarkBtn.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {
                    binding.bookmarkBack.setBackgroundResource(R.drawable.background_bookmark_green)
                    favouriteClick.onClick(1, writer)
                }

                override fun unLiked(likeButton: LikeButton?) {
                    binding.bookmarkBack.setBackgroundResource(R.drawable.background_bookmark_white)
                    favouriteClick.onClick(0, writer)
                }

            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemWriterBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Writer>() {
        override fun areItemsTheSame(oldItem: Writer, newItem: Writer): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Writer, newItem: Writer): Boolean {
            return when {
                oldItem.favourite != newItem.favourite -> {
                    false
                }
                else -> true
            }
        }

    }
}