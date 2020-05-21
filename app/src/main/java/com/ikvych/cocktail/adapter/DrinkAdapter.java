package com.ikvych.cocktail.adapter;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ikvych.cocktail.R;
import com.ikvych.cocktail.model.Drink;

import java.util.List;
import java.util.zip.Inflater;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    private List<Drink> drinkList;
    private Context context;

    public DrinkAdapter(Context context, List<Drink> drinkList) {
        this.context = context;
        this.drinkList = drinkList;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drink_list_item, parent, false);
        return new DrinkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
        Drink drink = drinkList.get(position);

        holder.drinkName.setText(drink.getStrDrink());
        String imagePath = drink.getStrDrinkThumb();
        Glide.with(context)
                .load(imagePath)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    public class DrinkViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView drinkName;

        public DrinkViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.drinkImageView);
            drinkName = itemView.findViewById(R.id.drinkName);
        }
    }

    public void setDrinkList(List<Drink> drinkList) {
        this.drinkList = drinkList;
        notifyDataSetChanged();
    }
}
