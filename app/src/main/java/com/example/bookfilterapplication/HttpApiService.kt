package com.example.bookfilterapplication

import retrofit2.http.GET

interface HttpApiService {
    @GET("/books")
    suspend fun getAllBooks(): List<BookResults>
}