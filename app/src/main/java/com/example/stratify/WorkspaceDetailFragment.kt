package com.example.stratify

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stratify.databinding.FragmentWorkspaceDetailBinding
import java.util.Date



class WorkspaceDetailFragment : Fragment() {

    private var _binding: FragmentWorkspaceDetailBinding? = null
    private val binding get() = _binding!!

    private val args: WorkspaceDetailFragmentArgs by navArgs()

    private val informationList = mutableListOf<InformationItem>()
    private lateinit var informationAdapter: InformationAdapter
    private var isDeleteMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkspaceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- MENGATUR TAMPILAN AWAL ---
        binding.tvWorkspaceTitle.text = args.workspace.name
        binding.chipMembers.text = "No member"

        val initialStatusColor = when (args.workspace.status) {
            "In Progress" -> R.color.status_inprogress
            "To Verify" -> R.color.status_toverify
            "Done" -> R.color.status_done
            else -> R.color.status_todo
        }
        binding.tvStatusText.text = args.workspace.status
        binding.btnStatus.backgroundTintList = ContextCompat.getColorStateList(requireContext(), initialStatusColor)

        // --- MENGAKTIFKAN SEMUA FITUR ---
        setupInlineEditor()
        setupInformationList()
        updateDeleteModeUI()

        // --- LISTENER UNTUK TOMBOL ---
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnToggleDeleteMode.setOnClickListener {
            isDeleteMode = !isDeleteMode
            updateDeleteModeUI()
        }

        binding.btnAddInfo.setOnClickListener {
            informationList.forEach { it.isEditing = false }
            val newItem = InformationItem(details = "", isEditing = true)
            informationList.add(newItem)
            informationAdapter.notifyDataSetChanged()
            binding.rvInformation.scrollToPosition(informationList.size - 1)
        }

        binding.btnConfirmDelete.setOnClickListener {
            val itemsToDelete = informationList.filter { it.isSelectedForDeletion }
            if (itemsToDelete.isNotEmpty()) {
                informationList.removeAll(itemsToDelete.toSet())
                informationAdapter.notifyDataSetChanged()
            }
            isDeleteMode = false
            updateDeleteModeUI()
        }

        binding.btnStatus.setOnClickListener {
            val statusDialog = StatusDialogFragment()
            statusDialog.show(childFragmentManager, "StatusDialog")
        }

        binding.chipMembers.setOnClickListener {
            val membersDialog = MembersDialogFragment()
            membersDialog.show(childFragmentManager, "MembersDialog")
        }

        childFragmentManager.setFragmentResultListener("status_request", viewLifecycleOwner) { _, bundle ->
            val newStatusName = bundle.getString("new_status_name") ?: "To Do"
            val newStatusColor = bundle.getInt("new_status_color") ?: R.color.status_todo

            binding.tvStatusText.text = newStatusName
            binding.btnStatus.backgroundTintList = ContextCompat.getColorStateList(requireContext(), newStatusColor)

            args.workspace.status = newStatusName
            sendResultBack()
        }
    }

    private fun sendResultBack() {
        parentFragmentManager.setFragmentResult("workspace_update_request", bundleOf("updated_workspace" to args.workspace))
    }

    // --- FUNGSI UNTUK MENGATUR MODE DELETE ---
    private fun updateDeleteModeUI() {
        if (::informationAdapter.isInitialized) {
            informationAdapter.setDeleteMode(isDeleteMode)
        }
        if (!isDeleteMode) {
            informationList.forEach { it.isSelectedForDeletion = false }
        }
        binding.btnToggleDeleteMode.text = if (isDeleteMode) "Cancel" else "Delete"
        binding.btnConfirmDelete.isVisible = isDeleteMode
        binding.btnAddInfo.isVisible = !isDeleteMode
    }

    // --- FUNGSI UNTUK SETUP RECYCLERVIEW ---
    private fun setupInformationList() {
        informationAdapter = InformationAdapter(
            items = informationList,
            onActionClick = { item, position ->
                if (!isDeleteMode) {
                    if (item.isEditing) {
                        item.isEditing = false
                        val holder = binding.rvInformation.findViewHolderForAdapterPosition(position) as? InformationAdapter.InformationViewHolder
                        item.details = holder?.binding?.etInfoDetails?.text.toString().ifBlank { "Empty Information" }
                    } else {
                        item.isSaved = !item.isSaved
                        item.savedDate = if (item.isSaved) Date() else null
                    }
                    informationAdapter.notifyItemChanged(position)
                }
            },
            onTextChange = { item, newText ->
                item.details = newText.ifBlank { "Empty Information" }
                item.isEditing = false
                informationAdapter.notifyDataSetChanged()
            }
        )

        binding.rvInformation.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = informationAdapter
        }
    }

    // --- FUNGSI UNTUK LOGIKA INLINE EDITING ---
    private fun setupInlineEditor() {
        // Setup untuk Departemen
        binding.tvDepartmentName.text = args.workspace.department.ifBlank { "Click here" }
        binding.tvDepartmentName.setOnClickListener {
            if (!isDeleteMode) toggleDepartmentEdit(true)
        }
        binding.etDepartmentName.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val newText = textView.text.toString()
                binding.tvDepartmentName.text = newText.ifBlank { "Click here" }
                args.workspace.department = newText
                sendResultBack()
                toggleDepartmentEdit(false)
                hideKeyboard(textView)
                return@setOnEditorActionListener true
            }
            false
        }
        binding.etDepartmentName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val newText = binding.etDepartmentName.text.toString()
                binding.tvDepartmentName.text = newText.ifBlank { "Click here" }
                args.workspace.department = newText
                sendResultBack()
                toggleDepartmentEdit(false)
            }
        }

        // Setup untuk Details
        binding.tvDetailsContent.text = args.workspace.details.ifBlank { "Click here" }
        binding.tvDetailsContent.setOnClickListener {
            if (!isDeleteMode) toggleDetailsEdit(true)
        }
        binding.btnSaveDetails.setOnClickListener {
            val newText = binding.etDetailsContent.text.toString()
            binding.tvDetailsContent.text = newText.ifBlank { "Click here" }
            args.workspace.details = newText
            sendResultBack()
            toggleDetailsEdit(false)
            hideKeyboard(it)
        }
    }

    private fun toggleDepartmentEdit(isEditing: Boolean) {
        binding.tvDepartmentName.isVisible = !isEditing
        binding.etDepartmentName.isVisible = isEditing
        if (isEditing) {
            val currentText = binding.tvDepartmentName.text.toString()
            binding.etDepartmentName.setText(if (currentText == "Click here") "" else currentText)
            binding.etDepartmentName.requestFocus()
            showKeyboard(binding.etDepartmentName)
        } else {
            hideKeyboard(binding.etDepartmentName)
        }
    }

    private fun toggleDetailsEdit(isEditing: Boolean) {
        binding.tvDetailsContent.isVisible = !isEditing
        binding.editDetailsGroup.isVisible = isEditing
        if (isEditing) {
            val currentText = binding.tvDetailsContent.text.toString()
            binding.etDetailsContent.setText(if (currentText == "Click here") "" else currentText)
            binding.etDetailsContent.requestFocus()
            showKeyboard(binding.etDetailsContent)
        } else {
            hideKeyboard(binding.etDetailsContent)
        }
    }

    // --- FUNGSI BANTU UNTUK KEYBOARD ---
    private fun hideKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}