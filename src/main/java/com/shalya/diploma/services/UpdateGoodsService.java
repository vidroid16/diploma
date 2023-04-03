package com.shalya.diploma.services;

import com.shalya.diploma.models.Category;
import com.shalya.diploma.models.Good;
import com.shalya.diploma.repositories.CategoryRepository;
import com.shalya.diploma.repositories.GoodRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateGoodsService {

    private WebDriver driver;
    private final CategoryRepository categoryRepository;
    private final GoodRepository goodRepository;

    @PostConstruct
    private void postConstruct(){
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
//        WebDriver driver = new ChromeDriver(options);
//        this.driver = driver;
//        driver.manage().window().minimize();
//        driver.get("https://www.perekrestok.ru/");
    }

    private Double convertIntPrice(int price){
        String str = String.valueOf(price);
        String firstPart = str.substring(0,str.length()-2);
        String secondPart = StringUtils.right(str,2);
        String resultStr = firstPart.concat(".").concat(secondPart);
        return Double.valueOf(resultStr);
    }

    public void getCategoryItems(int category, String name){

        String sessionCookie = driver.manage().getCookieNamed("session").getValue().substring(2);
        JSONObject sessionJson = new JSONObject(sessionCookie);
        String token = sessionJson.getString("accessToken");

        String uri = "https://www.perekrestok.ru/api/customer/1.4.1.0/catalog/product/grouped-feed"; // or any other uri

        HttpClient httpClient = HttpClient.newHttpClient();

        String params = "{\"page\":1,\"perPage\":1000,\"filter\":{\"category\":"+category+",\"onlyWithProductReviews\":false},\"withBestProductReviews\":false}";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .uri(new URI(uri)).build();
            HttpResponse response = null;

            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject res = new JSONObject(response.body().toString());
            JSONArray arr = res.getJSONObject("content").getJSONArray("items");
            for (int i = 0; i < arr.length(); i++) {
                String group = arr.getJSONObject(i).getJSONObject("group").getString("title");
                if(group.equals("Рекомендуем"))
                    continue;
                JSONArray products = arr.getJSONObject(i).getJSONArray("products");
                ArrayList<Good> goods = new ArrayList<>();
                Category categoryToSave = new Category();
                categoryToSave.setCategory(category);
                categoryToSave.setGroup(group);
                categoryToSave.setName(name);
                categoryRepository.save(categoryToSave);
            }
            log.debug("end");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getAllCategoryGoods(Long id){
        Category category = categoryRepository.getById(id);
        String sessionCookie = driver.manage().getCookieNamed("session").getValue().substring(2);
        JSONObject sessionJson = new JSONObject(sessionCookie);
        String token = sessionJson.getString("accessToken");

        String uri = "https://www.perekrestok.ru/api/customer/1.4.1.0/catalog/product/grouped-feed"; // or any other uri

        HttpClient httpClient = HttpClient.newHttpClient();

        String params = "{\"page\":1,\"perPage\":1000,\"filter\":{\"category\":"+category.getCategory()+",\"onlyWithProductReviews\":false},\"withBestProductReviews\":false}";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .uri(new URI(uri)).build();
            HttpResponse response = null;

            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            ArrayList<Good> goods = new ArrayList<>();
            JSONObject res = new JSONObject(response.body().toString());
            JSONArray arr = res.getJSONObject("content").getJSONArray("items");
            for (int i = 0; i < arr.length(); i++) {
                String group = arr.getJSONObject(i).getJSONObject("group").getString("title");
                if(!group.equals(category.getGroup()))
                    continue;
                JSONArray products = arr.getJSONObject(i).getJSONArray("products");
                for (int j = 0; j < products.length(); j++) {
                    try {
                        Good good = new Good();
                        JSONObject product = products.getJSONObject(j);
                        good.setName(product.getString("title"));
                        good.setCategory(category);
                        good.setUnit(product.getJSONObject("masterData").getString("unitName"));
                        if(good.getUnit().equals("кг")){
                            good.setAmount(1000);
                        }else {
                            Integer amount = product.getJSONObject("masterData").getInt("weight");
                            good.setAmount(amount);
                        }
                        good.setRating(product.getInt("rating"));
                        good.setPrice(convertIntPrice(product.getInt("medianPrice")));

                        goodRepository.save(good);
                        goods.add(good);
                        log.info(good.getName());
                    }catch (Exception e){
                        e.printStackTrace();
                        log.info(products.getJSONObject(i).toString());
                    }
                }
            }
            log.info(String.valueOf(goods.size()));
            goods.forEach(goodRepository::save);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateAllGoods(){
        List<Category> categoryList = categoryRepository.findAll();
        for (Category category:categoryList) {
            getAllCategoryGoods(category.getId());
        }
    }
}
