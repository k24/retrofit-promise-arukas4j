package com.github.k24.arukas4j.model;

/**
 * Created by k24 on 2017/04/10.
 */
public class App extends DataRef {
    public static class Attributes {
        public String name;
        public String image_id;
        public String created_at;
        public String updated_at;
    }

    public Attributes attributes;

    public static class Relationships {
        public Relationship user;
        public Relationship container;
    }

    public Relationships relationships;
}
