package com.david.trackshere.utils;

//import com.mapbox.mapboxsdk.constants.MapboxConstants;
//import com.squareup.okhttp.Cache;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.OkUrlFactory;

/**
 * Created by davidhodge on 12/23/14.
 */
public class NetowrkUtils {

//    public static boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivityManager =
//                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
//
//    public static HttpURLConnection getHttpURLConnection(final URL url) {
//        return getHttpURLConnection(url, null, null);
//    }
//
//    public static HttpURLConnection getHttpURLConnection(final URL url, final Cache cache) {
//        return getHttpURLConnection(url, cache, null);
//    }
//
//    public static HttpURLConnection getHttpURLConnection(final URL url, final Cache cache, final SSLSocketFactory sslSocketFactory) {
//        OkHttpClient client = new OkHttpClient();
//        if (cache != null) {
//            client.setCache(cache);
//        }
//        if (sslSocketFactory != null) {
//            client.setSslSocketFactory(sslSocketFactory);
//        }
//        HttpURLConnection connection = new OkUrlFactory(client).open(url);
//        connection.setRequestProperty("User-Agent", MapboxConstants.USER_AGENT);
//        return connection;
//    }
//
//    public static Cache getCache(final File cacheDir, final int maxSize) throws IOException {
//        return new Cache(cacheDir, maxSize);
//    }
}
