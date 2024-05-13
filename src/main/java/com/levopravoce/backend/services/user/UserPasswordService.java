package com.levopravoce.backend.services.user;

import com.levopravoce.backend.common.UserUtils;
import com.levopravoce.backend.common.cache.CacheStore;
import com.levopravoce.backend.entities.User;
import com.levopravoce.backend.repository.UserRepository;
import com.levopravoce.backend.services.user.dto.PasswordRestore;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserPasswordService {

    private final CacheStore<PasswordRestore> passwordRestoreCache;
    private final UserRepository userRepository;
    private final UserUtils userUtils;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    @Value("${spring.mail.username}")
    private String mailFrom;

    public void restorePassword(String email) throws MessagingException {
        if (email == null) {
            throw new IllegalArgumentException("Email é obrigatório");
        }

        boolean userExist = userRepository.existsByEmail(email);
        if (!userExist) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        String randomCode = RandomStringUtils.randomAlphabetic(8);
        MimeMessage mailSenderMimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mailMessage = new MimeMessageHelper(mailSenderMimeMessage, false, StandardCharsets.UTF_8.name());
        mailMessage.setFrom(mailFrom);
        mailMessage.setTo(email);
        mailMessage.setSubject("Password restore");
        mailMessage.setText("""
                Seu código de recuperação de senha é:
            """ + randomCode
        );
        javaMailSender.send(mailSenderMimeMessage);

        PasswordRestore passwordRestore = new PasswordRestore();
        passwordRestore.setEmail(email);
        passwordRestore.setRestoreTime(LocalDateTime.now());
        passwordRestoreCache.add(randomCode, passwordRestore);
    }

    public boolean existCode(String code) {
        return passwordRestoreCache.exist(code);
    }

    public void changePassword(String code, String password) {
        if (code == null || password == null) {
            throw new IllegalArgumentException("Code and password are required");
        }

        PasswordRestore passwordRestore = passwordRestoreCache.get(code);
        if (passwordRestore == null) {
            throw new IllegalArgumentException("Code not found");
        }

        if (passwordRestore.getRestoreTime().plusHours(1).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Code expired");
        }

        if (userUtils.passwordIsInvalid(password)) {
            throw new IllegalArgumentException("Password is invalid");
        }

        Optional<User> optionalUser = userRepository.findByEmail(passwordRestore.getEmail());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = optionalUser.get();

        String encodedPassword = passwordEncoder.encode(password);
        if (passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Password is the same as the current one");
        }

        user.setPassword(encodedPassword);
        userRepository.saveAndFlush(user);
        passwordRestoreCache.remove(code);
    }
}
