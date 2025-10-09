package com.example.workspave

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.workspave.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    // Menerima argumen dari Navigasi
    private val args: StartFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tampilkan tombol back jika argumennya true
        binding.btnBackToList.isVisible = args.showBackButton
        binding.btnBackToList.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnCreate.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_createWorkspaceFragment)
        }

        binding.btnJoin.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_joinWorkspaceFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}