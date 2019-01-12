package cn.skyui.library.data.constant;

public class ImageConstants {

    private static final String DOMAIN_PREFIX_HTTPS = "https://astatic.oss";
    private static final String DOMAIN_PREFIX_HTTP = "http://astatic.oss";
    private static final String SMALL_200_IMAGE_SUFFIX = "/s.jpg";
    private static final String MEDIUM_400_IMAGE_SUFFIX = "/m.jpg";
    private static final String MEDIUM_800_IMAGE_SUFFIX = "/c.jpg";
    private static final String LARGE_1600_IMAGE_SUFFIX = "/l.jpg";
    private static final String LARGE_2048_IMAGE_SUFFIX = "/h.jpg";
    private static final String ORIGINAL_IMAGE_SUFFIX = "/o.jpg";

    private static boolean isOssUrl(String origin) {
        return origin.startsWith(DOMAIN_PREFIX_HTTPS) || origin.startsWith(DOMAIN_PREFIX_HTTP);
    }
    
    public static String getSmallUrl(String origin) {
        if (origin != null && isOssUrl(origin)) {
            return origin + SMALL_200_IMAGE_SUFFIX;
        }
        return origin;
    }

    public static String getMediumUrl(String origin) {
        if (origin != null && isOssUrl(origin)) {
            return origin + MEDIUM_400_IMAGE_SUFFIX;
        }
        return origin;
    }

    public static String getMedium800Url(String origin) {
        if (origin != null && isOssUrl(origin)) {
            return origin + MEDIUM_800_IMAGE_SUFFIX;
        }
        return origin;
    }

    public static String getLargeUrl(String origin) {
        if (origin != null && isOssUrl(origin)) {
            return origin + LARGE_1600_IMAGE_SUFFIX;
        }
        return origin;
    }

    public static String getHdUrl(String origin) {
        if (origin != null && isOssUrl(origin)) {
            return origin + LARGE_2048_IMAGE_SUFFIX;
        }
        return origin;
    }

    public static String getOriginUrl(String origin) {
        if (origin != null && isOssUrl(origin)) {
            return origin + ORIGINAL_IMAGE_SUFFIX;
        }
        return origin;
    }
}
