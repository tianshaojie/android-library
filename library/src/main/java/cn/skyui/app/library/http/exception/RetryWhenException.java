package cn.skyui.app.library.http.exception;

import com.orhanobut.logger.Logger;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import retrofit2.HttpException;

/**
 * Created by tianshaojie on 2017/11/10.
 */
public class RetryWhenException implements Function<Observable<? extends Throwable>, Observable<?>> {

    private int retryTimes = 2;             // 重试次数
    private long delayTime = 1000;          // 延时时间：毫秒
    private long increaseDelayTime = 1000;  // 叠加延迟

    public RetryWhenException() {}

    public RetryWhenException(int retryTimes, long delayTime) {
        this.retryTimes = retryTimes;
        this.delayTime = delayTime;
    }

    public RetryWhenException(int retryTimes, long delayTime, long increaseDelayTime) {
        this.retryTimes = retryTimes;
        this.delayTime = delayTime;
        this.increaseDelayTime = increaseDelayTime;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable
                .zipWith(Observable.range(1, retryTimes + 1), new BiFunction<Throwable, Integer, Wrapper>() {
                    @Override
                    public Wrapper apply(Throwable throwable, Integer integer) {
                        return new Wrapper(throwable, integer);
                    }
                }).flatMap(new Function<Wrapper, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Wrapper wrapper) throws Exception {
                        if ((wrapper.throwable instanceof ConnectException
                                || wrapper.throwable instanceof SocketTimeoutException
                                || wrapper.throwable instanceof TimeoutException
                                || wrapper.throwable instanceof HttpException)
                                && wrapper.index < retryTimes + 1) { //如果超出重试次数也抛出错误进入onError，否则默认是会进入onCompleted
                            Logger.e("retry---->" + wrapper.index);
                            return Observable.timer(delayTime + (wrapper.index - 1) * increaseDelayTime, TimeUnit.MILLISECONDS);
                        }
                        return Observable.error(wrapper.throwable);
                    }
                });
    }

    private class Wrapper {
        private int index;
        private Throwable throwable;

        Wrapper(Throwable throwable, int index) {
            this.index = index;
            this.throwable = throwable;
        }
    }

}
