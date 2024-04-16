package core.entities;

import api.Position;
import api.entities.Moveable;
import api.entities.Redirector;

/**
 * An abstract class used for entities that are able to move
 */
public abstract class Moving extends Occupant implements Moveable {
    private Position direction;

    public Moving(Position position, Position direction) {
        super(position);
        this.direction = direction;
    }

    /**
     * @return The velocity of this entity
     */
    public Position getDirection() {
        return direction;
    }

    /**
     * A setter for the velocity of this entity
     * @param direction The new direction
     */
    public void setDirection(Position direction) {
        this.direction = direction;
    }

    @Override
    public Position getPresentPosition() {
        return getPosition();
    }

    @Override
    public Position getTargetPosition() {
        return getPresentPosition().clone().add(direction);
    }

    @Override
    public boolean moveTo(Position pos) {
        if (pos.getOccupant().isEmpty()) {
            setPosition(getTargetPosition());
        }
        else {
            if (pos.getOccupant().get() instanceof Redirector redirector) redirector.redirect(this);
            else return false;
        }
        return true;
    }
}
