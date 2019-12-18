package com.rondao.shufflesongs.ui.songslist

import android.os.Bundle
import android.view.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
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
        viewModel.fetchSongsList()

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        getDrawable(context!!, R.drawable.divider)?.let { divider.setDrawable(it) }
        binding.songsList.addItemDecoration(divider)

        val adapter = SongsListAdapter()
        binding.songsList.adapter = adapter

        viewModel.songsList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.songs_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_shuffle_songs -> viewModel.shuffleSongs()
        }
        return true
    }
}
