package com.example.fingertipsdemoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;



/**
 * Created by him on 27-May-17.
 */

public abstract class BaseActivity extends AppCompatActivity  {

    @Override
    public void onCreate(Bundle save) {
        super.onCreate(save);
        setContentView(getActivityLayout());
        initialize();
        init(save);
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public abstract int getActivityLayout();

    public abstract void initialize();

    public abstract void init(Bundle save);


    @Override
    public void onBackPressed() {
        hideKeyBoard();
        super.onBackPressed();
    }


    public void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void sendToThisActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    public void sendToThisActivity(Class activity, int flag) {
        Intent intent = new Intent(this, activity);
        intent.setFlags(flag);
        startActivity(intent);
    }

    public void sendToThisActivity(Class activity, String s[]) {
        Intent intent = new Intent(this, activity);
        for (int i = 0; i < s.length; i++) {
            String p[] = s[i].split(";");
            intent.putExtra(p[0], p[1]);
        }
        startActivity(intent);
    }

    public void sendToThisActivity(Class activity, String s[], int flag) {
        Intent intent = new Intent(this, activity);
        intent.setFlags(flag);
        for (int i = 0; i < s.length; i++) {
            String p[] = s[i].split(";");
            intent.putExtra(p[0], p[1]);
        }
        startActivity(intent);
    }








}
