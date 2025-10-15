package com.example.stratify.view.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.stratify.databinding.FragmentProfileOptionsBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileOptionsFragment : DialogFragment() {

    // 1. Define the listener interface
    interface OnOptionSelectedListener {
        fun onProfileEditSelected()
        fun onAccountSelected()
    }

    private var listener: OnOptionSelectedListener? = null

    private var _binding: FragmentProfileOptionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Correctly get the listener from the activity or parent fragment
        listener = when {
            context is OnOptionSelectedListener -> context
            parentFragment is OnOptionSelectedListener -> parentFragment as OnOptionSelectedListener
            else -> null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileOptionsBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        loadUserProfile()

        binding.optionProfileEdit.setOnClickListener {
            // 2. Call the listener and dismiss the dialog
            if (listener != null) {
                listener?.onProfileEditSelected()
                dismiss()
            } else {
                // This else block is a fallback, in a correct implementation this should not be reached
                Toast.makeText(requireContext(), "Error: Listener not implemented", Toast.LENGTH_SHORT).show()
            }
        }

        binding.optionAccount.setOnClickListener {
            // 2. Call the listener and dismiss the dialog
            if (listener != null) {
                listener?.onAccountSelected()
                dismiss()
            } else {
                 // This else block is a fallback, in a correct implementation this should not be reached
                Toast.makeText(requireContext(), "Error: Listener not implemented", Toast.LENGTH_SHORT).show()
            }
        }

        binding.optionLanguage.setOnClickListener {
            Toast.makeText(requireContext(), "Language settings coming soon!", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun loadUserProfile() {
        val user = auth.currentUser
        if (user != null) {
            binding.tvProfileName.text = user.displayName
            binding.tvProfileEmail.text = user.email
            user.photoUrl?.let {
                Glide.with(this).load(it).circleCrop().into(binding.ivProfileImage)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
