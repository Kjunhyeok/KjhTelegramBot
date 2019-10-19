package com.kjh.telegram.file;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class KjhFile {

    private final String filePath = "D:/telegrambotdata";

    private String fileName;
    private int userId;

    public KjhFile(String fileName, int userId){
        this.fileName = fileName;
        this.userId = userId;
    }

    public void saveFile(File file, String ext) throws IOException {
        String pathName = filePath + "/" + userId + fileName + ext;
        if (!file.renameTo(new java.io.File(pathName)))
            throw new IOException("파일 저장에 실패하였습니다.");
        if (!file.createNewFile())
            throw new IOException("파일 저장에 실패하였습니다.");
    }

    public boolean deleteFile() throws FileNotFoundException {
        return getFile().delete();
    }

    public String getFileList(){
        StringBuilder fileNameList = new StringBuilder();
        File[] files = getFiles();
        for (java.io.File file : files) {
            fileNameList.append(file.getName()).append("\n");
        }

       return fileNameList.toString()
                .replace(String.valueOf(userId), "");
    }

    public boolean isVideo() throws IOException {
        return getMime().contains("media");
    }

    public SendVideo getVideo() throws FileNotFoundException {
        return new SendVideo()
                .setVideo(getFile());
    }

    public boolean isPhoto() throws IOException {
        return getMime().contains("image");
    }

    public SendPhoto getPhoto() throws FileNotFoundException {
        return new SendPhoto()
                .setPhoto(getFile());
    }

    public SendDocument getDocument() throws FileNotFoundException {
        return new SendDocument()
                .setDocument(getFile());
    }

    private File[] getFiles(){
        return new java.io.File(filePath)
                .listFiles((dir, name) -> name.contains(userId + fileName));
    }

    public File getFile() throws FileNotFoundException {
        File[] files = getFiles();
        assert files != null;
        if(files.length == 0){
            throw new FileNotFoundException();
        }
        return files[0];
    }

    private String getMime() throws IOException {
        return Files.probeContentType(getFile().toPath());
    }
}
