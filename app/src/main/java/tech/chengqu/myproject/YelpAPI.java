package tech.chengqu.myproject;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

public class YelpAPI {
    private static final String API_HOST = "api.yelp.com";
    private static final String DEFAULT_TERM = "dinner";
    private static final int SEARCH_LIMIT = 20;
    private static final String SEARCH_PATH = "/v3/businesses/search";
    private static final String API_KEY = "0RM0wH9pwE3_yzjg9sezkJpdIqJawjoU-dJlyhG8573p-Hx2IzDq5Gf1suECB7RI41o8xNqgfcplrLp4YrsozMTI6Ke1AWJhMNs_a7OpMw8ReTM2EdfNCQGVUKwvWnYx";
    private static final String CONSUMER_SECRET = "nQoajRQm7lM8SE0FsKBpdOiN96v7NVXXjvyrnksUIZcBy5GctzKt50m1JDFYVdXJ";


    /**
     * Setup the Yelp API OAuth credentials.
     */
    public YelpAPI() {
    }

    /**
     * Creates and sends a request to the Search API by term and location.
     */
    public String searchForBusinessesByLocation(String term, String location, int searchLimit) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "https://" + API_HOST
                + SEARCH_PATH);
        request.addHeader("Authorization", "Bearer " + API_KEY);
        request.addQuerystringParameter("term", term);
        //request.addQuerystringParameter("latitude", lat + "");
        //request.addQuerystringParameter("longitude", lon + "");
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("limit", String.valueOf(searchLimit));
        return sendRequestAndGetResponse(request);
    }

    /**
     * Sends an {@link OAuthRequest} and returns the {@link Response} body.
     */
    private String sendRequestAndGetResponse(OAuthRequest request) {
        Log.i("Querying " , request.getCompleteUrl() + " ...");
        //this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    /**
     * Queries the Search API based on the command line arguments and takes the
     * first result to query the Business API.
     *
    private static void queryAPI(YelpAPI yelpApi, double lat, double lon) {
        String searchResponseJSON = yelpApi.searchForBusinessesByLocation(lat,
                lon);
        JSONObject response = null;
        try {
            response = new JSONObject(searchResponseJSON);
            JSONArray businesses = (JSONArray) response.get("businesses");
            for (int i = 0; i < businesses.length(); i++) {
                JSONObject business = (JSONObject) businesses.get(i);
                System.out.println(business);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main entry for sample Yelp API requests.
     *
    public static void main(String[] args) {
        YelpAPI yelpApi = new YelpAPI();
        queryAPI(yelpApi, 37.38, -122.08);
    }*/
}


