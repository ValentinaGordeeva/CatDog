package com.example.catdog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.bumptech.glide.Glide;

import java.io.ObjectInputStream;
import java.text.BreakIterator;
import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {
    private List<Animal> animals;


    // private List<Animal> mAnimals;

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
      /*  Animal animal = animals.get(position);
        Log.d("AnimalAdapter", "Name: " + animal.getName());
        Log.d("AnimalAdapter", "Type: " + animal.getType());
        Log.d("AnimalAdapter", "Age: " + animal.getAge());
        Log.d("AnimalAdapter", "Weight: " + animal.getWeight());
        // Log.d("AnimalAdapter", "Photo URL: " + animal.getPhotoUrl());

        holder.tvName.setText("Кличка: " + animal.getName());
        holder.tvType.setText("Тип: " + animal.getType());
        holder.tvAge.setText("Возраст: " + animal.getAge());
        holder.tvWeight.setText("Вес: " + animal.getWeight());
        holder.ivPhoto.setText("Фото" + animal.getImage());
      /*  Glide.with(holder.itemView.getContext())
                .load(animal.getPhotoUrl())
                .into(holder.ivPhoto);
        Picasso.get().load(animal.getPhotoUrl()).into(holder.ivPhoto);
*/
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
        private TextView ivPhoto;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_animal_name);
            tvType = itemView.findViewById(R.id.tv_animal_type);
            tvAge = itemView.findViewById(R.id.tv_animal_age);
            tvWeight = itemView.findViewById(R.id.tv_animal_weight);
            imageView = itemView.findViewById(R.id.ivPhoto);
        }

        public void bind(Animal animal) {
            imageView.setImageBitmap(animal.getImage());
            tvName.setText(animal.getName());
            tvType.setText(animal.getType());
            tvAge.setText("Age: " + animal.getAge());
            tvWeight.setText("Weight: " + animal.getWeight() + " kg");
        }
    }
}


