package com.example.catdog;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.bumptech.glide.Glide;

import java.io.ObjectInputStream;
import java.text.BreakIterator;
import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {
    private List<Animal> animals;

    public AnimalAdapter(List<Animal> animals) {
        this.animals = animals;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Animal animal = animals.get(position);
        holder.bind(animal);

    }
    public int getItemCount() {
        return animals.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvType;
        public TextView tvAge;
        public TextView tvWeight;
               private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_animal_name);
            tvType = itemView.findViewById(R.id.tv_animal_type);
            tvAge = itemView.findViewById(R.id.tv_animal_age);
            tvWeight = itemView.findViewById(R.id.tv_animal_weight);
            imageView = itemView.findViewById(R.id.tv_imageView);
        }

        public void bind(Animal animal) {
            Log.d("AnimalAdapter", "imageURL = " + animal.getImageUrl());
            tvName.setText("Кличка:" + animal.getName());
            tvType.setText("Тип: "+ animal.getType());
            tvAge.setText("Возраст: " + animal.getAge());
            tvWeight.setText("Вес: " + animal.getWeightFormatted() + " кг");
            if (animal.getImageUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(animal.getImageUrl())
                        .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.nophoto);
            }


        }
    }
}


