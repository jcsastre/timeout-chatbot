package com.timeout.chatbot.block.template.generic.element;

import com.cloudinary.Cloudinary;
import com.timeout.chatbot.configuration.TimeoutConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GenericTemplateElementFilmHelper extends GenericTemplateElementHelper {

    @Autowired
    public GenericTemplateElementFilmHelper(
        Cloudinary cloudinary,
        RestTemplate restTemplate,
        TimeoutConfiguration timeoutConfiguration
    ) {
        super(cloudinary, restTemplate, timeoutConfiguration);
    }

//    public List<Button> buildButtons(
//        PageItem pageItem
//    ) {
//        final GraffitiFilmResponse graffitiFilmResponse =
//            restTemplate.getForObject(
//                pageItem.getUrl(),
//                GraffitiFilmResponse.class
//            );
//
//        final String url = graffitiFilmResponse.getBody().getTrailer().getUrl();
//        if (url != null) {
//            final Button.ListBuilder buttonsBuilder = Button.newListBuilder();
//
//            buttonsBuilder.addUrlButton(
//                "See trailer",
//                url
//            ).toList();
//
//            buttonsBuilder.addPostbackButton(
//                "Find a cinema",
//                new JSONObject()
//                    .put("type", PayloadType.films_find_cinemas)
//                    .toString()
//            ).toList();
//
//            buttonsBuilder.addShareButton().toList();
//
//            return
//                buttonsBuilder.build();
//        }
//
//        return null;
//    }
}
