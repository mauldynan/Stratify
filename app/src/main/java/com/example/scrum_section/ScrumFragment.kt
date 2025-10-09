package com.example.scrum_section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scrum_section.adapter.TaskAdapter
import com.example.scrum_section.data.TaskRepository
import com.example.scrum_section.model.Task
import com.example.scrum_section.util.TaskStatus
import com.example.stratify.R
import com.example.stratify.databinding.FragmentScrumBinding
import com.google.android.material.tabs.TabLayout

class ScrumFragment : Fragment() {

    private var _binding: FragmentScrumBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TaskAdapter
    private var currentStatus = TaskStatus.ALL
    private var fullTaskList = listOf<Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScrumBinding.inflate(inflater, container, false)
        val view = binding.root

        setupRecyclerView()
        setupFab()
        setupTabLayout()
        setupSearch()
        setupSortSpinner()

        return view
    }

    private fun setupRecyclerView() {
        fullTaskList = TaskRepository.getTasksByStatus(currentStatus)
        adapter = TaskAdapter(fullTaskList)
        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasks.adapter = adapter
    }

    private fun setupFab() {
        binding.btnAddTask.setOnClickListener {
            AddTaskDialog { refreshData() }.show(parentFragmentManager, "AddTaskDialog")
        }
    }

    private fun setupTabLayout() {
        val tabLayout = binding.tabLayout
        val tabData = listOf(
            "All" to R.color.gray,
            "To Do" to R.color.blue,
            "In Progress" to R.color.orange,
            "To Verify" to R.color.purple,
            "Done" to R.color.green
        )

        tabData.forEach { (title, colorRes) ->
            val tab = tabLayout.newTab().setText(title)
            tab.view.setBackgroundColor(requireContext().getColor(colorRes))
            tabLayout.addTab(tab)
        }

        tabLayout.setSelectedTabIndicatorColor(requireContext().getColor(R.color.redw))
        tabLayout.setSelectedTabIndicatorHeight(6)
        tabLayout.setSelectedTabIndicatorGravity(TabLayout.INDICATOR_GRAVITY_BOTTOM)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentStatus = when (tab?.position) {
                    1 -> TaskStatus.TODO
                    2 -> TaskStatus.IN_PROGRESS
                    3 -> TaskStatus.TO_VERIFY
                    4 -> TaskStatus.DONE
                    else -> TaskStatus.ALL
                }
                refreshData()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterAndSort(newText)
                return true
            }
        })
    }

    private fun setupSortSpinner() {
        val sortOptions = arrayOf("Terbaru", "Terlama", "A-Z", "Z-A")
        binding.spinnerSort.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            sortOptions
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
                filterAndSort(binding.searchView.query.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun refreshData() {
        fullTaskList = TaskRepository.getTasksByStatus(currentStatus)
        filterAndSort(binding.searchView.query.toString())
    }

    private fun filterAndSort(query: String?) {
        val searchText = query?.lowercase()?.trim() ?: ""
        val filtered = fullTaskList.filter { it.name.lowercase().contains(searchText) }

        val sortType = binding.spinnerSort.selectedItem?.toString() ?: "Terbaru"

        val sorted = when (sortType) {
            "A-Z" -> filtered.sortedBy { it.name.lowercase() }
            "Z-A" -> filtered.sortedByDescending { it.name.lowercase() }
            "Terlama" -> filtered.sortedBy { it.id }
            else -> filtered.sortedByDescending { it.id } // default Terbaru
        }

        adapter.updateData(sorted)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
