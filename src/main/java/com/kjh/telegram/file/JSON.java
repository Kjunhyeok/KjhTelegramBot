package com.kjh.telegram.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSON {

    private String filePath;

    public void write(String message) throws IOException {
        Files.write(Paths.get(filePath), message.getBytes());
    }
}
