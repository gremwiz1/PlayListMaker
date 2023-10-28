package com.example.playlistmaker

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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private lateinit var arrowBackButton: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var problemImage: ImageView
    private lateinit var problemText: TextView
    private lateinit var refreshButton: Button
    private lateinit var problemLinearLayout: LinearLayout
    private lateinit var arrayTrack: ArrayList<Track>
    private lateinit var retrofit: Retrofit
    private lateinit var iTunesApiService: ITunesApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.base_url_itunes))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        iTunesApiService = retrofit.create(ITunesApiService::class.java)

        inputEditText = findViewById(R.id.inputEditText)
        arrowBackButton = findViewById(R.id.backArrow)
        clearButton = findViewById(R.id.clearIcon)
        problemLinearLayout = findViewById(R.id.problemLinearLayout)
        problemImage = findViewById(R.id.problemImage)
        problemText = findViewById(R.id.problemText)
        refreshButton = findViewById(R.id.refreshButton)

        arrayTrack = ArrayList<Track>()
        val trackAdapter = TrackAdapter(arrayTrack)
        val trackList = findViewById<RecyclerView>(R.id.trackList)
        trackList.layoutManager = LinearLayoutManager(this)

        trackList.adapter = trackAdapter

        arrowBackButton.setOnClickListener {
            finish()
        }

        fun hideProblemsElement() {
            problemImage.visibility = View.GONE
            problemText.visibility = View.GONE
            refreshButton.visibility = View.GONE
            problemLinearLayout.visibility = View.GONE
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard()
            hideProblemsElement()
            arrayTrack.clear()
            trackAdapter.notifyDataSetChanged()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    // linearLayout.setBackgroundColor(getColor(R.color.prime_neutral))
                } else {
                    val input = s.toString()
                }
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        fun searchSongs() {
            if (inputEditText.text.toString().isNotEmpty()) {
                arrayTrack.clear()
                iTunesApiService.searchSongs(inputEditText.text.toString())
                    .enqueue(object : Callback<TrackResponseBody> {
                        override fun onResponse(
                            call: Call<TrackResponseBody>,
                            response: Response<TrackResponseBody>,
                        ) {
                            if (response.code() == 200) {
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    arrayTrack.addAll(response.body()?.results!!)
                                    hideProblemsElement()
                                    trackAdapter.notifyDataSetChanged()
                                } else {
                                    problemText.text = getString(R.string.textNotFound)
                                    refreshButton.visibility = View.GONE
                                    problemImage.setImageResource(R.drawable.not_found_tracks)
                                    problemImage.visibility = View.VISIBLE
                                    problemText.visibility = View.VISIBLE
                                    problemLinearLayout.visibility = View.VISIBLE
                                }
                            } else {

                            }
                        }

                        override fun onFailure(call: Call<TrackResponseBody>, t: Throwable) {

                        }
                    })
            }
        }
        //добавляем созданный simpleTextWatcher к EditText
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSongs()
            }
            false
        }

        refreshButton.setOnClickListener() {
            searchSongs()
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val inputText = inputEditText.text.toString()
        outState.putString(KEY_INPUT_TEXT, inputText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val inputText = savedInstanceState.getString(KEY_INPUT_TEXT)
        inputEditText.setText(inputText)
    }

    companion object {
        private val KEY_INPUT_TEXT = "inputText"
    }
}