package alxr.ru.demophotos;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

interface Photos {

    @GET("/api/photo")
    Call<List<Image>> list();

    @GET("/api/photo")
    Observable<List<Image>> observableList();

}