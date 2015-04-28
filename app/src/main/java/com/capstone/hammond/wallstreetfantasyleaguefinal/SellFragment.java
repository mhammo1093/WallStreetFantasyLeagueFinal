package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class SellFragment extends Fragment {
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    Button sellStock;
    EditText tick;
    EditText shares;
    TextView bankView;
    static String bankA;

    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_sell, container, false);
        return rootview;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sellStock = (Button)view.findViewById(R.id.btnSellStock);
        tick = (EditText) view.findViewById(R.id.sellStockName);
        shares = (EditText) view.findViewById(R.id.sellSharesAmount);
        bankView = (TextView) view.findViewById(R.id.bank);

        try {
            List<String> results = new SellableStocks().execute().get();

            ListView userStocks = (ListView) view.findViewById(R.id.userStocks);

            ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, results);

            userStocks.setAdapter(listAdapter);

            bankA = new BuyFragment.BankTask().execute().get();

            bankView.setText("Your bank: " + (currencyFormat.format(Float.parseFloat(bankA))));

        }catch(InterruptedException e) {
            e.printStackTrace();
        }catch(ExecutionException e) {
            e.printStackTrace();
        }



        sellStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    sellStockM();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void sellStockM() throws SQLException {

        try {

            List<String> results = new Yahoo().execute(tick.getText().toString()).get();
            float mSharePrice = Float.parseFloat(results.get(1));
            float mBankAmt = Float.parseFloat(bankA);
            float mShareNum = Float.parseFloat(shares.getText().toString());
            float mNewBank = mBankAmt + (mSharePrice * mShareNum);

            String mTick = tick.getText().toString().toUpperCase();
            String mShares = shares.getText().toString();

            boolean test,test2;

            test = new BuyFragment.CheckTask(mTick).execute().get();

            test2 = new CheckTask2(mShares, mTick).execute().get();


            if(test==true) {
                Toast c = Toast.makeText(getActivity(),"You do not have any stocks from that company",Toast.LENGTH_LONG);
                c.show();
            }

            if(test2==true) {
                Toast d = Toast.makeText(getActivity(),"You do not have that many stocks from that company",Toast.LENGTH_LONG);
                d.show();
            }
            else {

                new SellTask(mTick, mShares, mNewBank, mSharePrice, mBankAmt, mShareNum).execute();

                bankView.setText("Your bank: " + currencyFormat.format(mNewBank));

                tick.setText("");

                shares.setText("");

                Toast a = Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG);
                a.show();

                List<String> results2 = new SellableStocks().execute().get();

                ListView userStocks = (ListView)rootview.findViewById(R.id.userStocks);

                ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, results2);

                userStocks.setAdapter(listAdapter);

                bankA = new BuyFragment.BankTask().execute().get();

                bankView.setText("Your bank: " + (currencyFormat.format(Float.parseFloat(bankA))));

            }



        }catch(InterruptedException e) {
            e.printStackTrace();
        }catch(ExecutionException e) {
            e.printStackTrace();
        }

    }

    public class SellableStocks extends AsyncTask<Void, Void, List<String>> {
        List<String> results;
        Connection conn;
        ResultSet rs;
        Statement st;

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                results =  new ArrayList<>();

                conn = null;
                conn = ConnectionManager.getConnection();
                rs = null;
                st = null;
                if (conn != null)
                    st = conn.createStatement();
                if (st != null)
                    rs = st.executeQuery("SELECT * FROM L1_STOCKS WHERE USERID = '" + UserLoginInfo.userID + "'");
                if (rs != null)
                    while(rs.next()) {
                        String tick = (rs.getString("TICKER_SYMBOL"));
                        String share = (rs.getString("NUM_SHARES"));
                        results.add(share + " shares of " + tick.toUpperCase());
                    }

                return results;

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(rs!=null)
                        rs.close();
                    if(st!=null)
                        st.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }
    }

    public static class SellTask extends AsyncTask<Void, Void, Void> {
        Connection conn = null;
        ResultSet rs;
        Statement st;
        Statement statement_1 = null;
        Statement statement_2 = null;

        float newBank;
        float sharePrice;
        float bankAmt;
        float shareNum;
        float temp;
        float newShareNum;

        String tick, shares;

        SellTask(String mTick, String mShares, float mNewBank, float mSharePrice, float mBankAmt, float mShareNum) {
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

                newShareNum = temp - (Float.parseFloat(shares));

                String statement1 = "UPDATE L1_Standings SET BANK = '" + newBank + "' WHERE EMAIL = '" + UserLoginInfo.userEmail + "'";

                String statement2 = "UPDATE L1_STOCKS SET NUM_SHARES =" + newShareNum + " WHERE TICKER_SYMBOL='" + tick + "' AND USERID=" + UserLoginInfo.userID;

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

    public static class CheckTask2 extends AsyncTask<Void, Void, Boolean> {
        ResultSet rs;
        Statement st;
        Connection conn;
        String mNShares, ticker;

        CheckTask2(String shareNum, String tickerS) {
            mNShares = shareNum;
            ticker = tickerS;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<String> list = new ArrayList<>();
            String nShares;
            nShares = null;

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
                        nShares = (rs.getString("NUM_SHARES"));
                    }

                if((Float.parseFloat(mNShares))>(Float.parseFloat(nShares))) {
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
