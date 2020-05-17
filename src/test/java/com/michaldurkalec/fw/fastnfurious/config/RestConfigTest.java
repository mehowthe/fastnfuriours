package com.michaldurkalec.fw.fastnfurious.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.michaldurkalec.fw.fastnfurious.domain.dto.OMDbMovieDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;


@SpringBootTest
public class RestConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:json/OMDbMovieDetails.json")
    private Resource exampleJson;

    @Test
    public void shouldDeserializeJsonCaseInsensitive() throws IOException, IllegalAccessException {
        String json = new String(Files.readAllBytes(exampleJson.getFile().toPath()));
        OMDbMovieDetails targetObject  = objectMapper.readValue(json, OMDbMovieDetails.class);
        assertThat(targetObject.getTitle(), is("The Fast and the Furious"));
        for (Field f : targetObject.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            assertThat(f.get(targetObject), is(notNullValue()));
        }
    }
}
