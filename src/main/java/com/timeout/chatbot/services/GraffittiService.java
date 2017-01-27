package com.timeout.chatbot.services;

import com.timeout.chatbot.graffitti.domain.response.facets.v5.GraffittiFacetV5Node;
import com.timeout.chatbot.graffitti.domain.response.facets.v5.GraffittiFacetV5Response;
import com.timeout.chatbot.graffitti.uri.GraffittiEndpoints;
import com.timeout.chatbot.graffitti.domain.response.facets.CategoryPrimary;
import com.timeout.chatbot.graffitti.domain.response.facets.CategorySecondary;
import com.timeout.chatbot.graffitti.domain.response.facets.FacetGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class GraffittiService {

    private final FacetGroup facetGroup;
    private final GraffittiFacetV5Response graffittiFacetV5Response;

    @Autowired
    public GraffittiService(
        RestTemplate restTemplate,
        GraffittiEndpoints graffittiEndpoints
    ) {
        facetGroup =
            restTemplate.getForObject(graffittiEndpoints.getFacetsV5(), FacetGroup.class);

        graffittiFacetV5Response =
            restTemplate.getForObject(graffittiEndpoints.getFacetsV5(), GraffittiFacetV5Response.class);
    }

    public List<CategoryPrimary> getPrimaryCategories() {
        return facetGroup.getBody().getFacets().getWhat().getPrimaryCategories();
    }

    public CategoryPrimary getCategoryPrimaryByName(String name) {
        for (CategoryPrimary categoryPrimary : getPrimaryCategories()) {
            if (categoryPrimary.getName().equals(name)) {
                return categoryPrimary;
            }
        }

        return null;
    }

    public CategoryPrimary getCategoryPrimaryById(String id) {
        for (CategoryPrimary categoryPrimary : getPrimaryCategories()) {
            if (categoryPrimary.getId().equals(id)) {
                return categoryPrimary;
            }
        }

        return null;
    }

    public List<CategorySecondary> getSecondaryCategories(CategoryPrimary primaryCategoryPrimary) {
        for (CategoryPrimary categoryPrimary : getPrimaryCategories()) {
            if (categoryPrimary.getId().equals(primaryCategoryPrimary.getId())) {
                return categoryPrimary.getSecondaryCategories();
            }

        }

        return null;
    }

    public GraffittiFacetV5Response getGraffittiFacetV5Response() {
        return graffittiFacetV5Response;
    }

    public GraffittiFacetV5Node getGraffittiFacetV5NodeById(String id) {
        for (GraffittiFacetV5Node parentNode : graffittiFacetV5Response.getBody().getFacets().getWhat().getChildren()) {
            for (GraffittiFacetV5Node childNode : parentNode.getChildren()) {
                if (childNode.getId().equalsIgnoreCase(id)) {
                    return childNode;
                }
            }
        }

        return null;
    }
}
