package com.amandfarrell.www.changecalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Boolean mIgnoreNextTextChange = false;

    private EditText mTotalEditText;
    private EditText mCashEditText;

    private TextView mChangeDueTextView;

    //Dollar denomination TextViews
    private TextView mTwenty;
    private TextView mTen;
    private TextView mFive;
    private TextView mOne;
    private TextView mQuarter;
    private TextView mDime;
    private TextView mNickel;
    private TextView mPenny;

    private int mDollarDenom[] = {2000, 1000, 500, 100, 25, 10, 5, 1};
    private TextView mDollarTextViews[];

    private int mTotal = 0;
    private int mCash = 0;
    private int mChangeDue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTotalEditText = (EditText) findViewById(R.id.total);
        mCashEditText = (EditText) findViewById(R.id.cash);

        mChangeDueTextView = (TextView) findViewById(R.id.change_due);
        mTwenty = (TextView) findViewById(R.id.twenty);
        mTen = (TextView) findViewById(R.id.ten);
        mFive = (TextView) findViewById(R.id.five);
        mOne = (TextView) findViewById(R.id.one);
        mQuarter = (TextView) findViewById(R.id.quarter);
        mDime = (TextView) findViewById(R.id.dime);
        mNickel = (TextView) findViewById(R.id.nickel);
        mPenny = (TextView) findViewById(R.id.penny);

        mDollarTextViews = new TextView[]{mTwenty, mTen, mFive, mOne, mQuarter, mDime, mNickel, mPenny};

        //All the text should be selected when the text box is tapped
        mTotalEditText.setSelectAllOnFocus(true);
        mCashEditText.setSelectAllOnFocus(true);

        //Listen for changes to text so the formatting can be updated to match the changes
        mTotalEditText.addTextChangedListener(totalTextWatcher);
        mCashEditText.addTextChangedListener(cashTextWatcher);

        //Start all bills and coins at 0
        mTwenty.setText("0");
        mTen.setText("0");
        mFive.setText("0");
        mOne.setText("0");
        mQuarter.setText("0");
        mDime.setText("0");
        mNickel.setText("0");
        mPenny.setText("0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_donate:
                Intent intent = new Intent(MainActivity.this, DonateActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private TextWatcher totalTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (mIgnoreNextTextChange) {
                mIgnoreNextTextChange = false;
                return;
            } else {
                mIgnoreNextTextChange = true;
            }

            try {
                String totalStr = mTotalEditText.getText().toString();
                totalStr = totalStr.replaceAll("[$.]", "");
                if (totalStr == null || totalStr.isEmpty()) {
                    mTotal = 0;
                } else {
                    mTotal = Integer.parseInt(totalStr);
                }
                totalStr = getResources().getString(R.string.currency_symbol)
                        + String.format(Locale.ENGLISH, "%.2f", mTotal / 100.0);
                mTotalEditText.setText(totalStr);
                mTotalEditText.setSelection(mTotalEditText.getText().length());

                calculateChangeDue();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), R.string.large_number_error_toast, Toast.LENGTH_LONG).show();

                mTotal = 0;
                mTotalEditText.setText(R.string.zeroCurrency);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private TextWatcher cashTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (mIgnoreNextTextChange) {
                mIgnoreNextTextChange = false;
                return;
            } else {
                mIgnoreNextTextChange = true;
            }

            try {
                String cashStr = mCashEditText.getText().toString();
                cashStr = cashStr.replaceAll("[$.]", "");
                if (cashStr == null || cashStr.isEmpty()) {
                    mCash = 0;
                } else {
                    mCash = Integer.parseInt(cashStr);
                }
                cashStr = getResources().getString(R.string.currency_symbol)
                        + String.format(Locale.ENGLISH, "%.2f", mCash / 100.0);
                mCashEditText.setText(cashStr);

                //Todo remove?
                mCashEditText.setSelection(mCashEditText.getText().length());

                calculateChangeDue();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), R.string.large_number_error_toast, Toast.LENGTH_LONG).show();

                mCash = 0;
                mCashEditText.setText(R.string.zeroCurrency);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private void calculateChangeDue() {
        mChangeDue = mCash - mTotal;
        String changeDueStr = getResources().getString(R.string.currency_symbol)
                + String.format(Locale.ENGLISH, "%.2f", mChangeDue / 100.0);
        mChangeDueTextView.setText(changeDueStr);

        //Remember that the amounts are in pennies, not dollars
        if (mChangeDue > 0) {
            int bills;
            for (int i = 0; i < mDollarDenom.length; i++) {
                bills = mChangeDue / mDollarDenom[i];
                mDollarTextViews[i].setText(Integer.toString(bills));

                mChangeDue = mChangeDue % mDollarDenom[i];
            }

        } else {
            mTwenty.setText("0");
            mTen.setText("0");
            mFive.setText("0");
            mOne.setText("0");
            mQuarter.setText("0");
            mDime.setText("0");
            mNickel.setText("0");
            mPenny.setText("0");
        }
    }
}
