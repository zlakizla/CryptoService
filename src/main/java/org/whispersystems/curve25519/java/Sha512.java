package org.whispersystems.curve25519.java;

public interface Sha512 {

    void calculateDigest(byte[] out, byte[] in, long length);
}
