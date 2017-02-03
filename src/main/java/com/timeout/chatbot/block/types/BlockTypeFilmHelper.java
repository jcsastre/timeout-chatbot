package com.timeout.chatbot.block.types;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.github.messenger4j.send.buttons.Button;
import com.timeout.chatbot.domain.payload.PayloadType;
import com.timeout.chatbot.graffitti.response.films.GraffitiFilmResponse;
import com.timeout.chatbot.graffitti.response.search.page.PageItem;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BlockTypeFilmHelper {

    private final Cloudinary cloudinary;

    public BlockTypeFilmHelper() {
        Map config = new HashMap();
        config.put("cloud_name", "drdyx3lpb");
        config.put("api_key", "414418524479882");
        config.put("api_secret", "iY0YAPSOo-xztbHp-42UO2BS_t4");
        cloudinary = new Cloudinary(config);
    }

    public String buildImageUrlForGenericTemplateElement(PageItem pageItem) {
        String name = pageItem.getName();
        name = name.replace(" ", "%20");

        final PageItem.Image image = pageItem.getImage();
        if (image != null) {
            final String imageId = image.getId();
            if (imageId != null) {
                String url = "http://media.timeout.com/images/" + imageId + "/320/210/image.jpg";

                Transformation transformation = new Transformation();
                transformation =
                    transformation.width(320).height(180).gravity("center").crop("crop").chain();
//                transformation =
//                    transformation.overlay("see_at_timeout").gravity("north_east").x(0.02).y(0.08).chain();

                final Integer editorialRating = pageItem.getEditorialRating();
                if (editorialRating != null) {
                    transformation =
                        transformation.overlay("rs" + editorialRating + "5").gravity("south_west").x(0.02).y(0.08);
                }

                url =
                    cloudinary.url()
                        .transformation(transformation)
                        .format("png")
                        .type("fetch")
                        .generate(url);

                System.out.println(url);
                return url;
            }
        }

        return null;
    }

    public String buildSubtitleForGenericTemplateElement(PageItem pageItem) {
        String subtitle = pageItem.getSummary();
        if (subtitle == null) {
            subtitle = pageItem.getDescription();
            if (subtitle == null) {
                subtitle = pageItem.getAnnotation();
            }
        }

        if (subtitle != null && subtitle.length() > 80) {
            subtitle = subtitle.substring(0, 77);
            subtitle = subtitle + "...";
        }

        return subtitle;
    }

    public List<Button> buildButtonsList(
        GraffitiFilmResponse graffitiFilmResponse
    ) {
        final String url = graffitiFilmResponse.getBody().getTrailer().getUrl();
        if (url != null) {
            System.out.println(url);

            final Button.ListBuilder buttonsBuilder = Button.newListBuilder();

            buttonsBuilder.addUrlButton(
                "See trailer",
                url
            ).toList();

            buttonsBuilder.addPostbackButton(
                "Find a cinema",
                new JSONObject()
                    .put("type", PayloadType.films_find_cinemas)
                    .toString()
            ).toList();

            buttonsBuilder.addShareButton().toList();

            return
                buttonsBuilder.build();
        }

        return null;
    }
}
