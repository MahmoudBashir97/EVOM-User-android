package com.mahmoud.bashir.evom_user_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



/* important hint for nesma do not delete it :)
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose an animal");
// add a radio button list
        String[] animals = {"horse", "cow", "camel", "sheep", "goat"};
        int checkedItem = 1; // cow
        builder.setSingleChoiceItems(animals, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
            }
        });
// add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
            }
        });
        builder.setNegativeButton("Cancel", null);
// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
 */
    }
}