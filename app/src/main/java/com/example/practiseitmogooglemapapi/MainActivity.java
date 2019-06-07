package com.example.practiseitmogooglemapapi;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiActivity;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private static final int ERROR_DIALOG_REQUEST = 9001;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (isServiceReady()) {
      inti();
    }
  }

  private void inti() {
    Button button = (Button) findViewById(R.id.start_button);
    Log.d(TAG, "Is in button method");
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);
      }
    });
  }

  public boolean isServiceReady() {
    Log.d(TAG, "isServiceReady: Checking google services version");

    int available = GoogleApiAvailability.getInstance()
        .isGooglePlayServicesAvailable(MainActivity.this);
    if (available == ConnectionResult.SUCCESS) {
      Log.d(TAG, "isServiceReady: google services can be used");
      return true;
    } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
      Log.d(TAG, "isServiceReady: Bad version of google services");
      Dialog dialog = GoogleApiAvailability.getInstance()
          .getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
      dialog.show();
    } else {
      Toast.makeText(this, "You cant use Google maps", Toast.LENGTH_SHORT).show();
    }
    return false;
  }
}
