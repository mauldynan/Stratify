package com.example.stratify

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.stratify.databinding.FragmentProfileOptionsBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileOptionsFragment : DialogFragment() {

    // 1. DEFINE THE LISTENER INTERFACE
    // This interface acts as a contract that the host must follow.
    interface OnOptionSelectedListener {
        fun onProfileEditSelected()
        fun onAccountSelected()
        fun onLanguagesSelected()
    }

    private var _binding: FragmentProfileOptionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    // 2. DECLARE THE LISTENER VARIABLE
    private var listener: OnOptionSelectedListener? = null

    // 3. IMPLEMENT onAttach TO CONNECT THE LISTENER
    // This method is called when the dialog attaches to its host (Activity or Fragment).
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // A dialog can be hosted by an Activity or a Fragment. We need to check both.
        // First, check if the parent fragment implements the listener.
        if (parentFragment is OnOptionSelectedListener) {
            listener = parentFragment as OnOptionSelectedListener
        }
        // If not, check if the hosting activity implements the listener.
        else if (context is OnOptionSelectedListener) {
            listener = context
        }
        // If neither does, the dialog can't communicate. Crash with a helpful message.
        else {
            throw ClassCastException("$context must implement OnOptionSelectedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.DialogAnimation)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileOptionsBinding.inflate(inflater, container, false)
        dialog?.setCanceledOnTouchOutside(true)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            it.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            it.setGravity(Gravity.END)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        loadUserProfile()
        setupClickListeners()
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        currentUser?.let {
            binding.tvProfileName.text = it.displayName ?: "No Name"
            binding.tvProfileEmail.text = it.email ?: "No Email"

            it.photoUrl?.let { url ->
                Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_profile)
                    .into(binding.ivProfileImage)
            }
        }
    }

    // 4. MODIFY CLICK LISTENERS TO USE THE INTERFACE
    // Instead of navigating, the buttons now call the listener methods.
    private fun setupClickListeners() {
        binding.optionProfileEdit.setOnClickListener {
            listener?.onProfileEditSelected()
            dismiss() // Close the dialog after an option is selected
        }

        binding.optionAccount.setOnClickListener {
            listener?.onAccountSelected()
            dismiss() // Close the dialog
        }

        binding.optionLanguage.setOnClickListener {
            listener?.onLanguagesSelected()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ProfileOptionsFragment"
    }
}
