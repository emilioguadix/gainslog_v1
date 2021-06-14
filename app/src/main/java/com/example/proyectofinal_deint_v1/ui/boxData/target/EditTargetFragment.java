package com.example.proyectofinal_deint_v1.ui.boxData.target;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;

import com.example.proyectofinal_deint_v1.GainsLogApplication;
import com.example.proyectofinal_deint_v1.R;
import com.example.proyectofinal_deint_v1.data.model.model.target.Target;
import com.example.proyectofinal_deint_v1.ui.service.JobServiceAlarm;
import com.example.proyectofinal_deint_v1.ui.utils.CommonUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class EditTargetFragment extends Fragment implements TargetContract.View{

    //Campos para el servicio en segundo plano
    private static int REQUEST_CODE = 345;
    private static final int JOBID= 0;
    private static final int PERIOD_MS = 5000;
    private static final String CHANNEL_ID = "123";

    //Campos UI
    private TextInputLayout tilNameTarget;
    private TextInputEditText tieNameTarget;
    private TextInputLayout tilDescription;
    private TextInputEditText tieDescription;
    private CheckBox cbx_favorite;
    private DatePicker datePicker;
    private boolean addMode;
    private Target target;
    private Target oldTarget;
    private FloatingActionButton btnSave;
    private SharedPreferences sharedPreferences;

    private TargetContract.Presenter presenter;

    @Override
    public void onStart() {
        super.onStart();
        //Gracias al objeto recibido por el fragment, vemos si es para modificar/borrar un ejercicio o añadirlo.
        if(addMode){
            if(target == null) {
                target = new Target();
            }
        }
        else {
            oldTarget = target;
            loadDataInputs();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void scheduleJob(Context context){
        ComponentName componentName = new ComponentName(context, JobServiceAlarm.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOBID, componentName);
        //Gestionamos el momento de la alarma
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,datePicker.getYear());
        calendar.set(Calendar.MONTH,datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());

        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(calendar.getTime().getTime() - now.getTime().getTime());
        builder.setMinimumLatency(now.getTimeInMillis());
        builder.setOverrideDeadline(now.getTimeInMillis());

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        addMode = EditTargetFragmentArgs.fromBundle(getArguments()).getAddMode();
        target = EditTargetFragmentArgs.fromBundle(getArguments()).getTarget();
        tieNameTarget = view.findViewById(R.id.tieNameTarget);
        tilNameTarget = view.findViewById(R.id.tilNameTarget);
        tieDescription = view.findViewById(R.id.tiedescriptionTarget);
        tilDescription = view.findViewById(R.id.tildescriptionTarget);
        cbx_favorite = view.findViewById(R.id.cbx_favoriteTarget);
        datePicker = view.findViewById(R.id.dpExpDate);datePicker.setMinDate(System.currentTimeMillis());
        btnSave = view.findViewById(R.id.btnEditTarget);
        if(CommonUtils.isCoachUser(getContext()) ){
            disableEdit();
        }

        presenter = new TargetPresenter(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                setTargetFields();
                if(addMode){
                    presenter.addTarget(getContext(),target);
                }
                else{
                    presenter.modifyTarget(getContext(),oldTarget,target);
                }
            }
        });
    }

    private void disableEdit() {
        tieDescription.setEnabled(false);
        tieNameTarget.setEnabled(false);
        cbx_favorite.setEnabled(false);
        btnSave.setVisibility(View.GONE);
        datePicker.setEnabled(false);
    }

    private String getDateString(Calendar calendar){
        return String.valueOf(CommonUtils.getTimeStampOfCalendar(calendar)).split(" ")[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification(){
        Bundle bundle = new Bundle();
        bundle.putBoolean("addMode",false);
        bundle.putSerializable("target",target);
        bundle.putString("oldNameTarget",null);
        PendingIntent pendingIntent =new NavDeepLinkBuilder(getActivity())
                .setGraph(R.navigation.new_graph)
                .setDestination(R.id.editTargetFragment)
                .setArguments(bundle)
                .createPendingIntent();
        Notification.Builder builder = new Notification.Builder(getActivity(), GainsLogApplication.CHANNEL_ID)
                .setAutoCancel(true)
                .setSound(Uri.parse(""))
                .setSmallIcon(R.mipmap.ic_launcher_gainslogger)
                .setContentTitle(getResources().getString(R.string.notification_title_edit_target))
                .setContentText(getResources().getString(R.string.notification_text_edit_target) + getDateString(target.getExpirationDate()))
                .setContentIntent(pendingIntent);
        //Se añade la notificación creada, al gestor de notificaciones
        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(1000),builder.build());
        playNotificationSound();
    }

    private void playNotificationSound() {
        try {
            Uri path = Uri. parse (ContentResolver. SCHEME_ANDROID_RESOURCE + "://" + getContext().getPackageName() + "/raw/" + sharedPreferences.getString(getString(R.string.key_tone),""));
            Ringtone r = RingtoneManager.getRingtone(getContext(),path);
            if(sharedPreferences.getBoolean(getString(R.string.key_notifications),true)){
                r.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTargetFields() {
        target.setNameTarget(tieNameTarget.getText().toString());
        target.setDescription(tieDescription.getText().toString());
        target.setExpirationDate(CommonUtils.getDateFromDatePicker(datePicker));
        target.setOvercome((cbx_favorite.isChecked()) ? "1":"0" );
    }

    private void loadDataInputs(){
        tieNameTarget.setText(target.getNameTarget());
        tieDescription.setText(target.getDescription());
        datePicker.updateDate(target.getExpirationDate().get(Calendar.YEAR),target.getExpirationDate().get(Calendar.MONTH),target.getExpirationDate().get(Calendar.DAY_OF_MONTH));
        cbx_favorite.setChecked(target.isOvercome());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_target, container, false);
    }

    @Override
    public void setNameEmptyError() {
        Snackbar.make(getView(),getResources().getString(R.string.err_nameEmpty),Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setExerciseNotExistsError() {
        Snackbar.make(getView(),getResources().getString(R.string.err_exNotExists),Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setDateExpError() {
        Snackbar.make(getView(),getResources().getString(R.string.errDateExp),Snackbar.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSuccesAdd() {
        NavHostFragment.findNavController(EditTargetFragment.this).navigate(R.id.targetListFragment);
        showNotification();
        scheduleJob(getContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSuccesModify() {
        NavHostFragment.findNavController(EditTargetFragment.this).navigate(R.id.targetListFragment);
        showNotification();
        scheduleJob(getContext());
    }

    @Override
    public void onSucess() {

    }

    @Override
    public void onSuccessDelete() {

    }

    @Override
    public void onSuccess(List repository) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}