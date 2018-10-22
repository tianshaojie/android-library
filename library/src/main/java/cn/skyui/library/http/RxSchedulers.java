package cn.skyui.library.http;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tianshaojie on 2017/11/8.
 */

public class RxSchedulers {

    public static <T> ObservableTransformer<T, T> io2main() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
