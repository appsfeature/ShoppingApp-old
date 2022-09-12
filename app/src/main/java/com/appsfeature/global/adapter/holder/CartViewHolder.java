package com.appsfeature.global.adapter.holder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.R;
import com.appsfeature.global.dialog.AppDialog;
import com.appsfeature.global.model.ContentModel;
import com.appsfeature.global.model.ProductDetail;
import com.google.gson.reflect.TypeToken;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;
import com.helper.util.GsonParser;
import com.squareup.picasso.Picasso;

/**
 * @implNote R.layout.slot_cart_view
 */
public class CartViewHolder extends ProductViewHolder implements View.OnClickListener {

    public final TextView tvTitle, tvPrice, tvSize, tvColor, tvQuantity;
    public final ImageView ivIcon;
    private final RemoveListener listener;
    private Activity activity;

    public interface RemoveListener {
        void onItemRemove(int position);

        void onItemSaveForLater(int position);
    }

    public CartViewHolder(@NonNull View view, RemoveListener listener) {
        super(view);
        this.listener = listener;
        ivIcon = view.findViewById(R.id.iv_icon);
        tvTitle = view.findViewById(R.id.tv_title);
        tvSize = view.findViewById(R.id.tv_size);
        tvColor = view.findViewById(R.id.tv_color);
        tvPrice = view.findViewById(R.id.tv_price);
        tvQuantity = view.findViewById(R.id.tv_quantity);
        (view.findViewById(R.id.ll_quantity)).setOnClickListener(this);
        (view.findViewById(R.id.btn_remove)).setOnClickListener(this);
        (view.findViewById(R.id.btn_save_for_later)).setOnClickListener(this);
    }

    public void setData(Activity activity, ContentModel item, String imageUrl) {
        this.activity = activity;
        tvTitle.setText(BaseUtil.fromHtml(item.getTitle()));
        ProductDetail productDetail = item.getProductDetail();
        if (productDetail != null) {
            if (productDetail.getPrice() > 0) {
                tvPrice.setText(getPrice(productDetail.getPrice(), item.getDiscountPrice()));
            } else {
                tvPrice.setText(getPrice(item.getPrice(), item.getDiscountPrice()));
            }
            tvSize.setText("Size - " + productDetail.getSize());
            tvSize.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(productDetail.getColor())) {
                tvColor.setText("Color - " + productDetail.getColor());
                tvColor.setVisibility(View.VISIBLE);
            } else {
                tvColor.setVisibility(View.GONE);
            }
        } else {
            tvPrice.setText(getPrice(item.getPrice(), item.getDiscountPrice()));
            tvSize.setVisibility(View.GONE);
        }

        if (ivIcon != null) {
            String imagePath = getImageUrlFromJson(imageUrl, item.getImage());
            int placeHolder = R.drawable.ic_placeholder_icon;
            if (BaseUtil.isValidUrl(imagePath)) {
                Picasso.get().load(imagePath)
                        .placeholder(placeHolder)
                        .into(ivIcon);
            } else {
                ivIcon.setImageResource(placeHolder);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_remove) {
            if (listener != null) {
                listener.onItemRemove(getAbsoluteAdapterPosition());
            }
        } else if (view.getId() == R.id.btn_save_for_later) {
            if (listener != null) {
                listener.onItemSaveForLater(getAbsoluteAdapterPosition());
            }
        } else if (view.getId() == R.id.ll_quantity) {
            displayPopupWindow(activity, tvQuantity);
        } else if (view.getId() == R.id.tv_quantity_1) {
            tvQuantity.setText("Qun - 1");
            dismissPopupWindow();
        } else if (view.getId() == R.id.tv_quantity_2) {
            tvQuantity.setText("Qun - 2");
            dismissPopupWindow();
        } else if (view.getId() == R.id.tv_quantity_3) {
            tvQuantity.setText("Qun - 3");
            dismissPopupWindow();
        } else if (view.getId() == R.id.tv_quantity_more) {
            AppDialog.openQuantity(activity, new Response.Status<String>() {
                @Override
                public void onSuccess(String response) {
                    tvQuantity.setText("Qun - " + response);
                }
            });
            dismissPopupWindow();
        }
    }

    private void dismissPopupWindow() {
        if(popup != null && popup.isShowing()) {
            popup.dismiss();
        }
    }

    private PopupWindow popup;

    private void displayPopupWindow(Activity activity, View anchorView) {
        Context wrapper = new ContextThemeWrapper(activity, R.style.ActivityTheme_ActionBar_Material);
        popup = new PopupWindow(wrapper);
        View layout = viewHolder(activity.getLayoutInflater().inflate(R.layout.popup_quantity, null));
        popup.setContentView(layout);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.showAsDropDown(anchorView, -50,0);
    }

    private View viewHolder(View inflate) {
        TextView tvQun1 = inflate.findViewById(R.id.tv_quantity_1);
        TextView tvQun2 = inflate.findViewById(R.id.tv_quantity_2);
        TextView tvQun3 = inflate.findViewById(R.id.tv_quantity_3);
        TextView tvQunMore = inflate.findViewById(R.id.tv_quantity_more);

        tvQun1.setOnClickListener(this);
        tvQun2.setOnClickListener(this);
        tvQun3.setOnClickListener(this);
        tvQunMore.setOnClickListener(this);
        return inflate;
    }
}
