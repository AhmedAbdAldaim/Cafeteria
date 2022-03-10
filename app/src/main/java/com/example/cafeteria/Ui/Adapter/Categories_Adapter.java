package com.example.cafeteria.Ui.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cafeteria.Local_DB.LocalSession;
import com.example.cafeteria.Ui.Activity.ItemsByCategoryId;
import com.example.cafeteria.Model.CategoriesModel;
import com.example.cafeteria.R;
import com.example.cafeteria.Ui.Fragment.HomeFragment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Categories_Adapter extends RecyclerView.Adapter<Categories_Adapter.ViewHolder> implements Filterable {

    private static Context context;
    private List<CategoriesModel> list;
    private List<CategoriesModel> filter;
    HomeFragment homeFragment;


    public Categories_Adapter(List<CategoriesModel> categoriesModels, Context context) {
        this.context = context;
        this.list = categoriesModels;
        filter = new ArrayList<>(categoriesModels);
        homeFragment = new HomeFragment();
    }

    @NonNull
    @Override
    public Categories_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_categories, parent, false);
        return new Categories_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Categories_Adapter.ViewHolder holder, int position) {
        holder.Tv_categorie_name.setText(list.get(position).getName());
        holder.Tv_Items_count.setText(list.get(position).getItems_count());

        if(HomeFragment.tv_categories.getText().toString().equals(context.getString(R.string.expirt) + " " + holder.localSession.getExpirDate()+ " " + context.getString(R.string.expirt1))){
          holder.Tv_categorie_name1.setText(list.get(position).getName());
          holder.Tv_Items_count1.setText(list.get(position).getItems_count());
          holder.cardView.setVisibility(View.INVISIBLE);
          holder.cardView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Toast.makeText(context, R.string.msg_card, Toast.LENGTH_SHORT).show();
                  holder.cardView.setEnabled(false);
              }
          });
            holder.cardview1.setVisibility(View.VISIBLE);
            holder.cardview1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, R.string.msg_card, Toast.LENGTH_SHORT).show();
                    holder.cardview1.setEnabled(false);
                }
            });
          HomeFragment.floatingActionButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Toast.makeText(context, R.string.msg_card, Toast.LENGTH_SHORT).show();
                  HomeFragment.floatingActionButton.setEnabled(false);
              }
          });
        }



        // < -- Intent To Itemes -->
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ItemsByCategoryId.class).putExtra("Category_id",list.get(position).getId()).putExtra("item_name",list.get(position).getName()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // <-- Search -->
    @Override
    public Filter getFilter() {
        return filterr;
    }

    public Filter filterr = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String key = charSequence.toString();
            List<CategoriesModel> categoriesModels = new ArrayList<>();
            if (key.isEmpty() || key.length() == 0)
            {
                categoriesModels.addAll(filter);
            }
            else
            {
                for (CategoriesModel item : list)
                {
                    if (item.getName().toLowerCase().contains(key)||item.getName().toUpperCase().contains(key))
                    {
                        categoriesModels.add(item);
                    }
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = categoriesModels;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends CategoriesModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };

 class ViewHolder extends RecyclerView.ViewHolder {
        TextView Tv_categorie_name,Tv_categorie_name1,Tv_Items_count,Tv_Items_count1;
        ConnectivityManager connectivityManager;
        CardView cardView,cardview1;
        HomeFragment homeFragment;
        LocalSession localSession;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Tv_categorie_name = itemView.findViewById(R.id.categorie_name);
            Tv_categorie_name1 = itemView.findViewById(R.id.categorie_name1);
            Tv_Items_count = itemView.findViewById(R.id.items_count);
            Tv_Items_count1 = itemView.findViewById(R.id.items_count1);
            cardView = itemView.findViewById(R.id.card);
            cardview1 = itemView.findViewById(R.id.card1);
            connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
            homeFragment = new HomeFragment();
            localSession = new LocalSession(context);

        }
    }
}




