package net.bytebond.core.util.io;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Check {

    @Getter
    private static final Check instance = new Check();

    public void checkDirectory(String directory) {
        File dataFolder = new File("plugins/Core/" + directory);
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

}
