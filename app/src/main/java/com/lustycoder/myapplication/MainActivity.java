package com.lustycoder.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.lustycoder.myapplication.adapter.Meetingadapter;
import com.lustycoder.myapplication.model.MeetingModel;
import com.lustycoder.myapplication.rest.ApiClient;
import com.lustycoder.myapplication.rest.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView meetinglist;
    private TextView selcted_date,prev_meet,next_meet;
    DatePickerDialog datePickerDialog;
    private AppCompatButton sch_meet_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        meetinglist = findViewById(R.id.meetinglist);
        selcted_date = findViewById(R.id.selcted_date);
        sch_meet_main = findViewById(R.id.sch_meet_main);
        prev_meet = findViewById(R.id.prev_meet);
        next_meet = findViewById(R.id.next_meet);
        prev_meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // for example
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date date = format.parse(selcted_date.getText().toString());
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);

                    c.add(Calendar.DATE, -1);
                    selcted_date.setText( format.format(c.getTime()));
                    getDummyDataToPass(selcted_date.getText().toString());

                    System.out.println(date);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    selcted_date.setText( "");
                }
            }
        });

        next_meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for example
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date date = format.parse(selcted_date.getText().toString());
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);

                    c.add(Calendar.DATE, +1);
                    selcted_date.setText( format.format(c.getTime()));
                    getDummyDataToPass(selcted_date.getText().toString());

                    System.out.println(date);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    selcted_date.setText( "");
                }
            }
        });
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        if (mMonth <= 8) {
            selcted_date.setText(mDay + "-0"
                    + (mMonth + 1) + "-" + mYear);
            getDummyDataToPass(selcted_date.getText().toString());
        } else {
            selcted_date.setText(mDay + "-"
                    + (mMonth + 1) + "-" + mYear);
            getDummyDataToPass(selcted_date.getText().toString());
        }

        sch_meet_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Meetingenq.class);
                startActivity(intent);
            }
        });
    }

    private void getDummyDataToPass(String toString) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<MeetingModel>> call = apiService.postRawJSON(toString);
        call.enqueue(new Callback<List<MeetingModel>>() {
            @Override
            public void onResponse(Call<List<MeetingModel>> call, Response<List<MeetingModel>> response) {
                List<MeetingModel> meetingModelList = response.body();
                Log.d("ss", String.valueOf(meetingModelList.size()));
                if (meetingModelList.size() >= 1) {
                    meetinglist.setVisibility(View.VISIBLE);
                    meetinglist.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    meetinglist.setAdapter(new Meetingadapter(MainActivity.this, meetingModelList));
                } else {
                    meetinglist.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,"No data found",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<MeetingModel>> call, Throwable t) {

            }
        });


    }
}