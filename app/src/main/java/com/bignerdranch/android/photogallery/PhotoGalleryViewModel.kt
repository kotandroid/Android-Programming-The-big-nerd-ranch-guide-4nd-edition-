package com.bignerdranch.android.photogallery

import android.app.Application
import androidx.lifecycle.*

class PhotoGalleryViewModel(private val app: Application) : AndroidViewModel(app) {

    val galleryItemLiveData: LiveData<List<GalleryItem>>
    // 뷰모델의 생명주기 동안 FlickrFetchr의 인스턴스를 한번만 생성된다
    // 장점 : Retrofit 인스턴스와 FlickrApi 인스턴스를 다시 생성하는 것을 방지할 수 있어서 앱의 실행 속도가 빨라진다.
    private val flickrFetchr = FlickrFetchr()
    private val mutableSearchTerm = MutableLiveData<String>()

    val searchTerm: String
        get() = mutableSearchTerm.value ?: ""


    // 최초로 생성되면 공유 프리퍼런스에서 쿼리 문자열을 읽어 mutableSearchTerm을 초기화하고, 변경될 때마다 공유 프리퍼런스에 넣는다.
    // Transformations.switchMap은 Observer의 lifecycle에 안전하게 데이터를 전달할 수 있다.
    // lazy로 동작하기 때문에 원천이 되는 객체의 변화가 일어나지 않는다면 동작하지 않는다.
    init {
        mutableSearchTerm.value = QueryPreferences.getStoredQuery(app)

        galleryItemLiveData = Transformations.switchMap(mutableSearchTerm) { searchTerm ->
            if (searchTerm.isBlank()) {
                flickrFetchr.fetchPhotos()
            } else {
                flickrFetchr.searchPhotos(searchTerm)
            }
        }
    }

    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app, query)
        mutableSearchTerm.value = query
    }
}