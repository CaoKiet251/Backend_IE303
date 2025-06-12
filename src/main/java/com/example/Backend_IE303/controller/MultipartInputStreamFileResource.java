package com.example.Backend_IE303.controller;

import io.jsonwebtoken.io.IOException;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;

public class MultipartInputStreamFileResource extends InputStreamResource {
    private final String filename;
    public MultipartInputStreamFileResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }
    @Override
    public String getFilename() {
        return this.filename;
    }
    @Override
    public long contentLength() throws IOException {
        return -1;
    }
}
