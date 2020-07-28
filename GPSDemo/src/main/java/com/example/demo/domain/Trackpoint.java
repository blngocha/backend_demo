package com.example.demo.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "TRACKPOINT")
public class Trackpoint implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="Latitude")
    private String trkptLatitude;

    @Column(name = "Longtitude")
    private String trkptLontitude;

    @Column(name = "Element")
    private String trkptElement;

    @Column(name = "DateTime")
    private String trkptDateTime;

    public Trackpoint(){
        super();
    }

    public Trackpoint(String trkptLatitude, String trkptLontitude, String trkptElement, String trkptDateTime){
        super();
        this.trkptLatitude = trkptLatitude;
        this.trkptLontitude = trkptLontitude;
        this.trkptElement = trkptElement;
        this.trkptDateTime = trkptDateTime;
    }

    public String getTrkptLatitude() {
        return trkptLatitude;
    }

    public void setTrkptLatitude(String trkptLatitude) {
        this.trkptLatitude = trkptLatitude;
    }

    public String getTrkptLontitude() {
        return trkptLontitude;
    }

    public void setTrkptLontitude(String trkptLontitude) {
        this.trkptLontitude = trkptLontitude;
    }

    public String getTrkptElement() {
        return trkptElement;
    }

    public void setTrkptElement(String trkptElement) {
        this.trkptElement = trkptElement;
    }

    public String getTrkptDateTime() {
        return trkptDateTime;
    }

    public void setTrkptDateTime(String trkptDateTime) {
        this.trkptDateTime = trkptDateTime;
    }

    @Override
    public String toString() {
        return "Trackpoint{" +
                "latitude='" + trkptLatitude + '\'' +
                ", longtitude='" + trkptLontitude + '\'' +
                ", ele='" + trkptElement + '\'' +
                ", datetime=" + trkptDateTime +
                '}';
    }
}
