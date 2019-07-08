package com.bulunduc.shoppingcart.fragments;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.adapters.TemplateProductsAdapter;
import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.models.CartItem;
import com.bulunduc.shoppingcart.models.Item;
import com.bulunduc.shoppingcart.utilities.AppUtilities;

import java.util.ArrayList;

public class ShowTemplateProductsFragment extends DialogFragment {

    private static final String TAG = "ShowTemplateProductsFragment";
    private static Context mContext;
    RecyclerView rv;
    TemplateProductsAdapter adapter;
    private static ArrayList<Item> mProducts = new ArrayList<>();
    private static String mCategory = "Template";

    public ShowTemplateProductsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        Bundle args = getArguments();
        if (args != null) {
            mCategory = args.getString(AppConstants.KEY_TEMPLATE_TITLE);
            mProducts = args.getParcelableArrayList(AppConstants.KEY_TEMPLATE_PRODUCT_LIST);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_template, null);
        rv = rootView.findViewById(R.id.rvTemplateProducts);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        adapter = new TemplateProductsAdapter(this.getActivity(), mProducts);
        rv.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setView(rootView)
                .setPositiveButton("OK", (dialog, which) -> {
                    for (Item mProduct : adapter.getCheckedProductList()) {
                        AppUtilities.addItemAndReturnCartList(mContext, new CartItem(mProduct, mCategory, false));
                    }
                    AppUtilities.showToast(mContext, getString(R.string.product_added_to_cart));

                })
                .setNegativeButton("Отмена", null);

        final AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }
}
