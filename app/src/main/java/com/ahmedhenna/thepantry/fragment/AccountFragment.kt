package com.ahmedhenna.thepantry.fragment

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ahmedhenna.thepantry.R
import com.ahmedhenna.thepantry.common.px
import com.ahmedhenna.thepantry.databinding.FragmentAccountBinding
import com.ahmedhenna.thepantry.dialog.ResetConfirmDialogFragment
import com.ahmedhenna.thepantry.view_model.AuthViewModel

class AccountFragment : LoadableFragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var parentNavController: NavController
    private val authViewModel: AuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentNavController = Navigation.findNavController(requireActivity(), R.id.mainNav)

        binding.logOut.setOnClickListener {
            val action = BottomNavigationFragmentDirections.actionBottomNavigationFragmentToOnboardingFragment()
            parentNavController.navigate(action)
        }

        binding.changePassword.setOnClickListener {
            showLoading()
            authViewModel.sendResetPasswordEmail(onComplete = {
                val resetConfirmDialogFragment = ResetConfirmDialogFragment {}
                resetConfirmDialogFragment.show(childFragmentManager, "RESET")
                hideLoading()
            }, onFail = {
                Log.e("RESET FAIL", it)
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                hideLoading()
            })
        }

        binding.logOut.setOnClickListener {
            authViewModel.signOut()
            val action =
                BottomNavigationFragmentDirections.actionBottomNavigationFragmentToOnboardingFragment()
            parentNavController.navigate(action)
        }

        requireContext().let {
            val drawable: GradientDrawable = binding.userImageBorder.background as GradientDrawable
            drawable.setStroke(2.px, ContextCompat.getColor(it, R.color.red))
        }

        val user = authViewModel.getCurrentUser()
        binding.userName.text = user.displayName


    }


}