package com.example.stratify

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainWorkspace : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_workspace)

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("workspace_list_json", null)
        val type = object : TypeToken<List<Workspace>>() {}.type
        val workspaceList: List<Workspace>? = gson.fromJson(json, type)

        val hasWorkspace = !workspaceList.isNullOrEmpty()

        // Ambil NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        // Atur halaman awal berdasarkan kondisi apakah list workspace ada isinya
        if (hasWorkspace) {
            navGraph.setStartDestination(R.id.workspaceListFragment)
        } else {
            navGraph.setStartDestination(R.id.startFragment)
        }

        navController.graph = navGraph
    }
}