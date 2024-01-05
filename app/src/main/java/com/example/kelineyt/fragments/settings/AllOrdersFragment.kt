package com.example.kelineyt.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelineyt.R
import com.example.kelineyt.adapter.AllOrdersAdapter
import com.example.kelineyt.adapter.makeIt.AllOrdersAdapters
import com.example.kelineyt.databinding.FragmentOrdersBinding
import com.example.kelineyt.util.Resource
import com.example.kelineyt.viewmodel.AllOrdersViewModel
import com.example.kelineyt.viewmodel.makeIt.AllOrdersViewModels
import com.google.android.material.snackbar.Snackbar
import com.google.api.Context
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AllOrdersFragment : Fragment(){
    lateinit var binding: FragmentOrdersBinding
    private val adapter by lazy { AllOrdersAdapters()}
    private val viewModel by viewModels<AllOrdersViewModels>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allOrdersRv()
        lifecycleScope.launchWhenStarted {
            viewModel.order.collectLatest {
                when (it){
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        adapter.differ.submitList(it.data)
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        adapter.onClick = {
            val bundle = Bundle().apply {
                putParcelable("order",it)
            }
            findNavController().navigate(R.id.action_allOrdersFragment_to_orderDetailFragment, bundle)
        }



    }

    private fun allOrdersRv() {
        binding.rvAllOrders.adapter = adapter
        binding.rvAllOrders.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
    }


//    private lateinit var binding: FragmentOrdersBinding
//    val viewModel by viewModels<AllOrdersViewModel>()
//    val allOrdersAdapter by lazy { AllOrdersAdapter() }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentOrdersBinding.inflate(inflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setupOrdersRv()
//
//        lifecycleScope.launchWhenStarted {
//            viewModel.allOrder.collectLatest {
//                when (it) {
//                    is Resource.Loading -> {
//                        binding.progressbarAllOrders.visibility = View.VISIBLE
//                    }
//                    is Resource.Success -> {
//                        binding.progressbarAllOrders.visibility = View.GONE
//                        allOrdersAdapter.differ.submitList(it.data)
//                        if (it.data.isNullOrEmpty()) {
//                            binding.tvEmptyOrders.visibility = View.VISIBLE
//                        }
//                    }
//                    is Resource.Error -> {
//                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
//                        binding.progressbarAllOrders.visibility = View.GONE
//
//                    }
//                    else -> Unit
//                }
//            }
//        }
//
//        allOrdersAdapter.onClick = {
//            val action = AllOrdersFragmentDirections.actionAllOrdersFragmentToOrderDetailFragment(it)
//            findNavController().navigate(action)
//        }
//
//    }
//
//    private fun setupOrdersRv() {
//        binding.rvAllOrders.apply {
//            adapter = allOrdersAdapter
//            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
//
//        }
//    }
//

}