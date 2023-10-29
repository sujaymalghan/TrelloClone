package edu.syr.task.util;

import edu.syr.task.model.State;

public class TaskUtil {

    public static boolean areStringsEqualIgnoreCase(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return str1 == str2;
        }
        return str1.toLowerCase().equals(str2.toLowerCase());
    }

    public static boolean areTaskStates(State state1, State state2) {
        if (state1 == null || state2 == null) {
            return state1 == state2;
        }
        return state1.name().equalsIgnoreCase(state2.name());
    }
}
