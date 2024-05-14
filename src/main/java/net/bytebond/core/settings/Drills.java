package net.bytebond.core.settings;

import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.ArrayList;
import java.util.List;

public class Drills extends YamlStaticConfig {

    protected int getConfigVersion() {
        return 1;
    }

    @Override
    protected void onLoad() throws Exception {
        this.loadConfiguration("settings/drills.yml");
    }

    public static class Drill { // Cannot be called Drills of course

        public static class Messages {
            public static List<String> position;

            private static void init() {
                pathPrefix("Drill.Messages");
                position = getStringList("positions");
            }
        }

        public static class Wood {
            public static Integer rate_per_hour;
            public static List<String> position;
            public static String permission;
            private static void init() {
                pathPrefix("Drill.Wood");
                rate_per_hour = getInteger("rate_per_hour");
                position = getStringList("positions");
                permission = getString("permission");
            }
        }
        public static class Stone {
            public static Integer rate_per_hour;
            public static List<String> position;
            public static String permission;
            private static void init() {
                pathPrefix("Drill.Stone");
                rate_per_hour = getInteger("rate_per_hour");
                position = getStringList("positions");
                permission = getString("permission");
            }
        }
        public static class Brick {
            public static Integer rate_per_hour;
            public static List<String> position;
            public static String permission;
            private static void init() {
                pathPrefix("Drill.Brick");
                rate_per_hour = getInteger("rate_per_hour");
                position = getStringList("positions");
                permission = getString("permission");
            }
        }
        public static class Darkstone {
            public static Integer rate_per_hour;
            public static List<String> position;
            public static String permission;
            private static void init() {
                pathPrefix("Drill.Darkstone");
                rate_per_hour = getInteger("rate_per_hour");
                position = getStringList("positions");
                permission = getString("permission");
            }
        }
        public static class Obsidian {
            public static Integer rate_per_hour;
            public static List<String> position;
            public static String permission;
            private static void init() {
                pathPrefix("Drill.Obsidian");
                rate_per_hour = getInteger("rate_per_hour");
                position = getStringList("positions");
                permission = getString("permission");
            }
        }

        private static void init() {

        }
    }



}
