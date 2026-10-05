package graphics.polygon;

import model.Line;
import model.Point;

import java.util.ArrayList;
import java.util.List;

public class Polygon implements Points {

    // Polygon se ukládá jako uspořádané vrcholy, ne jako samostatné hrany.
    // edgeColors[i] obsahuje barvu hrany vycházející z vertices[i].
    private final List<Point> vertices = new ArrayList<>();
    private final List<Integer> edgeColors = new ArrayList<>();

    @Override
    public void addVertex(Point point, int color) {
        // Předchozí poslední vrchol teď má následujícího souseda; uloží se barva této hrany.
        if (!vertices.isEmpty()) {
            edgeColors.set(edgeColors.size() - 1, color);
        }
        // Přidá nový vrchol a rezervuje barvu hrany, která z něj povede dál.
        vertices.add(point);
        edgeColors.add(color);
    }

    public int detectPoint(int x, int y) {
        // Vrátí první vrchol do vzdálenosti 10 pixelů, nebo -1, pokud žádný takový není.
        for (int i = 0; i < vertices.size(); i++) {
            if (isNear(vertices.get(i), x, y)) {
                return i;
            }
        }
        return -1;
    }

    public void moveVertex(int index, int x, int y) {
        // Point je neměnný, proto se přesunutý vrchol nahradí novým objektem.
        vertices.set(index, new Point(x, y));
    }

    public void removeVertex(int index) {
        // Při odebrání se odstraní i barva příslušné hrany, aby indexy zůstaly sladěné.
        vertices.remove(index);
        edgeColors.remove(index);
    }

    public void clear() {
        // Smaže všechny uložené vrcholy i barvy, aby šel polygon nakreslit znovu.
        vertices.clear();
        edgeColors.clear();
    }

    public List<Point> getVertices() {
        return vertices;
    }

    public List<Line> getLines() {
        List<Line> lines = new ArrayList<>();

        // Vytvoří hrany mezi každým vrcholem a jeho následujícím sousedem.
        for (int i = 0; i < vertices.size() - 1; i++) {
            lines.add(new Line(vertices.get(i), vertices.get(i + 1), edgeColors.get(i)));
        }
        if (vertices.size() > 2) {
            // Od tří vrcholů uzavře obvod hranou z posledního vrcholu do prvního.
            int lastIndex = vertices.size() - 1;
            lines.add(new Line(vertices.get(lastIndex), vertices.get(0), edgeColors.get(lastIndex)));
        }

        return lines;
    }

    private boolean isNear(Point point, int x, int y) {
        int dx = point.getX() - x;
        int dy = point.getY() - y;
        // Porovnání druhých mocnin vzdáleností odpovídá kruhovému dosahu 10 pixelů.
        return dx * dx + dy * dy <= 100;
    }
}
