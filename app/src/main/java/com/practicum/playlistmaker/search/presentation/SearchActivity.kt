package com.practicum.playlistmaker.search.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.Creator

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerViewSearch: RecyclerView
    private lateinit var recyclerViewSearchHistory: RecyclerView
    private lateinit var inputEditText: EditText
    private lateinit var buttonGoBack: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var searchHistoryViewGroup: ConstraintLayout
    private lateinit var placeholderMessage: TextView
    private lateinit var searchHistoryText: TextView
    private lateinit var iconNothingFound:ImageView
    private lateinit var iconNoInternet:ImageView
    private lateinit var buttonRefresh:Button
    private lateinit var buttonClearSearchHistory:Button
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: SearchViewModel
    private lateinit var router: SearchRouter
    private val trackAdapter = TrackAdapter()
    private val trackAdapterHistory = TrackAdapter()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { loadTracks() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initView()
        initAdapters()
        initSharedPreferences()
        val interactor = Creator.provideSearchInteracor(sharedPreferences)
        router = SearchRouter(this)
        viewModel = ViewModelProvider(this, SearchViewModelFactory(interactor))[SearchViewModel::class.java]
        initHistory()

        viewModel.state.observe(this){ state ->
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
                    if(state.isShown){
                        showSearchHistoryViewGroup()
                        hideNoInternetNothingFoundViews()
                    } else {
                        hideSearchHistoryViewGroup()
                    }
                }
                SearchState.ClearHistory -> clearSearchHistory()
            }
        }

        viewModel.sharedPreferencesLiveData.observe(this){
            trackAdapterHistory.tracks = it
        }

        buttonClearSearchHistory.setOnClickListener {
            viewModel.onClearHistoryClicked()
        }

        buttonGoBack.setOnClickListener {
            router.goBack()
        }

        clearButton.setOnClickListener {
            viewModel.searchTextClearClicked()
        }

        buttonRefresh.setOnClickListener {
            viewModel.loadTracks(inputEditText.text.toString())
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                viewModel.showHistoryTracksEditTextOnFocus(inputEditText, trackAdapterHistory.tracks)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        trackAdapter.setOnTrackClickListener(object: TrackAdapter.onTrackClickListener {
            override fun onTrackClick(position: Int) {
                if(clickDebounce()){
                    val chosenTrack = trackAdapter.tracks[position]
                    addTrackToHistory(chosenTrack)
                    router.openTrack(chosenTrack)
                }
            }
        })

        trackAdapterHistory.setOnTrackClickListener(object: TrackAdapter.onTrackClickListener {
            override fun onTrackClick(position: Int) {
                if(clickDebounce()) {
                    val chosenTrack = trackAdapterHistory.tracks[position]
                    addTrackOnTopSearchHistory(chosenTrack, position)
                    router.openTrack(chosenTrack)
                }
            }
        })

        inputEditText.setOnFocusChangeListener { _, _ ->
            viewModel.showHistoryTracksEditTextOnFocus(inputEditText, trackAdapterHistory.tracks)
        }

    }

    private fun initView(){
        buttonGoBack = findViewById(R.id.button_go_back)
        buttonRefresh = findViewById(R.id.button_refresh)
        inputEditText = findViewById(R.id.inputEditText)
        clearButton = findViewById(R.id.clearIcon)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        iconNothingFound = findViewById(R.id.icon_nothing_found)
        iconNoInternet = findViewById(R.id.icon_no_internet)
        progressBar = findViewById(R.id.progressBar)
        searchHistoryText = findViewById(R.id.search_message)
        searchHistoryViewGroup = findViewById(R.id.search_history)
        buttonClearSearchHistory = findViewById(R.id.button_clear_history)
        recyclerViewSearch = findViewById(R.id.recyclerview)
        recyclerViewSearchHistory = findViewById(R.id.recyclerview_history_search)
    }

    private fun initSharedPreferences(){
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
    }
    private fun initHistory(){
        viewModel.readFromSharedPreferences()
    }

    private fun initAdapters() {
        recyclerViewSearch.adapter = trackAdapter
        recyclerViewSearch.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerViewSearchHistory.adapter = trackAdapterHistory
        recyclerViewSearchHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun loadTracks(){
        viewModel.loadTracks(inputEditText.text.toString())
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
        searchHistoryViewGroup.visibility = View.GONE
    }

    private fun showSearchHistoryViewGroup(){
        searchHistoryViewGroup.visibility = View.VISIBLE
    }

    private fun hideSearchHistoryViewGroup(){
        searchHistoryViewGroup.visibility = View.GONE
    }

    private fun showEmptyResult(){
        progressBar.visibility = View.GONE
        iconNothingFound.visibility = View.VISIBLE
        placeholderMessage.visibility = View.VISIBLE
        placeholderMessage.text = getString(R.string.nothing_found)
    }

    private fun showTracks(tracks: List<Track>){
        progressBar.visibility = View.GONE
        recyclerViewSearch.visibility = View.VISIBLE
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        trackAdapter.notifyDataSetChanged()
    }

    private fun showTracksError(){
        progressBar.visibility = View.GONE
        iconNoInternet.visibility = View.VISIBLE
        buttonRefresh.visibility = View.VISIBLE
        placeholderMessage.visibility = View.VISIBLE
        placeholderMessage.text = getString(R.string.something_went_wrong)
    }

    private fun clearSearchText() {
        inputEditText.setText("")
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

    private fun hideTracks() {
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
    }

    private fun hideNoInternetNothingFoundViews(){
        iconNoInternet.visibility = View.GONE
        buttonRefresh.visibility = View.GONE
        iconNothingFound.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
    }

    private fun showLoading() {
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
        recyclerViewSearch.visibility = View.GONE
        iconNoInternet.visibility = View.GONE
        buttonRefresh.visibility = View.GONE
        iconNothingFound.visibility = View.GONE
        searchHistoryViewGroup.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
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

    companion object {
        const val SHARED_PREFERENCES = "shared_preferences_playlistmaker"
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}