package com.github.amiguetes.edufeedai.edufeedaijavafx.model;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.github.cdimascio.dotenv.Dotenv;

public class AssessmentGradingArgumentsProvider  implements ArgumentsProvider {

    private static final Gson gson = new Gson();

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(Dotenv.load().get("ASSESSMENT_GRADING_CONFIG_FILE"))
                .map(this::loadJson)
                .map(Arguments::of);
    }
    private AssessmentGradingConfig loadJson(String filename){
            try (InputStream input = getClass().getClassLoader().getResourceAsStream(filename);
                 InputStreamReader reader = new InputStreamReader(input)) {

                if (input == null){
                    throw new IllegalArgumentException("File not found: " + filename);
                }
                Type type = new TypeToken<AssessmentGradingConfig>(){}.getType();
                return gson.fromJson(reader,type);

            } catch (IOException e){
                throw new RuntimeException("Unable to Load JSON File: " + filename,e);
            }
    }
}

