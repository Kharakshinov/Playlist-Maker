package com.practicum.playlistmaker.search.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.audioplayer.presentation.AudioPlayerActivity
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment: Fragment() {

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val viewModel: SearchViewModel by viewModel()
    private val trackAdapter = TrackAdapter()
    private val trackAdapterHistory = TrackAdapter()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { loadTracks() }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()

        viewModel.state.observe(viewLifecycleOwner){ state ->
            when(state){
                SearchState.Empty -> showEmptyResult()
                SearchState.Error -> showTracksError()
                SearchState.Loading -> showLoading()
                is SearchState.Tracks -> showTracks(state.tracks)
                SearchState.ClearTracks -> {
                    clearSearchText()
                    hideKeyboard()
                    hideTracks()
                }
                is SearchState.History -> {
                    trackAdapterHistory.tracks = state.history
                    trackAdapterHistory.notifyDataSetChanged()
                    if(state.isShown and trackAdapterHistory.tracks.isNotEmpty()){
                        showSearchHistoryViewGroup()
                        hideNoInternetNothingFoundViews()
                    } else {
                        hideSearchHistoryViewGroup()
                    }
                }
                SearchState.ClearHistory -> clearSearchHistory()
            }
        }

        binding.buttonClearHistory.setOnClickListener {
            viewModel.onClearHistoryClicked()
        }

        binding.clearSearchTextButton.setOnClickListener {
            viewModel.searchTextClearClicked()
        }

        binding.buttonRefresh.setOnClickListener {
            viewModel.loadTracks(binding.inputEditText.text.toString())
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                binding.clearSearchTextButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                viewModel.showHistoryTracksEditTextOnFocus(binding.inputEditText)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

        trackAdapter.setOnTrackClickListener(object: TrackAdapter.onTrackClickListener {
            override fun onTrackClick(position: Int) {
                if(clickDebounce()){
                    val chosenTrack = trackAdapter.tracks[position]
                    addTrackToHistory(chosenTrack)

                    val displayAudioPlayer = Intent(requireContext(), AudioPlayerActivity::class.java)
                    displayAudioPlayer.putExtra("chosen_track", Gson().toJson(chosenTrack))
                    startActivity(displayAudioPlayer)
                }
            }
        })

        trackAdapterHistory.setOnTrackClickListener(object: TrackAdapter.onTrackClickListener {
            override fun onTrackClick(position: Int) {
                if(clickDebounce()) {
                    val chosenTrack = trackAdapterHistory.tracks[position]
                    addTrackOnTopSearchHistory(chosenTrack, position)

                    val displayAudioPlayer = Intent(requireContext(), AudioPlayerActivity::class.java)
                    displayAudioPlayer.putExtra("chosen_track", Gson().toJson(chosenTrack))
                    startActivity(displayAudioPlayer)
                }
            }
        })

        binding.inputEditText.setOnFocusChangeListener { _, _ ->
            viewModel.showHistoryTracksEditTextOnFocus(binding.inputEditText)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(searchRunnable)
        _binding = null
    }

    private fun initAdapters() {
        binding.recyclerview.adapter = trackAdapter
        binding.recyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.recyclerviewHistorySearch.adapter = trackAdapterHistory
        binding.recyclerviewHistorySearch.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun loadTracks(){
        viewModel.loadTracks(binding.inputEditText.text.toString())
    }

    private fun addTrackToHistory(chosenTrack: Track){
        if (trackAdapterHistory.tracks.size < 10){
            if(trackAdapterHistory.tracks.isNotEmpty()){
                if(trackAdapterHistory.tracks.contains(chosenTrack)){
                    trackAdapterHistory.tracks.remove(chosenTrack)
                }
                trackAdapterHistory.tracks.add(0, chosenTrack)
            } else {
                trackAdapterHistory.tracks.add(chosenTrack)
            }
        } else {
            if(trackAdapterHistory.tracks.contains(chosenTrack)){
                trackAdapterHistory.tracks.remove(chosenTrack)
                trackAdapterHistory.tracks.add(0, chosenTrack)
            } else {
                for(i in 9 downTo 1){
                    trackAdapterHistory.tracks[i] = trackAdapterHistory.tracks[i-1]
                }
                trackAdapterHistory.tracks[0] = chosenTrack
            }
        }
        trackAdapterHistory.notifyDataSetChanged()
        viewModel.writeToSharedPreferences(trackAdapterHistory.tracks)
    }

    private fun addTrackOnTopSearchHistory(chosenTrack: Track, position: Int){
        trackAdapterHistory.tracks.add(0, chosenTrack)
        trackAdapterHistory.tracks.removeAt(position + 1)
        trackAdapterHistory.notifyDataSetChanged()
        viewModel.writeToSharedPreferences(trackAdapterHistory.tracks)
    }

    private fun clearSearchHistory(){
        trackAdapterHistory.tracks.clear()
        trackAdapterHistory.notifyDataSetChanged()
        viewModel.writeToSharedPreferences(trackAdapterHistory.tracks)
        binding.searchHistory.visibility = View.GONE
    }

    private fun showSearchHistoryViewGroup(){
        binding.searchHistory.visibility = View.VISIBLE
    }

    private fun hideSearchHistoryViewGroup(){
        binding.searchHistory.visibility = View.GONE
    }

    private fun showEmptyResult(){
        binding.progressBar.visibility = View.GONE
        binding.iconNothingFound.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderMessage.text = getString(R.string.nothing_found)
    }

    private fun showTracks(tracks: List<Track>){
        binding.progressBar.visibility = View.GONE
        binding.recyclerview.visibility = View.VISIBLE
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        trackAdapter.notifyDataSetChanged()
    }

    private fun showTracksError(){
        binding.progressBar.visibility = View.GONE
        binding.iconNoInternet.visibility = View.VISIBLE
        binding.buttonRefresh.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding. placeholderMessage.text = getString(R.string.something_went_wrong)
    }

    private fun clearSearchText() {
        binding.inputEditText.setText("")
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val focusedView = requireActivity().currentFocus
        if (focusedView != null) {
            inputMethodManager?.hideSoftInputFromWindow(focusedView.windowToken, 0)
        }
    }

    private fun hideTracks() {
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
    }

    private fun hideNoInternetNothingFoundViews(){
        binding.iconNoInternet.visibility = View.GONE
        binding.buttonRefresh.visibility = View.GONE
        binding.iconNothingFound.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
    }

    private fun showLoading() {
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
        binding.recyclerview.visibility = View.GONE
        binding.iconNoInternet.visibility = View.GONE
        binding.buttonRefresh.visibility = View.GONE
        binding.iconNothingFound.visibility = View.GONE
        binding.searchHistory.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}