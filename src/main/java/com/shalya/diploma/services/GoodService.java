package com.shalya.diploma.services;

import com.shalya.diploma.dto.GoodDto;
import com.shalya.diploma.mappers.GoodMapper;
import com.shalya.diploma.models.Category;
import com.shalya.diploma.models.Good;
import com.shalya.diploma.models.User;
import com.shalya.diploma.repositories.CategoryRepository;
import com.shalya.diploma.repositories.GoodRepository;
import com.shalya.diploma.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodService {
    private final GoodRepository goodRepository;
    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    public GoodDto createUserGood(GoodDto dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        Good good = GoodMapper.goodDtoToGood(dto);
        good.setUser(user);
        goodRepository.save(good);
        return dto;
    }
    public void deleteUserGood(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        Good good = goodRepository.getById(id).orElse(null);
        if (good!=null && good.getUser().equals(user))
                goodRepository.delete(good);
    }
    public List<GoodDto> getAllUserGoods(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        List<Good> goods = goodRepository.findAllByUser(user);
        return goods.stream().map(GoodMapper::goodToGoodDto).collect(Collectors.toList());
    }
    public List<GoodDto> getAllCategoryGoods(Long catId){
        Category category = categoryRepository.getById(catId);
        if (category!=null){
            List<Good> goods = goodRepository.findAllByCategory(category);
            return goods.stream().map(GoodMapper::goodToGoodDto).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
