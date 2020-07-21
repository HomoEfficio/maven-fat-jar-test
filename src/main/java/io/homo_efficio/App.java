package io.homo_efficio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-07-20
 */
@Slf4j
public class App {

    public static void main(String[] args) throws IOException {
        ResourceLoader resourceLoader =
                new ResourceLoader("/static", Path.of("/static"));

        resourceLoader.loadResourceAsFile("/folder1/sample1");
        resourceLoader.loadResourceAsStream("/folder1/sample1");
        resourceLoader.loadConfig("/folder1/config.json");
    }
}
