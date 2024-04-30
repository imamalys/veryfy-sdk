package com.ionnex.veryfy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FRModel implements Parcelable {
    private ImageBestLivenessModel imageBestLiveness;
    private Double score;

    protected FRModel(Parcel in) {
        imageBestLiveness = in.readParcelable(ImageBestLivenessModel.class.getClassLoader());
        if (in.readByte() == 0) {
            score = null;
        } else {
            score = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(imageBestLiveness, flags);
        if (score == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(score);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FRModel> CREATOR = new Creator<FRModel>() {
        @Override
        public FRModel createFromParcel(Parcel in) {
            return new FRModel(in);
        }

        @Override
        public FRModel[] newArray(int size) {
            return new FRModel[size];
        }
    };

    public ImageBestLivenessModel getImageBestLiveness() {
        return imageBestLiveness;
    }

    public void setImageBestLiveness(ImageBestLivenessModel imageBestLiveness) {
        this.imageBestLiveness = imageBestLiveness;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}