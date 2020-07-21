package io.homo_efficio;

import lombok.Data;

import java.util.Set;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-07-21
 */
@Data
public class Config {

    private String title;
    private Set<String> tags;
}
