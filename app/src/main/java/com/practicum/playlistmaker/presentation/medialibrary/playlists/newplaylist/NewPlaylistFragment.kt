package com.practicum.playlistmaker.presentation.medialibrary.playlists.newplaylist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment: Fragment() {

    private val viewModel: NewPlaylistViewModel by viewModel()

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var bottomNavigationViewLine: View

    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private lateinit var pickMedia : ActivityResultLauncher<PickVisualMediaRequest>
    private var imageUri: Uri? = null
    private var fileString: String? = null

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean->
        if (isGranted) {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            showToastNeedPermission()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPlaylistNameTextWatcher()
        setPlaylistDescriptionTextWatcher()
        setConfirmDialog()
        setPickMedia()
        hideBottomNavigationView()

        binding.playlistPhotoTemplate.setOnClickListener{
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        binding.buttonGoBack.setOnClickListener{
            backPressedLogic()
        }

        binding.buttonCreate.setOnClickListener{
            findNavController().navigateUp()
            if(imageUri != null){
                saveImageToPrivateStorage(imageUri!!)
            }
            viewModel.createPlaylist(
                Playlist(
                    playlistId = null,
                    playlistName = binding.playlistName.text.toString(),
                    playlistDescription = binding.playlistDescription.text.toString(),
                    playlistImage = fileString,
                    addedTracksId = listOf(),
                    addedTracksNumber = 0,
                )
            )
            showToast(binding.playlistName.text.toString())
        }

        onBackPressedDispatcher()
    }

    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher()
        hideBottomNavigationView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigationView()
        _binding = null
    }

    private fun setPlaylistNameTextWatcher(){
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty()){
                    binding.buttonCreate.isEnabled = false
                    binding.playlistName.isActivated = false
                    binding.nameActiveText.visibility = View.GONE
                } else{
                    binding.buttonCreate.isEnabled = true
                    binding.playlistName.isActivated = true
                    binding.nameActiveText.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.playlistName.addTextChangedListener(simpleTextWatcher)
    }

    private fun setPlaylistDescriptionTextWatcher(){
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty()){
                    binding.playlistDescription.isActivated = false
                    binding.descriptionActiveText.visibility = View.GONE
                } else {
                    binding.playlistDescription.isActivated = true
                    binding.descriptionActiveText.visibility = View.VISIBLE
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.playlistDescription.addTextChangedListener(simpleTextWatcher)
    }

    private fun setPickMedia(){
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    imageUri = uri
                    binding.playlistPhotoTemplate.setImageURI(uri)
                } else {
                    Log.d(requireActivity().getString(R.string.photo_picker), requireActivity().getString(R.string.no_media_selected))
                }
            }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), requireActivity().getString(R.string.myalbum))
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        var number = 1
        var file = File(filePath, "cover_$number.jpg")
        while(file.exists()){
            number++
            file = File(filePath, "cover_$number.jpg")
        }
        fileString = file.toString()
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun setConfirmDialog(){
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireActivity().getString(R.string.finish_creating_playlist))
            .setMessage(requireActivity().getString(R.string.all_unsaved_data_will_be_lost))
            .setNegativeButton(requireActivity().getString(R.string.cancel)) { _, _ ->

            }.setPositiveButton(requireActivity().getString(R.string.finish)) { _, _ ->
                findNavController().navigateUp()
            }
    }

    private fun backPressedLogic(){
        if(imageUri != null || binding.playlistName.text.isNotEmpty() || binding.playlistDescription.text.isNotEmpty())
            confirmDialog.show()
        else
            findNavController().navigateUp()
    }

    private fun onBackPressedDispatcher(){
        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backPressedLogic()
            }
        })
    }

    private fun showToast(playlistName: String){
        val message = "Плейлист $playlistName успешно создан"
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
            .show()
    }

    private fun showToastNeedPermission(){
        val message = requireActivity().getString(R.string.need_permission)
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
            .show()
    }

    private fun hideBottomNavigationView(){
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigationViewLine = requireActivity().findViewById(R.id.bottomNavigationViewLine)
        bottomNavigationView.visibility = View.GONE
        bottomNavigationViewLine.visibility = View.GONE
    }

    private fun showBottomNavigationView(){
        bottomNavigationView.visibility = View.VISIBLE
        bottomNavigationViewLine.visibility = View.VISIBLE
    }
}