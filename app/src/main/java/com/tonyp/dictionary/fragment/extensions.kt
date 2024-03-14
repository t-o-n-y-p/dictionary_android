package com.tonyp.dictionary.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tonyp.dictionary.R
import com.tonyp.dictionary.fragment.modal.login.LoginOnlyBottomSheetDialogFragment

fun BottomSheetDialogFragment.dismissWithToast(resId: Int) {
    dismiss()
    Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
}

fun Fragment.setFragmentResult(fragment: String, key: String, value: String) =
    setFragmentResult(
        fragment,
        Bundle().apply { putString(key, value) }
    )

fun FragmentActivity.showLogInAlert(): AlertDialog =
    MaterialAlertDialogBuilder(this, R.style.Alert)
        .setTitle(getString(R.string.logged_out))
        .setMessage(getString(R.string.you_must_log_in_again))
        .setNegativeButton(getString(R.string.later)) { _, _ -> }
        .setPositiveButton(getString(R.string.log_in)) { _, _ ->
            LoginOnlyBottomSheetDialogFragment().show(
                supportFragmentManager,
                LoginOnlyBottomSheetDialogFragment::class.simpleName
            )
        }
        .show()