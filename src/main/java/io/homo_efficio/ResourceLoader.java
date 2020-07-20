package io.homo_efficio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-07-20
 */
@Slf4j
@RequiredArgsConstructor
public class ResourceLoader {

    private final String root;
    private final Path rootPath;


    public void loadResourceAsFile(String resourceLocation) throws IOException {
        log.info("*** getResource() + File 방식");
        log.info("content root: {}", rootPath);
        log.info("resourceLocation: {}", resourceLocation);

        String fileLocation = this.getClass().getResource(root + resourceLocation).getFile();
        log.info("fileLocation from URL: {}", fileLocation);

        File file = new File(fileLocation);
        FileReader fileReader = new FileReader(file);
        char[] chars = new char[(int) file.length()];
        fileReader.read(chars);

        log.info("resource contents: {}", new String(chars));
    }
}
