package com.example.kelineyt.fragments.settings

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.kelineyt.data.User
import com.example.kelineyt.databinding.FragmentUserAccountBinding
import com.example.kelineyt.dialog.setupBottomSheetDialog
import com.example.kelineyt.util.Resource
import com.example.kelineyt.viewmodel.UserAccountViewModel
import com.example.kelineyt.viewmodel.makeIt.UserAccountViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class UserAccountFragment : Fragment() {

    lateinit var binding: FragmentUserAccountBinding
    private val viewModel by viewModels<UserAccountViewModels>()
    private val GALLERY = 1
    private var imageData: Uri? = null
    lateinit var oldUser: User

    private lateinit var callback: OnBackPressedCallback


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAccountBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showUserLoading()
                    }
                    is Resource.Success -> {
                        hideUserLoading()
                        enterData(it.data!!)
                        oldUser = it.data
                        Log.e("old user", oldUser.toString())
                    }
                    is Resource.Error -> {
                        hideUserLoading()
                        binding.progressbarAccount.visibility = View.GONE

                    }
                    else -> Unit

                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.updateUser.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonSave.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.buttonSave.revertAnimation()
                        Toast.makeText(requireContext(), "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error -> {
                        binding.buttonSave.revertAnimation()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> Unit

                }
            }
        }

        binding.buttonSave.setOnClickListener {
            val edFirstName = binding.edFirstName.text.toString()
            val edLastName = binding.edLastName.text.toString()
            val edEmail = binding.edEmail.text.toString()
            if (imageData == null) {
                val user = User(edFirstName, edLastName, edEmail, oldUser.imagePath)
//                user.copy(imagePath = )
                viewModel.saveUser(user)
            } else {
                val user = User(edFirstName, edLastName, edEmail)
                viewModel.saveUserWithImage(user, imageData!!)

            }

        }
        binding.imageCloseUserAccount.setOnClickListener {
            backPress()

        }


        // 갤러리 여는 방법
//https://believecom.tistory.com/722

        binding.imageUser.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, GALLERY)
        }

        binding.imageEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, GALLERY)
        }

        binding.tvUpdatePassword.setOnClickListener {
            this.setupBottomSheetDialog {

            }
        }




    }

    private fun backPress() {
        val edFirstName = binding.edFirstName.text.toString()
        val edLastName = binding.edLastName.text.toString()
        if (oldUser.firstName == edFirstName &&
            oldUser.lastName == edLastName &&
            imageData == null
        ) {
            findNavController().navigateUp()
        } else {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("저장이 되지 않았습니다.")
                .setMessage("그래도 나가시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        findNavController().navigateUp()
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            builder.show()
        }
    }


    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                imageData = data?.data
                try {
                    binding.imageUser.setImageURI(imageData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun hideUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.GONE
            imageUser.visibility = View.VISIBLE
            imageEdit.visibility = View.VISIBLE
            edFirstName.visibility = View.VISIBLE
            edLastName.visibility = View.VISIBLE
            edEmail.visibility = View.VISIBLE
            tvUpdatePassword.visibility = View.VISIBLE
            buttonSave.visibility = View.VISIBLE
        }
    }

    private fun showUserLoading() {
        binding.apply {
            progressbarAccount.visibility = View.VISIBLE
            imageUser.visibility = View.INVISIBLE
            imageEdit.visibility = View.INVISIBLE
            edFirstName.visibility = View.INVISIBLE
            edLastName.visibility = View.INVISIBLE
            edEmail.visibility = View.INVISIBLE
            tvUpdatePassword.visibility = View.INVISIBLE
            buttonSave.visibility = View.INVISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 클릭시 동작하는 로직
                backPress()
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(0, R.anim.horizon_exit_front)
//                    .remove(this@CartFragment)
//                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }




    private fun enterData(user: User) {
        Glide.with(this).load(user.imagePath).into(binding.imageUser)
        binding.apply {
            edFirstName.setText(user.firstName)
            edLastName.setText(user.lastName)
            edEmail.setText(user.email)
        }
    }

//
//    private lateinit var binding: FragmentUserAccountBinding
//    private val viewModel by viewModels<UserAccountViewModel>()
//    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>
//
//
//    private var imageUri: Uri? =null
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        imageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            imageUri = it.data?.data
//            Glide.with(this).load(imageUri).into(binding.imageUser)
//        }
//    }
//
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentUserAccountBinding.inflate(inflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        lifecycleScope.launchWhenStarted {
//            viewModel.user.collectLatest {
//                when (it) {
//                    is Resource.Loading -> {
//                        showUserLoading()
//                    }
//                    is Resource.Success -> {
//                        hideUserLoading()
//                        showUserInformation(it.data!!)
//                        }
//                    is Resource.Error -> {
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//                    }
//                    else -> Unit
//                }
//            }
//        }
//
//        lifecycleScope.launchWhenStarted {
//            viewModel.updateInfo.collectLatest {
//                when (it) {
//                    is Resource.Loading -> {
//                        binding.buttonSave.startAnimation()
//                    }
//                    is Resource.Success -> {
//                        binding.buttonSave.revertAnimation()
//                        findNavController().navigateUp()
//                    }
//                    is Resource.Error -> {
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//                    }
//                    else -> Unit
//                }
//            }
//        }
//
//        binding.tvUpdatePassword.setOnClickListener {
//            setupBottomSheetDialog {
//
//            }
//        }
//
//
//        binding.buttonSave.setOnClickListener {
//            binding.apply {
//                val firstName = edFirstName.text.toString().trim()
//                val lastName = edLastName.text.toString().trim()
//                val email = edEmail.text.toString().trim()
//                val user = User(firstName,lastName,email)
//                viewModel.updateUser(user, imageUri)
//            }
//        }
//
//        binding.imageEdit.setOnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "image/*"
//            imageActivityResultLauncher.launch(intent)
//
//        }
//
//
//
//    }
//
//    private fun showUserInformation(data: User) {
//
//        binding.apply {
//            Glide.with(this@UserAccountFragment).load(data.imagePath).error(ColorDrawable(Color.BLACK)).into(imageUser)
//            edFirstName.setText(data.firstName)
//            edLastName.setText(data.lastName)
//            edEmail.setText(data.email)
//        }
//
//    }
//
//    private fun hideUserLoading() {
//        binding.apply {
//            progressbarAccount.visibility = View.GONE
//            imageUser.visibility = View.VISIBLE
//            imageEdit.visibility = View.VISIBLE
//            edFirstName.visibility = View.VISIBLE
//            edLastName.visibility = View.VISIBLE
//            edEmail.visibility = View.VISIBLE
//            tvUpdatePassword.visibility = View.VISIBLE
//            buttonSave.visibility = View.VISIBLE
//        }    }
//
//    private fun showUserLoading() {
//        binding.apply {
//            progressbarAccount.visibility = View.VISIBLE
//            imageUser.visibility = View.INVISIBLE
//            imageEdit.visibility = View.INVISIBLE
//            edFirstName.visibility = View.INVISIBLE
//            edLastName.visibility = View.INVISIBLE
//            edEmail.visibility = View.INVISIBLE
//            tvUpdatePassword.visibility = View.INVISIBLE
//            buttonSave.visibility = View.INVISIBLE
//        }
//    }
}