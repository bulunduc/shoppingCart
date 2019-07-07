package com.bulunduc.shoppingcart.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.adapters.AddTemplateProductsAdapter;
import com.bulunduc.shoppingcart.filters.NumberInputFilter;
import com.bulunduc.shoppingcart.models.Item;
import com.bulunduc.shoppingcart.utilities.AppUtilities;

import java.util.ArrayList;

public class AddEditTemplateFragment extends DialogFragment {

    private static final String TAG = "ShowTemplateProductsFragment";
    private static Context mContext;
    RecyclerView rvProductsList;
    AddTemplateProductsAdapter adapter;
    private static ArrayList<Item> mProducts = new ArrayList<>();
    private static String mTemplateTitle = "title";
    private EditText etTemplateTitle;
    private EditText etTitle;
    private EditText etCount;
    private EditText etPrice;
    private Spinner spUnit;
    private Button btnAddProduct;

    public static AddEditTemplateFragment newInstance(Context context) {
        mContext = context;
        return new AddEditTemplateFragment();
    }

    public static AddEditTemplateFragment newInstance(Context context, String title, ArrayList<Item> products) {
        mContext = context;
        mProducts = products;
        mTemplateTitle = title;
        return new AddEditTemplateFragment();
    }

    private void initView(View rootView) {
        etTemplateTitle = rootView.findViewById(R.id.etTemplateTitle);
        rvProductsList = rootView.findViewById(R.id.rvTemplProducts);

        etTitle = rootView.findViewById(R.id.etTemplProdTitle);
        etCount = rootView.findViewById(R.id.etTemplProdCount);
        spUnit = rootView.findViewById(R.id.spTemplProdUnit);
        etPrice = rootView.findViewById(R.id.etTemplProdPrice);
        btnAddProduct = rootView.findViewById(R.id.btnTemplAddProduct);
        etCount.setFilters(new NumberInputFilter[]{new NumberInputFilter(6, 2)});
        etPrice.setFilters(new NumberInputFilter[]{new NumberInputFilter(6, 2)});
    }

    private boolean checkFields() {
        if (!etTemplateTitle.getText().toString().isEmpty()
                && mProducts.size() > 0) return true;
        return false;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_template, null);
        initView(rootView);
        rvProductsList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new AddTemplateProductsAdapter(this.getActivity(), mProducts);
        rvProductsList.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setView(rootView)
                .setPositiveButton("OK", (dialog, which) -> {/*overrided*/})

                .setNegativeButton("Отмена", null);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (checkFields()) {

            } else {
                AppUtilities.showToast(getActivity().getApplicationContext(), getString(R.string.check_fields));
            }
        });
        return dialog;
    }
}
