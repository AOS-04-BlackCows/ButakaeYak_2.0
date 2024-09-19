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
import com.blackcows.butakaeyak.databinding.FragmentTermsBinding
import com.blackcows.butakaeyak.databinding.FragmentUserBinding
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
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

        binding.cvServices.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.alpha,R.anim.none)
                .replace(R.id.fragment_container_view,TermsFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
