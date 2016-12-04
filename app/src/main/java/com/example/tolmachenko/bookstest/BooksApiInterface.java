package com.example.tolmachenko.bookstest;

import com.example.tolmachenko.bookstest.model.CustomResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static android.R.attr.key;

/**
 * Created by Olga Tolmachenko on 02.12.16.
 */

public interface BooksApiInterface {

    @Headers("content-type: application/json")
    @GET("books/v1/volumes")
    Call<CustomResponse> getBooks(@Query("q") String searchQuery, @Query("startIndex") int startIndex, @Query("key") String key);
}
