package day01.huy.imagechoosing.CelebNameAPI;

public class FetchCelebResponse {

    private String name;
    private String des;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public FetchCelebResponse(String name, String des, String imgUrl) {
        this.name = name;
        this.des = des;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
