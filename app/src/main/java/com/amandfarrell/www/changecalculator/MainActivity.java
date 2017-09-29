package com.amandfarrell.www.changecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Boolean mIgnoreNextTextChange = false;

    private TextView mTotalTextView;
    private TextView mCashTextView;
    private TextView mChangeDueTextView;

    private EditText mTotalEditText;
    private EditText mCashEditText;

    private double mTotal = 0;
    private double mCash = 0;
    private double mChangeDue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTotalTextView = (TextView) findViewById(R.id.total_textview);
        //mCashTextView = (TextView) findViewById(R.id.cash_textview);
        mChangeDueTextView = (TextView) findViewById(R.id.change_due);

        mTotalEditText = (EditText) findViewById(R.id.total);
        mCashEditText = (EditText) findViewById(R.id.cash);

        mTotalEditText.addTextChangedListener(totalTextWatcher);
        mCashEditText.addTextChangedListener(cashTextWatcher);

    }

    private TextWatcher totalTextWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (mIgnoreNextTextChange) {
                mIgnoreNextTextChange = false;
                return;}
            else{
                mIgnoreNextTextChange = true;
            }

            String totalStr = mTotalEditText.getText().toString();
            totalStr = totalStr.replaceAll("[$.]","");
            if( totalStr == null || totalStr.isEmpty()) {
                mTotal = 0;
            }
            else {
                mTotal = Double.parseDouble(totalStr);
            }
            totalStr = getResources().getString(R.string.currency_symbol)
                    + String.format(Locale.getDefault(), "%.2f",  mTotal/100.0);
            mTotalEditText.setText(totalStr);
            mTotalEditText.setSelection(mTotalEditText.getText().length());

            calculateChangeDue();
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    private TextWatcher cashTextWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (mIgnoreNextTextChange) {
                mIgnoreNextTextChange = false;
                return;}
            else{
                mIgnoreNextTextChange = true;
            }

            String cashStr = mCashEditText.getText().toString();
            cashStr = cashStr.replaceAll("[$.]","");
            if (cashStr == null || cashStr.isEmpty()){
                mCash = 0;
            }
            else {
                mCash = Double.parseDouble(cashStr);
            }
            cashStr = getResources().getString(R.string.currency_symbol)
                    + String.format(Locale.getDefault(), "%.2f", mCash/100.0);
            mCashEditText.setText(cashStr);
            mCashEditText.setSelection(mCashEditText.getText().length());

            calculateChangeDue();
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    private void calculateChangeDue(){
        mChangeDue = mCash - mTotal;
        String changeDueStr = getResources().getString(R.string.currency_symbol)
                + String.format(Locale.getDefault(), "%.2f", mChangeDue/100.0);
        mChangeDueTextView.setText(changeDueStr);
    }
}
