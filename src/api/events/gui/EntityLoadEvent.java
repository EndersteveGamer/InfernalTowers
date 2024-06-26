package api.events.gui;

import api.events.Event;
import api.events.occupants.OccupantSpawnEvent;

/**
 * An event called when a graphical entity is being initialized
 */
public class EntityLoadEvent extends Event {
    private final OccupantSpawnEvent spawnEvent;

    public EntityLoadEvent(OccupantSpawnEvent spawnEvent) {
        this.spawnEvent = spawnEvent;
    }

    public OccupantSpawnEvent getSpawnEvent() {
        return spawnEvent;
    }

    @Override
    public String toString() {
        return "GUI entity loaded at grid space " + spawnEvent.getOccupant().getPosition();
    }
}
