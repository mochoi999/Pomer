package com.mochoi.pomer.view;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mochoi.pomer.R;
import com.mochoi.pomer.databinding.ReportDetailItemBinding;
import com.mochoi.pomer.databinding.ReportMainBinding;
import com.mochoi.pomer.databinding.ReportReasonItemBinding;
import com.mochoi.pomer.di.DaggerAppComponent;
import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.vo.ReasonKind;
import com.mochoi.pomer.util.Utility;
import com.mochoi.pomer.viewmodel.ReportDetailItemVM;
import com.mochoi.pomer.viewmodel.ReportReasonItemVM;
import com.mochoi.pomer.viewmodel.ReportVM;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * レポート画面用アクティビティ
 */
public class ReportActivity extends BaseActivity {
    private ReportVM vm;
    private ReportMainBinding binding;
    private Date fromDate;
    private Date toDate;
    private RecyclerView detailRecyclerView;
    private ImageButton detailBtn;
    private final static int DURATION_DETAIL = 300;    // Detailボタンのアニメーションにかける時間(ミリ秒)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = DaggerAppComponent.create().makeReportVM();
        binding = DataBindingUtil.setContentView(this, R.layout.report_main);
        binding.setReportVM(vm);

        // アクションバーをカスタマイズ
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Report");

        detailBtn = findViewById(R.id.detail_btn);
        detailRecyclerView = findViewById(R.id.detail_recycler_view);

        //Task Detailリスト
        DetailRecyclerViewAdapter adapter = new DetailRecyclerViewAdapter();
        RecyclerView recyclerView = binding.detailRecyclerView;
        //data set
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //decoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        //理由一覧
        ReasonRecyclerViewAdapter reasonAdapter = new ReasonRecyclerViewAdapter();
        RecyclerView reasonRecyclerView = binding.reasonRecyclerView;
        //data set
        reasonRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reasonRecyclerView.setAdapter(reasonAdapter);
        //decoration
        DividerItemDecoration reasonDividerItemDecoration = new DividerItemDecoration(reasonRecyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        reasonRecyclerView.addItemDecoration(reasonDividerItemDecoration);

        refreshActivity(Utility.getNowDate());
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

    public void preWeek(View view){
        refreshActivity(Utility.addDay(toDate, -7));
    }

    public void nextWeek(View view){
        refreshActivity(Utility.addDay(toDate, 7));
    }

    private void refreshActivity(Date date){
        setTermDate(date);
        createBarChart();
        vm.setAvgWorkedPomo4Week(fromDate, toDate);
        vm.setDiffTaskForecastAndWorked(fromDate, toDate);

        //Detailリスト
        vm.setDetailList(fromDate, toDate);

        //Detailリストを閉じる
        detailBtn.setBackgroundResource(R.drawable.open);
        detailBtn.clearAnimation();
        int height = detailRecyclerView.getHeight();
        ResizeAnimation collapseAnimation = new ResizeAnimation(detailRecyclerView
                , -height
                , height);
        detailRecyclerView.startAnimation(collapseAnimation);

        //理由リスト
        vm.setReasonList(fromDate, toDate);

    }

    private void setTermDate(Date toDate){
        this.toDate = toDate;
        vm.toDate.set(Utility.convDate2String(this.toDate, "MM/dd"));

        this.fromDate = Utility.addDay(toDate, -6);
        vm.fromDate.set(Utility.convDate2String(this.fromDate, "MM/dd"));
    }

    /**
     * 実績グラフを作成
     */
    private void createBarChart() {
        BarChart barChart = (BarChart) findViewById(R.id.bar_chart);

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(true);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setEnabled(true);

        barChart.setTouchEnabled(true);
        barChart.setPinchZoom(true);
        barChart.setDoubleTapToZoomEnabled(true);

        barChart.setScaleEnabled(true);

        barChart.getLegend().setEnabled(true);

        //X軸周り
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(0f);
        xAxis.setSpaceMax(1f);

        //Y軸(左)
        YAxis left = barChart.getAxisLeft();
        left.setAxisMinimum(0f);
        left.setLabelCount(1);

        //グラフ上の表示
        barChart.getDescription().setEnabled(false);

        //data set
        barChart.setData(createBarChartData(barChart));
        float groupSpace = 0.1f;
        float barSpace = 0.1f;
        if(barChart.getData() != null) {
            barChart.groupBars(0f, groupSpace, barSpace);
        }

        barChart.invalidate();// refresh

        // アニメーション
        barChart.animateY(2000, Easing.EasingOption.EaseInBack);
    }

    /**
     * BarChartの設定
     * @param barChart BarChart
     * @return BarChart用データ
     */
    private BarData createBarChartData(BarChart barChart) {
        List<Reason> reasons = vm.findReasonForGraph(fromDate, toDate);
        if(reasons.isEmpty()){
            return null;
        }

        //X軸ラベル用
        final String[] labels = new String[7];
        Date date = toDate;
        for (int i=6; i>=0; i--){
            labels[i] = Utility.convDate2String(date, "MM/dd");
            date = Utility.addDay(date, -1);
        }

        int good = 0; // 集中できた
        int normal = 0; // まあまあ
        int notConcentrate = 0; // 集中できない
        List<BarEntry> goods = new ArrayList<>();
        List<BarEntry> normals = new ArrayList<>();
        List<BarEntry> notConcentrates = new ArrayList<>();
        int labelIndex = 0;
        for(String label : labels){
            for(Reason reason : reasons){
                //X軸の日付と異なるデータの場合はスキップ
                if(!label.equals(Utility.convDate2String(reason.registerDate, "MM/dd"))) {
                    continue;
                }
                ReasonKind kind = ReasonKind.getReason(reason.kind);
                switch (kind) {
                    case GoodConcentration : good++; break;
                    case NormalConcentration: normal++; break;
                    case NotConcentrate: notConcentrate++; break;
                    default: break;
                }
            }

            goods.add(new BarEntry(labelIndex, good));
            normals.add(new BarEntry(labelIndex, normal));
            notConcentrates.add(new BarEntry(labelIndex, notConcentrate));
            good = 0;
            normal = 0;
            notConcentrate = 0;
            labelIndex++;
        }

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        BarDataSet set1 = new BarDataSet(goods, "集中できた");
        set1.setColor(Color.BLUE);
        setStyleBarDataSet(set1);
        BarDataSet set2 = new BarDataSet(normals, "まぁまぁ");
        set2.setColor(Color.MAGENTA);
        setStyleBarDataSet(set2);
        BarDataSet set3 = new BarDataSet(notConcentrates, "できなかった");
        set3.setColor(Color.RED);
        setStyleBarDataSet(set3);

        BarData barData = new BarData(set1, set2, set3);
        barData.setBarWidth(0.2f);
        return barData;
    }

    private void setStyleBarDataSet(BarDataSet dataSet){
        //整数で表示
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "" + (int) value;
            }
        });
        //ハイライトさせない
        dataSet.setHighlightEnabled(false);
    }

    public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder> {
        private List<ReportDetailItemVM> items;

        @Override
        @NonNull
        public DetailRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // DataBinding
            ReportDetailItemBinding binding = ReportDetailItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new DetailRecyclerViewAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull DetailRecyclerViewAdapter.ViewHolder holder, int position) {
            ReportDetailItemVM item = items.get(position);
            holder.binding.setItemVM(item);
        }

        @Override
        public int getItemCount() {
            if(items == null){
                return 0;
            } else {
                return items.size();
            }
        }

        void replaceData(List<ReportDetailItemVM> items) {
            setList(items);
        }
        private void setList(List<ReportDetailItemVM> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            final ReportDetailItemBinding binding;

            ViewHolder(final ReportDetailItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // 展開ボタンのデフォルトサイズを保持
        final int btnWidth = detailBtn.getWidth();
        final int btnHeight = detailBtn.getHeight();

        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailRecyclerView.clearAnimation();

                // ExpandするViewの元のサイズを保持する
                int originalHeight = vm.details.get().size() * 90;

                if(detailRecyclerView.getHeight() > 0) {
                    detailBtn.setBackgroundResource(R.drawable.close);
                    startRotateAnim(-180, btnWidth / 2, btnHeight / 2);

                    // ビューを閉じるアニメーションを生成
                    final ResizeAnimation collapseAnimation = new ResizeAnimation(detailRecyclerView
                            , -originalHeight
                            , originalHeight);
                    collapseAnimation.setDuration(DURATION_DETAIL);
                    detailRecyclerView.startAnimation(collapseAnimation);
                } else{
                    detailBtn.setBackgroundResource(R.drawable.open);
                    startRotateAnim(180, btnWidth / 2, btnHeight / 2);

                    // ビューを開くアニメーションを生成
                    final ResizeAnimation expandAnimation = new ResizeAnimation(detailRecyclerView, originalHeight, 0);
                    expandAnimation.setDuration(DURATION_DETAIL);
                    detailRecyclerView.startAnimation(expandAnimation);
                }
            }
        });

    }

    /**
     * 展開ボタンのアニメーション
     * @param toDegrees 角度
     * @param pivotX
     * @param pivotY
     */
    private void startRotateAnim(int toDegrees, int pivotX, int pivotY) {
        // 展開ボタンの180度回転アニメーションを生成
        RotateAnimation rotate = new RotateAnimation(0, toDegrees, pivotX, pivotY);
        rotate.setDuration(DURATION_DETAIL);       // アニメーションにかける時間(ミリ秒)
        rotate.setFillAfter(true);          // アニメーション表示後の状態を保持
        detailBtn.startAnimation(rotate);    // アニメーション開始
    }

    public class ReasonRecyclerViewAdapter extends RecyclerView.Adapter<ReasonRecyclerViewAdapter.ViewHolder> {
        private List<ReportReasonItemVM> items;

        @Override
        @NonNull
        public ReasonRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // DataBinding
            ReportReasonItemBinding binding = ReportReasonItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ReasonRecyclerViewAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ReasonRecyclerViewAdapter.ViewHolder holder, int position) {
            ReportReasonItemVM item = items.get(position);
            holder.binding.setItemVM(item);
        }

        @Override
        public int getItemCount() {
            if(items == null){
                return 0;
            } else {
                return items.size();
            }
        }

        void replaceData(List<ReportReasonItemVM> items) {
            setList(items);
        }
        private void setList(List<ReportReasonItemVM> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            final ReportReasonItemBinding binding;

            ViewHolder(final ReportReasonItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
