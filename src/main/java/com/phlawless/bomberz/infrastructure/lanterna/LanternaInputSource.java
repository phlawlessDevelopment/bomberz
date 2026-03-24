package com.phlawless.bomberz.infrastructure.lanterna;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.phlawless.bomberz.core.domain.math.Direction;
import com.phlawless.bomberz.core.logic.input.Command;
import com.phlawless.bomberz.core.logic.input.MoveCommand;
import com.phlawless.bomberz.core.logic.input.PlaceBombCommand;

/**
 * Handles keyboard input from a Lanterna screen and translates it to game commands.
 */
public class LanternaInputSource {
    private final Screen screen;

    public LanternaInputSource(Screen screen) {
        this.screen = screen;
    }

    /**
     * Polls all pending input and returns a list of commands.
     * Returns an empty list if no input is available.
     *
     * @return list of commands from queued input
     * @throws IOException if there is an error reading input
     */
    public List<Command> pollCommands() throws IOException {
        List<Command> commands = new ArrayList<>();

        KeyStroke keyStroke;
        while ((keyStroke = screen.pollInput()) != null) {
            Command command = translateKeyStroke(keyStroke);
            if (command != null) {
                commands.add(command);
            }
        }

        return commands;
    }

    /**
     * Checks if the given keystroke is an exit command (ESC or Ctrl+C).
     */
    public boolean isExitKey(KeyStroke keyStroke) {
        if (keyStroke == null) {
            return false;
        }
        if (keyStroke.getKeyType() == KeyType.Escape) {
            return true;
        }
        if (keyStroke.getKeyType() == KeyType.Character
                && keyStroke.getCharacter() == 'c'
                && keyStroke.isCtrlDown()) {
            return true;
        }
        return false;
    }

    /**
     * Polls input and checks if an exit key was pressed.
     *
     * @return true if ESC or Ctrl+C was pressed
     * @throws IOException if there is an error reading input
     */
    public boolean shouldExit() throws IOException {
        KeyStroke keyStroke = screen.pollInput();
        return isExitKey(keyStroke);
    }

    /**
     * Translates a Lanterna KeyStroke to a game Command.
     *
     * @param keyStroke the key pressed
     * @return the corresponding command, or null if no mapping exists
     */
    private Command translateKeyStroke(KeyStroke keyStroke) {
        // Check for exit first
        if (isExitKey(keyStroke)) {
            return null; // Exit is handled separately
        }

        return switch (keyStroke.getKeyType()) {
            case ArrowUp -> new MoveCommand(Direction.UP);
            case ArrowDown -> new MoveCommand(Direction.DOWN);
            case ArrowLeft -> new MoveCommand(Direction.LEFT);
            case ArrowRight -> new MoveCommand(Direction.RIGHT);
            case Character -> translateCharacter(keyStroke.getCharacter());
            default -> null;
        };
    }

    /**
     * Translates character keys (WASD, SPACE) to commands.
     */
    private Command translateCharacter(char c) {
        return switch (Character.toLowerCase(c)) {
            case 'w' -> new MoveCommand(Direction.UP);
            case 's' -> new MoveCommand(Direction.DOWN);
            case 'a' -> new MoveCommand(Direction.LEFT);
            case 'd' -> new MoveCommand(Direction.RIGHT);
            case ' ' -> new PlaceBombCommand();
            default -> null;
        };
    }
}
