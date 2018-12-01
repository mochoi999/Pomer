package com.mochoi.pomer.view;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;

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
import com.mochoi.pomer.databinding.ReportMainBinding;
import com.mochoi.pomer.di.DaggerAppComponent;
import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.vo.ReasonKind;
import com.mochoi.pomer.util.Utility;
import com.mochoi.pomer.viewmodel.ReportVM;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * レポート画面用アクティビティ
 */
public class ReportActivity extends BaseActivity {
    private ReportVM vm;
    private Date fromDate;
    private Date toDate;
    private List<Reason> reasons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = DaggerAppComponent.create().makeReportVM();
        ReportMainBinding binding = DataBindingUtil.setContentView(this, R.layout.report_main);
        binding.setReportVM(vm);

        // アクションバーをカスタマイズ
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Report");

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
    }

    private void setTermDate(Date toDate){
        this.toDate = toDate;
        vm.toDate.set(Utility.convDate2String(this.toDate, "MM/dd"));

        this.fromDate = Utility.addDay(toDate, -6);
        vm.fromDate.set(Utility.convDate2String(this.fromDate, "MM/dd"));
    }

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

    // BarChartの設定
    private BarData createBarChartData(BarChart barChart) {
//        for(Reason reason :reasons){
//            Log.d("TEST", reason.registerDate + " " +reason.kind);
//        }

        reasons = vm.findReasonForGraph(fromDate, toDate);
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
//            Log.d("TEST", "///////"+labelIndex + "  " + label+" / " + reasonIndex );

            for(Reason reason : reasons){

//                Log.d("TEST", "" + reason.registerDate);

                //X軸の日付と異なるデータの場合はスキップ
                if(!label.equals(Utility.convDate2String(reason.registerDate, "MM/dd"))) {
//                    Log.d("TEST", "skip");
                    continue;
                }

//                Log.d("TEST", "ADD");

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


}
