package com.example.demo.domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Table(name = "WAYPOINT")
public class Waypoint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Latitude")
    private String latitude;

    @Column(name = "Longitude")
    private String longitude;

    @Column(name = "name")
    private String name;

    @Column(name = "Sym")
    private String sym;

    public Waypoint(){
        super();
    }
    public Waypoint(String latitude, String longitude, String name, String sym){
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.sym = sym;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSym() {
        return sym;
    }

    public void setSym(String sym) {
        this.sym = sym;
    }

    @Override
    public String toString() {
        return "Waypoint{" +
                "latitude='" + latitude + '\'' +
                ", longtitude='" + longitude + '\'' +
                ", name='" + name + '\'' +
                ", sym='" + sym + '\'' +
                '}';
    }
}
