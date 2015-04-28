package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;

import oracle.jdbc.proxy.annotation.Pre;


public class BuyFragment extends Fragment {
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    BankTask mBankTask = null;
    static String bankS;


    EditText setSymbol;
    EditText tick;
    EditText shares;
    TextView symbolOut;
    TextView priceOut;
    TextView changePercentageOut;
    TextView bank;
    Button getQuote;
    Button buyStock;

    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_buy, container, false);
        return rootview;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setSymbol = (EditText) view.findViewById(R.id.setSymbol);
        symbolOut = (TextView) view.findViewById(R.id.stockSymbolOutput);
        priceOut = (TextView) view.findViewById(R.id.stockPriceOutput);
        changePercentageOut = (TextView) view.findViewById(R.id.stockChangePriceOutput);
        getQuote = (Button) view.findViewById(R.id.get_quote_button);
        tick = (EditText) view.findViewById(R.id.buyStockName);
        shares = (EditText) view.findViewById(R.id.buySharesAmount);
        buyStock = (Button) view.findViewById(R.id.btnBuyStock);
        bank = (TextView) view.findViewById(R.id.bank);

        try {
            bankS = new BankTask().execute().get();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }catch(ExecutionException e) {
            e.printStackTrace();
        }

        bank.setText("Your bank: " + currencyFormat.format(Float.parseFloat(bankS)));


        getQuote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    List<String> results = new Yahoo().execute(setSymbol.getText().toString()).get();
                    setResult(results.get(0), results.get(1), results.get(2));
                    tick.setText((setSymbol.getText().toString()).toUpperCase());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        buyStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    BuyStock();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    public void setResult(String fstockSymbol, String stockPrice, String fstockChangePercentage) {
        symbolOut.setText(fstockSymbol);
        priceOut.setText(currencyFormat.format(Float.parseFloat(stockPrice)));
        changePercentageOut.setText(fstockChangePercentage + "%");
        char c = fstockChangePercentage.charAt(0);
        if (c == '+') {
            changePercentageOut.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else if (c == '-') {
            changePercentageOut.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    public void BuyStock() throws SQLException {

        try {

            List<String> results = new Yahoo().execute(tick.getText().toString()).get();
            float mSharePrice = Float.parseFloat(results.get(1));
            float mBankAmt = Float.parseFloat(bankS);
            float mShareNum = Float.parseFloat(shares.getText().toString());
            float mNewBank = mBankAmt - (mSharePrice * mShareNum);

            String mTick = tick.getText().toString().toUpperCase();
            String mShares = shares.getText().toString();

            if (mSharePrice*(Float.parseFloat(mShares))>(mBankAmt)) {
                Toast b = Toast.makeText(getActivity(),"You do not have sufficient funds. Enter a different amount.", Toast.LENGTH_LONG);
                b.show();
            } else {

                boolean test;

                test = new CheckTask(mTick).execute().get();

                if (test==true) {
                    new BuyTask(mTick, mShares, mNewBank, mSharePrice, mBankAmt, mShareNum).execute();
                }
                else {
                    new BuyTask2(mTick, mShares, mNewBank, mSharePrice, mBankAmt, mShareNum).execute();
                }

                bank.setText("Your bank: " + currencyFormat.format(mNewBank));

                tick.setText("");

                shares.setText("");

                Toast a = Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG);
                a.show();

            }

        }catch(InterruptedException e) {
            e.printStackTrace();
        }catch(ExecutionException e) {
            e.printStackTrace();
        }

    }

    public static class BuyTask extends AsyncTask<Void, Void, Void> {
        Connection conn = null;
        Statement statement_1 = null;
        Statement statement_2 = null;

        float newBank;
        float sharePrice;
        float bankAmt;
        float shareNum;

        String tick, shares;

        BuyTask(String mTick, String mShares, float mNewBank, float mSharePrice, float mBankAmt, float mShareNum) {
            tick = mTick;
            shares = mShares;
            newBank = mNewBank;
            sharePrice = mSharePrice;
            bankAmt = mBankAmt;
            shareNum = mShareNum;

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                String statement1 = "UPDATE L1_Standings SET BANK = '" + newBank + "' WHERE EMAIL = '" + UserLoginInfo.userEmail + "'";

                String statement2 = "INSERT INTO L1_STOCKS(userid,email,ticker_symbol,num_shares) VALUES" +
                        " (" + UserLoginInfo.userID + ",'" + UserLoginInfo.userEmail + "','" + tick +
                        "'," + shares + ")";

                conn = ConnectionManager.getConnection();
                statement_1 = conn.createStatement();
                statement_2 = conn.createStatement();

                statement_1.execute(statement1);
                statement_2.execute(statement2);

                return null;

            } catch(SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class BuyTask2 extends AsyncTask<Void, Void, Void> {
        Connection conn = null;
        ResultSet rs = null;
        Statement st = null;
        Statement statement_1 = null;
        Statement statement_2 = null;

        float newBank;
        float sharePrice;
        float bankAmt;
        float shareNum;
        float temp;
        float newShareNum;

        String tick, shares;

        BuyTask2(String mTick, String mShares, float mNewBank, float mSharePrice, float mBankAmt, float mShareNum) {
            tick = mTick;
            shares = mShares;
            newBank = mNewBank;
            sharePrice = mSharePrice;
            bankAmt = mBankAmt;
            shareNum = mShareNum;

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                conn = null;
                conn = ConnectionManager.getConnection();
                rs = null;
                st = null;
                if (conn != null)
                    st = conn.createStatement();
                if (st != null)
                    rs = st.executeQuery("SELECT * FROM L1_STOCKS WHERE USERID ='" + UserLoginInfo.userID + "' AND TICKER_SYMBOL='" + tick + "'");
                if (rs != null)
                    while (rs.next()) {
                        temp = Float.parseFloat(rs.getString("NUM_SHARES"));
                    }

                newShareNum = temp + (Float.parseFloat(shares));

                String statement1 = "UPDATE L1_Standings SET BANK = '" + newBank + "' WHERE EMAIL = '" + UserLoginInfo.userEmail + "'";

                String statement2 = "UPDATE L1_STOCKS SET NUM_SHARES=" + newShareNum + "WHERE TICKER_SYMBOL='" + tick + "'";

                statement_1 = conn.createStatement();
                statement_2 = conn.createStatement();

                statement_1.execute(statement1);
                statement_2.execute(statement2);

                return null;

            } catch(SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }





    public static class BankTask extends AsyncTask<Void, Void, String> {
        ResultSet rs;
        Statement st;
        Connection conn;

        @Override
        protected String doInBackground(Void... params) {
            String bankString = null;

            try {
                conn = null;
                conn = ConnectionManager.getConnection();
                rs = null;
                st = null;
                if (conn != null)
                    st = conn.createStatement();
                if (st != null)
                    rs = st.executeQuery("SELECT * FROM L1_STANDINGS WHERE USERID ='" + UserLoginInfo.userID + "'");
                if (rs != null)
                    while (rs.next()) {
                        bankString = rs.getString("BANK");
                    }

                return bankString;

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null)
                        rs.close();
                    if (st != null)
                        st.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    public static class CheckTask extends AsyncTask<Void, Void, Boolean> {
        ResultSet rs;
        Statement st;
        Connection conn;
        String ticker;

        CheckTask(String mTick) {
            ticker = mTick;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<String> list = new ArrayList<>();

            try {
                conn = null;
                conn = ConnectionManager.getConnection();
                rs = null;
                st = null;
                if (conn != null)
                    st = conn.createStatement();
                if (st != null)
                    rs = st.executeQuery("SELECT * FROM L1_STOCKS WHERE USERID ='" + UserLoginInfo.userID + "' AND TICKER_SYMBOL='" + ticker + "'");
                if (rs != null)
                    while (rs.next()) {
                        list.add(rs.getString("TICKER_SYMBOL"));
                    }

                if(list.isEmpty()) {
                    return true;
                }

                else return false;

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null)
                        rs.close();
                    if (st != null)
                        st.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}



