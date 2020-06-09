package com.ikvych.cocktail.util;

import android.app.Activity;
import android.view.View;

import com.ikvych.cocktail.R;

public class ActivityUtil {

    public static void setSearchEmptyListVisible(Activity activity) {
        activity.findViewById(R.id.empty_list_id).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.empty_search_id).setVisibility(View.GONE);
        activity.findViewById(R.id.search_recycler_view).setVisibility(View.GONE);
    }

    public static void setSearchRecyclerViewVisible(Activity activity) {
        activity.findViewById(R.id.empty_list_id).setVisibility(View.GONE);
        activity.findViewById(R.id.empty_search_id).setVisibility(View.GONE);
        activity.findViewById(R.id.search_recycler_view).setVisibility(View.VISIBLE);
    }

    public static void setEmptySearchVisible(Activity activity) {
        activity.findViewById(R.id.empty_list_id).setVisibility(View.GONE);
        activity.findViewById(R.id.empty_search_id).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.search_recycler_view).setVisibility(View.GONE);
    }

    public static void setDbRecyclerViewVisible(Activity activity) {
        activity.findViewById(R.id.empty_history).setVisibility(View.GONE);
        activity.findViewById(R.id.db_recycler_view).setVisibility(View.VISIBLE);
    }

    public static void setDbEmptyHistoryVisible(Activity activity) {
        activity.findViewById(R.id.empty_history).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.db_recycler_view).setVisibility(View.GONE);
    }
}
