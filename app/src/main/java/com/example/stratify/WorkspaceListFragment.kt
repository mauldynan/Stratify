package com.example.stratify

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stratify.Workspace
import com.example.stratify.WorkspaceAdapter
import com.example.stratify.databinding.FragmentWorkspaceListBinding
import com.example.stratify.WorkspaceListFragmentDirections

class WorkspaceListFragment : Fragment() {

    private var _binding: FragmentWorkspaceListBinding? = null
    private val binding get() = _binding!!

    private val workspaceList = mutableListOf<Workspace>()
    private lateinit var workspaceAdapter: WorkspaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkspaceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Adapter
        workspaceAdapter = WorkspaceAdapter(workspaceList) { workspace ->
            val action = WorkspaceListFragmentDirections.actionWorkspaceListFragmentToWorkspaceDetailFragment(workspace)
            findNavController().navigate(action)
        }

        // Setup RecyclerView
        binding.rvWorkspaces.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = workspaceAdapter
        }

        // Listener untuk menerima data BARU dari Create/Join Fragment
        arguments?.let { bundle ->
            val newWorkspace = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("new_workspace", Workspace::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable("new_workspace")
            }

            newWorkspace?.let {
                // Hindari duplikat jika user menekan tombol back dan kembali lagi
                if (workspaceList.none { workspace -> workspace.id == it.id }) {
                    workspaceList.add(it)
                    workspaceAdapter.notifyItemInserted(workspaceList.size - 1)
                }
            }
            arguments = null
        }

        // Listener untuk menerima data UPDATE dari Detail Fragment
        parentFragmentManager.setFragmentResultListener("workspace_update_request", viewLifecycleOwner) { _, bundle ->
            val updatedWorkspace = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("updated_workspace", Workspace::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable("updated_workspace")
            }

            updatedWorkspace?.let { ws ->
                // Cari item di daftar dan perbarui
                val index = workspaceList.indexOfFirst { it.id == ws.id }
                if (index != -1) {
                    workspaceList[index] = ws
                    workspaceAdapter.notifyItemChanged(index)
                }
            }
        }

        // Atur navigasi untuk tombol +
        binding.btnAddWorkspace.setOnClickListener {
            // Menggunakan Safe Args untuk mengirim argumen
            val action = WorkspaceListFragmentDirections.actionWorkspaceListFragmentToStartFragment(showBackButton = true)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}