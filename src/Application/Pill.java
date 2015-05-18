package Application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;

/**
 * Created by Sarah on 08/05/2015.
 */
public class Pill implements IDrawable, IConsumable {

    protected int diameter=2;
    protected int score=10;
    private boolean visible=true;

    @Override
    public void draw(GraphicsContext gc) {
        int actualX=-1;
        int actualY=-1;

        if (visible==true) {
            actualX = x * Maze.cellwidth;
            actualY = y * Maze.cellheight;
            int fudgeFactor = (Maze.cellheight - diameter) / 2;
            gc.setFill(Paint.valueOf("white"));
            gc.fillOval(actualX + fudgeFactor, actualY + fudgeFactor, diameter, diameter);
        }
    }

    private int x=0;
    private int y=0;

    @Override
    public void updatePosition(int x, int y) {
        this.x=x;
        this.y=y;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public boolean eat(IMoveable eater) {
        if (visible==false)
            return false;

        if ((eater instanceof Pacman)==false)
            return false;

        //Main.getInstance().UpdateScore(score);
        visible = false;
        return true;
    }
}
