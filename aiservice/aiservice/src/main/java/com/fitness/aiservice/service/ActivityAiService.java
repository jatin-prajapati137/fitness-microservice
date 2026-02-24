package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ActivityAiService {

//    private final GeminiService geminiService;
//
//    private final RecommendationRepository recommendationRepository;
//
//    public Recommendation generateRecommendation(Activity activity){
//        String prompt = createPromptForActivity(activity);
//        String aiResponse  = geminiService.getRecommendation(prompt);
//        log.info("RESPONSE FROM AI {} ", aiResponse );
//        return processAIResponse(activity, aiResponse);
//    }
//
//
//    private Recommendation processAIResponse(Activity activity, String aiResponse) {
//
//        try{
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode rootNode = mapper.readTree(aiResponse);
//            JsonNode textNode = rootNode.path("candidates")
//                    .get(0)
//                    .path("content")
//                    .get("parts")
//                    .get(0)
//                    .path("text");
//
////            String jsonContent = textNode.asText()
////                    .replaceAll("'''\\n ", "")
////                    .replaceAll("\\'''", "")
////                    .trim();\
//
//            String rawText = textNode.asText().trim();
//            String jsonContent = rawText
//                    .replace("```json", "")
//                    .replace("```", "")
//                    .trim();
//
//
//
////            log.info("RESPONSE FROM CLEANED AI {} ", jsonContent );
//
//
//            JsonNode analysisJson = mapper.readTree(jsonContent);
//            JsonNode analysisNode = analysisJson.path("analysis");
//            StringBuilder fullAnalysis = new StringBuilder();
//            addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall:");
//            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
//            addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate:");
//            addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories:");
//
//            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
//            List<String> suggestions = extractSuggestion(analysisJson.path("suggestions"));
//            List<String> safety = extractSafety(analysisJson.path("safety"));
//
//            Recommendation recommendation = Recommendation.builder()
//                    .activityId(activity.getId())
//                    .userId(activity.getUserId())
//                    .type(activity.getType().toString())
//                    .recommendation(fullAnalysis.toString())
//                    .improvements(improvements)
//                    .suggestion(suggestions)
//                    .safety(safety)
//                    .createdAt(LocalDateTime.now())
//                    .build();
//
//            log.info("Saving recommendation for activity: {}", activity.getId());
//            log.info("Saved successfully");
//            return recommendationRepository.save(recommendation);
//
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//            return createDefaultRecommendation(activity);
//        }
//
////        return recommendationRepository.save(recommendation);
//    }
//
//    private Recommendation createDefaultRecommendation(Activity activity) {
//        return Recommendation.builder()
//                .activityId(activity.getId())
//                .userId(activity.getUserId())
//                .type(activity.getType().toString())
//                .recommendation("Unable to generate detailed analysis")
//                .improvements(Collections.singletonList("Continue with your current routine"))
//                .suggestion(Collections.singletonList("Consider consulting a fitness consultant"))
//                .safety(Arrays.asList(
//                        "Always warm up before exercise",
//                        "Stay hydrated",
//                        "Listen to your body"
//                ))
//                .createdAt(LocalDateTime.now())
//                .build();
//    }
//
//    private List<String> extractSafety(JsonNode safetyNode) {
//        List<String> safety = new ArrayList<>();
//        if (safetyNode.isArray()){
//            safetyNode.forEach(item -> safety.add(item.asText()));
//        }
//        return safety.isEmpty()  ?
//                Collections.singletonList("Follow general safety guidelines") :
//                safety;
//    }
//
//    private List<String> extractSuggestion(JsonNode suggestionsNode) {
//        List<String> suggestions = new ArrayList<>();
//        if (suggestionsNode.isArray()){
//            suggestionsNode.forEach(suggestion -> {
//                String workout = suggestion.path("workout").asText();
//                String description = suggestion.path("description").asText();
//                suggestions.add(String.format("%s: %s",workout,description));
//            });
//        }
//        return suggestions.isEmpty() ?
//                Collections.singletonList("No specific suggestion provided") :
//                suggestions;
//    }
//
//    private List<String> extractImprovements(JsonNode improvementsNode) {
//        List<String> improvements = new ArrayList<>();
//        if (improvementsNode.isArray()){
//            improvementsNode.forEach(improvement -> {
//                String area = improvement.path("area").asText();
//                String detail = improvement.path("recommendation").asText();
//                improvements.add(String.format("%s: %s", area,detail));
//                    });
//        }
//        return improvements.isEmpty() ?
//                Collections.singletonList("No specific improvements provided") :
//                improvements;
//    }
//
//    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
//        if (!analysisNode.path(key).isMissingNode()){
//            fullAnalysis.append(prefix)
//                    .append(analysisNode.path(key).asText())
//                    .append("\n\n");
//        }
//
//    }
//
//    private String createPromptForActivity(Activity activity) {
//        return String.format("""
//                Analyze this fitness and provide detailed recommendation in the following EXACT JSON format:
//                {
//                    "analysis":{
//                        "overall": "Overall analysis here",
//                        "pace": "Pace analysis here ",
//                        "heartRate": "Heart rate analysis here",
//                        "caloriesBurned": "Calories analysis here"
//                 },
//
//                 "improvements": [
//                   {
//                    "area": "Area name",
//                    "recommendation": "Detailed recommendation"
//                    }
//                 ],
//
//                 "suggestions": [
//                   {
//                     "workout": "Workout name":
//                     "description": Detailed workout description"
//                   }
//                 ],
//
//                 "safety": [
//                      "Safety point 1",
//                      "Safety point 2"
//                    ]
//                 }
//
//                Analyze this activity:
//                Activity Type: %s
//                Duration: %d minutes
//                Calories Burned: %d
//                Additional Metrics: %s
//
//                Provide details analysis focusing on performance, improvements, next workout suggestion, and safety guidelines.
//                Ensure the response follows the EXACT JSON format shown above.
//                """,
//
//                    activity.getType(),
//                    activity.getDuration(),
//                    activity.getCaloriesBurned(),
//                    activity.getAdditionalMetrics()
//        );
//    }
//}



    private final GeminiService geminiService;
    private final RecommendationRepository recommendationRepository;
    private final ObjectMapper mapper = new ObjectMapper();

    public Recommendation generateRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse  = geminiService.getRecommendation(prompt);
        log.info("RAW RESPONSE FROM AI: {}", aiResponse);
        return processAIResponse(activity, aiResponse);
    }

    private Recommendation processAIResponse(Activity activity, String aiResponse) {

        try{
            // Step 1: Parse outer Gemini response
            JsonNode outerNode = mapper.readTree(aiResponse);

            String rawText = outerNode
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText()
                    .trim();

            // Step 2: Clean markdown + remove trailing commas
            String cleanedJson = rawText
                    .replace("```json", "")
                    .replace("```", "")
                    .replaceAll(",\\s*}", "}")
                    .replaceAll(",\\s*]", "]")
                    .trim();

            log.info("CLEANED AI JSON: {}", cleanedJson);

            // Step 3: Parse actual JSON safely
            JsonNode analysisJson = mapper.readTree(cleanedJson);

            JsonNode analysisNode = analysisJson.path("analysis");

            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall: ");
            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace: ");
            addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate: ");
            addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories: ");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestion(analysisJson.path("suggestions"));
            List<String> safety = extractSafety(analysisJson.path("safety"));

            Recommendation recommendation = Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .type(activity.getType().toString())
                    .recommendation(fullAnalysis.toString())
                    .improvements(improvements)
                    .suggestion(suggestions)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    .build();

            log.info("Saving recommendation for activity: {}", activity.getId());
            return recommendationRepository.save(recommendation);

        }catch (Exception e){
            log.error("Error processing AI response", e);
            return recommendationRepository.save(createDefaultRecommendation(activity));
        }
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .type(activity.getType().toString())
                .recommendation("Unable to generate detailed analysis")
                .improvements(Collections.singletonList("Continue with your current routine"))
                .suggestion(Collections.singletonList("Consider consulting a fitness consultant"))
                .safety(Arrays.asList(
                        "Always warm up before exercise",
                        "Stay hydrated",
                        "Listen to your body"
                ))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafety(JsonNode safetyNode) {
        List<String> safety = new ArrayList<>();
        if (safetyNode.isArray()){
            safetyNode.forEach(item -> safety.add(item.asText()));
        }
        return safety.isEmpty() ?
                Collections.singletonList("Follow general safety guidelines") :
                safety;
    }

    private List<String> extractSuggestion(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if (suggestionsNode.isArray()){
            suggestionsNode.forEach(suggestion -> {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestions.add(workout + ": " + description);
            });
        }
        return suggestions.isEmpty() ?
                Collections.singletonList("No specific suggestion provided") :
                suggestions;
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();
        if (improvementsNode.isArray()){
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();
                improvements.add(area + ": " + detail);
            });
        }
        return improvements.isEmpty() ?
                Collections.singletonList("No specific improvements provided") :
                improvements;
    }

    private void addAnalysisSection(StringBuilder fullAnalysis,
                                    JsonNode analysisNode,
                                    String key,
                                    String prefix) {
        if (!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
                You are a professional fitness AI.

                Return ONLY valid JSON.
                No markdown.
                No explanation.
                No trailing commas.
                Ensure strictly valid JSON.

                Required JSON structure:
                {
                  "analysis": {
                    "overall": "text",
                    "pace": "text",
                    "heartRate": "text",
                    "caloriesBurned": "text"
                  },
                  "improvements": [
                    {
                      "area": "text",
                      "recommendation": "text"
                    }
                  ],
                  "suggestions": [
                    {
                      "workout": "text",
                      "description": "text"
                    }
                  ],
                  "safety": [
                    "text"
                  ]
                }

                Activity Details:
                Type: %s
                Duration: %d minutes
                Calories: %d
                Metrics: %s
                """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }
}