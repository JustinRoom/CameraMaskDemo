package jsc.exam.com.cameramask.retrofit;

import io.reactivex.Observable;
import jsc.exam.com.cameramask.BuildConfig;
import retrofit2.http.GET;

public interface ApiService {

    @GET(BuildConfig.VERSION_URL)
    Observable<String> getVersionInfo();

}
