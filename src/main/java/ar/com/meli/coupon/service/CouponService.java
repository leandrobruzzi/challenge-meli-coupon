package ar.com.meli.coupon.service;

import ar.com.meli.coupon.dto.CalculateCouponRequestDto;
import ar.com.meli.coupon.dto.CalculateCouponResponseDto;
import ar.com.meli.coupon.dto.CombinationDto;
import ar.com.meli.coupon.exceptions.InsufficientAmountException;

import java.util.List;
import java.util.Map;

public interface CouponService {

    CalculateCouponResponseDto calculateProducts(CalculateCouponRequestDto calculateCouponRequestDto) throws InsufficientAmountException;

    List<String> stressTest(Map<String, Float> items, Float amount, CombinationDto maxCombination) throws InsufficientAmountException;
}
