package day01.huy.imagechoosing.Models;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Face {

    @SerializedName("x1")
    @Expose
    private Double x1;
    @SerializedName("y1")
    @Expose
    private Double y1;
    @SerializedName("x2")
    @Expose
    private Double x2;
    @SerializedName("y2")
    @Expose
    private Double y2;
    @SerializedName("features")
    @Expose
    private Features features;
    @SerializedName("celebrity")
    @Expose
    private List<Celebrity> celebrity = null;

    public Double getX1() {
        return x1;
    }

    public void setX1(Double x1) {
        this.x1 = x1;
    }

    public Double getY1() {
        return y1;
    }

    public void setY1(Double y1) {
        this.y1 = y1;
    }

    public Double getX2() {
        return x2;
    }

    public void setX2(Double x2) {
        this.x2 = x2;
    }

    public Double getY2() {
        return y2;
    }

    public void setY2(Double y2) {
        this.y2 = y2;
    }

    public Features getFeatures() {
        return features;
    }

    public void setFeatures(Features features) {
        this.features = features;
    }

    public List<Celebrity> getCelebrity() {
        return celebrity;
    }

    public void setCelebrity(List<Celebrity> celebrity) {
        this.celebrity = celebrity;
    }

}