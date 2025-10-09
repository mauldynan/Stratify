package com.example.scrum_section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scrum_section.adapter.TaskAdapter
import com.example.scrum_section.data.TaskRepository
import com.example.scrum_section.util.TaskStatus
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class ScrumFragment : Fragment() {

    private lateinit var adapter: TaskAdapter
    private var currentStatus = TaskStatus.ALL
    private var fullTaskList = listOf<com.example.scrum_section.model.Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scrum, container, false)

        val rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvTasks)
        rv.layoutManager = LinearLayoutManager(requireContext())
        fullTaskList = TaskRepository.getTasksByStatus(currentStatus)
        adapter = TaskAdapter(fullTaskList)
        rv.adapter = adapter

        val fab = view.findViewById<FloatingActionButton>(R.id.btnAddTask)
        fab.setOnClickListener {
            AddTaskDialog {
                refreshData()
            }.show(parentFragmentManager, "AddTaskDialog")
        }

        // --- Setup TabLayout ---
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
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

        // 🔴 Garis bawah merah di bawah tab (pakai cara aman untuk semua versi)
        tabLayout.setSelectedTabIndicatorColor(requireContext().getColor(R.color.redw))
        tabLayout.setSelectedTabIndicatorHeight(6)
        tabLayout.setSelectedTabIndicatorGravity(TabLayout.INDICATOR_GRAVITY_BOTTOM)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Garis bawah tetap merah
                tabLayout.setSelectedTabIndicatorColor(requireContext().getColor(R.color.redw))

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

        // --- SearchView setup ---
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterAndSort(newText)
                return true
            }
        })

        // --- Spinner Sort setup ---
        val spinnerSort = view.findViewById<Spinner>(R.id.spinnerSort)
        val sortOptions = arrayOf("Terbaru", "Terlama", "A-Z", "Z-A")
        spinnerSort.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
                filterAndSort(searchView.query.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return view
    }

    private fun refreshData() {
        fullTaskList = TaskRepository.getTasksByStatus(currentStatus)
        filterAndSort("")
    }

    private fun filterAndSort(query: String?) {
        val searchText = query?.lowercase()?.trim() ?: ""
        val filtered = fullTaskList.filter { it.name.lowercase().contains(searchText) }

        val spinnerSort = view?.findViewById<Spinner>(R.id.spinnerSort)
        val sortType = spinnerSort?.selectedItem?.toString() ?: "Terbaru"

        val sorted = when (sortType) {
            "A-Z" -> filtered.sortedBy { it.name.lowercase() }
            "Z-A" -> filtered.sortedByDescending { it.name.lowercase() }
            "Terlama" -> filtered.sortedBy { it.id }
            else -> filtered.sortedByDescending { it.id } // default Terbaru
        }

        adapter.updateData(sorted)
    }
}
