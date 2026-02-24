package com.fitness.activityservice.service;

import com.fitness.activityservice.ActivityRepository;
import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;


    public  ActivityResponse trackActivity(ActivityRequest request) {

        boolean isValidUser = userValidationService.validateUser(request.getUserId());

        if (!isValidUser){
            throw new RuntimeException("Invalid User:" + request.getUserId());
        }


        System.out.println("REQUEST RECEIVED: " + request.getUserId());
        System.out.println("Saving to Mongo...");

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
//                .startTime(
//                        Date.from(
//                                request.getStartTime()
//                                        .atZone(ZoneId.systemDefault())
//                                        .toInstant()
//                        )
//                )
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);
        System.out.println("SAVED ID = " + savedActivity.getId());

        try{
            kafkaTemplate.send(topicName,savedActivity.getUserId(), savedActivity);
        } catch (Exception e){
            e.printStackTrace();
        }

        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
//        response.setStartTime(
//                activity.getStartTime()
//                        .toInstant()
//                        .atZone(ZoneId.systemDefault())
//                        .toLocalDateTime()
//        );
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }

    public @Nullable List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activityList = activityRepository.findByUserId(userId);
        return activityList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
