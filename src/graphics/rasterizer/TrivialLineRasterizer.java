package graphics.rasterizer;

import graphics.Raster;
import model.Line;
import graphics.polygon.Polygon;

public class TrivialLineRasterizer implements LineRasterizer {

    private final Raster raster;
    public TrivialLineRasterizer(Raster raster) {
        this.raster = raster;
    }
    private final Polygon polygon = new Polygon();

    @Override
    public void rasterize(Line line) {
        rasterize(
                line.getPoint1().getX(),
                line.getPoint1().getY(),
                line.getPoint2().getX(),
                line.getPoint2().getY(),
                line.getColor()
        );
    }


    /**
     * Rasterizuje úsečku algoritmem DDA (Digital Differential Analyzer).
     * Počet kroků určí podle delší osy, v každém kroku posune souřadnice
     * o přírůstky dx/steps a dy/steps a vykreslí nejbližší pixel.
     */
    private void rasterize(int x1, int y1, int x2, int y2, int color) {
        int dx = x2 - x1;
        int dy = y2 - y1;

        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        if (steps == 0) {
            raster.setPixel(x1, y1, color);
            return;
        }

        float xIncrement = dx / (float) steps;
        float yIncrement = dy / (float) steps;

        float x = x1;
        float y = y1;

        for (int i = 0; i <= steps; i++) {
            raster.setPixel(Math.round(x), Math.round(y), color);

            x += xIncrement;
            y += yIncrement;
        }

    }

}
