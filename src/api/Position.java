package api;

import java.util.Vector;

public interface Position {
    int getX();
    int getY();
    int getZ();
    Position add(int x, int y, int z);
    Position add(Position other);
}
