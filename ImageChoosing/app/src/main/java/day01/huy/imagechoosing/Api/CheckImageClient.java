package day01.huy.imagechoosing.Api;

import day01.huy.imagechoosing.Models.Result;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CheckImageClient {
    @Multipart
    @POST("/1.0/check.json")
        Call<Result> upload(
                @Part("api_user")RequestBody api_user,
                @Part("api_secret")RequestBody api_secret,
                @Part("models")RequestBody model,
                @Part MultipartBody.Part img
                );

}
