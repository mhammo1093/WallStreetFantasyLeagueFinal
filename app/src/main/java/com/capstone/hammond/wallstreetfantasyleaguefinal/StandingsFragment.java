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


public class StandingsFragment extends Fragment {
    List<Integer> idList = new ArrayList<>();
    List<String> winList = new ArrayList<>();
    List<String> lossList = new ArrayList<>();
    List<String> firstName = new ArrayList<>();
    List<String> lastName = new ArrayList<>();


    int resultID;
    String resultWins;
    String resultLosses;
    String firstNameS;
    String lastNameS;

    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_standings, container, false);
        return rootview;

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            List<List> standingsResult = new Standings().execute().get();

            firstName = standingsResult.get(0);
            lastName = standingsResult.get(1);
            winList = standingsResult.get(2);
            lossList = standingsResult.get(3);

            TextView player1;
            TextView player2;
            TextView player3;
            TextView player4;
            TextView player5;
            TextView player6;
            TextView player1Win;
            TextView player2Win;
            TextView player3Win;
            TextView player4Win;
            TextView player5Win;
            TextView player6Win;
            TextView player1Loss;
            TextView player2Loss;
            TextView player3Loss;
            TextView player4Loss;
            TextView player5Loss;
            TextView player6Loss;

            player1 = (TextView) view.findViewById(R.id.player1);
            player1.setText(firstName.get(0) + " " + lastName.get(0));
            player2 = (TextView) view.findViewById(R.id.player2);
            player2.setText(firstName.get(1) + " " + lastName.get(1));
            player3 = (TextView) view.findViewById(R.id.player3);
            player3.setText(firstName.get(2) + " " + lastName.get(2));
            player4 = (TextView) view.findViewById(R.id.player4);
            player4.setText(firstName.get(3) + " " + lastName.get(3));
            player5 = (TextView) view.findViewById(R.id.player5);
            player5.setText(firstName.get(4) + " " + lastName.get(4));
            player6 = (TextView) view.findViewById(R.id.player6);
            player6.setText(firstName.get(5) + " " + lastName.get(5));

            player1Win = (TextView) view.findViewById(R.id.player1_win);
            player1Win.setText(winList.get(0));
            player2Win = (TextView) view.findViewById(R.id.player2_win);
            player2Win.setText(winList.get(1));
            player3Win = (TextView) view.findViewById(R.id.player3_win);
            player3Win.setText(winList.get(2));
            player4Win = (TextView) view.findViewById(R.id.player4_win);
            player4Win.setText(winList.get(3));
            player5Win = (TextView) view.findViewById(R.id.player5_win);
            player5Win.setText(winList.get(4));
            player6Win = (TextView) view.findViewById(R.id.player6_win);
            player6Win.setText(winList.get(5));

            player1Loss = (TextView) view.findViewById(R.id.player1_loss);
            player1Loss.setText(lossList.get(0));
            player2Loss = (TextView) view.findViewById(R.id.player2_loss);
            player2Loss.setText(lossList.get(1));
            player3Loss = (TextView) view.findViewById(R.id.player3_loss);
            player3Loss.setText(lossList.get(2));
            player4Loss = (TextView) view.findViewById(R.id.player4_loss);
            player4Loss.setText(lossList.get(3));
            player5Loss = (TextView) view.findViewById(R.id.player5_loss);
            player5Loss.setText(lossList.get(4));
            player6Loss = (TextView) view.findViewById(R.id.player6_loss);
            player6Loss.setText(lossList.get(5));

            standingsResult = null;



        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e) {
            e.printStackTrace();
        }




    }

    public class Standings extends AsyncTask<Void, Void, List<List>> {
        List<List> standingsResult;
        ResultSet rs,rs1,rs2,rs3,rs4,rs5,rs6;
        Statement st,st1,st2,st3,st4,st5,st6;
        Connection conn;

        @Override
        protected List<List> doInBackground(Void... params) {

            List<List> standingsResult;

            try {
                conn = null;
                conn = ConnectionManager.getConnection();
                rs = null;
                st = null;
                if (conn != null)
                    st = conn.createStatement();
                if (st != null)
                    rs = st.executeQuery("SELECT * FROM L1_STANDINGS");
                if (rs != null)
                    while(rs.next()) {
                        resultID = rs.getInt("USERID");
                        idList.add(resultID);
                        resultWins = rs.getString("WINS");
                        winList.add(resultWins);
                        resultLosses = rs.getString("LOSSES");
                        lossList.add(resultLosses);
                    }

                st1 = null;
                if (conn != null)
                    st1 = conn.createStatement();
                rs1 = null;
                if (st1 != null)
                    rs1 = st1.executeQuery("SELECT * FROM USERINFO WHERE USERID = " + (idList.get(0)));
                if (rs1 != null)
                    while(rs1.next()) {
                        firstNameS = rs1.getString("FIRSTNAME");
                        firstName.add(firstNameS);
                        lastNameS = rs1.getString("LASTNAME");
                        lastName.add(lastNameS);
                    }
                st2 = null;
                if (conn != null)
                    st2 = conn.createStatement();
                rs2 = null;
                if (st2 != null)
                    rs2 = st2.executeQuery("SELECT * FROM USERINFO WHERE USERID = " + (idList.get(1)));
                if (rs2 != null)
                    while(rs2.next()) {
                        firstNameS = rs2.getString("FIRSTNAME");
                        firstName.add(firstNameS);
                        lastNameS = rs2.getString("LASTNAME");
                        lastName.add(lastNameS);
                    }
                st3 = null;
                if (conn != null)
                    st3 = conn.createStatement();
                rs3 = null;
                if (st3 != null)
                    rs3 = st3.executeQuery("SELECT * FROM USERINFO WHERE USERID = " + (idList.get(2)));
                if (rs3 != null)
                    while(rs3.next()) {
                        firstNameS = rs3.getString("FIRSTNAME");
                        firstName.add(firstNameS);
                        lastNameS = rs3.getString("LASTNAME");
                        lastName.add(lastNameS);
                    }
                st4 = null;
                if (conn != null)
                    st4 = conn.createStatement();
                rs4 = null;
                if (st4 != null)
                    rs4 = st4.executeQuery("SELECT * FROM USERINFO WHERE USERID = " + (idList.get(3)));
                if (rs4 != null)
                    while(rs4.next()) {
                        firstNameS = rs4.getString("FIRSTNAME");
                        firstName.add(firstNameS);
                        lastNameS = rs4.getString("LASTNAME");
                        lastName.add(lastNameS);
                    }
                st5 = null;
                if (conn != null)
                    st5 = conn.createStatement();
                rs5 = null;
                if (st5 != null)
                    rs5 = st5.executeQuery("SELECT * FROM USERINFO WHERE USERID = " + (idList.get(4)));
                if (rs5 != null)
                    while(rs5.next()) {
                        firstNameS = rs5.getString("FIRSTNAME");
                        firstName.add(firstNameS);
                        lastNameS = rs5.getString("LASTNAME");
                        lastName.add(lastNameS);
                    }
                st6 = null;
                if (conn != null)
                    st6 = conn.createStatement();
                rs6 = null;
                if (st6 != null)
                    rs6 = st6.executeQuery("SELECT * FROM USERINFO WHERE USERID = " + (idList.get(5)));
                if (rs6 != null)
                    while(rs6.next()) {
                        firstNameS = rs6.getString("FIRSTNAME");
                        firstName.add(firstNameS);
                        lastNameS = rs6.getString("LASTNAME");
                        lastName.add(lastNameS);
                    }

                standingsResult = new ArrayList<>();
                standingsResult.add(firstName);
                standingsResult.add(lastName);
                standingsResult.add(winList);
                standingsResult.add(lossList);


                return standingsResult;


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
                    if(rs3!=null)
                        rs3.close();
                    if(rs4!=null)
                        rs4.close();
                    if(rs5!=null)
                        rs5.close();
                    if(rs6!=null)
                        rs6.close();
                    if(st!=null)
                        st.close();
                    if(st1!=null)
                        st1.close();
                    if(st2!=null)
                        st2.close();
                    if(st3!=null)
                        st3.close();
                    if(st4!=null)
                        st4.close();
                    if(st5!=null)
                        st5.close();
                    if(st6!=null)
                        st6.close();


                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(List<List> lists) {
            super.onPostExecute(lists);

            standingsResult = null;
        }
    }
}



