package com.rondao.shufflesongs.ui.songslist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.rondao.shufflesongs.R
import com.rondao.shufflesongs.databinding.FragmentSongsListBinding

class SongsListFragment : Fragment() {

    private val viewModel: SongsListViewModel by lazy {
        ViewModelProviders.of(this).get(SongsListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentSongsListBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }
}
