package com.fitness.aiservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

//@Document(collection = "activities")
//@Document(collection = "activities")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Activity {

    @Id
    private String id;

    private String userId;
    private ActivityType type;
    private Integer duration;
    private Integer caloriesBurned;

//    @Field(targetType = FieldType.DATE_TIME)
    private LocalDateTime startTime;

    @Field("metrics")
    private Map<String, Object> additionalMetrics;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

//    private Date startTime;
//    @CreatedDate
//    private Date createdAt;
//    @LastModifiedDate
//    private Date updatedAt;

}
