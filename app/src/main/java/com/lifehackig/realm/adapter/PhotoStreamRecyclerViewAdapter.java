package com.lifehackig.realm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lifehackig.realm.R;
import com.lifehackig.realm.model.Photo;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Sheena on 1/27/17.
 */

public class PhotoStreamRecyclerViewAdapter extends RealmRecyclerViewAdapter<Photo,
        PhotoStreamRecyclerViewAdapter.MyViewHolder> {

    private final Context context;

    public PhotoStreamRecyclerViewAdapter(Context context, OrderedRealmCollection<Photo> data) {
        super(context, data, true);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Photo photo = getData().get(position);
        byte[] byteArray = photo.getImage();
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        holder.imageView.setImageBitmap(bmp);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }

}
