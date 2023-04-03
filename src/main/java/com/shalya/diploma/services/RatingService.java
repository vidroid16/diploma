package com.shalya.diploma.services;

import com.beust.ah.A;
import com.shalya.diploma.dto.CosMeasureDto;
import com.shalya.diploma.dto.RatingDto;
import com.shalya.diploma.dto.UserVector;
import com.shalya.diploma.models.Rating;
import com.shalya.diploma.models.RatingId;
import com.shalya.diploma.models.User;
import com.shalya.diploma.repositories.GoodRepository;
import com.shalya.diploma.repositories.RatingRepository;
import com.shalya.diploma.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final UserRepository userRepository;
    private final GoodRepository goodRepository;
    private final RatingRepository ratingRepository;

    private static final int  FIRST_N= 3;

    public void updateAllUsers(){
        var users = userRepository.findAll();
        for (User user : users) {
            updateUser(user.getId());
        }
    }
    @Transactional
    public void updateUser(Long id){
        var cosMeasure = getCosMeasureForUser(id);
        var userRatings = this.getUsersRatingsVector();
        updateRatingsForUser(id,cosMeasure,userRatings);
    }
    public List<UserVector> getUsersRatingsVector(){
        var users = userRepository.findAll();
        var iterator = users.iterator();
        List<UserVector> ratingVectors = new ArrayList<>();
        while (iterator.hasNext()){
            User user = iterator.next();
            UserVector userVector = new UserVector();
            userVector.setRatings(ratingRepository.getUserRatingsVector(user.getId()));
            userVector.setUserId(user.getId());
            ratingVectors.add(userVector);
        }
        return ratingVectors;
    }
    public List<RatingDto> getUserRatingsVector(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            return null;
        return ratingRepository.getUserRatingsVector(userId);
    }
    public List<CosMeasureDto> getCosMeasureForUser(Long userId) {
        var userVector = getUserRatingsVector(userId);
        var otherUsersVectors = getUsersRatingsVector();
        List<CosMeasureDto> result = new ArrayList<>();
        for (UserVector vector : otherUsersVectors) {
            if (vector.getUserId().equals(userId))
                continue;
            CosMeasureDto dto = new CosMeasureDto();
            double measure = getCosMeasureBetween2Vectors(userVector, vector.getRatings());
            dto.setMeasure(measure);
            dto.setUserId(vector.getUserId());
            result.add(dto);
        }
        return result;
    }
    public void updateRatingsForUser(Long userId, List<CosMeasureDto> cos, List<UserVector> vectors){
        cos.sort(Comparator.comparing(CosMeasureDto::getMeasure).reversed());
        var bestUsersMeasures = cos.stream().filter(p->!p.getMeasure().isNaN()).limit(FIRST_N).toList();
        var userMeasureMap = bestUsersMeasures.stream()
                .collect(Collectors.toMap(CosMeasureDto::getUserId, CosMeasureDto::getMeasure));
        double measureSum = bestUsersMeasures.stream().mapToDouble(CosMeasureDto::getMeasure).sum();
        var bestUsersVectors = vectors.stream().
                filter(v->bestUsersMeasures.stream().
                        anyMatch(b->b.getUserId().equals(v.getUserId()))).
                toList();
        bestUsersVectors.forEach(p->{
            p.getRatings().forEach(k->{
                k.setRating(k.getRating()==null ? 0.0 : k.getRating() *userMeasureMap.get(p.getUserId()));
            });
        });

        double rating = 0.0;
        List<RatingDto> result = new ArrayList<>();
        //подсчет новой оценки
        for (int i = 0; i < bestUsersVectors.get(0).getRatings().size(); i++) {
            for (int j = 0; j < bestUsersVectors.size(); j++) {
                rating += bestUsersVectors.get(j).getRatings().get(i).getRating();
            }
            Long uId = bestUsersVectors.get(0).getRatings().get(i).getUserId();
            Long gId = bestUsersVectors.get(0).getRatings().get(i).getGoodId();
            result.add(new RatingDto(uId,gId,rating/measureSum));
            rating = 0;
        }
        //проставление оценки
        for (RatingDto ratingUpd:result) {
            var user = userRepository.findById(userId).orElse(null);
            var good = goodRepository.getById(ratingUpd.getGoodId()).orElse(null);
            var point = ratingUpd.getRating();
            Rating ratingToAdd = ratingRepository.getById(new RatingId(userId,ratingUpd.getGoodId()))
                    .orElse(new Rating(user,good,point,false));
            if (!ratingToAdd.getIsUserOwned())
                ratingRepository.save(ratingToAdd);
        }
    }
    public double getCosMeasureBetween2Vectors(List<RatingDto> v1, List<RatingDto> v2){
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < v1.size(); i++) {
            double r1 = (v1.get(i).getRating()!=null) ? v1.get(i).getRating() : 0.0;
            double r2 = (v2.get(i).getRating()!=null) ? v2.get(i).getRating() : 0.0;
            dotProduct += r1 * r2;
            normA += Math.pow(r1, 2);
            normB += Math.pow(r2, 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
    public void randomRatings(int amount, Long userId, Long min, Long max){
        Random randomL = new Random();
        Random randomD = new Random();
        User user = userRepository.findById(userId).orElse(null);
        for (int i = 0; i < amount; i++) {
            Rating rating = new Rating();
            Long rand = randomL.nextLong(max-min)+min;
            rating.setGood(goodRepository.getById(rand).orElse(null));
            rating.setUser(user);
            rating.setRating(randomD.nextDouble(5.0));
            rating.setIsUserOwned(true);
            ratingRepository.save(rating);
        }

    }


}
