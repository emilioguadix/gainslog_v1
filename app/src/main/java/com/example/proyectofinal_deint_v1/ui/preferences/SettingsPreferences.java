package com.example.proyectofinal_deint_v1.ui.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.example.proyectofinal_deint_v1.R;

public class SettingsPreferences extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences sharedPreferences;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        addPreferencesFromResource(R.xml.setting_preferences);
        onSharedPreferenceChanged(PreferenceManager.getDefaultSharedPreferences(getContext()),getString(R.string.key_ringtone));
        initPreferenceAccount();
    }

    //Registramos el listener.
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    //Cada vez que el fragment no sea visible se desvincula el listener
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if(key.equals(getString(R.string.key_ringtone))){
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(sharedPreferences.getString(key,""));
            //Para guardar en la descripcion de la preferencia el valor que haya pulsado el usuario
            if(index>= 0){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                preference.setSummary(listPreference.getEntries()[index]);
                String tmp = "";
                if(listPreference.getEntries()[index].toString().split(" ").length > 1){
                    tmp = listPreference.getEntries()[index].toString().split(" ")[1].toLowerCase();
                }
                else{
                    tmp = listPreference.getEntries()[index].toString();
                }
                editor.putString(getString(R.string.key_tone),tmp);
            }
        }

    }

    public void initPreferenceAccount(){
        Preference accountPreference = getPreferenceManager().findPreference(getString(R.string.key_account));
        accountPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                NavHostFragment.findNavController(SettingsPreferences.this).navigate(R.id.action_settingsPreferences_to_accountPreferences);
                return true;
            }
        });
    }
}
