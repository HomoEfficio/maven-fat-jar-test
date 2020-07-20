package io.homo_efficio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

    public void loadResource(String resourceLocation) throws IOException {
        log.info("OOO getResourceAsStream() 방식");
        log.info("content root: {}", root);
        InputStream resourceAsStream = this.getClass().getResourceAsStream(root + resourceLocation);
        byte[] bytes = resourceAsStream.readAllBytes();
        log.info("resourceLocation: {}", resourceLocation);
        log.info("resource contents: {}", new String(bytes, StandardCharsets.UTF_8));
    }

    public void loadResource(Path resourcePath) throws IOException {
        log.info("*** getResourceAsStream() 방식");
        log.info("content root: {}", rootPath);
        URL resourceURL = this.getClass().getResource(root + resourcePath.toString());
        log.info("resourceURL: {}", resourceURL);
        log.info("resourceURL.getPath(): {}", resourceURL.getPath());
        // path.resolve(Path또는String) 는 인자가
        //   절대경로면 그냥 그 인자에 해당하는 Path 를 반환
        //   상대경로면 인자를 path 하위에 붙여서 만든 Path 를 반환
        Path resolvedResourcePath = rootPath.resolve(resourceURL.getPath());
        log.info("resourceLocation: {}", resolvedResourcePath);
        byte[] bytes = Files.readAllBytes(resolvedResourcePath);
        log.info("resource contents: {}", new String(bytes, StandardCharsets.UTF_8));
    }
}
