package com.library.java.config;


import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class InMemoryMultipartFile implements MultipartFile {
    private final String name;
    private final String originalFileName;
    private final String contentType;
    private final byte[] payload;

    public InMemoryMultipartFile(String name, String originalFileName, String contentType, byte[] payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null.");
        }
        this.name = name;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.payload = payload;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFileName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return payload.length == 0;
    }

    @Override
    public long getSize() {
        return payload.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return payload;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(payload);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(payload);
    }
}
