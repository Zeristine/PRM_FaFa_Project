package day01.huy.imagechoosing.Api;

public class ApiController {
    public static String base_url1 = "https://api.sightengine.com";

    public static CheckImageClient checkImage() {
        return RetrofitClient.getClient(base_url1).create(CheckImageClient.class);
    }

}
