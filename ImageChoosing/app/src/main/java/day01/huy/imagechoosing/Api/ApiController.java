package day01.huy.imagechoosing.Api;

import retrofit2.Retrofit;

public class ApiController {
    public static String base_url1 = "https://api.sightengine.com";
    public static String base_url2 = "";

    public static CheckImageClient checkImage(){
        return RetrofitClient.getClient(base_url1).create(CheckImageClient.class);
    }

    public static CheckNameClient checkName(){
        return RetrofitClient.getClient(base_url2).create(CheckNameClient.class);
    }
}
