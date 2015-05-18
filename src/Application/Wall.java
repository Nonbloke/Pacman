package Application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Created by Sarah on 08/05/2015.
 */
public class Wall implements IDrawable {

    private int diameter=Maze.cellheight;

    @Override
    public void draw(GraphicsContext gc) {
        int actualX=x*Maze.cellwidth;
        int actualY=y*Maze.cellheight;
        int fudgeFactor=0;
        gc.setFill(Paint.valueOf("darkblue"));

        int x=actualX + fudgeFactor;
        int y=actualY + fudgeFactor;
        int dx=diameter;
        int dy=diameter;
        int delta=3;

        if (bAbove==false) {
            y=y+delta;
            dy=dy-delta;
        }
        if (bBelow==false) {
            dy=dy-delta;
        }
        if (bLeft==false) {
            x=x+delta;
            dx=dx-delta;
        }
        if (bRight==false) {
            dx=dx-delta;
        }
        gc.fillRect(x, y, dx, dy);
    }

    private int x=-1;
    private int y=-1;

    @Override
    public void updatePosition(int x, int y) {
        this.x=x;
        this.y=y;
    }

    boolean bAbove=false;
    boolean bBelow=false;
    boolean bLeft=false;
    boolean bRight=false;

    public void smooth(Maze maze) {
        if (maze.isWall(x-1, y)==true) {
            bLeft=true;
        }
        if (maze.isWall(x+1, y)==true) {
            bRight=true;
        }
        if (maze.isWall(x, y-1)==true) {
            bAbove=true;
        }
        if (maze.isWall(x, y+1)==true) {
            bBelow=true;
        }
    }
}
