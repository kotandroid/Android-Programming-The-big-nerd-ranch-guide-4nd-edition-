package com.bignerdranch.android.photogallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrApi {
    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
                "&api_key=a66754461878f0ce660546d4a0259c71" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s"
    )

    fun fetchPhotos(): Call<FlickrResponse>

//    @GET(
//        "services/rest/?method=flickr.interestingness.getList"
//    )
//    fun fetchPhotos(
//        @Query("api_key") api_key :String,
//        @Query("format") format: String ="json",
//        @Query("nojsoncallback") nojsoncallback: Int =1,
//        @Query("extras") extras: String ="url_s"
//    ): Call<PhotoResponse>


    /**
     * @GET 어노테이션에 파라미터가 없으면 지정된 URL로 요청함
     * ex ) photogallery D/OkHttp: --> GET https://live.staticflickr.com/65535/51892852059_a874e780a0_m.jpg
     * */
    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>
}