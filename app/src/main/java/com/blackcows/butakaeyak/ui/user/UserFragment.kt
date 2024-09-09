package com.blackcows.butakaeyak.ui.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.data.source.LocalDataSource
import com.blackcows.butakaeyak.databinding.FragmentUserBinding
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.blackcows.butakaeyak.ui.SignIn.SignInActivity
import com.blackcows.butakaeyak.ui.home.data.DataSource.Companion.saveData
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import com.blackcows.butakaeyak.ui.take.fragment.NameFragment
import com.blackcows.butakaeyak.ui.take.fragment.TermsFragment
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify

@AndroidEntryPoint
class UserFragment : Fragment() {


    //binding 설정
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    //ViewPager 설정
    private val userFavoriteViewPagerAdapter by lazy {
        UserFavoriteViewPagerAdapter { mainViewModel.cancelFavoritePharmacy(it.id) }
    }

    private val MIN_SCALE = 0.85f // 뷰가 몇퍼센트로 줄어들 것인지
    private val MIN_ALPHA = 0.5f // 어두워지는 정도를 나타낸 듯 하다.

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
            vp2Favorite.apply {
                adapter = userFavoriteViewPagerAdapter
                setPageTransformer(ZoomOutPageTransformer())
            }

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

                    if(userData.thumbnail.isNullOrBlank()){
                        Glide.with(ivMyProfile).load(R.drawable.icon_user).into(ivMyProfile)
                    }else{
                        Glide.with(ivMyProfile).load(userData.thumbnail).into(ivMyProfile)
                    }
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

    //ViewPager 스와이프 시 화면 애니메이션
    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }

                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }

                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
}