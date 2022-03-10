package com.example.cafeteria.Utilty;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.cafeteria.Ui.Activity.Logout;
import com.example.cafeteria.R;

import java.text.DecimalFormat;

import androidx.appcompat.app.AlertDialog;

public class Utility {
    /**
     * custom method to show alert dialog
     *
     * @param msg:     String to be set as alert dialog title
     * @param title:   String to be displayed as alert dialog message
     * @param context: contains activity context
     */
    public static void showAlertDialog(String title, String msg, Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View view1 =((Activity)context).getLayoutInflater().inflate(R.layout.custom_error_massage, null);
        TextView title1 = view1.findViewById(R.id.title);
        TextView content1 = view1.findViewById(R.id.massage);
        TextView btn = view1.findViewById(R.id.btn);
        title1.setText(title);
        content1.setText(msg);
        alertDialogBuilder.setView(view1);
        AlertDialog alertDialog = alertDialogBuilder.create();

        InsetDrawable insetDrawable = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT),20);
        alertDialog.getWindow().setBackgroundDrawable(insetDrawable);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if (msg.contains("عفوا ، هذا الحساب غير نشط . يرجى مراجعة الإدارة")) {
                     context.startActivity(new Intent(context,Logout.class));
//                     Logout logoutt = new Logout();
//                     logoutt.log();
                 }

             if(title.equals(R.string.error)){
                 alertDialog.dismiss();
             }
                else {
                   alertDialog.dismiss();
                 }
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    public static void printLog(String... msg) {
        String str = "";
        for (String i : msg) {
            str = str + "\n" + i;
        }
        Log.i("Orderpharma", str);

    }

    public static String getRoundedPrice(String price){
        try {
            double thePrice = Double.parseDouble(price);
            return new DecimalFormat("0.#").format(Math.round(thePrice * 100.0) / 100.0);
        }catch (Exception e){
            return price;
        }
    }

    public static ProgressDialog GetProcessDialog(Activity activity) {
        // prepare the dialog box
        ProgressDialog dialog = new ProgressDialog(activity, 5);
        // make the progress bar cancelable
        dialog.setCancelable(true);
        // set a message text
//        dialog.setMessage(""+R.string.loading);

        // show it
        return dialog;
    }
}
