package com.example.yactong.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yactong.databinding.FragmentHomeBinding
import com.example.yactong.firebase.auth.FirebaseAuthManager
import com.google.firebase.FirebaseApp

class HomeFragment(timer: TextView) : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseApp.initializeApp(requireContext())
        val firebaseAuthManager = FirebaseAuthManager(object:
            FirebaseAuthManager.SmsVerificationListener(binding.timer) {
            override fun onSuccess() {
                Log.d("FirebaseAuth", "Success!")
            }
            override fun onFail(e: Exception) {
                Log.d("FirebaseAuth", "Fail!) ${e.message}")
            }
        })
        firebaseAuthManager.verifyPhoneNumber(requireActivity(), "+82 17-1234-5678")
        binding.submit.setOnClickListener {
            firebaseAuthManager.submitSmsCode("123456")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}