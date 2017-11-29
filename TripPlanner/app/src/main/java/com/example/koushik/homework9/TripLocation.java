package com.example.koushik.homework9;

import java.io.Serializable;

/**
 * Created by KOUSHIK on 29-04-2017.
 */

public class TripLocation implements Serializable {
    String locality;
    Double lat,lng;

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
