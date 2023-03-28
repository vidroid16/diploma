package com.shalya.diploma.api;

import com.shalya.diploma.dto.GetUserShopListDto;
import com.shalya.diploma.dto.GetUserShopListsDto;
import com.shalya.diploma.dto.requests.CreateListRequest;
import com.shalya.diploma.exceptions.NoPermissionException;
import com.shalya.diploma.exceptions.ObjectNotFoundException;
import com.shalya.diploma.services.ShopListService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${baseUrl}/shoplist")
public class ShopListController {

    private final ShopListService shopListService;

    @GetMapping
    public ResponseEntity<List<GetUserShopListsDto>> getUserLists(){
        List<GetUserShopListsDto> dto = shopListService.getUserShopLists();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserShopList(@PathVariable("id") Long id){
        try {
            GetUserShopListDto dto = shopListService.getUserShopList(id);
            return ResponseEntity.ok(dto);
        } catch (NoPermissionException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<Object> createShopList(@RequestBody CreateListRequest dto){
        try {
            shopListService.createShopList(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(504).body("Что-то пошло не так!");
        }
    }

    @PostMapping("/{list-id}/{good-id}")
    public ResponseEntity<Object> addGoodToShopList(@PathVariable("list-id") Long listId, @PathVariable("good-id") Long goodId){
        try {
            shopListService.addGoodInShopList(listId,goodId);
            return ResponseEntity.ok().build();
        } catch (NoPermissionException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{list-id}/{good-id}")
    public ResponseEntity<Object> deleteGoodToShopList(@PathVariable("list-id") Long listId, @PathVariable("good-id") Long goodId){
        try {
            shopListService.addGoodInShopList(listId,goodId);
            return ResponseEntity.ok().build();
        } catch (NoPermissionException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
