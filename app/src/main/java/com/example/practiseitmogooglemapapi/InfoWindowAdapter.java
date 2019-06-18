package com.example.practiseitmogooglemapapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

  private final View mWindow;
  private Context context;

  public InfoWindowAdapter(Context context) {
    this.mWindow = LayoutInflater.from(context).inflate(R.layout.custom_marker, null);
    this.context = context;
  }

  private void rendomWindowText(Marker marker, View view) {
    String title = marker.getTitle();
    TextView textView = view.findViewById(R.id.title);
    if (!title.equals("")) {
      textView.setText(title);
    }

    String snippet = marker.getSnippet();
    TextView Tsnippet = view.findViewById(R.id.snippet);
    if (!snippet.equals("")) {
      Tsnippet.setText(snippet);
    }
  }

  @Override
  public View getInfoWindow(Marker marker) {
    rendomWindowText(marker, mWindow);
    return mWindow;
  }

  @Override
  public View getInfoContents(Marker marker) {
    rendomWindowText(marker, mWindow);
    return mWindow;
  }
}
