package com.example.cindyfeng.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    /** the info button*/
    private Button settings_button;

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    /**
     * Default logging tag for messages from the main activity.
     */
    private static final String TAG = "Lab11:Main";

    private static String previous = "";
    /**
     * the sound effects for the buttons in the main activity.
     */
    private SoundPlayer sound;

    /**
     * Request queue for our API requests.
     */
    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sound = new SoundPlayer(this);

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
            }
        });

    }
    /** method for button that opens the info page.*/
    public void openActivity2() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
        sound.playWowSound();
    }


    /** method for calling the activity generator button. */
    void getSuggestionCall(final int participants) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://www.boredapi.com/api/activity?participants=" + participants,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            apiCallDone(response, participants);
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
        sound.playOverSound();
    }

    /** our call to the API. */
    void apiCallDone(final JSONObject response, final int participants) {
        try {
            String activityNameString = response.get("activity").toString();
            String searchTerm = activityNameString;
            if (searchTerm.equals("Shop at support your local farmers market")) {
                searchTerm = "Shop at your local farmer's market";
            }
            if (searchTerm.equals("Learn how to iceskate or rollerskate")) {
                searchTerm = "Learn how to ice skate or roller skate";
            }
            if (searchTerm.equals(previous)) {
                getSuggestionCall(participants);
                return;
            }
            previous = searchTerm;

            TextView activityNameText = (TextView) findViewById(R.id.activityNameText);
            activityNameText.setText("ACTIVITY: " + activityNameString);
            activityNameText.setVisibility(View.VISIBLE);
            imageCall(searchTerm);


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


    /** a method to categorize the accesiblity.*/
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

    /** a method to categorize the price.*/
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

    /** the method that calls the image that comes with every activity. */
    void imageCall(final String searchTerm) {
        //Log.d(TAG, "IMAGE CALLED");
        ImageView responseImage = (ImageView) findViewById(R.id.responseImage);
        //Random random = new Random();
       /*if(random.nextBoolean()) {
           Glide.with(this).load("https://cdn.pixabay.com/photo/2017/01/20/00/30/maldives-1993704_1280.jpg").into(responseImage);
       } else {
           Glide.with(this).load("https://cdn.pixabay.com/photo/2014/12/15/17/16/pier-569314_1280.jpg").into(responseImage);
       }
       */
        switch(searchTerm) {
            case "Plan a vacation you've always wanted to take":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/01/20/00/30/maldives-1993704_1280.jpg").into(responseImage);
                break;
            case "Pot some plants and put them around your house":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/04/10/17/03/pots-716579_1280.jpg").into(responseImage);
                break;
            case "Shop at your local farmer's market":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/06/10/17/39/market-3466906__340.jpg").into(responseImage);
                break;
            case "Learn to write with your nondominant hand":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/07/02/10/40/writing-828911__340.jpg").into(responseImage);
                break;
            case "Write a thank you letter to an influential person in your life":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/11/03/17/45/thank-you-515514__340.jpg").into(responseImage);
                break;
            case "Start a garden":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/12/17/21/30/wild-flowers-571940__340.jpg").into(responseImage);
                break;
            case "Repaint a room in your house":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/05/19/23/40/painting-3414649__340.jpg").into(responseImage);
                break;
            case "Make a budget":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/08/07/16/07/shopping-879498__340.jpg").into(responseImage);
                break;
            case "Make a simple musical instrument":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/06/23/09/13/music-818459__340.jpg").into(responseImage);
                break;
            case "Create a cookbook with your favorite recipes":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/04/29/19/33/cookbook-746005__340.jpg").into(responseImage);
                break;
            case "Create a personal website":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/12/28/13/20/wordpress-581849__340.jpg").into(responseImage);
                break;
            case "Start a daily journal":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/10/14/09/56/journal-2850091__340.jpg").into(responseImage);
                break;
            case "Make a to-do list for your week":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/02/18/11/00/checklist-2077020__340.jpg").into(responseImage);
                break;
            case "Listen to a new podcast":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/05/05/19/49/microphone-338481__340.jpg").into(responseImage);
                break;
            case "Take a class at your local community center that interests you":
                Glide.with(this).load("https://image.shutterstock.com/image-photo/community-centre-building-260nw-755780284.jpg").into(responseImage);
                break;
            case "Learn how the internet works":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/03/15/16/11/background-3228704__340.jpg").into(responseImage);
                break;
            case "Listen to a new music genre":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/01/20/13/13/ipad-605439__340.jpg").into(responseImage);
                break;
            case "Volunteer at your local food shelter":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/02/10/12/12/volunteer-2055042__340.png").into(responseImage);
                break;
            case "Learn how to play a new sport":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2013/02/20/14/50/golf-83869__340.jpg").into(responseImage);
                break;
            case "Create or update your resume":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/11/05/11/15/resume-1799953__340.png").into(responseImage);
                break;
            case "Start a collection":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/11/20/08/58/arranged-1842261__340.jpg").into(responseImage);
                break;
            case "Watch a classic movie":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/12/09/17/12/popcorn-1085072__340.jpg").into(responseImage);
                break;
            case "Make a scrapbook with pictures of your favorite memories":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/08/25/00/18/booklet-426781__340.jpg").into(responseImage);
                break;
            case "Do something nice for someone you care about":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/05/01/17/45/tea-335649__340.jpg").into(responseImage);
                break;
            case "Fix something that's broken in your house":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/07/28/20/55/tools-864983__340.jpg").into(responseImage);
                break;
            case "Go to the gym":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/11/17/13/17/crossfit-534615__340.jpg").into(responseImage);
                break;
            case "Start a book you've never gotten around to reading":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/11/19/21/10/knowledge-1052010__340.jpg").into(responseImage);
                break;
            case "Learn how to use a french press":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/04/11/02/00/kettle-2220369__340.jpg").into(responseImage);
                break;
            case "Catch up on world news":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/05/31/12/12/coffee-791439__340.jpg").into(responseImage);
                break;
            case "Volunteer at your local food pantry":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/04/11/10/00/soup-3310066__340.jpg").into(responseImage);
                break;
            case "Take a bubble bath":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/11/19/20/02/background-1051850__340.jpg").into(responseImage);
                break;
            case "Think of a new business idea":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/03/07/13/02/thought-2123970__340.jpg").into(responseImage);
                break;
            case "Volunteer and help out at a senior center":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/12/21/07/40/care-3031259__340.jpg").into(responseImage);
                break;
            case "Surprise your significant other with something considerate":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/11/16/08/35/box-2953722__340.jpg").into(responseImage);
                break;
            case "Learn to greet someone in a new language":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/01/20/19/42/frog-3095250__340.jpg").into(responseImage);
                break;
            case "Learn how to ice skate or roller skate":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/07/01/12/25/roller-skates-381216__340.jpg").into(responseImage);
                break;
            case "Volunteer at a local animal shelter":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/08/23/08/33/cats-eyes-2671903__340.jpg").into(responseImage);
                break;
            case "Find a DIY to do":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/07/11/14/53/plumbing-840835__340.jpg").into(responseImage);
                break;
            case "Clean out your garage":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/07/31/23/37/motorbike-407186__340.jpg").into(responseImage);
                break;
            case "Explore the nightlife of your city":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/02/19/11/46/night-1209938__340.jpg").into(responseImage);
                break;
            case "Wash your car":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/11/14/02/34/auto-1822415__340.jpg").into(responseImage);
                break;
            case "Learn origami":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/07/12/14/30/origami-842024__340.png").into(responseImage);
                break;
            case "Watch a movie you'd never usually watch":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/07/13/23/11/cinema-2502213__340.jpg").into(responseImage);
                break;
            case "Take a hike at a local park":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/09/21/17/56/wanderer-455338__340.jpg").into(responseImage);
                break;
            case "Take your dog on a walk":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/09/18/21/08/man-3687274__340.jpg").into(responseImage);
                break;
            case "Go stargazing":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/01/19/15/20/star-gazing-1149228__340.jpg").into(responseImage);
                break;
            case "Create and follow a savings plan":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/03/01/09/33/paper-3190198__340.jpg").into(responseImage);
                break;
            case "Learn how to play a new instrument":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/09/08/21/09/piano-1655558__340.jpg").into(responseImage);
                break;
            case "Make bread from scratch":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/11/29/08/24/bakery-1868396__340.jpg").into(responseImage);
                break;
            case "Clean out your car":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/02/20/20/34/car-3168762__340.jpg").into(responseImage);
                break;
            case "Learn how to sew on a button":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/11/22/15/13/sewing-needle-541737__340.jpg").into(responseImage);
                break;
            case "Fill out a basketball bracket":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/11/18/22/10/ball-1837119__340.jpg").into(responseImage);
                break;
            case "Plan a trip to another country":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/05/03/00/42/vw-camper-336606__340.jpg").into(responseImage);
                break;
            case "Find a charity and donate to it":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/08/06/14/11/money-1574450__340.png").into(responseImage);
                break;
            case "Go to the library and find an interesting book":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/06/02/12/59/narrative-794978__340.jpg").into(responseImage);
                break;
            case "Learn how to make an Alexa skill":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/09/28/22/14/speech-icon-2797263__340.png").into(responseImage);
                break;
            case "Binge watch a trending series":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/10/26/11/14/tv-3774381__340.jpg").into(responseImage);
                break;
            case "Draw and color a Mandala":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/02/01/20/08/mandala-2031287__340.png").into(responseImage);
                break;
            case "Read a formal research paper on an interesting subject":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/03/22/10/12/paper-3249919__340.jpg").into(responseImage);
                break;
            case "Make a couch fort":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/09/01/01/43/little-boy-1635065__340.jpg").into(responseImage);
                break;
            case "Hold a yard sale":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/09/29/23/40/arrow-964733__340.jpg").into(responseImage);
                break;
            case "Pick up litter around your favorite park":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/09/08/18/20/garbage-2729608__340.jpg").into(responseImage);
                break;
            case "Make tie dye shirts":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/08/08/00/46/tie-dye-3591130__340.jpg").into(responseImage);
                break;
            case "Learn how to fold a paper crane":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/09/12/08/16/origami-936729__340.jpg").into(responseImage);
                break;
            case "Meditate for five minutes":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/11/22/23/29/meditate-1851165__340.jpg").into(responseImage);
                break;
            case "Learn about the Golden Ratio":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/03/09/08/40/fibonacci-3210943__340.jpg").into(responseImage);
                break;
            case "Learn calligraphy":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/11/12/10/12/calligraphy-3810555__340.jpg").into(responseImage);
                break;
            case "Clean out your closet and donate the clothes you've outgrown":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/04/19/13/39/store-1338629__340.jpg").into(responseImage);
                break;
            case "Go on a long drive with no music":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/10/21/14/50/autumn-1758194__340.jpg").into(responseImage);
                break;
            case "Start a blog for something you're passionate about":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/05/31/10/55/man-791049__340.jpg").into(responseImage);
                break;
            case "Donate blood at a local blood center":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/04/20/21/42/blood-732298__340.jpg").into(responseImage);
                break;
            case "Bake something you've never tried before":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/05/07/15/08/pastries-756601__340.jpg").into(responseImage);
                break;
            case "Buy a new house decoration":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/03/19/01/18/living-room-2155353__340.jpg").into(responseImage);
                break;
            case "Listen to your favorite album":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/11/19/00/12/wave-1837426__340.png").into(responseImage);
                break;
            case "Take a caffeine nap":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/04/19/13/03/coffee-2242213__340.jpg").into(responseImage);
                break;
            case "Bake pastries for you and your neighbor":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/05/26/16/27/baking-1417494__340.jpg").into(responseImage);
                break;
            case "Paint the first thing you see":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/11/23/00/37/art-1851483__340.jpg").into(responseImage);
                break;
            case "Create a compost pile":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/08/16/11/17/compost-419261__340.jpg").into(responseImage);
                break;
            case "Try a food you don't like":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/08/11/08/04/vegetables-1584999__340.jpg").into(responseImage);
                break;
            case "Resolve a problem you've been putting off":
                Glide.with(this).load(" https://cdn.pixabay.com/photo/2017/10/30/13/36/stress-2902537__340.jpg").into(responseImage);
                break;
            case "Rearrange and organize your room":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/11/21/16/21/bed-1846251__340.jpg").into(responseImage);
                break;
            case "Go to a local thrift shop":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/10/11/13/37/clothing-3739798__340.jpg").into(responseImage);
                break;
            case "Make a bucket list":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/06/08/08/26/chalkboard-801266__340.jpg").into(responseImage);
                break;
            case "Pull a harmless prank on one of your friends":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/11/17/18/21/snow-man-1832316__340.jpg").into(responseImage);
                break;
            case "Learn how to make a website":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/05/18/15/30/webdesign-3411373__340.jpg").into(responseImage);
                break;
            case "Write a short story":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/04/12/17/22/once-upon-a-time-719174__340.jpg").into(responseImage);
                break;
            case "Learn Express.js":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/05/08/08/44/artificial-intelligence-3382507__340.jpg").into(responseImage);
                break;
            case "Go to a nail salon":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/08/28/06/36/nail-art-2688565__340.jpg").into(responseImage);
                break;
            case "Teach your dog a new trick":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/12/13/05/15/puppy-1903313__340.jpg").into(responseImage);
                break;
            case "Learn how to french braid hair":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/11/23/17/30/blur-1853957__340.jpg").into(responseImage);
                break;
            case "Learn how to whistle with your fingers":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/02/22/07/04/whistle-3172289__340.png").into(responseImage);
                break;
            case "Memorize the fifty states and their capitals":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2013/07/13/01/21/usa-155594__340.png").into(responseImage);
                break;
            case "Make homemade ice cream":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/04/10/19/46/ice-2219574__340.jpg").into(responseImage);
                break;
            case "Conquer one of your fears":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/10/24/18/27/lion-2885618__340.jpg").into(responseImage);
                break;
            case "Take your cat on a walk":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/11/16/22/14/cat-1046544__340.jpg").into(responseImage);
                break;
            case "Learn how to write in shorthand":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/01/08/18/30/entrepreneur-593378__340.jpg").into(responseImage);
                break;
            case "Learn a new recipe":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/10/26/07/21/soup-1006694__340.jpg").into(responseImage);
                break;
            case "Learn a new programming language":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/04/20/13/17/work-731198__340.jpg").into(responseImage);
                break;
            case "Make a new friend":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/08/07/18/57/dog-2606759__340.jpg").into(responseImage);
                break;
            case "Play a game of tennis with a friend":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/07/22/08/49/tennis-3554019__340.jpg").into(responseImage);
                break;
            case "Catch up with a friend over a lunch date":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2018/07/14/15/27/cafe-3537801__340.jpg").into(responseImage);
                break;
            case "Go swimming with a friend":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/03/27/21/55/sunglasses-1284419__340.jpg").into(responseImage);
                break;
            case "Text a friend you haven't talked to in a long time":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/09/02/12/43/phone-918633__340.jpg").into(responseImage);
                break;
            case "Go to a concert with local artists with some friends":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/07/30/17/24/audience-868074__340.jpg").into(responseImage);
                break;
            case "Have a picnic with some friends":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2015/09/02/12/55/picnic-918754__340.jpg").into(responseImage);
                break;
            case "Go to a karaoke bar with some friends":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/11/23/00/43/audio-1851517__340.jpg").into(responseImage);
                break;
            case "Take a spontaneous road trip with some friends":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/07/30/00/03/winding-road-1556177__340.jpg").into(responseImage);
                break;
            case "Go to an escape room":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/01/22/11/50/live-escape-game-1155620__340.jpg").into(responseImage);
                break;
            case "Go to a music festival with some friends":
                Glide.with(this).load(" https://cdn.pixabay.com/photo/2015/11/22/19/04/crowd-1056764__340.jpg").into(responseImage);
                break;
            case "Invite some friends over for a game night":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2014/08/12/20/06/letters-416960__340.jpg").into(responseImage);
                break;
            case "Have a bonfire with your close friends":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2013/06/09/17/04/fire-123784__340.jpg").into(responseImage);
                break;
            case "Play a game of Monopoly":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/08/13/05/18/monopoly-2636268__340.jpg").into(responseImage);
                break;
            case "Go to a concert with some friends":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/10/17/16/35/concert-1748102__340.jpg").into(responseImage);
                break;
            case "Have a paper airplane contest with some friends":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/08/20/11/34/paper-plane-1607340__340.jpg").into(responseImage);
                break;
            case "Go see a movie in theaters with a few friends":
                Glide.with(this).load("https://cdn.pixabay.com/photo/2017/11/24/10/43/admission-2974645__340.jpg").into(responseImage);
                break;
            default:
                Glide.with(this).load("https://cdn.pixabay.com/photo/2016/11/12/23/34/learn-1820039__340.jpg").into(responseImage);
                break;
        }

    }
}
