package com.example.candycrush.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.candycrush.R
import com.example.candycrush.databinding.FragmentDashboardBinding
import com.example.candycrush.ui.activities.DashboardActivity
import com.example.candycrush.ui.activities.MainActivity

class DashboardFragment : Fragment() {
    private lateinit var btnPractice:Button

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        btnPractice = binding.btnPractice
        btnPractice.setOnClickListener {
            Toast.makeText(activity, " clicked" ,Toast.LENGTH_SHORT).show()

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }


}