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
import java.sql.Connection;
import java.sql.PreparedStatement;

import oracle.jdbc.proxy.annotation.Pre;


public class BuyFragment extends Fragment {
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();


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

        String bankS = null;
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

        Connection conn = null;
        Statement statement_1 = null;
        Statement statement_2 = null;
        Statement statement_3 = null;

        int i = 0;
        String username = UserLoginInfo.userEmail;
        int bank = 25;

        String statement1 = "UPDATE L1_Standings SET BANK = '" + bank + "' WHERE EMAIL = '" + username + "'";
        String statement2 = "UPDATE L1_Stocks SET TICKER_SYMBOL = '" + tick.getText().toString() + "' WHERE EMAIL = '" + username + "'";
        String statement3 = "UPDATE L1_Stocks SET NUM_SHARES = '" + shares.getText().toString() + "' WHERE EMAIL = '" + username + "'";

        try {
            conn = ConnectionManager.getConnection();
            statement_1 = conn.createStatement();
            statement_2 = conn.createStatement();
            statement_3 = conn.createStatement();

            statement_1.execute(statement1);
        } catch (SQLException e) {
        } finally {
            try {
                statement_2.execute(statement2);
            } catch (SQLException e) {
            } finally {
                statement_3.execute(statement3);
            }

        }
    }

    public class BankTask extends AsyncTask<Void, Void, String> {
        ResultSet rs, rs1, rs2, rs3, rs4, rs5, rs6;
        Statement st, st1, st2, st3, st4, st5, st6;
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
                    if (rs1 != null)
                        rs1.close();
                    if (rs2 != null)
                        rs2.close();
                    if (rs3 != null)
                        rs3.close();
                    if (rs4 != null)
                        rs4.close();
                    if (rs5 != null)
                        rs5.close();
                    if (rs6 != null)
                        rs6.close();
                    if (st != null)
                        st.close();
                    if (st1 != null)
                        st1.close();
                    if (st2 != null)
                        st2.close();
                    if (st3 != null)
                        st3.close();
                    if (st4 != null)
                        st4.close();
                    if (st5 != null)
                        st5.close();
                    if (st6 != null)
                        st6.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}



