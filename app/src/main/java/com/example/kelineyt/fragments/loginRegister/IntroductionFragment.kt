package com.example.kelineyt.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.kelineyt.R
import com.example.kelineyt.activities.ShoppingActivity
import com.example.kelineyt.databinding.FragmentIntroductionBinding
import com.example.kelineyt.viewmodel.IntroductionViewModel
import com.example.kelineyt.viewmodel.IntroductionViewModel.Companion.ACCOUNT_OPTIONS_FRAGMENT
import com.example.kelineyt.viewmodel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IntroductionFragment : Fragment(R.layout.fragment_introduction) {
    private lateinit var binding: FragmentIntroductionBinding
    private val viewModel by viewModels<IntroductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // collect는 새로운 값이 생길 때마다 호출되는 함수를 매개변수로 받습니다 이건 suspend 함수이므로 코루틴 내에서 실행해야 합니다
        //Flow에 대해서 collect가 호출될 때마다 새로운 Flow가 생성된다

        //https://pluu.github.io/blog/android/androiddevsummit/2021/11/07/ads21-Kotlin-Flows-in-practice/
        lifecycleScope.launchWhenStarted {
            viewModel.navigate.collect {
                when (it) {
                    SHOPPING_ACTIVITY -> {
                        //FLAG_ACTIVITY_NEW_TASK -> 새로운 태스크를 생성하여 그 태스크안에 엑티비티를 추가하게 됩니다.

                        // FLAG_ACTIVITY_CLEAR_TASK
                        //이 Activity와 관련된 Task가 수행중이면 Activity가 수행되기 전에 다른 액티비티는 모두 종료된다.
                        //이 플래그는 FLAG_ACTIVITY_NEW_TASK와 같이 사용되어야 한다.

                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)

                        }
                    }

                    ACCOUNT_OPTIONS_FRAGMENT -> {
                        findNavController().navigate(it)
                    }
                    else -> Unit
                }
            }
        }


        binding.buttonStart.setOnClickListener {
            viewModel.startButtonClick()
            findNavController().navigate(R.id.action_introductionFragment_to_accountOptionsFragment)
        }
    }


}