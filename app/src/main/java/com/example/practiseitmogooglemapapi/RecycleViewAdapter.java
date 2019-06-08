package com.example.practiseitmogooglemapapi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

  private ArrayList<String> mImageName = new ArrayList<>();
  private ArrayList<String> mImage = new ArrayList<>();
  private Context mContext;

  public RecycleViewAdapter(ArrayList<String> ImageName, ArrayList<String> Image, Context Context) {
    mImageName = ImageName;
    mImage = Image;
    mContext = Context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.layout_kistitem, viewGroup, false);
    ViewHolder viewHolder = new ViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder viewHolder, int i) {
    Glide.with(mContext).asBitmap().load(mImage.get(i)).into(viewHolder.circleImageView);
    viewHolder.textView.setText(mImageName.get(i));
    viewHolder.relativeLayout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(mContext, "weq", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public int getItemCount() {
    return mImageName.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    CircleImageView circleImageView;
    TextView textView;
    RelativeLayout relativeLayout;

    public ViewHolder(View itemView) {
      super(itemView);
      circleImageView = itemView.findViewById(R.id.image);
      textView = itemView.findViewById(R.id.image_name);
      relativeLayout = itemView.findViewById(R.id.parent_layout);
    }
  }

}
