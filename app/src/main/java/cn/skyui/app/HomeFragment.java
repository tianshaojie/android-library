package cn.skyui.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import cn.skyui.library.base.fragment.BaseFragment;
import cn.skyui.library.utils.DeviceUtils;

public class HomeFragment extends BaseFragment {

    public static Fragment newInstance(String title) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = view.findViewById(R.id.btn_login);
        Button demo = view.findViewById(R.id.btn_demo);

        view.findViewById(R.id.btn_thread).setOnClickListener(v -> {
//            Intent intent = new Intent();
//            intent.setClass(getActivity(), ThreadLeakActivity.class);
//            getActivity().startActivity(intent);
        });

        view.findViewById(R.id.btn_handler).setOnClickListener(v -> {
//            Intent intent = new Intent();
//            intent.setClass(getActivity(), HandlerLeakActivity.class);
//            getActivity().startActivity(intent);
        });

        view.findViewById(R.id.btn_praise).setOnClickListener(v -> {
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), PraiseActivity.class);
//                getActivity().startActivity(intent);
        });

        demo.setOnClickListener(v -> {
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), DemoActivity.class);
//                getActivity().startActivity(intent);
        });

        Button btnTestRetry = view.findViewById(R.id.btn_test);
        btnTestRetry.setOnClickListener(view1 -> {
            Logger.i("test retry");
//            RetrofitFactory.createService(ApiService.class).testRetry()
//                    .compose(bindToLifecycle())
//                    .compose(RxSchedulers.io2main())
//                    .retryWhen(new RetryWhenException(3, 1000))
//                    .subscribe(new HttpObserver(getActivity()) {
//                        @Override
//                        protected void onSuccess(Object response) {
//                            Logger.i(response.toString());
//                        }
//                    });
        });

        String mobile = "13521468436";
        final Map<String, String> param = new HashMap<>();
        param.put("mobile", "13521468436");
        param.put("verifyCode", "536885");
        param.put("terminalType", "1");
        param.put("osVersion", "7.0.1");
        param.put("brand", "xiaomi");
        param.put("model", DeviceUtils.getModel());
        param.put("deviceId", DeviceUtils.getAndroidID());

        button.setOnClickListener(view1 -> {
//            RetrofitFactory.createService(ApiService.class).sendSms(mobile)
//                    .compose(bindUntilEvent(FragmentEvent.DESTROY)) // Bind Lifecycle 防止内存泄漏
//                    .flatMap(httpResponse -> RetrofitFactory.createService(ApiService.class).loginMapParam(param))
//                    .compose(RxSchedulers.io2main())
//                    .retryWhen(new RetryWhenException(2, 1000, 2000))
//                    .subscribe(new HttpObserver<User>(getActivity()) {
//                        @Override
//                        protected void onSuccess(User response) {
//                            Logger.i(response.toString());
//                            ToastUtils.showShort(response.toString());
//                        }
//                    });

            // 使用BaseObserver简化subscribe
//            apiService.sendSms(mobile)
//                    .flatMap(new Function<HttpResponse, ObservableSource<HttpResponse<User>>>() {
//                        @Override
//                        public ObservableSource<HttpResponse<User>> apply(@NonNull HttpResponse httpResponse) {
//                            return apiService.loginMapParam(param);
//                        }
//                    })
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new HttpObserver<User>(getActivity()) {
//                        @Override
//                        protected void onSuccess(User response) {
//                            Logger.i(response.toString());
//                        }
//                    });

            // subscribe原始Observer实现，比较繁琐
//            apiService.sendSms(mobile)
//                    .flatMap(new Function<HttpResponse, ObservableSource<HttpResponse<User>>>() {
//                        @Override
//                        public ObservableSource<HttpResponse<User>> apply(@NonNull HttpResponse httpResponse) {
//                            return apiService.loginMapParam(param);
//                        }
//                    })
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<HttpResponse<User>>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            Logger.i("onSubscribe");
//                        }
//
//                        @Override
//                        public void onNext(HttpResponse<User> value) {
//                            if(value.isSuccess()) {
//                                Logger.i(value.getBody().toString());
//                            } else {
//                                ToastUtils.showShort(value.getMsg());
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Logger.i(e.getMessage());
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            Logger.i("onComplete");
//                        }
//                    });

            // 原始多参数，传递麻烦，可以转化为map
//            RetrofitFactory.getService().login("13521468436", "536885", 2, "7.0.1", "xiaomi", DeviceUtils.getModel(), DeviceUtils.getAndroidID())
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(Schedulers.io())
//                    .subscribe(new Observer<HttpResponse<User>>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            Logger.i("onSubscribe");
//                        }
//
//                        @Override
//                        public void onNext(HttpResponse<User> value) {
//                            Logger.i(value.toString());
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Logger.i(e.getMessage());
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            Logger.i("onComplete");
//                        }
//                    });
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.i("fragment.isDetached=" + isDetached());
        Logger.i("getActivity.isFinishing=" + getActivity().isFinishing());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("fragment.isDetached=" + isDetached());
        Logger.i("getActivity.isFinishing=" + getActivity().isFinishing());
    }
}