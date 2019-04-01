package com.bulunduc.shoppingcart.fragments;

import android.app.Activity;
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

    private Context mContext;
    private Activity mActivity;

    private int mCategoryId;
    private ArrayList<String> mCategories;
    private ArrayList<Item> mProducts;

    private ItemViewAdapter mAdapter;
    private RecyclerViewEmptySupport mRvProducts;
    private View mEmptyView;
    private ImageButton mDeleteCategory;

    public static CategoryPageFragment newInstance(int page, ArrayList<String> categories, ArrayList<Item> products) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putStringArrayList(CATEGORIES, categories );
        args.putParcelableArrayList(PRODUCTS, products);
        CategoryPageFragment fragment = new CategoryPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContext = mActivity.getApplicationContext();
        if (getArguments() != null) {
            mCategoryId = getArguments().getInt(ARG_PAGE) - 1;
            mCategories = new ArrayList<>();
            mCategories = getArguments().getStringArrayList(CATEGORIES);
            mProducts = getArguments().getParcelableArrayList(PRODUCTS);
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
        mDeleteCategory = mEmptyView.findViewById(R.id.deleteCategory);
        mRvProducts.setEmptyView(mEmptyView);
    }

    private void initFunctionality(){
        mDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ItemCategoryActivity)getActivity()).deleteCurrentCategory();

            }
        });

        mAdapter = new ItemViewAdapter(getActivity(), mProducts);
        mAdapter.setItemRecyclerViewClickListener(new ItemRecyclerViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                EditItemFragment editItemFragment = EditItemFragment.newInstance(getActivity(), mProducts.get(position).getItemName(),
                        mProducts.get(position).getCount(), mProducts.get(position).getCountUnit(),
                        mProducts.get(position).getPrice(),mCategoryId, mCategories,  position);
                editItemFragment.setTargetFragment(CategoryPageFragment.this, AppConstants.EDIT_ITEM_REQUEST_CODE);
                editItemFragment.show(manager, AppConstants.KEY_DIALOG_FRAGMENT);
            }

            @Override
            public void onItemDoubleClick(View view, int position) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                EditItemFragment editItemFragment = EditItemFragment.newInstance(getActivity(), mProducts.get(position).getItemName(),
                        mProducts.get(position).getCount(), mProducts.get(position).getCountUnit(),
                        mProducts.get(position).getPrice(),mCategoryId, mCategories,  position);
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
            if (resultCode == Result.OK.getCode()) {
                int position = data.getIntExtra(AppConstants.KEY_POSITION, AppConstants.INVALID_VALUE_IDENTIFIER);
                String newCategory = data.getStringExtra(AppConstants.KEY_ITEM_CATEGORY);

                Item item = data.getParcelableExtra(AppConstants.KEY_ITEM);
                ((ItemCategoryActivity)getActivity()).updateProductLists(item,newCategory, mCategories.get(mCategoryId), position);
                mAdapter.notifyDataSetChanged();
            }
            if (resultCode == Result.DELETE.getCode()) {
                int position = data.getIntExtra(AppConstants.KEY_POSITION, AppConstants.INVALID_VALUE_IDENTIFIER);
                String category = data.getStringExtra(AppConstants.KEY_ITEM_CATEGORY);

                ((ItemCategoryActivity)getActivity()).deleteItem(category, position);
            }
        }
    }

}
