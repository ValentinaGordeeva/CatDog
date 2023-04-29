package com.example.catdog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

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
    @Override
    public int getItemCount() {
        return animals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvType;
        public TextView tvAge;
        public TextView tvWeight;
        // private TextView ivPhoto;
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

              if (animal.getImage() != null) {
                imageView.setImageBitmap(animal.getImage());
            } else {
                // здесь можно установить другое изображение-заполнитель
                imageView.setImageResource(R.drawable.cat);
            }
            /*
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(animal.getImageUrl());

            // Загружаем изображение из Firebase Storage в ImageView с помощью Glide
            Glide.with(itemView.getContext())
                    .load(storageRef)
                    .into(imageView);
                    */

            tvName.setText(animal.getName());
            tvType.setText(animal.getType());
            tvAge.setText("Возраст: " + animal.getAge());
            tvWeight.setText("Вес: " + animal.getWeight() + " кг");
        }
    }
}


