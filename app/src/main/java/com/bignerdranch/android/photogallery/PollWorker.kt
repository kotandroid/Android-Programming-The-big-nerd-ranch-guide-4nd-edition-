package com.bignerdranch.android.photogallery

import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 *
 */

private const val TAG = "PollWorker"

class PollWorker(val context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

    override fun doWork(): Result {
        val query = QueryPreferences.getStoredQuery(context)
        val lastResultId = QueryPreferences.getLastResultId(context)
        val items: List<GalleryItem> = if (query.isEmpty()) {
            FlickrFetchr().fetchPhotosRequest()
                .execute()
                .body()
                ?.photos
                ?.galleryItems
        } else {
            FlickrFetchr().searchPhotosRequest(query)
                .execute()
                .body()
                    ?.photos
                ?.galleryItems
        } ?: emptyList()

        // 최근 사진들을 가져온 후에는 첫 번째 사진의 ID가 공유 프리퍼런스에 저장된 사진(사용자가 최근에 본 사진) ID와 같은지 검사한다.
        // 일치하지 않으면 새로운 사진이 있다는 알림을 사용자에게 보여주고 새로 가져온 사진의 ID를 공유 프리퍼런스에 저장한다.
        if (items.isEmpty()) {
            return Result.success()
        }

        val resultId = items.first().id
        if (resultId == lastResultId) {
            Log.i(TAG, "Got an old result: $resultId")
        } else {
            Log.i(TAG, "Got a new result: $resultId")
            QueryPreferences.setLastResultId(context, resultId)

            val intent = PhotoGalleryActivity.newIntent(context)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            /**
             * NotificationCompat 클래스는 알림을 지원한다.
             * NotificationCompat.Builder는 생성자 인자로 채널 ID를 받으며,
             * 사용자가 안드로이드 8버전 이상 버전이 실행되는 장치를 사용 중이면 이 ID를 사용해서 알림의 채널을 생성한다.
             * 이전 버전의 장치일 떄는 Builder가 채널을 무시한다.
             * NOTIFICATION_CHANNEL_ID 이 상수는 PhotoGalleryApplication에 선언되어있다.
             */
            val resources = context.resources
            val notification = NotificationCompat
                .Builder(context, NOTIFICATION_CHANNEL_ID)
                .setTicker(resources.getString(R.string.new_pictures_title))    // 티커 텍스트
                .setSmallIcon(android.R.drawable.ic_menu_report_image)  // 작은 아이콘
                .setContentTitle(resources.getString(R.string.new_pictures_title))  // 제목
                .setContentText(resources.getString(R.string.new_pictures_text))    // 텍스트
                .setContentIntent(pendingIntent)    // PendingIntent 객체를 사용해서 사용자가 알림을 눌렀을 때 수행될 일을 지정한다.
                .setAutoCancel(true)    // 사용자가 알림을 눌렀을 때 알림 드로어에서 해당 알림이 삭제되도록 설정
                .build()

            val notificationManager = NotificationManagerCompat.from(context)   // 현재 컨텍스트의 NotificationManager 인스턴스를 얻는다.
            notificationManager.notify(0, notification) // 호출해서 알림 게시 notify(알림의 식별자,notification )
            // 알림 드로어에 남아있는 알림은 같은 ID 를 갖는 다른 알림으로 교체될 수 있다. 같은 알림ID가 없으면 새로 생성함

        }

        return Result.success()
    }
}