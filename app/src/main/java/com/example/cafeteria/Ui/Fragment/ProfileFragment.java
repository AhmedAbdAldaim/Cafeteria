package com.example.cafeteria.Ui.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cafeteria.Ui.Activity.ThePrivate;
import com.example.cafeteria.Ui.Activity.AboutApp;
import com.example.cafeteria.Ui.Activity.EditProfile;
import com.example.cafeteria.Local_DB.LocalSession;
import com.example.cafeteria.Model.ListviewProfileModel;
import com.example.cafeteria.Model.LogoutResponse;
import com.example.cafeteria.Network.ApiClient;
import com.example.cafeteria.Network.RequestInterface;
import com.example.cafeteria.R;
import com.example.cafeteria.Ui.Activity.Login;
import com.example.cafeteria.Ui.Activity.MainActivity;
import com.example.cafeteria.Ui.Adapter.ListviewProfileAdapter;
import com.example.cafeteria.Utilty.Utility;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    ListView listView;
    LocalSession localSession;
    TextView tv_username;
    CardView cardView_status;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_fragment, container, false);

        tv_username = root.findViewById(R.id.username);
        cardView_status = root.findViewById(R.id.status);
        listView = root.findViewById(R.id.listview);
        localSession = new LocalSession(getActivity());

        tv_username.setText(localSession.getName());
        if(localSession.getStatus().equals("Active")){
            cardView_status.setBackground(getActivity().getDrawable(R.color.greann));
            }else{
            cardView_status.setBackground(getActivity().getDrawable(R.color.red));
        }

        ArrayList<ListviewProfileModel> arrayList = new ArrayList<ListviewProfileModel>();
        arrayList.add(new ListviewProfileModel(getResources().getString(R.string.edit_profile),R.drawable.ic_person1));
        arrayList.add(new ListviewProfileModel(getResources().getString(R.string.about),R.drawable.ic_about));
        arrayList.add(new ListviewProfileModel(getResources().getString(R.string.theprivate),R.drawable.ic_private));
        arrayList.add(new ListviewProfileModel(getResources().getString(R.string.service),R.drawable.ic_expir_date));
        arrayList.add(new ListviewProfileModel(getResources().getString(R.string.logout),R.drawable.ic_logout));
        ListviewProfileAdapter arrayAdapter = new ListviewProfileAdapter(getActivity(),R.layout.custom_profile,arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {

                    case 0:
                        startActivity(new Intent(getContext(), EditProfile.class));
                        break;

                    case 1:
                        startActivity(new Intent(getContext(), AboutApp.class));
                        break;

                    case 2:
                        startActivity(new Intent(getContext(), ThePrivate.class));
                        break;

                    case 3:
                        Toast.makeText(getContext(), getResources().getString(R.string.expir_date) + " " + localSession.getExpirDate() + "\n" + getResources().getString(R.string.expir_date2), Toast.LENGTH_LONG).show();
                        break;

                    case 4:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        View view1 = getLayoutInflater().inflate(R.layout.custom_delete_item_massage, null);
                        TextView textView = view1.findViewById(R.id.item_delete_massage);
                        ImageView image_cancle = view1.findViewById(R.id.cancle);
                        textView.setText(getResources().getString(R.string.logout_massage));
                        Button confirm = view1.findViewById(R.id.confirm_btn);
                        Button cancle = view1.findViewById(R.id.cancle_btn);
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
                        confirm.setOnClickListener(v ->
                        {
                            dialog.dismiss();
                            ProgressDialog loading = ProgressDialog.show(getContext(), null, getString(R.string.wait), false, false);
                            loading.setContentView(R.layout.custom_progressbar);
                            loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            loading.setCancelable(false);
                            loading.setCanceledOnTouchOutside(false);

                            // <-- Connect WIth Network And Check Response Successful or Failure -- >
                            final RequestInterface requestInterface = ApiClient.getClient(ApiClient.LOGOUT_URL).create(RequestInterface.class);
                            Call<LogoutResponse> call = requestInterface.LogoutApi("Bearer " + localSession.getToken());
                            call.enqueue(new Callback<LogoutResponse>() {
                                @Override
                                public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                                    if (response.isSuccessful()) {
                                        if (!response.body().isError()) {
                                            loading.dismiss();
                                            LocalSession.clearSession();
                                            Intent intent1 = new Intent(getContext(), Login.class);
                                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent1);
                                            ((MainActivity) getContext()).finish();
                                        } else {
                                            loading.dismiss();
                                            Utility.showAlertDialog(getString(R.string.error), response.body().getMessage_ar(), getActivity());
                                        }
                                    } else {
                                        loading.dismiss();
                                        Utility.showAlertDialog(getString(R.string.error), getString(R.string.servererror), getActivity());
                                    }
                                }

                                @Override
                                public void onFailure(Call<LogoutResponse> call, Throwable t) {
                                    loading.dismiss();
                                    Utility.showAlertDialog(getString(R.string.error), getString(R.string.connect_internet_slow), getActivity());
                                }
                            });

                        });
                        cancle.setOnClickListener(v -> dialog.dismiss());
                        dialog.show();
                        break;
                }
            }
        });
        return root;
    }
}
