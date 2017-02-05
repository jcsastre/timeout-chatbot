package com.timeout.chatbot.services;

import com.timeout.chatbot.domain.Neighborhood;
import com.timeout.chatbot.graffitti.response.facets.v4.GraffittiFacetV4FacetNode;
import com.timeout.chatbot.graffitti.response.facets.v4.GraffittiFacetV4Response;
import com.timeout.chatbot.graffitti.response.facets.v5.GraffittiFacetV5Node;
import com.timeout.chatbot.graffitti.response.facets.v5.GraffittiFacetV5Response;
import com.timeout.chatbot.graffitti.uri.GraffittiEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class GraffittiService {

    private final GraffittiFacetV5Response graffittiFacetV5Response;

//    private final List<Cuisine> cuisines;
//    private final List<Category> categories;

    final GraffittiFacetV4FacetNode facetWhatTree;
    final GraffittiFacetV4FacetNode facetWhatCategoriesRootNode;

    private final List<Neighborhood> neighborhoods;


    @Autowired
    public GraffittiService(
        RestTemplate restTemplate,
        GraffittiEndpoints graffittiEndpoints
    ) {
        graffittiFacetV5Response =
            restTemplate.getForObject(graffittiEndpoints.getFacetsV5(), GraffittiFacetV5Response.class);

        final GraffittiFacetV4Response facetV4Response =
            restTemplate.getForObject(graffittiEndpoints.getFacetsV4(), GraffittiFacetV4Response.class);

//        this.categories = populateCategories(facetV4Response);

//        this.cuisines = populateCuisines(facetV4Response);

        facetWhatTree = getFacetWhatTree(facetV4Response);
        facetWhatCategoriesRootNode = getNodeCategoriesFromFaceWhatTree(facetWhatTree);
        neighborhoods = buildNeighborhoodList(facetV4Response);
    }

    public List<Neighborhood> buildNeighborhoodList(
        GraffittiFacetV4Response v4Response
    ) {
        List<Neighborhood> neighborhoods = new ArrayList<>();

        for (GraffittiFacetV4FacetNode facet : v4Response.getBody().getFacets()) {
            if (facet.getId().equalsIgnoreCase("where")) {
                for (GraffittiFacetV4FacetNode facetWhereChild : facet.getChildren()) {
                    if(!facetWhereChild.getId().equalsIgnoreCase("canned-near_here")) {
                        neighborhoods.add(
                            new Neighborhood(
                                facetWhereChild.getId(),
                                facetWhereChild.getName()
                            )
                        );
                    }
                }
            }
        }

        return neighborhoods;
    }

    public Neighborhood getNeighborhoodByGraffittiId(String graffittiId) {
        for (Neighborhood neighborhood : neighborhoods) {
            if (neighborhood.getGraffitiId().equalsIgnoreCase(graffittiId)) {
                return neighborhood;
            }
        }

        return null;
    }

    public List<GraffittiFacetV5Node> getFacetsV5PrimaryCategories() {
        return
            graffittiFacetV5Response.getBody().getFacets().getWhat().getChildren();
    }

    //    public GraffittiFacetV5Node getCategoryPrimaryByName(String name) {
//        for (GraffittiFacetV5Node categoryPrimary : getFacetsV5PrimaryCategories()) {
//            if (categoryPrimary.getName().equals(name)) {
//                return categoryPrimary;
//            }
//        }
//
//        return null;
//    }

//    public GraffittiFacetV5Node getCategoryPrimaryById(String id) {
//        for (GraffittiFacetV5Node categoryPrimary : getFacetsV5PrimaryCategories()) {
//            if (categoryPrimary.getId().equals(id)) {
//                return categoryPrimary;
//            }
//        }
//
//        return null;
//    }

//    public List<CategorySecondary> getSecondaryCategories(CategoryPrimary primaryCategoryPrimary) {
//        for (CategoryPrimary categoryPrimary : getFacetsV5PrimaryCategories()) {
//            if (categoryPrimary.getId().equals(primaryCategoryPrimary.getId())) {
//                return categoryPrimary.getSecondaryCategories();
//            }
//
//        }
//
//        return null;
//    }

//    public GraffittiFacetV5Response getGraffittiFacetV5Response() {
//        return graffittiFacetV5Response;
//    }

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

    public GraffittiFacetV5Node getGraffittiFacetV5NodeByIdByName(String name) {
        for (GraffittiFacetV5Node parentNode : graffittiFacetV5Response.getBody().getFacets().getWhat().getChildren()) {
            for (GraffittiFacetV5Node childNode : parentNode.getChildren()) {
                if (childNode.getName().equalsIgnoreCase(name)) {
                    return childNode;
                }
            }
        }

        return null;
    }

//    public List<GraffittiFacetV5Node> getWhatChildren(
//        GraffittiFacetV5Node parentWhat,
//        Integer pageNumber
//    ) {
//        List<GraffittiFacetV5Node> subcategories = new ArrayList<>();
//
//        if (parentWhat.getChildren() != null) {
//            for (GraffittiFacetV5Node subcategory : parentWhat.getChildren()) {
//                subcategories.add(subcategory);
//            }
//        }
//
//        return subcategories;
//    }

//    private List<Cuisine> populateCuisines(GraffittiFacetV4Response v4Response) {
//        List<Cuisine> cuisines = new ArrayList<>();
//
//        final GraffittiFacetV4FacetNode restaurantsTree = getNodeRestaurantsOnNodeCategories(facetWhatCategoriesRootNode);
//
//        for (GraffittiFacetV4FacetNode child : restaurantsTree.getChildren()) {
//            if (child.getCount()>0) {
//                cuisines.add(
//                    new Cuisine(
//                        child.getId(),
//                        child.getName()
//                    )
//                );
//                if (child.getChildren()!=null && child.getChildren().size()>0) {
//                    for (GraffittiFacetV4FacetNode childOfChild : child.getChildren()) {
//                        if (childOfChild.getCount()>0) {
//                            cuisines.add(
//                                new Cuisine(
//                                    childOfChild.getId(),
//                                    childOfChild.getName()
//                                )
//                            );
//                        }
//                    }
//                }
//            }
//        }
//
//        return cuisines;
//    }

//    private List<Category> populateCategories(GraffittiFacetV4Response v4Response) {
//        List<Category> categories = new ArrayList<>();
//
//        final GraffittiFacetV4FacetNode facetWhat = getFacetWhatTree(v4Response);
//        final GraffittiFacetV4FacetNode nodeCategoriesOnFaceWhat = getNodeCategoriesFromFaceWhatTree(facetWhat);
//
//        final List<GraffittiFacetV4FacetNode> categoryNodes = nodeCategoriesOnFaceWhat.getChildren();
//        for (GraffittiFacetV4FacetNode categoryNode : categoryNodes) {
//            if (categoryNode.getCount()> 0) {
//                List<Subcategory> subcategories = new ArrayList<>();
//                for (GraffittiFacetV4FacetNode subcategoryNode : categoryNode.getChildren()) {
//                    if (subcategoryNode.getCount()> 0) {
//                        subcategories.add(
//                            new Subcategory(
//                                subcategoryNode.getId(),
//                                subcategoryNode.getName(),
//                                subcategoryNode.getConcept().getName()
//                            )
//                        );
//                        if (subcategoryNode.getChildren() != null && subcategoryNode.getChildren().size() > 0) {
//                            for (GraffittiFacetV4FacetNode subcategoryNodeChild : subcategoryNode.getChildren()) {
//                                if (subcategoryNodeChild.getCount()>0) {
//                                    subcategories.add(
//                                        new Subcategory(
//                                            subcategoryNode.getId(),
//                                            subcategoryNode.getName(),
//                                            subcategoryNode.getConcept().getName()
//                                        )
//                                    );
//                                }
//                            }
//                        }
//                    }
//                }
//
//                categories.add(
//                    new Category(
//                        categoryNode.getId(),
//                        categoryNode.getName(),
//                        categoryNode.getConcept().getName(),
//                        subcategories
//                    )
//                );
//            }
//        }
//
//        return categories;
//    }

    public GraffittiFacetV4FacetNode findNodeInfacetWhatCategoriesRootNode(String id) {
        return
            findRecursiveInWhatCategoryNodeById(
                id,
                facetWhatCategoriesRootNode
            );
    }

    private GraffittiFacetV4FacetNode findRecursiveInWhatCategoryNodeById(
        String id,
        GraffittiFacetV4FacetNode node
    ) {
        if (node.getId().equalsIgnoreCase(id)) {
            return node;
        }

        GraffittiFacetV4FacetNode foundNode = null;
        final List<GraffittiFacetV4FacetNode> children = node.getChildren();
        if (children!=null) {
            final int size = children.size();
            if (size>0) {
                for (int i = 0; foundNode == null && i < size; i++) {
                    foundNode = findRecursiveInWhatCategoryNodeById(id, children.get(i));
                }
            }
        }
        return foundNode;
    }

    public GraffittiFacetV4FacetNode findWhatCategoryNodeByConceptName(String conceptName) {
        for (GraffittiFacetV4FacetNode node : this.facetWhatCategoriesRootNode.getChildren()) {
            if (node.getConcept().getName().equalsIgnoreCase(conceptName)) {
                return node;
            }
        }

        return null;
    }

    private GraffittiFacetV4FacetNode getFacetWhatTree(GraffittiFacetV4Response v4Response) {
        for (GraffittiFacetV4FacetNode facet : v4Response.getBody().getFacets()) {
            if (facet.getId().equalsIgnoreCase("what")) {
                return facet;
            }
        }
        return null;
    }

    private GraffittiFacetV4FacetNode getNodeCategoriesFromFaceWhatTree(GraffittiFacetV4FacetNode facetWhat) {
        for (GraffittiFacetV4FacetNode facetWhatChild : facetWhat.getChildren()) {
            if (facetWhatChild.getConcept().getName().equalsIgnoreCase("categories")) {
                return facetWhatChild;
            }
        }
        return null;
    }

//    private GraffittiFacetV4FacetNode getNodeRestaurantsOnNodeCategories(GraffittiFacetV4FacetNode nodeRestaurants) {
//        for (GraffittiFacetV4FacetNode child : nodeRestaurants.getChildren()) {
//            if (child.getConcept().getName().equalsIgnoreCase("restaurants (category)")) {
//                return child;
//            }
//        }
//        return null;
//    }

//    public List<Cuisine> getCuisines() {
//        return cuisines;
//    }

//    public List<Category> getCategories() {
//        return categories;
//    }


    public List<Neighborhood> getNeighborhoods() {
        return neighborhoods;
    }

    public class WhatChildrenPage {
        private Integer remainingItems;
        private List<GraffittiFacetV5Node> children;

        public WhatChildrenPage(
            Integer remainingItems,
            List<GraffittiFacetV5Node> children
        ) {
            this.remainingItems = remainingItems;
            this.children = children;
        }

        public Integer getRemainingItems() {
            return remainingItems;
        }

        public List<GraffittiFacetV5Node> getChildren() {
            return children;
        }
    }
}
