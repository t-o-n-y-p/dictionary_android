package com.tonyp.dictionary.fragment.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tonyp.dictionary.R
import com.tonyp.dictionary.databinding.FragmentRecentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentFragment : Fragment(R.layout.fragment_recent) {

    private lateinit var binding: FragmentRecentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentBinding.inflate(inflater)
        return binding.root
    }
}