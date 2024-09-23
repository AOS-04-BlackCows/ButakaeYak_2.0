package com.blackcows.butakaeyak.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentUserBinding
import com.blackcows.butakaeyak.ui.SignIn.SignInFragment
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.fragment.TermsFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {


    //binding 설정
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isLoggedIn = checkLoginStatus()

        if (isLoggedIn) {
            //로그인 된 경우
            binding.loggedInLayout.visibility = View.VISIBLE
            binding.notLoggedInLayout.visibility = View.GONE
        } else {
            // 로그인 되지 않은 경우
            binding.loggedInLayout.visibility = View.GONE
            binding.notLoggedInLayout.visibility = View.VISIBLE

            //로그인 화면으로 이동
            binding.notLoggedInLayout.setOnClickListener {
                // 로그인 화면으로 이동하는 로직
                MainNavigation.addFragment(SignInFragment(), FragmentTag.SignInFragment)
            }
        }

        // 서비스 이용 약관
        binding.cvServices.setOnClickListener {
            MainNavigation.addFragment(TermsFragment(), FragmentTag.TermsFragment)
        }
    }
    // 로그인 상태를 확인하는 메서드
    private fun checkLoginStatus() : Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }
}
