package com.david.trackshere.models.geo;

import com.google.gson.annotations.Expose;

/**
 * Created by davidhodge on 12/23/14.
 */
public class Feature {
    @Expose
    private Geometry geometry;
    @Expose
    private String id;
    @Expose
    private Properties properties;
    @Expose
    private String type;

    /**
     *
     * @return
     * The geometry
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     *
     * @param geometry
     * The geometry
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
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
     * The properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     *
     * @param properties
     * The properties
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
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
