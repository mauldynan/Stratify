package com.example.stratify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController // Import yang benar
import com.example.stratify.databinding.FragmentDashboardBinding

class FragmentDashboard : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // ✅ Tombol "View Full Analysis"
        binding.btnViewFullAnalysis.setOnClickListener {
            // Menggunakan Navigation Component untuk berpindah ke FullAnalysisFragment
            // Pastikan ID aksi (action ID) sesuai dengan yang ada di nav_graph.xml
            findNavController().navigate(R.id.action_dashboardFragment_to_fullAnalysisFragment)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Re-trigger animasi donut chart setiap kali fragment dibuka kembali
        binding.donutChart?.let { chart ->
            if (chart is DonutChartView) {
                chart.post {
                    chart.invalidate() // redraw chart
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}