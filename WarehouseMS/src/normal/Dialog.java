package normal;

import javafx.scene.control.Alert;

public class Dialog {

    public static void showDialog(String type, String tip) {
        Alert alert = null;
        if(type.equals("INFORMATION")) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("INFORMATION");
        } else if(type.equals("ERROR")){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
        } else {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("WARNING");
        }
        alert.setContentText(tip);
        alert.show();
    }
}
