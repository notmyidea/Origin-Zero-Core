package net.bytebond.core.settings;

import org.mineacademy.fo.settings.YamlStaticConfig;

public class Data extends YamlStaticConfig {

    protected int getConfigVersion() {
        return 1;
    }

    @Override
    protected void onLoad() {
        this.loadConfiguration("data.yml");
    }

    public static class storage {
        public static String method;



        public static class mysql {
            public static String username;
            public static String password;
            public static String hostname;
            public static String database;
            public static String port;
            public static Boolean autoReconnect;
            public static Boolean useSSL;
            public static Boolean verifyServerCertificate;
            public static Boolean returnOnConnectionIssues;

            private static void init() {
                pathPrefix("storage.mysql");
                username = getString("username");
                password = getString("password");
                hostname = getString("hostname");
                database = getString("database");
                port = getString("port");
                autoReconnect = getBoolean("autoReconnect");
                useSSL = getBoolean("useSSL");
                verifyServerCertificate = getBoolean("verifyServerCertificate");
                returnOnConnectionIssues = getBoolean("returnOnConnectionIssues");
            }


        }

        private static void init() {
            pathPrefix("storage");
            method = getString("method");
        }


    }


}
