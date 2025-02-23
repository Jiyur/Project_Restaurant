package com.example.project_login.Activities.StaffManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.project_login.DAO.UserDAO;
import com.example.project_login.DTO.User;
import com.example.project_login.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

public class add_staff extends AppCompatActivity {
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;
    private DatabaseReference mDatabase;
    private TextInputEditText fullname_txt, phone_txt, pass_txt, birth_txt;
    private RadioButton male_radioBtn, female_radioBtn, staff_radioBtn, manager_radioBtn;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        final Calendar c = Calendar.getInstance();
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        male_radioBtn = findViewById(R.id.male_radioBtn);
        female_radioBtn = findViewById(R.id.female_radioBtn);
        staff_radioBtn = findViewById(R.id.staff_radioBtn);
        manager_radioBtn = findViewById(R.id.manager_radioBtn);
        fullname_txt = findViewById(R.id.textInputEditText_full_name);
        phone_txt = findViewById(R.id.textInputEditText_phone);
        pass_txt = findViewById(R.id.textInputEditText_pass);
        birth_txt = findViewById(R.id.textInputEditText_birth);
        mDatabase = UserDAO.getMyDatabase();

        toolbar = findViewById(R.id.listStaff_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void chooseOnclick(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                birth_txt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDayOfMonth = dayOfMonth;
            }
        };
        DatePickerDialog datePickerDialog = null;
        datePickerDialog = new DatePickerDialog(this,
                dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
        datePickerDialog.show();
    }

    public void addOnclick(View view) {
        String fullName = fullname_txt.getText().toString();
        String phone = phone_txt.getText().toString();
        String pass = pass_txt.getText().toString();
        String birth = birth_txt.getText().toString();
        String gender = "";
        String role = "";

        if(staff_radioBtn.isChecked()){
            role = "staff";
        }
        else{
            if(manager_radioBtn.isChecked()){role = "manager"; }
        }

        if(male_radioBtn.isChecked()){
            gender= "male";
        }
        else{
            if(female_radioBtn.isChecked()){ gender = "female"; }
        }

        User user = new User(fullName, phone, pass, role, gender, birth);
        mDatabase.child(user.getPhone()).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null){
                    Toast.makeText(add_staff.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(add_staff.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }
        return super.onOptionsItemSelected(item);
    }
}