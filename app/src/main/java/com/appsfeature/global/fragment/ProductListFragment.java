package com.appsfeature.global.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.appsfeature.global.R;
import com.appsfeature.global.adapter.HomeChildAdapter;
import com.appsfeature.global.adapter.app.ProductAdapter;
import com.appsfeature.global.dialog.AppDialog;
import com.appsfeature.global.listeners.CategoryType;
import com.appsfeature.global.listeners.FilterType;
import com.appsfeature.global.listeners.LoadMore;
import com.appsfeature.global.model.AppBaseModel;
import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.model.ContentModel;
import com.appsfeature.global.model.ExtraProperty;
import com.appsfeature.global.model.FilterModel;
import com.appsfeature.global.network.AppDataManager;
import com.appsfeature.global.util.ClassUtil;
import com.dynamic.adapter.holder.DMAutoSliderViewHolder;
import com.dynamic.fragment.base.DMBaseGenericFragment;
import com.dynamic.listeners.DynamicCallback;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;
import com.helper.widget.RecyclerViewCardMarginGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ProductListFragment extends DMBaseGenericFragment<ExtraProperty> implements LoadMore {
    private View llNoData, llLoadMore;
    private ProductAdapter adapter;
    private final List<ContentModel> mList = new ArrayList<>();
    private Activity activity;
    private RecyclerView rvList;
    private SwipeRefreshLayout swipeRefresh;
    private ViewPager2 viewPager;
    private int mTotalRows = 0, mTotalPages = 0, mCurrentPage = 1;
    private boolean isLoadMore = true;
    private final Map<Integer, String> filterMap = new TreeMap<>();
    private final List<FilterModel> mFilterList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        activity = getActivity();
        initView(view);
        loadData();
        return view;
    }

    private void loadData() {
        getDataFromServer(mCurrentPage);
    }


    private void initView(View view) {
        llNoData = view.findViewById(R.id.ll_no_data);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        rvList = view.findViewById(R.id.recycler_view);
        llLoadMore = view.findViewById(R.id.ll_load_more);
        viewPager = view.findViewById(R.id.view_pager);
        rvList.addItemDecoration(new RecyclerViewCardMarginGrid(2, 16, true));
        rvList.setLayoutManager(new GridLayoutManager(activity, 2));

        adapter = new ProductAdapter(activity, mList, this, new Response.OnClickListener<ContentModel>() {
            @Override
            public void onItemClicked(View view, ContentModel item) {
                openItemOnClicked(view, item);
            }
        });
        rvList.setAdapter(adapter);

        if (swipeRefresh != null) {
            showProgress(false);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData();
                }
            });
        }
    }

    private void openItemOnClicked(View view, ContentModel item) {
        ClassUtil.openActivityProductView(activity, property, item);
    }

    private void getDataFromServer(int currentPage) {
        this.mCurrentPage = currentPage;
        AppDataManager.get(activity).getAppProductBySubCategory(property.getParentId(), property.getCatId(), currentPage, filterMap, new DynamicCallback.Listener<AppBaseModel>() {
            @Override
            public void onSuccess(AppBaseModel response) {
                showProgress(false);
                loadList(response);
            }

            @Override
            public void onValidate(AppBaseModel list, Response.Status<AppBaseModel> callback) {
                DynamicCallback.Listener.super.onValidate(list, callback);
            }

            @Override
            public void onFailure(Exception e) {
                showProgress(false);
                if (mList.size() == 0) {
                    BaseUtil.showNoData(llNoData, View.VISIBLE);
                }else {
                    BaseUtil.showNoData(llNoData, View.GONE);
                }
            }

            @Override
            public void onRequestCompleted() {
                hideLoadMoreUi();
                showProgress(false);
            }
        });
    }

    private void loadList(AppBaseModel response) {
        updatePagination(response);
        rvList.setVisibility(View.VISIBLE);
        int initialSize = mList.size();
        BaseUtil.showNoData(llNoData, View.GONE);
        if (response.getProductList() != null && response.getProductList().size() > 0) {
            mList.addAll(response.getProductList());
        }
        if (mList.size() <= 0) {
            BaseUtil.showNoData(llNoData, View.VISIBLE);
        }
        adapter.notifyItemRangeChanged(initialSize, mList.size());
        if (response.getSliderList() != null && response.getSliderList().size() > 0) {
            viewPager.setVisibility(View.VISIBLE);
            AppDataManager.get(activity).processSliderList(response.getSliderList(), new Response.Status<List<ContentModel>>() {
                @Override
                public void onSuccess(List<ContentModel> response) {
                    loadSliderList(response);
                }
            });
        } else {
            viewPager.setVisibility(View.GONE);
        }
    }


    private void loadSliderList(List<ContentModel> sliderList) {
        DMAutoSliderViewHolder<CategoryModel, ContentModel> slider = new DMAutoSliderViewHolder((View) viewPager.getParent()) {
            @Override
            protected RecyclerView.Adapter<RecyclerView.ViewHolder> getChildAdapter(int itemType, Object category, List childList) {
                return new HomeChildAdapter(activity, itemType, (CategoryModel) category, (List<ContentModel>) childList, new DynamicCallback.OnClickListener<CategoryModel, ContentModel>() {
                    @Override
                    public void onItemClicked(View v, CategoryModel category, ContentModel item) {

                    }
                });
            }
        };
        CategoryModel mCategory = new CategoryModel();
        mCategory.setItemType(CategoryType.TYPE_VIEWPAGER_AUTO_SLIDER_NO_TITLE);
        mCategory.setChildList(sliderList);
        slider.setData(mCategory, 0);
    }

    protected void showProgress(boolean isShow) {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(isShow);
        }
    }

    @Override
    public void onLoadMore() {
        if (isLoadMore)
            handleLoadMore();
    }

    private void handleLoadMore(){
        // check is load more in progress
        // if not in progress call api for load more
        if (!isLoadMoreUiVisible() && mList.size() >= 9){
            mCurrentPage += 1;
            if(mCurrentPage < mTotalPages) {
                showLoadMoreUi();
                getDataFromServer(mCurrentPage);
            }else {
                isLoadMore = false;
            }
        }
    }

    private void updatePagination(AppBaseModel response) {
        this.mTotalRows = response.getTotalRows();
        this.mTotalPages = response.getTotalPage();
        if(mTotalRows < 10 || mTotalPages <= 0 || mList.size() == mTotalRows){
            isLoadMore = false;
        }
    }

    private void hideLoadMoreUi() {
        if (llLoadMore != null) {
            llLoadMore.setVisibility(View.GONE);
        }
    }

    private void showLoadMoreUi() {
        if (llLoadMore != null) {
            llLoadMore.setVisibility(View.VISIBLE);
        }
    }

    private boolean isLoadMoreUiVisible() {
        return llLoadMore != null && llLoadMore.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_product, menu);
//        MenuItem testingMenu = menu.findItem(R.id.action_testing);
//        if (testingMenu != null) {
//            testingMenu.setVisible(AppConstant.IS_TESTING);
//        }
//
//        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
//        searchView = (SearchView) searchViewItem.getActionView();
//        searchView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                getAppAnalytics().onSubjectSearchClicked(AnalyticsKeys.ParamValue.SEARCH);
//                return false;
//            }
//        });
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchView.clearFocus();
//                /*   if(list.contains(query)){
//                    adapter.getFilter().filter(query);
//                }else{
//                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
//                }*/
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.setSearchQuery(newText);
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_filter) {
            AppDialog.openFilterProduct(activity, mFilterList, new Response.Status<List<FilterModel>>() {
                @Override
                public void onSuccess(List<FilterModel> response) {
                    AppDataManager.getFilterData(response, new Response.Status<Map<Integer, String>>() {
                        @Override
                        public void onSuccess(Map<Integer, String> result) {
                            if(result != null && result.size() > 0){
                                applyFilter(result);
                            }
                        }
                    });
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void applyFilter(Map<Integer, String> response) {
        this.mList.clear();
        this.adapter.notifyDataSetChanged();
        this.filterMap.clear();
        this.filterMap.putAll(response);
        BaseUtil.showNoDataProgress(llNoData);
        getDataFromServer(1);
    }
}
