package com.example.ttucu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class Ministries extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText ministryEvent, description;

    TextView timeToStart, dateToStart, dateToEnd, sendMessage;
    String dateType="";

    String mId="";
    Button music_button, evangelism_button, lighters_button, hospitality_button, hcm_button, intercessory_button,
            ushering_button, sunday_school_button, media_button, nurturing_button;
    public  static  Switch joinMinistry;
    int assignId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_church_programme);
        music_button=findViewById(R.id.music_button);
        music_button.animate().rotationX(720).setDuration(100);
        music_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId="1";
                Ministry("1","Music Ministry", "MUSIC MINISTRY MEMBERS");
            }
        });
        hospitality_button=findViewById(R.id.hospitality_button);
        hospitality_button.animate().rotationX(720).setDuration(700);
        hospitality_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId="2";
                Ministry("2","Hospitality Ministry", "HOSPITALITY MINISTRY MEMBERS");
            }
        });
        media_button=findViewById(R.id.media_button);
        media_button.animate().rotationX(720).setDuration(800);
        media_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId="3";
                Ministry("3","Media Ministry", "MEDIA MINISTRY MEMBERS");
            }
        });
        lighters_button=findViewById(R.id.lighters_button);
        lighters_button.animate().rotationX(720).setDuration(200);
        lighters_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId="4";
                Ministry("4","Lighters Ministry", "LIGHTERS MINISTRY MEMBERS");
            }
        });
        evangelism_button=findViewById(R.id.evangelism_button);
        evangelism_button.animate().rotationX(720).setDuration(300);
        evangelism_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId="5";
                Ministry("5","Evangelism Ministry", "EVANGELISM MINISTRY MEMBERS");
            }
        });
        hcm_button=findViewById(R.id.hcm_button);
        hcm_button.animate().rotationX(720).setDuration(500);
        hcm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId="6";
                Ministry("6","HCM Ministry", "HANDS OF COMPASSION MINISTRY MEMBERS");
            }
        });
        ushering_button=findViewById(R.id.ushering_button);
        ushering_button.animate().rotationX(720).setDuration(1000);
        ushering_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId="7";
                Ministry("7","Ushering Ministry", "USHERING MINISTRY MEMBERS");
            }
        });
        sunday_school_button=findViewById(R.id.sunday_school_button);
        sunday_school_button.animate().rotationX(720).setDuration(600);
        sunday_school_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId="8";
                Ministry("8","Sunday School Ministry", "SUNDAY SCHOOL MINISTRY MEMBERS");
            }
        });
        nurturing_button=findViewById(R.id.nurturing_button);
        nurturing_button.animate().rotationX(720).setDuration(900);
        nurturing_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId="9";
                Ministry("9","Nurturing Ministry", "NURTURING MINISTRY MEMBERS");
            }
        });
        intercessory_button=findViewById(R.id.intercessory_button);
        intercessory_button.animate().rotationX(720).setDuration(400);
        intercessory_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId="10";
                Ministry("10","Intercessory Ministry", "INTERCESSORY MINISTRY MEMBERS");
            }

        });


    }
    public void Ministry( final String ministryId, final String heading, final String ministryMembers){
       setContentView(R.layout.ministries);
       TextView headText= findViewById(R.id.heading);
       TextView aboutUs=findViewById(R.id.about);
       TextView update=findViewById(R.id.updateMembers);
       final ListView listView=findViewById(R.id.ministryMembersList);
       joinMinistry = findViewById(R.id.join_music);



      // for(int i=0; i<UserDetails.INSTANCE.getMinistries().size(); i++){
           if(/*heading.equals(UserDetails.INSTANCE.getMinistries()) ||*/ UserDetails.INSTANCE.getGroupId()==Integer.parseInt(ministryId)){
               update.setVisibility(View.VISIBLE);
               joinMinistry.setVisibility(View.INVISIBLE);
           }



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ministryLeader();
            }
        });

        headText.setText(heading+"\t\t");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        joinMinistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Do you want to Join  "+heading+"???")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int i) {
                                finish();
                                joinMinistry(ministryId);
                                    String exit = "Exit";
                                    joinMinistry.setText(exit);
                                    Toast.makeText(getApplicationContext(), "You Joined " + heading, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Join Ministry");
                alert.show();
            }
        });
        new android.os.AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                try {
                    final String type="ministryMembers";
                    String strings[]=new String[2];
                    strings[0]=type;
                    strings[1]=ministryId;
                    HttpProcesses httpProcesses=new HttpProcesses();
                    response = httpProcesses.sendRequest(strings);
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String response) {
                //do something with response
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean result=jsonObject.getBoolean("status");
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    if(result==true){
                        JSONObject jsonObject1=null;
                        final ArrayList<String> grpmembers = new ArrayList<>();
                        grpmembers.add(ministryMembers);
                        for(int i=0;i<jsonArray.length();i++){
                            jsonObject1 =jsonArray.getJSONObject(i);
                            grpmembers.add(jsonObject1.getString("name")+"\t\t"+jsonObject1.getString("rolePlayed")+"\n"+jsonObject1.getString("phone"));
                        }
                        if(jsonObject1==null){
                            return;
                        }
                        final ArrayAdapter grpAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, grpmembers);
                        listView.setAdapter(grpAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.execute();
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutMinistry(mId);

            }
        });


    }
    public void aboutMinistry(final String ministryId){
        setContentView(R.layout.church_info);
        ScrollView scrollView=findViewById(R.id.about);
        scrollView.setVisibility(View.VISIBLE);
        final TextView vision=findViewById(R.id.vision);
        final TextView mission=findViewById(R.id.mission);
        final TextView aboutG=findViewById(R.id.infoAbout);
        TextView textView=findViewById(R.id.his);
        textView.setVisibility(View.INVISIBLE);

        new android.os.AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                try {
                    final String type="groupInformation";
                    String strings[]=new String[2];
                    strings[0]=type;
                    strings[1]=ministryId;
                    HttpProcesses httpProcesses=new HttpProcesses();
                    response = httpProcesses.sendRequest(strings);
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String response) {
                //do something with response
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Boolean result=jsonObject.getBoolean("status");
                    if(result==true){
                        JSONArray dataArr=jsonObject.getJSONArray("data");
                        JSONObject data = dataArr.getJSONObject(0);
                        String vis=data.getString("vision");
                        String mis=data.getString("mission");
                        String about=data.getString("aboutMinistry");


                        mission.setText(vis);
                        vision.setText(mis);
                        aboutG.setText(about);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.execute();

    }
    public void joinMinistry(final String ministry_id){
        final String type="joinMinistry";
        new AsyncTask<Void, Void, String>() {
            protected String doInBackground(Void[] params) {
                String response = "";
                try {
                    String[] strings = new String[2];
                    strings[0] = type;
                    strings[1]= ministry_id;
                    HttpProcesses httpProcesses = new HttpProcesses();
                    response = httpProcesses.sendRequest(strings);

                } catch (Exception e) {
                    response = e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String response) {
                //do something with response
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean result = jsonObject.getBoolean("status");
                    String message = jsonObject.getString("message");
                    if (result == true) {
                        joinMinistry.setChecked(true);
                        Toast.makeText(getApplicationContext(),message , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Already a Member" , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();

    }


    public void ministryLeader(){
        setContentView(R.layout.activity_l_s);
        LinearLayout linearLayout=findViewById(R.id.createUpdate);
        if(UserDetails.INSTANCE.getGroupId()>0){
            linearLayout.setVisibility(View.VISIBLE);
        }
        Spinner selectMinistry=findViewById(R.id.chooseMinistry);
        selectMinistry.setVisibility(View.VISIBLE);
        ministryEvent=findViewById(R.id.event);
        dateToStart=findViewById(R.id.dateToStart);
        dateToEnd=findViewById(R.id.dateToEnd);
        timeToStart=findViewById(R.id.timeToStart);
        description=findViewById(R.id.description);
        sendMessage=findViewById(R.id.sendMessage);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMinistryUpdate();
            }
        });
        timeToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });
        dateToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
                dateType="startDate";
            }
        });
        dateToEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
                dateType="endDate";
            }
        });

        String[] arraySpinner = new String[] {
                "Select Ministry",
                "Music Ministry",
                "Hospitality Ministry",
                "Media Ministry",
                "Lighters Ministry",
                "Evangelism Ministry",
                "Hands Of Compassion Ministry",
                "Ushering Ministry",
                "Sunday School Ministry",
                "Nurturing Ministry",
                "Intercessory Ministry"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectMinistry.setAdapter(adapter);
        selectMinistry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    assignId=0;
                }else if(position==1){
                    assignId=1;
                }else if(position==2){
                    assignId=2;
                }else if(position==3){
                    assignId=3;
                }else if(position==4){
                    assignId=4;
                }else if(position==5){
                    assignId=5;
                }else if(position==6){
                    assignId=6;
                }else if(position==7){
                    assignId=7;
                }else if(position==8){
                    assignId=8;
                }else if(position==9){
                    assignId=9;
                }else if(position==10){
                    assignId=10;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void sendMinistryUpdate(){
        final String type="ministryUpdates";
        final String ministryid= String.valueOf(assignId);
        final String event=ministryEvent.getText().toString();
        final String start=dateToStart.getText().toString();
        final String time=timeToStart.getText().toString();
        final String end=dateToEnd.getText().toString();
        final String descr=description.getText().toString();
        new AsyncTask<Void, Void, String>() {
            protected String doInBackground(Void[] params) {
                String response = "";
                try {
                    String[] strings = new String[7];
                    strings[0] = type;
                    strings[1]=ministryid;
                    strings[2]= event;
                    strings[3]=start;
                    strings[4]=time;
                    strings[5]=end;
                    strings[6]=descr;
                    HttpProcesses httpProcesses = new HttpProcesses();
                    response = httpProcesses.sendRequest(strings);

                } catch (Exception e) {
                    response = e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String response) {
                //do something with response
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean result = jsonObject.getBoolean("status");
                    String message = jsonObject.getString("message");
                    if (result == true) {
                        Toast.makeText(getApplicationContext(),message , Toast.LENGTH_SHORT).show();;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }
    public void timePicker(){
        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeToStart.setText(hourOfDay+":"+minute);
            }
        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }
    public void datePicker(){
        Calendar cal=Calendar.getInstance();

        Date date=cal.getTime();
        SimpleDateFormat format=new SimpleDateFormat("yyyy");
        int year=Integer.valueOf(format.format(date));
        format=new SimpleDateFormat("MM");
        int month=Integer.valueOf(format.format(date));
        format=new SimpleDateFormat("dd");
        int day=Integer.valueOf(format.format(date));

        DatePickerDialog datePickerDialog=new DatePickerDialog(this,
                this, year,
                month,
                day
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date=year+"/"+month+"/"+dayOfMonth;

        if(dateType.equals("startDate")){
            dateToStart.setText(date);
        }else  if(dateType.equals("endDate")){
            dateToEnd.setText(date);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }
}
