package com.bulunduc.shoppingcart.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.constants.Result;
import com.bulunduc.shoppingcart.exceptions.EmptyTextException;
import com.bulunduc.shoppingcart.exceptions.InvalidCountException;
import com.bulunduc.shoppingcart.exceptions.InvalidPriceException;
import com.bulunduc.shoppingcart.filters.NumberInputFilter;
import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.models.Item;
import com.bulunduc.shoppingcart.utilities.AppUtilities;

import java.util.ArrayList;
import java.util.Collections;

public class EditItemFragment extends DialogFragment {
    private static final String TAG = "EditItemFragment";
    private Activity mActivity;
    private EditText etTitle;
    private EditText etCount;
    private Spinner spUnit;
    private Spinner spItemCategory;
    private EditText etPrice;
    private EditText etNewCategory;

    private String mItemName, mUnit, mNewCategory;
    private Double mMinCount, mPrice;
    private ArrayList<String> mCategories;
    private String mCategory;
    private int mPosition;

    public EditItemFragment() {
        mCategories = new ArrayList<>();
    }

    private void initItemModelFields() throws EmptyTextException, InvalidCountException, InvalidPriceException {
        mItemName = etTitle.getText().toString();
        if (mItemName.isEmpty()) throw new EmptyTextException();
        try {
            mMinCount = Double.parseDouble(etCount.getText().toString());
        } catch (NumberFormatException e) {
            throw new InvalidCountException();
        }
        try {
            mPrice = Double.parseDouble(etPrice.getText().toString());

        } catch (NumberFormatException e) {
            throw new InvalidPriceException();
        }
        mUnit = spUnit.getSelectedItem().toString();
        mNewCategory = etNewCategory.getText().toString();
        if (mNewCategory.isEmpty()) throw new EmptyTextException();

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_item, null);
        initVar();
        initView(rootView);
        initFunctionality();
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(rootView)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    //Do nothing here because we override this button later to change the close behaviour.
                })
                .setNeutralButton(R.string.delete, (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.putExtra(AppConstants.KEY_ITEM_CATEGORY, mCategory);
                    intent.putExtra(AppConstants.KEY_POSITION, mPosition);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Result.DELETE.getCode(), intent);
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    Intent intent = new Intent();
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Result.CANCEL.getCode(), intent);
                });

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            try {
                initItemModelFields();
                Item item = new Item(mItemName, mMinCount, mUnit, mPrice);

                Intent intent = new Intent();
                intent.putExtra(AppConstants.KEY_ITEM, item);
                intent.putExtra(AppConstants.KEY_ITEM_CATEGORY, mNewCategory);
                intent.putExtra(AppConstants.KEY_POSITION, mPosition);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Result.OK.getCode(), intent);
                dialog.dismiss();
            } catch (EmptyTextException e) {
                if (mItemName.isEmpty()) showInvalidFields(getString(R.string.field_title), etTitle);
                else if (mNewCategory.isEmpty()) showInvalidFields(getString(R.string.category), etNewCategory);

                showInvalidFields(getString(R.string.field_title), etTitle);
            } catch (InvalidCountException e) {
                showInvalidFields(getString(R.string.field_count), etCount);
            } catch (InvalidPriceException e) {
                showInvalidFields(getString(R.string.field_price), etPrice);
            }
        });
        return dialog;
    }

    private void showInvalidFields(String field, EditText view) {
        AppUtilities.showToast(mActivity.getApplicationContext(), String.format(getString(R.string.invalid_field), field));
        view.getBackground().setColorFilter(getResources().getColor(R.color.editTextHighlightColor), PorterDuff.Mode.SRC_ATOP);
    }

    private void initVar() {
        mActivity = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mItemName = bundle.getString(AppConstants.KEY_ITEM_NAME);
            mMinCount = bundle.getDouble(AppConstants.KEY_ITEM_COUNT);
            mUnit = bundle.getString(AppConstants.KEY_ITEM_UNIT);
            mPrice = bundle.getDouble(AppConstants.KEY_ITEM_PRICE);
            mCategory = bundle.getString(AppConstants.KEY_ITEM_CATEGORY);
            mCategories.addAll(bundle.getStringArrayList(AppConstants.KEY_ITEM_CATEGORIES));
            //Collections.sort(mCategories);
            mPosition = bundle.getInt(AppConstants.KEY_POSITION);
        }
    }

    private void initView(View view) {
        etTitle = view.findViewById(R.id.et_edit_item_name);
        etCount = view.findViewById(R.id.et_edit_item_count);
        etCount.setFilters(new NumberInputFilter[]{new NumberInputFilter(6, 2)});
        spUnit = view.findViewById(R.id.sp_edit_item_unit);
        etPrice = view.findViewById(R.id.et_edit_item_price);
        etPrice.setFilters(new NumberInputFilter[]{new NumberInputFilter(6, 2)});
        spItemCategory = view.findViewById(R.id.sp_edit_item_category);
        etNewCategory = view.findViewById(R.id.et_edit_new_category);
    }

    private void initFunctionality() {
        etTitle.setText(mItemName);
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etTitle.getBackground().setColorFilter(getResources().getColor(R.color.editTextDefaultColor), PorterDuff.Mode.SRC_ATOP);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etCount.setText(mMinCount.toString());
        etCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etCount.getBackground().setColorFilter(getResources().getColor(R.color.editTextDefaultColor), PorterDuff.Mode.SRC_ATOP);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, mActivity.getResources().getStringArray(R.array.units));
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spUnit.setAdapter(spinnerAdapter);
        spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUnit = spinnerAdapter.getItem(position).toString();
                etCount.setText(Item.getStringFormatCount(mMinCount, mUnit));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spUnit.setSelection(spinnerAdapter.getPosition(mUnit));
        etPrice.setText(mPrice.toString());
        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etPrice.getBackground().setColorFilter(getResources().getColor(R.color.editTextDefaultColor), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mCategories.add(getString(R.string.new_category));

        final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(mActivity, R.layout.spinner_item, mCategories);
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spItemCategory.setAdapter(categoryAdapter);
        spItemCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategory = mCategories.get(position);
                Log.d(TAG, position + " : " + mCategory);

                if (mCategory.equals(getString(R.string.new_category))) {
                    etNewCategory.setVisibility(View.VISIBLE);
                    etNewCategory.setText("");
                } else {
                    etNewCategory.setVisibility(View.GONE);
                    etNewCategory.setText(mCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Log.d(TAG, "mCategories : " + mCategories.indexOf(mCategory));

        spItemCategory.setSelection(mCategories.indexOf(mCategory));
        //spItemCategory.setSelection(mCategory); //TODO check index here
    }
}
