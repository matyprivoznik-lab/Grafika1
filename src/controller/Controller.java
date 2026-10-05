package controller;

import graphics.rasterizer.LineRasterizer;
import graphics.rasterizer.TrivialLineRasterizer;
import model.Line;
import model.Point;
import view.Canvas;

import java.awt.*;
import java.awt.event.*;
import graphics.polygon.Polygon;
import java.util.List;

import javax.swing.*;

public class Controller {

    private Point startPoint;
    private Point currentPoint;
    // Nastaví se při pohybu myši; rozlišuje pouhé kliknutí od tažení.
    private boolean draggedToAddVertex;
    // Index právě přesouvaného vrcholu; -1 znamená, že není vybraný žádný.
    private int selectedVertexIndex = -1;

    private final Canvas canvas;
    private final LineRasterizer rasterizer;
    private final Polygon polygon = new Polygon();

    private int LINE_COLOR = Color.BLACK.getRGB();
    private static final int PREVIEW_COLOR = Color.RED.getRGB();

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- Constructors -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Controller(Canvas canvas) {
        this.canvas = canvas;
        this.rasterizer = new TrivialLineRasterizer(canvas.getRaster());
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Main functions -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void init() {

        canvas.clear();

        canvas.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    // C smaže uložené vrcholy i případný rozpracovaný tah.
                    polygon.clear();
                    startPoint = null;
                    currentPoint = null;
                    selectedVertexIndex = -1;
                    draggedToAddVertex = false;
                    render();
                }
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    // Delete odstraní vybraný vrchol.
                    if (selectedVertexIndex >= 0) {
                        polygon.removeVertex(selectedVertexIndex);
                        selectedVertexIndex = -1;
                        render();
                    }
                }

            }
        });
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1) {
                    return;
                }

                // Vykreslování a přesun vrcholů ovládá pouze levé tlačítko myši.
                selectedVertexIndex = polygon.detectPoint(e.getX(), e.getY());

                if (selectedVertexIndex >= 0) {
                    // Klik poblíž existujícího vrcholu zahájí jeho přesun, ne přidání dalšího.
                    startPoint = null;
                    currentPoint = null;
                } else {
                    // Zapamatuje se začátek gesta. Náhled se odtud kreslí ke kurzoru
                    // a aktualizuje se až do uvolnění tlačítka.
                    startPoint = getPoint(e);
                    currentPoint = startPoint;
                    draggedToAddVertex = false;
                    render();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1) {
                    return;
                }

                if (selectedVertexIndex >= 0) {
                    // Vrchol se měnil už během tažení; uvolněním tlačítka se přesun ukončí.
                    render();
                    return;
                }

                if (startPoint == null) {
                    return;
                }

                // První hrana vyžaduje tažení, aby bylo možné zadat oba její konce.
                // Jakmile jsou dva vrcholy uložené, další lze přidat kliknutím nebo tažením.
                if (!draggedToAddVertex && polygon.getVertices().size() < 2) {
                    startPoint = null;
                    currentPoint = null;
                    render();
                    return;
                }

                Point endPoint = getPoint(e);
                // První dokončené tažení uloží oba konce počáteční hrany.
                // Další gesta přidají pouze bod v místě uvolnění myši.
                if (polygon.getVertices().isEmpty()) {
                    polygon.addVertex(startPoint, LINE_COLOR);
                }
                polygon.addVertex(endPoint, LINE_COLOR);

                startPoint = null;
                currentPoint = null;
                draggedToAddVertex = false;
                render();
            }
        });





        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedVertexIndex >= 0) {
                    // Při každé události tažení se aktualizuje poloha vybraného vrcholu.
                    polygon.moveVertex(selectedVertexIndex, e.getX(), e.getY());
                    render();
                    return;
                }

                if (startPoint != null) {
                    // Zaznamená se tažení a dočasný konec náhledu se přesune ke kurzoru.
                    draggedToAddVertex = true;
                    currentPoint = getPoint(e);
                    render();
                }
            }
        });


        canvas.repaint();

    }

    private void render() {
        canvas.clear();

        // Smaže staré pixely a znovu vykreslí uložené hrany, aby scéna zůstala viditelná.
        for (Line line : polygon.getLines()) {
            rasterizer.rasterize(line);
        }

        if (startPoint != null && currentPoint != null) {
            List<Point> vertices = polygon.getVertices();
            if (vertices.isEmpty()) {
                // Před uložením prvního vrcholu se zobrazuje náhled první hrany od začátku tahu.
                rasterizer.rasterize(new Line(startPoint, currentPoint, PREVIEW_COLOR));
            } else {
                // Kurzorem se určuje nový vrchol. Náhled spojuje kurzor s posledním
                // a prvním uloženým vrcholem; po puštění se poloha kurzoru uloží.
                Point firstVertex = vertices.get(0);
                Point lastVertex = vertices.get(vertices.size() - 1);
                rasterizer.rasterize(new Line(lastVertex, currentPoint, PREVIEW_COLOR));
                if (vertices.size() > 1) {
                    rasterizer.rasterize(new Line(currentPoint, firstVertex, PREVIEW_COLOR));
                }
            }
        }

        canvas.repaint();
    }

    private Point getPoint(MouseEvent e) {
        return new Point(e.getX(), e.getY());
    }
    public void setLineColor(Color color) {
        LINE_COLOR = color.getRGB();
    }



}