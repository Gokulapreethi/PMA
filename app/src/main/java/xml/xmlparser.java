package xml;

import android.util.Base64;
import android.util.Log;

import com.ase.Appreference;
import com.ase.Bean.ProjectDetailsBean;
import com.ase.Bean.SipNotification;
import com.ase.Bean.TaskDetailsBean;
import com.ase.ContactBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.Forms.FormAccessBean;
import com.ase.chat.ChatBean;

import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.app.GroupMemberAccess;
import org.pjsip.pjsua2.app.MainActivity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by dinesh on 5/2/2016.
 */
public class xmlparser {

    private DocumentBuilderFactory dbf = null;
    private DocumentBuilder db = null;
    private InputSource is = null;
    private Document doc = null;
    private NodeList list = null;
    private NodeList list1 = null;
    private Node node = null, node1 = null;
    private NamedNodeMap nodeMap = null, nodeMap1 = null;
    String tasktime, taskUTCtime;

    public ChatBean parseChat(String xml) {

        ChatBean bean = new ChatBean();
        try {

            dbf = DocumentBuilderFactory.newInstance();

            db = dbf.newDocumentBuilder();
            is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);

            list = doc.getElementsByTagName("Buddychat");
            node = list.item(0);
            nodeMap = node.getAttributes();
            if (nodeMap.getNamedItem("type") != null)
                bean.setType(nodeMap.getNamedItem("type").getNodeValue());
            if (nodeMap.getNamedItem("chatname") != null)
                bean.setChatname(nodeMap.getNamedItem("chatname")
                        .getNodeValue());
            if (nodeMap.getNamedItem("chatid") != null)
                bean.setChatid(nodeMap.getNamedItem("chatid").getNodeValue());
            if (nodeMap.getNamedItem("fromname") != null) {
                Log.i("chat", "fromname !=null");
                bean.setFromUser(nodeMap.getNamedItem("fromname")
                        .getNodeValue());
            }
            if (nodeMap.getNamedItem("toname") != null)
                bean.setToname(nodeMap.getNamedItem("toname").getNodeValue());
            if (nodeMap.getNamedItem("datetime") != null)
                bean.setDatetime(nodeMap.getNamedItem("datetime")
                        .getNodeValue());
            if (nodeMap.getNamedItem("signalid") != null)
                bean.setSignalid(nodeMap.getNamedItem("signalid")
                        .getNodeValue());
            if (nodeMap.getNamedItem("msgtype") != null)
                bean.setMsgtype(nodeMap.getNamedItem("msgtype").getNodeValue());
            if (nodeMap.getNamedItem("scheduled") != null)
                bean.setScheduled(nodeMap.getNamedItem("scheduled").getNodeValue());
            if (nodeMap.getNamedItem("chatmembers") != null)
                bean.setChatmembers(nodeMap.getNamedItem("chatmembers").getNodeValue());
//            if (bean.getType().equals("CallGroupChat") || bean.getType().equals("CallSingleChat")) {
//                if (nodeMap.getNamedItem("activeURI") != null) {
//                    bean.setConferenceuri(nodeMap.getNamedItem("activeURI").getNodeValue());
//                }
//            }
            String msg = doc.getElementsByTagName("message").item(0)
                    .getTextContent();
            Log.d("msdecode", "UTF string chat " + msg);
            bean.setMessage(msg);
//            bean.setUser_name(AppReferences.user_name);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return bean;
        }
    }


    public ChatBean parseAddPeopleOnChat(String xml) {

        ChatBean bean = new ChatBean();
        try {

            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);

            list = doc.getElementsByTagName("Buddychatupdate");
            node = list.item(0);
            nodeMap = node.getAttributes();

            if (nodeMap.getNamedItem("chatid") != null)
                bean.setChatid(nodeMap.getNamedItem("chatid").getNodeValue());
            if (nodeMap.getNamedItem("chatmembers") != null)
                bean.setChatmembers(nodeMap.getNamedItem("chatmembers")
                        .getNodeValue());
            if (nodeMap.getNamedItem("chatname") != null)
                bean.setChatname(nodeMap.getNamedItem("chatname")
                        .getNodeValue());
//            if (nodeMap.getNamedItem("chatcoordinator") != null)
//                bean.setCoordinator(nodeMap.getNamedItem("chatcoordinator")
//                        .getNodeValue());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return bean;
        }
    }

    public ArrayList<String[]> parseCallMessages(String xml) {
        ArrayList<String[]> parse_result = new ArrayList<String[]>();
        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);

            list = doc.getElementsByTagName("coordinator");
            node = list.item(0);
            nodeMap = node.getAttributes();
            String[] own_details = new String[5];

            if (nodeMap.getNamedItem("name") != null)
                own_details[0] = nodeMap.getNamedItem("name").getNodeValue()
                        .toString().trim();

            if (nodeMap.getNamedItem("number") != null)
                own_details[1] = nodeMap.getNamedItem("number").getNodeValue()
                        .toString().trim();

            if (nodeMap.getNamedItem("contacturi") != null)
                own_details[2] = nodeMap.getNamedItem("contacturi")
                        .getNodeValue().toString().trim();

            if (nodeMap.getNamedItem("sessionid") != null)
                own_details[3] = nodeMap.getNamedItem("sessionid")
                        .getNodeValue().toString().trim();

            if (nodeMap.getNamedItem("type") != null)
                own_details[4] = nodeMap.getNamedItem("type")
                        .getNodeValue().toString().trim();
            parse_result.add(own_details);

            list = doc.getElementsByTagName("user");
            for (int i = 0; i < list.getLength(); i++) {
                node = list.item(i);
                nodeMap = node.getAttributes();
                String[] user_details = new String[6];

                if (nodeMap.getNamedItem("name") != null)
                    user_details[0] = nodeMap.getNamedItem("name")
                            .getNodeValue().toString().trim();

                if (nodeMap.getNamedItem("number") != null)
                    user_details[1] = nodeMap.getNamedItem("number")
                            .getNodeValue().toString().trim();

                if (nodeMap.getNamedItem("status") != null)
                    user_details[2] = nodeMap.getNamedItem("status")
                            .getNodeValue().toString().trim();

                if (nodeMap.getNamedItem("contacturi") != null)
                    user_details[3] = nodeMap.getNamedItem("contacturi")
                            .getNodeValue().toString().trim();

                if (nodeMap.getNamedItem("mic") != null)
                    user_details[4] = nodeMap.getNamedItem("mic")
                            .getNodeValue().toString().trim();
                if (nodeMap.getNamedItem("userendpoint") != null) {

                    user_details[5] = nodeMap.getNamedItem("userendpoint")
                            .getNodeValue().toString().trim();
                    Log.i("mailid", "parser mailid--->" + user_details[5]);
                }

                parse_result.add(user_details);
            }

            list = doc.getElementsByTagName("Callinfo");
            if (list != null) {

                node = list.item(0);
                if (node != null) {
                    String[] call_details = new String[6];
                    nodeMap = node.getAttributes();

                    call_details[0] = "Callinfo";

                    if (nodeMap.getNamedItem("conferenceuri") != null)
                        call_details[1] = nodeMap.getNamedItem("conferenceuri").getNodeValue()
                                .toString().trim();

                    if (nodeMap.getNamedItem("conferencename") != null)
                        call_details[2] = nodeMap.getNamedItem("conferencename").getNodeValue()
                                .toString().trim();

                    if (nodeMap.getNamedItem("callstarttime") != null)
                        call_details[3] = nodeMap.getNamedItem("callstarttime")
                                .getNodeValue().toString().trim();

                    if (nodeMap.getNamedItem("newhost") != null)
                        call_details[4] = nodeMap.getNamedItem("newhost")
                                .getNodeValue().toString().trim();

                    if (nodeMap.getNamedItem("callsessionid") != null)
                        call_details[5] = nodeMap.getNamedItem("callsessionid")
                                .getNodeValue().toString().trim();

                    parse_result.add(call_details);
                }

            }
            list = doc.getElementsByTagName("pwhishper");
            if (list != null) {
                for (int i = 0; i < list.getLength(); i++) {

                    node = list.item(i);
                    if (node != null) {
                        nodeMap = node.getAttributes();
                        String[] wisper_details = new String[3];
                        wisper_details[0] = "pwhishper";
                        if (nodeMap.getNamedItem("owner") != null) {
                            wisper_details[1] = nodeMap.getNamedItem("owner")
                                    .getNodeValue().toString().trim();
                        }
                        if (nodeMap.getNamedItem("participants") != null) {
                            wisper_details[2] = nodeMap.getNamedItem("participants")
                                    .getNodeValue().toString().trim();
                        }
                        parse_result.add(wisper_details);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return parse_result;
        }
    }

    public TaskDetailsBean parsePositiveTaskResponse(String val) {
        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
        try {
            JSONObject json = new JSONObject(val);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return taskDetailsBean;
    }
    /*
     <?xml version="1.0"?><TaskDetailsinfo><TaskDetails taskName="Task for Android Dev1" taskDescription="t9" fromUserId="27"
     toUserId="26" taskNo="T00003" plannedStartDateTime="2016-07-01 16:49:14" plannedEndDateTime="2016-07-01 23:55:00"
     isRemainderRequired="Y" remainderFrequency="2016-07-01 23:40:00" signalid="T00003" /></TaskDetailsinfo>
     */

    public TaskDetailsBean parseTaskDetailsSIPMessage(String xml) {
        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();

        try {
//            String result = EmojiParser.parseToAliases(xml);
//            Log.i("parser Log", "description ----- 00 "+ result);

            dbf = DocumentBuilderFactory.newInstance();

            db = dbf.newDocumentBuilder();
            is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);

            list = doc.getElementsByTagName("TaskDetails");
            node = list.item(0);
            nodeMap = node.getAttributes();

            String dateforrow;

            SimpleDateFormat utcDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            utcDateFormat.setTimeZone(utcZone);

            SimpleDateFormat localDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            TimeZone localZone = TimeZone.getTimeZone("UTC");
            localDateFormat.setTimeZone(localZone);

            Log.i("profiledownload", "description ----- 00 " + nodeMap.getNamedItem("taskDescription").getNodeValue());


            if (nodeMap.getNamedItem("toUserId") != null)
                taskDetailsBean.setToUserId(nodeMap.getNamedItem("toUserId").getNodeValue());

            if (nodeMap.getNamedItem("toUserId").getNodeValue().equalsIgnoreCase("")) {
                taskDetailsBean.setToUserId(nodeMap.getNamedItem("taskId").getNodeValue());
            }
            if (nodeMap.getNamedItem("fromUserName") != null)
                taskDetailsBean.setFromUserName(nodeMap.getNamedItem("fromUserName").getNodeValue());

            if (nodeMap.getNamedItem("toUserName") != null)
                taskDetailsBean.setToUserName(nodeMap.getNamedItem("toUserName").getNodeValue());

            if (nodeMap.getNamedItem("taskReceiver") != null)
                taskDetailsBean.setTaskReceiver(nodeMap.getNamedItem("taskReceiver").getNodeValue());

            if (nodeMap.getNamedItem("taskToUsersList") != null
                    && nodeMap.getNamedItem("taskToUsersList").getNodeValue() != null
                    && !nodeMap.getNamedItem("taskToUsersList").getNodeValue().toString().equalsIgnoreCase("null")) {
                taskDetailsBean.setGroupTaskMembers(nodeMap.getNamedItem("taskToUsersList").getNodeValue());
            }

            if (nodeMap.getNamedItem("fromUserId") != null) {
                taskDetailsBean.setFromUserId(nodeMap.getNamedItem("fromUserId").getNodeValue());
            }

            if (nodeMap.getNamedItem("taskRemoveObservers") != null && nodeMap.getNamedItem("taskRemoveObservers").getNodeValue() != null) {
                String removed_observer = nodeMap.getNamedItem("taskRemoveObservers").getNodeValue().toString().trim();
                if (removed_observer != null && !removed_observer.equalsIgnoreCase("") && !removed_observer.equalsIgnoreCase("null")
                        && !removed_observer.equalsIgnoreCase("(null)"))
                    taskDetailsBean.setRejectedObserver(removed_observer);
            }

            if (nodeMap.getNamedItem("taskAddObservers") != null && nodeMap.getNamedItem("taskAddObservers").getNodeValue() != null) {
                String added_observer = nodeMap.getNamedItem("taskAddObservers").getNodeValue().toString().trim();
                if (added_observer != null && !added_observer.equalsIgnoreCase("") && !added_observer.equalsIgnoreCase("null")
                        && !added_observer.equalsIgnoreCase("(null)"))
                    taskDetailsBean.setTaskObservers(added_observer);
            }

//            SimpleDateFormat dateFormatt = new SimpleDateFormat(
//                    "dd/MM/yyyy hh:mm:ss a");
//            tasktime = dateFormatt.format(new Date());
//            tasktime = tasktime.split(" ")[1] + " " + tasktime.split(" ")[2];
//            taskDetailsBean.setTasktime(tasktime);

            if (nodeMap.getNamedItem("dateTime").getNodeValue() != null && !nodeMap.getNamedItem("dateTime").getNodeValue().equalsIgnoreCase("null")) {
                Log.i("UTC", "parser  utc time" + nodeMap.getNamedItem("dateTime").getNodeValue());

                Date date = utcDateFormat.parse(nodeMap.getNamedItem("dateTime").getNodeValue());
                utcDateFormat.setTimeZone(TimeZone.getDefault());
                dateforrow = utcDateFormat.format(date);
                taskDetailsBean.setDateTime(utcDateFormat.format(date));
                taskDetailsBean.setTaskUTCDateTime(nodeMap.getNamedItem("dateTime").getNodeValue());
                Log.i("UTC", "setDateTime utc time" + taskDetailsBean.getDateTime());
                tasktime = taskDetailsBean.getDateTime();
                if (tasktime != null) {
                    tasktime = tasktime.split(" ")[1];
                    taskDetailsBean.setTasktime(tasktime);
//                    taskDetailsBean.setTasktime(taskDetailsBean.getDateTime());
                    Log.i("UTC", "parser utc to local time time" + tasktime);
                }
                taskUTCtime = taskDetailsBean.getTaskUTCDateTime();
                if (taskUTCtime != null) {
//                    taskUTCtime = taskUTCtime.split(" ")[1];
                    taskDetailsBean.setTaskUTCTime(taskUTCtime);
                    Log.i("UTC", "parser utc time" + taskUTCtime);
                }
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                dateforrow = dateFormat.format(new Date());
                taskDetailsBean.setDateTime(dateforrow);
                taskDetailsBean.setTaskUTCDateTime(dateforrow);

                String dateforrow1 = dateforrow.split(" ")[1];
                taskDetailsBean.setTaskUTCTime(dateforrow1);
                taskDetailsBean.setTasktime(dateforrow1);
            }

            if (nodeMap.getNamedItem("plannedStartDateTime").getNodeValue() != null && !nodeMap.getNamedItem("plannedStartDateTime").getNodeValue().equalsIgnoreCase("(null)") && !nodeMap.getNamedItem("plannedStartDateTime").getNodeValue().equalsIgnoreCase("")) {
                taskDetailsBean.setPlannedStartDateTime(Appreference.utcToLocalTime(nodeMap.getNamedItem("plannedStartDateTime").getNodeValue()));
                taskDetailsBean.setUtcPlannedStartDateTime(nodeMap.getNamedItem("plannedStartDateTime").getNodeValue());
            }

            if (nodeMap.getNamedItem("completedPercentage") != null && !nodeMap.getNamedItem("completedPercentage").getNodeValue().equalsIgnoreCase("(null)") && !nodeMap.getNamedItem("completedPercentage").getNodeValue().equalsIgnoreCase(""))
                taskDetailsBean.setCompletedPercentage(nodeMap.getNamedItem("completedPercentage").getNodeValue());

            Log.i("xmlcheck", "taskDetailsBean.getCompletedPercentage() " + taskDetailsBean.getCompletedPercentage());

            if (nodeMap.getNamedItem("plannedEndDateTime").getNodeValue() != null && !nodeMap.getNamedItem("plannedEndDateTime").getNodeValue().equalsIgnoreCase("(null)") && !nodeMap.getNamedItem("plannedEndDateTime").getNodeValue().equalsIgnoreCase("")) {
                taskDetailsBean.setPlannedEndDateTime(Appreference.utcToLocalTime(nodeMap.getNamedItem("plannedEndDateTime").getNodeValue()));
                taskDetailsBean.setUtcplannedEndDateTime(nodeMap.getNamedItem("plannedEndDateTime").getNodeValue());
            }

            if (nodeMap.getNamedItem("isRemainderRequired") != null)
                taskDetailsBean.setIsRemainderRequired(nodeMap.getNamedItem("isRemainderRequired").getNodeValue());

            if (nodeMap.getNamedItem("remainderDateTime").getNodeValue() != null && !nodeMap.getNamedItem("remainderDateTime").getNodeValue().equalsIgnoreCase("(null)") && !nodeMap.getNamedItem("remainderDateTime").getNodeValue().equalsIgnoreCase("")) {
                taskDetailsBean.setRemainderFrequency(Appreference.utcToLocalTime(nodeMap.getNamedItem("remainderDateTime").getNodeValue()));
                taskDetailsBean.setUtcPemainderFrequency(nodeMap.getNamedItem("remainderDateTime").getNodeValue());
            }

            if (nodeMap.getNamedItem("signalid") != null) {
                taskDetailsBean.setSignalid(nodeMap.getNamedItem("signalid").getNodeValue());
                Log.i("parser", "mmfiles **** " + taskDetailsBean.getSignalid());
            }

            if (nodeMap.getNamedItem("parentId") != null)
                taskDetailsBean.setParentId(nodeMap.getNamedItem("parentId").getNodeValue());

            if (nodeMap.getNamedItem("subType") != null)
                taskDetailsBean.setSubType(nodeMap.getNamedItem("subType").getNodeValue());

            if (Pattern.compile(Pattern.quote(nodeMap.getNamedItem("taskName").getNodeValue()), Pattern.CASE_INSENSITIVE).matcher("&lt;").find()
                    || Pattern.compile(Pattern.quote(nodeMap.getNamedItem("taskName").getNodeValue()), Pattern.CASE_INSENSITIVE).matcher("&amp;").find()
                    || Pattern.compile(Pattern.quote(nodeMap.getNamedItem("taskName").getNodeValue()), Pattern.CASE_INSENSITIVE).matcher("&quot;").find()) {
                if (nodeMap.getNamedItem("taskName") != null) {
//                    taskDetailsBean.setTaskName(nodeMap.getNamedItem("taskName").getNodeValue().replaceAll("&lt;", "<"));
                    String taskName = nodeMap.getNamedItem("taskName").getNodeValue();
                    if (taskName.contains("&lt;") || taskName.contains("&amp;") || taskName.contains("&quot;")) {
                        if (taskName.contains("<")) {
                            taskName = taskName.replaceAll("&lt;", "<");
                        }
                        if (taskName.contains("&")) {
                            taskName = taskName.replaceAll("&amp;", "&");
                        }
                        if (taskName.contains("\"")) {
                            taskName = taskName.replaceAll("&quot;", "\"");
                        }
                        taskDetailsBean.setTaskName(taskName);
                    }

                }
            } else {
                taskDetailsBean.setTaskName(nodeMap.getNamedItem("taskName").getNodeValue());
            }

            if (nodeMap.getNamedItem("taskNo") != null)
                taskDetailsBean.setTaskNo(nodeMap.getNamedItem("taskNo").getNodeValue());

            if (nodeMap.getNamedItem("taskId") != null)
                taskDetailsBean.setTaskId(nodeMap.getNamedItem("taskId").getNodeValue());

            if (nodeMap.getNamedItem("requestStatus") != null)
                taskDetailsBean.setRequestStatus(nodeMap.getNamedItem("requestStatus").getNodeValue());

            if (nodeMap.getNamedItem("taskRequestType") != null)
                taskDetailsBean.setTaskRequestType(nodeMap.getNamedItem("taskRequestType").getNodeValue());
            if (taskDetailsBean.getTaskRequestType() != null && !taskDetailsBean.getTaskRequestType().equalsIgnoreCase("") && !taskDetailsBean.getTaskRequestType().equalsIgnoreCase(null) && !taskDetailsBean.getTaskRequestType().equalsIgnoreCase("(null)") && !taskDetailsBean.getTaskRequestType().equalsIgnoreCase("")) {
                if (taskDetailsBean.getTaskRequestType().equalsIgnoreCase("buzzrequest"))
                    taskDetailsBean.setSubType(taskDetailsBean.getTaskRequestType());
            }

            if (nodeMap.getNamedItem("taskPriority") != null) {
                String project_status = nodeMap.getNamedItem("taskStatus").getNodeValue();
                if (project_status != null && (project_status.equalsIgnoreCase("Started") || project_status.equalsIgnoreCase("Start"))) {
                    taskDetailsBean.setProjectStatus("0");
                } else if (project_status != null && project_status.equalsIgnoreCase("Hold")) {
                    taskDetailsBean.setProjectStatus("1");
                } else if (project_status != null && (project_status.equalsIgnoreCase("Resumed") || project_status.equalsIgnoreCase("Resume"))) {
                    taskDetailsBean.setProjectStatus("2");
                } else if (project_status != null && (project_status.equalsIgnoreCase("Paused") || project_status.equalsIgnoreCase("Pause"))) {
                    taskDetailsBean.setProjectStatus("3");
                } else if (project_status != null && (project_status.equalsIgnoreCase("Restarted") || project_status.equalsIgnoreCase("Restart"))) {
                    taskDetailsBean.setProjectStatus("4");
                } else if (project_status != null && (project_status.equalsIgnoreCase("Completed") || project_status.equalsIgnoreCase("Complete"))) {
                    taskDetailsBean.setProjectStatus("5");
                } else if (project_status != null && project_status.equalsIgnoreCase("Unassigned")) {
                    taskDetailsBean.setProjectStatus("8");
                } else {
                    taskDetailsBean.setTaskStatus(nodeMap.getNamedItem("taskStatus").getNodeValue());
                }
            }

            if (nodeMap.getNamedItem("taskStatus") != null)
                taskDetailsBean.setTaskStatus(nodeMap.getNamedItem("taskStatus").getNodeValue());

            if (nodeMap.getNamedItem("taskType") != null)
                taskDetailsBean.setTaskType(nodeMap.getNamedItem("taskType").getNodeValue());

            if (nodeMap.getNamedItem("taskOwner") != null)
                taskDetailsBean.setOwnerOfTask(nodeMap.getNamedItem("taskOwner").getNodeValue());


            if (nodeMap.getNamedItem("timeFrequency") != null && !nodeMap.getNamedItem("timeFrequency").getNodeValue().equalsIgnoreCase("(null)") && !nodeMap.getNamedItem("timeFrequency").getNodeValue().equalsIgnoreCase("null") && !nodeMap.getNamedItem("timeFrequency").getNodeValue().equalsIgnoreCase("")) {
                taskDetailsBean.setTimeFrequency(nodeMap.getNamedItem("timeFrequency").getNodeValue());
                if (taskDetailsBean.getTimeFrequency() != null && !taskDetailsBean.getTimeFrequency().equalsIgnoreCase("") && !taskDetailsBean.getTimeFrequency().equalsIgnoreCase(null) && !taskDetailsBean.getTimeFrequency().equalsIgnoreCase("(null)") && !taskDetailsBean.getTimeFrequency().equalsIgnoreCase("")) {
                    String timefrequency = Appreference.TimeFrequencyConvertion(taskDetailsBean.getTimeFrequency());
                    taskDetailsBean.setTimeFrequency(timefrequency);
                }
            }

            if (nodeMap.getNamedItem("remainderQuotes") != null)
                taskDetailsBean.setReminderQuote(nodeMap.getNamedItem("remainderQuotes").getNodeValue());

            if (nodeMap.getNamedItem("remark") != null)
                taskDetailsBean.setRemark(nodeMap.getNamedItem("remark").getNodeValue());

            if (nodeMap.getNamedItem("daysOfTheWeek") != null)
                taskDetailsBean.setDaysOfTheWeek(nodeMap.getNamedItem("daysOfTheWeek").getNodeValue());

            if (nodeMap.getNamedItem("repeatFrequency") != null)
                taskDetailsBean.setRepeatFrequency(nodeMap.getNamedItem("repeatFrequency").getNodeValue());

            if (nodeMap.getNamedItem("taskTagName") != null)
                taskDetailsBean.setTaskTagName(nodeMap.getNamedItem("taskTagName").getNodeValue());

            if (nodeMap.getNamedItem("taskTagId") != null && !nodeMap.getNamedItem("taskTagId").getNodeValue().equalsIgnoreCase("(null)") && !nodeMap.getNamedItem("taskTagId").getNodeValue().equalsIgnoreCase("")) {
                taskDetailsBean.setCustomTagId(Integer.parseInt(nodeMap.getNamedItem("taskTagId").getNodeValue()));
            }

            if (nodeMap.getNamedItem("projectId") != null)
                taskDetailsBean.setProjectId(nodeMap.getNamedItem("projectId").getNodeValue());

            if (nodeMap.getNamedItem("taskTagGroupId") != null && !nodeMap.getNamedItem("taskTagId").getNodeValue().equalsIgnoreCase("(null)") && !nodeMap.getNamedItem("taskTagId").getNodeValue().equalsIgnoreCase("")) {
                taskDetailsBean.setCustomSetId(Integer.parseInt(nodeMap.getNamedItem("taskTagGroupId").getNodeValue()));
            }

            if (nodeMap.getNamedItem("taskCategory") != null && !nodeMap.getNamedItem("taskCategory").getNodeValue().equalsIgnoreCase("(null)") && !nodeMap.getNamedItem("taskCategory").getNodeValue().equalsIgnoreCase("")) {
                taskDetailsBean.setCatagory(nodeMap.getNamedItem("taskCategory").getNodeValue());
                if (nodeMap.getNamedItem("taskCategory").getNodeValue().equalsIgnoreCase("taskCreation")) {
                    taskDetailsBean.setCatagory("Task");
                } else {
                    taskDetailsBean.setCatagory(nodeMap.getNamedItem("taskCategory").getNodeValue());
                }
            }

            if (nodeMap.getNamedItem("parentTaskId") != null && !nodeMap.getNamedItem("parentTaskId").getNodeValue().equalsIgnoreCase("(null)") && !nodeMap.getNamedItem("parentTaskId").getNodeValue().equalsIgnoreCase("")) {
                taskDetailsBean.setIssueId((nodeMap.getNamedItem("parentTaskId").getNodeValue()));
                Log.i("Parser", "parentTaskId  == >> " + nodeMap.getNamedItem("parentTaskId").getNodeValue());
            }

            if (nodeMap.getNamedItem("isShowOnUI") != null) {
                if (nodeMap.getNamedItem("isShowOnUI").getNodeValue().equalsIgnoreCase("1")) {
                    taskDetailsBean.setCustomTagVisible(true);
                    Log.i("conversation", "schedulecall ** 5 " + taskDetailsBean.isCustomTagVisible());
                } else if (nodeMap.getNamedItem("isShowOnUI").getNodeValue().equalsIgnoreCase("0")) {
                    taskDetailsBean.setCustomTagVisible(false);
                    Log.i("conversation", "schedulecall ** 6 " + taskDetailsBean.isCustomTagVisible());
                } else {
                    taskDetailsBean.setCustomTagVisible(Boolean.valueOf(nodeMap.getNamedItem("isShowOnUI").getNodeValue()));
                    Log.i("conversation", "schedulecall ** 7 " + taskDetailsBean.isCustomTagVisible());
                }
            }
/*            if (nodeMap.getNamedItem("taskStatus") != null)
                taskDetailsBean.(nodeMap.getNamedItem("taskStatus").getNodeValue());*/

            if (nodeMap.getNamedItem("mimeType") != null) {
                taskDetailsBean.setMimeType(nodeMap.getNamedItem("mimeType").getNodeValue());

                if (nodeMap.getNamedItem("mimeType").getNodeValue().equalsIgnoreCase("audio") || nodeMap.getNamedItem("mimeType").getNodeValue().equalsIgnoreCase("document") || nodeMap.getNamedItem("mimeType").getNodeValue().equalsIgnoreCase("date") || nodeMap.getNamedItem("mimeType").getNodeValue().equalsIgnoreCase("video") || nodeMap.getNamedItem("mimeType").getNodeValue().equalsIgnoreCase("image")) {
                    if (nodeMap.getNamedItem("taskDescription") != null)
                        taskDetailsBean.setServerFileName(nodeMap.getNamedItem("taskDescription").getNodeValue());
                    taskDetailsBean.setTaskDescription(nodeMap.getNamedItem("taskDescription").getNodeValue());
                    Log.i("profiledownload", "description--1" + taskDetailsBean.getTaskDescription());
                }
            }

            if (nodeMap.getNamedItem("dateFrequency") != null && !nodeMap.getNamedItem("dateFrequency").getNodeValue().equalsIgnoreCase("(null)") && !nodeMap.getNamedItem("dateFrequency").getNodeValue().equalsIgnoreCase(""))
                taskDetailsBean.setDateFrequency(nodeMap.getNamedItem("dateFrequency").getNodeValue());

            if (nodeMap.getNamedItem("mimeType") != null) {
                taskDetailsBean.setMimeType(nodeMap.getNamedItem("mimeType").getNodeValue());
                byte[] decodedata = Base64.decode(nodeMap.getNamedItem("taskDescription").getNodeValue().trim(), Base64.DEFAULT);
                String text = new String(decodedata, "UTF-8");
                if (nodeMap.getNamedItem("mimeType").getNodeValue().equalsIgnoreCase("text") || nodeMap.getNamedItem("mimeType").getNodeValue().equalsIgnoreCase("date") || nodeMap.getNamedItem("mimeType").getNodeValue().equalsIgnoreCase("Remove") || nodeMap.getNamedItem("mimeType").getNodeValue().equalsIgnoreCase("Reassign") || nodeMap.getNamedItem("mimeType").getNodeValue().equalsIgnoreCase("map")) {
                    if (nodeMap.getNamedItem("taskDescription") != null)
                        if (Pattern.compile(Pattern.quote(nodeMap.getNamedItem("taskDescription").getNodeValue()), Pattern.CASE_INSENSITIVE).matcher("&lt;").find()) {
//                        if (nodeMap.getNamedItem("taskDescription").getNodeValue().contains("&lt;")) {
                            taskDetailsBean.setTaskDescription(text);
                            Log.i("profiledownload", "description-->2" + taskDetailsBean.getTaskDescription());
                        } else if (nodeMap.getNamedItem("taskRequestType") != null && nodeMap.getNamedItem("taskRequestType").getNodeValue().equalsIgnoreCase("customeAttributeRequest")) {
                            taskDetailsBean.setTaskDescription(text);
                            taskDetailsBean.setMimeType("text");
                            taskDetailsBean.setServerFileName(null);
//                            taskDetailsBean.setSignalid(taskDetailsBean.getSignalid());
                            Log.i("parser", "mmfiles ------->>> " + taskDetailsBean.getSignalid());
                            Log.i("profiledownload", "description--3" + taskDetailsBean.getTaskDescription());
                        } else {

//                            byte[] decodedata = Base64.decode(nodeMap.getNamedItem("taskDescription").getNodeValue().trim(), Base64.DEFAULT);
//                            String text = new String(decodedata, "UTF-8");
//                            cmbean.setMessage(text);

                            taskDetailsBean.setTaskDescription(text);
                            String description =taskDetailsBean.getTaskDescription();
                            if(description != null && (description.equalsIgnoreCase("Gathering Details...")
                                    || description.contains("StartTime :") || description.contains("EndTime :"))) {
                                taskDetailsBean.setProjectStatus("7");
                            }
                            Log.i("profiledownload", "description-->4" + taskDetailsBean.getTaskDescription());
                        }

                } else {
                    if (text != null) {
                        if (text.contains("chat/")) {
                            taskDetailsBean.setTaskDescription(text.split("chat/")[1]);
                            Log.i("profiledownload", "description--5" + taskDetailsBean.getTaskDescription());
                        } /*else if(nodeMap.getNamedItem("taskRequestType") != null && nodeMap.getNamedItem("taskRequestType").getNodeValue().equalsIgnoreCase("customeAttributeRequest")) {
                            taskDetailsBean.setTaskDescription(nodeMap.getNamedItem("taskTagName").getNodeValue()+" : "+nodeMap.getNamedItem("taskDescription").getNodeValue());
                        }*/ else {
                            taskDetailsBean.setTaskDescription(text);
                            Log.i("profiledownload", "description--6" + taskDetailsBean.getTaskDescription());
                        }
                    }
                }
                taskDetailsBean.setSendStatus("2");
                Log.i("profiledownload", "description" + taskDetailsBean.getTaskDescription());
                Log.i("profiledownload", "MimeType 123 " + taskDetailsBean.getMimeType());
                if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("textfile")) {
                    Log.i("text_file", "setLongmessage xml 22" + taskDetailsBean.getTaskDescription());
                    taskDetailsBean.setLongmessage("0");
                }
            }



         /*   if (nodeMap.getNamedItem("taskRequestType") != null && nodeMap.getNamedItem("taskRequestType").getNodeValue().equalsIgnoreCase("taskDateChangedApproval") ){
                if(nodeMap.getNamedItem("plannedStartDateTime") != null && nodeMap.getNamedItem("plannedStartDateTime").getNodeValue().equalsIgnoreCase("")){
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date date = dateFormat.parse(Appreference.utcToLocalTime(nodeMap.getNamedItem("plannedStartDateTime").getNodeValue()));
                    Date date1 = dateFormat.parse(dateforrow);
                    if(date.equals(date1) ){

                    }
                }

            }*/
            Log.i("Main--->", " Main 1 " + taskDetailsBean.getTaskDescription());
            Log.i("Main--->", " Main 2 " + taskDetailsBean.getServerFileName());
            Log.i("Main--->", " Main 3 " + taskDetailsBean.isCustomTagVisible());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskDetailsBean;
    }

    public TaskDetailsBean parseWithDrawTaskDetailsSIPMessage(String xml) {
        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();

        try {
            dbf = DocumentBuilderFactory.newInstance();

            db = dbf.newDocumentBuilder();
            is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);

            list = doc.getElementsByTagName("WithdrawTaskDetails");
            node = list.item(0);
            nodeMap = node.getAttributes();

            if (nodeMap.getNamedItem("fromUserName") != null)
                taskDetailsBean.setFromUserName(nodeMap.getNamedItem("fromUserName").getNodeValue());

            if (nodeMap.getNamedItem("toUserName") != null)
                taskDetailsBean.setToUserName(nodeMap.getNamedItem("toUserName").getNodeValue());

            if (nodeMap.getNamedItem("taskId") != null)
                taskDetailsBean.setTaskId(nodeMap.getNamedItem("taskId").getNodeValue());

            if (nodeMap.getNamedItem("signalid") != null)
                taskDetailsBean.setSignalid(nodeMap.getNamedItem("signalid").getNodeValue());

            if (nodeMap.getNamedItem("mimeType") != null) {
                taskDetailsBean.setMimeType(nodeMap.getNamedItem("mimeType").getNodeValue());

                taskDetailsBean.setSendStatus("2");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskDetailsBean;
    }


    public TaskDetailsBean parseFormsValueInSipMessage(String xml) {
        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();

        try {
            dbf = DocumentBuilderFactory.newInstance();

            db = dbf.newDocumentBuilder();
            is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);

            list = doc.getElementsByTagName("FormsDetails");
            node = list.item(0);
            nodeMap = node.getAttributes();


            if (nodeMap.getNamedItem("taskName") != null)
                taskDetailsBean.setTaskName(nodeMap.getNamedItem("taskName").getNodeValue());

            if (nodeMap.getNamedItem("taskNo") != null)
                taskDetailsBean.setTaskNo(nodeMap.getNamedItem("taskNo").getNodeValue());

            if (nodeMap.getNamedItem("taskId") != null)
                taskDetailsBean.setTaskId(nodeMap.getNamedItem("taskId").getNodeValue());

            if (nodeMap.getNamedItem("formId") != null)
                taskDetailsBean.setRemark(nodeMap.getNamedItem("formId").getNodeValue());

            if (nodeMap.getNamedItem("fromUserId") != null)
                taskDetailsBean.setFromUserId(nodeMap.getNamedItem("fromUserId").getNodeValue());

            if (nodeMap.getNamedItem("fromUserName") != null)
                taskDetailsBean.setFromUserName(nodeMap.getNamedItem("fromUserName").getNodeValue());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskDetailsBean;
    }

    public SipNotification parseSipNotificationSIPMessage(String xml) {
        SipNotification notification = new SipNotification();


        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);
            list = doc.getElementsByTagName("NotificationDetails");
            node = list.item(0);
            nodeMap = node.getAttributes();

            Log.i("Groupchat", "parser ** ");
            for (int i = 0; i < list.getLength(); i++) {

                node = list.item(i);
                nodeMap = node.getAttributes();
                if (nodeMap.getNamedItem("datetime") != null) {
                    notification.setUpdated_date(nodeMap.getNamedItem("datetime").getNodeValue());
                }
                if (nodeMap.getNamedItem("signalid") != null) {
                    notification.setSignal_id(nodeMap.getNamedItem("signalid").getNodeValue());
                }
                if (nodeMap.getNamedItem("alerttype") != null) {
                    notification.setAlert_type(nodeMap.getNamedItem("alerttype").getNodeValue());
                }

                Element element = (Element) list.item(i);

                NodeList nodeList = element.getElementsByTagName("groupchange");
                NodeList nodeList1 = element.getElementsByTagName("groupaccess");
                NodeList nodeList2 = element.getElementsByTagName("Project");

                if (nodeList1 != null && nodeList1.getLength() > 0) {
                    Log.i("groupaccess", "parser newuseradded $$ " + nodeList1.getLength());
                    for (int j = 0; j < nodeList1.getLength(); j++) {
                        Node sub_node = (Node) nodeList1.item(j);
                        NamedNodeMap sub_nodemap = sub_node.getAttributes();
                        if (sub_nodemap.getNamedItem("groupid") != null) {
                            notification.setSource_id(
                                    Long.valueOf(sub_nodemap.getNamedItem("groupid").getNodeValue().toString()));
                        }
                        Log.i("groupaccess", "parser groupaccess getSource_id " + notification.getSource_id());
                        if (sub_nodemap.getNamedItem("subtype") != null) {
                            notification.setAlert_sub_type(sub_nodemap.getNamedItem("subtype").getNodeValue().toString());
                            Log.i("groupaccess", "parser groupaccess getAlert_sub_type " + notification.getAlert_sub_type());
                            if (notification.getAlert_sub_type().equals("Access Restrictions")) {
                                GroupMemberAccess groupMemberAccess = new GroupMemberAccess();
                                groupMemberAccess.setGroupId(sub_nodemap.getNamedItem("groupid").getNodeValue().toString());
                                groupMemberAccess.setRespondText(sub_nodemap.getNamedItem("RespondText").getNodeValue().toString());
                                groupMemberAccess.setRespondPhoto(sub_nodemap.getNamedItem("RespondPhoto").getNodeValue().toString());
                                groupMemberAccess.setRespondAudio(sub_nodemap.getNamedItem("RespondAudio").getNodeValue().toString());
                                groupMemberAccess.setRespondVideo(sub_nodemap.getNamedItem("RespondVideo").getNodeValue().toString());
                                groupMemberAccess.setRespondFiles(sub_nodemap.getNamedItem("RespondFiles").getNodeValue().toString());
                                groupMemberAccess.setRespondLocation(sub_nodemap.getNamedItem("RespondLocation").getNodeValue().toString());
                                groupMemberAccess.setRespondSketch(sub_nodemap.getNamedItem("RespondSketch").getNodeValue().toString());
                                groupMemberAccess.setRespondConfCall(sub_nodemap.getNamedItem("RespondConfCall").getNodeValue().toString());
                                groupMemberAccess.setRespondDateChange(sub_nodemap.getNamedItem("RespondDateChange").getNodeValue().toString());
                                groupMemberAccess.setRespondPrivate(sub_nodemap.getNamedItem("RespondPrivate").getNodeValue().toString());
                                groupMemberAccess.setAccessScheduledCNF(sub_nodemap.getNamedItem("AccessScheduledCNF").getNodeValue().toString());
                                groupMemberAccess.setAccessForms(sub_nodemap.getNamedItem("AccessForms").getNodeValue().toString());
                                groupMemberAccess.setAccessReminder(sub_nodemap.getNamedItem("AccessReminder").getNodeValue().toString());
                                groupMemberAccess.setReassignTask(sub_nodemap.getNamedItem("ReassignTask").getNodeValue().toString());
                                groupMemberAccess.setApproveLeave(sub_nodemap.getNamedItem("ApproveLeave").getNodeValue().toString());
                                groupMemberAccess.setAddObserver(sub_nodemap.getNamedItem("AddObserver").getNodeValue().toString());
                                groupMemberAccess.setChangeTaskName(sub_nodemap.getNamedItem("ChangeTaskName").getNodeValue().toString());
                                groupMemberAccess.setTemplateExistingTask(sub_nodemap.getNamedItem("CreateTemplateExistingTask").getNodeValue().toString());
                                groupMemberAccess.setTaskPriority(sub_nodemap.getNamedItem("TaskPriority").getNodeValue().toString());
                                groupMemberAccess.setEscalations(sub_nodemap.getNamedItem("Escalations").getNodeValue().toString());
                                groupMemberAccess.setTaskDescriptions(sub_nodemap.getNamedItem("TaskDescriptions").getNodeValue().toString());
                                groupMemberAccess.setRemindMe(sub_nodemap.getNamedItem("RemindMe").getNodeValue().toString());
                                groupMemberAccess.setAdminAccess(sub_nodemap.getNamedItem("AdminAccess").getNodeValue().toString());
                                groupMemberAccess.setChatAccess(sub_nodemap.getNamedItem("ChatAccess").getNodeValue().toString());
                                groupMemberAccess.setAudioAccess(sub_nodemap.getNamedItem("AudioAccess").getNodeValue().toString());
                                groupMemberAccess.setVideoAccess(sub_nodemap.getNamedItem("VideoAccess").getNodeValue().toString());
                                groupMemberAccess.setGroup_Task(sub_nodemap.getNamedItem("GroupTask").getNodeValue().toString());
                                groupMemberAccess.setRespondTask(sub_nodemap.getNamedItem("RespondTask").getNodeValue().toString());
                                notification.setGroupMemberAccess(groupMemberAccess);
                                Log.i("groupaccess", "parser setGroupMemberAccess " + notification.getGroupMemberAccess().getGroupId());
                            }
                        }
                    }
                } else if (nodeList != null && nodeList.getLength() > 0) {
                    for (int k = 0; k < nodeList.getLength(); k++) {
                        Node sub_node = (Node) nodeList.item(k);
                        NamedNodeMap sub_nodemap = sub_node.getAttributes();
                        Log.i("Groupchat", "parser groupchange ");
                        if (sub_nodemap.getNamedItem("groupid") != null) {
                            notification.setSource_id(
                                    Long.valueOf(sub_nodemap.getNamedItem("groupid").getNodeValue().toString()));
                        }
                        Log.i("Groupchat", "parser groupchange getSource_id " + notification.getSource_id());
                        if (sub_nodemap.getNamedItem("subtype") != null) {
                            notification.setAlert_sub_type(sub_nodemap.getNamedItem("subtype").getNodeValue().toString());
                            if (notification.getAlert_sub_type().equals("Member Added")
                                    || notification.getAlert_sub_type().equals("Member Removed")) {

                                notification.setMember_added(sub_nodemap.getNamedItem("users").getNodeValue().toString());
                                Log.i("Groupchat", "parser groupchange getMember_added " + notification.getMember_added());

                            } else if (notification.getAlert_sub_type().equals("Added To Group")
                                    || notification.getAlert_sub_type().equals("Removed From Group")) {
                                notification.setMember_added(sub_nodemap.getNamedItem("users").getNodeValue().toString());
                                Log.i("Groupchat", "parser groupchange getMember_added another group " + notification.getMember_added());
                            } else if (notification.getAlert_sub_type().equals("Name Change")) {
                                notification.setGroup_name(sub_nodemap.getNamedItem("newgroupname").getNodeValue().toString());
                                Log.i("Groupchat", "parser groupchange getGroup_name " + notification.getGroup_name());
                            }
                        }
                    }
                } else if (nodeList2 != null && nodeList2.getLength() > 0) {
                    for (int l = 0; l < nodeList2.getLength(); l++) {
                        Node sub_node = (Node) nodeList2.item(l);
                        NamedNodeMap sub_nodemap = sub_node.getAttributes();
                        Log.i("Groupchat", "parser project ");
                        if (sub_nodemap.getNamedItem("projectid") != null) {
                            notification.setSource_id(
                                    Long.valueOf(sub_nodemap.getNamedItem("projectid").getNodeValue().toString()));
                        }
                        Log.i("Groupchat", "parser project getSource_id " + notification.getSource_id());
                        if (sub_nodemap.getNamedItem("subtype") != null) {
                            notification.setAlert_sub_type(sub_nodemap.getNamedItem("subtype").getNodeValue().toString());
                            if (notification.getAlert_sub_type().equals("New Project")) {
                                ProjectDetailsBean projectDetailsBean = new ProjectDetailsBean();
                                projectDetailsBean.setId(sub_nodemap.getNamedItem("projectid").getNodeValue().toString());
                                projectDetailsBean.setOwnerOfTask(sub_nodemap.getNamedItem("owner").getNodeValue().toString());
                                projectDetailsBean.setProjectName(sub_nodemap.getNamedItem("name").getNodeValue().toString());
                                notification.setProjectDetailsBean(projectDetailsBean);
                                Log.i("Groupchat", "parser project $$  " + notification.getGroup_name());
                            } else if (notification.getAlert_sub_type().equals("Member Added")) {
                                ProjectDetailsBean projectDetailsBean = new ProjectDetailsBean();
                                projectDetailsBean.setId(sub_nodemap.getNamedItem("projectid").getNodeValue().toString());
                                projectDetailsBean.setOwnerOfTask(sub_nodemap.getNamedItem("owner").getNodeValue().toString());
                                projectDetailsBean.setTaskMemberList(sub_nodemap.getNamedItem("members").getNodeValue().toString());
                                notification.setProjectDetailsBean(projectDetailsBean);
                            } else if (notification.getAlert_sub_type().equals("Percentage update")) {
                                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                                taskDetailsBean.setProjectId(String.valueOf(notification.getSource_id()));
                                if (sub_nodemap.getNamedItem("parenttaskid").getNodeValue() != null) {
                                    taskDetailsBean.setParentTaskId(sub_nodemap.getNamedItem("parenttaskid").getNodeValue().toString());
                                    taskDetailsBean.setTaskId(sub_nodemap.getNamedItem("parenttaskid").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("state").getNodeValue() != null) {
                                    taskDetailsBean.setTaskStatus(sub_nodemap.getNamedItem("state").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("percentage").getNodeValue() != null) {
                                    taskDetailsBean.setCompletedPercentage(sub_nodemap.getNamedItem("percentage").getNodeValue().toString());
                                }
                                notification.setTaskDetailsBean(taskDetailsBean);
                            } else if (notification.getAlert_sub_type().equals("Date Change")) {
                                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                                taskDetailsBean.setProjectId(String.valueOf(notification.getSource_id()));
                                if (sub_nodemap.getNamedItem("parenttaskid").getNodeValue() != null) {
                                    taskDetailsBean.setParentTaskId(sub_nodemap.getNamedItem("parenttaskid").getNodeValue().toString());
                                    taskDetailsBean.setTaskId(sub_nodemap.getNamedItem("parenttaskid").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("state").getNodeValue() != null) {
                                    taskDetailsBean.setTaskStatus(sub_nodemap.getNamedItem("state").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("starttime").getNodeValue() != null) {
                                    taskDetailsBean.setPlannedStartDateTime(sub_nodemap.getNamedItem("starttime").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("endtime").getNodeValue() != null) {
                                    taskDetailsBean.setPlannedEndDateTime(sub_nodemap.getNamedItem("endtime").getNodeValue().toString());
                                }
                                notification.setTaskDetailsBean(taskDetailsBean);
                            } else if (notification.getAlert_sub_type().equals("New Task") || notification.getAlert_sub_type().equalsIgnoreCase("Template")) {
                                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                                taskDetailsBean.setProjectId(String.valueOf(notification.getSource_id()));
                                if (sub_nodemap.getNamedItem("taskId") != null && sub_nodemap.getNamedItem("taskId").getNodeValue() != null) {
                                    taskDetailsBean.setTaskId(sub_nodemap.getNamedItem("taskId").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("signalid") != null && sub_nodemap.getNamedItem("signalid").getNodeValue() != null) {
                                    taskDetailsBean.setSignalid(sub_nodemap.getNamedItem("signalid").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("isGroupTask") != null && sub_nodemap.getNamedItem("isGroupTask").getNodeValue() != null) {
                                    if (sub_nodemap.getNamedItem("isGroupTask").getNodeValue().toString().equalsIgnoreCase("Y")) {
                                        taskDetailsBean.setTaskType("Group");
                                    } else {
                                        taskDetailsBean.setTaskType("Individual");
                                    }
                                }
                                if (sub_nodemap.getNamedItem("dateFrequency") != null && sub_nodemap.getNamedItem("dateFrequency").getNodeValue() != null) {
                                    taskDetailsBean.setDateFrequency(sub_nodemap.getNamedItem("dateFrequency").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("Description") != null && sub_nodemap.getNamedItem("Description").getNodeValue() != null) {
                                    taskDetailsBean.setDescription(sub_nodemap.getNamedItem("Description").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskReceiver") != null && sub_nodemap.getNamedItem("taskReceiver").getNodeValue() != null) {
                                    taskDetailsBean.setTaskReceiver(sub_nodemap.getNamedItem("taskReceiver").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("completedPercentage") != null && sub_nodemap.getNamedItem("completedPercentage").getNodeValue() != null) {
                                    taskDetailsBean.setCompletedPercentage(sub_nodemap.getNamedItem("completedPercentage").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("dateTime") != null && sub_nodemap.getNamedItem("dateTime").getNodeValue() != null) {
                                    taskDetailsBean.setDateTime(sub_nodemap.getNamedItem("dateTime").getNodeValue().toString());
                                    taskDetailsBean.setTasktime(sub_nodemap.getNamedItem("dateTime").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("fromUserId") != null && sub_nodemap.getNamedItem("fromUserId").getNodeValue() != null) {
                                    taskDetailsBean.setFromUserId(sub_nodemap.getNamedItem("fromUserId").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskPriority") != null && sub_nodemap.getNamedItem("taskPriority").getNodeValue() != null) {
                                    taskDetailsBean.setTaskPriority(sub_nodemap.getNamedItem("taskPriority").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskName") != null && sub_nodemap.getNamedItem("taskName").getNodeValue() != null) {
                                    taskDetailsBean.setTaskName(sub_nodemap.getNamedItem("taskName").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("parentId") != null && sub_nodemap.getNamedItem("parentId").getNodeValue() != null) {
                                    taskDetailsBean.setParentId(sub_nodemap.getNamedItem("parentId").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskStatus") != null && sub_nodemap.getNamedItem("taskStatus").getNodeValue() != null) {
                                    taskDetailsBean.setTaskStatus(sub_nodemap.getNamedItem("taskStatus").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("toUserId") != null && sub_nodemap.getNamedItem("toUserId").getNodeValue() != null) {
                                    taskDetailsBean.setToUserId(sub_nodemap.getNamedItem("toUserId").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("toUserName") != null && sub_nodemap.getNamedItem("toUserName").getNodeValue() != null) {
                                    taskDetailsBean.setToUserName(sub_nodemap.getNamedItem("toUserName").getNodeValue().toString());
                                    taskDetailsBean.setGroupTaskMembers(sub_nodemap.getNamedItem("toUserName").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskRequestType") != null && sub_nodemap.getNamedItem("taskRequestType").getNodeValue() != null) {
                                    taskDetailsBean.setTaskRequestType(sub_nodemap.getNamedItem("taskRequestType").getNodeValue().toString());
                                }
//                                if (sub_nodemap.getNamedItem("taskOwner") != null && sub_nodemap.getNamedItem("taskOwner").getNodeValue() != null) {
//                                    taskDetailsBean.setOwnerOfTask(sub_nodemap.getNamedItem("taskOwner").getNodeValue().toString());
//                                }
                                if (sub_nodemap.getNamedItem("taskNo") != null && sub_nodemap.getNamedItem("taskNo").getNodeValue() != null) {
                                    taskDetailsBean.setTaskNo(sub_nodemap.getNamedItem("taskNo").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskCategory") != null && sub_nodemap.getNamedItem("taskCategory").getNodeValue() != null) {
                                    taskDetailsBean.setCatagory(sub_nodemap.getNamedItem("taskCategory").getNodeValue().toString());
                                    if (taskDetailsBean.getCatagory().toLowerCase().contains("task")) {
                                        taskDetailsBean.setCatagory("Task");
                                    }
                                }
                                if (sub_nodemap.getNamedItem("fromUserName") != null && sub_nodemap.getNamedItem("fromUserName").getNodeValue() != null) {
                                    taskDetailsBean.setFromUserName(sub_nodemap.getNamedItem("fromUserName").getNodeValue().toString());
                                    taskDetailsBean.setOwnerOfTask(sub_nodemap.getNamedItem("fromUserName").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("mimeType") != null && sub_nodemap.getNamedItem("mimeType").getNodeValue() != null) {
                                    taskDetailsBean.setMimeType(sub_nodemap.getNamedItem("mimeType").getNodeValue().toString());
                                }

                                taskDetailsBean.setSubType("taskDescription");
                                if (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                                    taskDetailsBean.setTaskReceiver(taskDetailsBean.getTaskName());
                                } else {

                                }
                                if (taskDetailsBean.getTaskDescription() == null) {
                                    taskDetailsBean.setTaskDescription(taskDetailsBean.getTaskName());
                                }
//                                taskDetailsBean.setSignalid(notification.getSignal_id());
                                notification.setTaskDetailsBean(taskDetailsBean);
                            } else if (notification.getAlert_sub_type().equals("Project Deleted")) {
                                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                                if (sub_nodemap.getNamedItem("projectid") != null && sub_nodemap.getNamedItem("projectid").getNodeValue() != null) {
                                    taskDetailsBean.setProjectId(sub_nodemap.getNamedItem("projectid").getNodeValue().toString());

                                }
                                notification.setTaskDetailsBean(taskDetailsBean);
                            } else if (notification.getAlert_sub_type().equalsIgnoreCase("Draft delete")) {
                                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                                if (sub_nodemap.getNamedItem("projectid") != null && sub_nodemap.getNamedItem("projectid").getNodeValue() != null) {
                                    taskDetailsBean.setProjectId(sub_nodemap.getNamedItem("projectid").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskid") != null && sub_nodemap.getNamedItem("taskid").getNodeValue() != null) {
                                    taskDetailsBean.setTaskId(sub_nodemap.getNamedItem("taskid").getNodeValue().toString());
                                }
                                notification.setTaskDetailsBean(taskDetailsBean);
                            } else if (notification.getAlert_sub_type().equalsIgnoreCase("Observer Added")) {
                                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                                if (sub_nodemap.getNamedItem("projectid") != null && sub_nodemap.getNamedItem("projectid").getNodeValue() != null) {
                                    taskDetailsBean.setProjectId(sub_nodemap.getNamedItem("projectid").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("newobserver") != null && sub_nodemap.getNamedItem("newobserver").getNodeValue() != null) {
                                    taskDetailsBean.setTaskAddObservers(sub_nodemap.getNamedItem("newobserver").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskid") != null && sub_nodemap.getNamedItem("taskid").getNodeValue() != null) {
                                    taskDetailsBean.setTaskId(sub_nodemap.getNamedItem("taskid").getNodeValue().toString());
                                }
                                notification.setTaskDetailsBean(taskDetailsBean);
                            } else if (notification.getAlert_sub_type().equalsIgnoreCase("Observer Removed")) {
                                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                                if (sub_nodemap.getNamedItem("projectid") != null && sub_nodemap.getNamedItem("projectid").getNodeValue() != null) {
                                    taskDetailsBean.setProjectId(sub_nodemap.getNamedItem("projectid").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("removeobserver") != null && sub_nodemap.getNamedItem("removeobserver").getNodeValue() != null) {
                                    taskDetailsBean.setTaskRemoveObservers(sub_nodemap.getNamedItem("removeobserver").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskid") != null && sub_nodemap.getNamedItem("taskid").getNodeValue() != null) {
                                    taskDetailsBean.setTaskId(sub_nodemap.getNamedItem("taskid").getNodeValue().toString());
                                }
                                notification.setTaskDetailsBean(taskDetailsBean);
                            } else if (notification.getAlert_sub_type().equalsIgnoreCase("Issue")) {
                                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                                taskDetailsBean.setProjectId(String.valueOf(notification.getSource_id()));
                                if (sub_nodemap.getNamedItem("taskId") != null && sub_nodemap.getNamedItem("taskId").getNodeValue() != null) {
                                    taskDetailsBean.setTaskId(sub_nodemap.getNamedItem("taskId").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("signalid") != null && sub_nodemap.getNamedItem("signalid").getNodeValue() != null) {
                                    taskDetailsBean.setSignalid(sub_nodemap.getNamedItem("signalid").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("isGroupTask") != null && sub_nodemap.getNamedItem("isGroupTask").getNodeValue() != null) {
                                    if (sub_nodemap.getNamedItem("isGroupTask").getNodeValue().toString().equalsIgnoreCase("Y")) {
                                        taskDetailsBean.setTaskType("Group");
                                    } else {
                                        taskDetailsBean.setTaskType("Individual");
                                    }
                                }
                                if (sub_nodemap.getNamedItem("dateFrequency") != null && sub_nodemap.getNamedItem("dateFrequency").getNodeValue() != null) {
                                    taskDetailsBean.setDateFrequency(sub_nodemap.getNamedItem("dateFrequency").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("Description") != null && sub_nodemap.getNamedItem("Description").getNodeValue() != null) {
                                    taskDetailsBean.setDescription(sub_nodemap.getNamedItem("Description").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskReceiver") != null && sub_nodemap.getNamedItem("taskReceiver").getNodeValue() != null) {
                                    taskDetailsBean.setTaskReceiver(sub_nodemap.getNamedItem("taskReceiver").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("completedPercentage") != null && sub_nodemap.getNamedItem("completedPercentage").getNodeValue() != null) {
                                    taskDetailsBean.setCompletedPercentage(sub_nodemap.getNamedItem("completedPercentage").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("dateTime") != null && sub_nodemap.getNamedItem("dateTime").getNodeValue() != null) {
                                    taskDetailsBean.setDateTime(sub_nodemap.getNamedItem("dateTime").getNodeValue().toString());
                                    taskDetailsBean.setTasktime(sub_nodemap.getNamedItem("dateTime").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("fromUserId") != null && sub_nodemap.getNamedItem("fromUserId").getNodeValue() != null) {
                                    taskDetailsBean.setFromUserId(sub_nodemap.getNamedItem("fromUserId").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskPriority") != null && sub_nodemap.getNamedItem("taskPriority").getNodeValue() != null) {
                                    taskDetailsBean.setTaskPriority(sub_nodemap.getNamedItem("taskPriority").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskName") != null && sub_nodemap.getNamedItem("taskName").getNodeValue() != null) {
                                    taskDetailsBean.setTaskName(sub_nodemap.getNamedItem("taskName").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("parentId") != null && sub_nodemap.getNamedItem("parentId").getNodeValue() != null) {
                                    taskDetailsBean.setParentId(sub_nodemap.getNamedItem("parentId").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskStatus") != null && sub_nodemap.getNamedItem("taskStatus").getNodeValue() != null) {
                                    taskDetailsBean.setTaskStatus(sub_nodemap.getNamedItem("taskStatus").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("toUserId") != null && sub_nodemap.getNamedItem("toUserId").getNodeValue() != null) {
                                    taskDetailsBean.setToUserId(sub_nodemap.getNamedItem("toUserId").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("toUserName") != null && sub_nodemap.getNamedItem("toUserName").getNodeValue() != null) {
                                    taskDetailsBean.setToUserName(sub_nodemap.getNamedItem("toUserName").getNodeValue().toString());
                                    taskDetailsBean.setGroupTaskMembers(sub_nodemap.getNamedItem("toUserName").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskRequestType") != null && sub_nodemap.getNamedItem("taskRequestType").getNodeValue() != null) {
                                    taskDetailsBean.setTaskRequestType(sub_nodemap.getNamedItem("taskRequestType").getNodeValue().toString());
                                }
//                                if (sub_nodemap.getNamedItem("taskOwner") != null && sub_nodemap.getNamedItem("taskOwner").getNodeValue() != null) {
//                                    taskDetailsBean.setOwnerOfTask(sub_nodemap.getNamedItem("taskOwner").getNodeValue().toString());
//                                }
                                if (sub_nodemap.getNamedItem("taskNo") != null && sub_nodemap.getNamedItem("taskNo").getNodeValue() != null) {
                                    taskDetailsBean.setTaskNo(sub_nodemap.getNamedItem("taskNo").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskCategory") != null && sub_nodemap.getNamedItem("taskCategory").getNodeValue() != null) {
                                    taskDetailsBean.setCatagory(sub_nodemap.getNamedItem("taskCategory").getNodeValue().toString());
                                    if (taskDetailsBean.getCatagory().toLowerCase().contains("task")) {
                                        taskDetailsBean.setCatagory("Task");
                                    }
                                }
                                if (sub_nodemap.getNamedItem("fromUserName") != null && sub_nodemap.getNamedItem("fromUserName").getNodeValue() != null) {
                                    taskDetailsBean.setFromUserName(sub_nodemap.getNamedItem("fromUserName").getNodeValue().toString());
                                    taskDetailsBean.setOwnerOfTask(sub_nodemap.getNamedItem("fromUserName").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("mimeType") != null && sub_nodemap.getNamedItem("mimeType").getNodeValue() != null) {
                                    taskDetailsBean.setMimeType(sub_nodemap.getNamedItem("mimeType").getNodeValue().toString());
                                }

                                taskDetailsBean.setSubType("taskDescription");
                                if (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                                    taskDetailsBean.setTaskReceiver(taskDetailsBean.getTaskName());
                                } else {

                                }
                                if (taskDetailsBean.getTaskDescription() == null) {
                                    taskDetailsBean.setTaskDescription(taskDetailsBean.getTaskName());
                                }

                                notification.setTaskDetailsBean(taskDetailsBean);
                            } else if (notification.getAlert_sub_type().equalsIgnoreCase("Reassign Task")) {
                                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                                if (sub_nodemap.getNamedItem("projectid") != null && sub_nodemap.getNamedItem("projectid").getNodeValue() != null) {
                                    taskDetailsBean.setProjectId(sub_nodemap.getNamedItem("projectid").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskid") != null && sub_nodemap.getNamedItem("taskid").getNodeValue() != null) {
                                    taskDetailsBean.setTaskId(sub_nodemap.getNamedItem("taskid").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("parenttaskid") != null && sub_nodemap.getNamedItem("parenttaskid").getNodeValue() != null) {
                                    taskDetailsBean.setParentTaskId(sub_nodemap.getNamedItem("parenttaskid").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("reassigntaskfrom") != null && sub_nodemap.getNamedItem("reassigntaskfrom").getNodeValue() != null) {
                                    taskDetailsBean.setReAssignFrom(sub_nodemap.getNamedItem("reassigntaskfrom").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("reassigntaskto") != null && sub_nodemap.getNamedItem("reassigntaskto").getNodeValue() != null) {
                                    taskDetailsBean.setReAssignTo(sub_nodemap.getNamedItem("reassigntaskto").getNodeValue().toString());
                                }
                                notification.setTaskDetailsBean(taskDetailsBean);

                            } else if (notification.getAlert_sub_type().equalsIgnoreCase("taskDateChangedApproval")) {
                                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
//                                if (sub_nodemap.getNamedItem("datetime") != null) {
//                                    taskDetailsBean.setDateTime(sub_nodemap.getNamedItem("datetime")
//                                            .getNodeValue());
//                                    taskDetailsBean.setTasktime(sub_nodemap.getNamedItem("datetime")
//                                            .getNodeValue());
//                                }
                                if (sub_nodemap.getNamedItem("projectid") != null && sub_nodemap.getNamedItem("projectid").getNodeValue() != null) {
                                    taskDetailsBean.setProjectId(sub_nodemap.getNamedItem("projectid").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("subtype") != null && sub_nodemap.getNamedItem("subtype").getNodeValue() != null) {
                                    taskDetailsBean.setSubType("normal");
                                }
                                if (sub_nodemap.getNamedItem("taskName") != null && sub_nodemap.getNamedItem("taskName").getNodeValue() != null) {
                                    taskDetailsBean.setTaskName(sub_nodemap.getNamedItem("taskName").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("fromUserId") != null && sub_nodemap.getNamedItem("fromUserId").getNodeValue() != null) {
                                    taskDetailsBean.setFromUserId(sub_nodemap.getNamedItem("fromUserId").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("fromUserName") != null && sub_nodemap.getNamedItem("fromUserName").getNodeValue() != null) {
                                    taskDetailsBean.setFromUserName(sub_nodemap.getNamedItem("fromUserName").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("toUserName") != null && sub_nodemap.getNamedItem("toUserName").getNodeValue() != null) {
                                    taskDetailsBean.setToUserName(sub_nodemap.getNamedItem("toUserName").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskNo") != null && sub_nodemap.getNamedItem("taskNo").getNodeValue() != null) {
                                    taskDetailsBean.setTaskNo(sub_nodemap.getNamedItem("taskNo").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskId") != null && sub_nodemap.getNamedItem("taskId").getNodeValue() != null) {
                                    taskDetailsBean.setTaskId(sub_nodemap.getNamedItem("taskId").getNodeValue().toString());
                                    taskDetailsBean.setToUserId(sub_nodemap.getNamedItem("taskId").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskType") != null && sub_nodemap.getNamedItem("taskType").getNodeValue() != null) {
                                    if (sub_nodemap.getNamedItem("taskType").getNodeValue().toString().equalsIgnoreCase("N")) {
                                        taskDetailsBean.setTaskType("Individual");
                                    } else {
                                        taskDetailsBean.setTaskType("Group");
                                    }
                                }
                                if (sub_nodemap.getNamedItem("plannedStartDateTime") != null && sub_nodemap.getNamedItem("plannedStartDateTime").getNodeValue() != null) {
                                    taskDetailsBean.setPlannedStartDateTime(Appreference.utcToLocalTime(sub_nodemap.getNamedItem("plannedStartDateTime").getNodeValue().toString().substring(0, 19)));
                                    taskDetailsBean.setUtcPlannedStartDateTime(sub_nodemap.getNamedItem("plannedStartDateTime").getNodeValue().toString().substring(0, 19));
                                }

                                if (sub_nodemap.getNamedItem("plannedEndDateTime") != null && sub_nodemap.getNamedItem("plannedEndDateTime").getNodeValue() != null) {
                                    taskDetailsBean.setPlannedEndDateTime(Appreference.utcToLocalTime(sub_nodemap.getNamedItem("plannedEndDateTime").getNodeValue().toString().substring(0, 19)));
                                    taskDetailsBean.setUtcplannedEndDateTime(sub_nodemap.getNamedItem("plannedEndDateTime").getNodeValue().toString().substring(0, 19));
                                }
                                if (sub_nodemap.getNamedItem("isRemainderRequired") != null && sub_nodemap.getNamedItem("isRemainderRequired").getNodeValue() != null) {
                                    taskDetailsBean.setIsRemainderRequired(sub_nodemap.getNamedItem("isRemainderRequired").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("remainderDateTime") != null && sub_nodemap.getNamedItem("remainderDateTime").getNodeValue() != null) {
                                    taskDetailsBean.setRemainderFrequency(Appreference.utcToLocalTime(sub_nodemap.getNamedItem("remainderDateTime").getNodeValue().toString().substring(0, 19)));
                                    taskDetailsBean.setUtcPemainderFrequency(sub_nodemap.getNamedItem("remainderDateTime").getNodeValue().toString().substring(0, 19));
                                }
                                if (sub_nodemap.getNamedItem("signalid") != null && sub_nodemap.getNamedItem("signalid").getNodeValue() != null) {
                                    taskDetailsBean.setSignalid(sub_nodemap.getNamedItem("signalid").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("parentId") != null && sub_nodemap.getNamedItem("parentId").getNodeValue() != null) {
                                    taskDetailsBean.setParentId(sub_nodemap.getNamedItem("parentId").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskRequestType") != null && sub_nodemap.getNamedItem("taskRequestType").getNodeValue() != null) {
                                    taskDetailsBean.setTaskRequestType(sub_nodemap.getNamedItem("taskRequestType").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("timefrequency") != null && sub_nodemap.getNamedItem("timefrequency").getNodeValue() != null) {
                                    taskDetailsBean.setTimeFrequency(Appreference.TimeFrequencyConvertion(sub_nodemap.getNamedItem("timefrequency").getNodeValue().toString()));

                                }

                                if (sub_nodemap.getNamedItem("mimeType") != null && sub_nodemap.getNamedItem("mimeType").getNodeValue() != null) {
                                    taskDetailsBean.setMimeType(sub_nodemap.getNamedItem("mimeType").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("dateTime") != null && sub_nodemap.getNamedItem("dateTime").getNodeValue() != null) {
                                    String date_times = Appreference.utcToLocalTime(sub_nodemap.getNamedItem("dateTime").getNodeValue().toString().substring(0, 19));
                                    taskDetailsBean.setDateTime(date_times);
                                    taskDetailsBean.setTasktime(date_times.split(" ")[1]);
                                    taskDetailsBean.setTaskUTCDateTime(sub_nodemap.getNamedItem("dateTime").getNodeValue().toString().substring(0, 19));
                                    taskDetailsBean.setTaskUTCTime(sub_nodemap.getNamedItem("dateTime").getNodeValue().toString().substring(0, 19));
                                }
                                if (sub_nodemap.getNamedItem("remainderQuotes") != null && sub_nodemap.getNamedItem("remainderQuotes").getNodeValue() != null) {
                                    taskDetailsBean.setReminderQuote(sub_nodemap.getNamedItem("remainderQuotes").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskOwner") != null && sub_nodemap.getNamedItem("taskOwner").getNodeValue() != null) {
                                    taskDetailsBean.setOwnerOfTask(sub_nodemap.getNamedItem("taskOwner").getNodeValue().toString());
                                }
//                                if (sub_nodemap.getNamedItem("taskReceiver") != null && sub_nodemap.getNamedItem("taskReceiver").getNodeValue() != null) {
//                                    taskDetailsBean.setTaskReceiver(sub_nodemap.getNamedItem("taskReceiver").getNodeValue().toString());
//                                }
                                if (sub_nodemap.getNamedItem("taskToUsersList") != null && sub_nodemap.getNamedItem("taskToUsersList").getNodeValue() != null) {
                                    taskDetailsBean.setGroupTaskMembers(sub_nodemap.getNamedItem("taskToUsersList").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskObservers") != null && sub_nodemap.getNamedItem("taskObservers").getNodeValue() != null) {
                                    taskDetailsBean.setTaskObservers(sub_nodemap.getNamedItem("taskObservers").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("requestStatus") != null && sub_nodemap.getNamedItem("requestStatus").getNodeValue() != null) {
                                    taskDetailsBean.setTaskDescription(sub_nodemap.getNamedItem("requestStatus").getNodeValue().toString());
                                    taskDetailsBean.setRequestStatus(sub_nodemap.getNamedItem("requestStatus").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("projectId") != null && sub_nodemap.getNamedItem("projectId").getNodeValue() != null) {
                                    taskDetailsBean.setProjectId(sub_nodemap.getNamedItem("projectId").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("taskCategory") != null && sub_nodemap.getNamedItem("taskCategory").getNodeValue() != null) {
                                    taskDetailsBean.setCatagory(sub_nodemap.getNamedItem("taskCategory").getNodeValue().toString());
                                }
                                if (sub_nodemap.getNamedItem("parentTaskId") != null && sub_nodemap.getNamedItem("parentTaskId").getNodeValue() != null) {
                                    taskDetailsBean.setParentTaskId(sub_nodemap.getNamedItem("parentTaskId").getNodeValue().toString());
                                }
                                taskDetailsBean.setTaskStatus("inprogress");
                                if (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Individual")) {
                                    taskDetailsBean.setTaskReceiver(sub_nodemap.getNamedItem("toUserName").getNodeValue().toString());
                                } else {
                                    taskDetailsBean.setTaskReceiver(taskDetailsBean.getTaskName());
                                }
                                notification.setTaskDetailsBean(taskDetailsBean);
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return notification;
    }


    public TaskDetailsBean parseCustomTagsDeleteSIPMessage(String xml) {
        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();

        try {
            dbf = DocumentBuilderFactory.newInstance();

            db = dbf.newDocumentBuilder();
            is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);

            list = doc.getElementsByTagName("removeCustomeTag");
            node = list.item(0);
            nodeMap = node.getAttributes();

            if (nodeMap.getNamedItem("fromUserName") != null)
                taskDetailsBean.setFromUserName(nodeMap.getNamedItem("fromUserName").getNodeValue());

            if (nodeMap.getNamedItem("toUserName") != null)
                taskDetailsBean.setToUserName(nodeMap.getNamedItem("toUserName").getNodeValue());

            if (nodeMap.getNamedItem("taskno") != null)
                taskDetailsBean.setTaskId(nodeMap.getNamedItem("taskno").getNodeValue());

            if (nodeMap.getNamedItem("taskTagGroupId") != null && !nodeMap.getNamedItem("taskTagGroupId").getNodeValue().equalsIgnoreCase("(null)") && nodeMap.getNamedItem("taskTagGroupId").getNodeValue().equalsIgnoreCase(""))
                taskDetailsBean.setCustomSetId(Integer.parseInt(nodeMap.getNamedItem("taskTagGroupId").getNodeValue()));

            if (nodeMap.getNamedItem("taskRequestType") != null) {
                taskDetailsBean.setTaskRequestType(nodeMap.getNamedItem("taskRequestType").getNodeValue());

                if (nodeMap.getNamedItem("subType") != null) {
                    taskDetailsBean.setSubType(nodeMap.getNamedItem("subType").getNodeValue());


                    taskDetailsBean.setSendStatus("2");
                }

                if (nodeMap.getNamedItem("taskId") != null) {
                    taskDetailsBean.setTaskId(nodeMap.getNamedItem("taskId").getNodeValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskDetailsBean;
    }


    public TaskDetailsBean parseObserverDetailsSIPMessage(String xml) {
        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();

        try {
            dbf = DocumentBuilderFactory.newInstance();

            db = dbf.newDocumentBuilder();
            is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);

            list = doc.getElementsByTagName("AddObserver");
            node = list.item(0);
            nodeMap = node.getAttributes();

            if (nodeMap.getNamedItem("taskReceiver") != null) {
                taskDetailsBean.setToUserName(nodeMap.getNamedItem("taskReceiver").getNodeValue());
                taskDetailsBean.setTaskReceiver(nodeMap.getNamedItem("taskReceiver").getNodeValue());
//                taskDetailsBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));

                ContactBean contactBean = VideoCallDataBase.getDB(MainActivity.mainContext).getContactObject(nodeMap.getNamedItem("taskReceiver").getNodeValue());

                Log.d("TaskObserver", "get To User Id is == " + contactBean.getUserid());
                taskDetailsBean.setToUserId(String.valueOf(contactBean.getUserid()));

            }

            if (nodeMap.getNamedItem("taskOwner") != null) {
                taskDetailsBean.setOwnerOfTask(nodeMap.getNamedItem("taskOwner").getNodeValue());
                taskDetailsBean.setFromUserName(nodeMap.getNamedItem("taskOwner").getNodeValue());
                ContactBean contactBean1 = VideoCallDataBase.getDB(MainActivity.mainContext).getContactObject(nodeMap.getNamedItem("taskOwner").getNodeValue());
                taskDetailsBean.setFromUserId(String.valueOf(contactBean1.getUserid()));
            }

            String TaskOwner = VideoCallDataBase.getDB(MainActivity.mainContext).getname(taskDetailsBean.getOwnerOfTask());

            if (nodeMap.getNamedItem("taskRemoveObservers") != null)
                taskDetailsBean.setRejectedObserver(nodeMap.getNamedItem("taskRemoveObservers").getNodeValue());


            if (nodeMap.getNamedItem("completedPercentage") != null)
                taskDetailsBean.setCompletedPercentage(nodeMap.getNamedItem("completedPercentage").getNodeValue());

            if (nodeMap.getNamedItem("taskPriority") != null)
                taskDetailsBean.setTaskPriority(nodeMap.getNamedItem("taskPriority").getNodeValue());

            if (nodeMap.getNamedItem("dateTime") != null) {
                taskDetailsBean.setTaskUTCDateTime(nodeMap.getNamedItem("dateTime").getNodeValue());
                taskDetailsBean.setDateTime(Appreference.utcToLocalTime(nodeMap.getNamedItem("dateTime").getNodeValue()));
                taskDetailsBean.setTasktime(Appreference.utcToLocalTime(nodeMap.getNamedItem("dateTime").getNodeValue()).split(" ")[1]);
                taskDetailsBean.setTaskUTCTime(nodeMap.getNamedItem("dateTime").getNodeValue());
            }

            if (nodeMap.getNamedItem("signalid") != null)
                taskDetailsBean.setSignalid(nodeMap.getNamedItem("signalid").getNodeValue());

            if (nodeMap.getNamedItem("parentId") != null)
                taskDetailsBean.setParentId(nodeMap.getNamedItem("parentId").getNodeValue());

            if (nodeMap.getNamedItem("taskStatus") != null) {
                taskDetailsBean.setTaskStatus(nodeMap.getNamedItem("taskStatus").getNodeValue());
            }

            if (nodeMap.getNamedItem("taskAddObservers") != null) {
                String observers = nodeMap.getNamedItem("taskAddObservers").getNodeValue();
                String obervernames = null;
                Log.i("observername", "observers 1 " + observers);
                if (observers.contains(",")) {
                    String[] observers_array = observers.split(",");
                    for (String s : observers_array) {
                        if (obervernames == null) {
                            obervernames = VideoCallDataBase.getDB(Appreference.mainContect).getname(s);
                            Log.i("observername", "obervernames 2 " + obervernames);
                        } else {
                            obervernames = obervernames + "," + VideoCallDataBase.getDB(Appreference.mainContect).getname(s);
                            Log.i("observername", "obervernames 3 " + obervernames);
                        }
                    }
                } else {
                    obervernames = VideoCallDataBase.getDB(Appreference.mainContect).getname(observers);
                    Log.i("observer name", "obervernames 4.1 " + obervernames);
                }
                if (obervernames == null) {
                    obervernames = observers;
                    taskDetailsBean.setTaskDescription("");
                    Log.i("observer name", "obervernames 5 " + obervernames);
                } else if (obervernames.contains("null")) {
                    taskDetailsBean.setTaskDescription("");
                    Log.i("observer name", "obervernames 5.1 " + obervernames);
                } else {
                    taskDetailsBean.setTaskDescription(TaskOwner + " added " + obervernames + " as observer");
                    Log.i("observer name", "obervernames 6 " + obervernames);
                }


                Log.i("observer name", "observers 1 " + obervernames);
                taskDetailsBean.setTaskObservers(nodeMap.getNamedItem("taskAddObservers").getNodeValue());
            }
           /* if (nodeMap.getNamedItem("taskName") != null)
                taskDetailsBean.setTaskName(nodeMap.getNamedItem("taskName").getNodeValue());*/

            if (Pattern.compile(Pattern.quote(nodeMap.getNamedItem("taskName").getNodeValue()), Pattern.CASE_INSENSITIVE).matcher("&lt;").find()
                    || Pattern.compile(Pattern.quote(nodeMap.getNamedItem("taskName").getNodeValue()), Pattern.CASE_INSENSITIVE).matcher("&amp;").find()
                    || Pattern.compile(Pattern.quote(nodeMap.getNamedItem("taskName").getNodeValue()), Pattern.CASE_INSENSITIVE).matcher("&quot;").find()) {
                if (nodeMap.getNamedItem("taskName") != null) {
//                    taskDetailsBean.setTaskName(nodeMap.getNamedItem("taskName").getNodeValue().replaceAll("&lt;", "<"));
                    String taskName = nodeMap.getNamedItem("taskName").getNodeValue();
                    if (taskName.contains("&lt;") || taskName.contains("&amp;") || taskName.contains("&quot;")) {
                        if (taskName.contains("<")) {
                            taskName = taskName.replaceAll("&lt;", "<");
                        }
                        if (taskName.contains("&")) {
                            taskName = taskName.replaceAll("&amp;", "&");
                        }
                        if (taskName.contains("\"")) {
                            taskName = taskName.replaceAll("&quot;", "\"");
                        }
                        taskDetailsBean.setTaskName(taskName);
                    }

                }
            } else {
                taskDetailsBean.setTaskName(nodeMap.getNamedItem("taskName").getNodeValue());
            }


            if (nodeMap.getNamedItem("taskCategory") != null && !nodeMap.getNamedItem("taskCategory").getNodeValue().equalsIgnoreCase("(null)") && !nodeMap.getNamedItem("taskCategory").getNodeValue().equalsIgnoreCase("")) {
                taskDetailsBean.setCatagory(nodeMap.getNamedItem("taskCategory").getNodeValue());
                if (nodeMap.getNamedItem("taskCategory").getNodeValue().equalsIgnoreCase("taskCreation")) {
                    taskDetailsBean.setCatagory("Task");
                } else {
                    taskDetailsBean.setCatagory(nodeMap.getNamedItem("taskCategory").getNodeValue());
                }
            }

            if (nodeMap.getNamedItem("projectId") != null)
                taskDetailsBean.setProjectId(nodeMap.getNamedItem("projectId").getNodeValue());

            if (nodeMap.getNamedItem("taskNo") != null)
                taskDetailsBean.setTaskNo(nodeMap.getNamedItem("taskNo").getNodeValue());

            if (nodeMap.getNamedItem("taskId") != null) {
                taskDetailsBean.setTaskId(nodeMap.getNamedItem("taskId").getNodeValue());
                taskDetailsBean.setMimeType("observer");
                taskDetailsBean.setSendStatus("3");
                Log.i("conversation", "schedulecall * 1 " + taskDetailsBean.isCustomTagVisible());
                taskDetailsBean.setCustomTagVisible(true);
                taskDetailsBean.setTaskType("Individual");
            }

            if (nodeMap.getNamedItem("isShowOnUI") != null) {
                if (nodeMap.getNamedItem("isShowOnUI").getNodeValue().equalsIgnoreCase("1")) {
                    taskDetailsBean.setCustomTagVisible(true);
                    Log.i("conversation", "schedulecall * 2 " + taskDetailsBean.isCustomTagVisible());
                } else if (nodeMap.getNamedItem("isShowOnUI").getNodeValue().equalsIgnoreCase("0")) {
                    taskDetailsBean.setCustomTagVisible(false);
                    Log.i("conversation", "schedulecall # 3 " + taskDetailsBean.isCustomTagVisible());
                } else {
                    taskDetailsBean.setCustomTagVisible(Boolean.valueOf(nodeMap.getNamedItem("isShowOnUI").getNodeValue()));
                    Log.i("conversation", "schedulecall ** 4 " + taskDetailsBean.isCustomTagVisible());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskDetailsBean;
    }

    public String parseConferenceCallInfo(String xml) {

//        ConferenceLiveCallBean bean = new ConferenceLiveCallBean();
//        ArrayList<ParticipantBean> participantBeanList = new ArrayList<ParticipantBean>();
        String call = null;
        try {

            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);

            list = doc.getElementsByTagName("Conferencecallinfo");
            node = list.item(0);
            nodeMap = node.getAttributes();
            if (nodeMap.getNamedItem("callstarttime") != null)
//                bean.setStartTime(nodeMap.getNamedItem("callstarttime")
//                        .getNodeValue());

//            ParticipantBean parBean = new ParticipantBean();
                if (nodeMap.getNamedItem("name") != null) {
//                parBean.setFirst_name(nodeMap.getNamedItem("name")
//                        .getNodeValue());
//                parBean.setSipEndPoint(nodeMap.getNamedItem("name")
//                        .getNodeValue());
                }

            if (nodeMap.getNamedItem("callname") != null) {
                call = nodeMap.getNamedItem("callname").getNodeValue();
            }
//            if (nodeMap.getNamedItem("sipendpoint") != null) {
//                parBean.setSipEndPoint(nodeMap.getNamedItem("sipendpoint")
//                        .getNodeValue());
//            }
//            parBean.setIsOrganizer("1");
//            participantBeanList.add(parBean);

            list = doc.getElementsByTagName("Participant");
            int len = list.getLength();
            for (int i = 0; i < len; i++) {
                node = list.item(i);
                nodeMap = node.getAttributes();
//                ParticipantBean pBean = new ParticipantBean();
                if (nodeMap.getNamedItem("name") != null) {
//                    pBean.setFirst_name(nodeMap.getNamedItem("name")
//                            .getNodeValue());
//                    pBean.setSipEndPoint(nodeMap.getNamedItem("name")
//                            .getNodeValue());
                }
                if (nodeMap.getNamedItem("status") != null) {
//                    pBean.setCall_status(nodeMap.getNamedItem("status")
//                            .getNodeValue());
                }
//                if (nodeMap.getNamedItem("sipendpoint") != null) {
//                    pBean.setSipEndPoint(nodeMap.getNamedItem("sipendpoint")
//                            .getNodeValue());
//                }

//                pBean.setIsOrganizer("0");
//                participantBeanList.add(pBean);
            }
//            bean.setAllParticipants(participantBeanList);
            list = doc.getElementsByTagName("NewBuddyChat");
            node = list.item(0);
            nodeMap = node.getAttributes();

            if (nodeMap.getNamedItem("chattype") != null) {
//                bean.setChatType(nodeMap.getNamedItem("chattype").getNodeValue());
            }
            if (nodeMap.getNamedItem("chatname") != null) {
//                bean.setChatName(nodeMap.getNamedItem("chatname").getNodeValue());
            }
            if (nodeMap.getNamedItem("chatid") != null) {
//                bean.setChatID(nodeMap.getNamedItem("chatid").getNodeValue());
            }
            if (nodeMap.getNamedItem("chatmembers") != null) {
//                bean.setChatMembers(nodeMap.getNamedItem("chatmembers").getNodeValue());
            }
            if (nodeMap.getNamedItem("chatcoordinator") != null) {
//                bean.setChatCoordinator(nodeMap.getNamedItem("chatcoordinator").getNodeValue());
            }

            if (nodeMap.getNamedItem("conferencename") != null) {
//                bean.setName(nodeMap.getNamedItem("conferencename")
//                        .getNodeValue());
            }
            if (nodeMap.getNamedItem("conferenceuri") != null) {
//                bean.setUri(nodeMap.getNamedItem("conferenceuri")
//                        .getNodeValue());
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return call;
    }

    public ArrayList<FormAccessBean> parseFromAccess(String xml) {
        ArrayList<FormAccessBean> formAccessBeen = new ArrayList<>();
        try {
            dbf = DocumentBuilderFactory.newInstance();

            db = dbf.newDocumentBuilder();
            is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);

            list1 = doc.getElementsByTagName("FormAccess");
            node1 = list1.item(0);
            nodeMap1 = node1.getAttributes();

            list1 = doc.getElementsByTagName("FormAccessInfo");
            node1 = list1.item(0);
            nodeMap1 = node1.getAttributes();

            list = doc.getElementsByTagName("MemberAccess");

            for (int i = 0; i < list.getLength(); i++) {
                node = list.item(i);
                nodeMap = node.getAttributes();
                FormAccessBean formAccessBean = new FormAccessBean();
                if (nodeMap.getNamedItem("formAccessId") != null) {
                    formAccessBean.setFromAccessId(nodeMap.getNamedItem("formAccessId").getNodeValue());
                }

                if (nodeMap.getNamedItem("memberName") != null) {
                    formAccessBean.setMemberName(nodeMap.getNamedItem("memberName").getNodeValue());
                }

                if (nodeMap.getNamedItem("accessMode") != null) {
                    formAccessBean.setAccessMode(nodeMap.getNamedItem("accessMode").getNodeValue());
                }

//                if (nodeMap1.getNamedItem("signalId") != null) {
//                formAccessBean.setSignalId(nodeMap1.getNamedItem("signalId").getNodeValue());
//                }

                if (nodeMap1.getNamedItem("taskId") != null) {
                    formAccessBean.setTaskId(nodeMap1.getNamedItem("taskId").getNodeValue());
                }

                if (nodeMap1.getNamedItem("formId") != null) {
                    formAccessBean.setFormId(nodeMap1.getNamedItem("formId").getNodeValue());
                }

                if (nodeMap1.getNamedItem("taskGiver") != null) {
                    formAccessBean.setGiver(nodeMap1.getNamedItem("taskGiver").getNodeValue());
                }

                formAccessBeen.add(formAccessBean);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formAccessBeen;
    }

    public <T> void foo(T bar) {

    }


}
