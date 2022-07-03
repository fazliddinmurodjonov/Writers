package com.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import by.kirich1409.viewbindingdelegate.viewBinding
import com.adapter.SpinnerAdapter
import com.example.connectionlesson_5_firebase_storage.MainActivity
import com.example.connectionlesson_5_firebase_storage.R
import com.example.connectionlesson_5_firebase_storage.databinding.AddWriterFragmentBinding
import com.example.connectionlesson_5_firebase_storage.databinding.DialogPermissionBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.room.entity.Writer
import com.utils.EmptySpace
import com.utils.NetworkHelper
import com.utils.WriterFirebase
import java.io.File
import java.io.FileOutputStream


class AddWriter : Fragment(R.layout.add_writer_fragment) {
    private val binding: AddWriterFragmentBinding by viewBinding()
    private val arrayList = ArrayList<String>()
    private lateinit var spinnerAdapter: SpinnerAdapter
    var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private var reference: StorageReference = firebaseStorage.getReference("images")
    var firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var imageUri: String? = null
    var downloadUri: String? = null
    var getImgUri = true
    var processingSaveData = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.addImgBtn.setOnClickListener {
            loadImageFromGallery()
        }
        binding.saveBtn.setOnClickListener {
            uploadToFirebase()
        }
        dataOfSpinner()
    }

    private fun uploadToFirebase() {
        if (NetworkHelper(requireContext()).isNetworkConnected()) {
            if (downloadUri != null) {
                val fullName = binding.name.text.toString()
                val yearBirth = binding.birthday.text.toString()
                val yearDied = binding.died.text.toString()
                val typeOfLiterature = binding.typeOfWriterSpn.selectedItemPosition
                val fullInformation = binding.about.text.toString()

                val fullNameEmpty = EmptySpace.emptyAndSpace(fullName)
                val yearBirthEmpty = EmptySpace.emptyAndSpace(yearBirth)
                val yearDiedEmpty = EmptySpace.emptyAndSpace(yearDied)
                val fullInformationEmpty = EmptySpace.emptyAndSpace(fullInformation)
                val checkEmptyFields =
                    fullNameEmpty && yearBirthEmpty && yearDiedEmpty && fullInformationEmpty
                if (checkEmptyFields && typeOfLiterature != 0) {
                    val writer = WriterFirebase(fullName,
                        yearBirth.toInt(),
                        yearDied.toInt(),
                        typeOfLiterature,
                        fullInformation,
                        downloadUri,
                        0)
                    val currentTime = System.currentTimeMillis()
                    firebaseFirestore.collection("writers")
                        .document(currentTime.toString())
                        .set(writer)
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener {

                        }
                    activity?.onBackPressed()
                }

            } else {
                Toast.makeText(requireContext(),
                    "Please, fill fields.",
                    Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(),
                "Please, check your network connection.",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun getImageUri() {
        if (imageUri != null && getImgUri) {
            getImgUri = false
            val currentTime = System.currentTimeMillis()
            val uploadTask = reference.child(currentTime.toString()).putFile(imageUri!!.toUri())
            uploadTask.addOnSuccessListener {
                if (it.task.isSuccessful) {
                    val downloadUrl = it.metadata?.reference?.downloadUrl
                    downloadUrl?.addOnSuccessListener { imgUrl ->
                        downloadUri = imgUrl.toString()
                    }
                }
            }
        }
    }

    private fun loadImageFromGallery() {
        pickImageFromGalleryNew()
    }

    private fun pickImageFromGalleryNew() {
        getImageContent.launch("image/*")
    }

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                binding.writerImg.setImageURI(uri)
                imageUri = uri.toString()
                getImageUri()
                getImgUri = true
            }

        }

    private fun dataOfSpinner() {
        arrayList.add("Select category")
        val list = ArrayList(listOf(*resources.getStringArray(R.array.literatures)))
        arrayList.addAll(list)
        spinnerAdapter = SpinnerAdapter(arrayList, requireActivity())
        binding.typeOfWriterSpn.adapter = spinnerAdapter
    }
}
