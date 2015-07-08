package com.example.andrew.judgetabs.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 6/24/15.
 */
public class Category implements Parcelable {

    private int id;
    private int event;
    private String label;
    private String description;
    private int color;
    private String due_at;
    private Team[] teams;

    public Category(int id, int event, String label, String description, int color, String due_at,
                    Team[] teams) {
        this.id = id;
        this.event = event;
        this.label = label;
        this.description = description;
        this.color = color;
        this.due_at = due_at;
        this.teams = teams;
    }

    protected Category(Parcel in) {
        id = in.readInt();
        event = in.readInt();
        label = in.readString();
        description = in.readString();
        color = in.readInt();
        due_at = in.readString();
        teams = (Team[]) in.readParcelableArray(Team.class.getClassLoader());
    }

    public int getId() {
        return id;
    }

    public int getEvent() {
        return event;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public int getColor() {
        return color;
    }

    public String getDue_at() {
        return due_at;
    }

    public Team[] getTeams() {
        return this.teams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(event);
        dest.writeString(label);
        dest.writeString(description);
        dest.writeInt(color);
        dest.writeString(due_at);
        dest.writeArray(teams);
    }

    @SuppressWarnings("unused")
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
