package com.fitness.gateway;

import com.fitness.gateway.user.RegisterRequest;
import com.fitness.gateway.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.function.Consumer;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter {

    private final UserService userService;

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
//        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
//        RegisterRequest registerRequest = getUserDetails(token);
//        if (userId == null) {
//            userId = registerRequest.getKeyCloakId();
//        }
//
//        if (userId != null && token != null) {
//            String finalUserId = userId;
//            return userService.validateUser(userId)
//                    .flatMap(exist -> {
//                        if (exist) {
//                            if (registerRequest != null) {
//                                return userService.registerUser(registerRequest)
//                                        .then(Mono.empty());
//                            } else {
//                                return Mono.empty();
//                            }
//                        } else {
//                            log.info("User already exist, Skipping sync");
//                            return Mono.empty();
//                        }
//                    })
//                    .then(Mono.defer(() -> {
//                        ServerHttpRequest mutateRequest = (ServerHttpRequest) exchange.getRequest().mutate()
//                                .header("X-User-ID", finalUserId)
//                                .build();
//                        return chain.filter(exchange.mutate().request((Consumer<org.springframework.http.server.reactive.ServerHttpRequest.Builder>) mutateRequest).build());
//                    }));
//        }
//        return chain.filter(exchange);
//    }
@Override
public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

    String token = exchange.getRequest().getHeaders().getFirst("Authorization");

    if (token == null || !token.startsWith("Bearer ")) {
        return chain.filter(exchange);
    }

    RegisterRequest registerRequest = getUserDetails(token);

    if (registerRequest == null || registerRequest.getKeyCloakId() == null) {
        return chain.filter(exchange);
    }

    String userId = registerRequest.getKeyCloakId();

    return userService.validateUser(userId)
            .flatMap(exists -> {

                Mono<Void> syncMono;

                if (!exists) {
                    log.info("User not found. Registering...");
                    syncMono = userService.registerUser(registerRequest).then();
                } else {
                    log.info("User already exist, Skipping sync");
                    syncMono = Mono.empty();
                }

                return syncMono.then(
                        chain.filter(
                                exchange.mutate()
                                        .request(
                                                exchange.getRequest()
                                                        .mutate()
                                                        .headers(headers ->
                                                                headers.set("X-User-ID", userId)
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                );
            });
}


    private RegisterRequest getUserDetails(String token) {
        try{
            String tokenWithoutBearer = token.replace("Bearer", "").trim();
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            RegisterRequest request = new RegisterRequest();
            request.setEmail(claims.getStringClaim("email"));
            request.setKeyCloakId(claims.getStringClaim("sub"));
            request.setFirstName(claims.getStringClaim("given_name"));
            request.setLastName(claims.getStringClaim("family_name"));
            request.setPassword("dummy@123123");

            return request;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

