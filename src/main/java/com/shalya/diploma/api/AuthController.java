package com.shalya.diploma.api;

import com.shalya.diploma.dto.requests.AuthRequest;
import com.shalya.diploma.dto.requests.RegisterRequest;
import com.shalya.diploma.dto.requests.UpdateShopListRequest;
import com.shalya.diploma.knapsack.Item;
import com.shalya.diploma.knapsack.KnapsackSolver;
import com.shalya.diploma.knapsack.Packable;
import com.shalya.diploma.models.User;
import com.shalya.diploma.repositories.HelloEntityRepository;
import com.shalya.diploma.repositories.UserRepository;
import com.shalya.diploma.security.JwtUtils;
import com.shalya.diploma.services.UpdateGoodsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${baseUrl}/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final HelloEntityRepository helloEntityRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UpdateGoodsService updateGoodsService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        if (userDetails!= null){
            log.info("User was log in successfully: {}",userDetails.getUsername());
            return ResponseEntity.ok(jwtUtils.generateJwtToken(authentication));
        }
        else{
            log.info("User not found: {}", authRequest.getUsername());
            return ResponseEntity.status(403).body("Неверные данные входа!");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
        log.info("User was log out successfully: {}", authentication.getName());
        return ResponseEntity.ok("Вы вышли из системы");
    }

    @PostMapping("/register")
    public ResponseEntity addUser(@RequestBody RegisterRequest registerRequest) {
        User user = userRepository.findByLogin(registerRequest.getLogin()).orElse(null);
        if (user != null)
            return ResponseEntity.badRequest().body("User has already existed!");
        user = new User();
        user.setPassword(new BCryptPasswordEncoder(12).encode(registerRequest.getPassword()));
        user.setLogin(registerRequest.getLogin());
        if (registerRequest.getFirstName() != null && !registerRequest.getFirstName().isEmpty())
            user.setFirstName(registerRequest.getFirstName());
        if (registerRequest.getLastName() != null && !registerRequest.getLastName().isEmpty())
            user.setLastName(registerRequest.getLastName());
        userRepository.save(user);
        log.info("User was registered successfully: {}", user.getLogin());
        return ResponseEntity.ok().body("User was created");
    }

    @PostMapping("/t/{cat}/{name}")
    public String test(@PathVariable("cat") Long id, @PathVariable("name") String name){
        updateGoodsService.updateAllGoods();
        return "D";
    }
    @PostMapping("/t")
    public String test2(){
        List<Packable> cat1 = new ArrayList<>();
        cat1.add(new Item(1L,4,3));
        cat1.add(new Item(2L,3,2));
        cat1.add(new Item(3L,3,2));
        cat1.add(new Item(4L,3,2));

        List<Packable> cat2 = new ArrayList<>();
        cat2.add(new Item(5L,2,1));
        cat2.add(new Item(6L,2,1));
        cat2.add(new Item(7L,3,4));
        cat2.add(new Item(8L,3,5));
        cat2.add(new Item(9L,3,2));

        List<Packable> cat3 = new ArrayList<>();
        cat3.add(new Item(10L,2,1));
        cat3.add(new Item(11L,2,1));
        cat3.add(new Item(12L,3,4));
        cat3.add(new Item(13L,3,5));
        cat3.add(new Item(14L,3,2));

        List<Packable> cat4 = new ArrayList<>();
        cat4.add(new Item(15L,2,1));
        cat4.add(new Item(16L,2,1));
        cat4.add(new Item(17L,3,4));
        cat4.add(new Item(18L,3,5));
        cat4.add(new Item(19L,3,2));

        ArrayList<List<Packable>> items = new ArrayList<>();

        items.add(cat1);
        items.add(cat2);
        items.add(cat3);
        items.add(cat4);

        KnapsackSolver solver = new KnapsackSolver();
        solver.setItems(items);
        solver.setMaxWeight(12);
        return solver.solve();
    }
}
