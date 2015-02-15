package com.bootcamp.amberved.googleimagesearch.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.bootcamp.amberved.googleimagesearch.R;


public class SettingActivity extends ActionBarActivity {

    private Button submit;
    private Spinner image;
    private Spinner color;
    private Spinner imageSize;
    private EditText siteFilter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Bundle extras = getIntent().getExtras();
        submit = (Button) findViewById(R.id.btn_submit);

        image = (Spinner) findViewById(R.id.image_type_spinner);
        imageSize = (Spinner) findViewById(R.id.image_size_spinner);
        color = (Spinner) findViewById(R.id.color_filter_spinner);
        siteFilter = (EditText) findViewById(R.id.et_site_filter);

        String siteFilterFromIntent = extras.getString("site");

        setDefaultSelection(image, extras.getString("type"));
        setDefaultSelection(imageSize, extras.getString("size"));
        setDefaultSelection(color, extras.getString("color"));

        if (siteFilterFromIntent == null) {
            siteFilter.setText("nationalgeographic.com");
        } else {
            siteFilter.setText(siteFilterFromIntent);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("type", image.getSelectedItem().toString());
                intent.putExtra("size", imageSize.getSelectedItem().toString());
                intent.putExtra("color", color.getSelectedItem().toString());
                intent.putExtra("site", siteFilter.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }
    
    private void setDefaultSelection(Spinner spinner, String defaultValue) {
        if (defaultValue == null) {
            return;
        }
        for (int i = 0; i < spinner.getCount(); i ++ ) {
            String option = (String) spinner.getItemAtPosition(i);
            if (option.equals(defaultValue)) {
                spinner.setSelection(i);
            }
        }
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
