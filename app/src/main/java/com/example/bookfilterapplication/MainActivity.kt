package com.example.bookfilterapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    var author = ""
    var country = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val authorText = findViewById<TextInputEditText>(R.id.authorTextView)
        val countryText = findViewById<TextInputEditText>(R.id.countryTextView)
        val filterButton = findViewById<Button>(R.id.filterButton)
        val resultCount = findViewById<TextView>(R.id.resultCountTextView)
        val result1 = findViewById<TextView>(R.id.resultTextView1)
        val result2 = findViewById<TextView>(R.id.resultTextView2)
        val result3 = findViewById<TextView>(R.id.resultTextView3)

        filterButton.setOnClickListener {
            author = authorText.text.toString()
            country = countryText.text.toString()

            val booksList = mutableListOf<BookResults>()

            if (author.isNullOrEmpty() && country.isNullOrEmpty()) {
                resultCount.text = "Please enter Author Name and Country"
                result1.text = ""
                result2.text = ""
                result3.text = ""
            }
            else if (author.isNullOrEmpty()) {
                resultCount.text = "Please enter Author Name"
                result1.text = ""
                result2.text = ""
                result3.text = ""
            }
            else if (country.isNullOrEmpty()) {
                resultCount.text = "Please enter Country"
                result1.text = ""
                result2.text = ""
                result3.text = ""
            }else {
                val myApp = application as MyApplication
                val httpApiService = myApp.httpApiService

                //run in network(IO) thread
                CoroutineScope(Dispatchers.IO).launch {
                    val result = httpApiService.getAllBooks()     //http req here

                    for (books in result) {
                        if ((books.author == author) && (books.country == country)) {
                            booksList.add(books)
                        }

                    }
                    var count = booksList.size

                    withContext(Dispatchers.Main) {
                        resultCount.text = "Results: $count"

                        if (count >= 3) {
                            val book1 = booksList.get(0).title
                            result1.text = "Result: $book1"

                            val book2 = booksList.get(1).title
                            result2.text = "Result: $book2"

                            val book3 = booksList.get(2).title
                            result3.text = "Result: $book3"
                        }

                        if (count == 2) {
                            val book1 = booksList.get(0).title
                            result1.text = "Result: $book1"

                            val book2 = booksList.get(1).title
                            result2.text = "Result: $book2"

                            result3.text = ""
                        }

                        if (count == 1) {
                            val book1 = booksList.get(0).title
                            result1.text = "Result: $book1"

                            result2.text = ""
                            result3.text = ""
                        }


                    }
                }
            }
        }
    }
}