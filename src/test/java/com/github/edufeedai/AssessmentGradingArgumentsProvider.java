package com.github.edufeedai;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import com.github.edufeedai.model.AssessmentGradingConfig;
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
            try (FileReader reader = new FileReader(filename)) {
                Type type = new TypeToken<AssessmentGradingConfig>(){}.getType();
                return gson.fromJson(reader, type);
            } catch (IOException e) {
                throw new RuntimeException("Unable to load JSON file: " + filename, e);
            }
    }
}

