package com.sahni.rahul.ieee_niec.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.sahni.rahul.ieee_niec.networking.NetworkingUtils.BASE_URL;

/**
 * Created by sahni on 27-Aug-17.
 */

public class RetrofitClient {

    private static Retrofit retrofit;

    public static Retrofit getInstance(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }
}
