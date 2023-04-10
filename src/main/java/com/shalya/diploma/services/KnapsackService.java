package com.shalya.diploma.services;

import com.shalya.diploma.dto.GetUserShopListDto;
import com.shalya.diploma.dto.requests.CreateKnapsackRequest;
import com.shalya.diploma.knapsack.KnapsackSolver;
import com.shalya.diploma.knapsack.Packable;
import com.shalya.diploma.models.Category;
import com.shalya.diploma.repositories.CategoryRepository;
import com.shalya.diploma.repositories.GoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KnapsackService {
    private final GoodRepository goodRepository;
    private final CategoryRepository categoryRepository;
}
