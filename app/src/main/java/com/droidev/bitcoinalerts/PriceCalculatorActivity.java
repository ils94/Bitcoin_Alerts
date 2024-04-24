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

    EditText BTCQuantidade, BRLQuantidade, USDQuantidade;
    TextView precoBRL, precoUSD, valorBRLBTC, valorUSDBTC;

    Button calcular1, calcular2, calcular3;

    CheckBox sats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_calculator);

        setTitle("Calcular Valores");

        BTCQuantidade = findViewById(R.id.BTCQuantidade);
        BRLQuantidade = findViewById(R.id.BRLQuantidade);
        USDQuantidade = findViewById(R.id.USDQuantidade);

        precoBRL = findViewById(R.id.precoBRL);
        precoUSD = findViewById(R.id.precoUSD);
        valorBRLBTC = findViewById(R.id.valorBRLBTC);
        valorUSDBTC = findViewById(R.id.valorUSDBTC);

        calcular1 = findViewById(R.id.calcular1);
        calcular2 = findViewById(R.id.calcular2);
        calcular3 = findViewById(R.id.calcular3);

        sats = findViewById(R.id.SATs);

        calcular1.setOnClickListener(v -> priceBTC());

        calcular2.setOnClickListener(v -> valueBRL());

        calcular3.setOnClickListener(v -> valueUSD());
    }

    public void priceBTC() {

        if (BTCQuantidade.getText().toString().isEmpty()) {

            Toast.makeText(this, "Erro, campo vazio.", Toast.LENGTH_SHORT).show();

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

                        double btcValue = Double.parseDouble(BTCQuantidade.getText().toString()) / 100000000.0;

                        result1 = btcValue * brlValue;
                        result2 = btcValue * usdValue;
                    } else {

                        result1 = Double.parseDouble(BTCQuantidade.getText().toString()) * brlValue;
                        result2 = Double.parseDouble(BTCQuantidade.getText().toString()) * usdValue;
                    }

                    NumberFormat currencyFormatBRL = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                    NumberFormat currencyFormatUSD = NumberFormat.getCurrencyInstance(new Locale("en", "US"));

                    precoBRL.setText("Valor em BRL: " + currencyFormatBRL.format(result1));
                    precoUSD.setText("Valor em USD: " + currencyFormatUSD.format(result2));

                } catch (Exception e) {

                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        fetcher.execute();
    }

    public void valueUSD() {

        if (USDQuantidade.getText().toString().isEmpty()) {

            Toast.makeText(this, "Erro, campo vazio.", Toast.LENGTH_SHORT).show();

            return;
        }

        @SuppressLint("SetTextI18n") BitcoinPriceFetcher fetcher = new BitcoinPriceFetcher(bitcoinPrice -> {
            if (bitcoinPrice != null) {

                try {

                    DecimalFormat df = new DecimalFormat("#.########");

                    double usdValue = bitcoinPrice.getUsdValue();

                    double amount = Double.parseDouble(USDQuantidade.getText().toString());

                    double amountBTC = amount / usdValue;

                    valorUSDBTC.setText("Valor em BTC: " + df.format(amountBTC).replace(",", "."));

                } catch (Exception e) {

                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        fetcher.execute();
    }

    public void valueBRL() {

        if (BRLQuantidade.getText().toString().isEmpty()) {

            Toast.makeText(this, "Erro, campo vazio.", Toast.LENGTH_SHORT).show();

            return;
        }

        @SuppressLint("SetTextI18n") BitcoinPriceFetcher fetcher = new BitcoinPriceFetcher(bitcoinPrice -> {
            if (bitcoinPrice != null) {

                try {

                    DecimalFormat df = new DecimalFormat("#.########");

                    double brlValue = bitcoinPrice.getBrlValue();

                    double amount = Double.parseDouble(BRLQuantidade.getText().toString());

                    double amountBTC = amount / brlValue;

                    valorBRLBTC.setText("Valor em BTC: " + df.format(amountBTC).replace(",", "."));

                } catch (Exception e) {

                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        fetcher.execute();
    }
}