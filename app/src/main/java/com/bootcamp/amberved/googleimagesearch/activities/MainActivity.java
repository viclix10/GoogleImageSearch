package com.bootcamp.amberved.googleimagesearch.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.bootcamp.amberved.googleimagesearch.adapaters.GoogleImageResultsAdapater;
import com.bootcamp.amberved.googleimagesearch.models.GoogleImageResults;
import com.bootcamp.amberved.googleimagesearch.R;
import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    //Google Image Search API is:
    final static String BASE_GOOGLE_IMAGE_SEARCH_URL = 
            "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8";
    
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<GoogleImageResults> googleImageResults;
    private GoogleImageResultsAdapater aGoogleImageResults;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setupView();
        
        setupListViewListener();
        
        //Create DataSource
        googleImageResults = new ArrayList<GoogleImageResults>();
        //Attach DataSource to Adapater
        aGoogleImageResults = new GoogleImageResultsAdapater(this, googleImageResults);
        //Link Adapter to AdapaterView
        gvResults.setAdapter(aGoogleImageResults);

    }

    private void setupListViewListener() {
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GoogleImageResults img = googleImageResults.get(position);

                Intent i = new Intent(MainActivity.this, FullImage.class);
                i.putExtra("img", img.getUrl());
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setupView()
    {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
    }
    
    public void onImageSearch(View v)
    {
        String query = etQuery.getText().toString();
        String searchUrl = BASE_GOOGLE_IMAGE_SEARCH_URL+"&q="+query;
        AsyncHttpClient client = new AsyncHttpClient();
        
        client.get(searchUrl, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("MainActivity.OnSuccess", response.toString());
                JSONArray searchResultsJson = null;
                try {
                    searchResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    googleImageResults.clear();
                    aGoogleImageResults.addAll(GoogleImageResults.fromJSONArray(searchResultsJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("Info", googleImageResults.toString());
            }
            
            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
