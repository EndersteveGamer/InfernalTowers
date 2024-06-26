package core.entities.builders;

import api.Position;
import api.world.World;
import core.entities.MultiTile;
import core.entities.instances.multitiles.Ladder;
import core.entities.instances.multitiles.Tower;
import core.utils.JsonParser;
import core.utils.builders.JsonBuilder;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * A {@link JsonBuilder} that can build {@link MultiTile}s
 */
public class JsonMultiTileBuilder extends JsonBuilder<MultiTile> {
    public JsonMultiTileBuilder(World world, boolean debugMode) {
        super(debugMode);
        builderMap.put("tower", getTowerBuilder(world));
        builderMap.put("ladder", getLadderBuilder(world));
    }

    private Function<JsonParser, MultiTile> getTowerBuilder(World world) {
        return json -> {
            Position pos = requirePosition(json, "position", world);
            Optional<Number> size = json.getObjectAtPath("size");
            Optional<UUID> owner = json.<String>getObjectAtPath("owner").map(UUID::fromString);
            Tower tower = size.map(number -> new Tower(pos, number.intValue())).orElseGet(() -> new Tower(pos));
            owner.ifPresent(tower::setOwner);
            return tower;
        };
    }

    private Function<JsonParser, MultiTile> getLadderBuilder(World world) {
        return json -> {
            Position pos = requirePosition(json, "position", world);
            int size = this.<Number>requireKey(json, "size").intValue();
            return new Ladder(pos, size);
        };
    }
}
