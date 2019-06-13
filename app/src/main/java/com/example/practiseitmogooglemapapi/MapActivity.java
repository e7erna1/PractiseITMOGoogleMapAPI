package com.example.practiseitmogooglemapapi;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

  private static boolean access = false;
  private static final int acessCode = 1234;
  static GoogleMap gMap;
  private static LatLng myLatlng = null;

  private ArrayList<String> mImageURLs = new ArrayList<>();
  private ArrayList<String> mImages = new ArrayList<>();
  private ArrayList<Double> mLatitude = new ArrayList<>();
  private ArrayList<Double> mLongitude = new ArrayList<>();


  private EditText editText;
  private EditText scText;
  private MyDataBase myDataBase;

  RecyclerView recyclerView;

  private SparseArray<Double> sparseArray1 = new SparseArray<>();
  private SparseArray<Double> sparseArray2 = new SparseArray<>();
  private SparseArray<String> sparseArray3 = new SparseArray<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);
    getLocationAcess();
  }

  private void initImage(String name, double myLatitude, double myLongitude) {
    final String url = "https://banner2.kisspng.com/20180405/xwe/kisspng-google-map-maker-google-maps-computer-icons-map-co-map-marker-5ac6446cc7bc26.3264700815229430848181.jpg";
    mImageURLs.add(url);
    mImages.add(name);
    mLatitude.add(myLatitude);
    mLongitude.add(myLongitude);
    initRecycleView();
  }

  void initRecycleView() {
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
    RecycleViewAdapter adapter = new RecycleViewAdapter(mImages, mImageURLs, this, mLatitude,
        mLongitude, this);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    LatLng latLng;
    myDataBase = new MyDataBase(this);
    SQLiteDatabase sqLiteDatabase = myDataBase.getWritableDatabase();
    final Cursor cursor = sqLiteDatabase
        .query(MyDataBase.TABLE_NAME, null, null, null, null, null, null);
    if (cursor.moveToFirst()) {
      int latitude = cursor.getColumnIndex(MyDataBase.KEY_LATITUDE);
      int longitude = cursor.getColumnIndex(MyDataBase.KEY_LONGITUDE);
      int Snippet = cursor.getColumnIndex(MyDataBase.KEY_SNIPPET);
      do {
        latLng = new LatLng(cursor.getDouble(latitude), cursor.getDouble(longitude));
        gMap.addMarker(new MarkerOptions().position(latLng).title("New Marker.")
            .snippet(cursor.getString(Snippet)));
      } while (cursor.moveToNext());
      cursor.close();
    }

    gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
      @Override
      public void onMapClick(LatLng latLng) {
        myLatlng = latLng;
      }
    });
    Button markBt = (Button) findViewById(R.id.btMark);
    editText = (EditText) findViewById(R.id.editText);
    markBt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        SQLiteDatabase sqLiteDatabase = myDataBase.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        gMap.addMarker(new MarkerOptions().position(myLatlng).title("This is new Marker.")
            .snippet(editText.getText().toString()));
        contentValues.put(MyDataBase.KEY_LATITUDE, myLatlng.latitude);
        contentValues.put(MyDataBase.KEY_LONGITUDE, myLatlng.longitude);
        contentValues.put(MyDataBase.KEY_SNIPPET, editText.getText().toString());
        sqLiteDatabase.insert(MyDataBase.TABLE_NAME, null, contentValues);
        editText.setText("");
      }
    });

    Button SCbutton = (Button) findViewById(R.id.SCbutton);
    scText = (EditText) findViewById(R.id.scText);
    SCbutton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        SQLiteDatabase sqLiteDatabase = myDataBase.getWritableDatabase();
        String scSnipper = "%" + scText.getText().toString() + "%";

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        recyclerView.setVisibility(View.VISIBLE);

        sparseArray1.clear();
        sparseArray2.clear();
        sparseArray3.clear();

        mImageURLs.clear();
        mImages.clear();
        mLatitude.clear();
        mLongitude.clear();
        initRecycleView();

        Cursor cursor1 = sqLiteDatabase
            .rawQuery("SELECT * FROM 'MainTable' WHERE Snippet LIKE  ?", new String[]{scSnipper});
        if (cursor1 != null) {
          try {
            if (cursor1.moveToFirst()) {
              int _id = cursor1.getColumnIndex(MyDataBase.KEY_ID);
              int latitude = cursor1.getColumnIndex(MyDataBase.KEY_LATITUDE);
              int longitude = cursor1.getColumnIndex(MyDataBase.KEY_LONGITUDE);
              int Snippet = cursor1.getColumnIndex(MyDataBase.KEY_SNIPPET);
              do {
                sparseArray1.put(cursor1.getInt(_id), cursor1.getDouble(latitude));
                sparseArray2.put(cursor1.getInt(_id), cursor1.getDouble(longitude));
                sparseArray3.put(cursor1.getInt(_id), cursor1.getString(Snippet));
              } while (cursor1.moveToNext());
            }
          } finally {
            cursor1.close();
          }
        }
        for (int i = 0; i < sparseArray1.size(); i++) {
          int key = sparseArray1.keyAt(i);
          initImage(sparseArray3.get(key), sparseArray1.get(key), sparseArray2.get(key));
        }
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
    FusedLocationProviderClient fusedLocationProviderClient = LocationServices
        .getFusedLocationProviderClient(this);
    try {
      if (access) {
        final Task tasks = fusedLocationProviderClient.getLastLocation();
        tasks.addOnCompleteListener(new OnCompleteListener() {
          @Override
          public void onComplete(@NonNull Task task) {
            if (task.isSuccessful()) {
              Location location = (Location) task.getResult();
              if (location != null) {
                moveCamera(new LatLng(location.getLatitude(), location.getLongitude()));
              }
            } else {
              Toast.makeText(MapActivity.this, "Cant find current location", Toast.LENGTH_SHORT)
                  .show();
            }
          }
        });
      }
    } catch (SecurityException ignored) {
    }
  }

  private void moveCamera(LatLng latLng) {
    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 15.0));
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    access = false;
    if (requestCode == acessCode) {
      if (grantResults.length > 0) {
        for (int ints : grantResults) {
          if (ints != PackageManager.PERMISSION_GRANTED) {
            return;
          }
        }
        access = true;
        initMap();
      }
    }
  }
}
