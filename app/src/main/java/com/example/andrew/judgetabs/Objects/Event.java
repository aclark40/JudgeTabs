package com.example.andrew.judgetabs.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 6/24/15.
 */
public class Event implements Parcelable {

    private int id;
    private String name;
    private String location;
    private String start_time;
    private String end_time;
    private int[] organizer_ids;
    private int[] category_ids;
    private int[] judge_ids;
    private int[] team_ids;

    public Event(int id, String name, String location, String start_time, String end_time,
                 int[] organizer_ids, int[] category_ids, int[] judge_ids, int[] team_ids) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.start_time = start_time;
        this.end_time = end_time;
        this.organizer_ids = organizer_ids;
        this.category_ids = category_ids;
        this.judge_ids = judge_ids;
        this.team_ids = team_ids;
    }

    protected Event(Parcel in) {
        id = in.readInt();
        name = in.readString();
        location = in.readString();
        start_time = in.readString();
        end_time = in.readString();
        organizer_ids = in.createIntArray();
        category_ids = in.createIntArray();
        judge_ids = in.createIntArray();
        team_ids = in.createIntArray();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public int[] getOrganizer_ids() {
        return organizer_ids;
    }

    public int[] getCategory_ids() {
        return category_ids;
    }

    public int[] getJudge_ids() {
        return judge_ids;
    }

    public int[] getTeam_ids() {
        return team_ids;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(start_time);
        dest.writeString(end_time);
        dest.writeIntArray(organizer_ids);
        dest.writeIntArray(category_ids);
        dest.writeIntArray(judge_ids);
        dest.writeIntArray(team_ids);
    }

    @SuppressWarnings("unused")
    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
