package com.ikvych.cocktail.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ikvych.cocktail.R;
import com.ikvych.cocktail.databinding.DrinkListItemBinding;
import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.view.DrinkDetails;

import java.util.ArrayList;
import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    private List<Drink> drinkList;
    private Context context;
    private String activityName;

    public DrinkAdapter(Context context, String activityName) {
        this.context = context;
        drinkList = new ArrayList<>();
        this.activityName = activityName;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DrinkListItemBinding drinkListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.drink_list_item,
                parent,
                false
        );

        return new DrinkViewHolder(drinkListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
        Drink drink = drinkList.get(position);
        holder.drinkListItemBinding.setDrink(drink);
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }


    public class DrinkViewHolder extends RecyclerView.ViewHolder {

        private DrinkListItemBinding drinkListItemBinding;

        public DrinkViewHolder(@NonNull DrinkListItemBinding drinkListItemBinding) {
            super(drinkListItemBinding.getRoot());
            this.drinkListItemBinding = drinkListItemBinding;

            drinkListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        Drink drink = drinkList.get(position);
                        Intent intent = new Intent(context, DrinkDetails.class);
                        switch (activityName) {
                            case "main":
                                intent.putExtra("viewModelType", "main");
                                break;
                            case "search":
                                intent.putExtra("viewModelType", "search");
                                break;
                        }
                        intent.putExtra("drink", drink);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public void setDrinkList(List<Drink> drinkList) {
        this.drinkList = drinkList;
        notifyDataSetChanged();
    }

    public List<Drink> getDrinkList() {
        return drinkList;
    }
}
