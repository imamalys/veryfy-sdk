package com.ionnex.veryfy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageBestLivenessModel implements Parcelable {
    private Double probability;
    private Double score;
    private Double quality;

    protected ImageBestLivenessModel(Parcel in) {
        if (in.readByte() == 0) {
            probability = null;
        } else {
            probability = in.readDouble();
        }
        if (in.readByte() == 0) {
            score = null;
        } else {
            score = in.readDouble();
        }
        if (in.readByte() == 0) {
            quality = null;
        } else {
            quality = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (probability == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(probability);
        }
        if (score == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(score);
        }
        if (quality == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(quality);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageBestLivenessModel> CREATOR = new Creator<ImageBestLivenessModel>() {
        @Override
        public ImageBestLivenessModel createFromParcel(Parcel in) {
            return new ImageBestLivenessModel(in);
        }

        @Override
        public ImageBestLivenessModel[] newArray(int size) {
            return new ImageBestLivenessModel[size];
        }
    };

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getQuality() {
        return quality;
    }

    public void setQuality(Double quality) {
        this.quality = quality;
    }
}