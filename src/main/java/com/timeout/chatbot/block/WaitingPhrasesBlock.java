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
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                isCancelled = true;
                e.printStackTrace();
            }

            if (!isCancelled) {
                try {
                    msc.sendTextMessage(
                        userMessengerId,
                        "Please, wait a moment"
                    );
                    senderActionsHelper.typingOn(userMessengerId);
                } catch (MessengerApiException | MessengerIOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancel() {
        isCancelled = true;
    }
}
