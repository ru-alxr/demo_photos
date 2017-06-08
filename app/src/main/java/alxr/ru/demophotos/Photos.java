package alxr.ru.demophotos;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

interface Photos {

    @GET("/api/photo")
    Observable<List<Image>> observableList();

}