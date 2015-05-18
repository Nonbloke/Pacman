package Application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Created by Sarah on 09/05/2015.
 */
public class Score implements IDrawable {

    private int x=-1;
    private int y=-1;
    private int score=0;
    private final int maximumPipValue=2600;

    @Override
    public void draw(GraphicsContext gc) {
        int actualX=x*Maze.cellwidth;
        int actualY=y*Maze.cellheight;
        actualY+=Maze.cellheight-2;
        gc.setFill(Paint.valueOf("yellow"));

        if (isComplete()==true) {
            gc.fillText("Score: "+score+" you won!!!", actualX, actualY);
        } else {
            gc.fillText("Score: " + score, actualX, actualY);
        }
    }

    @Override
    public void updatePosition(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public void add(int value) {
        score+=value;
    }

    public boolean isComplete() {
        //if (score==maximumPipValue) {
//            return true;
//        }
        return false;
    }
}
