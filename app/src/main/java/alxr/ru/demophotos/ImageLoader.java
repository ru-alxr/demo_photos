package alxr.ru.demophotos;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class ImageLoader extends AsyncTask<Void, Void, List<Image>> {

    private static final String URL = "https://photomaton.herokuapp.com/api/photo";

    @Override
    protected List<Image> doInBackground(Void... params) {
        OkHttpClient mOkHttpClient;
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        String raw;
        Request.Builder builder = new Request.Builder()
                .url(URL)
                .header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .header("Accept", "application/json");
        try {
            Response response = mOkHttpClient.newCall(builder.build()).execute();
            raw = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        JSONArray array;
        try {
            array = new JSONArray(raw);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        Gson gson = new Gson();
        List<Image> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++){
            JSONObject object;
            try {
                object = array.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            Image image = gson.fromJson(object.toString(), Image.class);
            list.add(image);
        }
        return list;
    }

}