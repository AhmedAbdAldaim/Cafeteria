package com.example.cafeteria.Ui.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cafeteria.Model.CategoriesModel;
import com.example.cafeteria.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class Categories_spinner_Adapter extends ArrayAdapter<CategoriesModel> {

    private Context context;
    private List<CategoriesModel> list;
    private List<CategoriesModel> filter;

    public Categories_spinner_Adapter(List<CategoriesModel> categoriesModels, Context context) {
        super(context,0,categoriesModels);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return init(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return init(position, convertView, parent);
    }

    private View init(int pos, View view, ViewGroup viewGroup){
        if(view==null){
            view =LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner,viewGroup,false);
        }
        TextView categorie_name = view.findViewById(R.id.categories);

        CategoriesModel categoriesModel =getItem(pos);
        if(categoriesModel!=null) {
            categorie_name.setText(categoriesModel.getName());
        }
        return view;
    }
}




