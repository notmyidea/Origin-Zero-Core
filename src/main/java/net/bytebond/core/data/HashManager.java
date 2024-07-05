package net.bytebond.core.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class HashManager {

    /*
     * This class is used to generate a hash for a nation.
     * This apporach ensures a high level of uniqueness and ballsy-ness.
     * Also, a very-low probability of collisions which could be devastating for the world of minecraft banks.
     */


    static String algorithm = "SHA-256";
    // SHA-256 :: widely used
    // SHA-1 :: 160-bit but weak
    // SHA-512 :: 512-bit
    // MD-5
    // BLAKE-2


    public static String generateHash(String nationName, UUID ownerUUID) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            String bigmessage = nationName + ownerUUID.toString();
            byte[] encodedHash = digest.digest(bigmessage.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(algorithm + " algorithm not found!", e);
        }
    }

    //hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}