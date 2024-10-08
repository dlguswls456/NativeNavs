package com.circus.nativenavs.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.circus.nativenavs.R
import com.circus.nativenavs.config.BaseFragment
import com.circus.nativenavs.databinding.FragmentSignUpCompleteBinding
import com.circus.nativenavs.ui.home.HomeActivity
import com.circus.nativenavs.ui.login.LoginActivity

class SignUpCompleteFragment : BaseFragment<FragmentSignUpCompleteBinding>(
    FragmentSignUpCompleteBinding::bind,
    R.layout.fragment_sign_up_complete
) {

    private lateinit var signUpActivity: SignUpActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        signUpActivity = context as SignUpActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupCompleteBtn.setOnClickListener {
            signUpActivity.finish()
        }
    }

}