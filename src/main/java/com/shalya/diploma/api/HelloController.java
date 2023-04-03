package com.shalya.diploma.api;

import com.shalya.diploma.dto.GetUserShopListsDto;
import com.shalya.diploma.dto.requests.AuthRequest;
import com.shalya.diploma.models.HelloEntity;
import com.shalya.diploma.models.ShopList;
import com.shalya.diploma.models.Rating;
import com.shalya.diploma.models.User;
import com.shalya.diploma.repositories.*;
import com.shalya.diploma.security.JwtUtils;
import com.shalya.diploma.services.ShopListService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("${baseUrl}/hello")
public class HelloController {

    private final HelloEntityRepository helloEntityRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final ListRepository listRepository;
    private final GoodRepository goodRepository;
    private final RatingRepository ratingRepository;
    private final ShopListService shopListService;

    @PostMapping("/test")
    public String test(){
        var entity = new HelloEntity();
        entity.setName("Надо же с чего-то начинать!");
        helloEntityRepository.save(entity);
        log.info("Тест прошел успешно!");
        return "OK";
    }

    @GetMapping
    public ResponseEntity<List<GetUserShopListsDto>> getUserLists(){
        List<GetUserShopListsDto> dto = shopListService.getUserShopLists();
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/onetomany")
    public String oneToMany(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        ShopList list = new ShopList();
        list.setName("FirstList");
        list.setUser(user);
        user.getLists();
        Rating rating = new Rating();
//        rating.setUser(user);
//        rating.setGood(goodRepository.getById(1L).orElse(null));
//        rating.setRating(6.54);
        //ratingRepository.save(rating);
        java.util.List<Rating> us = ratingRepository.getAllByUser(user);
        return "OK";
    }
    @PostMapping("/mtom")
    public String manyToMany(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(authentication.getName()).orElse(null);
        ShopList list = new ShopList();
        list.setName("FirstList");
        list.setUser(user);
        user.getLists();

        return "OK";
    }
    @PostMapping("/auf")
    public ResponseEntity<String> auf(@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        if (userDetails!= null)
            return ResponseEntity.ok(jwtUtils.generateJwtToken(authentication));
        else
            return ResponseEntity.status(403).body("SHOTOTAM");
    }
    @PostMapping("/add")
    public ResponseEntity addUser(@RequestParam(value = "login") String login, @RequestParam(value = "password") String password,
                                  @RequestParam(value = "first_name", required = false) String firstName, @RequestParam(value = "last_name", required = false) String lastName) {
        User user = userRepository.findByLogin(login).orElse(null);
        if (user != null)
            return ResponseEntity.badRequest().body("User has already existed!");
        user = new User();
        user.setPassword(new BCryptPasswordEncoder(12).encode(password));
        user.setLogin(login);
        if (firstName != null && !firstName.isEmpty())
            user.setFirstName(firstName);
        if (lastName != null && !lastName.isEmpty())
            user.setLastName(lastName);
        userRepository.save(user);
        return ResponseEntity.ok().body("User was created");
    }
}
