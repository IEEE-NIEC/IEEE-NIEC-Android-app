package com.sahni.rahul.ieee_niec.networking;

import com.sahni.rahul.ieee_niec.models.User;
import com.sahni.rahul.ieee_niec.models.UserStatus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sahni on 27-Aug-17.
 */

public interface ApiService {

    @GET("events/get_all_events.php")
    Call<EventsResponse> getEvents();

    @POST("post_people_details.php")
    Call<PostUserDetailsResponse> postUserDetails(@Body User user);

    @GET("post_people_details.php")
    Call<UserStatus> checkUserStatus(@Query("id") String userId);

    @GET("search_user.php")
    Call<SearchResponse> searchForUsers(@Query("search") String query);
}
