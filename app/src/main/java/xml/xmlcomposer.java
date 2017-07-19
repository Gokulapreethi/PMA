package xml;

import android.util.Log;

import com.ase.Bean.TaskDetailsBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dinesh on 5/25/2016.
 */
public class xmlcomposer {

    private String quotes = "\"";


   /* <?xml version="1.0"?>
    <protostart>
    <coordinator name="1001" number="1001" status="connected" sessionid="" type="dashboardrequest" hold="" broadcast="" mic="" speaker="" />
    <user name="1002" number="1002" status="ringing" mic="" hold="" />
    <Callinfo conferenceuri="6396073@testconferenceuri" callstarttime="2016-05-25 09:38:32 +0000" callsessionid="" />
    </protostart>*/



    public String dashboardRequestXml(String[] conf_callinfo,
                                      ArrayList<String[]> userinfo, String type) {
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append("<?xml version=\"1.0\"?>"
                    + "<protostart><coordinator");
            buffer.append(" name=" + quotes + conf_callinfo[0] + quotes);
            buffer.append(" number=" + quotes + conf_callinfo[1] + quotes);
            buffer.append(" status=" + quotes + conf_callinfo[2] + quotes);
            buffer.append(" sessionid=" + quotes + conf_callinfo[3] + quotes);
            buffer.append(" type=" + quotes + type + quotes);
            buffer.append(" hold=" + quotes + "" + quotes);
            buffer.append(" broadcast=" + quotes + "" + quotes);
            buffer.append(" mic=" + quotes + "" + quotes);
            buffer.append(" speaker=" + quotes + "" + quotes);
            buffer.append(" />");

            for (String[] user : userinfo) {

                buffer.append("<user ");
                buffer.append("name=");
                buffer.append(quotes).append(user[0]).append(quotes);
                buffer.append(" number=");
                buffer.append(quotes).append(user[1]).append(quotes);
                buffer.append(" status=");
                buffer.append(quotes).append(user[2]).append(quotes);
                buffer.append(" mic=");
                buffer.append(quotes).append("").append(quotes);
                buffer.append(" />");

            }

//            if (wisper_info != null && wisper_info.size() > 0) {
//                Iterator iterator1 = wisper_info.entrySet()
//                        .iterator();
//                while (iterator1.hasNext()) {
//                    Map.Entry mapEntry = (Map.Entry) iterator1.next();
//                    String parlist = (String) mapEntry.getKey();
//                    if (parlist != null) {
//                        ArrayList<String> participants = (ArrayList<String>) mapEntry.getValue();
//                        buffer.append("<pwhishper ");
//                        buffer.append("owner=");
//                        buffer.append(quotes).append(mapEntry.getKey()).append(quotes);
//                        buffer.append(" participants=");
//                        buffer.append(quotes);
//                        for (int i = 0; i < participants.size(); i++) {
//                            if (!mapEntry.getKey().equals(participants.get(i))) {
//                                buffer.append(participants.get(i));
//                                if (i != participants.size() - 1) {
//                                    buffer.append(",");
//                                }
//                            }
//                        }
//                        buffer.append(quotes);
//                        buffer.append(" />");
//
//                    }
//                }
//
//            }
            buffer.append("<Callinfo ");
            buffer.append("conferenceuri=");
            buffer.append(quotes).append(conf_callinfo[4]).append(quotes);
            buffer.append(" callstarttime=");
            buffer.append(quotes).append(conf_callinfo[5]).append(quotes);
            buffer.append(" callsessionid=");
            buffer.append(quotes).append(conf_callinfo[6]).append(quotes);
            buffer.append(" />");
            buffer.append("</protostart>");
            Log.d("dashboard", "composed xml for dashboardxml======>"
                    + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {


        }
        return buffer.toString();
    }

    public String composeChangeHostRequestXml(String[] conf_callinfo,
                                              ArrayList<String[]> userinfo, String type, HashMap<String, ArrayList<String>> wisper_info) {
        StringBuffer buffer = new StringBuffer();
        Log.d("changehost", "Inside composeChangeHostRequestXml");
        try {
            buffer.append("<?xml version=\"1.0\"?>"
                    + "<protostart><coordinator ");
            buffer.append(" name=" + quotes + conf_callinfo[0] + quotes);
            buffer.append(" number=" + quotes + conf_callinfo[1] + quotes);
            buffer.append(" contacturi=" + quotes + conf_callinfo[2] + quotes);
            buffer.append(" sessionid=" + quotes + conf_callinfo[3] + quotes);
            buffer.append(" type=" + quotes + type + quotes);
            buffer.append(" hold=" + quotes + "0" + quotes);
            buffer.append(" broadcast=" + quotes + "0" + quotes);
            buffer.append(" mic=" + quotes + "0" + quotes);
            buffer.append(" speaker=" + quotes + "0" + quotes);
            buffer.append(" />");

            for (String[] user : userinfo) {

                buffer.append("<user ");
                buffer.append("name=");
                buffer.append(quotes).append(user[0]).append(quotes);
                buffer.append(" number=");
                buffer.append(quotes).append(user[1]).append(quotes);
                buffer.append(" status=");
                buffer.append(quotes).append(user[2]).append(quotes);
                buffer.append(" contacturi=");
                buffer.append(quotes).append(user[3]).append(quotes);
                buffer.append(" mic=");
                buffer.append(quotes).append(user[4]).append(quotes);
                buffer.append(" hold=");
                buffer.append(quotes).append("0").append(quotes);
                buffer.append(" whisperoption=");
                buffer.append(quotes).append("0").append(quotes);
                buffer.append(" inbound=");
                buffer.append(quotes).append("0").append(quotes);
                buffer.append(" userendpoint=");
                if (user[5] != null) {
                    Log.i("mailid", "compose mailid--->" + user[5]);
                    buffer.append(quotes).append(user[5]).append(quotes);
                }
                buffer.append(" />");

            }

            if (wisper_info != null && wisper_info.size() > 0) {
                Iterator iterator1 = wisper_info.entrySet()
                        .iterator();
                while (iterator1.hasNext()) {
                    Map.Entry mapEntry = (Map.Entry) iterator1.next();
                    String parlist = (String) mapEntry.getKey();
                    if (parlist != null) {
                        ArrayList<String> participants = (ArrayList<String>) mapEntry.getValue();
                        buffer.append("<pwhishper ");
                        buffer.append("owner=");
                        buffer.append(quotes).append(mapEntry.getKey()).append(quotes);
                        buffer.append(" participants=");
                        buffer.append(quotes);
                        for (int i = 0; i < participants.size(); i++) {
                            if (!mapEntry.getKey().equals(participants.get(i))) {
                                buffer.append(participants.get(i));
                                if (i != participants.size() - 1) {
                                    buffer.append(",");
                                }
                            }
                        }
                        buffer.append(quotes);
                        buffer.append(" />");

                    }
                }

            }

            buffer.append("<Callinfo ");
            buffer.append("conferenceuri=");
            buffer.append(quotes).append(conf_callinfo[4]).append(quotes);
            buffer.append(" conferencename=");
            buffer.append(quotes).append(conf_callinfo[5]).append(quotes);
            buffer.append(" callstarttime=");
            buffer.append(quotes).append(conf_callinfo[6]).append(quotes);
            buffer.append(" newhost=");
            buffer.append(quotes).append(conf_callinfo[7]).append(quotes);
            buffer.append(" callsessionid=");
            buffer.append(quotes).append(conf_callinfo[8]).append(quotes);
            buffer.append(" />");
            buffer.append("</protostart>");
            Log.d("changehost", "composed xml for changehostmsg======>"
                    + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return buffer.toString();
    }

    public String composeChangeHostResponseXml(String[] conf_callinfo) {
        StringBuffer buffer = new StringBuffer();
        Log.d("changehost", "Inside composeChangeHostResponseXml");
        try {
            buffer.append("<?xml version=\"1.0\"?>" + "<changehost_response ");
            buffer.append(" conferenceuri=" + quotes + conf_callinfo[0]
                    + quotes + " >");
            buffer.append("<session ");
            buffer.append("callid=" + quotes + conf_callinfo[1] + quotes + "/>");
            buffer.append("</changehost_response>");
            Log.d("changehost", "composed xml for changehostmsg======>"
                    + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return buffer.toString();
    }

    /*
    <?xml version="1.0"?><TaskDetailsinfo><TaskDetails taskName="Task For Dev IOS" taskDescription="Type Your Description Here hello"
    fromUserId="0" toUserId="0" taskNo="201607011337472648289" plannedStartDateTime="(null)" plannedEndDateTime="(null)"
    isRemainderRequired="1" remainderFrequency="2016-06-24 20:00:00" taskStatus="A"
    signalid="8459926191602335744"</TaskDetails></TaskDetailsinfo>
     */
    public String composeTaskDetailsinfo(TaskDetailsBean detailsBean) {
        StringBuffer stringBuffer = new StringBuffer();
        try {

            stringBuffer.append("<?xml version=\"1.0\"?><TaskDetailsinfo><TaskDetails taskName=");
            stringBuffer.append(quotes + detailsBean.getTaskName() + quotes);
            stringBuffer.append(" taskDescription=" + quotes + detailsBean.getTaskDescription() + quotes);
            stringBuffer.append(" fromUserId=" + quotes + detailsBean.getFromUserId() + quotes);
            stringBuffer.append(" toUserId=" + quotes + detailsBean.getToUserId() + quotes);
            stringBuffer.append(" taskNo=" + quotes + detailsBean.getTaskNo() + quotes);
            stringBuffer.append(" plannedStartDateTime=" + quotes + detailsBean.getPlannedStartDateTime() + quotes);
            stringBuffer.append(" plannedEndDateTime=" + quotes + detailsBean.getPlannedEndDateTime() + quotes);
            stringBuffer.append(" isRemainderRequired=" + quotes + detailsBean.getIsRemainderRequired() + quotes);
            stringBuffer.append(" remainderFrequency=" + quotes + detailsBean.getRemainderFrequency() + quotes);
            stringBuffer.append(" signalid=" + quotes + detailsBean.getTaskNo() + quotes);
            stringBuffer.append(" />");
            stringBuffer.append("</TaskDetailsinfo>");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }

    /*
    *'<?xml version="1.0"?><TaskDetailsinfo><TaskDetails taskName="20160715064456.caf" taskDescription="This task is overdue"
    * fromUserId="murugan_cognitivemobile.net" toUserId="aparna_cognitivemobile.net" taskNo="150720161844543562065688"
    * plannedStartDateTime="(null)" plannedEndDateTime="(null)" isRemainderRequired="(null)" remainderDateTime="(null)"
    * taskStatus="overdue" signalid="260720161458051742802728" parentId="260720161458053914631072" mimeType="text"
    * dateTime="2016-07-26 14:58:05" taskPriority="Medium" dateFrequency="(null)" timeFrequency="0" taskRequestType=""
    * taskId="55" taskOwner="" completedPercentage=""  /></TaskDetailsinfo>'
     */

    public String composeTaskCancellinfo(TaskDetailsBean cmbean) {
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append("<?xml version=\"1.0\"?>"
                    + "<TaskDetailsinfo><TaskDetails");
            buffer.append(" taskName=" + quotes + cmbean.getTaskName() + quotes);
            buffer.append(" taskDescription=" + quotes + cmbean.getTaskDescription() + quotes);
            buffer.append(" fromUserId=" + quotes + cmbean.getFromUserId() + quotes);
            buffer.append(" toUserId=" + quotes + cmbean.getToUserId() + quotes);
            buffer.append(" fromUserName=" + quotes + cmbean.getFromUserName() + quotes);
            buffer.append(" toUserName=" + quotes + cmbean.getToUserName() + quotes);
            buffer.append(" taskNo=" + quotes + cmbean.getTaskNo() + quotes);
            buffer.append(" taskId=" + quotes + cmbean.getTaskId() + quotes);
            buffer.append(" plannedStartDateTime=" + quotes + cmbean.getPlannedStartDateTime() + quotes);
            buffer.append(" plannedEndDateTime=" + quotes + cmbean.getPlannedEndDateTime() + quotes);
            buffer.append(" isRemainderRequired=" + quotes + cmbean.getIsRemainderRequired() + quotes);
            buffer.append(" remainderDateTime=" + quotes + cmbean.getRemainderFrequency() + quotes);
            buffer.append(" taskStatus=" + quotes + cmbean.getTaskStatus() + quotes);
            buffer.append(" signalid=" + quotes + cmbean.getSignalid() + quotes);
            buffer.append(" parentId=" + quotes + cmbean.getParentId() + quotes);
            buffer.append(" taskOwner=" + quotes + cmbean.getOwnerOfTask() + quotes);
            buffer.append(" mimeType=" + quotes + cmbean.getMimeType() + quotes);
            buffer.append(" dateTime=" + quotes + cmbean.getTaskUTCDateTime() + quotes);
            buffer.append(" taskReceiver=" + quotes + cmbean.getTaskReceiver() + quotes);
            buffer.append(" taskType=" + quotes + cmbean.getTaskType() + quotes);
            buffer.append(" taskPriority=" + quotes + cmbean.getTaskPriority() + quotes);
            buffer.append(" completedPercentage=" + quotes + cmbean.getCompletedPercentage() + quotes);
            buffer.append(" taskPriority=" + quotes + cmbean.getTaskPriority() + quotes);
            buffer.append(" dateFrequency=" + quotes + cmbean.getDateFrequency() + quotes);
            buffer.append(" timeFrequency=" + quotes + cmbean.getTimeFrequency() + quotes);
            buffer.append(" requestStatus=" + quotes + cmbean.getRequestStatus() + quotes);
            buffer.append(" taskMemberList=" + quotes + cmbean.getGroupTaskMembers() + quotes);
            buffer.append(" taskObservers=" + quotes + cmbean.getTaskObservers() + quotes);
//            buffer.append(" parentId=" + quotes + cmbean.getMsgtype() + quotes);
//            if (cmbean.getType().equals("CallSingleChat") || cmbean.getType().equals("CallGroupChat")) {
//                buffer.append(" activeURI=" + quotes + cmbean.getConferenceuri() + quotes);
//            } else {
//                buffer.append(" activeURI=" + quotes + "" + quotes);
//            }
//            buffer.append(" chatmembers="+quotes+cmbean.getChatmembers()+quotes);
            buffer.append(" />");
            /*buffer.append("<message>" + "<![CDATA[" + cmbean.getMessage()
                    + "]]>");
            buffer.append("</message>");*/
//            buffer.append("</>");
//            buffer.append("<SendingStatus signalid=" + quotes
//                    + cmbean.getSendingStatusid() + quotes + " />");
//            buffer.append("<session callid=" + quotes
//                    + cmbean.getSessionCallid() + quotes + " />");
            buffer.append("</TaskDetailsinfo>");
            Log.d("xml", "composed xml for chat======>" + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return buffer.toString();
        }

    }

 public String composeConferenceInfoXML(String[] conf_callinfo,
                                           String[] participantinfo, String[] chatinfo) {
        StringBuffer buffer = new StringBuffer();
        Log.d("Live", "Inside composeConferenceInfoXML");
        Log.d("FT", "Inside composeConferenceInfoXML");

        try {
            buffer.append("<?xml version=\"1.0\"?>" + "<Conferencecallinfo ");
            buffer.append(" name=" + quotes + conf_callinfo[0] + quotes);
            buffer.append(" callstarttime=" + quotes + conf_callinfo[1]
                    + quotes);
            buffer.append(" sessionid=" + quotes + conf_callinfo[2] + quotes);
            buffer.append(" ConferenceUri=" + quotes + conf_callinfo[3] + quotes);
            buffer.append(" callname=" + quotes + conf_callinfo[4] + quotes+">");

            for (int i = 0; i < participantinfo.length; i++) {
                Log.d("Live", "Inside composeConferenceInfoXML particpant");
                buffer.append("<Participant ");
                buffer.append(" name=");
                buffer.append(quotes).append(participantinfo[i]).append(quotes);
                buffer.append(" status=");
                buffer.append(quotes).append("Connecting").append(quotes);
                buffer.append("/>");
            }

            buffer.append("<NewBuddyChat ");
            buffer.append(" chattype=");
            buffer.append(quotes).append(chatinfo[0]).append(quotes);
            buffer.append(" chatname=");
            buffer.append(quotes).append(chatinfo[1]).append(quotes);
            buffer.append(" chatid=");
            buffer.append(quotes).append(chatinfo[2]).append(quotes);
            buffer.append(" chatmembers=");
            buffer.append(quotes).append(chatinfo[3]).append(quotes);
            buffer.append(" chatcoordinator=");
            buffer.append(quotes).append(chatinfo[4]).append(quotes);
            buffer.append(" conferencename=");
            buffer.append(quotes).append(chatinfo[5]).append(quotes);
            buffer.append(" msgtype=");
            buffer.append(quotes).append(chatinfo[6]).append(quotes);
            buffer.append(" conferenceuri=");
            buffer.append(quotes).append(chatinfo[7]).append(quotes);
            buffer.append("/>");
            buffer.append("<session ");
            buffer.append(" callid=");
            buffer.append(quotes).append(chatinfo[8]).append(quotes);
            buffer.append("/>");
            buffer.append("</Conferencecallinfo>");
            Log.d("FT",
                    "composed xml for conferencecall======>"
                            + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return buffer.toString();
    }
}
