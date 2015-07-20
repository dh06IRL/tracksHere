package com.david.trackshere.models.geo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by davidhodge on 12/23/14.
 */
public class Properties {
    @Expose
    private String description;
    @Expose
    private String id;
    @SerializedName("marker-color")
    @Expose
    private String markerColor;
    @SerializedName("marker-size")
    @Expose
    private String markerSize;
    @SerializedName("marker-symbol")
    @Expose
    private String markerSymbol;
    @Expose
    private String title;

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * The markerColor
     */
    public String getMarkerColor() {
        return markerColor;
    }

    /**
     *
     * @param markerColor
     * The marker-color
     */
    public void setMarkerColor(String markerColor) {
        this.markerColor = markerColor;
    }

    /**
     *
     * @return
     * The markerSize
     */
    public String getMarkerSize() {
        return markerSize;
    }

    /**
     *
     * @param markerSize
     * The marker-size
     */
    public void setMarkerSize(String markerSize) {
        this.markerSize = markerSize;
    }

    /**
     *
     * @return
     * The markerSymbol
     */
    public String getMarkerSymbol() {
        return markerSymbol;
    }

    /**
     *
     * @param markerSymbol
     * The marker-symbol
     */
    public void setMarkerSymbol(String markerSymbol) {
        this.markerSymbol = markerSymbol;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
