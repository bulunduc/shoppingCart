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
import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.filters.NumberInputFilter;
import com.bulunduc.shoppingcart.listeners.TemplateDialogClickListener;
import com.bulunduc.shoppingcart.models.Item;
import com.bulunduc.shoppingcart.models.Template;
import com.bulunduc.shoppingcart.utilities.AppUtilities;

import java.util.ArrayList;

public class AddEditTemplateFragment extends DialogFragment {

    private static final String TAG = "AddTemplateFragment";
    private Context mContext;
    RecyclerView rvProductsList;
    AddTemplateProductsAdapter adapter;
    private ArrayList<Item> mProducts = new ArrayList<>();
    private String mTemplateTitle = "";
    private EditText etTemplateTitle;
    private EditText etTitle;
    private EditText etCount;
    private EditText etPrice;
    private Spinner spUnit;
    private Button btnAddProduct;
    private int mPosition = AppConstants.INVALID_VALUE_IDENTIFIER;
    private TemplateDialogClickListener mTemplateDialogClickListener;

    public AddEditTemplateFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mContext = getActivity().getApplicationContext();
        if (args != null) {
            mTemplateTitle = args.getString(AppConstants.KEY_TEMPLATE_TITLE);
            mProducts = args.getParcelableArrayList(AppConstants.KEY_TEMPLATE_PRODUCT_LIST);
            mPosition = args.getInt(AppConstants.KEY_POSITION);
        }
    }


    private void initView(View rootView) {
        etTemplateTitle = rootView.findViewById(R.id.et_template_title);
        rvProductsList = rootView.findViewById(R.id.rv_templ_products);

        etTitle = rootView.findViewById(R.id.et_templ_prod_title);
        etCount = rootView.findViewById(R.id.et_templ_prod_count);
        spUnit = rootView.findViewById(R.id.sp_templ_prod_unit);
        etPrice = rootView.findViewById(R.id.et_templ_prod_price);
        btnAddProduct = rootView.findViewById(R.id.btn_templ_add_product);
        etCount.setFilters(new NumberInputFilter[]{new NumberInputFilter(6, 2)});
        etPrice.setFilters(new NumberInputFilter[]{new NumberInputFilter(6, 2)});
    }


    private void initFunctionality() {
        mTemplateDialogClickListener = (TemplateDialogClickListener) getActivity();
        etTemplateTitle.setText(mTemplateTitle);
        rvProductsList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new AddTemplateProductsAdapter(this.getActivity(), mProducts);
        rvProductsList.setAdapter(adapter);
        btnAddProduct.setOnClickListener(v -> {
            if (checkProductList()) {
                mProducts.add(getItemFromFields());
                rvProductsList.getAdapter().notifyDataSetChanged();
                etTitle.setText("");
                etCount.setText("1");
                etPrice.setText("100");
            } else {
                AppUtilities.showToast(getActivity().getApplicationContext(), getString(R.string.check_fields));
            }
        });
    }

    private Item getItemFromFields() {
        String title = etTitle.getText().toString();
        Double count = Double.parseDouble(etCount.getText().toString());
        String unit = spUnit.getSelectedItem().toString();
        Double price = Double.parseDouble(etPrice.getText().toString());
        return new Item(title, count, unit, price);
    }

    private boolean checkProductList() {
        return !etTitle.getText().toString().isEmpty()
                && !etCount.getText().toString().isEmpty()
                && !etPrice.getText().toString().isEmpty();
    }

    private boolean checkTemplatesFields() {
        return !etTemplateTitle.getText().toString().isEmpty()
                && mProducts.size() > 0;
    }

    private String getTemplateTitle() {
        return etTemplateTitle.getText().toString();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_template, null);
        initView(rootView);
        initFunctionality();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setView(rootView)
                .setPositiveButton("OK", (dialog, which) -> {/*overrided*/})

                .setNegativeButton("Отмена", null);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (checkTemplatesFields()) {
                mTemplateDialogClickListener.onTemplateAddClick(new Template(getTemplateTitle(), mProducts), mPosition);
                AppUtilities.showToast(getActivity().getApplicationContext(), getString(R.string.added_template_message));
                dialog.dismiss();

            } else {
                AppUtilities.showToast(getActivity().getApplicationContext(), getString(R.string.check_fields));             }
        });
        return dialog;
    }
}
