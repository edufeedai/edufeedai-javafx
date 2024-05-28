package com.github.amiguetes.edufeedai.edufeedaijavafx;

import com.github.amiguetes.edufeedai.edufeedaijavafx.model.Assessment;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.CheckResults;
import com.github.amiguetes.edufeedai.edufeedaijavafx.model.exceptions.AssessmentErrorException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class HelloController {

    @FXML
    private TextArea txtEvaluationCriteria;

    @FXML
    private TextArea txtAssessmentSend;

    @FXML
    private TextArea txtGradingResult;

    @FXML
    private Button btGrade;

    @FXML
    void btGrade(ActionEvent actionEvent) {

        String evaluationCriteria = txtEvaluationCriteria.getText();
        String assessmentSend = txtAssessmentSend.getText();

        Assessment assessment = CheckResults.getInstance().createNewAssessment(
                evaluationCriteria,assessmentSend
        );

        try {
            CheckResults.getInstance().gradeAssessment(assessment);
        } catch (Exception e) {
            handleError(e);
        }


    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Void handleError(Throwable throwable) {
        // Mostrar el error en una alerta
        javafx.application.Platform.runLater(() -> showAlert("Error", "Error evaluando: " + throwable.getMessage()));
        return null;
    }


}