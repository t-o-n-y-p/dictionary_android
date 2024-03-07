package com.tonyp.dictionary.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun BottomSheetDialogFragment.dismissWithToast(resId: Int) {
    dismiss()
    Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
}

fun Fragment.setFragmentResult(fragment: String, key: String, value: String) =
    setFragmentResult(
        fragment,
        Bundle().apply { putString(key, value) }
    )