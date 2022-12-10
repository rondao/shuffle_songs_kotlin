package com.rondao.shufflesongs.ui.songslist

import android.os.Bundle
import android.view.*
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.rondao.shufflesongs.R
import com.rondao.shufflesongs.databinding.FragmentSongsListBinding


class SongsListFragment : Fragment() {

    /**
     * Lazy construction of this fragment ViewModel.
     */
    private val viewModel: SongsListViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this,
            SongsListViewModel.Factory(activity.application)
        )[SongsListViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentSongsListBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Add [RecyclerView] line separator.
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        getDrawable(requireContext(), R.drawable.divider)?.let { divider.setDrawable(it) }
        binding.songsList.addItemDecoration(divider)

        val adapter = SongsListAdapter()
        binding.songsList.adapter = adapter

        // Observe [songs] to update [RecyclerView] adapter.
        viewModel.songs.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        // One-time event for failure warning.
        viewModel.eventStatusFailed.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let {
                Snackbar.make(
                    binding.root,
                    getString(R.string.snackbar_fail_retrieve_songs),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.songs_list_menu, menu)

        // Shuffling action button should only be available if [songs] have content.
        viewModel.isSongsListNotEmpty.observe(viewLifecycleOwner) {
            menu.findItem(R.id.action_shuffle_songs).isVisible = it
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_shuffle_songs -> viewModel.shuffleSongs()
        }
        return true
    }
}
