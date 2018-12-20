package com.mochoi.pomer.viewmodel;

import android.databinding.ObservableField;
import android.util.Log;

import com.mochoi.pomer.model.entity.ForecastPomo;
import com.mochoi.pomer.model.entity.Reason;
import com.mochoi.pomer.model.entity.Task;
import com.mochoi.pomer.model.entity.WorkedPomo;
import com.mochoi.pomer.model.repository.FindTaskRepository;
import com.mochoi.pomer.model.vo.ReasonKind;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class ReportVM {
    public final ObservableField<String> fromDate = new ObservableField<>();
    public final ObservableField<String> toDate = new ObservableField<>();
    public final ObservableField<String> avgWorkedPomo = new ObservableField<>();
    public final ObservableField<String> taskCountDiffForcastAndWorked = new ObservableField<>();
    public final ObservableField<String> taskCountDiffDenominator = new ObservableField<>();
    public final ObservableField<List<ReportDetailItemVM>> details = new ObservableField<>();
    public final ObservableField<List<ReportReasonItemVM>> reasons = new ObservableField<>();
    private FindTaskRepository findTaskRepository;

    @Inject
    public ReportVM(FindTaskRepository findTaskRepository){
        this.findTaskRepository = findTaskRepository;
    }

    public List<Reason> findReasonForGraph(Date fromDate, Date toDate){
        return findTaskRepository.findReasonForGraph(fromDate, toDate);
    }

    /**
     * 指定期間の平均実績ポモドーロ数を設定
     * @param fromDate 期間From
     * @param toDate 期間To
     */
    public void setAvgWorkedPomo4Week(Date fromDate, Date toDate){
        List<WorkedPomo> pomos = findTaskRepository.findWorkedPomoInTerm(fromDate, toDate);

        if(pomos.isEmpty()){
            avgWorkedPomo.set("-");
            return;
        }

        //実績数を、ポモドーロをやった日数の和で割る
        int day = 1;
        Date date = pomos.get(0).registerDate;
        for(WorkedPomo wp : pomos){
            if(date.getTime() != wp.registerDate.getTime()){
                day++;
                date = wp.registerDate;
            }
        }

        int avg = Math.round(pomos.size() / day);
        avgWorkedPomo.set(String.valueOf(avg));
    }

    /**
     * 指定期間の予想と実績のポモドーロ数を設定
     * @param fromDate 期間From
     * @param toDate 期間To
     */
    public void setDiffTaskForecastAndWorked(Date fromDate, Date toDate){
        List<Task> tasks = findTaskRepository.findTaskWorked(fromDate, toDate);
        if(tasks == null){
            taskCountDiffForcastAndWorked.set("-");
            taskCountDiffDenominator.set("-");
            return;
        }

        int count = 0;
        for(Task task : tasks){
            String firstForecastPomo = findTaskRepository.findFirstForecastPomo(task.id);
            String workedPomoCount = findTaskRepository.countWorkedPomo(task.id);
            if(!firstForecastPomo.equals(workedPomoCount)){
                count++;
            }
        }
        taskCountDiffForcastAndWorked.set("" + count);
        taskCountDiffDenominator.set("" + tasks.size());
    }

    public void setDetailList(Date fromDate, Date toDate){
        List<Task> tasks = findTaskRepository.findTaskWorked(fromDate, toDate);
        List<ReportDetailItemVM> details = new ArrayList<>();

        if(tasks == null){
            this.details.set(details);
            return;
        }

        for (Task t : tasks){
            //「タスク名　実績数／予想数→予想数…（変更の数分）」の文字列を作る
            StringBuilder task = new StringBuilder(t.taskName+" : "+t.getWorkedPomoCount()+"／");

            int i = 0;
            for(ForecastPomo fp : t.forecastPomos){
                if(i++ != 0){
                    task.append("→");
                }
                task.append(fp.pomodoroCount);
            }

            ReportDetailItemVM vm = new ReportDetailItemVM();
            vm.task.set(task.toString());
            details.add(vm);
        }
        this.details.set(details);
    }

    public void setReasonList(Date fromDate, Date toDate){
        List<Reason> reasons = findTaskRepository.findReasonForReportList(fromDate, toDate);
        List<Reason> goodConcentration = reasons.stream()
                .filter(reason -> reason.kind == ReasonKind.GoodConcentration.getValue())
                .filter(reason -> StringUtils.isNotBlank(reason.reason))
                .collect(Collectors.toList());
        List<Reason> normalConcentration = reasons.stream()
                .filter(reason -> reason.kind == ReasonKind.NormalConcentration.getValue())
                .filter(reason -> StringUtils.isNotBlank(reason.reason))
                .collect(Collectors.toList());
        List<Reason> notConcentrate = reasons.stream()
                .filter(reason -> reason.kind == ReasonKind.InComplete.getValue()
                        || reason.kind == ReasonKind.NotConcentrate.getValue())
                .filter(reason -> StringUtils.isNotBlank(reason.reason))
                .collect(Collectors.toList());

        //画面表示用リストを作る
        List<String> reasonList = new ArrayList<>();
        reasonList.add("■集中できた");
        if(goodConcentration.isEmpty()){
            reasonList.add("--");
        } else {
            goodConcentration.forEach(reason -> reasonList.add("・"+reason.reason));
        }
        reasonList.add("■まぁまぁ集中できた");
        if(normalConcentration.isEmpty()){
            reasonList.add("--");
        } else {
            normalConcentration.forEach(reason -> reasonList.add("・"+reason.reason));
        }
        reasonList.add("■集中できなかった／中断した");
        if(notConcentrate.isEmpty()){
            reasonList.add("--");
        } else {
            notConcentrate.forEach(reason -> reasonList.add("・"+reason.reason));
        }

        List<ReportReasonItemVM> vmList = new ArrayList<>();
        for(String reason : reasonList){
            ReportReasonItemVM vm = new ReportReasonItemVM();
            vm.reason.set(reason);
            vmList.add(vm);
        }

        this.reasons.set(vmList);
    }
}
