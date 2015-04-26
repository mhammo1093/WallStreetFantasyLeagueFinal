package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class WeeklyMatchFragment extends Fragment {
    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_weekly_match, container, false);
        return rootview;

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            TextView user1;
            TextView user2;
            TextView bank1;
            TextView bank2;

            List<String> weeklyMatch = new MatchDetails().execute().get();

            user1 = (TextView) view.findViewById(R.id.player1);
            user1.setText(UserLoginInfo.fName + " " + UserLoginInfo.lName);
            bank1 = (TextView) view.findViewById(R.id.bank1);
            bank1.setText("Bank: $" + weeklyMatch.get(0));
            user2 = (TextView) view.findViewById(R.id.user2);
            user2.setText(weeklyMatch.get(1) + " " + weeklyMatch.get(2));
            bank2 = (TextView) view.findViewById(R.id.bank2);
            bank2.setText("Bank: $" + weeklyMatch.get(3));

            weeklyMatch = null;

        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e) {
            e.printStackTrace();
        }


    }

    public class MatchDetails extends AsyncTask<Void, Void, List<String>> {
        List<String> weeklyMatch;
        Connection conn;
        ResultSet rs,rs1,rs2;
        Statement st,st1,st2;

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                weeklyMatch = new ArrayList<>();
                conn = null;
                conn = ConnectionManager.getConnection();
                rs = null;
                st = null;
                if (conn != null)
                    st = conn.createStatement();
                if (st != null)
                    rs = st.executeQuery("SELECT * FROM L1_STANDINGS WHERE EMAIL = '" + UserLoginInfo.userEmail + "'");
                if (rs != null)
                    while(rs.next()) {
                        weeklyMatch.add(rs.getString("BANK"));
                        UserLoginInfo.currOpp = rs.getInt("CURR_OPP");
                    }
                st1 = null;
                if (conn != null)
                    st1 = conn.createStatement();
                rs1 = null;
                if (st1 != null)
                    rs1 = st1.executeQuery("SELECT * FROM USERINFO WHERE USERID = '" + UserLoginInfo.currOpp + "'");
                if (rs1 != null)
                    while(rs1.next()) {
                        weeklyMatch.add(rs1.getString("FIRSTNAME"));
                        weeklyMatch.add(rs1.getString("LASTNAME"));
                    }
                rs2 = null;
                st2 = null;
                if (conn != null)
                    st2 = conn.createStatement();
                if (st2 != null)
                    rs2 = st2.executeQuery("SELECT * FROM L1_Standings WHERE USERID = '" + UserLoginInfo.currOpp + "'");
                if (rs2 != null)
                    while(rs2.next()) {
                        weeklyMatch.add(rs2.getString("BANK"));
                    }

                return weeklyMatch;

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(rs!=null)
                        rs.close();
                    if(rs1!=null)
                        rs1.close();
                    if(rs2!=null)
                        rs2.close();
                    if(st!=null)
                        st.close();
                    if(st1!=null)
                        st1.close();
                    if(st2!=null)
                        st2.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);

            weeklyMatch = null;





        }
    }
}



