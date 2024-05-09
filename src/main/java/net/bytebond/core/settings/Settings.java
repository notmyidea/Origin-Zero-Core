package net.bytebond.core.settings;

import org.mineacademy.fo.settings.YamlStaticConfig;

@SuppressWarnings("unused")
public class Settings extends YamlStaticConfig {

    protected int getConfigVersion() {
        return 1;
    }


    @Override
    protected void onLoad() {
        this.loadConfiguration("settings.yml");
    }

    public static class Nations {

        public static class Creation {
            public static Boolean Enabled;
            public static Double Cost;
            public static String RequiredPermission;
            public static String[] Blacklist;
            public static Double MaxNameLength;
            public static Double MinNameLength;

            private static void init() {
                pathPrefix("Nations.Creation");
                Enabled = getBoolean("Enabled");
                Cost = getDouble("Cost");
                RequiredPermission = getString("Permission");
                Blacklist = getString("Blacklist").split(",");
                MaxNameLength = getDouble("MaxCharacters");
                MinNameLength = getDouble("MinCharacters");
            }
        }

        public static class Territory {
            public static Boolean AutoClaim;
           public static Double ChunkPrize;
           public static Double MaxChunks;
           public static Double MinChunks;

           private static void init() {
               pathPrefix("Nations.Territory");
               AutoClaim = getBoolean("AutoClaim");
               ChunkPrize = getDouble("Cost");
               MaxChunks = getDouble("MaxChunks");
               MinChunks = getDouble("MinChunks");
           }
        }



    }






    // To my future self, this function NEEDS to be here, called "init()" and it needs to be private + static
    private static void init() {


    }

}
