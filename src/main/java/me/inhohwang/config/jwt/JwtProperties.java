package me.inhohwang.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("jwt") //자바클래스에 프로피티 값을 가져와서 사용하는 애너테이션
public class JwtProperties {
    private String issuer;
    private String secretkey;
}
