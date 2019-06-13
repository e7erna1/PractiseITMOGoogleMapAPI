package com.example.practiseitmogooglemapapi;

import static com.example.practiseitmogooglemapapi.MapActivity.gMap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

  private ArrayList<String> mImageName;
  private ArrayList<String> mImage;
  private ArrayList<Double> mlatitude;
  private ArrayList<Double> mlongitude;
  private Context mContext;
  private MapActivity mmapActivity;

  RecycleViewAdapter(ArrayList<String> ImageName, ArrayList<String> Image, Context Context,
      ArrayList<Double> latitude, ArrayList<Double> longitude, MapActivity mapActivity) {
    mImageName = ImageName;
    mImage = Image;
    mContext = Context;
    mlatitude = latitude;
    mlongitude = longitude;
    mmapActivity = mapActivity;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.layout_kistitem, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
    Glide.with(mContext).asBitmap().load(mImage.get(i)).into(viewHolder.circleImageView);
    viewHolder.textView.setText(mImageName.get(i));
    viewHolder.relativeLayout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(mContext, "The map is on: " + mImageName.get(i), Toast.LENGTH_SHORT).show();
        gMap.moveCamera(CameraUpdateFactory
            .newLatLngZoom(new LatLng(mlatitude.get(i), mlongitude.get(i)), (float) 15.0));
        mImageName.clear();
        mImage.clear();
        mlatitude.clear();
        mlongitude.clear();
        mmapActivity.recyclerView.setVisibility(View.GONE);
        mmapActivity.initRecycleView();
      }
    });
  }

  @Override
  public int getItemCount() {
    return mImageName.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    CircleImageView circleImageView;
    TextView textView;
    RelativeLayout relativeLayout;

    ViewHolder(View itemView) {
      super(itemView);
      circleImageView = itemView.findViewById(R.id.image);
      textView = itemView.findViewById(R.id.image_name);
      relativeLayout = itemView.findViewById(R.id.parent_layout);
    }
  }

}
