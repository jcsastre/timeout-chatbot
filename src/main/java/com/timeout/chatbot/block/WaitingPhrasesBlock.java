package com.timeout.chatbot.block;

import com.github.messenger4j.exceptions.MessengerApiException;
import com.github.messenger4j.exceptions.MessengerIOException;
import com.github.messenger4j.send.MessengerSendClient;
import com.timeout.chatbot.messenger4j.SenderActionsHelper;

public class WaitingPhrasesBlock implements Runnable {

    private String userMessengerId;
    private MessengerSendClient msc;
    private Boolean isCancelled = false;
    private final SenderActionsHelper senderActionsHelper;
    private Integer counter = 0;

    public WaitingPhrasesBlock(
        String userMessengerId,
        MessengerSendClient msc,
        SenderActionsHelper senderActionsHelper
    ) {
        this.userMessengerId = userMessengerId;
        this.msc = msc;
        this.senderActionsHelper = senderActionsHelper;
    }

    @Override
    public void run() {

        while (!isCancelled) {

            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                isCancelled = true;
                e.printStackTrace();
            }

            senderActionsHelper.typingOff(userMessengerId);

            String text = null;
            String gifUrl = null;
            if (counter==0) {
                text = "Don't worry, I'm on it :)";
            } else if (counter==1) {
                text = "I'm still working on it :|";
//                gifUrl = "https://media.giphy.com/media/6hE5JyVPvivCg/giphy.gif";
            } else if (counter==2) {
                text = "Work in progress :)";
//                text = "I'm still working on it :|";
            } else if (counter==3) {
                text = "Don't worry, I'm on it :)";
//                gifUrl = "https://media.giphy.com/media/XIqCQx02E1U9W/giphy.gif";
            } else if (counter==4) {
                text = "I'm still working on it :|";
//                text = "Work in progress :)";
            } else if (counter==5) {
                text = "Work in progress :)";
//                gifUrl = "https://media.giphy.com/media/l0HlJR8AWpaS22XIs/giphy.gif";
            } else {
                text = "Don't worry, I'm on it :)";
//                gifUrl = " https://media.giphy.com/media/MIY4jpusckRmU/giphy.gif";
            }

            if (!isCancelled) {
                try {
                    if (text != null) {
                        senderActionsHelper.typingOn(userMessengerId);
                        msc.sendTextMessage(
                            userMessengerId,
                            text
                        );
                    }
                    if (gifUrl != null) {
                        senderActionsHelper.typingOn(userMessengerId);
                        msc.sendImageAttachment(
                            userMessengerId,
                            gifUrl
                        );
                    }
                    senderActionsHelper.typingOn(userMessengerId);
                } catch (MessengerApiException | MessengerIOException e) {
                    e.printStackTrace();
                }
            }

            counter++;
        }
    }

    public void cancel() {
        isCancelled = true;
    }
}
