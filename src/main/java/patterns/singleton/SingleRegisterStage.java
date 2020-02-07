package patterns.singleton;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SingleRegisterStage {
    // 懒汉单例模式
    private static Stage registerInstance;

    private SingleRegisterStage() {}

    public static Stage getInstance() {
        if(registerInstance == null) {
            FXMLLoader Loader = new FXMLLoader(SingleRegisterStage.class.getResource("/views/register.fxml"));
            Parent parent = null;
            try {
                parent = Loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setTitle("注册界面");
            stage.setScene(new Scene(parent, 500, 400));
            stage.setResizable(false);
            registerInstance = stage;
        }
        return registerInstance;
    }
}
