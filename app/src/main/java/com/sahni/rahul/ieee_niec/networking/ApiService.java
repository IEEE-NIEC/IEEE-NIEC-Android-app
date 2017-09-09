package com.sahni.rahul.ieee_niec.networking;

import com.sahni.rahul.ieee_niec.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by sahni on 27-Aug-17.
 */

public interface ApiService {

    @GET("events/get_all_events.php")
    Call<EventsResponse> getEvents();

    @POST("post_people_details.php")
    Call<PostUserDetailsResponse> postUserDetails(@Body User user);

//    Call
}
