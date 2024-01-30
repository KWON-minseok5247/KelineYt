package com.example.kelineyt.fragments.shopping

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kelineyt.BuildConfig
import com.example.kelineyt.R
import com.example.kelineyt.activities.LoginRegisterActivity
import com.example.kelineyt.data.User
import com.example.kelineyt.databinding.FragmentProfileBinding
import com.example.kelineyt.util.Resource
import com.example.kelineyt.util.showBottomNavigationView
import com.example.kelineyt.viewmodel.ProfileViewModel
import com.example.kelineyt.viewmodel.makeIt.ProfileViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileFragment: Fragment() {

    lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModels>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.userImage.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                    is Resource.Success -> {
                        hideProgressBar()
                        fetchUserData(it.data!!)

                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
        binding.linearAllOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_allOrdersFragment)
        }

        binding.logout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireActivity(),LoginRegisterActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.linearBilling.setOnClickListener {
            val b = ProfileFragmentDirections.actionProfileFragmentToBillingFragment(
                0f, emptyArray(),false
            )
            findNavController().navigate(b)
//            findNavController().navigate(R.id.action_profileFragment_to_billingFragment)
        }







    }

    private fun fetchUserData(user: User) {
        binding.apply {
            Glide.with(requireView()).load(user.imagePath).into(binding.imageUser)
            tvUserName.text = "${user.firstName} ${user.lastName}"

        }
    }

    private fun showProgressBar() {
        binding.progressbarSettings.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressbarSettings.visibility = View.GONE
    }


//    private lateinit var binding: FragmentProfileBinding
//    val viewModel by viewModels<ProfileViewModel>()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentProfileBinding.inflate(inflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.constraintProfile.setOnClickListener {
//            findNavController().navigate(R.id.action_profileFragment_to_userAccountFragment)
//        }
//        binding.linearAllOrders.setOnClickListener {
//            findNavController().navigate(R.id.action_profileFragment_to_allOrdersFragment)
//
//        }
//
//        binding.linearBilling.setOnClickListener {
//            val action = ProfileFragmentDirections
//                .actionProfileFragmentToBillingFragment(0f, emptyArray(),false)
//            findNavController().navigate(action)
//        }
//
//        binding.logout.setOnClickListener {
//            viewModel.logout()
//            val intent = Intent(requireActivity(), LoginRegisterActivity::class.java)
//            startActivity(intent)
//            // finish로 하면 login화면에서 뒤로가기시 이 화면으로 안넘어감.
//            requireActivity().finish()
//        }
//
//        binding.tvVersion.text = "Version ${BuildConfig.VERSION_CODE}"
//
//        lifecycleScope.launchWhenStarted {
//            viewModel.user.collectLatest {
//                when (it) {
//                    is Resource.Loading -> {
//                        binding.progressbarSettings.visibility = View.VISIBLE
//                    }
//                    is Resource.Success -> {
//                        binding.progressbarSettings.visibility = View.GONE
//                        Glide.with(requireView()).load(it.data!!.imagePath).error(ColorDrawable(
//                            Color.BLACK)).into(binding.imageUser)
//                        binding.tvUserName.text = "${it.data.firstName} ${it.data.lastName}"
//                    }
//                    is Resource.Error -> {
//                        binding.progressbarSettings.visibility = View.GONE
//                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
//                    }
//                    else -> Unit
//                }
//            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        showBottomNavigationView()
//    }

//


}