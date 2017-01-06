package com.timeout.chatbot;

public enum Direction {

    NORTH, EAST, SOUTH, WEST;

    public static void main(String[] args) {
        // Iterate through all direction values
        for (Direction direction : Direction.values()) {
            System.out.println(
                "Direction ordinal=" + direction.ordinal()
                    + " Identifier=" + direction.name());
        }

        // Given identifier, find corresponding direction instance.
        String directionIdentifier = "EAST";
        Direction direction = Direction.valueOf(directionIdentifier);
        System.out.println("\nDirection identifier '"
            + directionIdentifier + "' produces terrain "
            + direction.name());
    }

}