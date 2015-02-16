package com.bootcamp.amberved.googleimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.bootcamp.amberved.googleimagesearch.adapaters.GoogleImageResultsAdapater;
import com.bootcamp.amberved.googleimagesearch.helpers.EndlessScrollListener;
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
    
    private String typeFilter  = "Any";
    private String sizeFilter  = "Any";
    private String colorFilter = "Any";
    private String siteFilter  = "Any";
    private int pageCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setupView();
        
        //setupListViewListener();
        
        //Create DataSource
        googleImageResults = new ArrayList<GoogleImageResults>();
        //Attach DataSource to Adapater
        aGoogleImageResults = new GoogleImageResultsAdapater(this, googleImageResults);
        //Link Adapter to AdapaterView
        gvResults.setAdapter(aGoogleImageResults);

        gvResults.setOnScrollListener( new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.i("GoogleImageSearch", "onScrollListerner "+totalItemsCount);
                Toast.makeText(getApplicationContext(),"onScrollListerner"+totalItemsCount,Toast.LENGTH_SHORT).show();
                pageCount = totalItemsCount;
                googleImageSearch();
            }
        });
        
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GoogleImageResults img = googleImageResults.get(position);

                Intent i = new Intent(MainActivity.this, FullImageActivity.class);
                i.putExtra("img", img.getUrl());
                startActivity(i);
            }
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                etQuery.setText(query);
                googleImageSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setupView()
    {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
    }
    
    public void onImageSearch(View v) {
        if (!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "No active network to get images..", Toast.LENGTH_SHORT).show();
            return;
        }
        
        pageCount = 0; //Reset
        googleImageSearch();
    }
    
    public void googleImageSearch()
    {
        String query = etQuery.getText().toString();
        String searchUrl = BASE_GOOGLE_IMAGE_SEARCH_URL+"&q="+query;
        
        if (typeFilter != "Any") {
            searchUrl += "&imgtype=" + typeFilter;
        }

        if (sizeFilter != "Any") {
            searchUrl += "&imgz=" + sizeFilter;
        }

        if (colorFilter != "Any") {
            searchUrl += "&imgcolor=" + colorFilter;
        }

        if ((siteFilter != "Any") && (!(siteFilter.trim().isEmpty()))) {
            searchUrl += "&as_sitesearch=" + siteFilter.trim();
        }

        if (pageCount > 0) {
            searchUrl += "&start=" + pageCount;
        }
        
        Log.i("GoogleImageSearch", searchUrl);

        AsyncHttpClient client = new AsyncHttpClient();
        
        client.get(searchUrl, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray searchResultsJson = null;
                try {
                    searchResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    if (pageCount == 0) {
                        googleImageResults.clear();
                    }
                    aGoogleImageResults.addAll(GoogleImageResults.fromJSONArray(searchResultsJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Log.i("GoogleImageSearch", googleImageResults.toString());
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
    
    private static final int FETCH_SETTING = 101;
    
    private void onClickActionSettings() {
        
        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
        intent.putExtra("type", typeFilter == "Any" ? "Any" : typeFilter);
        intent.putExtra("size", sizeFilter == "Any" ? "Any": sizeFilter);
        intent.putExtra("color", colorFilter == "Any" ? "Any" : colorFilter);
        intent.putExtra("site", siteFilter == "Any" ? "nationalgeographic.com" : siteFilter);
        startActivityForResult(intent, FETCH_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FETCH_SETTING && resultCode == RESULT_OK) {
            typeFilter = data.getExtras().getString("type");
            sizeFilter = data.getExtras().getString("size");
            colorFilter = data.getExtras().getString("color");
            siteFilter = data.getExtras().getString("site");
            boolean isFilterSet = true;
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            onClickActionSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
       
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkStatus = (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting());
        
        if (!networkStatus) {
            Log.i("GoogleImageSearch", "No active network to get images..");
        }
        return networkStatus;
    }
    
}
