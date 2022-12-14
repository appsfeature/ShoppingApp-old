package com.appsfeature.global.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.R;
import com.appsfeature.global.adapter.FilterAdapter;
import com.appsfeature.global.listeners.GenderType;
import com.appsfeature.global.listeners.SeasonType;
import com.appsfeature.global.model.FilterModel;
import com.appsfeature.global.network.AppDataManager;
import com.appsfeature.global.util.AppPreference;
import com.dynamic.listeners.DynamicCallback;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;
import com.helper.widget.SelectionSwitch;

import java.util.ArrayList;
import java.util.List;

public class AppDialog {

    public static void openFilterHome(Activity activity, Response.Status<Boolean> callback) {
        try {
            Dialog dialog = new Dialog(activity, R.style.DialogThemeFullScreen);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            dialog.setContentView(R.layout.dialog_filter_home);

            View filterFemale = dialog.findViewById(R.id.ll_filter_female);
            View filterMale = dialog.findViewById(R.id.ll_filter_male);
            SelectionSwitch swGender = new SelectionSwitch(filterFemale, filterMale)
                    .setSelected(AppPreference.getGender() == GenderType.TYPE_GIRL);

            View filterWinter = dialog.findViewById(R.id.ll_filter_winter);
            View filterSummer = dialog.findViewById(R.id.ll_filter_summer);
            SelectionSwitch swSeason = new SelectionSwitch(filterWinter, filterSummer)
                    .setSelected(AppPreference.getSeason() == SeasonType.TYPE_WINTER);

            dialog.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppPreference.setGender(swGender.isFirstSelected() ? GenderType.TYPE_GIRL : GenderType.TYPE_BOY);
                    AppPreference.setSeason(swSeason.isFirstSelected() ? SeasonType.TYPE_WINTER : SeasonType.TYPE_SUMMER);
                    dismissDialog(dialog);
                    callback.onSuccess(true);
                }
            });

            dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog(dialog);
                }
            });
            dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog(dialog);
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openQuantity(Context context, Response.Status<String> callback) {
        try {
            Dialog dialog = new Dialog(context, R.style.DialogThemeFullScreen);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            dialog.setContentView(R.layout.dialog_quantity);

            EditText etQuantity = dialog.findViewById(R.id.et_quantity);

            dialog.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!TextUtils.isEmpty(etQuantity.getText())) {
                        dismissDialog(dialog);
                        callback.onSuccess(etQuantity.getText().toString());
                    }else {
                        BaseUtil.showToast(context, "Quantity is invalid.");
                    }
                }
            });

            dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog(dialog);
                }
            });
            dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog(dialog);
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openSizeChart(Context context, int type) {
        try {
            Dialog dialog = new Dialog(context, R.style.DialogThemeFullScreen);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            dialog.setContentView(R.layout.dialog_size_chart);

            ImageView ivImage = dialog.findViewById(R.id.iv_image);

            ivImage.setImageResource(type == 2 ? R.drawable.ic_size_chart_mens : R.drawable.ic_size_chart_womens);

            dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog(dialog);
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void dismissDialog(Dialog mDialog) {
        try {
            if ((mDialog != null) && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (final Exception e) {
        }
    }

    public static void openFilterProduct(Activity activity, List<FilterModel> mList, Response.Status<List<FilterModel>> callback) {
        try {
            Dialog dialog = new Dialog(activity, R.style.DialogThemeFullScreen);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            dialog.setContentView(R.layout.dialog_filter_list);

            View llNoData = dialog.findViewById(R.id.ll_no_data);
            RecyclerView recyclerView = dialog.findViewById(R.id.recycler_view);
            FilterAdapter adapter = new FilterAdapter(mList);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            if(mList.size() <= 0) {
                AppDataManager.get(activity).getAttributeData(new DynamicCallback.Listener<List<FilterModel>>() {
                    @Override
                    public void onSuccess(List<FilterModel> response) {
                        if (response != null && response.size() > 0) {
                            mList.clear();
                            mList.addAll(response);
                            adapter.notifyDataSetChanged();
                            BaseUtil.showNoData(llNoData, View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            BaseUtil.showNoData(llNoData, View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (recyclerView.getAdapter() == null) {
                            BaseUtil.showNoData(llNoData, View.VISIBLE);
                        }
                    }
                });
            }else {
                BaseUtil.showNoData(llNoData, View.GONE);
            }

            dialog.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog(dialog);
                    callback.onSuccess(mList);
                }
            });

            dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog(dialog);
                }
            });
            dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog(dialog);
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
