package com.example.kelineyt.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.kelineyt.R
import com.example.kelineyt.activities.ShoppingActivity
import com.example.kelineyt.databinding.FragmentLoginBinding
import com.example.kelineyt.dialog.setupBottomSheetDialog
import com.example.kelineyt.util.Resource
import com.example.kelineyt.util.validateEmail
import com.example.kelineyt.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDontHaveAccount.setOnClickListener {
            // graph를 통한 프래그먼트라면 finNav를 사용하는 듯?
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment2)
        }

        binding.apply {
            buttonLoginLogin.setOnClickListener {
                val email = edEmailLogin.text.toString().trim()
                val password = edPasswordLogin.text.toString()
                viewModel.login(email, password)
            }
        }
        binding.tvForgotPasswordLogin.setOnClickListener {

            setupBottomSheetDialog { email ->
                if (!email.isNullOrEmpty()) {
                    viewModel.resetPasswordFun(email)
                } else {
                    Snackbar.make(requireView(), "email is empty", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collectLatest {
                when (it) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        Snackbar.make(
                            requireView(),
                            "We send a message to your email.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    else -> Unit
                }
            }
        }
// 12-05 임시 삭제
//        binding.tvForgotPasswordLogin.setOnClickListener {
//            setupBottomSheetDialog {email ->
//                // email이 입력하는 부분임
//                viewModel.resetPassword(email)
//
//            }
//        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonLoginLogin.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.buttonLoginLogin.revertAnimation()
                        val intent =
                            Intent(requireContext(), ShoppingActivity::class.java).addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK or
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                            )
                        startActivity(intent)
                    }
                    is Resource.Error -> {
                        binding.buttonLoginLogin.revertAnimation()
                        Toast.makeText(
                            requireContext(),
                            "Check your Id or Password.",
                            Toast.LENGTH_SHORT
                        ).show()


                    }
                    else -> {

                    }
                }
            }
        }
// 12-05 공부를 위해 주석처리
//        lifecycleScope.launchWhenStarted {
//            viewModel.resetPassword.collect{
//                when (it) {
//                    is Resource.Loading -> {
//                    }
//                    is Resource.Success -> {
//                        Snackbar.make(requireView(), "Reset link was sent to your email.",Snackbar.LENGTH_LONG).show()
//                    }
//                    is Resource.Error -> {
//                        Snackbar.make(requireView(), "Error: ${it.message}",Snackbar.LENGTH_LONG).show()
//
//                    }
//                    else -> Unit
//                }
//            }
//        }
//
//        lifecycleScope.launchWhenStarted {
//            viewModel.login.collect {
//                when (it) {
//                    is Resource.Loading -> {
//                        binding.buttonLoginLogin.startAnimation()
//                    }
//                    is Resource.Success -> {
////                        val intent = Intent(requireActivity(),ShoppingActivity::class.java)
////                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
////                        startActivity(intent)
//                        Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                            startActivity(intent)
//                        } // 이게 로그인하고 뒤로가기 누를때 로그인 화면으로 돌아가지 않는 것을 뜻하는 듯? 바로 액티비티 종료로 이어짐
//                    }
//                    is Resource.Error -> {
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
//                        binding.buttonLoginLogin.revertAnimation()
//
//                    }
//                    else -> Unit
//                }
//            }
//        }
    }
}