//package com.example.auth;
//
//
//import com.example.entity.UserEntity;
//import com.example.exceptions.InvalidCredentialsException;
//import com.example.repository.UserRepository;
//import com.example.security.BcryptEncoderService;
//import io.micronaut.core.annotation.Nullable;
//import io.micronaut.http.HttpRequest;
//import io.micronaut.security.authentication.AuthenticationProvider;
//import io.micronaut.security.authentication.AuthenticationRequest;
//import io.micronaut.security.authentication.AuthenticationResponse;
//import jakarta.inject.Inject;
//import jakarta.inject.Singleton;
//import org.reactivestreams.Publisher;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.FluxSink;
//
//import java.util.Optional;
//
//
//@Singleton
//public class AuthenticationProviderUserPassword implements AuthenticationProvider<HttpRequest<?>> {
//
//    @Inject
//    private UserRepository userRepository;
//    @Inject
//    private BcryptEncoderService bcryptEncoderService;
//    @Override
//    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest,
//                                                          AuthenticationRequest<?, ?> authenticationRequest) {
//        // Fetch the user from the database based on the provided username
//        Optional<UserEntity> userOpt = userRepository.findByUserName(String.valueOf(authenticationRequest.getIdentity()));
//
//
//        return Flux.create(emitter -> {
//            if (userOpt.isPresent()) {
//                UserEntity user = userOpt.get();
//                // Compare the provided password with the stored password in the database
//                if (bcryptEncoderService.verifyPassword((String) authenticationRequest.getSecret(),user.getPassword())){
//                    emitter.next(AuthenticationResponse.success((String) authenticationRequest.getIdentity()));
//                    emitter.complete();
//                } else {
//                    // Throw a custom exception for invalid credentials
//                    emitter.error(new InvalidCredentialsException("Invalid credentials: Password Incorrect"));
//                }
//            } else {
//                // User not found in the database
//                emitter.error(new InvalidCredentialsException("Invalid credentials: Username Incorrect"));
//            }
//        }, FluxSink.OverflowStrategy.ERROR);
//    }
//}
