package com.example.cafeteria.Ui.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.ConnectivityManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cafeteria.Model.DeleteItemResponse;
import com.example.cafeteria.Ui.Activity.ItemsByCategoryId;
import com.example.cafeteria.Local_DB.LocalSession;
import com.example.cafeteria.Model.CategoriesModel;
import com.example.cafeteria.Model.CategoriesResponse;
import com.example.cafeteria.Model.EditItemResponse;
import com.example.cafeteria.Model.ItemByCategoryIdModel;
import com.example.cafeteria.Network.ApiClient;
import com.example.cafeteria.Network.RequestInterface;
import com.example.cafeteria.R;
import com.example.cafeteria.Utilty.Utility;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemByCategoryId_Adapter extends RecyclerView.Adapter<ItemByCategoryId_Adapter.ViewHolder> implements Filterable {

    private Context context;
    private List<ItemByCategoryIdModel> list;
    private List<ItemByCategoryIdModel> filter;

    public ItemByCategoryId_Adapter(List<ItemByCategoryIdModel> itemByCategoryIdModels, Context context) {
        this.context = context;
        this.list = itemByCategoryIdModels;
        filter = new ArrayList<>(itemByCategoryIdModels);
    }

    @NonNull
    @Override
    public ItemByCategoryId_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_item_by_categorie_id, parent, false);
        return new ItemByCategoryId_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemByCategoryId_Adapter.ViewHolder holder, int position) {
        holder.Tv_item_name.setText(list.get(position).getName());
        holder.Tv_item_price.setText(list.get(position).getPrice()+" ج.س");

    holder.edit_cardviwe.setOnClickListener(new View.OnClickListener() {
        Categories_spinner_Adapter categories_spinner_adapter;
        EditText ed_item_name, ed_item_price;
        ImageView img_cancle;
        Button edit_btn,btn_before;
        String item_name, item_price, categorie_id;
        Spinner spinner;
        ProgressBar progressBar;


        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view1 = ((Activity) context).getLayoutInflater().inflate(R.layout.custom_edit_item, null);

            spinner = view1.findViewById(R.id.categories_type);
            ed_item_name = view1.findViewById(R.id.item_name);
            ed_item_price = view1.findViewById(R.id.item_price);
            edit_btn = view1.findViewById(R.id.edit_btn);
            btn_before = view1.findViewById(R.id.edit_btn2);
            progressBar = view1.findViewById(R.id.prog);
            img_cancle = view1.findViewById(R.id.cancle);

            GetAllCategories();

            ed_item_name.setText(list.get(position).getName());
            ed_item_price.setText(list.get(position).getPrice().replaceAll(",",""));



            ed_item_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().length() > 0) {
                        ed_item_name.setBackgroundResource(R.drawable.custom_edittext_additems);
                    }

                }
                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            ed_item_price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().length() > 0) {
                        ed_item_price.setBackgroundResource(R.drawable.custom_edittext_additems);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            //<---EditText Hidden EditText Cursor When OnClick Done On Keyboard-->
            ed_item_price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId() == ed_item_price.getId()) {
                        ed_item_price.setCursorVisible(true);
                    }
                }
            });
            ed_item_price.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                    ed_item_price.setCursorVisible(false);
                    if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(ed_item_price.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return false;
                }
            });


            builder.setView(view1);
            final AlertDialog dialog = builder.create();
            InsetDrawable insetDrawable = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 20);
            dialog.getWindow().setBackgroundDrawable(insetDrawable);


            // <-- cancle dialog -->
            img_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   dialog.dismiss();
                }
            });


            // <--Spinner On Item Seleted -->
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    CategoriesModel categoriesModell = (CategoriesModel) adapterView.getItemAtPosition(i);
                    categorie_id = categoriesModell.getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



            edit_btn.setOnClickListener(v ->
            {
                item_name = ed_item_name.getText().toString().trim();
                item_price = ed_item_price.getText().toString().trim();

                if (Checked(item_name, item_price)) {
                    if (holder.connectivityManager.getActiveNetworkInfo() != null && holder.connectivityManager.getActiveNetworkInfo().isConnected()) {
                        dialog.dismiss();
                        //<--Hidden Keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager.isAcceptingText()) {
                            inputMethodManager.hideSoftInputFromWindow(((ItemsByCategoryId) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        EditItem(item_name, item_price, categorie_id);
                    } else {
                        Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.connect_internet), context);
                    }
                }
            });
            dialog.show();
        }


        // <-- Check Fields Function -->
        public Boolean Checked(String item_name, String price) {
            if (item_name.isEmpty())
            {
                ed_item_name.setBackgroundResource(R.drawable.custom_edittext_border_error);
                return false;
            }
            if (price.isEmpty())
            {
                ed_item_price.setBackgroundResource(R.drawable.custom_edittext_border_error);
                return false;
            }
            return true;
        }


        // <-- Edit Item
        public void EditItem(String item, String price, String categorie_id) {
            ProgressDialog loading = ProgressDialog.show(context, null, context.getString(R.string.wait), false, false);
            loading.setContentView(R.layout.custom_progressbar);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loading.setCancelable(false);
            loading.setCanceledOnTouchOutside(false);


            // <-- Connect WIth Network And Check Response Successful or Failure -- >
            final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
            Call<EditItemResponse> call = requestInterface.EditItem(list.get(position).getId(),item, price, categorie_id, "Bearer " + holder.localSession.getToken());
            call.enqueue(new Callback<EditItemResponse>() {
                @Override
                public void onResponse(Call<EditItemResponse> call, Response<EditItemResponse> response) {
                    if (response.isSuccessful())
                    {
                        if (!response.body().isError())
                        {
                           ((ItemsByCategoryId)context).GetItemByCategoryId(list.get(position).getCategory_id());
                           Toast.makeText(context, context.getResources().getString(R.string.successfulyy_edited), Toast.LENGTH_SHORT).show();
                           loading.dismiss();
                        }
                        else
                        {
                            loading.dismiss();
                            Utility.showAlertDialog(context.getString(R.string.error), response.body().getMessage_ar(), context);
                        }
                    } else
                        {
                        loading.dismiss();
                        Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.servererror), context);
                        }
                }

                @Override
                public void onFailure(Call<EditItemResponse> call, Throwable t) {
                    loading.dismiss();
                    Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.connect_internet_slow), context);

                }
            });
        }


        //<--  Git All Goldbars -->
        public void GetAllCategories() {
            // <-- Connect WIth Network And Check Response Successful or Failure -- >
            final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
            Call<CategoriesResponse> call = requestInterface.GetCategories_Spinner("Bearer " + holder.localSession.getToken());
            call.enqueue(new Callback<CategoriesResponse>() {
                @Override
                public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().isError()) {
                            categories_spinner_adapter = new Categories_spinner_Adapter(response.body().getCategoriesModelList(), context);
                            categories_spinner_adapter.notifyDataSetChanged();
                            spinner.setAdapter(categories_spinner_adapter);
                            spinner.setSelection(Integer.valueOf(list.get(position).getCategory_id())-1);
                            progressBar.setVisibility(View.INVISIBLE);
                            btn_before.setVisibility(View.INVISIBLE);
                            edit_btn.setVisibility(View.VISIBLE);

                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Utility.showAlertDialog(context.getString(R.string.error), response.body().getMessage_ar(), context);
                        }
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.servererror), context);
                    }
                }

                @Override
                public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.connect_internet_slow), context);

                }
            });

        }
     });


    holder.delete_cardview.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (holder.connectivityManager.getActiveNetworkInfo() != null && holder.connectivityManager.getActiveNetworkInfo().isConnected())
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view1 = ((Activity)context).getLayoutInflater().inflate(R.layout.custom_delete_item_massage, null);
                TextView item_delete_massage = view1.findViewById(R.id.item_delete_massage);
                Button confirm_btn = view1.findViewById(R.id.confirm_btn);
                Button cancle_btn = view1.findViewById(R.id.cancle_btn);
                ImageView image_cancle = view1.findViewById(R.id.cancle);
                item_delete_massage.setText(context.getResources().getString(R.string.item_delete_massage)+" "+ list.get(position).getName());
                builder.setView(view1);
                final AlertDialog dialog = builder.create();
                InsetDrawable insetDrawable = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), 20);
                dialog.getWindow().setBackgroundDrawable(insetDrawable);


                image_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                confirm_btn.setOnClickListener(v ->
                {
                    dialog.dismiss();
                    ProgressDialog loading = ProgressDialog.show(context, null, context.getString(R.string.wait), false, false);
                    loading.setContentView(R.layout.custom_progressbar);
                    loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    loading.setCancelable(false);
                    loading.setCanceledOnTouchOutside(false);

                    final RequestInterface requestInterface = ApiClient.getClient(ApiClient.BASE_URL).create(RequestInterface.class);
                    Call<DeleteItemResponse> call = requestInterface.DeleteItem(list.get(position).getId(), "Bearer " + holder.localSession.getToken());
                    call.enqueue(new Callback<DeleteItemResponse>() {
                        @Override
                        public void onResponse(Call<DeleteItemResponse> call, Response<DeleteItemResponse> response) {
                            if (response.isSuccessful()) {
                                if (!response.body().isError())
                                {

                                    ((ItemsByCategoryId) context).GetItemByCategoryId(list.get(position).getCategory_id());
                                    Toast.makeText(context, context.getResources().getString(R.string.successfulyy_deleteed) + "", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();

                                } else{
                                    Utility.showAlertDialog(context.getString(R.string.error), response.body().getMessage_ar(), context);
                                    loading.dismiss();
                                }
                            }
                            else
                            {
                                loading.dismiss();
                                Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.servererror), context);
                            }
                        }

                        @Override
                        public void onFailure(Call<DeleteItemResponse> call, Throwable t) {
                            loading.dismiss();
                            Utility.showAlertDialog(context.getString(R.string.error),context.getString(R.string.connect_internet_slow), context);
                        }
                    });
                });

                cancle_btn.setOnClickListener(v -> dialog.dismiss());
                dialog.show();
            }
            else
            {
                Utility.showAlertDialog(context.getString(R.string.error), context.getString(R.string.connect_internet), context);
            }
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
            List<ItemByCategoryIdModel> itemByCategoryIdModels = new ArrayList<>();
            if (key.isEmpty() || key.length() == 0)
            {
                itemByCategoryIdModels.addAll(filter);
            }
            else
            {
                for (ItemByCategoryIdModel item : list)
                {
                    if (item.getName().toLowerCase().contains(key)||item.getName().toUpperCase().contains(key)|| item.getPrice().toLowerCase().contains(key))
                    {
                        itemByCategoryIdModels.add(item);
                    }
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = itemByCategoryIdModels;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends ItemByCategoryIdModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Tv_item_name,Tv_item_price;
        CardView delete_cardview,edit_cardviwe;
        ConnectivityManager connectivityManager;
        Intent intent;
        String Categoryid ;
        LocalSession localSession;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Tv_item_name = itemView.findViewById(R.id.item_name);
            Tv_item_price = itemView.findViewById(R.id.item_price);
            edit_cardviwe = itemView.findViewById(R.id.edit_cardview);
            delete_cardview = itemView.findViewById(R.id.delete_cardview);
            intent = ((ItemsByCategoryId)context).getIntent();
            Categoryid = intent.getStringExtra("Category_id");
            connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
            localSession = new LocalSession(context);
        }
    }
}




