package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Range;

import com.myapplication3.Appreference;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;
import com.myapplication3.activity.SchedulerActivity;

import org.pjsip.pjsua2.app.MainActivity;

import java.util.ArrayList;

/**
 * Created by Amuthan on 10/01/2017.
 */

public class ScheduleCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent != null) {
                String taskid = intent.getExtras().getString("Taskid");
                int setid = intent.getExtras().getInt("customsetid", 0);
                if(Appreference.loginuserdetails == null) {
                    Intent mainAct_intent = new Intent(context, MainActivity.class);
                    mainAct_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mainAct_intent.putExtra("from", "scheduler");
                    mainAct_intent.putExtra("Taskid", taskid);
                    mainAct_intent.putExtra("customsetid", setid);
                    context.startActivity(mainAct_intent);
                } else {
                    boolean percentage_value = false;
                    Log.i("Schedule", "ScheduleCallReceiver == > taskid :" + taskid + " setid : " + setid);
                    TaskDetailsBean taskDetailsBean = null, taskDetailsBean1 = null;

                    String query = "select * from taskDetailsInfo where taskId = '" + taskid + "'  and subType = 'customeAttribute' and customSetId = '" + setid + "'";
                    ArrayList<TaskDetailsBean> detailsBeanArrayList = VideoCallDataBase.getDB(context).getTaskHistory(query);
                    Log.i("Schedule", "ScheduleCallReceiver == > taskid :" + taskid + " size : " + detailsBeanArrayList.size());
                    if (detailsBeanArrayList.size() > 0) {
                        for (TaskDetailsBean detailsBean : detailsBeanArrayList) {
                            Log.i("Schedule", "ScheduleCallReceiver == > detailsBean.getTaskDescription() :" + detailsBean.getTaskDescription() + " boolean value  : " + detailsBean.getTaskDescription().matches("[0-100]+"));
                            if (detailsBean.getTaskTagName().equalsIgnoreCase("Value") && detailsBean.getTaskDescription().length() < 4 && (0 < Integer.valueOf(detailsBean.getTaskDescription()) && Integer.valueOf(detailsBean.getTaskDescription()) < 100)) {
                                percentage_value = true;
                                taskDetailsBean = detailsBean;
                                Log.i("Schedule", "ScheduleCallReceiver percentage_value == > taskid :" + percentage_value + " size : " + taskDetailsBean.getTaskDescription());
                                break;
                            } else if (detailsBean.getTaskTagName().equalsIgnoreCase("Value") && (detailsBean.getTaskDescription().equalsIgnoreCase("assigned") || detailsBean.getTaskDescription().equalsIgnoreCase("overdue"))) {
                                percentage_value = false;
                                taskDetailsBean = detailsBean;
                                Log.i("Schedule", "ScheduleCallReceiver percentage_value == > taskid :" + percentage_value + " size : " + taskDetailsBean.getTaskDescription());
                                break;
                            } else if (detailsBean.getTaskTagName().equalsIgnoreCase("Conference Host")) {
                                percentage_value = false;
                                taskDetailsBean = detailsBean;
                            }
                        }
                    }
                    String query1 = "select * from taskHistoryInfo where taskId = '" + taskid + "'";
                    ArrayList<TaskDetailsBean> detailsBeanArrayList1 = VideoCallDataBase.getDB(context).getTaskHistoryInfo(query1);
                    Log.i("Schedule", "ScheduleCallReceiver == > taskid :" + taskid + " size 1 : " + detailsBeanArrayList1.size());

                    if (detailsBeanArrayList1.size() > 0) {
                        taskDetailsBean1 = detailsBeanArrayList1.get(0);
                        Log.i("Schedule", "ScheduleCallReceiver taskHistoryInfo == > taskid :" + percentage_value + " size : " + taskDetailsBean1.getTaskStatus());
                    }


                    //            Log.i("Schedule", "ScheduleCallReceiver percentage == > taskstatus :" + taskDetailsBean.getTaskDescription() + "taskstatus 1 :" + taskDetailsBean1.getTaskStatus());
                    //            Log.i("Schedule", "ScheduleCallReceiver status == > taskstatus :" + taskDetailsBean.getTaskStatus() + "taskstatus 1 :" + taskDetailsBean1.getTaskStatus() + " size 1 : " + taskDetailsBean.getTaskStatus().equalsIgnoreCase(taskDetailsBean1.getTaskStatus()));
                    if (percentage_value) {
                        if (taskDetailsBean1.getCompletedPercentage() == null)
                            taskDetailsBean1.setCompletedPercentage("0");
                        if (taskDetailsBean != null && taskDetailsBean.getTaskDescription() != null && ((Integer.valueOf(taskDetailsBean.getTaskDescription()) > (Integer.valueOf(taskDetailsBean1.getCompletedPercentage()))))) {
                            Intent intent1 = new Intent(context, SchedulerActivity.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent1.putExtra("Taskid", taskid);
                            intent1.putExtra("customsetid", setid);
                            context.startActivity(intent1);
                        }
                    } else if ((taskDetailsBean != null && taskDetailsBean.getTaskDescription() != null &&
                            (taskDetailsBean1 != null && taskDetailsBean1.getTaskStatus() != null && taskDetailsBean.getTaskDescription().equalsIgnoreCase(taskDetailsBean1.getTaskStatus()))
                            || (taskDetailsBean.getTaskTagName() != null && taskDetailsBean.getTaskTagName().equalsIgnoreCase("Conference Host")))
                            || taskDetailsBean.getProjectId() != null) {
                        Log.i("Schedule", "ScheduleCallReceiver status1 == > taskstatus :" + taskDetailsBean.getTaskStatus() + "taskstatus 1 :" + taskDetailsBean1.getTaskStatus());

                        Intent intent1 = new Intent(context, SchedulerActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra("Taskid", taskid);
                        intent1.putExtra("customsetid", setid);
                        context.startActivity(intent1);
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
