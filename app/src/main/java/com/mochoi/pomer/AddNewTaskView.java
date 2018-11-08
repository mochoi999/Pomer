package com.mochoi.pomer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * タスク追加ボタンのカスタムビュー用クラス
 */
//public class AddNewTaskView extends RelativeLayout {
public class AddNewTaskView extends Fragment {
    private Context context;
//    private OnClickListener listener;

//    public AddNewTaskView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        initialize(context);
//    }
//
//    private void initialize(Context context){
//        inflate(context, R.layout.part_fab_add_task, this);
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        Log.d("TEST","onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.part_fab_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.add_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEST","######################");
                Intent intent = new Intent(context, RegisterTaskActivity.class);
                startActivity(intent);            }
        });
    }
}
