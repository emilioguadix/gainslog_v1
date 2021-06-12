package com.example.proyectofinal_deint_v1.ui.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.products.Exercise.Muscle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    public static boolean isPasswordValid(String password){
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).*[A-Za-z0-9].{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public static boolean isEmailValid(String email){
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean checkSystemWritePermission(Context context) {
        boolean retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(context);
            if(!retVal){
                Toast.makeText(context,context.getString(R.string.write_not_allow), Toast.LENGTH_LONG).show();
            }
        }
        return retVal;
    }

    public static Intent openAndroidPermissionsMenu(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        return intent;
    }

    public static boolean isCoachUser(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        //Se recoge el Editor de preferencias
        return sharedPreferences.getBoolean(context.getString(R.string.key_user_coach),true);
    }


    //Método que dado un listado de músculos devuelve en un string la concatenación de estos separados por coma --> 1,2,3
    public static String getMusclesList(List<Muscle> arraylist,boolean getName){
        String tmpMusc = "";
        if(arraylist.isEmpty()){return "";}
        for (Muscle muscle : arraylist) {
            tmpMusc += ((getName) ? muscle.getName() : String.valueOf(muscle.getId())) + ",";
        }
        return (arraylist.size() >= 1) ? tmpMusc.substring(0, tmpMusc.length() - 1) : "";
    }

    public static ProgressDialog showLoadingDialog(Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        //Hay que visualizar el progressbar para posicionarlo después en la vetana.
        progressDialog.show();
        if(progressDialog.getWindow() != null){
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        return progressDialog;
    }

    public  static Calendar getDateFromStringTimeStamp(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateToCalendar(date);
    }

    private static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Timestamp getTimeStampOfCalendar(Calendar calendar){
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static String getStringfromCalendar(Calendar calendar){
        String fecha = String.format("%04d", calendar.get(Calendar.YEAR)) +"-"+String.format("%02d", calendar.get(Calendar.MONTH)+1)
                +"-"+String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        return fecha;
    }

    public static Calendar getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar;
    }
}
