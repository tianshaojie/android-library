package cn.skyui.moudle.market.data;

import cn.skyui.library.http.HttpResponse;
import cn.skyui.moudle.market.data.model.SearchResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * 股票搜索
     * @param count 数量
     * @param keyword 关键字
     * @return 股票信息
     */
    @GET("quote/hksearch/m")
    Observable<HttpResponse<SearchResult>> search(@Query("text")String keyword, @Query("count")int count);
}
