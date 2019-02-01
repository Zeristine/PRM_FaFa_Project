package day01.huy.imagechoosing.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Features {

    @SerializedName("left_eye")
    @Expose
    private LeftEye leftEye;
    @SerializedName("right_eye")
    @Expose
    private RightEye rightEye;
    @SerializedName("nose_tip")
    @Expose
    private NoseTip noseTip;
    @SerializedName("left_mouth_corner")
    @Expose
    private LeftMouthCorner leftMouthCorner;
    @SerializedName("right_mouth_corner")
    @Expose
    private RightMouthCorner rightMouthCorner;

    public LeftEye getLeftEye() {
        return leftEye;
    }

    public void setLeftEye(LeftEye leftEye) {
        this.leftEye = leftEye;
    }

    public RightEye getRightEye() {
        return rightEye;
    }

    public void setRightEye(RightEye rightEye) {
        this.rightEye = rightEye;
    }

    public NoseTip getNoseTip() {
        return noseTip;
    }

    public void setNoseTip(NoseTip noseTip) {
        this.noseTip = noseTip;
    }

    public LeftMouthCorner getLeftMouthCorner() {
        return leftMouthCorner;
    }

    public void setLeftMouthCorner(LeftMouthCorner leftMouthCorner) {
        this.leftMouthCorner = leftMouthCorner;
    }

    public RightMouthCorner getRightMouthCorner() {
        return rightMouthCorner;
    }

    public void setRightMouthCorner(RightMouthCorner rightMouthCorner) {
        this.rightMouthCorner = rightMouthCorner;
    }

}