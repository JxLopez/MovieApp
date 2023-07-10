package com.jxlopez.movieapp.ui.media

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.github.drjacky.imagepicker.ImagePicker
import com.jxlopez.movieapp.databinding.FragmentMediaBinding
import com.jxlopez.movieapp.util.extensions.setLocalImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaFragment : Fragment() {
    private lateinit var binding: FragmentMediaBinding
    private val viewModel: MediaViewModel by viewModels()

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data?.hasExtra(ImagePicker.EXTRA_FILE_PATH)!!) {
                    val uri = it.data?.data!!
                    viewModel.imageUri.postValue(uri)
                    binding.btnUpload.visibility = View.VISIBLE
                    binding.ivImageLoaded.setLocalImage(uri)
                }
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                viewModel.imageUri.postValue(uri)
                binding.btnUpload.visibility = View.VISIBLE
                binding.ivImageLoaded.setLocalImage(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setObservers()
    }

    private fun setListener() {
        binding.llContentSelectImage.setOnClickListener {
            galleryLauncher.launch(
                ImagePicker.with(requireActivity())
                    .crop()
                    .galleryOnly()
                    .setMultipleAllowed(false)
                    .cropFreeStyle()
                    .galleryMimeTypes( // no gif images at all
                        mimeTypes = arrayOf(
                            "image/png",
                            "image/jpg",
                            "image/jpeg"
                        )
                    )
                    .createIntent()
            )
        }
        binding.btnTakePicture.setOnClickListener {
            cameraLauncher.launch(
                ImagePicker.with(requireActivity())
                    .crop()
                    .cameraOnly()
                    .maxResultSize(1080, 1920, true)
                    .createIntent()
            )
        }
        binding.btnUpload.setOnClickListener {
            viewModel.startUploadImage()
        }
    }

    private fun setObservers() {
        viewModel.outputWorkInfos.observe(viewLifecycleOwner, workInfosObserver())
    }

    private fun workInfosObserver(): Observer<List<WorkInfo>> {
        return Observer { listOfWorkInfo ->

            if (listOfWorkInfo.isNullOrEmpty()) {
                return@Observer
            }
            val workInfo = listOfWorkInfo[0]

            if (workInfo.state.isFinished)
                showWorkFinished()
            else
                showWorkInProgress()
        }
    }

    private fun showWorkInProgress() {
        binding.btnUpload.visibility = View.INVISIBLE
        binding.btnTakePicture.visibility = View.GONE
        binding.llContentSelectImage.visibility = View.GONE
        binding.clDivisor.visibility = View.GONE
        binding.animationUpload.visibility = View.VISIBLE
    }

    private fun showWorkFinished() {
        binding.btnUpload.visibility = View.VISIBLE
        binding.btnTakePicture.visibility = View.VISIBLE
        binding.llContentSelectImage.visibility = View.VISIBLE
        binding.clDivisor.visibility = View.VISIBLE
        binding.animationUpload.visibility = View.GONE
        binding.ivImageLoaded.setImageResource(0)
        viewModel.imageUri.postValue(null)
    }
}