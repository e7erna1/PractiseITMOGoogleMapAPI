package com.example.practiseitmogooglemapapi;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

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
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);
      }
    });
  }

  public boolean isServiceReady() {
    int available = GoogleApiAvailability.getInstance()
        .isGooglePlayServicesAvailable(MainActivity.this);
    if (available == ConnectionResult.SUCCESS) {
      return true;
    } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
      Dialog dialog = GoogleApiAvailability.getInstance()
          .getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
      dialog.show();
    } else {
      Toast.makeText(this, "You cant use Google maps", Toast.LENGTH_SHORT).show();
    }
    return false;
  }
}
