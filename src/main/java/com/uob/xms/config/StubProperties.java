package com.uob.xms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "stub")
public class StubProperties {

  private Files files = new Files();

  public Files getFiles() {
    return files;
  }

  public void setFiles(Files files) {
    this.files = files;
  }

  public static class Files {

    /*
     * Equity
     */
    private String equityPricingBuy;
    private String equityPricingSell;
    private String equityPricingError;

    private String equityPlaceOrderBuy;
    private String equityPlaceOrderSell;
    private String equityPlaceOrderError;

    /*
     * Bonds
     */
    private String bondPricingBuy;
    private String bondPricingSell;
    private String bondPricingError;

    private String bondPlaceOrderBuy;
    private String bondPlaceOrderSell;
    private String bondPlaceOrderError;

    /*
     * FX
     */
    private String fxpxPricing;
    private String fxforwardPricing;
    private String fxswapPricing;

    // ==========================================
    // Equity Pricing
    // ==========================================

    public String getEquityPricingBuy() {
      return equityPricingBuy;
    }

    public void setEquityPricingBuy(String equityPricingBuy) {
      this.equityPricingBuy = equityPricingBuy;
    }

    public String getEquityPricingSell() {
      return equityPricingSell;
    }

    public void setEquityPricingSell(String equityPricingSell) {
      this.equityPricingSell = equityPricingSell;
    }

    public String getEquityPricingError() {
      return equityPricingError;
    }

    public void setEquityPricingError(String equityPricingError) {
      this.equityPricingError = equityPricingError;
    }

    // ==========================================
    // Equity Place Order
    // ==========================================

    public String getEquityPlaceOrderBuy() {
      return equityPlaceOrderBuy;
    }

    public void setEquityPlaceOrderBuy(String equityPlaceOrderBuy) {
      this.equityPlaceOrderBuy = equityPlaceOrderBuy;
    }

    public String getEquityPlaceOrderSell() {
      return equityPlaceOrderSell;
    }

    public void setEquityPlaceOrderSell(String equityPlaceOrderSell) {
      this.equityPlaceOrderSell = equityPlaceOrderSell;
    }

    public String getEquityPlaceOrderError() {
      return equityPlaceOrderError;
    }

    public void setEquityPlaceOrderError(String equityPlaceOrderError) {
      this.equityPlaceOrderError = equityPlaceOrderError;
    }

    // ==========================================
    // Bond Pricing
    // ==========================================

    public String getBondPricingBuy() {
      return bondPricingBuy;
    }

    public void setBondPricingBuy(String bondPricingBuy) {
      this.bondPricingBuy = bondPricingBuy;
    }

    public String getBondPricingSell() {
      return bondPricingSell;
    }

    public void setBondPricingSell(String bondPricingSell) {
      this.bondPricingSell = bondPricingSell;
    }

    public String getBondPricingError() {
      return bondPricingError;
    }

    public void setBondPricingError(String bondPricingError) {
      this.bondPricingError = bondPricingError;
    }

    // ==========================================
    // Bond Place Order
    // ==========================================

    public String getBondPlaceOrderBuy() {
      return bondPlaceOrderBuy;
    }

    public void setBondPlaceOrderBuy(String bondPlaceOrderBuy) {
      this.bondPlaceOrderBuy = bondPlaceOrderBuy;
    }

    public String getBondPlaceOrderSell() {
      return bondPlaceOrderSell;
    }

    public void setBondPlaceOrderSell(String bondPlaceOrderSell) {
      this.bondPlaceOrderSell = bondPlaceOrderSell;
    }

    public String getBondPlaceOrderError() {
      return bondPlaceOrderError;
    }

    public void setBondPlaceOrderError(String bondPlaceOrderError) {
      this.bondPlaceOrderError = bondPlaceOrderError;
    }

    // ==========================================
    // FX
    // ==========================================

    public String getFxpxPricing() {
      return fxpxPricing;
    }

    public void setFxpxPricing(String fxpxPricing) {
      this.fxpxPricing = fxpxPricing;
    }

    public String getFxforwardPricing() {
      return fxforwardPricing;
    }

    public void setFxforwardPricing(String fxforwardPricing) {
      this.fxforwardPricing = fxforwardPricing;
    }

    public String getFxswapPricing() {
      return fxswapPricing;
    }

    public void setFxswapPricing(String fxswapPricing) {
      this.fxswapPricing = fxswapPricing;
    }
  }
}