package com.shalya.diploma.api;

import com.shalya.diploma.knapsack.Item;
import com.shalya.diploma.knapsack.KnapsackSolver;
import com.shalya.diploma.knapsack.Packable;
import com.shalya.diploma.models.Good;
import com.shalya.diploma.repositories.CategoryRepository;
import com.shalya.diploma.repositories.GoodRepository;
import com.shalya.diploma.repositories.HelloEntityRepository;
import com.shalya.diploma.repositories.UserRepository;
import com.shalya.diploma.security.JwtUtils;
import com.shalya.diploma.services.UpdateGoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${baseUrl}/test")
@Slf4j
@RequiredArgsConstructor
public class TestController {
    private final HelloEntityRepository helloEntityRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final GoodRepository goodRepository;
    private final CategoryRepository categoryRepository;
    private final UpdateGoodsService updateGoodsService;

    @PostMapping("/t/{cat}/{name}")
    public String test(@PathVariable("cat") Long id, @PathVariable("name") String name){
        updateGoodsService.updateAllGoods();
        return "D";
    }
    @PostMapping("/t")
    public String test2(){
        List<Packable> cat1 = new ArrayList<>();
        cat1.add(new Item(1L,1,1));
        cat1.add(new Item(2L,4,4));
//        cat1.add(new Item(2L,3,2));
//        cat1.add(new Item(3L,3,2));
//        cat1.add(new Item(4L,3,2));

        List<Packable> cat2 = new ArrayList<>();
        cat2.add(new Item(5L,2,1));
        cat2.add(new Item(6L,2,4));
        cat2.add(new Item(7L,11,49));
//        cat2.add(new Item(8L,3,5));
//        cat2.add(new Item(9L,3,2));

        List<Packable> cat3 = new ArrayList<>();
        cat3.add(new Item(10L,1,1));
        cat3.add(new Item(11L,2,1));
        cat3.add(new Item(12L,3,4));
        cat3.add(new Item(13L,3,5));
        cat3.add(new Item(14L,3,2));

        List<Packable> cat4 = new ArrayList<>();
        cat4.add(new Item(15L,28,1));
        cat4.add(new Item(16L,28,1));
        cat4.add(new Item(17L,38,4));
        cat4.add(new Item(18L,38,5));
        cat4.add(new Item(19L,1,3));

        ArrayList<List<Packable>> items = new ArrayList<>();

        items.add(cat1);
        items.add(cat2);
        items.add(cat3);
        items.add(cat4);

        KnapsackSolver solver = new KnapsackSolver(items,12);
        solver.setItems(items);
        solver.setMaxWeight(12);
        solver.solve();
        List<Packable> res = solver.restoreItems();
        int counter = 2;
        while (res.size()!=items.size()){
            res = solver.restoreItemsTop(counter);
            if (counter>6)
                break;
            counter++;
        }
        return "!";
    }
    @PostMapping("/t3")
    public List<Packable> test3(){
        var categories = categoryRepository.findAll();
        List<Packable> cat1 = new ArrayList<>(goodRepository.findAllByCategory(categories.get(0)));
        List<Packable> cat2 = new ArrayList<>(goodRepository.findAllByCategory(categories.get(1)));
        List<Packable> cat3 = new ArrayList<>(goodRepository.findAllByCategory(categories.get(2)));
        List<Packable> cat4 = new ArrayList<>(goodRepository.findAllByCategory(categories.get(3)));

        List<List<Packable>> items = new ArrayList<>();
        items.add(cat1);
        items.add(cat2);
        items.add(cat3);
        items.add(cat4);

        KnapsackSolver solver = new KnapsackSolver(items,400);
        solver.solve();
        List<Packable> res = solver.restoreItems();
        return res;
    }
}
