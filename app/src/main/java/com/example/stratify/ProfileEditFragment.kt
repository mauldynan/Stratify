package com.example.stratify

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.stratify.databinding.FragmentProfileEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage

class ProfileEditFragment : Fragment() {

    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let {
                selectedImageUri = it
                binding.ivProfileImageEdit.setImageURI(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        loadUserData()
        setupClickListeners()
    }

    private fun loadUserData() {
        val user = auth.currentUser
        user?.let {
            binding.etName.setText(it.displayName)
            it.photoUrl?.let {
                Glide.with(this).load(it).into(binding.ivProfileImageEdit)
            }
        }
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ivEditPhotoIcon.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        binding.btnSaveChanges.setOnClickListener {
            saveChanges()
        }
    }

    private fun saveChanges() {
        val name = binding.etName.text.toString().trim()

        if (name.isEmpty()) {
            binding.tilName.error = "Name cannot be empty"
            return
        }

        binding.tilName.error = null

        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(context, "Error: Not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Disable button to prevent multiple clicks
        binding.btnSaveChanges.isEnabled = false

        if (selectedImageUri != null) {
            uploadImageAndUpdateProfile(user.uid, name)
        } else {
            updateProfile(name, user.photoUrl)
        }
    }

    private fun uploadImageAndUpdateProfile(userId: String, name: String) {
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/$userId.jpg")
        selectedImageUri?.let {
            storageRef.putFile(it).addOnSuccessListener { _ ->
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    updateProfile(name, downloadUri)
                }
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.btnSaveChanges.isEnabled = true
            }
        }
    }

    private fun updateProfile(name: String, photoUri: Uri?) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .setPhotoUri(photoUri)
            .build()

        auth.currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            binding.btnSaveChanges.isEnabled = true
            if (task.isSuccessful) {
                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
