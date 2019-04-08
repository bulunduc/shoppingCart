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
import java.util.Comparator;

public class EditItemFragment extends DialogFragment {
    private static final String TAG = "EditItemFragment";
    private static Activity mActivity;
    private EditText etTitle;
    private EditText etCount;
    private Spinner spUnit;
    private Spinner spItemCategory;
    private EditText etPrice;
    private EditText etNewCategory;

    private String mItemName, mUnit, mNewCategory;
    private Double mMinCount, mPrice;
    private ArrayList<String> mCategories;
    private int mCategoryPosition;
    private int mPosition;

    public static EditItemFragment newInstance(Activity activity, String itemName, Double minCount, String unit, Double price, int category, ArrayList<String> categories, int position) {
        Bundle args = new Bundle();
        mActivity = activity;
        args.putString(AppConstants.KEY_ITEM_NAME, itemName);
        args.putDouble(AppConstants.KEY_ITEM_MIN_COUNT, minCount);
        args.putString(AppConstants.KEY_ITEM_UNIT, unit);
        args.putDouble(AppConstants.KEY_ITEM_PRICE, price);
        args.putInt(AppConstants.KEY_ITEM_CATEGORY, category);
        args.putStringArrayList(AppConstants.KEY_ITEM_CATEGORIES, categories);
        args.putInt(AppConstants.KEY_POSITION, position);
        EditItemFragment fragment = new EditItemFragment();
        fragment.setArguments(args);
        return fragment;
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
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here because we override this button later to change the close behaviour.
                    }
                })
                .setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(AppConstants.KEY_ITEM_CATEGORY, mCategories.get(mCategoryPosition));
                        intent.putExtra(AppConstants.KEY_POSITION, mPosition);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Result.DELETE.getCode(), intent);
                       // getTargetFragment().onActivityResult(getTargetRequestCode(), AppConstants.ITEM_DELETE, intent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Result.CANCEL.getCode(), intent);
                        //getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, intent);
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    initItemModelFields();
                    Item item = new Item(mItemName, mMinCount, mUnit, mPrice);

                    Intent intent = new Intent();
                    intent.putExtra(AppConstants.KEY_ITEM, item);
                    intent.putExtra(AppConstants.KEY_ITEM_CATEGORY, mNewCategory);
                    intent.putExtra(AppConstants.KEY_POSITION, mPosition);
                    //getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
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
            }
        });
        return dialog;
    }

    private void showInvalidFields(String field, EditText view) {
        AppUtilities.showToast(mActivity.getApplicationContext(), String.format(getString(R.string.invalid_field), field));
        view.getBackground().setColorFilter(getResources().getColor(R.color.editTextHighlightColor), PorterDuff.Mode.SRC_ATOP);
    }

    private void initVar() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mItemName = bundle.getString(AppConstants.KEY_ITEM_NAME);
            mMinCount = bundle.getDouble(AppConstants.KEY_ITEM_MIN_COUNT);
            mUnit = bundle.getString(AppConstants.KEY_ITEM_UNIT);
            mPrice = bundle.getDouble(AppConstants.KEY_ITEM_PRICE);
            mCategoryPosition = bundle.getInt(AppConstants.KEY_ITEM_CATEGORY);
            mCategories = bundle.getStringArrayList(AppConstants.KEY_ITEM_CATEGORIES);
            Collections.sort(mCategories);
            mPosition = bundle.getInt(AppConstants.KEY_POSITION);
        }
    }

    private void initView(View view) {
        etTitle = view.findViewById(R.id.etItemName);
        etCount = view.findViewById(R.id.etItemMinCount);
        etCount.setFilters(new NumberInputFilter[]{new NumberInputFilter(6, 2)});
        spUnit = view.findViewById(R.id.spItemUnit);
        etPrice = view.findViewById(R.id.etItemPrice);
        etPrice.setFilters(new NumberInputFilter[]{new NumberInputFilter(6, 2)});
        spItemCategory = view.findViewById(R.id.spItemCategory);
        etNewCategory = view.findViewById(R.id.etNewCategory);
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

        final ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(mActivity, android.R.layout.simple_spinner_item, mActivity.getResources().getStringArray(R.array.units));
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
        final String[] categories = new String[mCategories.size() + 1];
        for (int i = 0; i < categories.length - 1; i++) {
            categories[i] = mCategories.get(i);
        }
        categories[categories.length - 1] = getString(R.string.newCategory);

        final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(mActivity, R.layout.spinner_item, categories);
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spItemCategory.setAdapter(categoryAdapter);
        spItemCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategoryPosition = position;
                if (categories[position].equals(getString(R.string.newCategory))) {
                    etNewCategory.setVisibility(View.VISIBLE);
                    etNewCategory.setText("");
                } else {
                    etNewCategory.setVisibility(View.GONE);
                    etNewCategory.setText(mCategories.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spItemCategory.setSelection(mCategoryPosition);
    }


}
