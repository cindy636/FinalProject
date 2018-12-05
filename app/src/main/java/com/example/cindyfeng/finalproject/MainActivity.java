package com.example.cindyfeng.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button settings_button;

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    /**
     * Default logging tag for messages from the main activity.
     */
    private static final String TAG = "Lab11:Main";

    /**
     * Request queue for our API requests.
     */
    private static RequestQueue requestQueue;

    static String searchTerm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);
        settings_button = (Button) findViewById(R.id.settings_button);
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();

            }
        });
        final Button getSuggestionBtn = (Button) findViewById(R.id.getSuggestionBtn);
        final SeekBar participantInput = (SeekBar) findViewById(R.id.participantInput);

        getSuggestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Get Suggestion button clicked");
                int participants = participantInput.getProgress() + 1;
                getSuggestionCall(participants);
                //imageCall(searchTerm);
            }
        });

    }
    public void openActivity2() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    void getSuggestionCall(final int participants) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://www.boredapi.com/api/activity?participants=" + participants,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            apiCallDone(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            jsonObjectRequest.setShouldCache(false);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void imageCall(final String searchTerm) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://www.boredapi.com/api/activity?participants=",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            apiCallDone(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            jsonObjectRequest.setShouldCache(false);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void apiCallDone(final JSONObject response) {
        try {
            String activityNameString = response.get("activity").toString();
            searchTerm = activityNameString;
            TextView activityNameText = (TextView) findViewById(R.id.activityNameText);
            activityNameText.setText("ACTIVITY: " + activityNameString);
            activityNameText.setVisibility(View.VISIBLE);

            String activityTypeString = response.get("type").toString();
            activityTypeString = activityTypeString.substring(0, 1).toUpperCase()
                    + activityTypeString.substring(1, activityTypeString.length());
            TextView activityTypeText = (TextView) findViewById(R.id.activityTypeText);
            activityTypeText.setText("TYPE: " + activityTypeString);
            activityTypeText.setVisibility(View.VISIBLE);

            String activityAccessibilityString = response.get("accessibility").toString();
            TextView activityAccessibilityText = (TextView) findViewById(R.id.activityAccessibilityText);
            double accessibilityRating = Double.parseDouble(activityAccessibilityString);
            activityAccessibilityText.setText("ACCESSIBILITY: " + accessRate(accessibilityRating));
            activityAccessibilityText.setVisibility(View.VISIBLE);

            String activityPriceString = response.get("price").toString();
            TextView activityPriceText = (TextView) findViewById(R.id.activityPriceText);
            double priceRating = Double.parseDouble(activityPriceString);
            activityPriceText.setText("PRICE: " + priceRate(priceRating));
            activityPriceText.setVisibility(View.VISIBLE);

        } catch (JSONException ignored) {
        }
    }

    String accessRate(final double number) {
        if (number <= 0.333) {
            return "Very accessible";
        }
        if (number <= 0.666) {
            return "Moderately accessible";
        } else {
            return "Perhaps inconvenient";
        }
    }

    String priceRate(final double number) {
        if (number <= 0.333) {
            return "Inexpensive";
        }
        if (number <= 0.666) {
            return "Moderately expensive";
        } else {
            return "Very expensive";
        }
    }
}