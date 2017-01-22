package com.timeout.chatbot.services;

import com.timeout.chatbot.graffitti.uri.GraffittiEndpoints;
import com.timeout.chatbot.graffitti.domain.response.facets.CategoryPrimary;
import com.timeout.chatbot.graffitti.domain.response.facets.CategorySecondary;
import com.timeout.chatbot.graffitti.domain.response.facets.FacetGroup;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class GraffittiService {
    private final RestTemplate restTemplate;

    private final FacetGroup facetGroup;
//    private final List<CategoryPrimary> categories;

    public GraffittiService(
        RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;

        facetGroup = this.restTemplate.getForObject(GraffittiEndpoints.FACETS.toString(), FacetGroup.class);
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
}
