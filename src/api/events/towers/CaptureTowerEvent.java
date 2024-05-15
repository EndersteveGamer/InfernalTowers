package api.events.towers;

import api.events.TowerEvent;
import core.entities.Occupant;
import core.entities.instances.multitiles.Tower;

import java.util.Optional;

public class CaptureTowerEvent extends TowerEvent {
    private final Occupant newOwner;
    private final Occupant oldOwner;
    public CaptureTowerEvent(Tower tower, Occupant oldOwner, Occupant newOwner) {
        super(tower);
        this.newOwner = newOwner;
        this.oldOwner = oldOwner;
    }

    public Occupant getNewOwner() {
        return newOwner;
    }

    public Optional<Occupant> getOldOwner() {
        return Optional.ofNullable(oldOwner);
    }

    @Override
    public String toString() {
        return "Captured tower at " + tower().getEntrance().getPosition()
            + " (top at " + tower().getTop().getPosition() + ")";
    }
}
