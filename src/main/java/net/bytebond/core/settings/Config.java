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

        public static Integer announcement_cooldown;

        // Nations Configuration
        // Nations.

        private static void init() {
            pathPrefix("Nations");
            announcement_cooldown = getInteger("announcement_cooldown");
        }

        public static class Creation {

            // Nations Creation Configuration
            // Nations.Creation.
            public static Boolean enabled;
            public static String requiredPermission;
            public static Integer starting_resources;

            public static Integer cost;

            private static void init() {
                pathPrefix("Nations.Creation");
                enabled = getBoolean("enabled");
                requiredPermission = getString("required_permissions");
                starting_resources = getInteger("starting_resources");
                cost = getInteger("cost");
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
            public static Integer cost;
            public static Integer max;
            public static Integer min;

            private static void init() {
                pathPrefix("Nations.Territory.Claiming");
                enabled = getBoolean("enabled");
                cost = getInteger("cost");
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

    public static class Housing {

        public static Boolean allow_housing;
        public static Integer max_houses;
        public static Integer max_housing_per_chunk;
        public static Boolean allow_dead_housing;
        public static Integer dead_housing_cost;

        private static void init() {
            pathPrefix("Housing");
            allow_housing = getBoolean("allow_housing");
            max_houses = getInteger("max_houses");
            max_housing_per_chunk = getInteger("max_housing_per_chunk");
            allow_dead_housing = getBoolean("allow_dead_housing");
            dead_housing_cost = getInteger("dead_housing_cost");
        }

    } // Housing

    public static class Tax {
        public static Boolean enabled;
        public static Integer max_tax_rate;
        public static String tax_collection_system;
        public static Integer tax_collection_interval;

        private static void init() {
            pathPrefix("Tax");
            enabled = getBoolean("enabled");
            max_tax_rate = getInteger("max_tax_rate");
            tax_collection_system = getString("tax_collection_system");
            tax_collection_interval = getInteger("tax_collection_interval");
        }
    }

}







