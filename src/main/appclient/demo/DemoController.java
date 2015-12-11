package main.appclient.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Optional;

public class DemoController {

    public static final String VECTOR_VIEW = "resources/vectorview.fxml";
    public static final String TEXT_VIEW = "resources/textview.fxml";
    public static final String PICTURE_VIEW = "resources/pictureview.fxml";
    @FXML
    RadioButton vectorRadio;

    @FXML
    RadioButton textRadio;

    @FXML
    RadioButton photoRadio;

    @FXML Pane scene;

    @FXML
    public void initialize(){
        vectorRadio.setOnAction(event -> {
            Optional<Node> vectorScene = getScene(VECTOR_VIEW);
            if (vectorScene.isPresent()) {
                scene.getChildren().clear();
                scene.getChildren().addAll((vectorScene.get()));
            }
        });
        textRadio.setOnAction((event -> {
            Optional<Node> vectorScene = getScene(TEXT_VIEW);
            if (vectorScene.isPresent()) {
                scene.getChildren().clear();
                scene.getChildren().addAll((vectorScene.get()));
            }
        }));
        photoRadio.setOnAction((event -> {
            Optional<Node> vectorScene = getScene(PICTURE_VIEW);
            if (vectorScene.isPresent()) {
                scene.getChildren().clear();
                scene.getChildren().addAll((vectorScene.get()));
            }
        }));
    }

    private Optional<Node> getScene(String sceneName){
        Node scene = null;
        try {
            scene = FXMLLoader.load(getClass().getClassLoader().getResource(sceneName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(scene);
    }
}
