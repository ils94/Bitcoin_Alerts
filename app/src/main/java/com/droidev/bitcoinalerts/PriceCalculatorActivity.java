package com.droidev.bitcoinalerts;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class PriceCalculatorActivity extends AppCompatActivity {

    EditText BTCamount, BRLamount, USDamount;
    TextView priceBRL, priceUSD, priceBRLBTC, priceUSDBTC;

    Button cal1, cal2, cal3;

    CheckBox sats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_calculator);

        setTitle("Calculator");

        BTCamount = findViewById(R.id.BTCamount);
        BRLamount = findViewById(R.id.BRLamount);
        USDamount = findViewById(R.id.USDamount);

        priceBRL = findViewById(R.id.priceBRL);
        priceUSD = findViewById(R.id.priceUSD);
        priceBRLBTC = findViewById(R.id.priceBRLBTC);
        priceUSDBTC = findViewById(R.id.priceUSDBTC);

        cal1 = findViewById(R.id.cal1);
        cal2 = findViewById(R.id.cal2);
        cal3 = findViewById(R.id.cal3);

        sats = findViewById(R.id.SATs);

        cal1.setOnClickListener(v -> priceBTC());

        cal2.setOnClickListener(v -> valueBRL());

        cal3.setOnClickListener(v -> valueUSD());
    }

    public void priceBTC() {

        if (BTCamount.getText().toString().isEmpty()) {

            Toast.makeText(this, "Field cannot be empty.", Toast.LENGTH_SHORT).show();

            return;
        }

        @SuppressLint("SetTextI18n") BitcoinPriceFetcher fetcher = new BitcoinPriceFetcher(bitcoinPrice -> {
            if (bitcoinPrice != null) {

                try {

                    double usdValue = bitcoinPrice.getUsdValue();
                    double brlValue = bitcoinPrice.getBrlValue();

                    double result1;
                    double result2;

                    if (sats.isChecked()) {

                        double btcValue = Double.parseDouble(BTCamount.getText().toString()) / 100000000.0;

                        result1 = btcValue * brlValue;
                        result2 = btcValue * usdValue;
                    } else {

                        result1 = Double.parseDouble(BTCamount.getText().toString()) * brlValue;
                        result2 = Double.parseDouble(BTCamount.getText().toString()) * usdValue;
                    }

                    NumberFormat currencyFormatBRL = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                    NumberFormat currencyFormatUSD = NumberFormat.getCurrencyInstance(new Locale("en", "US"));

                    priceBRL.setText("BRL amount: " + currencyFormatBRL.format(result1));
                    priceUSD.setText("USD amount: " + currencyFormatUSD.format(result2));

                } catch (Exception e) {

                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        fetcher.execute();
    }

    public void valueUSD() {

        if (USDamount.getText().toString().isEmpty()) {

            Toast.makeText(this, "Field cannot be empty.", Toast.LENGTH_SHORT).show();

            return;
        }

        @SuppressLint("SetTextI18n") BitcoinPriceFetcher fetcher = new BitcoinPriceFetcher(bitcoinPrice -> {
            if (bitcoinPrice != null) {

                try {

                    DecimalFormat df = new DecimalFormat("#.########");

                    double usdValue = bitcoinPrice.getUsdValue();

                    double amount = Double.parseDouble(USDamount.getText().toString());

                    double amountBTC = amount / usdValue;

                    priceUSDBTC.setText("BTC amount: " + df.format(amountBTC).replace(",", "."));

                } catch (Exception e) {

                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        fetcher.execute();
    }

    public void valueBRL() {

        if (BRLamount.getText().toString().isEmpty()) {

            Toast.makeText(this, "Field cannot be empty.", Toast.LENGTH_SHORT).show();

            return;
        }

        @SuppressLint("SetTextI18n") BitcoinPriceFetcher fetcher = new BitcoinPriceFetcher(bitcoinPrice -> {
            if (bitcoinPrice != null) {

                try {

                    DecimalFormat df = new DecimalFormat("#.########");

                    double brlValue = bitcoinPrice.getBrlValue();

                    double amount = Double.parseDouble(BRLamount.getText().toString());

                    double amountBTC = amount / brlValue;

                    priceBRLBTC.setText("BTC amount: " + df.format(amountBTC).replace(",", "."));

                } catch (Exception e) {

                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        fetcher.execute();
    }
}