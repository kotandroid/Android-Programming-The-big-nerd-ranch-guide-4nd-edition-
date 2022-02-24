# Android-Programming-The-big-nerd-ranch-guide_4nd-edition
## Chapter 25, Looper, Handler, HandlerThread
>PhotoGallery에서 사진을 동적으로 내려받아 보여주기 위해 Looper, Handler, HandlerThread를 사용하는 방법을 알아본다. 
>
>또한 앱의 main 스레드와 백그라운드 스레드가 할 수 있는 일과 main 스레드와 백그라운드 스레드 간의 소통 방법도 알아본다.

### PhotoGallery의 네트워크 작업 과정
> PhotoGalleryViewModel에서 FlickrFetchr().fetchPhotos()를 호출해서 플리커로부터 JSON 데이터를 내려받는다. 
> 
> 이때 FlickrFetchr는 빈 LiveData<List<GalleryItem>> 객체를 즉시 반환하고 플리커로부터 데이터를 가져오기 위해 비동기 Retrofit 요청을 하며, 이 네트워크 요청은 백그라운드 스레드에서 실행된다.
  
> 이 과정이 끝나면 FlickrFetchr가 GalleryItem이 저장된 List로 파싱해서 LiveData 객체로 전달한다. 이로써 GalleryItem에 URL을 갖을 수 있게 된다. 

### 섬네일 사진을 보여주는 방법
> 전용 백그라운드 스레드를 생성한다.
>  
> 이 스레드는 한번에 하나의 내려받기 요청을 받아 처리하며, 내려받기가 끝나면 각 요청의 결과 이미지를 제공한다. 
> 
>이때 모든 요청이 해당 백그라운드 스레드에서 관리되므로, 클린업이나 스레드 중단을 쉽게 할 수 있다는 이점이 있다.


### main 스레드와 ThumbnailDownloader 스레드의 소통 과정
  > 1. main스레드의 messageQueue에서 요청 - onBindViewHoler()에서 이미지 요청
  2. '이 이미지를 내려받아라' - queueThumbnail() 호출 
  3. requestHandler가 ThumbnailDownloader 스레드의 루퍼에게 전달 -> 루퍼는 메시지 큐에 적재 
  4. 메시지의 순서가 되면 루퍼가 메시지 큐에 있는 메시지를 requestHandler에게 요청 
  5. handleMessage()가 호출 
  6. '요청했던 이미지가 여기 있다' - handleRequest()가 호출되면서 bitmap을 반환하고 responseHandler.post() 함수를 호출
  7. responseHandler()가 post() 실행 - post 함수는 Runnable 객체를 메시지 큐에 메시지를 적재
  8. 루퍼가 responseHandler에게 메시지 전달하면서 run() 호출 
  
