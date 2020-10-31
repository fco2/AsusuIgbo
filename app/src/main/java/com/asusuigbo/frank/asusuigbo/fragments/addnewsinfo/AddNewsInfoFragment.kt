package com.asusuigbo.frank.asusuigbo.fragments.addnewsinfo

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels

import com.asusuigbo.frank.asusuigbo.databinding.FragmentAddNewsInfoBinding
import com.asusuigbo.frank.asusuigbo.helpers.Constants.PICK_IMAGE_REQUEST_CODE
import com.asusuigbo.frank.asusuigbo.helpers.Constants.getBeginningOfWeekDate
import com.asusuigbo.frank.asusuigbo.models.NewsInfo
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import timber.log.Timber
import java.util.*

/**
 * A simple [Fragment] subclass.
 */

class AddNewsInfoFragment : Fragment() {

    private lateinit var binding : FragmentAddNewsInfoBinding
    private val addNewsViewModel: AddNewsInfoViewModel by viewModels()
    private lateinit var storageRef: StorageReference
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddNewsInfoBinding.inflate(layoutInflater)
        binding.viewModel = addNewsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.selectImageBtn.setOnClickListener { selectImageToUpload(it) }
        binding.saveButton.setOnClickListener { saveData() }
        addEditTextChangedListeners()
        return binding.root
    }

    private fun selectImageToUpload(view: View) {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    private fun addEditTextChangedListeners(){
        binding.newsTitle.addTextChangedListener {
            addNewsViewModel.setTitle(it.toString())
        }
        binding.newsSource.addTextChangedListener {
            addNewsViewModel.setNewsSource(it.toString())
        }

        binding.content.addTextChangedListener {
            addNewsViewModel.setContent(it.toString())
        }

        binding.date.addTextChangedListener {
            addNewsViewModel.setDate(it.toString())
        }

        binding.language.addTextChangedListener {
            addNewsViewModel.setLanguage(it.toString())
        }
    }

    private fun saveData(){
        //Snackbar.make(requireView(), "${addNewsViewModel.title.value} | ${addNewsViewModel.content.value}", Snackbar.LENGTH_SHORT).show()

        storageRef = FirebaseStorage.getInstance().reference.child("Images")
        dbRef = FirebaseDatabase.getInstance().reference
            .child("Language/${addNewsViewModel.language.value}/WeeklyNews/${getBeginningOfWeekDate()}")

        dbRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val fileNameAndExt = "${System.currentTimeMillis()}.${getFileExt()}"

                storageRef.child(fileNameAndExt).putFile(addNewsViewModel.imgUri.value!!).addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener {uri ->
                        val downloadUrl = uri.toString()
                        val newsInfo = NewsInfo(
                            addNewsViewModel.title.value!!,
                            addNewsViewModel.newsSource.value!!,
                            "",
                            downloadUrl,
                            addNewsViewModel.content.value!!,
                            "",
                            addNewsViewModel.date.value!!
                        )
                        val key = dbRef.push().key!!
                        dbRef.child(key).setValue(newsInfo)
                        Snackbar.make(requireView(), "Added News Item!", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getFileExt(): String{
        val cr = requireContext().contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getExtensionFromMimeType(cr.getType(addNewsViewModel.imgUri.value!!))!!
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PICK_IMAGE_REQUEST_CODE && data != null && data.data != null && resultCode == RESULT_OK){
            addNewsViewModel.setImgUri(data.data!!)
            Picasso.with(requireContext()).load(addNewsViewModel.imgUri.value).into(binding.selectImageBtn)
            Snackbar.make(requireView(), "Selected Image!", Snackbar.LENGTH_SHORT).show()
        }
    }
}
