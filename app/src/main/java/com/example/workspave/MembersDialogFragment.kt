package com.example.workspave

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workspave.databinding.DialogMembersBinding

class MembersDialogFragment : DialogFragment() {

    private var _binding: DialogMembersBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
            setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMembersBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listener untuk tombol close (X)
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        // Buat data member dummy
        val dummyMembers = listOf(
            Member("Nama Anda", isYou = true),
            Member("Asep Galon"),
            Member("Siomay Intel")
        )

        // Setup RecyclerView
        val memberAdapter = MemberAdapter(dummyMembers)
        binding.rvMembers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = memberAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}