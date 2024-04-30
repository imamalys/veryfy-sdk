package com.ionnex.veryfy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DocumentModel implements Parcelable {

    @SerializedName("documentType")
    @Expose
    private String documentType;


    @SerializedName("result")
    @Expose
    private ArrayList<OCRModel> result;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( this.documentType );
        dest.writeTypedList( this.result );
    }

    public void readFromParcel(Parcel source) {
        this.documentType = source.readString();
        this.result = source.createTypedArrayList( OCRModel.CREATOR );
    }

    public DocumentModel() {
    }

    protected DocumentModel(Parcel in) {
        this.documentType = in.readString();
        this.result = in.createTypedArrayList( OCRModel.CREATOR );
    }

    public static final Creator<DocumentModel> CREATOR = new Creator<DocumentModel>() {
        @Override
        public DocumentModel createFromParcel(Parcel source) {
            return new DocumentModel( source );
        }

        @Override
        public DocumentModel[] newArray(int size) {
            return new DocumentModel[size];
        }
    };

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public ArrayList<OCRModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<OCRModel> result) {
        this.result = result;
    }

}

