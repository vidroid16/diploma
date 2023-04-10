package com.shalya.diploma.api;

import com.shalya.diploma.dto.GoodDto;
import com.shalya.diploma.dto.RatingDto;
import com.shalya.diploma.dto.requests.RateRequest;
import com.shalya.diploma.services.GoodService;
import com.shalya.diploma.services.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${baseUrl}/goods")
@Slf4j
@RequiredArgsConstructor
public class GoodController {
    private final GoodService goodService;
    private final RatingService ratingService;

    @GetMapping("/cat/{id}")
    public List<GoodDto> getAllGoodsFromCategory(@PathVariable Long id){
        return goodService.getAllCategoryGoods(id);
    }
    @GetMapping
    public List<GoodDto> getAllUserGoods(){
        return goodService.getAllUserGoods();
    }
    @PostMapping("/{id}")
    public ResponseEntity rateGood(@PathVariable Long id,@RequestBody RateRequest request){
        ratingService.rateGood(id,request.getMark());
        return ResponseEntity.ok().build();
    }
}
