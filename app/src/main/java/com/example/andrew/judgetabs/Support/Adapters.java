package com.example.andrew.judgetabs.Support;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.andrew.judgetabs.Objects.Category;
import com.example.andrew.judgetabs.Objects.Event;
import com.example.andrew.judgetabs.Objects.Team;
import com.example.andrew.judgetabs.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by root on 6/30/15.
 */
public class Adapters {

    /**
     * Defines the adapter used to display events
     */
    // TODO: Cleanup (duplicate in LoginActivity)
    public static class EventAdapter extends ArrayAdapter<Event> {

        private Context context;
        private Event[] eventsArr;

        public EventAdapter(Context context, Event[] eventsArr) {
            super(context, android.R.layout.simple_list_item_2, eventsArr);
            this.context = context;
            this.eventsArr = eventsArr;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = LayoutInflater.from(this.getContext()).inflate(
                        android.R.layout.simple_list_item_2, parent, false);
            }

            TextView firstText = (TextView) convertView.findViewById(android.R.id.text1);
            TextView secondText = (TextView) convertView.findViewById(android.R.id.text2);
            firstText.setText(getItem(position).getName());
            secondText.setText(getItem(position).getStart_time());
            return convertView;
        }

        @Override
        public Event getItem(int position) {
            return this.eventsArr[position];
        }
    }

    /**
     * Defines the adapter used to display teams
     */
    public static class TeamAdapter extends ArrayAdapter<Team> {

        private Context context;
        private Team[] teamsArr;

        public TeamAdapter(Context context, Team[] teamsArr) {
            super(context, android.R.layout.simple_list_item_1, teamsArr);
            this.context = context;
            this.teamsArr = teamsArr;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext()).inflate(
                        android.R.layout.simple_list_item_1, parent, false);
            }

            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(getItem(position).getName());
            return convertView;
        }

        @Override
        public Team getItem(int position) {
            return this.teamsArr[position];
        }
    }

    /**
     * Defines the adapter used to display categories
     */
    public static class CategoryAdapter extends ArrayAdapter<Category> {

        private Context context;
        private Category[] categoriesArr;

        public CategoryAdapter(Context context, Category[] categoriesArr) {
            super(context, android.R.layout.simple_list_item_1, categoriesArr);
            this.context = context;
            this.categoriesArr = categoriesArr;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = LayoutInflater.from(this.getContext()).inflate(
                        android.R.layout.simple_list_item_1, parent, false);
            }

            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(getItem(position).getLabel());
            return convertView;
        }

        @Override
        public Category getItem(int position) {
            return this.categoriesArr[position];
        }
    }

     public static class JSONAdapter extends BaseAdapter implements ListAdapter {

        private final Activity activity;
        private final JSONArray jsonArray;
        private JSONAdapter(Activity activity, JSONArray jsonArray) {
            assert activity != null;
            assert jsonArray != null;

            this.jsonArray = jsonArray;
            this.activity = activity;
        }


        @Override public int getCount() {

            return jsonArray.length();
        }

        @Override public JSONObject getItem(int position) {

            return jsonArray.optJSONObject(position);
        }

        @Override public long getItemId(int position) {
            JSONObject jsonObject = getItem(position);

            return jsonObject.optLong("id");
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = activity.getLayoutInflater().inflate(R.layout.recycler_view, null);

            JSONObject jsonObject = getItem(position);

            return convertView;
        }
    }

    public class CategoryAdapt extends RecyclerView.Adapter<CategoryAdapt.CategoryList> {

        private List<CatInfo> contactList;

        public CategoryAdapt(List<CatInfo> contactList) {
            this.contactList = contactList;
        }

        @Override
        public int getItemCount() {
            return contactList.size();
        }

        @Override
        public void onBindViewHolder(CategoryList contactViewHolder, int i) {
            CatInfo ci = contactList.get(i);
            contactViewHolder.tNum.setText(ci.tnum);        //implement JSON/GSON information here
            contactViewHolder.rNum.setText(ci.rnum);
            contactViewHolder.tLeft.setText(ci.tremaning);
        }

        @Override
        public CategoryList onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.card_item_cats, viewGroup, false);

            return new CategoryAdapt(itemView);
        }
    }

    public static class CategoryList extends RecyclerView.ViewHolder {
        protected TextView tNum;
        protected TextView rNum;
        protected TextView tLeft;

        public CategoryList(View v) {
            super(v);
            tNum =  (TextView) v.findViewById(R.id.teamsNumber);
            rNum = (TextView)  v.findViewById(R.id.rubricNumber);
            tLeft = (TextView)  v.findViewById(R.id.timeLeft);
        }
    }

    public class CatInfo {
        protected int teamNumber;
        protected int rubricNumber;
        protected int timeRemaining;

        //implement get method to pull information from JSON/GSON
    }
}
