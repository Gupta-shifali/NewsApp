package com.example.hpnotebook.newstime;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.os.Bundle;

/**
 * Created by Hp Notebook on 01-02-2018.
 */

public class DatePickerFragment {
    public void setArguments(Bundle arguments) {
        this.arguments = arguments;
    }

    public void setCallBack(DatePickerDialog.OnDateSetListener callBack) {
        this.callBack = callBack;
    }

    public void show(FragmentManager fragmentManager, String s) {
    }
}
