package com.michaldurkalec.fw.fastnfurious.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;


public class MovieDetailsTest {

    private static String exampleOMDbJson;

    @BeforeAll
    public static void setUp() throws IOException {
        File jsonFile = new ClassPathResource("json/OMDbMovieDetails.json").getFile();
        exampleOMDbJson = new String(Files.readAllBytes(jsonFile.toPath()));
    }

    @Test
    public void shouldDeserializeOMDbJson() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        OMDbMovieDetails OMDbMovieDetails = objectMapper.readValue(exampleOMDbJson, OMDbMovieDetails.class);
        assertThat(OMDbMovieDetails, is(notNullValue()));
        assertThat(OMDbMovieDetails.getTitle(), is("The Fast and the Furious"));
    }
}
