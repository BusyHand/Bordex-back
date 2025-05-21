package com.ugrasu.bordexback.auth.service;

import com.ugrasu.bordexback.auth.dto.Tokens;
import com.ugrasu.bordexback.auth.security.authenfication.AuthenticatedUser;
import com.ugrasu.bordexback.auth.security.authenfication.TelegramUserAuthentication;
import com.ugrasu.bordexback.auth.security.provider.TokenProvider;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.Role;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${telegrambots.token}")
    private String botToken;

    private Tokens login(Authentication toAuthenticate) {
        Authentication authentication = authenticationManager.authenticate(toAuthenticate);
        UserDetails loginUser = (UserDetails) authentication.getPrincipal();
        return tokenProvider.generateTokens(loginUser);
    }

    public Tokens login(AuthenticatedUser authenticatedUser) {
        return tokenProvider.generateTokens(authenticatedUser);
    }

    public Tokens login(User user) {
        return login(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    }

    public Tokens loginTelegram(User user) {
        return login(new TelegramUserAuthentication(user.getTelegramPasscode(), user.getChatId()));
    }

    public User register(User user) {
        if (userRepository.existsUserByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new EntityExistsException("User with username or email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User autheficateTelegramUser(User user) {
        if (userRepository.existsByChatId(user.getChatId())) {
            User alreadyExistUser = userRepository.findByChatId(user.getChatId()).get();
            if (!alreadyExistUser.getTelegramUsername().equals(user.getTelegramUsername())
                && (userRepository.existsUserByUsername(user.getTelegramUsername())
                    || userRepository.existsByTelegramUsername(user.getTelegramUsername()))) {
                user.setTelegramUsername(user.getTelegramUsername() + UUID.randomUUID());
            }
            alreadyExistUser.setTelegramUsername(user.getTelegramUsername());
            alreadyExistUser.setTelegramPasscode(user.getTelegramPasscode());
            user = alreadyExistUser;

        } else if (userRepository.existsUserByUsername(user.getTelegramUsername())) {
            user.setRoles(Set.of(Role.USER));
            user.setTelegramUsername(user.getTelegramUsername() + UUID.randomUUID());
        }
        User saved = userRepository.save(user);
        return userRepository.save(saved);
    }


    public String generateTelegramPasscode(Long userId) {
        String randomPasscode = getRandomPasscode();
        User user = userRepository.findById(userId).get();
        user.setTelegramPasscode(randomPasscode);
        userRepository.save(user);
        return randomPasscode;
    }

    private String getRandomPasscode() {
        String passcode;
        do {
            passcode = generateRandomPasscode();
        } while (userRepository.existsByTelegramPasscode(passcode));
        return passcode;
    }

    private String generateRandomPasscode() {
        String characters = "QWERTYUIOPASDFGHJKLZXCVBNM";
        SecureRandom random = new SecureRandom();
        StringBuilder passcode = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(characters.length());
            passcode.append(characters.charAt(index));
        }
        return passcode.toString();
    }

    public User unassignTelegram(Long userId) {
        User user = userRepository.findById(userId).get();
        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new AccessDeniedException("Полностью зарегистрируйтесь чтобы отвязать телеграмм");
        }
        user.setTelegramPasscode(null);
        user.setChatId(null);
        user.setTelegramUsername(null);
        return userRepository.save(user);
    }

    public User telegramPostRegister(Long userId, User user) {
        if (userRepository.existsUserByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new EntityExistsException("User with username or email already exists");
        }
        User loggedUser = userRepository.findById(userId).get();
        loggedUser.setLastName(user.getLastName());
        loggedUser.setFirstName(user.getFirstName());
        loggedUser.setUsername(user.getUsername());
        loggedUser.setEmail(user.getEmail());
        loggedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(loggedUser);
    }

    public boolean validateInitData(String initData) {
        Map<String, String> dataMap = parseInitData(initData);
        String hash = dataMap.remove("hash");
        try {
            return validateTelegramAuth(dataMap, hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> parseInitData(String initData) {
        return Arrays.stream(initData.split("&"))
                .map(pair -> pair.split("=", 2))
                .collect(Collectors.toMap(
                        arr -> URLDecoder.decode(arr[0], StandardCharsets.UTF_8),
                        arr -> arr.length > 1 ? URLDecoder.decode(arr[1], StandardCharsets.UTF_8) : ""
                ));
    }

    private boolean validateTelegramAuth(Map<String, String> paramMap, String receivedHash) throws Exception {

        // Пункт 1. Убираем hash и сортируем оставшиеся параметры
        String dataString = paramMap.entrySet().stream()
                .filter(e -> !"hash".equals(e.getKey()))
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue()) // Берем первый элемент из массива параметров
                .collect(Collectors.joining("\n"));

        // Пункт 2. Создаем HMAC SHA-256 хеш
        var sha256HMAC = Mac.getInstance("HmacSHA256");
        var secretKeySpec = new SecretKeySpec(getSecretHashByInitData(), "HmacSHA256");
        sha256HMAC.init(secretKeySpec);

        byte[] hash2 = sha256HMAC.doFinal(dataString.getBytes());

        // Пункт 3. Преобразуем байты хеша в строку в hex формате
        var calculatedHash = bytesToHex(hash2);

        // Пункт 4. Сравниваем полученный хеш с тем, что был в запросе
        return calculatedHash.equals(receivedHash);
    }

    private byte[] getSecretHashByInitData() throws InvalidKeyException, NoSuchAlgorithmException {
        var sha256HMAC = Mac.getInstance("HmacSHA256");
        var secretKeySpec = new SecretKeySpec("WebAppData".getBytes(), "HmacSHA256");
        sha256HMAC.init(secretKeySpec);

        return sha256HMAC.doFinal(botToken.getBytes());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
