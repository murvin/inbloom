package com.uikit.globalization;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

class Bundle {

    long offsetTableEntry[];
    byte[] raw_data;
    String locale;
    final long LASTRESOURCE_ID = 0xffffffff80000000L;
    final int LASTRESOURCE_TYPE = 0;

    public Bundle(String locale) {
        this.locale = locale;
    }

    String getStringResource(int id) throws UnsupportedEncodingException {
        int length = getResourceLength(id);
        int offset = getResourceOffset(getEntry(id));

        byte[] content = new byte[length];
        for (int j = 0; j < content.length; j++) {
            content[j] = raw_data[offset + j];
        }
        return new String(content, "UTF-8");
    }

    byte[] getResource(int id) {
        int length = getResourceLength(id);
        if (length < 0) {
            return null;
        }
        int offset = getResourceOffset(getEntry(id));

        byte[] content = new byte[length];
        for (int j = 0; j < content.length; j++) {
            content[j] = raw_data[offset + j];
        }
        return content;
    }

    long getEntry(int id) {
        int index = getEntryIndex(id);
        return index <= -1 ? 0L : offsetTableEntry[index];
    }

    int getEntryIndex(int id) {
        for (int i = 0; i < offsetTableEntry.length; i++) {
            if (id == getResourceId(offsetTableEntry[i])) {
                return i;
            }
        }

        return -1;
    }

    int getResourceId(long resource) {
        return (int) (resource >>> 32 & -1L);
    }

    boolean isSigValid(byte[] sig) {
        return (((sig[0] << 24 | sig[1] << 16 | sig[2] << 8 | 0) == 0xee4d4900) && sig[3] == 0x10);
    }

    int getResourceLength(int id) {
        int index = getEntryIndex(id);
        return (index == -1 ? index : getResourceOffset(offsetTableEntry[index + 1]) - getResourceOffset(offsetTableEntry[index]));
    }

    byte getResourceType(long resource) {
        return (byte) (int) (resource >> 24 & 0xffL);
    }

    int getResourceOffset(long resource) {
        return (int) (resource & 0xffffffL);
    }

    public void deSerialise(InputStream is) throws IOException {
        int rsFileLength = is.available();
        int offset = 0;
        raw_data = new byte[rsFileLength];

        is.read(raw_data);

        if (!isSigValid(raw_data)) {
            System.out.println("signature not valid");
            return;
        }

        offset += 4;

        int headerLength = readInt(raw_data, offset);
        offset += 4;

        int resourceCount = headerLength / 8;

        if (resourceCount == 0) {
            return;
        }

        offsetTableEntry = new long[resourceCount];
        offsetTableEntry[0] = readLong(raw_data, offset);
        offset += 8;

        /**
        bits 63...32     bits 31...0
        |----------------|---------------|
        resource ID    type   offset
        |-----------|----|
         */
        for (int i = 1; i < resourceCount - 1; i++) {
            offsetTableEntry[i] = readLong(raw_data, offset);
            offset += 8;
        }

        if (resourceCount > 1) {
            offsetTableEntry[resourceCount - 1] = readLong(raw_data, offset);
            offset += 8;
        }

        if ((long) getResourceId(offsetTableEntry[resourceCount - 1]) != LASTRESOURCE_ID || getResourceType(offsetTableEntry[resourceCount - 1]) != LASTRESOURCE_TYPE) {
            throw new IllegalArgumentException();
        }

        validateResource(headerLength, resourceCount, rsFileLength);

    }

    private void validateResource(int headerLength, int resourceCount, int rsFileLength) {

        int absDataOffset = headerLength + 8;
        int length = 0;
        int nextOffset = 0;
        int offset = 0;
        long lastId = 0L;
        boolean nextNonEmptyFails = false;
        if (rsFileLength == 0) {
            rsFileLength = (int) offsetTableEntry[resourceCount - 1] & 0xffffff;
        }
        for (int i = 0; i < resourceCount - 1; i++) {
            long id = (offsetTableEntry[i] & 0xffffffff00000000L) >> 32;
            if (id < 0L || id > 0x7fffffffL) {
                throw new IllegalArgumentException("Invalid resource id ");
            }
            if (id < lastId) {
                throw new IllegalArgumentException("Resource ids not in ascending order.");
            }
            offset = (int) offsetTableEntry[i] & 0xffffff;
            nextOffset = (int) offsetTableEntry[i + 1] & 0xffffff;
            length = nextOffset - offset;

            if (offset >= rsFileLength || offset < absDataOffset) {
                if (length == 0 && offset == rsFileLength) {
                    nextNonEmptyFails = true;
                } else {
                    throw new IllegalArgumentException("Invalid resource offset.");
                }
            }
            if (length < 0) {
                throw new IllegalArgumentException("Invalid resource size");
            }
            if (length > 0 && nextNonEmptyFails) {
                throw new IllegalArgumentException("Invalid resource offset.");
            }
            lastId = id;
        }
    }

    final int readInt(byte[] bytes, int offset) {
        return ((bytes[0 + offset] & 0xff) << 24
                | (bytes[1 + offset] & 0xff) << 16
                | (bytes[2 + offset] & 0xff) << 8
                | (bytes[3 + offset]) & 0xff);

    }

    final long readLong(byte[] readBuffer, int offset) {
        return ((long) readBuffer[0 + offset] << 56)
                + ((long) (readBuffer[1 + offset] & 0xff) << 48)
                + ((long) (readBuffer[2 + offset] & 0xff) << 40)
                + ((long) (readBuffer[3 + offset] & 0xff) << 32)
                + ((long) (readBuffer[4 + offset] & 0xff) << 24)
                + (long) ((readBuffer[5 + offset] & 0xff) << 16)
                + (long) ((readBuffer[6 + offset] & 0xff) << 8)
                + (long) ((readBuffer[7 + offset] & 0xff) << 0);
    }
}
