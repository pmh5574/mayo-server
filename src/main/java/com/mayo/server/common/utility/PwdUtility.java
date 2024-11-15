package com.mayo.server.common.utility;

import com.mayo.server.common.exception.CustomException;
import com.mayo.server.common.enums.ErrorCode;
import com.mayo.server.common.exception.NotEqualException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class PwdUtility {

    private static final String ALGORITHM = "SHA-256";

    @Value("${secret.sha256-salt}")
    private String LOAD_SALT;

    private static String SHA_256_SALT;

    @PostConstruct
    public void init() {

        SHA_256_SALT = LOAD_SALT;

    }
    public static String hash(String inputPwd) {

        try {

            byte[] salt = SHA_256_SALT.getBytes();
            byte[] inputs = inputPwd.getBytes(StandardCharsets.UTF_8);
            byte[] bytes = new byte[inputs.length + salt.length];

            System.arraycopy(inputs, 0, bytes, 0, inputs.length);
            System.arraycopy(salt, 0, bytes, inputs.length, salt.length);

            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(bytes);

            byte[] hash = digest.digest();

            return Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    public static void comparedPwd(
            String originPwd,
            String inputPwd
    ) {

        if(!hash(inputPwd).equals(originPwd)) {
            throw new NotEqualException(ErrorCode.PWD_NOT_EQUALS);
        }

    }
}
