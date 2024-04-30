package com.ionnex.veryfy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LabelModel implements Parcelable {

    @SerializedName("value")
    @Expose
    private int value;

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("description")
    @Expose
    private String description;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt( this.value );
        dest.writeString( this.key );
        dest.writeString( this.description );
    }

    public void readFromParcel(Parcel source) {
        this.value = source.readInt();
        this.key = source.readString();
        this.description = source.readString();
    }

    public LabelModel() {
    }

    protected LabelModel(Parcel in) {
        this.value = in.readInt();
        this.key = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<LabelModel> CREATOR = new Parcelable.Creator<LabelModel>() {
        @Override
        public LabelModel createFromParcel(Parcel source) {
            return new LabelModel( source );
        }

        @Override
        public LabelModel[] newArray(int size) {
            return new LabelModel[size];
        }
    };

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
