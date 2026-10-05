package view;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import graphics.polygon.Polygon;

/**
 * Represents the main application window that displays the {@link Canvas}.
 * The window initializes the canvas, configures the Swing frame, and provides access to the canvas.
 *
 * @author PGRF FIM UHK
 * @version 2026
 */
public class Window extends JFrame {
    private final Polygon polygon = new Polygon();
    public static int colorValue;
    private final Canvas canvas;
    private Consumer<Color> colorSelector;

    public void setColorSelectionHandler(Consumer<Color> handler) {
        this.colorSelector = handler;
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- Constructors -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Window(int width, int height) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("FIM UHK | PGRF1 | 2026 | PŘÍVOZNÍK MATĚJ");
        setLayout(new BorderLayout());
        setResizable(false);

        canvas = new Canvas((int) (width), height);
        add(canvas, BorderLayout.CENTER);
        JMenuBar menuBar = new JMenuBar();
        JMenu ColorMenu = new JMenu("color");
        menuBar.add(ColorMenu);
        JMenuItem chooseColor = new JMenuItem("choose color");

        JMenuItem colorButton = new JMenuItem();
        colorButton.setSize(20, 20);


        chooseColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Vyber barvu", Color.BLACK);
            if (color != null) {
                colorButton.setBackground(color);
                if (colorSelector != null) {
                    colorSelector.accept(color);
                }
            }
            colorButton.setBackground(color);
            colorValue = color.getRGB();
        });

        ColorMenu.add(chooseColor);
        setJMenuBar(menuBar);
        menuBar.add(colorButton);

        pack();

        setLocationRelativeTo(null);
        canvas.setFocusable(true);
        canvas.grabFocus();
        setVisible(true);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Getters -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Canvas getCanvas() {
        return canvas;
    }

}