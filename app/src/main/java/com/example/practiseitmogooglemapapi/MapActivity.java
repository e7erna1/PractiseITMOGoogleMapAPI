package com.example.practiseitmogooglemapapi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

  private static final String TAG = "MapActivity";
  private static boolean access = false;
  private static final int acessCode = 1234;
  private static GoogleMap gMap;
  private static FusedLocationProviderClient fusedLocationProviderClient;
  public static Double myLatitude = null;
  public static Double myLongitude = null;
  public static LatLng myLatlng = null;

  Button markBt;
  Button geoLocationBt;
  Button satView;
  Button clear;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);
    getLocationAcess();

//    markBt = (Button) findViewById(R.id.btMark);
//    markBt.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        LatLng myLocation = new LatLng(myLatitude, myLongitude);
//        gMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
//      }
//    });

//    geoLocationBt = (Button) findViewById(R.id.btSearch);
//    geoLocationBt.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        EditText searchText = (EditText) findViewById(R.id.etLocationEntry);
//        String search = searchText.getText().toString();
//        if (search != null && !search.equals("")) {
//          List<Address> addressList = null;
//          Geocoder geocoder = new Geocoder(MapActivity.this);
//          try {
//            addressList = geocoder.getFromLocationName(search, 1);
//          } catch (IOException e) {
//            e.printStackTrace();
//          }
//          Address address = addressList.get(0);
//          LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//          gMap.addMarker(new MarkerOptions().position(latLng).title("from geocoder"));
//          gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        }
//      }
//    });

//    clear = (Button) findViewById(R.id.btClear);
//    clear.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        gMap.clear();
//      }
//    });

  }

  private void initMap() {
    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    assert supportMapFragment != null;
    supportMapFragment.getMapAsync(MapActivity.this);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    gMap = googleMap;
    Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
    //Current location
    if (access) {
      getDeviceLocation();
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
          != PackageManager.PERMISSION_GRANTED
          && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
          != PackageManager.PERMISSION_GRANTED) {
        return;
      }
      gMap.setMyLocationEnabled(true);
    }

//    gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//      @Override
//      public void onMapClick(LatLng latLng) {
//        gMap.addMarker(new MarkerOptions().position(latLng).title("from onMapClick"));
//        gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        Log.d(TAG, new MarkerOptions().position(latLng).toString());
//      }
//    });

//    markBt = (Button) findViewById(R.id.btMark);
//    markBt.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        LatLng myLocation = new LatLng(myLatitude, myLongitude);
//        gMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
//      }
//    });

    gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
      @Override
      public void onMapClick(LatLng latLng) {
         myLatlng = latLng;
        Log.d(TAG, new MarkerOptions().position(latLng).toString());
      }
    });

    markBt = (Button) findViewById(R.id.btMark);
    markBt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        LatLng myLocation = myLatlng;
        gMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
      }
    });


  }

  private void getLocationAcess() {
    String[] acess = {
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    };
    if (ContextCompat
        .checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
          Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        access = true;
        initMap();
      } else {
        ActivityCompat.requestPermissions(this, acess, acessCode);
      }
    } else {
      ActivityCompat.requestPermissions(this, acess, acessCode);
    }
  }

  private void getDeviceLocation() {
    Log.d(TAG, "get device location: getting current device location");
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    try {
      if (access) {
        final Task tasks = fusedLocationProviderClient.getLastLocation();
        tasks.addOnCompleteListener(new OnCompleteListener() {
          @Override
          public void onComplete(@NonNull Task task) {
            if (task.isSuccessful()) {
              Log.d(TAG, "get device location: found location");
              Location location = (Location) task.getResult();
              if (location != null) {
                moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), 15f);
              }
            } else {
              Log.d(TAG, "get device location: cant find location");
              Toast.makeText(MapActivity.this, "Cant find current location", Toast.LENGTH_SHORT)
                  .show();
            }
          }
        });
      }
    } catch (SecurityException securetyException) {
      Log.e(TAG, "get device location: exception" + securetyException.getMessage());
    }
  }

  private void moveCamera(LatLng latLng, float zoom) {
    Log.d(TAG, "Moving camera to");
    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    access = false;
    switch (requestCode) {
      case acessCode: {
        if (grantResults.length > 0) {
          for (int ints : grantResults) {
            if (ints != PackageManager.PERMISSION_GRANTED) {
              access = false;
              return;
            }
          }
          access = true;
          initMap();
        }
      }
    }
  }
}
