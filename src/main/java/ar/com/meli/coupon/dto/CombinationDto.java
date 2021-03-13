package ar.com.meli.coupon.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CombinationDto {

    private List<Map.Entry<String, Float>> combination = new ArrayList<>();
    private Float value = 0F;

    public CombinationDto(){

    }
    public CombinationDto(List<Map.Entry<String, Float>> combination) {
        this.combination = combination;
        setValue();
    }

    public List<Map.Entry<String, Float>> getCombination() {
        return combination;
    }

    public void setCombination(List<Map.Entry<String, Float>> combination) {
        this.combination = combination;
        setValue();
    }

    private void setValue() {
        Float aux = 0F;
        for (int i = 0; i<this.combination.size(); i++){
            aux = aux + this.combination.get(i).getValue();
        }
        this.value = aux;
    }

    public Float getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "Combination{" +
                "combination=" + combination +
                ", value=" + value +
                '}';
    }
}
