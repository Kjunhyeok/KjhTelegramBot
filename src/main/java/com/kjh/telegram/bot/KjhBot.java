package com.kjh.telegram.bot;

import com.kjh.telegram.baseball.BaseballGame;
import com.kjh.telegram.baseball.BaseballReceiver;
import com.kjh.telegram.exception.CaptionIsEmptyException;
import com.kjh.telegram.file.KjhFile;
import com.kjh.telegram.file.exception.FileDeleteFailException;
import com.kjh.telegram.naverapi.translate.Translation;
import com.kjh.telegram.naverapi.cloud.Maps;
import com.kjh.telegram.naverapi.search.Dictionary;
import com.kjh.telegram.naverapi.search.WebDocument;
import kong.unirest.json.JSONException;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Slf4j
public class KjhBot extends TelegramLongPollingBot {

    private String botName;
    private String botToken;
    private Message message;

    public KjhBot(String botName, String botToken){
        this.botName = botName;
        this.botToken = botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        message = update.getMessage();
        log.info("{}", message.toString());
        try {
            if (message.hasPhoto() || message.hasDocument()) {
                onFileCommand();
            } else {
                String text = message.getText().replaceFirst("/", "");
                if (message.getText().startsWith("/") && update.hasMessage() && update.getMessage().hasText()) {
                    onTextCommand(text);
                }
            }
        } catch (TelegramApiException e) {
            log.error("Telegram Api Exception", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void sendMessage(String text) throws TelegramApiException {
        sendMessage(text, false);
    }

    public void sendMessage(String text, boolean isHtml) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(text);
        sendMessage.enableHtml(isHtml);
        execute(sendMessage);
    }

    public void sendPhoto(InputFile file) throws TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId());
        sendPhoto.setPhoto(file);
        execute(sendPhoto);
    }

    public void sendVideo(InputFile file) throws TelegramApiException {
        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(message.getChatId());
        sendVideo.setVideo(file);
        execute(sendVideo);
    }

    public void sendDocument(InputFile file) throws TelegramApiException {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(message.getChatId());
        sendDocument.setDocument(file);
        execute(sendDocument);
    }

    private GetFile saveFile(Message message){
        String id = "";
        if(message.hasPhoto()) {
            List<PhotoSize> photoSizeList = message.getPhoto();
            PhotoSize photo = photoSizeList.get(photoSizeList.size() - 1);
            id = photo.getFileId();
        } else if(message.hasDocument()){
            Document document = message.getDocument();
            id = document.getFileId();
        }
        GetFile getFile = new GetFile();
        getFile.setFileId(id);
        if (message.getCaption() == null || message.getCaption().equals("")) {
            throw new CaptionIsEmptyException("파일 설명을 적어주세요.");
        }
        return getFile;
    }

    /**
     * 텍스트 명령어가 올 시 수행
     * @param text 명령어
     * @throws TelegramApiException exception
     */
    private void onTextCommand(String text) throws TelegramApiException {
        if (text.startsWith("파일")) {
            text = text.replace("파일", "");
            try {
                String name = text.substring(text.lastIndexOf(" ") + 1);
                KjhFile kjhFile = new KjhFile(name, message.getFrom().getId().intValue());
                if(text.equals("")){
                    sendMessage("저장된 파일 리스트\n\n" + kjhFile.getFileList());
                } else if(text.startsWith("삭제")) {
                    if(!kjhFile.deleteFile())
                        throw new FileDeleteFailException();

                    sendMessage("삭제가 완료되었습니다");
                } else {
                    InputFile file = kjhFile.getFile();
                    if(kjhFile.isPhoto())
                        sendPhoto(file);
                    else if(kjhFile.isVideo())
                        sendVideo(file);
                    else
                        sendDocument(file);
                }
            } catch (IOException e) {
                String str;
                if(e instanceof FileNotFoundException){
                    str = "파일을 찾을 수 없습니다.";
                } else if(e instanceof FileDeleteFailException){
                    str = "삭제에 실패 하였습니다.";
                } else {
                    str = "형식이 올바르지 않습니다.";
                }
                sendMessage(str);
            }
        } else if(text.startsWith("지도")){
            try {
                java.io.File mapFile = Maps.get(text.replace("지도 ", ""));
                sendPhoto(new InputFile(mapFile));
                if(!mapFile.delete())
                    log.warn("Map file delete fail");
            } catch (IOException | JSONException e){
                sendMessage("검색 결과를 찾을 수 없습니다.");
            }
        } else {
            String sendText;
            boolean isHtml = false;
            if (text.equals("누구")) {
                User user = message.getFrom();
                sendText = "넌 " + user.getLastName() + user.getFirstName() + "이야";
            } else if (text.startsWith("숫자야구"))
                sendText = new BaseballReceiver().check(text);
            else if (BaseballGame.isStart() && text.matches("^[0-9]*$"))
                sendText = BaseballGame.verify(text);
            else if (text.startsWith("기사"))
                sendText = WebDocument.search(text.replaceFirst("기사 ", ""));
            else if (text.startsWith("사전")) {
                isHtml = true;
                sendText = Dictionary.search(text.replaceFirst("사전 ", ""));
            } else if (Translation.Language.contains(text.substring(0, 1))
                    && Translation.Language.contains(text.substring(1, 2)))
                sendText = Translation.translate(text);
            else
                sendText = text;

            sendMessage(sendText, isHtml);
        }
    }

    /**
     * 봇으로 파일을 올릴 경우 수행
     * @throws TelegramApiException exception
     */
    private void onFileCommand() throws TelegramApiException {
        try {
            GetFile getFile = saveFile(message);
            File photoFile = execute(getFile);
            java.io.File file = downloadFile(photoFile);
            String path = photoFile.getFilePath();
            String ext = path.substring(path.lastIndexOf("."));
            KjhFile kjhFile = new KjhFile(message.getCaption(), message.getFrom().getId().intValue());
            kjhFile.saveFile(file, ext);
            sendMessage("파일 저장이 완료되었습니다.");
        } catch (IOException e) {
            sendMessage(e.getMessage());
            log.error("파일 저장 실패 : ", e);
        }
    }

}
