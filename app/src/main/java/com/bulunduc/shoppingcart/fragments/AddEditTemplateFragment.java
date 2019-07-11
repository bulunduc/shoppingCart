package com.bulunduc.shoppingcart.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private int position = -1;
    private TemplateDialogClickListener mTemplateDialogClickListener;

    public AddEditTemplateFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mContext = getActivity().getApplicationContext();
        if (args != null){
            mTemplateTitle = args.getString(AppConstants.KEY_TEMPLATE_TITLE);
            mProducts = args.getParcelableArrayList(AppConstants.KEY_TEMPLATE_PRODUCT_LIST);
            position = args.getInt(AppConstants.KEY_POSITION);
        }
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
            }
            else {
                AppUtilities.showToast(getActivity().getApplicationContext(), getString(R.string.check_fields)); //TODO описание получше
            }

        });
    }

    private Item getItemFromFields(){
        String title = etTitle.getText().toString();
        Double count = Double.parseDouble(etCount.getText().toString());
        String unit = spUnit.getSelectedItem().toString();
        Double price = Double.parseDouble(etPrice.getText().toString());

        Item item = new Item(title, count, unit, price);
        Log.d(TAG, "getItemFromFields: " + item.toString());
        return item;
    }
    private boolean checkProductList(){
        if (!etTitle.getText().toString().isEmpty()
                && !etCount.getText().toString().isEmpty()
                && !etPrice.getText().toString().isEmpty()) return true;
        return false;
    }
    private boolean checkTemplatesFields() {
        if (!etTemplateTitle.getText().toString().isEmpty()
                && mProducts.size() > 0) return true;
        return false;
    }

    private String getTemplateTitle(){
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
                mTemplateDialogClickListener.onTemplateAddClick(new Template(getTemplateTitle(),  mProducts),position);
                AppUtilities.showToast(getActivity().getApplicationContext(), "Шаблон добавлен!"); //TODO описание получше
                dialog.dismiss();

            } else {
                AppUtilities.showToast(getActivity().getApplicationContext(), getString(R.string.check_fields)); //TODO описание получше
            }
        });
        return dialog;
    }
}
