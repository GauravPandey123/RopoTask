package com.lustycoder.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lustycoder.myapplication.model.MeetingModel;
import com.lustycoder.myapplication.rest.ApiClient;
import com.lustycoder.myapplication.rest.ApiInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Meetingenq extends AppCompatActivity {
    private TextView meet_date,meet_st_time,meet_end_date,back;
    DatePickerDialog datePickerDialog;
    private int  mHour, mMinute;
    Button submit_meet_des_date;
    private ArrayList<String> availbelslot=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_meetingenq);
        meet_date=findViewById(R.id.meet_date);
        meet_st_time=findViewById(R.id.meet_st_time);
        meet_end_date=findViewById(R.id.meet_end_date);
        submit_meet_des_date=findViewById(R.id.submit_meet_des_date);
        back=findViewById(R.id.back);
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        if (mMonth <= 8) {
            meet_date.setText(mDay + "-0"
                    + (mMonth + 1) + "-" + mYear);

        } else {
            meet_date.setText(mDay + "-"
                    + (mMonth + 1) + "-" + mYear);

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        meet_st_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);


                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Meetingenq.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                meet_st_time.setText(hourOfDay + ":"+minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });
        meet_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Meetingenq.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                meet_end_date.setText(hourOfDay + ":"+minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        meet_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Meetingenq.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                if (monthOfYear <= 8) {
                                    meet_date.setText(dayOfMonth + "-0"
                                            + (monthOfYear + 1) + "-" + year);

                                } else {
                                    meet_date.setText(dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year);




                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
        });

        submit_meet_des_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDummyDataToPass(meet_date.getText().toString(),meet_st_time.getText().toString(),meet_end_date.getText().toString());
            }
        });
    }

    private void getDummyDataToPass(String string, final String s, final String toString) {
        availbelslot.clear();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<MeetingModel>> call = apiService.postRawJSON(string);
        call.enqueue(new Callback<List<MeetingModel>>() {
            @Override
            public void onResponse(Call<List<MeetingModel>> call, Response<List<MeetingModel>> response) {
                List<MeetingModel> meetingModelList = response.body();
                Log.d("ss", String.valueOf(meetingModelList.size()));
                if (meetingModelList.size() >= 1) {
                    for (int i=0;i<meetingModelList.size();i++) {
                        String pattern = "HH:mm";
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        try {
                            Date date1 = sdf.parse(meetingModelList.get(i).getStartTime());
                            Date date2 = sdf.parse(meetingModelList.get(i).getEndTime());
                            Date date3 = sdf.parse(s);
                            Date date4 = sdf.parse(toString);
                            if (date3.after(date1) && date4.before(date2))
                            {
                                availbelslot.add(meetingModelList.get(i).getStartTime());

                            }
                            else
                            {
                                Toast.makeText(Meetingenq.this,"Slot  Not available",Toast.LENGTH_LONG).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                } else {

                    Toast.makeText(Meetingenq.this,"No data found",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<MeetingModel>> call, Throwable t) {

            }
        });

    }
}