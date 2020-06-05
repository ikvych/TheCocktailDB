package com.ikvych.cocktail.ect.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ikvych.cocktail.R;
import com.ikvych.cocktail.databinding.DrinkListItemBinding;
import com.ikvych.cocktail.data.entity.Drink;
import com.ikvych.cocktail.ui.activity.DrinkDetails;

import java.util.ArrayList;
import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    public static final String VIEW_MODEL_TYPE = "viewModelType";
    public static final String MAIN_MODEL_TYPE = "main";
    public static final String SEARCH_MODEL_TYPE = "search";
    public static final String DRINK = "drink";

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


    class DrinkViewHolder extends RecyclerView.ViewHolder {

        private DrinkListItemBinding drinkListItemBinding;

        DrinkViewHolder(@NonNull DrinkListItemBinding drinkListItemBinding) {
            super(drinkListItemBinding.getRoot());
            this.drinkListItemBinding = drinkListItemBinding;

            drinkListItemBinding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    Drink drink = drinkList.get(position);
                    Intent intent = new Intent(context, DrinkDetails.class);
                    switch (activityName) {
                        case MAIN_MODEL_TYPE:
                            intent.putExtra(VIEW_MODEL_TYPE, MAIN_MODEL_TYPE);
                            break;
                        case SEARCH_MODEL_TYPE:
                            intent.putExtra(VIEW_MODEL_TYPE, SEARCH_MODEL_TYPE);
                            break;
                    }
                    intent.putExtra(DRINK, drink);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void setDrinkList(List<Drink> drinkList) {
        this.drinkList = drinkList;
        notifyDataSetChanged();
    }
}
