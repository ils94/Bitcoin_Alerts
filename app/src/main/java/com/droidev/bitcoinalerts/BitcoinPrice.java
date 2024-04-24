package com.droidev.bitcoinalerts;

public class BitcoinPrice {
    private final double usdValue;
    private final double brlValue;

    public BitcoinPrice(double usdValue, double brlValue) {
        this.usdValue = usdValue;
        this.brlValue = brlValue;
    }

    public double getUsdValue() {
        return usdValue;
    }

    public double getBrlValue() {
        return brlValue;
    }
}
