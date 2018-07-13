package com.testapp.myapplication.utils;

import com.testapp.myapplication.models.RepoItems;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("repos")
    Call<List<RepoItems>> getRepoList(@Query("page") String page, @Query("per_page") String perPage);

}
