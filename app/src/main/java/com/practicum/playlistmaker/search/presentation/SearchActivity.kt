package com.practicum.playlistmaker.search.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.data.SearchRepository
import com.practicum.playlistmaker.search.data.SharedPreferencesWriteRead
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.iTunesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), SearchScreenView {

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
    private lateinit var sharedPreferencesWriteRead : SharedPreferencesWriteRead
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: SearchPresenter
    private val trackAdapter = TrackAdapter()
    private val trackAdapterHistory = TrackAdapter()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val repository = SearchRepository(retrofit.create(iTunesApi::class.java))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initView()
        initAdapters()
        initSharedPreferences()
        presenter = SearchPresenter(
            view = this,
            trackAdapter = trackAdapter,
            trackAdapterHistory = trackAdapterHistory,
            interactor = SearchInteractor(sharedPreferencesWriteRead, repository),
            inputEditText = inputEditText,
            router = SearchRouter(this)
        )
        initHistory()

        buttonClearSearchHistory.setOnClickListener {
            presenter.onClearHistoryClicked()
        }

        buttonGoBack.setOnClickListener {
            presenter.buttonGoBackClicked()
        }

        clearButton.setOnClickListener {
            presenter.searchTextClearClicked()
        }

        buttonRefresh.setOnClickListener {
            presenter.loadTracks(inputEditText.text.toString())
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.searchDebounce()
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                presenter.showHistoryTracksEditTextOnFocus()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        trackAdapter.setOnTrackClickListener(object: TrackAdapter.onTrackClickListener {
            override fun onTrackClick(position: Int) {
                presenter.onTrackClicked(position)
            }
        })

        trackAdapterHistory.setOnTrackClickListener(object: TrackAdapter.onTrackClickListener {
            override fun onTrackClick(position: Int) {
                presenter.onHistoryTrackClicked(position)
            }
        })

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            presenter.searchFocusChanged(hasFocus, inputEditText.text.toString())
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
        sharedPreferencesWriteRead = SharedPreferencesWriteRead(sharedPreferences)
    }
    private fun initHistory(){
        trackAdapterHistory.tracks = presenter.readFromSharedPreferences()
    }

    private fun initAdapters() {
        recyclerViewSearch.adapter = trackAdapter
        recyclerViewSearch.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerViewSearchHistory.adapter = trackAdapterHistory
        recyclerViewSearchHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun showSearchHistory(position: Int){
        trackAdapterHistory.tracks = presenter.readFromSharedPreferences()
        val chosenTrack = trackAdapter.tracks[position]
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
        presenter.writeToSharedPreferences(trackAdapterHistory.tracks)
    }

    override fun addTrackOnTopSearchHistory(chosenTrack: Track, position: Int){
        trackAdapterHistory.tracks = presenter.readFromSharedPreferences()
        trackAdapterHistory.tracks.add(0, chosenTrack)
        trackAdapterHistory.tracks.removeAt(position + 1)
        trackAdapterHistory.notifyDataSetChanged()
        presenter.writeToSharedPreferences(trackAdapterHistory.tracks)
    }

    override fun clearSearchHistory(){
        trackAdapterHistory.tracks.clear()
        trackAdapterHistory.notifyDataSetChanged()
        presenter.writeToSharedPreferences(trackAdapterHistory.tracks)
        searchHistoryViewGroup.visibility = View.GONE
    }

    override fun showSearchHistoryViewGroup(){
        searchHistoryViewGroup.visibility = View.VISIBLE
    }

    override fun hideSearchHistoryViewGroup(){
        searchHistoryViewGroup.visibility = View.GONE
    }

    override fun showEmptyResult(){
        progressBar.visibility = View.GONE
        iconNothingFound.visibility = View.VISIBLE
        placeholderMessage.visibility = View.VISIBLE
        placeholderMessage.text = getString(R.string.nothing_found)
    }

    override fun showTracks(tracks: List<Track>){
        progressBar.visibility = View.GONE
        recyclerViewSearch.visibility = View.VISIBLE
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        trackAdapter.notifyDataSetChanged()
    }

    override fun showTracksError(){
        progressBar.visibility = View.GONE
        iconNoInternet.visibility = View.VISIBLE
        buttonRefresh.visibility = View.VISIBLE
        placeholderMessage.visibility = View.VISIBLE
        placeholderMessage.text = getString(R.string.something_went_wrong)
    }

    override fun clearSearchText() {
        inputEditText.setText("")
    }

    override fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

    override fun hideTracks() {
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
    }

    override fun hideNoInternetNothingFoundViews(){
        iconNoInternet.visibility = View.GONE
        buttonRefresh.visibility = View.GONE
        iconNothingFound.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
    }

    override fun showLoading() {
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

    companion object {
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
        const val SHARED_PREFERENCES = "shared_preferences_playlistmaker"
    }
}