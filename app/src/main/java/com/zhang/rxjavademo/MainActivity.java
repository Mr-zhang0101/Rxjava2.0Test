package com.zhang.rxjavademo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN";
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.btn4)
    Button btn4;
    @BindView(R.id.btn5)
    Button btn5;
    @BindView(R.id.btn6)
    Button btn6;
    @BindView(R.id.btn7)
    Button btn7;
    @BindView(R.id.btn8)
    Button btn8;
    @BindView(R.id.tv)
    TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        Log.d(TAG, "subscribe: emit_1");
                        e.onNext("事件1");
                        Log.d(TAG, "subscribe: emit_2");
                        e.onNext("事件2");
                        Log.d(TAG, "subscribe: emit_3");
                        e.onNext("事件3");
                        Log.d(TAG, "subscribe: emit_onComplete");
                        e.onComplete();
                        Log.d(TAG, "subscribe: emit_4");
                        e.onNext("事件4");
                    }
                });
                Observer<String> observer =new Observer<String>() {
                    private Disposable mDisposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(String value) {
                        Log.d(TAG, "onNext: 接收"+value);
                        if (value.equals("事件1")){
                            Log.d(TAG, "dispose");
                            mDisposable.dispose();
                            Log.d(TAG, "isDisposed : " + mDisposable.isDisposed());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: "+e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                };
                observable.subscribe(observer);
                break;
            case R.id.btn2:
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        Log.d(TAG, "subscribe: emit_1");
                        e.onNext("事件1");
                        Log.d(TAG, "subscribe: emit_2");
                        e.onNext("事件2");
                        Log.d(TAG, "subscribe: emit_3");
                        e.onNext("事件3");
                        Log.d(TAG, "subscribe: emit_onComplete");
                        e.onComplete();
                        Log.d(TAG, "subscribe: emit_4");
                        e.onNext("事件4");
                    }
                }).subscribe(new Consumer<String>() {

                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: "+s);
                    }
                });

                break;
            case R.id.btn3:
                Observable.just("事件1","事件2","事件3","事件4").subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: "+s);
                    }
                });
                break;
            case R.id.btn4:
                Observable.interval(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: "+aLong);
                    }
                });

                break;
            case R.id.btn5:
                Observable.range(10, 5).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: "+integer);
                    }
                });
                break;
            case R.id.btn6:
                Observable.just("repeat").repeat(3).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: "+s);
                    }
                });
                break;
            case R.id.btn7:

                break;
            case R.id.btn8:
                startActivity(new Intent(this,ThreadAvtivity.class));
                break;
        }
    }
}
