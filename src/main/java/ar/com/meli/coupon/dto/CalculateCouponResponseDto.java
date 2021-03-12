package ar.com.meli.coupon.dto;

import java.util.List;

public class CalculateCouponResponseDto {

    private List<String> item_ids;
    private Float total;

    public List<String> getItem_ids() {
        return item_ids;
    }

    public void setItem_ids(List<String> item_ids) {
        this.item_ids = item_ids;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public CalculateCouponResponseDto(List<String> item_ids, Float total) {
        this.item_ids = item_ids;
        this.total = total;
    }
}
