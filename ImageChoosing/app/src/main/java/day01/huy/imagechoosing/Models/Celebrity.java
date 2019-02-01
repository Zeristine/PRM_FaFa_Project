package day01.huy.imagechoosing.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Celebrity {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("prob")
    @Expose
    private Double prob;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getProb() {
        return prob;
    }

    public void setProb(Double prob) {
        this.prob = prob;
    }

}
