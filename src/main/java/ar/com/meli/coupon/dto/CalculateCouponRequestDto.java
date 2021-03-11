package ar.com.meli.coupon.dto;

import java.util.List;

public class CalculateCouponRequestDto {

    private List<String> item_ids;
    private float amount;

    public List<String> getItem_ids() {
        return item_ids;
    }

    public void setItem_ids(List<String> item_ids) {
        this.item_ids = item_ids;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
