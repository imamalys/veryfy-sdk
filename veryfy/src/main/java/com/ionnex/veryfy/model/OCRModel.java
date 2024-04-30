package com.ionnex.veryfy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OCRModel implements Parcelable {

    @SerializedName("label")
    @Expose
    private LabelModel label;
    @SerializedName("value")
    @Expose
    private String value;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable( this.label, flags );
        dest.writeString( this.value );
    }

    public void readFromParcel(Parcel source) {
        this.label = source.readParcelable( LabelModel.class.getClassLoader() );
        this.value = source.readString();
    }

    public OCRModel() {
    }

    protected OCRModel(Parcel in) {
        this.label = in.readParcelable( LabelModel.class.getClassLoader() );
        this.value = in.readString();
    }

    public static final Parcelable.Creator<OCRModel> CREATOR = new Parcelable.Creator<OCRModel>() {
        @Override
        public OCRModel createFromParcel(Parcel source) {
            return new OCRModel( source );
        }

        @Override
        public OCRModel[] newArray(int size) {
            return new OCRModel[size];
        }
    };

    public LabelModel getLabel() {
        return label;
    }

    public void setLabel(LabelModel label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
