package com.phlawless.bomberz.core.logic.input;

import com.phlawless.bomberz.core.domain.math.Direction;

/**
 * Command to move an entity in a direction.
 */
public class MoveCommand implements Command {
    private final Direction direction;

    public MoveCommand(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
