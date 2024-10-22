package com.github.edufeedai.javafx;

import com.github.edufeedai.javafx.model.Assessment;
import com.github.edufeedai.javafx.model.CheckResults;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;


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



        try {
            Assessment assessment = CheckResults.getInstance().createNewAssessment(
                    evaluationCriteria,assessmentSend
            );

            txtGradingResult.setText(assessment.getAssesmentFeedback());


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