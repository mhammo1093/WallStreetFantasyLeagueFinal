package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AccountFragment extends Fragment {
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_account, container, false);
        return rootview;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // initialize your views

        TextView mTextView1 = (TextView)view.findViewById(R.id.user_name);
        mTextView1.setText(UserLoginInfo.fName + " " + UserLoginInfo.lName);


        TextView mTextView2 = (TextView)view.findViewById(R.id.user_email);
        mTextView2.setText(UserLoginInfo.userEmail);


    }

}
