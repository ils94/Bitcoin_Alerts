package com.droidev.bitcoinalerts;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_NOTIFICATIONS_PERMISSION_FEE = 1;
    private static final int REQUEST_NOTIFICATIONS_PERMISSION_PRICE = 2;


    TextView btcBRL, btcUSD, fastestFee, halfHourFee, hourFee, dateTimePrice, dateTimeFee;

    Menu menuItem;

    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tinyDB = new TinyDB(this);

        btcBRL = findViewById(R.id.btcBRL);
        btcUSD = findViewById(R.id.btcUSD);

        fastestFee = findViewById(R.id.fastestFee);
        halfHourFee = findViewById(R.id.halfHourFee);
        hourFee = findViewById(R.id.hourFee);

        dateTimePrice = findViewById(R.id.dateTimePrice);
        dateTimeFee = findViewById(R.id.dateTimeFee);

        loadSavedData();
    }


    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.fetchPrices) {

            fetchPrice();

        }

        if (item.getItemId() == R.id.calculator) {

            Intent intent = new Intent(MainActivity.this, PriceCalculatorActivity.class);
            startActivity(intent);

        }

        if (item.getItemId() == R.id.memPool) {

            String url = "https://mempool.space/pt/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);

        }

        if (item.getItemId() == R.id.feeNotificationsON) {

            requestNotificationsPermission(REQUEST_NOTIFICATIONS_PERMISSION_FEE);

        }

        if (item.getItemId() == R.id.feeNotificationsOFF) {

            Toast.makeText(this, "Notificações de Taxa desativadas.", Toast.LENGTH_SHORT).show();

            feeCancelAlarm();

        }

        if (item.getItemId() == R.id.priceNotificationsON) {

            requestNotificationsPermission(REQUEST_NOTIFICATIONS_PERMISSION_PRICE);

        }

        if (item.getItemId() == R.id.priceNotificationsOFF) {

            Toast.makeText(this, "Notificações de Preço desativadas.", Toast.LENGTH_SHORT).show();

            priceCancelAlarm();

        }

        if (item.getItemId() == R.id.allNotificationsOFF) {

            Toast.makeText(this, "Todas as notificações foram desativadas.", Toast.LENGTH_SHORT).show();

            priceCancelAlarm();
            feeCancelAlarm();

        }

        return super.onOptionsItemSelected(item);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        menuItem = menu;

        return super.onCreateOptionsMenu(menu);
    }


    public void fetchPrice() {

        @SuppressLint("SetTextI18n") BitcoinPriceFetcher fetcher = new BitcoinPriceFetcher(bitcoinPrice -> {
            if (bitcoinPrice != null) {

                try {

                    double usdValue = bitcoinPrice.getUsdValue();
                    double brlValue = bitcoinPrice.getBrlValue();

                    NumberFormat currencyFormatBRL = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                    NumberFormat currencyFormatUSD = NumberFormat.getCurrencyInstance(new Locale("en", "US"));

                    btcBRL.setText("Preço do BTC em BRL: " + currencyFormatBRL.format(brlValue));
                    btcUSD.setText("Preço do BTC em USD: " + currencyFormatUSD.format(usdValue));

                    tinyDB.putString("btcbrl", btcBRL.getText().toString());
                    tinyDB.putString("btcusd", btcUSD.getText().toString());

                    getFees();

                    Toast.makeText(this, "Valores atualizados.", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        fetcher.execute();
    }


    @SuppressLint("SetTextI18n")
    public void saveTime() {

        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm:ss", Locale.getDefault());

        String formattedDate = dateFormat.format(currentDate);

        dateTimePrice.setText("Preço atualizado em: " + formattedDate);
        dateTimeFee.setText("Taxa atualizada em: " + formattedDate);

        tinyDB.putString("datetimeprice", dateTimePrice.getText().toString());
        tinyDB.putString("datetimefee", dateTimeFee.getText().toString());
    }

    @SuppressLint("SetTextI18n")
    public void getFees() {

        try {

            BitcoinFeeFetcher feeFetcher = new BitcoinFeeFetcher();
            feeFetcher.execute();

            int[] fees = feeFetcher.get();

            if (fees != null) {
                int fastestfee = fees[0];
                int halfhourfee = fees[1];
                int hourfee = fees[2];

                fastestFee.setText("Taxa de Prioridade Alta: " + fastestfee + " sat/vB");
                halfHourFee.setText("Taxa de Prioridade Média: " + halfhourfee + " sat/vB");
                hourFee.setText("Taxa de Prioridade Baixa: " + hourfee + " sat/vB");

                tinyDB.putString("fastestfee", fastestFee.getText().toString());
                tinyDB.putString("halfhourfee", halfHourFee.getText().toString());
                tinyDB.putString("hourfee", hourFee.getText().toString());

                saveTime();
            }

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public void loadSavedData() {

        String btcbrl = tinyDB.getString("btcbrl");

        if (!btcbrl.isEmpty()) {

            btcBRL.setText(btcbrl);
        }

        String btcusd = tinyDB.getString("btcusd");

        if (!btcusd.isEmpty()) {

            btcUSD.setText(btcusd);
        }


        String datetimePrice = tinyDB.getString("datetimeprice");

        if (!datetimePrice.isEmpty()) {

            dateTimePrice.setText(datetimePrice);
        }

        String datetimeFee = tinyDB.getString("datetimefee");

        if (!datetimeFee.isEmpty()) {

            dateTimeFee.setText(datetimeFee);
        }

        String fastestfee = tinyDB.getString("fastestfee");

        if (!fastestfee.isEmpty()) {

            fastestFee.setText(fastestfee);
        }

        String halfhourfee = tinyDB.getString("halfhourfee");

        if (!halfhourfee.isEmpty()) {

            halfHourFee.setText(halfhourfee);
        }

        String hourfee = tinyDB.getString("hourfee");

        if (!hourfee.isEmpty()) {

            hourFee.setText(hourfee);
        }
    }


    public void feeStartAlarmPrompt() {

        EditText value = new EditText(this);
        value.setInputType(InputType.TYPE_CLASS_NUMBER);
        value.setMaxLines(1);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(value);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Alerta de Taxas")
                .setMessage("Insira o valor da taxa de transação em Sats. Quando a taxa de transação chegar nesse valor ou estiver menor, você receberá uma notificação.")
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
                .setView(lay)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.setOnClickListener(v -> {

            String txFee = value.getText().toString();

            if (!txFee.isEmpty()) {

                dialog.dismiss();

                tinyDB.putInt("altaprioridade", Integer.parseInt(txFee));

                Toast.makeText(this, "Notificações ativadas.", Toast.LENGTH_SHORT).show();

                feeStartAlarm();

            } else {

                Toast.makeText(this, "É necessário inserir um valor.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void feeStartAlarm() {

        feeCancelAlarm();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, FeesAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        long interval = 60 * 1000;
        long startTime = System.currentTimeMillis();
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, startTime, interval, pendingIntent);
    }

    private void feeCancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, FeesAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        alarmManager.cancel(pendingIntent);
    }

    public void priceStartAlarmPrompt() {

        EditText value = new EditText(this);
        value.setInputType(InputType.TYPE_CLASS_NUMBER);
        value.setMaxLines(1);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(value);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Alerta de Preço")
                .setMessage("Insira o tempo em minutos para receber notificações sobre o preço do Bitcoin.")
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
                .setView(lay)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.setOnClickListener(v -> {

            String time = value.getText().toString();

            if (!time.isEmpty()) {

                dialog.dismiss();

                Toast.makeText(this, "Notificações ativadas.", Toast.LENGTH_SHORT).show();

                priceStartAlarm(Integer.parseInt(time));

            } else {

                Toast.makeText(this, "É necessário inserir um valor.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void priceStartAlarm(int time) {

        priceCancelAlarm();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, PriceAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        long interval = (long) time * 60 * 1000;
        long startTime = System.currentTimeMillis();
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, startTime, interval, pendingIntent);
    }

    private void priceCancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, PriceAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        alarmManager.cancel(pendingIntent);
    }

    @SuppressLint("InlinedApi")
    private void requestNotificationsPermission(int permissionRequestCode) {

        if (permissionRequestCode == REQUEST_NOTIFICATIONS_PERMISSION_FEE) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATIONS_PERMISSION_FEE);
            } else {

                feeStartAlarmPrompt();
            }
        } else if (permissionRequestCode == REQUEST_NOTIFICATIONS_PERMISSION_PRICE) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATIONS_PERMISSION_PRICE);
            } else {

                priceStartAlarmPrompt();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATIONS_PERMISSION_FEE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                feeStartAlarmPrompt();

            } else {

                Toast.makeText(this, "É necessário conceder permissões para notificações.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_NOTIFICATIONS_PERMISSION_PRICE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                priceStartAlarmPrompt();

            } else {

                Toast.makeText(this, "É necessário conceder permissões para notificações.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}