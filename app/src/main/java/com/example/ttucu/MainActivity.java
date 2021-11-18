package com.example.ttucu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class MainActivity extends AppCompatActivity {
    EditText login_user_name, login_password;
    TextView error;
    public  static EditText ip;
    LinearLayout linearLayout, connection;
    public static final String SharedPref="SharedPef";
    public static final String Text="Text";
    public static   String text;


    SQLitebatabase sqLitebatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_user_name=findViewById(R.id.login_userName);
        login_password=findViewById(R.id.login_password);
        error=findViewById(R.id.errorDetails);
        linearLayout=findViewById(R.id.loginLayout);


        loadip();
        sqLitebatabase=new SQLitebatabase(getApplicationContext());

       Cursor cursor= sqLitebatabase.syncDetails();
       if(cursor !=null && cursor.getCount()>0){


           Intent intent=new Intent(getApplicationContext(), ChurchActivities.class);
           if(!isMyServiceRunning(JobService.class)) {
               startService(new Intent(MainActivity.this, android.app.job.JobService.class));
               startJob();

           }
           startActivity(intent);


       }





        Button login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName=login_user_name.getText().toString();
                final String password=login_password.getText().toString();
                final String type="login";
                if(userName.matches("") || password.matches("")){
                    login_user_name.setText("");
                    login_user_name.setHint("Enter UserName");
                    login_password.setText("");
                    login_password.setHint("Enter Password");


                    Toast.makeText(getApplicationContext(),"Enter username and Password.", Toast.LENGTH_LONG);
                }else {
                    new android.os.AsyncTask<Void, Void, String>(){
                        protected String doInBackground(Void[] params) {
                            String response="";
                            try {
                                String strings[]=new String[3];
                                strings[0]=type;
                                strings[1]=userName;
                                strings[2]=password;
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
                                    String message=jsonObject.getString("message");
                                    int  userId=jsonObject.getInt("id");
                                    int group_role=jsonObject.getInt("group_role_leader");
                                    int executive=jsonObject.getInt("Executive");
                                    int subcom=jsonObject.getInt("Subcom");
                                    int mission=jsonObject.getInt("Missions_committee");
                                    int hike=jsonObject.getInt("hike_Committee");
                                    int project=jsonObject.getInt("project_committee");
                                    int elders=jsonObject.getInt("elders");
                                    UserDetails.INSTANCE.setUserId(userId);
                                    UserDetails.INSTANCE.setGroupId(group_role);
                                    UserDetails.INSTANCE.setExec(executive);
                                    UserDetails.INSTANCE.setSubcom(subcom);
                                    UserDetails.INSTANCE.setMission(mission);
                                    UserDetails.INSTANCE.setHike(hike);
                                    UserDetails.INSTANCE.setProject(project);
                                    UserDetails.INSTANCE.setElders(elders);
                                    UserDetails.INSTANCE.setPhone(jsonObject.getInt("Phone"));
                                    UserDetails.INSTANCE.setName(jsonObject.getString("Name"));
                                   sqLitebatabase.insertData(userId,userName,group_role,executive,subcom, mission, hike, project, elders);

                                    if(!isMyServiceRunning(JobService.class)) {
                                        startJob();
                                    }
                                    Intent intent=new Intent(getApplicationContext(), ChurchActivities.class);

                                    startActivity(intent);

                                    Toast.makeText(getApplicationContext(), message+"\n \tWelcome", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    error.setText("Invalid UserName Or Password");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    }.execute();

                }

            }
        });
        TextView signUp=findViewById(R.id.signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), registration.class);
                startActivity(intent);
            }
        });
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public void startJob() {
        ComponentName componentName=new ComponentName(this, JobService.class);
        JobInfo jobInfo=new JobInfo.Builder(123, componentName)
                .setPersisted(true)
                .setPeriodic(15*60*1000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
        JobScheduler jobScheduler= (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int result=jobScheduler.schedule(jobInfo);

        if(result==JobScheduler.RESULT_SUCCESS){
            Log.i("Scheduled", "Running" );
        }else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }

    }

    public void connection(){
        new android.os.AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                try {
                    final String type="connection";
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



                    if(response!=null) {
                        JSONObject jsonObject=new JSONObject(response);
                        Boolean result = jsonObject.getBoolean("status");
                        if (result == true) {
                            saveip();
                            linearLayout.setVisibility(View.VISIBLE);
                            connection.setVisibility(View.INVISIBLE);

                        } else {
                            connection.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }.execute();

    }
    public void saveip(){
        SharedPreferences sharedPreferences=getSharedPreferences(SharedPref, MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("url", ip.getText().toString());
        editor.apply();
    }
    public  void loadip(){
        SharedPreferences sharedPreferences=getSharedPreferences(SharedPref, MODE_PRIVATE);
        text=sharedPreferences.getString("url","");
        if(text ==null || text.isEmpty()){
            connection.setVisibility(View.VISIBLE);
        }else{
            connection();
        }
    }

}
