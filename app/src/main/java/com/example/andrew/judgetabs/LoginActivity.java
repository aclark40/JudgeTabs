package com.example.andrew.judgetabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.andrew.judgetabs.Objects.Event;
import com.example.andrew.judgetabs.Objects.User;
import com.example.andrew.judgetabs.Support.Adapters.EventAdapter;


public class LoginActivity extends ActionBarActivity
{

    public final static String API_ROOT = "http://api.stevedolan.me/";
    private User user;

    private ListView events_lv;
    private Event[] eventsArr;
    private PopupWindow eventsPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Called on login button click
     * @param v The calling View
     */
    public void loginClick(View v)
    {
        String email = ((EditText) findViewById(R.id.email_et)).getText().toString();
        String password = ((EditText) findViewById(R.id.password_et)).getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth(email, password);
        client.get(API_ROOT+"login", new BaseJsonHttpResponseHandler<User>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse,
                                  User response) {
                Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                user = response;

                // Hide the keyboard after a successful login (so it doesn't get in the way of events)
                RelativeLayout loginLL = (RelativeLayout) findViewById(R.id.activity_login_layout);
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(loginLL.getWindowToken(), 0);

                showEventSelectPopup();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error,
                                  String rawJsonData, User errorResponse) {
                Toast.makeText(LoginActivity.this, "Login failure",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            protected User parseResponse(String rawJsonData, boolean isFailure)
                throws Throwable {

                if(!isFailure) {
                    // Need to extract user variables from the first JSON object
                    JSONObject userJSON = new JSONObject(rawJsonData).getJSONObject("user");
                    return new Gson().fromJson(userJSON.toString(), User.class);
                }
                else return null;
            }
        });
    }

    /**
     * Shows a popup to prompt the user to select an available event
     */
    private void showEventSelectPopup() {
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
        eventsPopup.showAtLocation(popupLayout, Gravity.CENTER, 0, 0);
        populateUserEvents();
    }

    /**
     * Adds all available events that the user has access to in the corresponding ListView
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
                if(eventsArr.length == 0) {
                    eventsArr = new Event[1];
                    eventsArr[0] = new Event(-1, "(No events. Please see an admin for help.)", null,
                            null, null, null, null, null, null);
                    events_lv.setAdapter(new EventAdapter(LoginActivity.this,
                            eventsArr));
                }
                else {
                    events_lv.setAdapter(new EventAdapter(LoginActivity.this,
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

                if(!isFailure) {
                    // Need to extract array from the first/outer JSON object
                    JSONArray eventsJSONArr = new JSONObject(rawJsonData).getJSONArray("events");
                    return new Gson().fromJson(eventsJSONArr.toString(), Event[].class);
                }
                else return null;
            }
        });
    }

    /**
     * Defines the behavior for a contact click on the contact ListView adapter
     */
    private class EventClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Event selectedEvent = (Event) events_lv.getAdapter()
                    .getItem(position);
            Toast.makeText(LoginActivity.this, selectedEvent.getName(), Toast.LENGTH_SHORT).show();

            Intent toSelectedEvent = new Intent(LoginActivity.this, SelectionActivity.class);
            toSelectedEvent.putExtra("user", user);
            toSelectedEvent.putExtra("event", selectedEvent);

            eventsPopup.dismiss();
            LoginActivity.this.startActivity(toSelectedEvent);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
