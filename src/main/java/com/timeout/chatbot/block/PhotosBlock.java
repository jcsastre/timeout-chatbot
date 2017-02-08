package com.timeout.chatbot.block;

import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.Recipient;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.timeout.chatbot.block.quickreply.QuickReplyBuilderHelper;
import com.timeout.chatbot.graffitti.response.images.GraffittiImage;
import com.timeout.chatbot.graffitti.response.venue.GraffittiVenueResponse;
import com.timeout.chatbot.messenger4j.send.MessengerSendClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhotosBlock {

    private final MessengerSendClientWrapper messengerSendClientWrapper;
    private final QuickReplyBuilderHelper quickReplyBuilderHelper;

    @Autowired
    public PhotosBlock(
        MessengerSendClientWrapper messengerSendClientWrapper,
        QuickReplyBuilderHelper quickReplyBuilderHelper
    ) {
        this.messengerSendClientWrapper = messengerSendClientWrapper;
        this.quickReplyBuilderHelper = quickReplyBuilderHelper;
    }

    public void send(
        String userId,
        GraffittiVenueResponse graffittiVenueResponse,
        List<GraffittiImage> graffittiImages
    ) {
        if (graffittiImages.size() > 10) {
            graffittiImages = graffittiImages.subList(0, 10);
        }

        final GenericTemplate.Builder genericTemplateBuilder = GenericTemplate.newBuilder();
        final GenericTemplate.Element.ListBuilder listBuilder = genericTemplateBuilder.addElements();
        for (GraffittiImage graffittiImage : graffittiImages) {
            String title = graffittiImage.getAltText();
            if (title == null) {
                title = graffittiImage.getTitle();
                if (title == null) {
                    title = " ";
                }
            }

            listBuilder.addElement(title).imageUrl(graffittiImage.getUrl()).toList().done();
        }
        final GenericTemplate genericTemplate = genericTemplateBuilder.build();

        messengerSendClientWrapper.sendTemplate(
            Recipient.newBuilder().recipientId(userId).build(),
            NotificationType.REGULAR,
            genericTemplate,
            quickReplyBuilderHelper.buildForSeeVenueItem(graffittiVenueResponse)
        );
    }
}
