package com.blackcows.butakaeyak.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentUserBinding
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.blackcows.butakaeyak.ui.SignIn.SignInActivity
import com.blackcows.butakaeyak.ui.take.fragment.TermsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserFragment : Fragment() {


    //binding 설정
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    //ViewPager 설정
    private val userFavoriteViewPagerAdapter by lazy {
        UserFavoriteViewPagerAdapter { mainViewModel.cancelFavoritePharmacy(it.id) }
    }

    private val userViewModel: UserViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

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

        mainViewModel.getPharmacyList()
        mainViewModel.pharmacies.observe(viewLifecycleOwner) {
            Log.d("UserFragment", "pharmacies size: ${it.size}")
            userFavoriteViewPagerAdapter.submitList(it.toList())
        }

        binding.apply {
            binding.vp2Favorite.adapter = userFavoriteViewPagerAdapter

            userViewModel.currentUser.observe(viewLifecycleOwner) {
                Log.d("UserFragment", "userName: ${it?.name ?: "없음"}")
                if(it != null) {
                    tvMyName.text = "환영합니다. ${it.name!!}님"
                    //로그아웃 버튼
                    clMyLogin.visibility = View.INVISIBLE
                    clMyLogout.visibility = View.VISIBLE
                } else {
                    tvMyName.text = "환영합니다!"
                    //로그인 버튼
                    clMyLogout.visibility = View.INVISIBLE
                    clMyLogin.visibility = View.VISIBLE
                }
            }

            clMyTerms.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.alpha,R.anim.none)
                    .replace(R.id.fragment_container_view,TermsFragment())
                    .addToBackStack(null)
                    .commit()
            }

            // 데이터 받기
            //TODO 회원가입 데이터 받기 (이름, 프로필), 프로필 없을 때 기본 사진 설정
            val signInResultCallback = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    val userData = result.data?.getParcelableExtra<UserData>("userData") ?: return@registerForActivityResult
                    userViewModel.setUser(userData)

//                    if(userData.thumbnail.isNullOrBlank()){
//                        Glide.with(ivMyProfile).load(R.drawable.icon_user).into(ivMyProfile)
//                    }else{
//                        Glide.with(ivMyProfile).load(userData.thumbnail).into(ivMyProfile)
//                    }
                    tvMyName.text = "환영합니다. ${userData.name}님"
                }
            }

            clMyLogin.setOnClickListener {
                    val intent = Intent(requireActivity(), SignInActivity::class.java)
                    signInResultCallback.launch(intent)
            }
            clMyLogout.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    userViewModel.logout()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        userViewModel.loadUser()
    }
}