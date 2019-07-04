package com.bulunduc.shoppingcart.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.adapters.AddTemplateProductsAdapter;
import com.bulunduc.shoppingcart.models.Item;

import java.util.ArrayList;

public class AddEditTemplateFragment extends DialogFragment {

    private static final String TAG = "ShowTemplateProductsFragment";
    private static Context mContext;
    RecyclerView mProductsList;
    AddTemplateProductsAdapter adapter;
    private static ArrayList<Item> mProducts = new ArrayList<>();
    private static String mTemplateTitle = "title";

    public static AddEditTemplateFragment newInstance(Context context){
        mContext = context;
        return new AddEditTemplateFragment();
    }

    public static AddEditTemplateFragment newInstance(Context context, String title, ArrayList<Item> products){
        mContext = context;
        mProducts = products;
        mTemplateTitle = title;
        return new AddEditTemplateFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_template, null);
        mProductsList = rootView.findViewById(R.id.rvProducts);
        mProductsList.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        adapter = new AddTemplateProductsAdapter(this.getActivity(), mProducts);
        mProductsList.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setView(rootView)
                .setPositiveButton("OK", (dialog, which) -> {

                })
                .setNegativeButton("Отмена", null);

        final AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }
}
