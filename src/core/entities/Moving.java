package core.entities;

import api.Position;

public abstract class Moving extends Occupant {
    private Position direction;

    public Moving(Position position, Position direction) {
        super(position);
        this.direction = direction;
    }

    public Position getDirection() {
        return direction;
    }
}
