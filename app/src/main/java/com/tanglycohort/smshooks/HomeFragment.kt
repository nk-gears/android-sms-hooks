package com.tanglycohort.smshooks

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {
    private var askedForPermissions = false
    private val registrationToAskPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted -> onAskPermissionResult(isGranted) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.RECEIVE_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        } else if (askedForPermissions) {
            askedForPermissions = false
            if (shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)) {
                NavOptions.Builder().apply {
                    setEnterAnim(R.anim.slide_in_bottom)
                    setPopExitAnim(R.anim.slide_out_bottom)
                    findNavController().navigate(R.id.permissionsEduFragment, null, build())
                }
            } else {
                NavOptions.Builder().apply {
                    setEnterAnim(R.anim.slide_in_bottom)
                    setPopExitAnim(R.anim.slide_out_bottom)
                    findNavController().navigate(R.id.permissionsDeniedFragment, null, build())
                }
            }
        } else {
            registrationToAskPermission.launch(Manifest.permission.RECEIVE_SMS)
        }
    }

    private fun onAskPermissionResult(isGranted: Boolean) {
        askedForPermissions = true
    }
}