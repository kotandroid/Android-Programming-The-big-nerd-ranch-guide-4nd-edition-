package com.bignerdranch.android.photogallery

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PhotoGalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, PhotoGalleryFragment.newInstance())
                .commit()
        }
    }

    // Notification 객체를 생성하면 시스템 서비스인 NotificationManager의 notify을 호출하여 알림을 게시할 수 있다.
    // PhotoGalleryActivity를 시작시키는 Intent 인스턴스를 반환한다. 궁극적으로는 PollWorker가 PhotoGalleryActivity.newIntent()를 호출하고
    // 결과로 반환된 인텐트를 Pending Intent에 포함한 후 이 PendingIntent를 알림에 설정할 것이다.
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, PhotoGalleryActivity::class.java)
        }
    }
}