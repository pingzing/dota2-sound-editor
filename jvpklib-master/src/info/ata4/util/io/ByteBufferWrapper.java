/*
 ** 2013 June 22
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.util.io;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class ByteBufferWrapper {
    
    protected final ByteBuffer buf;

    public ByteBufferWrapper(ByteBuffer buf) {
        this.buf = buf;
    }
    
    public ByteBuffer getBuffer() {
        return buf;
    }
    
    /**
     * Same as {@link java.nio.ByteBuffer}.hasRemaining()
     *
     * @return true, if there are remaining bytes in the byte buffer
     */
    public boolean hasRemaining() {
        return buf.hasRemaining();
    }

    /**
     * Same as {@link java.nio.ByteBuffer}.remaining()
     *
     * @return remaining bytes in the byte buffer
     */
    public int remaining() {
        return buf.remaining();
    }

    /**
     * Same as {@link java.nio.ByteBuffer}.position()
     *
     * @return position in the byte buffer
     */
    public int position() {
        return buf.position();
    }

    /**
     * Same as {@link java.nio.ByteBuffer}.position(int newPosition)
     * 
     * @param pos new buffer position
     * @throws IOException 
     */
    public void position(int pos) throws IOException {
        try {
            buf.position(pos);
        } catch (IllegalArgumentException ex) {
            throw new IOException(ex);
        }
    }

    /**
     * Sets the buffer's position relative to the current one. 
     * 
     * @param pos new relative buffer position
     * @throws IOException 
     */
    public void seek(int pos) throws IOException {
        position(position() + pos);
    }
}
