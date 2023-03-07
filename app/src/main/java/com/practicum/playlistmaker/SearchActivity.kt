package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.model.TracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var textSearch = ""
    private val hidePlaceholders = "Скрыть плейсхолдеры"
    private lateinit var rvTrack: RecyclerView
    private lateinit var inputEditText: EditText
    private lateinit var buttonGoBack: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var iconNothingFound:ImageView
    private lateinit var iconNoInternet:ImageView
    private lateinit var buttonRefresh:Button
    private val trackAdapter = TrackAdapter()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesApi::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        buttonGoBack = findViewById(R.id.button_go_back)
        buttonRefresh = findViewById(R.id.button_refresh)
        inputEditText = findViewById(R.id.inputEditText)
        clearButton = findViewById(R.id.clearIcon)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        iconNothingFound = findViewById(R.id.icon_nothing_found)
        iconNoInternet = findViewById(R.id.icon_no_internet)

        rvTrack = findViewById(R.id.recyclerview)

        buttonGoBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            trackAdapter.tracks.clear()
            trackAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }

        buttonRefresh.setOnClickListener {
            makeSearch()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                textSearch = inputEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        rvTrack.adapter = trackAdapter

        rvTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                makeSearch()
                true
            }
            false
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun makeSearch(){
        if (inputEditText.text.isNotEmpty()) {
            iTunesService.searchTrack(inputEditText.text.toString()).enqueue(object :
                Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>,
                                        response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        trackAdapter.tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackAdapter.tracks.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                            showPlaceholder(hidePlaceholders)
                        }
                        if (trackAdapter.tracks.isEmpty()) {
                            showMessage(getString(R.string.nothing_found), "")
                            showPlaceholder(getString(R.string.nothing_found))
                        } else {
                            showMessage("", "")
                        }
                    } else {
                        showMessage(getString(R.string.something_went_wrong), response.code().toString())
                        showPlaceholder(getString(R.string.something_went_wrong))
                    }
                }
                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showMessage(getString(R.string.something_went_wrong), t.message.toString())
                    showPlaceholder(getString(R.string.something_went_wrong))
                }
            })
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            trackAdapter.tracks.clear()
            trackAdapter.notifyDataSetChanged()
            placeholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeholderMessage.visibility = View.GONE
        }
    }

    private fun showPlaceholder(text: String){
        when (text) {
            getString(R.string.something_went_wrong) -> {
                iconNothingFound.visibility = View.GONE
                iconNoInternet.visibility = View.VISIBLE
                buttonRefresh.visibility = View.VISIBLE
            }
            getString(R.string.nothing_found) -> {
                iconNoInternet.visibility = View.GONE
                buttonRefresh.visibility = View.GONE
                iconNothingFound.visibility = View.VISIBLE
            }
            hidePlaceholders -> {
                iconNoInternet.visibility = View.GONE
                buttonRefresh.visibility = View.GONE
                iconNothingFound.visibility = View.GONE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, textSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(SEARCH_TEXT).toString()
        inputEditText.setText(textSearch)
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }
}