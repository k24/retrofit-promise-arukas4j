package com.github.k24.arukas4j.model;

import java.util.List;

/**
 * Created by k24 on 2017/04/10.
 */
public class Container extends DataRef {
    public static class Attributes {
        public String app_id;
        public String image_name;
        public String cmd;
        public boolean is_running;
        public int instances;
        public int mem;
        public List<Env> envs;
        public List<Port> ports;
        public List<PortMap> port_mapping;
        public String created_at;
        public String updated_at;
        public String status_text;
        public String arukas_domain;
        public String end_point;
    }

    public Attributes attributes;

    public static class Env {
        public String key;
        public String value;
    }

    public static class Port {
        public int number;
        public String protocol;
    }

    public static class PortMap {
        public int container_port;
        public int service_port;
        public String host;
    }

    public static class Relationships {
        public Relationship app;
    }
}
