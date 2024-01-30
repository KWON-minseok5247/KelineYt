package com.example.kelineyt.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kelineyt.R
import com.example.kelineyt.databinding.ActivityShoppingBinding
import com.example.kelineyt.fragments.categories.MainCategoryFragment
import com.example.kelineyt.fragments.shopping.CartFragment
import com.example.kelineyt.fragments.shopping.HomeFragment
import com.example.kelineyt.fragments.shopping.ProfileFragment
import com.example.kelineyt.fragments.shopping.SearchFragment
import com.example.kelineyt.util.Resource
import com.example.kelineyt.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    private val TAG_MAINCATEGORY_FRAGMENT = "main_category_fragment"
    private val TAG_SEARCH_FRAGMENT = "search_fragment"
    private val TAG_CART_FRAGMENT = "cart_fragment"
    private val TAG_PROFILE_FRAGMENT = "profile_fragment"

    private lateinit var fragmentManager: FragmentManager

    val binding by lazy {
        ActivityShoppingBinding.inflate(layoutInflater)
    }

    // 얘를 받아들이는 이유가 카트에 추가된 개수 그거 확인하려고 viewmodel을 추가한다.
    val viewModel by viewModels<CartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true) // 현재 화면이 이미 스택에 있으면 해당 화면을 재사용
            .build()

//        fragmentManager = supportFragmentManager
//        if (savedInstanceState == null) {
//            // 초기 화면 설정
//            showFragment(HomeFragment())
//        }
//
//        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.homeFragment -> showFragment(HomeFragment())
//                R.id.searchFragment -> showFragment(SearchFragment())
//                R.id.cartFragment -> showFragment(
//                    CartFragment()
//                )
//                R.id.profileFragment -> showFragment(ProfileFragment())
//            }
//
//            true
//        }

        // 아래 이것들로 인해 프래그먼트가 움직인다?
        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

//        if (savedInstanceState == null) {
//            // 초기 화면 설정
//            setFragment(TAG_MAINCATEGORY_FRAGMENT, HomeFragment())
//        }
//
//        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.homeFragment -> setFragment(TAG_MAINCATEGORY_FRAGMENT, HomeFragment())
//                R.id.searchFragment -> setFragment(TAG_SEARCH_FRAGMENT, SearchFragment())
//                R.id.cartFragment -> setFragment(
//                    TAG_CART_FRAGMENT,
//                    CartFragment()
//                )
//                R.id.profileFragment -> setFragment(TAG_PROFILE_FRAGMENT, ProfileFragment())
//            }
//
//            true
//        }

        lifecycleScope.launchWhenCreated {
            viewModel.cartProducts.collectLatest {
                when(it) {
                    is Resource.Success -> {
                        val count = it.data?.size ?: 0
                        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                        bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                            number = count
                            backgroundColor = resources.getColor(R.color.g_blue)
                        }
                    }
                    else -> Unit
                }
            }
        }
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

    private fun showFragment(fragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.shoppingHostFragment, fragment)
//        transaction.addToBackStack(null)
        transaction.commit()
    }



    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            ft.add(R.id.shoppingHostFragment, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_MAINCATEGORY_FRAGMENT)
        val records = manager.findFragmentByTag(TAG_SEARCH_FRAGMENT)
        val community = manager.findFragmentByTag(TAG_CART_FRAGMENT)
        val campaign = manager.findFragmentByTag(TAG_PROFILE_FRAGMENT)

        // Hide all Fragment
        if (home != null) {
            ft.hide(home)
        }
        if (records != null) {
            ft.hide(records)
        }
        if (community != null) {
            ft.hide(community)
        }
        if (campaign != null) {
            ft.hide(campaign)
        }

        // Show  current Fragment
        if (tag == TAG_MAINCATEGORY_FRAGMENT) {
            if (home != null) {
                ft.show(home)
            }
        }
        if (tag == TAG_SEARCH_FRAGMENT) {
            if (records != null) {
                ft.show(records)
            }
        }
        if (tag == TAG_CART_FRAGMENT) {
            if (community != null) {
                ft.show(community)
            }
        }
        if (tag == TAG_PROFILE_FRAGMENT) {
            if (campaign != null) {
                ft.show(campaign)
            }
        }

        ft.commitAllowingStateLoss()
    }
}