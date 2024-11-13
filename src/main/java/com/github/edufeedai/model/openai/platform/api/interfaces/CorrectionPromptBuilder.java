package com.github.edufeedai.model.openai.platform.api.interfaces;

import com.github.edufeedai.model.openai.platform.api.interfaces.exceptions.APIException;

public interface CorrectionPromptBuilder {

    public String generatePromptCheckString() throws APIException;

}
