package com.example.ttucu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChurchActivities extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    ListView church;
    Button lsUpdates, logo;
    ScrollView scrollView;
    int selected=0;
    ScrollView churchView, userDetails;
    EditText name, uName, uEmail, uPhone, uReg, uCourse;
    public static ListView ministriesActive;
    Bitmap bitmap;
    ImageView profile;
     TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_church);
        logo=findViewById(R.id.gallery);
        church=findViewById(R.id.church);
        lsUpdates=findViewById(R.id.LSUpdates);
        churchView=findViewById(R.id.churchView);
        userDetails=findViewById(R.id.userDetails);
        name=findViewById(R.id.name);
        uName=findViewById(R.id.uName);
        uEmail=findViewById(R.id.uEmail);
        uPhone=findViewById(R.id.uPhone);
        uReg=findViewById(R.id.uReg);
        uCourse=findViewById(R.id.uCourse);
        ministriesActive=findViewById(R.id.ministriesServing);
        profile=findViewById(R.id.myProfile);

        logo.animate().rotationXBy(1080).setDuration(900);
        church.animate().rotationXBy(1080).setDuration(900);

        ArrayList<String> church_list=new ArrayList<>();
        church_list.add("About");
        church_list.add("Programme");
        church_list.add("Leadership");
        church_list.add("Ministries");
        ArrayAdapter church_adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, church_list);
        church.setAdapter(church_adapter);


       if(UserDetails.INSTANCE.getExec()>0) {
           lsUpdates.setVisibility(View.VISIBLE);
        } else {
            lsUpdates.setVisibility(View.INVISIBLE);
        }

        church.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    aboutChurch();
                }else if(position==1){
                    programme();
                }else  if(position==2){
                  Intent intent=new Intent(getApplicationContext(), leadership.class);
                  startActivity(intent);
                }else if(position==3){
                    Intent intent=new Intent(getApplicationContext(),Ministries.class);
                    startActivity(intent);
                }
            }
        });
        lsUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LS.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.join_ministry, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.logout) {
            SQLitebatabase sqLitebatabase;
            sqLitebatabase=new SQLitebatabase(this);
            sqLitebatabase.logOut();
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return  true;
        } else if (item.getItemId() == R.id.youtube) {
            Intent intent = new Intent(getApplicationContext(), YouTube.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.userprofile) {
            churchView.setVisibility(View.INVISIBLE);
            userDetails.setVisibility(View.VISIBLE);
            userProfile();
            return true;
        } else if(item.getItemId()==R.id.notifications){
            setContentView(R.layout.socket);
            final ScrollView scrollView1=findViewById(R.id.update);
            final LinearLayout linearLayout=findViewById(R.id.events);
            final ListView listView=findViewById(R.id.eventsList);
            TextView updateType=findViewById(R.id.updateType);
            final TextView textView=findViewById(R.id.newEvent);
            final TextView textView1=findViewById(R.id.startsOn);
             textView2=findViewById(R.id.at);
            final TextView textView3=findViewById(R.id.endsOn);
            final TextView textView4=findViewById(R.id.eventDetails);
            SQLitebatabase sqLitebatabase=new SQLitebatabase(getApplicationContext());
            final List<newNotifications> list=sqLitebatabase.viewUpdates();
            final ArrayList<String> arrayList=new ArrayList();
            linearLayout.setVisibility(View.VISIBLE);
            updateType.setText("Upcoming Events");
            for(int i=0; i<list.size(); i++){
                arrayList.add(list.get(i).getEvent());
            }
            ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    linearLayout.setVisibility(View.INVISIBLE);
                    scrollView1.setVisibility(View.VISIBLE);
                    textView.setText(list.get(position).getEvent());
                    textView1.setText(list.get(position).getDateToStart());
                    textView2.setText(list.get(position).getTimeToStart());
                    textView3.setText(list.get(position).getDateToEnd());
                    textView4.setText(list.get(position).getDescription());
                }
            });
            return true;
        } else if (item.getItemId() == R.id.semSchedule) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("These are Our Fellowships.....");
            final String[]  day= {"SUNDAY SERVICE", "TUESDAY FELLOWSHIP"};
            builder.setItems(day, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:

                            schedule("sunday", "Sunday Schedule");
                            break;
                        case 1:
                            schedule("tuesday","Tuesday Fellowships");
                            break;
                    }
                   // scrollView1.setVisibility(View.VISIBLE);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }else if(item.getItemId()==R.id.home){
            Intent intent=new Intent(getApplicationContext(), ChurchActivities.class);
            startActivity(intent);
            return true;

        } else {
            return false;
        }
    }
    public void aboutChurch(){
        setContentView(R.layout.church_info);
        scrollView=findViewById(R.id.about);
        scrollView.setVisibility(View.VISIBLE);
        final TextView churchVision=findViewById(R.id.vision);
        final TextView churchMission=findViewById(R.id.mission);
        final TextView aboutChurch=findViewById(R.id.infoAbout);
        final TextView history=findViewById(R.id.history);

        new android.os.AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                try {
                    final String type="aboutChurch";
                    String strings[]=new String[1];
                    strings[0]=type;
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
                        String about=data.getString("aboutUs");
                        String his=data.getString("history");

                            churchVision.setText(vis);
                            churchMission.setText(mis);
                            aboutChurch.setText(about);
                            history.setText(his);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.execute();

    }
    public void programme(){
        setContentView(R.layout.church_info);
        final ScrollView pro=findViewById(R.id.pro);
        final TextView fell=findViewById(R.id.fell);
        final TextView day=findViewById(R.id.fday);
        final TextView ftime=findViewById(R.id.ftime);
        final TextView ven=findViewById(R.id.ven);
        final TextView des=findViewById(R.id.des);
        final String type="churchProgramme";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("These are Our Fellowships");
        final String[]  fellType= {"WEEKLY DEVOTIONS", "SUNDAY SERVICE", "TUESDAY FELLOWSHIP", "WORSHIP HOUR", "MINI-KESHA"};
        builder.setItems(fellType, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        selected=0;
                        break;
                    case 1:
                        selected=1;
                        break;
                    case 2:
                        selected=2;
                        break;
                    case 3:
                        selected=3;
                        break;
                    case 4:
                        selected=4;
                        break;

                }
                pro.setVisibility(View.VISIBLE);
                new android.os.AsyncTask<Void, Void, String>(){
                    protected String doInBackground(Void[] params) {
                        String response="";
                        try {

                            String strings[]=new String[2];
                            strings[0]=type;
                            strings[1]= String.valueOf(selected+1);
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
                                String f=data.getString("fellowship");
                                String d=data.getString("day");
                                String t=data.getString("time");
                                String v=data.getString("Venue");
                                String de=data.getString("description");

                                fell.setText(f);
                                day.setText(d);
                                ftime.setText(t);
                                ven.setText(v);
                                des.setText(de);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }.execute();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();



    }
    public void schedule(final String type, final String heading){
        setContentView(R.layout.socket);
        final ScrollView scrollView1=findViewById(R.id.schedule);
        final TextView textView=findViewById(R.id.day);
        final TextView textView1=findViewById(R.id.daysAndschedule);
        textView.setText(heading);
        scrollView1.setVisibility(View.VISIBLE);

        new android.os.AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                try {
                    String strings[]=new String[1];
                    strings[0]= type;
                    HttpProcesses httpProcesses=new HttpProcesses();
                    response = httpProcesses.sendRequest(strings);
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String response){
            //do something with response
            try {
                JSONObject jsonObject=new JSONObject(response);
                Boolean result=jsonObject.getBoolean("status");
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                if(result==true){
                    JSONObject jsonObject1=null;

                    for(int i=0;i<jsonArray.length();i++){
                        jsonObject1 =jsonArray.getJSONObject(i);

                        String date=jsonObject1.getString("sdate");
                        String speaker=jsonObject1.getString("speaker");
                        String programmer=jsonObject1.getString("progammer");

                        if(heading.equals("Sunday Schedule")){
                            textView1.setText("Date\t\t:"+date+"\nSpeaker\t\t:"+speaker+"\nProgrammer\t\t:"+programmer);
                        }else if(heading.equals("Tuesday Fellowships")){
                            String topic=jsonObject1.getString("topic");
                            textView1.setText("\n\nDate\t\t:"+date+"\nSpeaker\t\t:"+speaker+"\nProgrammer\t\t:"+programmer+"\nTopic\t:"+topic);

                        }

                    }
                    if(jsonObject1==null){
                        return;
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }

        }.execute();
    }
    public  void userProfile(){
        final String type="userDetails";
        final int id=UserDetails.INSTANCE.getUserId();
        new android.os.AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                try {

                    String strings[]=new String[2];
                    strings[0]=type;
                    strings[1]=String.valueOf(id);
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
                    Boolean hasMinistries =jsonObject.getBoolean("ministriesFound");

                    if(result ){
                        final ArrayList<String> ministries = new ArrayList<>();
                        if(hasMinistries) {
                            JSONArray dataArr=jsonObject.getJSONArray("data");
                            JSONObject jsonObject1=null;

                            for (int i = 0; i < dataArr.length(); i++) {
                                jsonObject1 = dataArr.getJSONObject(i);
                                ministries.add(jsonObject1.getString("ministryName"));
                                // List<String> list=ministries;
                                UserDetails.INSTANCE.setMinistries(ministries);
                            }
                        }
                      /*  if(jsonObject1==null){
                            return;
                        }*/
                      if(!ministries.isEmpty()){
                          final ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, ministries);
                          ministriesActive.setAdapter(adapter);
                      }else {
                          ministries.add("You have Not Joined Any Ministry");
                          final ArrayAdapter adapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, ministries);
                          ministriesActive.setAdapter(adapter);
                      }


                        String myName=jsonObject.getString("name");
                        String myUserName=jsonObject.getString("userName");
                        String myEmail=jsonObject.getString("email");
                        String myPhone=jsonObject.getString("phone");
                        String myReg=jsonObject.getString("regNo");
                        String myCourse=jsonObject.getString("Course");
                        String myprofile=jsonObject.getString("image");

                        byte [] encodeByte=Base64.decode(myprofile,Base64.DEFAULT);

                        InputStream inputStream  = new ByteArrayInputStream(encodeByte);
                        Bitmap profilePic  = BitmapFactory.decodeStream(inputStream);
                        profile.setImageBitmap(profilePic);

                        name.setText(myName);
                        uName.setText(myUserName);
                        uEmail.setText(myEmail);
                        uPhone.setText(myPhone);
                        uReg.setText(myReg);
                        uCourse.setText(myCourse);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }
    public void updateProfile(View view) {
        final String type="updateUserProfile";
        final String myNam=name.getText().toString();
        final String userName = uName.getText().toString();
        final String email_address =uEmail.getText().toString();
        final String phone = uPhone.getText().toString();
        final String regNo = uReg.getText().toString();
        final String course = uCourse.getText().toString();
        final int id=UserDetails.INSTANCE.getUserId();

        new android.os.AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    final String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                    String strings[]=new String[9];
                    strings[0]=type;
                    strings[1]=myNam;
                    strings[2]=userName;
                    strings[3]=email_address;
                    strings[4]=phone;
                    strings[5]=regNo;
                    strings[6]=course;
                    strings[7]=String.valueOf(id);
                    strings[8]=encodedImage;

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
                        Toast.makeText(getApplicationContext(), "User Details Updated", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 10);
        c.set(Calendar.MINUTE, 9);
        c.set(Calendar.SECOND, 0);
        startAlarm(c);
        String hour= String.valueOf(hourOfDay);
        String min= String.valueOf(minute);
        textView2.setText(hour+" : "+min);

    }
    public void choose(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
    }
    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {
        super.onActivityResult(RC, RQC, I);
        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {
            Uri uri = I.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startAlarm(Calendar c){
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this, alertReciever.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,1, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        String alarmText="Alarm Set For :";
        alarmText +=DateFormat.getDateTimeInstance().format(c.getTime());
        textView2.setText(alarmText);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //return;
       // Intent intent=new Intent(getApplicationContext(), ChurchActivities.class);
       // startActivity(intent);
        churchView.setVisibility(View.VISIBLE);
    }
}
