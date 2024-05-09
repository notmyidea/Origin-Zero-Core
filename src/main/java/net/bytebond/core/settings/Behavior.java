package net.bytebond.core.settings;

import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.List;

public class Behavior extends YamlStaticConfig {

    protected int getConfigVersion() {
        return 1;
    }

    @Override
    protected void onLoad(){
        this.loadConfiguration("behavior.yml");
    }

    public static class Nation {
        public static Boolean sendFirstInfoMessage;
        public static List<String> firstInfoMessage;





        public static class broadcastCreation {
            public static Boolean enabled;
            public static List<String> message;

            private static void init() {
                pathPrefix("Nation.broadcastCreation");
                enabled = getBoolean("enabled");
                message = getStringList("message");
            }
        }

        public static class broadcastDeletion {
            public static Boolean enabled;
            public static List<String> message;

            private static void init() {
                pathPrefix("Nation.broadcastDeletion");
                enabled = getBoolean("enabled");
                message = getStringList("message");
            }
        }



        private static void init() {
            pathPrefix("Nation");
            sendFirstInfoMessage = getBoolean("sendFirstInfoMessage");
            firstInfoMessage = getStringList("firstInfoMessage");
        }

    }



}
