package com.bulunduc.shoppingcart.fragments;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.adapters.TemplateProductsAdapter;
import com.bulunduc.shoppingcart.constants.Result;
import com.bulunduc.shoppingcart.models.Item;

import java.util.ArrayList;

public class ShowTemplateProductsFragment extends DialogFragment {

    private static final String TAG = "ShowTemplateProductsFragment";
    RecyclerView rv;
    TemplateProductsAdapter adapter;
    private static ArrayList<Item> mProducts = new ArrayList<>();

    public static ShowTemplateProductsFragment newInstance(ArrayList<Item> products){
        mProducts = products;
        return new ShowTemplateProductsFragment();
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
                .setPositiveButton("OK", null)
                .setNegativeButton("Отмена", null);

        final AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }
}
