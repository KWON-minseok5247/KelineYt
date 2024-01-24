package com.example.kelineyt.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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

    private val TAG_HOME_FRAGMENT = "home_fragment"
    private val TAG_RECORDS_FRAGMENT = "records_fragment"
    private val TAG_COMMUNITY_FRAGMENT = "community_fragment"
    private val TAG_CAMPAIGN_FRAGMENT = "campaign_fragment"

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

//
//        setFragment(TAG_HOME_FRAGMENT, HomeFragment())
//
//        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.navigation_home -> setFragment(TAG_HOME_FRAGMENT, HomeFragment())
//                R.id.navigation_records -> setFragment(TAG_RECORDS_FRAGMENT, RecordsFragment())
//                R.id.navigation_community -> setFragment(
//                    TAG_COMMUNITY_FRAGMENT,
//                    CommunityFragment()
//                )
//                R.id.navigation_campaign -> setFragment(TAG_CAMPAIGN_FRAGMENT, CampaignFragment())
//            }
//
//            true
//        }

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
//
//    private fun setFragment(tag: String, fragment: Fragment) {
//        val manager: FragmentManager = supportFragmentManager
//        val ft: FragmentTransaction = manager.beginTransaction()
//
//        if (manager.findFragmentByTag(tag) == null) {
//            ft.add(R.id.nav_fragment, fragment, tag)
//        }
//
//        val home = manager.findFragmentByTag(TAG_HOME_FRAGMENT)
//        val records = manager.findFragmentByTag(TAG_RECORDS_FRAGMENT)
//        val community = manager.findFragmentByTag(TAG_COMMUNITY_FRAGMENT)
//        val campaign = manager.findFragmentByTag(TAG_CAMPAIGN_FRAGMENT)
//
//        // Hide all Fragment
//        if (home != null) {
//            ft.hide(home)
//        }
//        if (records != null) {
//            ft.hide(records)
//        }
//        if (community != null) {
//            ft.hide(community)
//        }
//        if (campaign != null) {
//            ft.hide(campaign)
//        }
//
//        // Show  current Fragment
//        if (tag == TAG_HOME_FRAGMENT) {
//            if (home != null) {
//                ft.show(home)
//            }
//        }
//        if (tag == TAG_RECORDS_FRAGMENT) {
//            if (records != null) {
//                ft.show(records)
//            }
//        }
//        if (tag == TAG_COMMUNITY_FRAGMENT) {
//            if (community != null) {
//                ft.show(community)
//            }
//        }
//        if (tag == TAG_CAMPAIGN_FRAGMENT) {
//            if (campaign != null) {
//                ft.show(campaign)
//            }
//        }
//
//        ft.commitAllowingStateLoss()
//    }
}