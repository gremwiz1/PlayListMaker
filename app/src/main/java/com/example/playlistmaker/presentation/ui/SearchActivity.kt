package com.example.playlistmaker.presentation.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.impl.SearchHistoryManager
import com.example.playlistmaker.presentation.TrackAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private lateinit var arrowBackButton: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var problemImage: ImageView
    private lateinit var problemText: TextView
    private lateinit var youSearchText: TextView
    private lateinit var refreshButton: Button
    private lateinit var clearHistoryButton: Button
    private lateinit var problemLinearLayout: LinearLayout
    private lateinit var arrayTrack: ArrayList<Track>
    private lateinit var trackAdapter: TrackAdapter
    private var mainThreadHandler: Handler? = null
    private lateinit var searchRunnable: Runnable
    private var isClickAllowed = true
    private lateinit var progressBar: ProgressBar

    private val tracksInteractor = Creator.provideTracksInteractor()
    private lateinit var searchHistoryManager: SearchHistoryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchHistoryManager = SearchHistoryManager(this)

        progressBar = findViewById(R.id.progressBar)
        inputEditText = findViewById(R.id.inputEditText)
        arrowBackButton = findViewById(R.id.backArrow)
        clearButton = findViewById(R.id.clearIcon)
        problemLinearLayout = findViewById(R.id.problemLinearLayout)
        problemImage = findViewById(R.id.problemImage)
        problemText = findViewById(R.id.problemText)
        refreshButton = findViewById(R.id.refreshButton)
        youSearchText = findViewById(R.id.you_search)
        clearHistoryButton = findViewById(R.id.clear_history)

        arrayTrack = ArrayList()
        trackAdapter = TrackAdapter(arrayTrack) { clickedTrack ->
            if (clickDebounce()) {
                searchHistoryManager.addTrackInHistoryAndNavigate(this, clickedTrack)
            }
        }

        val trackList = findViewById<RecyclerView>(R.id.trackList)
        trackList.layoutManager = LinearLayoutManager(this)
        trackList.adapter = trackAdapter

        arrowBackButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard()
            hideProblemsElement()
            arrayTrack.clear()
            trackAdapter.notifyDataSetChanged()
            showHistoryTracks()
        }

        clearHistoryButton.setOnClickListener {
            searchHistoryManager.clearListHistory()
            arrayTrack.clear()
            trackAdapter.notifyDataSetChanged()
            clearHistoryButton.visibility = View.GONE
            youSearchText.visibility = View.GONE
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    if (inputEditText.hasFocus()) {
                        showHistoryTracks()
                    }
                } else {
                    arrayTrack.clear()
                    trackAdapter.notifyDataSetChanged()
                    clearHistoryButton.visibility = View.GONE
                    youSearchText.visibility = View.GONE
                    searchDebounce()
                }
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        searchRunnable = Runnable { searchSongs() }
        mainThreadHandler = Handler(Looper.getMainLooper())
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSongs()
                true
            } else {
                false
            }
        }
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) {
                showHistoryTracks()
            }
        }

        refreshButton.setOnClickListener {
            searchSongs()
        }
    }

    private fun searchDebounce() {
        mainThreadHandler?.removeCallbacks(searchRunnable)
        mainThreadHandler?.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(inputEditText.windowToken, 0)
    }

    private fun searchSongs() {
        if (inputEditText.text.toString().isNotEmpty()) {
            progressBar.visibility = View.VISIBLE
            tracksInteractor.searchTracks(inputEditText.text.toString(),
                object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>, isSuccessful: Boolean) {
                        mainThreadHandler?.post {
                            if (isSuccessful) {
                                arrayTrack.clear()
                                arrayTrack.addAll(foundTracks)
                                trackAdapter.notifyDataSetChanged()
                                if (arrayTrack.isEmpty()) {
                                    displayNoResultsFound()
                                } else {
                                    hideProblemsElement()
                                }
                            } else {
                                showProblemWithInternet()
                            }
                            progressBar.visibility = View.GONE
                        }
                    }
                })
        }
    }

    private fun displayNoResultsFound() {
        problemText.text = getString(R.string.textNotFound)
        refreshButton.visibility = View.GONE
        problemImage.setImageResource(R.drawable.not_found_tracks)
        problemImage.visibility = View.VISIBLE
        problemText.visibility = View.VISIBLE
        problemLinearLayout.visibility = View.VISIBLE
    }

    private fun hideProblemsElement() {
        problemImage.visibility = View.GONE
        problemText.visibility = View.GONE
        refreshButton.visibility = View.GONE
        problemLinearLayout.visibility = View.GONE
    }

    private fun showProblemWithInternet() {
        problemText.text = getString(R.string.textProblemWithInternet)
        refreshButton.visibility = View.VISIBLE
        problemImage.setImageResource(R.drawable.problem_with_internet)
        problemImage.visibility = View.VISIBLE
        problemText.visibility = View.VISIBLE
        problemLinearLayout.visibility = View.VISIBLE
    }

    private fun showHistoryTracks() {
        val historyTracks = searchHistoryManager.getListHistoryTracks()
        if (historyTracks.isNotEmpty()) {
            arrayTrack.clear()
            arrayTrack.addAll(historyTracks)
            trackAdapter.notifyDataSetChanged()
            clearHistoryButton.visibility = View.VISIBLE
            youSearchText.visibility = View.VISIBLE
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            mainThreadHandler?.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
