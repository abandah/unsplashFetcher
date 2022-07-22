package com.ex.assignment.retrofit;


import com.ex.assignment.model.UnSplashPicture;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    @GET("search/photos?client_id=S4CTYmINIsomHVMjXi70r7SrfoHEddRQi-7njoAhEIc")
    Call<UnSplashPicture> searchForPic(@Query("page") int page, @Query("query") String query);
}
