package com.blackcows.butakaeyak.ui.SignIn

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentSignInBinding
import com.blackcows.butakaeyak.domain.repo.LocalRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val TAG = "SignIn"

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var localRepository: LocalRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KakaoSdk.init(requireContext(), BuildConfig.NATIVE_APP_KEY)

        binding.ivBack.setOnClickListener {
            MainNavigation.popCurrentFragment()
        }

        binding.ivKakaoLogin.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val result = suspendCoroutine<Pair<OAuthToken?, Throwable?>> { continuation ->
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                        // 카카오톡 로그인
                        UserApiClient.instance.loginWithKakaoTalk(requireActivity()) { token, e ->
                            // 사용자 취소
                            if (e != null) {
                                if (e is ClientError && e.reason == ClientErrorCause.Cancelled) {
                                    return@loginWithKakaoTalk
                                }
                                // 로그인 실패 -> 이메일 로그인
                                UserApiClient.instance.loginWithKakaoAccount(requireActivity()) { token, e ->
                                    continuation.resume(Pair(token, e))
                                }
                            } else if (token != null) {
                                continuation.resume(Pair(token, null))
                            }
                        }
                    } else {
                        // 카카오 이메일 로그인
                        UserApiClient.instance.loginWithKakaoAccount(requireActivity()) { token, e ->
                            continuation.resume(Pair(token, e))
                        }
                    }
                }


            }
        }
    }
}
