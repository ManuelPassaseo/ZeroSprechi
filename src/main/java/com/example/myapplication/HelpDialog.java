package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HelpDialog {

    private final AlertDialog alertDialog;
    private final TextView description;
    private final TextView header;

    public HelpDialog(Context context) {
        RelativeLayout constraintLayout = ((Activity) context).findViewById(R.id.helpConstraintLayout);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_help, constraintLayout, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        alertDialog = builder.create();

        ImageView imageViewOk = view.findViewById(R.id.imageHelpOk);
        description = view.findViewById(R.id.textViewHelpDesc);
        header = view.findViewById(R.id.textViewHelpHeader);

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        imageViewOk.setOnClickListener(v -> alertDialog.dismiss());
    }

    public void show() {
        alertDialog.show();
    }

    public void setDescriptionText(String desc) {
        if (description != null) {
            description.setText(desc);
        }
    }

    public void setHeaderText(String headerText) {
        if (header != null) {
            header.setText(headerText);
        }
    }
}
