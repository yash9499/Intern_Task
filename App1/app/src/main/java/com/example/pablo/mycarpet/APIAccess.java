package com.example.pablo.mycarpet;


import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by pablo on 3/5/18.
 */

public class APIAccess {

    private static final String Url = "http://www.androidbegin.com/tutorial/";
    public static APIService apiService = null;

    public static APIService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Url)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
                            //build();
            apiService = retrofit.create(APIService.class);
        }
        return apiService;
    }

    public interface APIService {
        @GET("jsonparsetutorial.txt")
       // Call<CountryPopulationRank> getCountryList();
        Observable<CountryPopulationRank>getCountryList();

    }


}
