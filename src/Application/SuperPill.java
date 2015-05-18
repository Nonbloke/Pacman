package Application;

/**
 * Created by Sarah on 08/05/2015.
 */
public class SuperPill extends Pill {

    public SuperPill() {
        this.score=50;
        this.diameter=8;
    }

    @Override
    public boolean eat(IMoveable eater) {
        if (super.eat(eater)==true) {
            Main.getInstance().SuperPillHasBeenEaten();
            return true;
        }
        return false;
    }
}
