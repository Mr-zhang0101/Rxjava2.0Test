package com.zhang.rxjavademo.api;

import com.zhang.rxjavademo.api.bean.PhotoInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

import static com.zhang.rxjavademo.api.RetrofitService.CACHE_CONTROL_NETWORK;

/**
 * Project: RxjavaDemo
 * Author:  张佳林
 * Version:  1.0
 * Date:    2017/7/10
 * Modify:  //TODO
 * Description: //TODO
 * Copyright notice:
 */

public interface Api {
    //http://c.3g.163.com/photo/api/list/0096/4GJ60096.json

    @Headers(CACHE_CONTROL_NETWORK)
    @GET("photo/api/list/0096/4GJ60096.json")
    Observable<List<PhotoInfo>> getPhotoList();
}
