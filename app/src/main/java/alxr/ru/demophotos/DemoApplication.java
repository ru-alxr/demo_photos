package alxr.ru.demophotos;

import android.app.Application;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.io.File;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DemoApplication extends Application {

    private static final String TAG = "DEMO_APP";
    private Picasso cachedPicasso;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        final int cacheSize = maxMemory / 8;
        cachedPicasso = new Picasso.Builder(this)
                .memoryCache(new LruCache(cacheSize))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public synchronized Picasso getCachedPicasso() {
        return cachedPicasso;
    }

    public void cache(Image image){
        cachedPicasso.load(image.getPhoto()).stableKey(image.getStableKey()).fetch();
    }

    @NonNull
    public Retrofit getRetrofit() {
        return retrofit;
    }

    public File getStoreDir() {
        boolean isSdPresent = isSdPresent();
        Log.d(TAG, "isSdPresent =" + isSdPresent );
        if (isSdPresent) {
            return Environment.getExternalStorageDirectory();
        } else {
            return getFilesDir();
        }
    }

    public static boolean isSdPresent() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

}