package edu.syr.task.util;

import edu.syr.task.model.State;

public class TaskUtil {

    public static boolean areStringsEqualIgnoreCase(String str1, String str2) {

        return str1.equalsIgnoreCase(str2.toLowerCase());
    }

    public static boolean areTaskStates(State state1, State state2) {
        if (state1 == null && state2 == null) {
            return state1 == state2;
        }
        return state1.name().equalsIgnoreCase(state2.name());
    }

    public static boolean aredescription(String description, String description1) {
        if (description == null || description1 == null) {
            return description.equals(description1);
        }
        return description.equalsIgnoreCase(description1);
    }

}
