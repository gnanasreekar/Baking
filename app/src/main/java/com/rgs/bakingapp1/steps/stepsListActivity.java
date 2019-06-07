package com.rgs.bakingapp1.steps;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rgs.bakingapp1.POJO;
import com.rgs.bakingapp1.R;
import com.rgs.bakingapp1.widget.NewAppWidget;

import java.util.ArrayList;


public class stepsListActivity extends AppCompatActivity {

    TextView ingeridents;
    private boolean mTwoPane;
    private ArrayList<POJO.StepsBean> stepsBeans;
    ArrayList<POJO.IngredientsBean> ingredientsBeans;
    ArrayList<POJO> pojos;
    Intent intent;
    StringBuilder all;
    private static stepsDetailFragment stepsDetailFragment;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        stepsBeans = getIntent().getParcelableArrayListExtra("stepsList");
        ingredientsBeans = getIntent().getParcelableArrayListExtra("ingredientsList");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_list);
        ingeridents = findViewById(R.id.ingeridents);
        sharedPreferences = getSharedPreferences("myfile",MODE_PRIVATE);
        //Ingeridents
        all = new StringBuilder();
        for (int i=0; i<ingredientsBeans.size(); i++) {
            String quantity = ingredientsBeans.get(i).getQuantity();
            String measure = ingredientsBeans.get(i).getMeasure();
            String ingredient = ingredientsBeans.get(i).getIngredient();

            if (i < (ingredientsBeans.size()-1)) {
                all.append(quantity).append(" ").append(measure).append("   ").append(ingredient).append("\n");
            } else {
                all.append(quantity).append(" ").append(measure).append("   ").append(ingredient);
            }
        }
        String inger = all.toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ALL",inger);
        editor.apply();
        ingeridents.setText(inger);

        Intent intent = new Intent(this, NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(
                new ComponentName(getApplication(), NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);



        if (findViewById(R.id.steps_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.steps_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, stepsBeans, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final stepsListActivity mParentActivity;
        ArrayList<POJO.StepsBean> stepsBeans;
        private final boolean mTwoPane;


        SimpleItemRecyclerViewAdapter(stepsListActivity parent, ArrayList<POJO.StepsBean> items, boolean twoPane) {
            stepsBeans = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }



        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            POJO.StepsBean stepsBean = stepsBeans.get(position);
            String step = stepsBean.getShortDescription();
            holder.pos(step);



        }

        @Override
        public int getItemCount() {
            return stepsBeans.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);

                mContentView = (TextView) view.findViewById(R.id.content);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        int pos=getAdapterPosition();
                        stepsDetailFragment = new stepsDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("v1" , stepsBeans.get(pos).getDescription());
                        bundle.putString("v2" , stepsBeans.get(pos).getVideoURL() );
                        if (mTwoPane) {
                            stepsDetailFragment fragment = new stepsDetailFragment();
                            fragment.setArguments(bundle);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.steps_detail_container, fragment)
                                    .commit();
                        } else {
                            Context context = view.getContext();
                            Intent intent = new Intent(context, stepsDetailActivity.class);
                            intent.putExtras(bundle);

                            context.startActivity(intent);
                        }

                    }
                });
            }
            void pos(String pos)
            {
                mContentView.setText(pos);
            }
        }



    }
}

