package graphics.polygon;

import model.Point;

public interface Points {

    // Přidá vrchol polygonu a barvu hrany, která k němu vede.
    void addVertex(Point point, int color);
}
