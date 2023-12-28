package com.example.kelineyt.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kelineyt.R
import com.example.kelineyt.databinding.ActivityShoppingBinding
import com.example.kelineyt.util.Resource
import com.example.kelineyt.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }

    // 얘를 받아들이는 이유가 카트에 추가된 개수 그거 확인하려고 viewmodel을 추가한다.
    val viewModel by viewModels<CartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 아래 이것들로 인해 프래그먼트가 움직인다?
        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)


//        lifecycleScope.launchWhenCreated {
//            viewModel.cartProducts.collectLatest {
//                when(it) {
//                    is Resource.Success -> {
//                        val count = it.data?.size ?: 0
//                        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
//                        bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
//                            number = count
//                            backgroundColor = resources.getColor(R.color.g_blue)
//                        }
//                    }
//                    else -> Unit
//                }
//            }
//        }
        lifecycleScope.launchWhenCreated {
            viewModel.cartProducts.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        val count = it.data?.size
                        if (count == 0) {
                            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                            bottomNavigation.apply {
                                removeBadge(R.id.cartFragment)
                            }

                        } else {
                            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                            bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                                number = count!!
                                backgroundColor = resources.getColor(R.color.g_blue)
                            }
                        }

                    }
                    else -> Unit
                }
            }
        }

    }
}