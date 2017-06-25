package com.pangge.parsecitycode;

import android.content.Context;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by iuuu on 17/6/25.
 */

public class ParseCity {
    public static final String BASE_URL = "http://mobile.weather.com.cn/";

    private CompositeDisposable mCompositeDisposable;

    private List<Code> list;

    public ParseCity() {


        parseXml();
    }
    private void parseXml(){
        mCompositeDisposable = new CompositeDisposable();


        RequestInterface requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())

                .build().create(RequestInterface.class);

        mCompositeDisposable.add(requestInterface.getCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<XMLCity>(){
                    @Override
                    public void onComplete() {
                        Log.i("con","ssuccessful");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(e.toString(),"error");
                    }

                    @Override
                    public void onNext(XMLCity value) {
                        list = value.cities;
                        String d1 = list.get(1).d1;
                        Log.i("hello--", "list.size=" + list.size());
                        Log.i("hello--d1", d1);


                        if (list.size() == 0) {
                            return;
                        }
                    }
                }));

        mCompositeDisposable.clear();


    }
}
