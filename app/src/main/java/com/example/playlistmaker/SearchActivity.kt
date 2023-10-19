package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private lateinit var arrowBackButton: ImageView
    private lateinit var clearButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        inputEditText = findViewById(R.id.inputEditText)
        arrowBackButton = findViewById(R.id.backArrow)
        clearButton = findViewById(R.id.clearIcon)

        arrowBackButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard()
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
        //добавляем созданный simpleTextWatcher к EditText
        inputEditText.addTextChangedListener(simpleTextWatcher)
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
        const val KEY_INPUT_TEXT = "inputText"
    }
}