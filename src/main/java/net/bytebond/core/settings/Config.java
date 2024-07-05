package net.bytebond.core.settings;

import org.mineacademy.fo.settings.YamlStaticConfig;

public class Config extends YamlStaticConfig {

    protected int getConfigVersion() {
        return 1;
    }

    @Override
    protected void onLoad() {
        this.loadConfiguration("settings/config.yml");
    }

    // General
    public static class General {
        public static Boolean debugging;

        private static void init() {
            pathPrefix("General");
            debugging = getBoolean("debugging");
        }

    }


    // Economy
    public static class Economy {

        // Economy Configuration
        // Economy.

        public static String provider;
        public static String[] currencies;

        private static void init() {
            pathPrefix("Economy");
            provider = getString("provider");
            currencies = getString("currencies").split(", ");
        }
    }


    // Nations
    // / Creation
    // / / Naming
    // / Territory
    // / / Claiming
    // / Members


    public static class Nations {

        // Nations Configuration
        // Nations.

        public static class Creation {

            // Nations Creation Configuration
            // Nations.Creation.
            public static Boolean enabled;
            public static String requiredPermission;

            public static Double cost;

            private static void init() {
                pathPrefix("Nations.Creation");
                enabled = getBoolean("enabled");
                requiredPermission = getString("required_permissions");
                cost = getDouble("cost");
            }

            public static class Naming {

                // Nations Creation Naming Configuration
                // Nations.Creation.Naming.
                public static String color_coding_permission;
                public static Integer max_characters;
                public static Integer min_characters;
                public static String[] blacklist;

                private static void init() {
                    pathPrefix("Nations.Creation.Naming");
                    max_characters = getInteger("max_characters");
                    min_characters = getInteger("min_characters");
                    blacklist = getString("blacklist").split(",");
                    color_coding_permission = getString("color_coding_permission");
                }
            } // Naming

            public static class Tags {

                // Nations Creation Naming Configuration
                // Nations.Creation.Naming.
                public static Integer max_characters;
                public static Integer min_characters;
                public static String[] blacklist;

                private static void init() {
                    pathPrefix("Nations.Creation.Tags");
                    max_characters = getInteger("max_characters");
                    min_characters = getInteger("min_characters");
                    blacklist = getString("blacklist").split(",");
                }
            } // Tags

            public static class Description {
                public static String color_coding_permission;

                // Nations Creation Naming Configuration
                // Nations.Creation.Naming.
                public static Integer max_characters;
                public static Integer min_characters;
                public static String[] blacklist;

                private static void init() {
                    pathPrefix("Nations.Creation.Description");
                    max_characters = getInteger("max_characters");
                    min_characters = getInteger("min_characters");
                    blacklist = getString("blacklist").split(",");
                    color_coding_permission = getString("color_coding_permission");
                }
            } // Description

        } // Creation
    } // Nations


    public static class Territory {

        // Nations Territory Configuration
        // Nations.Territory

        public static class Claiming {

            // Nations Territory Claiming Configuration
            // Nations.Territory.Claiming.
            public static Boolean enabled;
            public static Double cost;
            public static Integer max;
            public static Integer min;

            private static void init() {
                pathPrefix("Nations.Territory.Claiming");
                enabled = getBoolean("enabled");
                cost = getDouble("cost");
                max = getInteger("max");
                min = getInteger("min");
            }
        } // Claiming

        public static class Building {
            public static Boolean allow_fighting;


            public static class Alliances {
                public static Boolean allow_building;
                public static Boolean allow_usage;
                public static Boolean allow_destroy;

                private static void init() {
                    pathPrefix("Nations.Territory.Building.Alliances");
                    allow_building = getBoolean("allow_building");
                    allow_usage = getBoolean("allow_usage");
                    allow_destroy = getBoolean("allow_destroy");
                }
            }
            public static class Neutral {
                public static Boolean allow_building;
                public static Boolean allow_usage;
                public static Boolean allow_destroy;

                private static void init() {
                    pathPrefix("Nations.Territory.Building.Neutral");
                    allow_building = getBoolean("allow_building");
                    allow_usage = getBoolean("allow_usage");
                    allow_destroy = getBoolean("allow_destroy");
                }
            }
            public static class War {
                public static Boolean allow_building;
                public static Boolean allow_usage;
                public static Boolean allow_destroy;

                private static void init() {
                    pathPrefix("Nations.Territory.Building.War");
                    allow_building = getBoolean("allow_building");
                    allow_usage = getBoolean("allow_usage");
                    allow_destroy = getBoolean("allow_destroy");
                }
            }



            private static void init() {
                pathPrefix("Nations.Territory.Building");
                allow_fighting = getBoolean("allow_fighting");
            }


        }


    } // Territory

}







