package com.example.andrew.judgetabs;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrew.judgetabs.Objects.Team;
import com.example.andrew.judgetabs.Objects.User;
import com.example.andrew.judgetabs.Support.RubricFragment;
import com.example.andrew.judgetabs.Support.TeamInfoFragment;

/**
 * Created by Andrew on 7/2/2015.
 */
public class TeamActivity extends FragmentActivity
            implements TeamInfoFragment.OnFragmentInteractionListener,
            RubricFragment.OnFragmentInteractionListener {
        private User user;
        private Team team;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_team);

            ViewPager pager = (ViewPager) findViewById(R.id.viewPager);

            // TODO: Move to Adapters
            pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager())
            {
                private static final int NUM_TABS = 2;

                @Override
                public Fragment getItem(int position)
                {
                    switch (position)
                    {
                        case 0:
                            return new TeamInfoFragment();
                        case 1:
                            return new RubricFragment();
                        default:
                            return null;
                    }
                }

                @Override
                public int getCount()
                {
                    return NUM_TABS;
                }

                @Override
                public CharSequence getPageTitle(int position)
                {
                    switch (position)
                    {
                        case 0:
                            return TeamActivity.this.team.getName();
                        case 1:
                            return "Rubric";
                        default:
                            return null;
                    }
                }
            });

//        criteria_lv = (ListView) findViewById(R.id.criteria_lv);
            user = getIntent().getParcelableExtra("user");
            team = getIntent().getParcelableExtra("team");

            // Set ActionBar title to team's name
            setTitle(team.getName());
        }

        /**
         * Called on logout button click
         *
         * @param v The calling view
         */
        public void logoutClick(View v)
        {
            AlertDialog logoutDialog = new AlertDialog.Builder(TeamActivity.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            APILogout();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
            logoutDialog.show();
        }

        /**
         * Makes an actual logout call through the API
         */
        private void APILogout()
        {
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Authorization", "Token token=" + user.getToken().getAccess_token());
            client.get(LoginActivity.API_ROOT + "logout", new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                {
                    Toast.makeText(TeamActivity.this, "Logout success!", Toast.LENGTH_LONG).show();

                    // Return to the login screen (first instance of it)
                    Intent toLoginScreen = new Intent(TeamActivity.this, LoginActivity.class);
                    toLoginScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    TeamActivity.this.finish();
                    TeamActivity.this.startActivity(toLoginScreen);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                                      Throwable error)
                {
                    Toast.makeText(TeamActivity.this, "Logout failure", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
            getMenuInflater().inflate(R.menu.menu_team, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            int itemId = item.getItemId();
            switch (itemId)
            {
                case android.R.id.home:
                    onBackPressed();
                    return true;
                case R.id.action_logout:
                    logoutClick(findViewById(itemId));
                    return true;
                default:
                    return false;
            }
        }

        public void onFragmentInteraction(Uri uri) {}
    }
}