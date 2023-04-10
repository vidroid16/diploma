package com.shalya.diploma.services;

import com.shalya.diploma.dto.GetUserShopListDto;
import com.shalya.diploma.dto.GetUserShopListsDto;
import com.shalya.diploma.dto.GoodInShopListDto;
import com.shalya.diploma.dto.requests.CreateKnapsackRequest;
import com.shalya.diploma.dto.requests.CreateListRequest;
import com.shalya.diploma.dto.requests.UpdateShopListRequest;
import com.shalya.diploma.exceptions.NoPermissionException;
import com.shalya.diploma.exceptions.ObjectNotFoundException;
import com.shalya.diploma.knapsack.KnapsackGood;
import com.shalya.diploma.knapsack.KnapsackSolver;
import com.shalya.diploma.knapsack.Packable;
import com.shalya.diploma.mappers.GoodMapper;
import com.shalya.diploma.mappers.ListMapper;
import com.shalya.diploma.models.*;
import com.shalya.diploma.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopListService {

    private final UserRepository userRepository;
    private final ListRepository listRepository;
    private final ListsGoodsRepository listsGoodsRepository;
    private final GoodRepository goodRepository;

    private final CategoryRepository categoryRepository;

    public List<GetUserShopListsDto> getUserShopLists(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);

        List<ShopList> shopLists = listRepository.getAllByUser(user).orElse(null);
        List<GetUserShopListsDto> resp = new ArrayList<>();
        shopLists.forEach(p->{
            resp.add(new GetUserShopListsDto(p.getId(), p.getName()));
        });
        return resp;
    }

    public GetUserShopListDto getUserShopList(Long id) throws NoPermissionException, ObjectNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);

        ShopList shopList = listRepository.getById(id).orElse(null);
        if (shopList == null)
            throw new ObjectNotFoundException("Cписка не существует!");
        if (shopList.getUser().getId() != user.getId())
            throw new NoPermissionException(user.getLogin());

        List<ListsGoods> listsGoods = listsGoodsRepository.getAllByShopList(shopList);
        List<GoodInShopListDto> goodsDtos = new ArrayList<>();
        listsGoods.forEach(p->{
            goodsDtos.add(GoodMapper.listsGoodsToGoodInShopListDto(p));
        });

        GetUserShopListDto getUserShopListDto = new GetUserShopListDto();
        getUserShopListDto.setGoods(goodsDtos);
        getUserShopListDto.setName(shopList.getName());
        getUserShopListDto.setBudget(shopList.getBudget());
        getUserShopListDto.setId(shopList.getId());
        getUserShopListDto.setTotalPrice(goodsDtos.stream().mapToDouble(GoodInShopListDto::getPrice).sum());

        return getUserShopListDto;
    }

    public void createShopList(CreateListRequest dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        ShopList shopList = new ShopList();
        shopList.setName(dto.getName());
        shopList.setBudget(dto.getBudget());
        shopList.setUser(user);
        listRepository.save(shopList);
    }
    public void addGoodInShopList(Long listId, Long goodId) throws NoPermissionException, ObjectNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);

        ShopList shopList = listRepository.getById(listId).orElse(null);
        if (shopList == null)
            throw new ObjectNotFoundException("Списка не существует!");
        if (shopList.getUser().getId() != user.getId())
            throw new NoPermissionException(user.getLogin());

        Good good = goodRepository.getById(goodId).orElse(null);
        if(good == null)
            throw new ObjectNotFoundException("Такого товара не существует");

        ListsGoods listsGoods = new ListsGoods();
        listsGoods.setGood(good);
        listsGoods.setShopList(shopList);
        listsGoods.setIsChecked(false);
        listsGoodsRepository.save(listsGoods);
    }
    public void deleteGoodInShopList(Long listId, Long goodId) throws NoPermissionException, ObjectNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);

        ShopList shopList = listRepository.getById(listId).orElse(null);
        if (shopList == null)
            throw new ObjectNotFoundException("Списка не существует!");
        if (shopList.getUser().getId() != user.getId())
            throw new NoPermissionException(user.getLogin());

        Good good = goodRepository.getById(goodId).orElse(null);
        if(good == null)
            throw new ObjectNotFoundException("Такого товара не существует");

        var goods = listsGoodsRepository.getByGoodAndShopList(good,shopList);
        listsGoodsRepository.deleteById(goods.get(0).getId());
    }
    public void setGoodAsChecked(Long listId, Long goodId) throws NoPermissionException, ObjectNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);

        ShopList shopList = listRepository.getById(listId).orElse(null);
        if (shopList == null)
            throw new ObjectNotFoundException("Списка не существует!");
        if (shopList.getUser().getId() != user.getId())
            throw new NoPermissionException(user.getLogin());

        Good good = goodRepository.getById(goodId).orElse(null);
        if(good != null)
            throw new ObjectNotFoundException("Такого товара не существует");

        var listsGoods = listsGoodsRepository.getByGoodIdAndShopListId(goodId,listId);
        if(listsGoods!=null){
            var uncheckedGoods = listsGoods.stream().filter(p-> !p.getIsChecked()).toList();
            if (uncheckedGoods.size()>0) {
                var goodToUpdate = uncheckedGoods.get(0);
                goodToUpdate.setIsChecked(true);
                listsGoodsRepository.save(goodToUpdate);
            }
        }

    }
    public void updateShopList(Long listId, UpdateShopListRequest dto) throws NoPermissionException, ObjectNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);

        ShopList shopList = listRepository.getById(listId).orElse(null);
        if (shopList == null)
            throw new ObjectNotFoundException("Списка не существует!");
        if (shopList.getUser().getId() != user.getId())
            throw new NoPermissionException(user.getLogin());

        ShopList updatedShopList = ListMapper.updateShopListRequestToShopList(dto);
        listRepository.save(updatedShopList);
    }
    public GetUserShopListDto createKnapsack(CreateKnapsackRequest request) throws ObjectNotFoundException, NoPermissionException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);

        List<List<Packable>> itemsInKnapsack = new ArrayList<>();
        request.getCategories().forEach(p->{
            Category category = categoryRepository.getById(p);
            if (category!=null){
                List<Good> goods = new ArrayList<>(goodRepository.findAllByCategory(category));
                List<Packable> gs = new ArrayList<>();
                goods.forEach(g->{
                    gs.add(new KnapsackGood(g));
                });
                itemsInKnapsack.add(gs);
            }
        });

        KnapsackSolver solver = new KnapsackSolver(itemsInKnapsack, request.getBudget());
        solver.solve();
        var knapsack = solver.restoreItems();
        for (int i = 2; i <= 5; i++) {
            if (knapsack.size()<request.getCategories().size())
                knapsack = solver.restoreItemsTop(i);
            else
                break;
        }

        List<Good> goods = new ArrayList<>();
        knapsack.forEach(p->Collections.fill(goods,goodRepository.getById(p.getId()).orElse(null)));

        ShopList shopList = new ShopList();
        shopList.setBudget(request.getBudget());
        shopList.setName(request.getName());
        shopList.setTotalPrice(goods.stream().mapToDouble(Good::getPrice).sum());
        shopList.setUser(user);
        ShopList createdShopList = shopList = listRepository.save(shopList);
        knapsack.forEach(p-> {
            try {
                addGoodInShopList(createdShopList.getId(),p.getId());
            } catch (NoPermissionException | ObjectNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return getUserShopList(createdShopList.getId());
    }

}
