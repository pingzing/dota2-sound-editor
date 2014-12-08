/*
 ** 2013 June 16
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.util.io;

import java.nio.ByteBuffer;

/**
 * Knuth-Morris-Pratt algorithm for pattern matching.
 * 
 * Based on http://stackoverflow.com/questions/1507780/searching-for-a-sequence-of-bytes-in-a-binary-file-with-java
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class KMPMatch {
    
    private KMPMatch() {
    }

    /**
     * Finds the first occurrence of the pattern in a byte buffer.
     */
    public static int indexOf(ByteBuffer bb, byte[] pattern) {
        if (bb.remaining() < pattern.length) {
            return -1;
        }
        
        int[] failure = computeFailure(pattern);

        int j = 0;

        while (bb.hasRemaining()) {
            byte b = bb.get();
            
            while (j > 0 && pattern[j] != b) {
                j = failure[j - 1];
            }
            if (pattern[j] == b) {
                j++;
            }
            if (j == pattern.length) {
                return bb.position() - pattern.length;
            }
        }
        return -1;
    }
    
    /**
     * Finds the first occurrence of the pattern in the byte array.
     */
    public int indexOf(byte[] data, byte[] pattern) {
        if (data.length < pattern.length) {
            return -1;
        }
        
        int[] failure = computeFailure(pattern);
        int j = 0;

        for (int i = 0; i < data.length; i++) {
            while (j > 0 && pattern[j] != data[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == data[i]) {
                j++;
            }
            if (j == pattern.length) {
                return i - pattern.length + 1;
            }
        }
        return -1;
    }
    
    /**
     * Computes the failure function using a boot-strapping process, where the
     * pattern is matched against itself.
     */
    private static int[] computeFailure(byte[] pattern) {
        int[] failure = new int[pattern.length];

        int j = 0;
        for (int i = 1; i < pattern.length; i++) {
            while (j > 0 && pattern[j] != pattern[i]) {
                j = failure[j - 1];
            }
            if (pattern[j] == pattern[i]) {
                j++;
            }
            failure[i] = j;
        }

        return failure;
    }
}