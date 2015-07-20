package com.david.trackshere.models.geo;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidhodge on 12/23/14.
 */
public class GeoResponse {
    @Expose
    private List<Feature> features = new ArrayList<Feature>();
    @Expose
    private String id;
    @Expose
    private String type;

    /**
     *
     * @return
     * The features
     */
    public List<Feature> getFeatures() {
        return features;
    }

    /**
     *
     * @param features
     * The features
     */
    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }
}
