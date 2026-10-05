import controller.Controller;

import view.Window;
import static javax.swing.SwingUtilities.invokeLater;


public class Main {




    public static void main(String[] args) {
        invokeLater(() -> {
            Window window = new Window(800, 600);
            Controller controller = new Controller(window.getCanvas());

            window.setColorSelectionHandler(controller::setLineColor);
            controller.init();
        });
    }
}
