package net.bytebond.core.settings;

import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.List;

public class Messages extends YamlStaticConfig {

    protected int getConfigVersion() {return 1;}

    @Override
    protected void onLoad() {
        this.loadConfiguration("localization/messages.yml");
    }

    public static class Nation {

        public static class Static {
            public static List<String> not_in_nation;
            public static List<String> in_nation;

            private static void init() {
                pathPrefix("Nation.Static");
                not_in_nation = getStringList("not_in_nation");
                in_nation = getStringList("in_nation");
            }


            public static class Notify_description {
                public static Boolean enabled;
                public static List<String> messages;

                private static void init() {
                    pathPrefix("Nation.Static.Notify_description");
                    enabled = getBoolean("enabled");
                    messages = getStringList("messages");
                }
            }
        }

        public static class Creation {
            public static String nation_creation_disabled;
            public static String need_name_for_creation;
            public static String only_letters;
            public static String max_characters;
            public static String min_characters;
            public static String already_in_nation;
            public static String success;
            public static String not_in_nation;

            private static void init() {
                pathPrefix("Nation.Creation");
                nation_creation_disabled = getString("nation_creation_disabled");
                need_name_for_creation = getString("need_name_for_creation");
                only_letters = getString("only_letters");
                max_characters = getString("max_characters");
                min_characters = getString("min_characters");
                already_in_nation = getString("already_in_nation");
                success = getString("success");
                not_in_nation = getString("not_in_nation");
            }
        }

        public static class Set {
            public static String missing_arg;
            public static String set_setting_success;
            public static String max_characters;
            public static String min_characters;
            public static String blacklisted;
            public static String color_coding;

            private static void init() {
                pathPrefix("Nation.Set");
                missing_arg = getString("missing_arg");
                set_setting_success = getString("set_setting_success");
                max_characters = getString("max_characters");
                min_characters = getString("min_characters");
                blacklisted = getString("blacklisted");
                color_coding = getString("color_coding");
            }
        }

        public static class Claim {
            public static String under_limit;
            public static String over_limit;
            public static String already_claimed;
            // no this is not a typo
            public static String not_enough_muney;
            public static String disabled;
            public static String success;
            public static String unclaim_success;

            private static void init() {
                pathPrefix("Nation.Claim");
                under_limit = getString("under_limit");
                over_limit = getString("over_limit");
                already_claimed = getString("already_claimed");
                not_enough_muney = getString("not_enough_muney");
                disabled = getString("disabled");
                success = getString("success");
                unclaim_success = getString("unclaim_success");
            }

        }



    private static void init() {

    }

    }


}
