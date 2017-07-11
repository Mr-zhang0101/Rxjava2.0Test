package com.zhang.rxjavademo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.rxjavademo.api.RetrofitService;
import com.zhang.rxjavademo.api.bean.PhotoInfo;
import com.zhang.rxjavademo.api.bean.User;
import com.zhang.rxjavademo.greendao.gen.DaoMaster;
import com.zhang.rxjavademo.greendao.gen.DaoSession;
import com.zhang.rxjavademo.greendao.gen.UserDao;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ThreadAvtivity extends AppCompatActivity {

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
    @BindView(R.id.tv)
    TextView mTv;
    private String TAG = "THREAD";
    private UserDao mUserDao;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_avtivity);
        ButterKnife.bind(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        Log.d(TAG, "Observable thread : " + Thread.currentThread().getName());
                        Log.d(TAG, "subscribe: emit_1");
                        e.onNext("事件1");
                    }
                }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "Observer thread :" + Thread.currentThread().getName());
                        Log.d(TAG, "onNext: " + s);
                    }
                });
                break;
            case R.id.btn2:
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        Log.d(TAG, "Observable thread : " + Thread.currentThread().getName());
                        e.onNext("事件1");
                        e.onNext("事件2");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "Observer thread :" + Thread.currentThread().getName());
                        Log.d(TAG, "onNext: " + s);
                    }
                });

                break;
            case R.id.btn3:
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        Log.d(TAG, "Observable thread : " + Thread.currentThread().getName());
                        e.onNext("事件1");
                        e.onNext("事件2");
                    }
                })
                        .subscribeOn(Schedulers.newThread())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Log.d(TAG, "Observer thread :" + Thread.currentThread().getName());
                                Log.d(TAG, "onNext: " + s);
                            }
                        });
                break;
            case R.id.btn4:
                RetrofitService.getPhotoList()
                        .subscribe(new Observer<List<PhotoInfo>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mCompositeDisposable.add(d);
                            }

                            @Override
                            public void onNext(List<PhotoInfo> value) {
                                Toast.makeText(ThreadAvtivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(ThreadAvtivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            case R.id.btn5:
                DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(),"user.db",null);
                mDaoMaster = new DaoMaster(helper.getWritableDatabase());
                mDaoSession = mDaoMaster.newSession();
                mUserDao = mDaoSession.getUserDao();
                final List<User> users = Arrays.asList(new User(null,"user_1","001"),
                        new User(null,"user_2","002"),
                        new User(null,"user_3","003"),
                        new User(null,"user_4","004"),
                        new User(null,"user_5","005"),
                        new User(null,"user_6","006"),
                        new User(null,"user_7","007"),
                        new User(null,"user_8","008"),
                        new User(null,"user_9","009"));
                mUserDao.getSession().runInTx(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < users.size(); i++) {
                            User user = users.get(i);
                            mUserDao.insertOrReplace(user);
                        }
                    }
                });
                Observable.create(new ObservableOnSubscribe<List<User>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<User>> e) throws Exception {
                        List<User> userList = mUserDao.queryBuilder()
                                //限定查询的条件
                                .where(UserDao.Properties.Id.notEq(10))
                                //设置按照排序方式
                                .orderAsc(UserDao.Properties.Id)
                                //限制查询个数
                                .limit(9)
                                .build().list();
                        e.onNext(userList);
                        e.onComplete();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<User> value) {
                        Log.d(TAG, "onNext: "+value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
                break;
            case R.id.btn6:
                break;
            case R.id.btn7:
                startActivity(new Intent(ThreadAvtivity.this,HandlerActivity.class));
                break;
        }
    }
    CompositeDisposable mCompositeDisposable ;

    /**
     * Disposable停止接收事件,相当于遍历集合disposable.dispose();
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable!=null)
        mCompositeDisposable.clear();
    }


}
