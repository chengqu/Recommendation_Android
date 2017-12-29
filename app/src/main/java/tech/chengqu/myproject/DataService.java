package tech.chengqu.myproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Service for retrieving data from backend.
 */
public class DataService {
    /**
     * Fake all the restaurant data for now. We will refine this and connect
     * to our backend later.
     */
    /**
     * Get nearby restaurants through Yelp API.
     */
    public List<Restaurant> getNearbyRestaurants() {
        YelpAPI yelp = new YelpAPI();
        String jsonResponse = yelp.searchForBusinessesByLocation("dinner", "San Francisco, CA", 20);
        return parseResponse(jsonResponse);
    }

    /**
     * Parse the JSON response returned by Yelp API.
     */
    public static String parseString(String str) {
        return str.replace("\"", "\\\"").replace("/", " or ");
    }
    private List<Restaurant> parseResponse(String jsonResponse)  {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray businesses = json.getJSONArray("businesses");
            ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
            for (int i = 0; i < businesses.length(); i++) {
                JSONObject business = businesses.getJSONObject(i);

                //Parse restaurant information
                if (business != null) {
                    String name = business.getString("name");
                    JSONArray catArray = (JSONArray) business.get("categories");
                    HashSet<String> set = new HashSet<>();
                    for (int index = 0; index < catArray.length(); index++) {
                        JSONObject subArray = catArray.getJSONObject(index);
                        set.add(parseString(subArray.get("alias").toString().toLowerCase()));
                        set.add(parseString(subArray.get("title").toString().toLowerCase()));
                    }
                    String type ="";
                    for(String s:set){
                       type += s + ",";
                    }
                    type = type.replaceAll(",$", "");
                    JSONObject location = (JSONObject) business.get("location");
                    JSONObject coordinate = (JSONObject) business.get("coordinates");
                    double lat = coordinate.getDouble("latitude");
                    double lng = coordinate.getDouble("longitude");
                    String address =
                            ((JSONArray) location.get("display_address")).get(0).toString();

                    // Download the image.
                    Bitmap thumbnail =  getBitmapFromURL(business.getString("image_url"));

                    restaurants.add(new Restaurant(name, address, type, lat, lng, thumbnail));
                }
            }
            return restaurants;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Download an Image from the given URL, then decodes and returns a Bitmap object.
     */
    public Bitmap getBitmapFromURL(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Error: ", e.getMessage().toString());
        }
        return bitmap;
    }
}