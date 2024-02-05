package com.example.kelineyt.adapter.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.kelineyt.R
import com.example.kelineyt.databinding.FragmentMyDialogBinding

class MyDialogFragment : DialogFragment() {

    lateinit var binding: FragmentMyDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 여기에 다이얼로그 내용을 설정합니다.

        // 확인 버튼 클릭 시 다이얼로그를 닫습니다.
        binding.btnConfirm.setOnClickListener {
            dismiss()
        }
    }
}
