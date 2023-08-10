package com.mangoplay.yeezymusic.objects;

public class Spot {
    Long id = 0L;
    String name;
    String city;
    String url;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getUrl() {
        return url;
    }

    public Spot(String name, String city, String url) {
        this.name = name;
        this.city = city;
        this.url = url;
    }
}


