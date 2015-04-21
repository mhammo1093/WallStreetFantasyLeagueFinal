package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CurrentMarketTrendsFragment extends Fragment {
    TextView spPrice;
    TextView spPercent;
    TextView nasdaqPrice;
    TextView nasdaqPercent;
    TextView nyPrice;
    TextView nyPercent;

    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_current_market_trends, container, false);
        return rootview;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spPrice = (TextView) view.findViewById(R.id.sp_price);
        spPercent = (TextView) view.findViewById(R.id.sp_percent);

        nasdaqPrice = (TextView) view.findViewById(R.id.nasdaq_price);
        nasdaqPercent = (TextView) view.findViewById(R.id.nasdaq_percent);

        nyPrice = (TextView) view.findViewById(R.id.ny_price);
        nyPercent = (TextView) view.findViewById(R.id.ny_percent);

        try {
            List<String> resultsS = new Yahoo().execute("^GSPC").get();
            setResult(spPrice, spPercent, resultsS.get(0), resultsS.get(1), resultsS.get(2));
            List<String> resultsN = new Yahoo().execute("^IXIC").get();
            setResult(nasdaqPrice, nasdaqPercent, resultsN.get(0), resultsN.get(1), resultsN.get(2));
            List<String> resultsD = new Yahoo().execute("^NYA").get();
            setResult(nyPrice, nyPercent, resultsD.get(0), resultsD.get(1), resultsD.get(2));

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setResult(TextView textViewOne, TextView textViewTwo, String fstockSymbol, String stockPrice, String fstockChangePercentage) {
        textViewOne.setText("$" + stockPrice);
        textViewTwo.setText(fstockChangePercentage + "%");
        char c = fstockChangePercentage.charAt(0);
        if(c=='+') {
            textViewTwo.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        }
        else if(c=='-') {
            textViewTwo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }
}

    class Yahoo extends AsyncTask<String, Void, List<String>> {

            protected List<String> doInBackground(String... symbols) {
                try {
                    URL url;

                    try {
                        url = new URL(
                                "http://download.finance.yahoo.com/d/quotes.csv?s="
                                        + symbols[0] + "&f=nl1p2");

                        InputStream stream = url.openStream();

                        BufferedInputStream bis = new BufferedInputStream(stream);
                        ByteArrayBuffer baf = new ByteArrayBuffer(50);

                        int current = 0;
                        while ((current = bis.read()) != -1) {
                            baf.append((byte) current);
                        }

                        String stockTxt = new String(baf.toByteArray());

                        String[] tokens = stockTxt.split(",");

                        String stockSymbol = tokens[0];
                        String stockPrice = tokens[1];
                        String stockChangePercentage = tokens[2];

                        String fstockSymbol = stockSymbol.substring(1,
                                stockSymbol.length() - 1);
                        String fstockChangePercentage = stockChangePercentage
                                .substring(1, stockChangePercentage.length() - 3);

                        List<String> results = new ArrayList<String>();
                        results.add(fstockSymbol);
                        results.add(stockPrice);
                        results.add(fstockChangePercentage);

                        return results;


                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

