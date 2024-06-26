package api.world;

import api.EventManager;

import java.io.IOException;

/**
 * An interface representing a world loader
 */
public interface WorldLoader {
    /**
     * Loads a world from the given file
     * @param filePath The path of the file to read
     * @return The loaded world
     */
    World loadWorld(String filePath, EventManager eventManager) throws IOException;
}
