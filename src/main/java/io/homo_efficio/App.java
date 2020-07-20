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
        log.info("maven으로 만든 fat-jar에서 logback + slf4j 정상 동작함");

        ResourceLoader resourceLoader = new ResourceLoader("/static", Path.of("/static"));
        resourceLoader.loadResource("/folder1/sample");
    }
}
