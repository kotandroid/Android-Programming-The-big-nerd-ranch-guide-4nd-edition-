package com.bignerdranch.android.photogallery.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 인터셉터는 요청이나 응답의 정보를 가로채서 우리가 원하는 처리를 할 수 있게 해준다.
 * 즉, 요청이 서버에 전송되기 전에 매개변수와 값을 URL에 추가하거나 변경할 수 있으며, 서버로부터 수신된 응답에서
 * 원하는 데이터만 발췌하여 사용할 수 있다.
 * */

private const val API_KEY = "a66754461878f0ce660546d4a0259c71"

class PhotoInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()  // 원래 요청

        val newUrl: HttpUrl = originalRequest.url().newBuilder()    // URL만 가져온다.
            .addQueryParameter("api_key", API_KEY)
            .addQueryParameter("format", "json")
            .addQueryParameter("nojsoncallback", "1")
            .addQueryParameter("extras", "url_s")
            .addQueryParameter("safesearch", "1")
            .build()

        val newRequest: Request = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        // proceed()를 호출해 요청을 전송하고 응답을 나타내는 Response 객체를 받는다.
        // 호출하지 않으면 네트워크 요청이 수행되지 않는다.
        return chain.proceed(newRequest)
    }
}