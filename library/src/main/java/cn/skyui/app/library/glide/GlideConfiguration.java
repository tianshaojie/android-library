package cn.skyui.app.library.glide;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class GlideConfiguration extends AppGlideModule {

    // 图片缓存最大容量，150M
    public static final int GLIDE_CATCH_SIZE = 150 * 1000 * 1000;

    // 图片缓存子目录
    public static final String GLIDE_CARCH_DIR = "ImageCache";

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //自定义缓存目录，磁盘缓存给150M 另外一种设置缓存方式
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, GLIDE_CARCH_DIR, GLIDE_CATCH_SIZE));
    }

    /**
     * 禁止解析Manifest文件
     * 主要针对V3升级到v4的用户，可以提升初始化速度，避免一些潜在错误
     * @return
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}