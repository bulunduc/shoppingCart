package com.bulunduc.shoppingcart.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.bulunduc.shoppingcart.R;
import com.bulunduc.shoppingcart.constants.AppConstants;
import com.bulunduc.shoppingcart.exceptions.EmptyTextException;
import com.bulunduc.shoppingcart.exceptions.InvalidCountException;
import com.bulunduc.shoppingcart.exceptions.InvalidPriceException;
import com.bulunduc.shoppingcart.filters.NumberInputFilter;
import com.bulunduc.shoppingcart.listeners.AddItemDialogClickListener;
import com.bulunduc.shoppingcart.models.Item;
import com.bulunduc.shoppingcart.utilities.ActivityUtilities;
import com.bulunduc.shoppingcart.utilities.AppUtilities;

import java.util.ArrayList;

public class AddItemFragment extends DialogFragment {
    private static final String TAG = "ItemAddBottomSheet";
    private ArrayList<String> mCategories = new ArrayList<>();
    private int mCurrentCategoryPosition;

    private EditText mTitleEditText, mCountEditText, mPriceEditText, mNewCategoryEditText;
    private Spinner mUnitSpinner, mCategorySpinner;
    private ImageButton mAddItemImageButton;

    private String mTitle, mUnit, mCategory;
    private Double mCount, mPrice;

    private AddItemDialogClickListener mAddItemDialogClickListener;

    public AddItemFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_item_add, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVar();
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    private void initVar() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCategories = bundle.getStringArrayList(AppConstants.KEY_ITEM_CATEGORIES);
            mCurrentCategoryPosition = bundle.getInt(AppConstants.KEY_ITEM_CATEGORY_POSITION);
        }
        mAddItemDialogClickListener = (AddItemDialogClickListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_add, container, false);

        initView(view);
        initFunctionality();
        return view;
    }

    private void initView(View rootView) {
        mTitleEditText = rootView.findViewById(R.id.et_item_name);
        mCountEditText = rootView.findViewById(R.id.et_item_count);
        mCountEditText.setFilters(new NumberInputFilter[]{new NumberInputFilter(6, 2)});
        mUnitSpinner = rootView.findViewById(R.id.sp_item_unit);
        mPriceEditText = rootView.findViewById(R.id.et_item_price);
        mPriceEditText.setFilters(new NumberInputFilter[]{new NumberInputFilter(6, 2)});
        mCategorySpinner = rootView.findViewById(R.id.sp_item_category);
        mNewCategoryEditText = rootView.findViewById(R.id.et_item_new_category);
        mAddItemImageButton = rootView.findViewById(R.id.btn_add_item_to_category);
    }

    private void initFunctionality() {
        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitleEditText.getBackground().setColorFilter(getResources().getColor(R.color.editTextDefaultColor), PorterDuff.Mode.SRC_ATOP);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mCountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCountEditText.getBackground().setColorFilter(getResources().getColor(R.color.editTextDefaultColor), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPriceEditText.getBackground().setColorFilter(getResources().getColor(R.color.editTextDefaultColor), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, getActivity().getResources().getStringArray(R.array.units));
        unitAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mUnitSpinner.setAdapter(unitAdapter);
        mUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUnit = unitAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mCategories.add(mCategories.size(), getString(R.string.new_category));
        final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, mCategories.toArray(new String[mCategories.size()]));
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mCategorySpinner.setAdapter(categoryAdapter);
        mCategorySpinner.setSelection(mCurrentCategoryPosition);
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mNewCategoryEditText.setText(mCategories.get(position));
                if (mCategories.get(position).equals(getString(R.string.new_category))) {
                    mNewCategoryEditText.setText("");
                    mNewCategoryEditText.setVisibility(View.VISIBLE);
                    mNewCategoryEditText.requestFocus();
                } else mNewCategoryEditText.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAddItemImageButton.setOnClickListener(v -> {
            try {
                initItemModelFields();
                mAddItemDialogClickListener.onItemAddClick(new Item(mTitle, mCount, mUnit, mPrice), mCategory);
                clear();
                updateCategorySpinner();
            } catch (EmptyTextException e) {
                if (mTitle.isEmpty())
                    showInvalidFields(getString(R.string.field_title), mTitleEditText);
                else if (mCategory.isEmpty())
                    showInvalidFields(getString(R.string.category), mNewCategoryEditText);
            } catch (InvalidCountException e) {
                showInvalidFields(getString(R.string.field_count), mCountEditText);
            } catch (InvalidPriceException e) {
                showInvalidFields(getString(R.string.field_price), mPriceEditText);
            }
        });
    }

    private void updateCategorySpinner() {
        final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, mCategories.toArray(new String[mCategories.size()]));
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mCategorySpinner.setAdapter(categoryAdapter);
        mCategorySpinner.setSelection(mCurrentCategoryPosition);
        mNewCategoryEditText.setVisibility(View.INVISIBLE);
    }

    private void showInvalidFields(String field, EditText view) {
        AppUtilities.showToast(getActivity().getApplicationContext(), String.format(getString(R.string.invalid_field), field));
        view.getBackground().setColorFilter(getResources().getColor(R.color.editTextHighlightColor), PorterDuff.Mode.SRC_ATOP);
    }

    private void initItemModelFields() throws EmptyTextException, InvalidCountException, InvalidPriceException {
        mTitle = mTitleEditText.getText().toString();
        if (mTitle.isEmpty()) throw new EmptyTextException();
        try {
            mCount = Double.parseDouble(mCountEditText.getText().toString());
        } catch (NumberFormatException e) {
            throw new InvalidCountException();
        }
        try {
            mPrice = Double.parseDouble(mPriceEditText.getText().toString());
        } catch (NumberFormatException e) {
            throw new InvalidPriceException();
        }
        mCategory = mNewCategoryEditText.getText().toString();
        if (mCategory.isEmpty()) throw new EmptyTextException();
        if (!mCategories.contains(mCategory)) {
            mCategories.add(mCategory);
            mCurrentCategoryPosition = mCategories.indexOf(mCategory);
        }
    }

    private void clear() {
        clearValues();
        clearFields();
    }

    private void clearValues() {
        mTitle = "";
        mCount = 0.0;
        mPrice = 0.0;
    }

    private void clearFields() {
        mTitleEditText.setText("");
        mCountEditText.setText("");
        mPriceEditText.setText("");
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        ActivityUtilities.hideKeyboardFrom(getActivity().getApplicationContext(), getView());
    }
}
