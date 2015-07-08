package com.example.andrew.judgetabs.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 6/24/15.
 */
public class Team implements Parcelable {

    private int id;
    private int event;
    private String name;
    private String logo;

    public Team(int id, int event, String name, String logo) {
        this.id = id;
        this.event = event;
        this.name = name;
        this.logo = logo;
    }

    protected Team(Parcel in) {
        id = in.readInt();
        event = in.readInt();
        name = in.readString();
        logo = in.readString();
    }

    public int getId() {
        return id;
    }

    public int getEvent() {
        return event;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(event);
        dest.writeString(name);
        dest.writeString(logo);
    }

    @SuppressWarnings("unused")
    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };
}
