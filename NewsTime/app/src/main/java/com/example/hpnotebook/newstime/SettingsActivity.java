package com.example.hpnotebook.newstime;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hp Notebook on 29-01-2018.
 */

public class SettingsActivity extends AppCompatActivity {

    private static final String LOG_TAG = SettingsActivity.class.getName();
    private static Preference mPreference;
    private static String dob = "2017-12-30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        private static final String KEY_PREF_DATE = "date";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference section = findPreference(getString(R.string.settings_section_key));
            bindPreferenceSummaryToValue(section);
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);
            Preference pgSize = findPreference(getString(R.string.settings_page_size_key));
            bindPreferenceSummaryToValue(pgSize);
            Preference date = findPreference(getString(R.string.settings_date_key));
            mPreference = date;
            formatDate();
            date.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    showDatePicker();
                    return true;
                }
            });
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            if(preference.getKey().equals(KEY_PREF_DATE)){
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.settings_date_key), dob);
                Log.v(LOG_TAG, dob + " inside bindPreferenceSummaryToValue");
                editor.apply();
                formatDate();
                return;
            }
            String preferenceString = sharedPref.getString(preference.getKey(),"");
            onPreferenceChange(preference, preferenceString);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        private void showDatePicker() {

            Log.v(LOG_TAG, "inside showDatePicker");
            DatePickerFragment date = new DatePickerFragment();

            /**
             * Set Up Current Date Into dialog
             */
            Calendar calender = Calendar.getInstance();
            Bundle args = new Bundle();
            args.putInt("year", calender.get(Calendar.YEAR));
            args.putInt("month", calender.get(Calendar.MONTH));
            args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
            date.setArguments(args);

            /**
             * Set Call back to capture selected date
             */
            date.setCallBack(ondate);
            date.show(getFragmentManager(), "Date Picker");
        }

        DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                dob = String.valueOf(year) + "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth);
                Log.v(LOG_TAG, dob + " inside showDatePicker");
                bindPreferenceSummaryToValue(mPreference);
            }
        };

        private void formatDate() {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mPreference.getContext());
            String preferenceString = sharedPref.getString(mPreference.getKey(),"");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = null;
            try {
                if(preferenceString.equals("")){
                    myDate = dateFormat.parse(dob);
                } else{
                    myDate = dateFormat.parse(preferenceString);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat date = new SimpleDateFormat("MMM dd, yyyy");
            String finalDate = date.format(myDate);
            mPreference.setSummary(finalDate);

        }
    }
}