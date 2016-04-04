/*
 * %W% %E%
 *
 * Copyright (c) 2009, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.synch4j.zip;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.CRC32;
import java.util.zip.Inflater;
import java.util.zip.ZipException;

import sun.security.action.GetPropertyAction;


/**
 * This class implements an input stream filter for reading files in the
 * ZIP file format. Includes support for both compressed and uncompressed
 * entries.
 *
 * @author	David Connelly
 * @version	%I%, %G%
 */
public
class CZipInputStream extends InflaterInputStream implements ZipConstants {
    private ZipEntry entry;
    private int flag;
    private CRC32 crc = new CRC32();
    private long remaining;
    private byte[] tmpbuf = new byte[512];

    private static final int STORED = ZipEntry.STORED;
    private static final int DEFLATED = ZipEntry.DEFLATED;

    private boolean closed = false;
    // this flag is set to true after EOF has reached for
    // one entry
    private boolean entryEOF = false;
    private String encoding="UTF-8"; 
    
    // Used to decide what encoding to use while reading zip entries 
    private static final String fileEncoding = java.security.AccessController
        .doPrivileged(new GetPropertyAction("sun.zip.encoding"));


    /**
     * Check to make sure that this stream has not been closed
     */
    private void ensureOpen() throws IOException {
	if (closed) {
	    throw new IOException("Stream closed");
        }
    }

    /**
     * Creates a new ZIP input stream.
     * @param in the actual input stream
     */
    public CZipInputStream(InputStream in,String encoding) {
	super(new PushbackInputStream(in, 20480), new Inflater(true), 20480);
        usesDefaultInflater = true;
        if(in == null) {
            throw new NullPointerException("in is null");
        }
        this.encoding=encoding;
    }

    /**
     * Reads the next ZIP file entry and positions the stream at the
     * beginning of the entry data.
     * @return the next ZIP file entry, or null if there are no more entries
     * @exception ZipException if a ZIP file error has occurred
     * @exception IOException if an I/O error has occurred
     */
    public ZipEntry getNextEntry() throws IOException {
        ensureOpen();
	if (entry != null) {
	    closeEntry();
	}
	crc.reset();
	inf.reset();
	if ((entry = readLOC()) == null) {
	    return null;
	}
	if (entry.method == STORED) {
	    remaining = entry.size;
	}
        entryEOF = false;
	return entry;
    }

    /**
     * Closes the current ZIP entry and positions the stream for reading the
     * next entry.
     * @exception ZipException if a ZIP file error has occurred
     * @exception IOException if an I/O error has occurred
     */
    public void closeEntry() throws IOException {
        ensureOpen();
	while (read(tmpbuf, 0, tmpbuf.length) != -1) ;
        entryEOF = true;
    }

    /**
     * Returns 0 after EOF has reached for the current entry data,
     * otherwise always return 1.
     * <p>
     * Programs should not count on this method to return the actual number
     * of bytes that could be read without blocking.
     *
     * @return     1 before EOF and 0 after EOF has reached for current entry.
     * @exception  IOException  if an I/O error occurs.
     *
     */
    public int available() throws IOException {
        ensureOpen();
        if (entryEOF) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Reads from the current ZIP entry into an array of bytes.
     * If <code>len</code> is not zero, the method
     * blocks until some input is available; otherwise, no
     * bytes are read and <code>0</code> is returned.
     * @param b the buffer into which the data is read
     * @param off the start offset in the destination array <code>b</code>
     * @param len the maximum number of bytes read
     * @return the actual number of bytes read, or -1 if the end of the
     *         entry is reached
     * @exception  NullPointerException If <code>b</code> is <code>null</code>.
     * @exception  IndexOutOfBoundsException If <code>off</code> is negative,
     * <code>len</code> is negative, or <code>len</code> is greater than
     * <code>b.length - off</code>
     * @exception ZipException if a ZIP file error has occurred
     * @exception IOException if an I/O error has occurred
     */
    public int read(byte[] b, int off, int len) throws IOException {
        ensureOpen();
        if (off < 0 || len < 0 || off > b.length - len) {
	    throw new IndexOutOfBoundsException();
	} else if (len == 0) {
	    return 0;
	}

	if (entry == null) {
	    return -1;
	}
	switch (entry.method) {
	case DEFLATED:
	    len = super.read(b, off, len);
	    if (len == -1) {
		readEnd(entry);
                entryEOF = true;
		entry = null;
	    } else {
		crc.update(b, off, len);
	    }
	    return len;
	case STORED:
	    if (remaining <= 0) {
                entryEOF = true;
		entry = null;
		return -1;
	    }
	    if (len > remaining) {
		len = (int)remaining;
	    }
	    len = in.read(b, off, len);
	    if (len == -1) {
		throw new ZipException("unexpected EOF");
	    }
	    crc.update(b, off, len);
	    remaining -= len;
	    if (remaining == 0 && entry.crc != crc.getValue()) {
		throw new ZipException(
		    "invalid entry CRC (expected 0x" + Long.toHexString(entry.crc) +
		    " but got 0x" + Long.toHexString(crc.getValue()) + ")");
	    }
	    return len;
	default:
	    throw new ZipException("invalid compression method");
	}
    }

    /**
     * Skips specified number of bytes in the current ZIP entry.
     * @param n the number of bytes to skip
     * @return the actual number of bytes skipped
     * @exception ZipException if a ZIP file error has occurred
     * @exception IOException if an I/O error has occurred
     * @exception IllegalArgumentException if n < 0
     */
    public long skip(long n) throws IOException {
        if (n < 0) {
            throw new IllegalArgumentException("negative skip length");
        }
        ensureOpen();
	int max = (int)Math.min(n, Integer.MAX_VALUE);
	int total = 0;
	while (total < max) {
	    int len = max - total;
	    if (len > tmpbuf.length) {
		len = tmpbuf.length;
	    }
	    len = read(tmpbuf, 0, len);
	    if (len == -1) {
                entryEOF = true;
		break;
	    }
	    total += len;
	}
	return total;
    }

    /**
     * Closes this input stream and releases any system resources associated
     * with the stream.
     * @exception IOException if an I/O error has occurred
     */
    public void close() throws IOException {
        if (!closed) {
	    super.close();
            closed = true;
        }
    }

    private byte[] b = new byte[256];

    /*
     * Reads local file (LOC) header for next entry.
     */
    private ZipEntry readLOC() throws IOException {
	try {
	    readFully(tmpbuf, 0, LOCHDR);
	} catch (EOFException e) {
	    return null;
	}
	if (get32(tmpbuf, 0) != LOCSIG) {
	    return null;
	}
	// get the entry name and create the ZipEntry first
	int len = get16(tmpbuf, LOCNAM);
        int blen = b.length;
        if (len > blen) {
            do
                blen = blen * 2;
            while (len > blen);
            b = new byte[blen];
        }
	readFully(b, 0, len);
//	String name = getFileName(b, len);

//        ZipEntry e = createZipEntry(name);
	
	ZipEntry e=null; 
	try 
	{ 
		if (this.encoding.toUpperCase().equals("UTF-8")) 
			e=createZipEntry(getUTF8String(b, 0, len)); 
		else 
			e=createZipEntry(new String(b,0,len,this.encoding)); 
	} 
	catch(Exception byteE) 
	{ 
		e=createZipEntry(getUTF8String(b, 0, len)); 
	} 	
	
	// now get the remaining fields for the entry
	flag = get16(tmpbuf, LOCFLG);
	if ((flag & 1) == 1) {
	    throw new ZipException("encrypted ZIP entry not supported");
	}
	e.method = get16(tmpbuf, LOCHOW);
	e.time = get32(tmpbuf, LOCTIM);
	if ((flag & 8) == 8) {
	    /* "Data Descriptor" present */
	    if (e.method != DEFLATED) {
		throw new ZipException(
			"only DEFLATED entries can have EXT descriptor");
	    }
	} else {
	    e.crc = get32(tmpbuf, LOCCRC);
	    e.csize = get32(tmpbuf, LOCSIZ);
	    e.size = get32(tmpbuf, LOCLEN);
	}
	len = get16(tmpbuf, LOCEXT);
	if (len > 0) {
	    byte[] bb = new byte[len];
	    readFully(bb, 0, len);
	    e.setExtra(bb);
	}
	return e;
    }

    /*
     * Fetches a UTF8-encoded String from the specified byte array.
     */
    private static String getUTF8String(byte[] b, int off, int len) {
	// First, count the number of characters in the sequence
	int count = 0;
	int max = off + len;
	int i = off;
	while (i < max) {
	    int c = b[i++] & 0xff;
	    switch (c >> 4) {
	    case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
		// 0xxxxxxx
		count++;
		break;
	    case 12: case 13:
		// 110xxxxx 10xxxxxx
		if ((int)(b[i++] & 0xc0) != 0x80) {
		    throw new IllegalArgumentException();
		}
		count++;
		break;
	    case 14:
		// 1110xxxx 10xxxxxx 10xxxxxx
		if (((int)(b[i++] & 0xc0) != 0x80) ||
		    ((int)(b[i++] & 0xc0) != 0x80)) {
		    throw new IllegalArgumentException();
		}
		count++;
		break;
	    default:
		// 10xxxxxx, 1111xxxx
		throw new IllegalArgumentException();
	    }
	}
	if (i != max) {
	    throw new IllegalArgumentException();
	}
	// Now decode the characters...
	char[] cs = new char[count];
	i = 0;
	while (off < max) {
	    int c = b[off++] & 0xff;
	    switch (c >> 4) {
	    case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
		// 0xxxxxxx
		cs[i++] = (char)c;
		break;
	    case 12: case 13:
		// 110xxxxx 10xxxxxx
		cs[i++] = (char)(((c & 0x1f) << 6) | (b[off++] & 0x3f));
		break;
	    case 14:
		// 1110xxxx 10xxxxxx 10xxxxxx
		int t = (b[off++] & 0x3f) << 6;
		cs[i++] = (char)(((c & 0x0f) << 12) | t | (b[off++] & 0x3f));
		break;
	    default:
		// 10xxxxxx, 1111xxxx
		throw new IllegalArgumentException();
	    }
	}
	return new String(cs, 0, count);
    }

    /**
     * Creates a new <code>ZipEntry</code> object for the specified
     * entry name.
     *
     * @param name the ZIP file entry name
     * @return the ZipEntry just created
     */
    protected ZipEntry createZipEntry(String name) {
    	return new ZipEntry(name);
    }

    /*
     * Reads end of deflated entry as well as EXT descriptor if present.
     */
    private void readEnd(ZipEntry e) throws IOException {
	int n = inf.getRemaining();
	if (n > 0) {
	    ((PushbackInputStream)in).unread(buf, len - n, n);
	}
	if ((flag & 8) == 8) {
	    /* "Data Descriptor" present */
	    readFully(tmpbuf, 0, EXTHDR);
	    long sig = get32(tmpbuf, 0);
            if (sig != EXTSIG) { // no EXTSIG present
                e.crc = sig;
                e.csize = get32(tmpbuf, EXTSIZ - EXTCRC);
                e.size = get32(tmpbuf, EXTLEN - EXTCRC);
                ((PushbackInputStream)in).unread(
                                           tmpbuf, EXTHDR - EXTCRC - 1, EXTCRC);
            } else {
                e.crc = get32(tmpbuf, EXTCRC);
                e.csize = get32(tmpbuf, EXTSIZ);
                e.size = get32(tmpbuf, EXTLEN);
            }
	}
	if (e.size != inf.getBytesWritten()) {
	    throw new ZipException(
		"invalid entry size (expected " + e.size +
		" but got " + inf.getBytesWritten() + " bytes)");
	}
	if (e.csize != inf.getBytesRead()) {
	    throw new ZipException(
		"invalid entry compressed size (expected " + e.csize +
		" but got " + inf.getBytesRead() + " bytes)");
	}
	if (e.crc != crc.getValue()) {
	    throw new ZipException(
		"invalid entry CRC (expected 0x" + Long.toHexString(e.crc) +
		" but got 0x" + Long.toHexString(crc.getValue()) + ")");
	}
    }

    /*
     * Reads bytes, blocking until all bytes are read.
     */
    private void readFully(byte[] b, int off, int len) throws IOException {
	while (len > 0) {
	    int n = in.read(b, off, len);
	    if (n == -1) {
		throw new EOFException();
	    }
	    off += n;
	    len -= n;
	}
    }

    /*
     * Fetches unsigned 16-bit value from byte array at specified offset.
     * The bytes are assumed to be in Intel (little-endian) byte order.
     */
    private static final int get16(byte b[], int off) {
	return (b[off] & 0xff) | ((b[off+1] & 0xff) << 8);
    }

    /*
     * Fetches unsigned 32-bit value from byte array at specified offset.
     * The bytes are assumed to be in Intel (little-endian) byte order.
     */
    private static final long get32(byte b[], int off) {
	return get16(b, off) | ((long)get16(b, off+2) << 16);
    }

    @SuppressWarnings("unused")
	private String getFileName(byte[] b, int len) throws IOException {
	String name;
	try {
	    if (fileEncoding == null || fileEncoding.equals("")) {
		name = getUTF8String(b, 0, len);
	    } else {
		if (fileEncoding.equalsIgnoreCase("default")) {
	   	    // use platforms's default encoding
		    name = new String(b, 0, len);
		} else {
		    try {
		    	name = new String(b, 0, len, fileEncoding);
	  	    } catch (UnsupportedEncodingException UEE) {
			throw new ZipException("Unable to encode entry " +
			    "name (sun.zip.encoding is " + fileEncoding + ")");
		    }
		}
	    }
	} catch (IllegalArgumentException e) {
	    // Alt encoding that can be used  if
	    // the initial decoding attempt fails.
	    String altEncoding = java.security.AccessController
	  	.doPrivileged(new GetPropertyAction("sun.zip.altEncoding"));
	    if (altEncoding == null || altEncoding.equals("") ) {
            	throw e;
            }
	    if (altEncoding.equalsIgnoreCase("default")) {
		// use platform's default encoding
		name = new String(b, 0, len);
	    } else {
		try {
		    // use the specified encoding
		    name = new String(b, 0, len, altEncoding);
		} catch (UnsupportedEncodingException UEE) {
		    throw new ZipException("Unable to encode entry " +
			"name (sun.zip.altEncoding is " + altEncoding + ")");
		}
	    }
		
	}
	return name;
    } 
}
