package io.homo_efficio;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

        URL resourceURL = this.getClass().getResource(root + resourceLocation);
        log.info("resourceURL: {}", resourceURL);

//        String fileLocation = resourceURL.getFile();
//        log.info("fileLocation from URL: {}", fileLocation);
//
//        File file = new File(fileLocation);
//        FileReader fileReader = new FileReader(file);
//        char[] chars = new char[(int) file.length()];
//        fileReader.read(chars);
//
//        log.info("resource contents: {}", new String(chars));

        URL resource = this.getClass().getResource(root + resourceLocation);
        InputStream inputStream = resource.openStream();
        byte[] bytes = inputStream.readAllBytes();
        log.info("resource contents: {}", new String(bytes, StandardCharsets.UTF_8));
    }

    public void loadResourceAsStream(String resourceLocation) throws IOException {
        log.info("OOO getResourceAsStream() 방식");
        log.info("content root: {}", root);
        log.info("resourceLocation: {}", resourceLocation);

        InputStream resourceAsStream = this.getClass().getResourceAsStream(root + resourceLocation);
        byte[] bytes = resourceAsStream.readAllBytes();
        log.info("resource contents: {}", new String(bytes, StandardCharsets.UTF_8));
    }

    public void loadConfig(String resourceLocation) {
        log.info("*** getResource() + Jackson 방식");
        log.info("content root: {}", rootPath);
        log.info("resourceLocation: {}", resourceLocation);

        URL configURL = this.getClass().getResource(root + resourceLocation);
        log.info("resourceURL: {}", configURL);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Config config = objectMapper.readValue(configURL, Config.class);
            log.info("title in config: {}", config.getTitle());
            log.info("tags in config: [{}]", String.join(", ", config.getTags()));
        } catch (IOException e) {
            throw new RuntimeException("설정 파일 로딩에 실패했습니다.", e);
        }
    }

    public void loadProperties(String resourceLocation) {
        log.info("*** getResource() + Properties File 방식");
        log.info("content root: {}", rootPath);
        log.info("resourceLocation: {}", resourceLocation);

        URL propsURL = this.getClass().getResource(root + resourceLocation);
        log.info("resourceURL: {}", propsURL);

        Properties properties = new Properties();

        try {
//            properties.load(new FileReader(propsURL.getFile()));
            properties.load(propsURL.openStream());
            log.info("title in props: {}", properties.getProperty("title"));
            log.info("description in props: {}", properties.getProperty("description"));
        } catch (IOException e) {
            throw new RuntimeException("properties 파일 로딩에 실패했습니다.", e);
        }
    }

    public void loadDirectoryAsStream(String dir) throws IOException {
        log.info("OOO getResourceAsStream() + Directory");
        log.info("content root: {}", root);
        log.info("dir: {}", dir);

        // IDE 에서는 잘 동작, jar 에서는 에러는 발생하지 않으나 esourceAsStream.readAllBytes() 값이 비어있음
        log.info("USING naive getResourceAsStream(String directory) -----");
        InputStream resourceAsStream = this.getClass().getResourceAsStream(root + dir);
        byte[] bytes = resourceAsStream.readAllBytes();
        log.info("resource contents length: {}", bytes.length);
        if (bytes.length > 0) {
            log.info("resource contents: {}", new String(bytes, StandardCharsets.UTF_8));
        } else {
            // jar 에서는 잘 동작
            // IDE 에서는 예외 발생: java.io.FileNotFoundException: /Users/1003604/gitRepo/study/maven-fat-jar-test/target/classes/io/homo_efficio (Is a directory)
            // 따라서 bytes.length = 0 일 때만 실행하도록
            log.info("USING JarFile -----");
            String path = this.getClass().getResource("").getPath();
            log.info("resourcePath: {}", path);
            int exclamationIndex = path.lastIndexOf("!") > 0 ? path.lastIndexOf("!") : path.length();
            String jarFilePath = path.substring(0, exclamationIndex).replaceAll("file:", "");
            log.info("jarFilePath : {}", jarFilePath);
            LocalDateTime start = LocalDateTime.now();
            log.info("jarFile start: {}", start);
            JarFile jarFile = new JarFile(jarFilePath);
            LocalDateTime end = LocalDateTime.now();
            log.info("jarFile end  : {}", end);
            enumerationAsStream(jarFile.entries())
                    .filter(entry -> entry.getRealName().startsWith((root + dir).substring(1)))
                    .forEach(entry -> log.info("jarEntry: {}", entry.getRealName()));
        }
    }

    // From https://stackoverflow.com/a/23276455
    static <T> Stream<T> enumerationAsStream(Enumeration<T> e) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<T>() {
                            @Override
                            public boolean hasNext() {
                                return e.hasMoreElements();
                            }

                            @Override
                            public T next() {
                                return e.nextElement();
                            }
                        },
                        Spliterator.ORDERED
                ), false
        );
    }
}
