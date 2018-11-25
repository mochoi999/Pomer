package com.mochoi.pomer.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.PastItemBinding;
import com.mochoi.pomer.databinding.PastMainBinding;
import com.mochoi.pomer.databinding.TodolistItemBinding;
import com.mochoi.pomer.di.DaggerAppComponent;
import com.mochoi.pomer.model.vo.TaskKind;
import com.mochoi.pomer.util.Utility;
import com.mochoi.pomer.viewmodel.PastItemVM;
import com.mochoi.pomer.viewmodel.PastTaskVM;
import com.mochoi.pomer.viewmodel.TodolistItemVM;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 過去のタスク画面用ビュー
 */
public class PastActivity extends BaseActivity {
    private PastTaskVM vm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PastMainBinding binding = DataBindingUtil.setContentView(this, R.layout.past_main);

        vm = DaggerAppComponent.create().makePastTaskVM();
        binding.setPastTaskVM(vm);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this);
        RecyclerView recyclerView = binding.recycler;
        //data set
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // アクションバーをカスタマイズ
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("過去のタスク");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void findList(View view){
        findList();
    }

    private void findList(){
        //validate
        String fromDateStr = ((EditText)findViewById(R.id.from_date)).getText().toString();
        String toDateStr = ((EditText)findViewById(R.id.to_date)).getText().toString();
        if(StringUtils.isBlank(fromDateStr) || StringUtils.isBlank(toDateStr)){
            showNotification("日付を入力してください");
            return;
        }

        Date fromDate;
        Date toDate;
        try {
            fromDate = Utility.convString2Date(fromDateStr);
            toDate = Utility.convString2Date(toDateStr);
        } catch (ParseException e) {
            showNotification("日付のフォーマットはYYYY/MM/DDです");
            return;
        }

        //リストを取得
        vm.setUpList(fromDate, toDate);
    }

    public void showFromDatePickerDialog(View view) {
        DialogFragment fragment = new FromDatePicker();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showToDatePickerDialog(View view) {
        DialogFragment fragment = new ToDatePicker();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    void releaseFinishStatus(final long id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("完了を解除しますか？")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vm.releaseFinishStatus(id);
                        findList();
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    public static class FromDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this,  year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            EditText date = (EditText)getActivity().findViewById(R.id.from_date);
            date.setText(String.format(Locale.JAPANESE, "%d/%d/%d",year, month+1, dayOfMonth));
        }
    }

    public static class ToDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this,  year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            EditText date = (EditText)getActivity().findViewById(R.id.to_date);
            date.setText(String.format(Locale.JAPANESE, "%d/%d/%d",year, month+1, dayOfMonth));
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private List<PastItemVM> items;
        private PastActivity activity;

        RecyclerViewAdapter(PastActivity activity){
            this.activity = activity;
        }

        @Override
        @NonNull
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // DataBinding
            PastItemBinding binding = PastItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new RecyclerViewAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
            PastItemVM item = items.get(position);
            holder.binding.setItem(item);
        }

        @Override
        public int getItemCount() {
            if(items == null){
                return 0;
            } else {
                return items.size();
            }
        }

        void replaceData(List<PastItemVM> items) {
            setList(items);
        }
        private void setList(List<PastItemVM> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            final PastItemBinding binding;

            ViewHolder(final PastItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;

                itemView.findViewById(R.id.task_name).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.releaseFinishStatus(binding.getItem().task.get().id);
                    }
                });
            }


        }
    }

}
