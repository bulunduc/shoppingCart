package com.bulunduc.shoppingcart.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.activity.ItemCategoryActivity;
import com.bulunduc.shoppingcart.adapters.ItemViewAdapter;
import com.bulunduc.shoppingcart.adapters.RecyclerViewEmptySupport;
import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.constants.Result;
import com.bulunduc.shoppingcart.listeners.ItemRecyclerViewClickListener;
import com.bulunduc.shoppingcart.models.Item;
import java.util.ArrayList;

public class CategoryPageFragment extends Fragment{
    private static final String TAG = "CategoryPageFragment";
    private static final String ARG_PAGE = "ARG_PAGE";
    private static final String CATEGORIES = "CATEGORIES";
    private static final String PRODUCTS = "PRODUCTS";
    private static final String HIGHLIGHT = "highlight";

    private Context mContext;
    private Activity mActivity;

    private int mCategoryId;
    private ArrayList<String> mCategories;
    private ArrayList<Item> mProducts;
    private String highlightString;

    private ItemViewAdapter mAdapter;
    private RecyclerViewEmptySupport mRvProducts;
    private View mEmptyView;
    private ImageButton mDeleteCategory;

    public CategoryPageFragment(){}

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContext = mActivity.getApplicationContext();
        if (getArguments() != null) {
            mCategoryId = getArguments().getInt(ARG_PAGE) - 1;
            mCategories = new ArrayList<>();
            mCategories = getArguments().getStringArrayList(CATEGORIES);
            mProducts = getArguments().getParcelableArrayList(PRODUCTS);
            highlightString = getArguments().getString(HIGHLIGHT);
        }
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_category_viewpager, container, false);
        initView(view);
        initFunctionality();
        return view;
    }

    private void initView(View view){
        mRvProducts = view.findViewById(R.id.recycler_view);
        mRvProducts.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mEmptyView = view.findViewById(R.id.empty_view);
        mDeleteCategory = mEmptyView.findViewById(R.id.btn_delete_category);
        mRvProducts.setEmptyView(mEmptyView);
    }

    private void initFunctionality(){
        mDeleteCategory.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.confirm_deleting)
                    .setMessage(getString(R.string.confirm_deleting_category))
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton(getString(R.string.ok), (dialog, id) -> {
                        ((ItemCategoryActivity)getActivity()).deleteCurrentCategory();
                        dialog.dismiss();
                    })
                    .setNegativeButton(getString(R.string.cancel), (dialog, id) -> {
                        dialog.cancel();
                    });
            builder.show();

        });

        mAdapter = new ItemViewAdapter(getActivity(), mProducts, highlightString);
        mAdapter.setItemRecyclerViewClickListener(new ItemRecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }

            @Override
            public void onItemDoubleClick(int position) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Bundle args = new Bundle();
                args.putString(AppConstants.KEY_ITEM_NAME, mProducts.get(position).getItemName());
                args.putDouble(AppConstants.KEY_ITEM_COUNT, mProducts.get(position).getCount());
                args.putString(AppConstants.KEY_ITEM_UNIT, mProducts.get(position).getCountUnit());
                args.putDouble(AppConstants.KEY_ITEM_PRICE, mProducts.get(position).getPrice());
                args.putInt(AppConstants.KEY_ITEM_CATEGORY, mCategoryId);
                args.putStringArrayList(AppConstants.KEY_ITEM_CATEGORIES, mCategories);
                args.putInt(AppConstants.KEY_POSITION, position);

                EditItemFragment editItemFragment = new EditItemFragment();
                editItemFragment.setArguments(args);
                editItemFragment.setTargetFragment(CategoryPageFragment.this, AppConstants.EDIT_ITEM_REQUEST_CODE);
                editItemFragment.show(manager, AppConstants.KEY_DIALOG_FRAGMENT);
            }
        });
        mRvProducts.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.EDIT_ITEM_REQUEST_CODE) {
            int position = data.getIntExtra(AppConstants.KEY_POSITION, AppConstants.INVALID_VALUE_IDENTIFIER);
            String category = data.getStringExtra(AppConstants.KEY_ITEM_CATEGORY);
            if (resultCode == Result.OK.getCode()) {
                Item item = data.getParcelableExtra(AppConstants.KEY_ITEM);
                ((ItemCategoryActivity)getActivity()).updateProductLists(item, category, mCategories.get(mCategoryId), position);
                mAdapter.notifyDataSetChanged();
            }
            if (resultCode == Result.DELETE.getCode()) {
                ((ItemCategoryActivity)getActivity()).deleteItem(category, position);
            }
        }
    }

}
