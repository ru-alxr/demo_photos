package alxr.ru.demophotos;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DemoApplication extends Application {

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

    public void cache(Image image) {
        cachedPicasso.load(image.getPhoto()).stableKey(image.getStableKey()).fetch();
    }

    @NonNull
    public Retrofit getRetrofit() {
        return retrofit;
    }

}