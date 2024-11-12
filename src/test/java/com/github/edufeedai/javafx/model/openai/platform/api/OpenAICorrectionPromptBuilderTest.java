package com.github.edufeedai.javafx.model.openai.platform.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import io.github.cdimascio.dotenv.Dotenv;

public class OpenAICorrectionPromptBuilderTest {

    String OpenAI_KEY = Dotenv.load().get("OPENAI_API_KEY");

    String context = "Es un segundo curso de un ciclo formativo de grado superior de ASIX en la comunidad valenciana";
    String activityStatement = "Se pide realizar una configuración DNS en linux utilizando bind 9 para un dominio llamado asixalcoy.org";
    String rubric = "Ha creado los ficheros de zona: db.asixalcoy.org y db.192.168.0 y ha configurado el servidor DNS para que resuelva correctamente los nombres de los equipos de la red " + 
                    "y el nombre del dominio asixalcoy.org a la dirección IP" + 
                    "ha configurado el servidor DHCP para que informe del nuevo dns a los clientes";


    @Test
    void testGeneratePromptCheckString() {

        OpenAICorrectionPromptBuilder oacpb = new OpenAICorrectionPromptBuilder(context, activityStatement, rubric, OpenAI_KEY);

        try {

            String prompt = oacpb.generatePromptCheckString();
            assertNotNull(prompt);

        } catch (Exception e) {
            fail(e);
        }


    }
}
