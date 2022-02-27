package com.bignerdranch.android.photogallery

import android.content.Context
import android.preference.PreferenceManager


/**
 *  모든 다른 컴포넌트가 공유하므로 앱에서 하나의 인스턴스만 생성하면 된다.
 *
 */
private const val PREF_SEARCH_QUERY = "searchQuery"


object QueryPreferences {

    fun getStoredQuery(context: Context): String {
        // 기본 이름과 퍼미션을 갖는 기본 SharedPreferences 인스턴스를 반환한다.
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_SEARCH_QUERY, "")!!
    }

    fun setStoredQuery(context: Context, query: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_SEARCH_QUERY, query)
            .apply()
    }
}