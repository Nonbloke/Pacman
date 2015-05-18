package Application;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Sarah on 08/05/2015.
 */
public interface IDrawable  {
    void draw(GraphicsContext gc);
    void updatePosition(int x, int y);
}
