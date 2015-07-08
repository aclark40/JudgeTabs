package com.example.andrew.judgetabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.andrew.judgetabs.Objects.Category;
import com.example.andrew.judgetabs.Objects.Event;
import com.example.andrew.judgetabs.Objects.Team;
import com.example.andrew.judgetabs.Objects.User;
import com.example.andrew.judgetabs.Support.Adapters.CategoryAdapter;
import com.example.andrew.judgetabs.Support.Adapters.EventAdapter;
import com.example.andrew.judgetabs.Support.Adapters.TeamAdapter;


public class SelectionActivity extends ActionBarActivity {

    private ListView events_lv, categories_lv, teams_lv;
    private User user;
    private PopupWindow eventsPopup;

    private Event[] eventsArr;
    private Event currEvent;
    private Category[] categoriesArr;
    private Category selectedCategory;
    private Team[] teamsArr;
    private Team[] displayedTeams;
    private Team selectedTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        this.user = getIntent().getParcelableExtra("user");
        this.currEvent = getIntent().getParcelableExtra("event");

        // Prepare the map image (tbd)
        ImageView map_iv = (ImageView) findViewById(R.id.mapButton);
        map_iv.setImageResource(R.mipmap.ic_gavel);
        map_iv.setVisibility(View.INVISIBLE);

        populateTeams();
        populateCategories();
    }

    /**
     * Fills the teams ListView with a TeamAdapter that contains all teams for the current event
     */
    private void populateTeams() {

        // TODO: Can't get teams assigned to judges for now. Instead, getting all teams in an event
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Token token=" + this.user.getToken().getAccess_token());
        client.get(LoginActivity.API_ROOT + "events/" + currEvent.getId() + "/teams",
                new BaseJsonHttpResponseHandler<Team[]>() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                          Team[] response) {
                        teams_lv = (ListView) findViewById(R.id.teams_lv);
                        teamsArr = response;

                        // "All" will be the default category
                        displayedTeams = teamsArr;

                        // Make an "empty" Event in the case that there are no Events.
                        if (teamsArr.length == 0) {
                            teamsArr = new Team[1];
                            teamsArr[0] = new Team(-1, -1, "(No teams. Please see an admin for help.)",
                                    null);
                            teams_lv.setAdapter(new TeamAdapter(SelectionActivity.this,
                                    teamsArr));
                        } else {
                            teams_lv.setAdapter(new TeamAdapter(SelectionActivity.this,
                                    teamsArr));
                            teams_lv.setOnItemClickListener(new TeamClickListener());
                        }
                        Log.d("GET TEAMS", "success");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable error,
                                          String rawJsonData, Team[] errorResponse) {
                        Log.d("GET TEAMS", "failure");
                    }

                    @Override
                    protected Team[] parseResponse(String rawJsonData, boolean isFailure)
                            throws Throwable {

                        if (!isFailure) {
                            // Need to extract array from the first/outer JSON object
                            JSONArray teamsJSONArr = new JSONObject(rawJsonData)
                                    .getJSONArray("event_teams");
                            return new Gson().fromJson(teamsJSONArr.toString(), Team[].class);
                        } else return null;
                    }
                });
    }

    /**
     * Fills the events ListView with an EventAdapter that contains all available events for the user
     */
    private void populateUserEvents() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Token token=" + user.getToken().getAccess_token());
        client.get(LoginActivity.API_ROOT + "events", new BaseJsonHttpResponseHandler<Event[]>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                  Event[] response) {
//                ListView events_lv = (ListView) findViewById(R.id.events_lv);
                eventsArr = response;

                // Make an "empty" Event in the case that there are no Events.
                if (eventsArr.length == 0) {
                    eventsArr = new Event[1];
                    eventsArr[0] = new Event(-1, "(No events. Please see an admin for help.)", null,
                            null, null, null, null, null, null);
                    events_lv.setAdapter(new EventAdapter(SelectionActivity.this,
                            eventsArr));
                } else {
                    events_lv.setAdapter(new EventAdapter(SelectionActivity.this,
                            eventsArr));
                    events_lv.setOnItemClickListener(new EventClickListener());
                }

                Log.d("GET EVENTS", "success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error,
                                  String rawJsonData, Event[] errorResponse) {
                Log.d("GET EVENTS", "failure");
            }

            @Override
            protected Event[] parseResponse(String rawJsonData, boolean isFailure)
                    throws Throwable {

                if (!isFailure) {
                    // Need to extract array from the first/outer JSON object
                    JSONArray eventsJSONArr = new JSONObject(rawJsonData).getJSONArray("events");
                    return new Gson().fromJson(eventsJSONArr.toString(), Event[].class);
                } else return null;
            }
        });
    }

    /**
     * Fills the categories ListView with a CategoryAdapter that contains all categories
     * for the current event
     */
    private void populateCategories() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Token token=" + user.getToken().getAccess_token());
        client.get(LoginActivity.API_ROOT + "events/" + currEvent.getId() + "/categories",
                new BaseJsonHttpResponseHandler<Category[]>() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                          Category[] response) {
                        categories_lv = (ListView) findViewById(R.id.categories_lv);
//                        categoriesArr = response;

                        // Make the first Category as "All" so that all teams can be shown
                        // All retrieved categories will be put after "All"
                        categoriesArr = new Category[response.length + 1];
                        categoriesArr[0] = new Category(0, currEvent.getId(), "All", null, 0, null,
                                teamsArr);
                        System.arraycopy(response, 0, categoriesArr, 1, response.length);

                        categories_lv.setAdapter(new CategoryAdapter(SelectionActivity.this,
                                categoriesArr));
                        categories_lv.setOnItemClickListener(new CategoryClickListener());

                        Log.d("GET CATEGORIES", "success");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable error,
                                          String rawJsonData, Category[] errorResponse) {
                        Log.d("GET CATEGORIES", "failure");
                    }

                    @Override
                    protected Category[] parseResponse(String rawJsonData, boolean isFailure)
                            throws Throwable {

                        if (!isFailure) {
                            // Need to extract array from the first/outer JSON object
                            JSONArray teamsJSONArr = new JSONObject(rawJsonData)
                                    .getJSONArray("event_categories");
                            return new Gson().fromJson(teamsJSONArr.toString(), Category[].class);
                        } else return null;
                    }
                });
    }

    /**
     * Called on logout button click
     *
     * @param v The calling view
     */
    // TODO: Add a confirmation dialog
    // TODO: Loading spinning thing in case it hangs (goes for all API calls really)
    public void logoutClick(View v)  {

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Token token=" + user.getToken().getAccess_token());
        client.get(LoginActivity.API_ROOT + "logout", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(SelectionActivity.this, "Logout success!", Toast.LENGTH_LONG).show();

                // Return to the login screen (first instance of it)
                Intent toLoginScreen = new Intent(SelectionActivity.this, LoginActivity.class);
                toLoginScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                SelectionActivity.this.finish();
                SelectionActivity.this.startActivity(toLoginScreen);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                                  Throwable error) {
                Toast.makeText(SelectionActivity.this, "Logout failure", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Called on map button click
     *
     * @param v The calling View
     */
    public void mapClick(View v) {
        ImageView mapView = (ImageView) findViewById(R.id.mapButton);
        if(mapView.getVisibility() == ImageView.VISIBLE) {
            mapView.setVisibility(ImageView.INVISIBLE);
        }
        else {
            mapView.setVisibility(ImageView.VISIBLE);
        }
        toggleDrawer();
    }

    /**
     * Called on help button click
     * @param v The calling View
     */
    public void helpClick(View v) {
        Toast.makeText(this, "Go see an admin!", Toast.LENGTH_SHORT).show();
        toggleDrawer();
    }

    /**
     * Called on "switch event" button click
     *
     * @param v The calling View
     */
    public void switchEventClick(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupLayout = inflater.inflate(R.layout.fragment_main,
                (ViewGroup) findViewById(R.id.main_fragment_layout));

        // Set the global Event ListView from the popup's layout
        events_lv = (ListView) popupLayout.findViewById(R.id.main_lv);

        // Want to set the popup window's size to be about 7/10 of the screen
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = (int) (metrics.widthPixels*.7);
        int height = (int) (metrics.heightPixels*.7);

        eventsPopup = new PopupWindow(popupLayout, width, height, true);
        toggleDrawer();
        eventsPopup.showAtLocation(popupLayout, Gravity.CENTER, 0, 0);
        populateUserEvents();
    }

    /**
     * Closes the drawer if open and vice versa
     */
    private void toggleDrawer() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.menu);
        if(drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        }
        else drawer.openDrawer(Gravity.START);
    }

    /**
     * Sets the back button to make a logout call. The running Activity will still finish() pending
     * a successful logout.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.menu);
        if(drawer.isDrawerOpen(Gravity.START)) {
           drawer.closeDrawer(Gravity.START);
        }
        else logoutClick(findViewById(android.R.id.home));
    }

    /**
     * Defines the behavior for an event click on the event ListView adapter
     */
    private class EventClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Event selectedEvent = (Event) events_lv.getAdapter()
                    .getItem(position);
            
            SelectionActivity.this.currEvent = selectedEvent;
            populateTeams();
            populateCategories();
            eventsPopup.dismiss();
        }
    }

    /**
     * Defines the behavior for a team click on the team ListView adapter
     */
    private class TeamClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedTeam = (Team) teams_lv.getAdapter()
                    .getItem(position);
            Toast.makeText(SelectionActivity.this, selectedTeam.getName(), Toast.LENGTH_SHORT).show();

            Intent toSelectedTeam = new Intent(SelectionActivity.this, TeamsFragment.class);
            toSelectedTeam.putExtra("team", selectedTeam);
            SelectionActivity.this.startActivity(toSelectedTeam);
        }
    }

    /**
     * Defines the behavior for a category click on the category ListView adapter
     */
    private class CategoryClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Category tappedCategory = (Category) categories_lv.getAdapter()
                    .getItem(position);

            if(tappedCategory.getLabel().equals("All")) {
                displayedTeams = teamsArr;
            }
            else {

                ArrayList<Team> teamsToDisplay = new ArrayList<>();
                Team[] teamsInTappedCategory = tappedCategory.getTeams();
                ArrayList<Integer> categoryTeamIds = new ArrayList<>();

                // Get all team IDs in the Category
                for(int i=0; i < teamsInTappedCategory.length; i++) {
                    categoryTeamIds.add(teamsInTappedCategory[i].getId());
                    Log.d("teamIdInTappedCat", ""+teamsInTappedCategory[i].getId());
                }

                // If the entire array of Teams has the ID from the category,
                // then that Team will be chosen to be displayed
                for(Team t : teamsArr) {
                    if(categoryTeamIds.contains(t.getId())) {
                        teamsToDisplay.add(t);
                    }
                }
                displayedTeams = teamsToDisplay.toArray(new Team[teamsToDisplay.size()]);
            }

            teams_lv.setAdapter(new TeamAdapter(SelectionActivity.this,
                    displayedTeams));
            toggleDrawer();
            Toast.makeText(SelectionActivity.this, tappedCategory.getLabel(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.drawer_menuItem:
                toggleDrawer();
                return true;

            case android.R.id.home:
                logoutClick(findViewById(android.R.id.home));
                return true;

            default:
                return false;
        }
    }
}

