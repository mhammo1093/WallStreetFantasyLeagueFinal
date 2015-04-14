package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;


public class CurrentMarketTrendsFragment extends Fragment {
    public static String spPriceS;
    public static String spPercentS;
    public static String nasdaqPriceS;
    public static String nasdaqPercentS;

    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_current_market_trends, container, false);
        return rootview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do your variables initialisations here except Views!!!
        String spSymbol = "^GSPC";
        YahooFinanceTask one = new YahooFinanceTask(spSymbol);


    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView spPrice = (TextView) view.findViewById(R.id.sp_price);
        spPrice.setText(spPriceS);
        TextView spPercent = (TextView) view.findViewById(R.id.sp_percent);
        spPercent.setText(spPercentS);

        /*TextView nasdaqPrice = (TextView) view.findViewById(R.id.nasdaq_price);
        nasdaqPrice.setText(nasdaqPriceS);
        TextView nasdaqPercent = (TextView) view.findViewById(R.id.nasdaq_percent);
        nasdaqPercent.setText(nasdaqPercentS);*/

    }

    public class YahooFinanceTask extends AsyncTask<Void, Void, Void> {
        public String mSymbol;

        YahooFinanceTask(String symbol) {
            mSymbol = symbol;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Stock stockOne = YahooFinance.get(mSymbol);
                BigDecimal spPriceBD = stockOne.getQuote().getPrice();
                CurrentMarketTrendsFragment.spPriceS = spPriceBD.toString();
                BigDecimal spPercentBD = stockOne.getQuote().getChangeInPercent();
                CurrentMarketTrendsFragment.spPercentS = spPercentBD.toString();

                /*yahoofinance.Stock stockTwo = YahooFinance.get("^IXIC");
                BigDecimal nasdaqPriceBD = stockTwo.getQuote().getPrice();
                HomeFragment.nasdaqPriceS = nasdaqPriceBD.toString();
                BigDecimal nasdaqPercentBD = stockTwo.getQuote().getChangeInPercent();
                HomeFragment.nasdaqPercentS = nasdaqPercentBD.toString();*/
            } catch(Exception e) {
                return null;
            }
            return null;
        }



    }









}
