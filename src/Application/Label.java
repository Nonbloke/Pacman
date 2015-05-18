package Application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Created by Sarah on 09/05/2015.
 */
public class Label implements IDrawable {

    private int x=-1;
    private int y=-1;
    private String message="";

    public Label(String message) {
        this.message=message;
    }

    @Override
    public void draw(GraphicsContext gc) {
        int actualX=x*Maze.cellwidth;
        int actualY=y*Maze.cellheight;
        actualY+=Maze.cellheight-2;
        gc.setFill(Paint.valueOf("yellow"));
        gc.fillText("Ready ???", actualX, actualY);
    }

    @Override
    public void updatePosition(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public void setMessage(String message) {
        this.message=message;
    }

}
