package com.mochoi.pomer.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.mochoi.pomer.R;

/**
 * メイン画面用アクティビティ
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void moveBacklog(View view){
        Intent intent = new Intent(this, BacklogActivity.class);
        startActivity(intent);
    }
    public void moveTodoList(View view){
        Intent intent = new Intent(this, TodoListActivity.class);
        startActivity(intent);
    }
//    public void moveReport(View view){
//        Intent intent = new Intent(this, TodoListActivity.class);
//        startActivity(intent);
//    }
//    public void moveAction(View view){
//        Intent intent = new Intent(this, TodoListActivity.class);
//        startActivity(intent);
//    }

}
