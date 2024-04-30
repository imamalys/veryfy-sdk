package com.ionnex.veryfy.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JourneyIdModel implements Parcelable {

    @SerializedName("sessionId")
    @Expose
    private int sessionId;

    @SerializedName("referenceId")
    @Expose
    private String referenceId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt( this.sessionId );
        dest.writeString( this.referenceId );
    }

    public void readFromParcel(Parcel source) {
        this.sessionId = source.readInt();
        this.referenceId = source.readString();
    }

    public JourneyIdModel() {
    }

    protected JourneyIdModel(Parcel in) {
        this.sessionId = in.readInt();
        this.referenceId = in.readString();
    }

    public static final Creator<JourneyIdModel> CREATOR = new Creator<JourneyIdModel>() {
        @Override
        public JourneyIdModel createFromParcel(Parcel source) {
            return new JourneyIdModel( source );
        }

        @Override
        public JourneyIdModel[] newArray(int size) {
            return new JourneyIdModel[size];
        }
    };

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
