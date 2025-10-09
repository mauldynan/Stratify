package com.example.workspave

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.workspave.databinding.FragmentCreateWorkspaceBinding

class CreateWorkspaceFragment : Fragment() {

    private var _binding: FragmentCreateWorkspaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateWorkspaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        generateAndSetWorkspaceId()

        binding.etWorkspaceId.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_refresh, 0)
        binding.etWorkspaceId.setOnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                binding.etWorkspaceId.compoundDrawables[DRAWABLE_RIGHT]?.let { drawable ->
                    if (event.rawX >= (binding.etWorkspaceId.right - drawable.bounds.width())) {
                        generateAndSetWorkspaceId()
                        return@setOnTouchListener true
                    }
                }
            }
            return@setOnTouchListener false
        }

        binding.btnCreateWorkspace.setOnClickListener {
            val workspaceId = binding.etWorkspaceId.text.toString()
            val workspaceName = binding.etName.text.toString()
            val password = binding.etPassword.text.toString()
            val creatorName = "Nama Anda" // Ganti dengan nama user yang login

            //validasi name
            if (workspaceName.isBlank()) {
                binding.etName.error = "Nama Workspace tidak boleh kosong"
                return@setOnClickListener
            }

            //validasi password
            if (password.isBlank()) {
                binding.etPassword.error = "Password tidak boleh kosong"
                return@setOnClickListener
            }

            // Hapus kode SharedPreferences yang lama
            // val prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            // prefs.edit().putBoolean("has_workspace", true).apply()

            val newWorkspace = Workspace(id = workspaceId, name = workspaceName, creatorName = creatorName)
            val bundle = bundleOf("new_workspace" to newWorkspace)

            findNavController().navigate(R.id.action_createWorkspaceFragment_to_workspaceListFragment, bundle)
        }
    }

    private fun generateAndSetWorkspaceId() {
        val allowedChars = ('A'..'Z') + ('0'..'9')
        val workspaceId = (1..6)
            .map { allowedChars.random() }
            .joinToString("")
        binding.etWorkspaceId.setText(workspaceId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}