package com.shalya.diploma.services;

import com.shalya.diploma.dto.GetUserShopListDto;
import com.shalya.diploma.dto.GetUserShopListsDto;
import com.shalya.diploma.dto.GoodInShopListDto;
import com.shalya.diploma.dto.requests.CreateListRequest;
import com.shalya.diploma.dto.requests.UpdateShopListRequest;
import com.shalya.diploma.exceptions.NoPermissionException;
import com.shalya.diploma.exceptions.ObjectNotFoundException;
import com.shalya.diploma.mappers.GoodMapper;
import com.shalya.diploma.mappers.ListMapper;
import com.shalya.diploma.models.Good;
import com.shalya.diploma.models.ListsGoods;
import com.shalya.diploma.models.ShopList;
import com.shalya.diploma.models.User;
import com.shalya.diploma.repositories.GoodRepository;
import com.shalya.diploma.repositories.ListRepository;
import com.shalya.diploma.repositories.ListsGoodsRepository;
import com.shalya.diploma.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopListService {

    private final UserRepository userRepository;
    private final ListRepository listRepository;
    private final ListsGoodsRepository listsGoodsRepository;
    private final GoodRepository goodRepository;

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
        if(good != null)
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
        if(good != null)
            throw new ObjectNotFoundException("Такого товара не существует");

        listsGoodsRepository.deleteByGoodIdAndShopListId(goodId,listId);
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

        ListsGoods listsGoods = listsGoodsRepository.getByGoodIdAndShopListId(goodId,listId).orElse(null);
        if(listsGoods!=null){
            listsGoods.setIsChecked(true);
            listsGoodsRepository.save(listsGoods);
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
}
