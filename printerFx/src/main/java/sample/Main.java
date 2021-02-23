package sample;

import cn.hutool.core.io.resource.ClassPathResource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private Stage main;

    @Override
    public void start(Stage primaryStage) throws Exception {
        main = primaryStage;
        main.setTitle("小陈同学");
        try {
            main.getIcons().add(new Image(getClass().getResourceAsStream("/img/pig.png")));
        } catch (Exception e){
            ClassPathResource resource = new ClassPathResource("static/office_template/word_replace_tpl.docx");
            main.getIcons().add(new Image(resource.getStream()));
        }

        Pane load1 = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/sample.fxml"));
        Scene scene = new Scene(load1,400,450);
        scene.getStylesheets().add("css/css.css");
        main.setScene(scene);
        main.setResizable(false);
        main.show();
    }
}
