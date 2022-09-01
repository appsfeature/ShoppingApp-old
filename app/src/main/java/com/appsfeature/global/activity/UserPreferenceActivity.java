package com.appsfeature.global.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.appsfeature.global.AppApplication;
import com.appsfeature.global.R;
import com.appsfeature.global.listeners.GenderType;
import com.appsfeature.global.listeners.SeasonType;
import com.appsfeature.global.model.CommonModel;
import com.appsfeature.global.network.NetworkManager;
import com.appsfeature.global.util.AppPreference;
import com.dynamic.listeners.DynamicCallback;
import com.helper.util.BaseUtil;
import com.helper.widget.SelectionSwitch;

import java.util.ArrayList;
import java.util.List;


public class UserPreferenceActivity extends AppCompatActivity {

    private final List<String> countryName = new ArrayList<>();
    private final List<String> countryCodes = new ArrayList<>();
    private Spinner spCountry;
    private String mSelectedCode;
    private View llNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preferences);
        initUi();
        getCountryCodes();
    }

    private void initUi() {
        llNoData = findViewById(R.id.ll_no_data);
        spCountry = findViewById(R.id.sp_country);
        View filterFemale = findViewById(R.id.ll_filter_female);
        View filterMale = findViewById(R.id.ll_filter_male);
        SelectionSwitch swGender = new SelectionSwitch(filterFemale, filterMale)
                .setSelected(AppPreference.getGender() == GenderType.TYPE_GIRL);

        View filterWinter = findViewById(R.id.ll_filter_winter);
        View filterSummer = findViewById(R.id.ll_filter_summer);
        SelectionSwitch swSeason = new SelectionSwitch(filterWinter, filterSummer)
                .setSelected(AppPreference.getSeason() == SeasonType.TYPE_WINTER);

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPreference.setGender(swGender.isFirstSelected() ? GenderType.TYPE_GIRL : GenderType.TYPE_BOY);
                AppPreference.setSeason(swSeason.isFirstSelected() ? SeasonType.TYPE_WINTER : SeasonType.TYPE_SUMMER);
                AppApplication.getInstance().updateLoginListener();
            }
        });

        (findViewById(R.id.iv_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void getCountryCodes() {
        NetworkManager.getInstance(this)
                .getCountryCodes(new DynamicCallback.Listener<List<CommonModel>>() {
                    @Override
                    public void onSuccess(List<CommonModel> response) {
                        BaseUtil.showNoData(llNoData, View.GONE);
                        countryCodes.clear();
                        countryName.clear();
                        countryCodes.add("");
                        countryName.add("Select Country");
                        for (CommonModel item : response) {
                            countryCodes.add(item.getId());
                            countryName.add(item.getTitle());
                        }
                        updateCountry();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        BaseUtil.showNoData(llNoData, View.GONE);
                    }
                });

    }

    private void updateCountry() {
        if(countryName != null && countryName.size() > 0) {
            ArrayAdapter<String> adapterSpSec = new ArrayAdapter<>(this, R.layout.adapter_spinner_white, countryName);
            adapterSpSec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCountry.setAdapter(adapterSpSec);
            spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    mSelectedCode = countryCodes.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }
}
