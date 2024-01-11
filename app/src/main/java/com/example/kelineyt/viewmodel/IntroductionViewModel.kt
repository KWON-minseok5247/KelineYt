package com.example.kelineyt.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.R
import com.example.kelineyt.util.Constants.INTRODUCTION_KEY
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    //StateFlow는 수집하는 대상이 없어도 데이터를 보유한다.
    //여러 번 수집할 수 있으므로 Activity/Fragment와 함께 사용하는 것도 안전하다
    //MutableStateFlow를 사용해서 원할 때마다 값을 업데이트할 수 있다
    //모든 Flow는 StateFlow로 변환할 수 있다
    //StateFlow는 업스트림 Flow에서 업데이트를 수신하고 최신 값을 저장한다
    //다양한 Flow가 존재하지만 StateFlow는 정확하게 최적화할 수 있으므로 권장한다.

    //자동 로그인 구현 과정
//https://velog.io/@saint6839/%ED%8C%8C%EC%9D%B4%EC%96%B4%EB%B2%A0%EC%9D%B4%EC%8A%A4-%EA%B5%AC%EA%B8%80-%EC%9E%90%EB%8F%99%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B8%B0%EB%8A%A5

    private val _navigate = MutableStateFlow(0)
    val navigate: StateFlow<Int> = _navigate

    companion object {
        const val SHOPPING_ACTIVITY = 23
        const val ACCOUNT_OPTIONS_FRAGMENT =
            R.id.action_introductionFragment_to_accountOptionsFragment
    }
// emit이 flow에 결과를 추가하는 것이다.

//    init {
//        // false는 디폴트 값이다.
//        val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY, false)
//        //
//
//        val user = firebaseAuth.currentUser
//
//        if (isButtonClicked) {
//            Log.e("isButtonClicked가 true", "ㅇㅇ")
//            user?.getIdToken(true)
//                ?.addOnCompleteListener(OnCompleteListener<GetTokenResult?> { task ->
//                    if (task.isSuccessful) {
//                        viewModelScope.launch {
//                            _navigate.emit(SHOPPING_ACTIVITY)
//                            Log.e("유저가 뭐지???", user.toString())
//                        }
//                    } else {
//                        viewModelScope.launch {
//                            _navigate.emit(ACCOUNT_OPTIONS_FRAGMENT)
//                        }
//                    }
//                })
//        } else {
//            Unit
//        }

//    init {
//        // false는 디폴트 값이다.
//        val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY,false)
//        //회원가입을 하고 앱 종료후 다시 접속시 바로 로그인되는 상황 발생 -> 중간에 auth.logout()을 넣음으로써 임시조치 완료
//        val user = firebaseAuth.currentUser
//
//        if (user != null) {
//            viewModelScope.launch {
//                _navigate.emit(SHOPPING_ACTIVITY)
//            }
//        } else if(isButtonClicked) {
//            viewModelScope.launch {
//                _navigate.emit(ACCOUNT_OPTIONS_FRAGMENT)
//            }
//        } else {
//            Unit
//        }


//    init {
//        // false는 디폴트 값이다.
//        val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY, false)
//
//        val user = firebaseAuth.currentUser
//        if (isButtonClicked) {
//            user?.getIdToken(true)?.addOnCompleteListener(OnCompleteListener<GetTokenResult?>{task ->
//                if (task.isSuccessful) {
//                    viewModelScope.launch {
//                        _navigate.emit(SHOPPING_ACTIVITY)
//                    }
//                } else {
//                    viewModelScope.launch {
//                        _navigate.emit(ACCOUNT_OPTIONS_FRAGMENT)
//                    }
//                }
//            })
//        } else {
//            Unit
//        }

    init {
        val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY, false)

        val user = firebaseAuth.currentUser
        if (user != null) {
            viewModelScope.launch {
                _navigate.emit(SHOPPING_ACTIVITY)
            }
        } else if (isButtonClicked) {
            viewModelScope.launch {
                _navigate.emit(ACCOUNT_OPTIONS_FRAGMENT)
            }
        } else {
            Unit
        }
    }

    fun startButtonClick() {
        sharedPreferences.edit().putBoolean(INTRODUCTION_KEY, true).apply()
    }
}