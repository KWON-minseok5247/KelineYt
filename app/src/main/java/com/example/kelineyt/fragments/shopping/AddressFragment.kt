package com.example.kelineyt.fragments.shopping

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kelineyt.R
import com.example.kelineyt.adapter.makeIt.AddressAdapters
import com.example.kelineyt.data.Address
import com.example.kelineyt.databinding.AddressRvItemBinding
import com.example.kelineyt.databinding.FragmentAddressBinding
import com.example.kelineyt.util.Resource
import com.example.kelineyt.viewmodel.AddressViewModel
import com.example.kelineyt.viewmodel.makeIt.AddressViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddressFragment: Fragment() {
    lateinit var binding: FragmentAddressBinding
    private val viewModel by viewModels<AddressViewModels>()
    private val navArgs by navArgs<AddressFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageAddressClose.setOnClickListener {
            findNavController().navigateUp()
        }
        val getAddress = navArgs?.address

        if (getAddress == null && navArgs.index == -1) { // Address를 새로 만들었을 때

            binding.buttonSave.setOnClickListener {
                val addressTitle = binding.edAddressTitle.text.toString()
                val fullName = binding.edFullName.text.toString()
                val street = binding.edStreet.text.toString()
                val phone = binding.edPhone.text.toString()
                val city = binding.edCity.text.toString()
                val state = binding.edState.text.toString()

                var emptyBoolean = (addressTitle.isNotEmpty() && fullName.isNotEmpty() && state.isNotEmpty() &&
                        street.isNotEmpty() && phone.isNotEmpty() && city.isNotEmpty())
                if (emptyBoolean) {
                    val address = Address(addressTitle, fullName, street, phone, city, state)
                    viewModel.saveAddress(address)
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(requireContext(),"Check the empty part.",Toast.LENGTH_SHORT).show()
                }

            }

        } else { // 이미 존재하는 Address를 불러왔을 때

            val oldAddress = navArgs?.address!!

            binding.apply {
                edAddressTitle.setText(oldAddress.addressTitle)
                edFullName.setText(oldAddress.fullName)
                edStreet.setText(oldAddress.street)
                edPhone.setText(oldAddress.phone)
                edCity.setText(oldAddress.city)
                edState.setText(oldAddress.state)
            }
            val index = navArgs!!.index
            binding.buttonDelelte.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("주소가 삭제됩니다.")
                    .setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, id ->
                            viewModel.deleteAddress(index)
                            findNavController().navigateUp()
                        })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener { dialog, id ->
                        })
                builder.show()
            }


            binding.buttonSave.setOnClickListener {
                val addressTitle = binding.edAddressTitle.text.toString()
                val fullName = binding.edFullName.text.toString()
                val street = binding.edStreet.text.toString()
                val phone = binding.edPhone.text.toString()
                val city = binding.edCity.text.toString()
                val state = binding.edState.text.toString()

                var emptyBoolean = (addressTitle.isNotEmpty() && fullName.isNotEmpty() && state.isNotEmpty() &&
                        street.isNotEmpty() && phone.isNotEmpty() && city.isNotEmpty())

                if (emptyBoolean) {
                    val newAddress = Address(addressTitle, fullName, street, phone, city, state)
                    viewModel.updateAddress(index, newAddress)
                    findNavController().navigateUp()

                } else {


                    Toast.makeText(requireContext(),"Check the empty part.",Toast.LENGTH_SHORT).show()
                }

            }
        }




    }



















//    private lateinit var binding: FragmentAddressBinding
//    val viewModel by viewModels<AddressViewModel>()
//    val args by navArgs<AddressFragmentArgs>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        lifecycleScope.launchWhenStarted {
//            viewModel.addNewAddress.collectLatest {
//                when (it) {
//                    is Resource.Loading -> {
//                        binding.progressbarAddress.visibility = View.VISIBLE
//                    }
//                    is Resource.Success -> {
//                        binding.progressbarAddress.visibility = View.INVISIBLE
//                        findNavController().navigateUp()
//                    }
//
//                    is Resource.Error -> {
//                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
//                            .show()
//                    }
//
//                    else -> Unit
//                }
//            }
//        }
//
//        lifecycleScope.launchWhenStarted {
//            viewModel.error.collectLatest {
//                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentAddressBinding.inflate(inflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val address = args.address
//        if (address == null) {
//            binding.buttonDelelte.visibility = View.GONE
//
//            binding.apply {
//                buttonSave.setOnClickListener {
//
//                    val addressTitle = edAddressTitle.text.toString()
//                    val fullName = edFullName.text.toString()
//                    val state = edState.text.toString()
//                    val city = edCity.text.toString()
//                    val phone = edPhone.text.toString()
//                    val street = edStreet.text.toString()
//                    val address = Address(addressTitle, fullName, street, phone, city, state)
//
//                    viewModel.addAddress(address)
//
//                }
//            }
//        } else {
//            binding.apply {
//                edAddressTitle.setText(address.addressTitle)
//                edFullName.setText(address.fullName)
//                edStreet.setText(address.street)
//                edPhone.setText(address.phone)
//                edCity.setText(address.city)
//                edState.setText(address.addressTitle)
//
//                buttonSave.setOnClickListener {
//
//                    val addressTitle = edAddressTitle.text.toString()
//                    val fullName = edFullName.text.toString()
//                    val state = edState.text.toString()
//                    val city = edCity.text.toString()
//                    val phone = edPhone.text.toString()
//                    val street = edStreet.text.toString()
//                    val address = Address(addressTitle, fullName, street, phone, city, state)
//
//                    viewModel.updateAddress(address)
//                }
//            }
//
//
//
//        }
//
//
//    }
}