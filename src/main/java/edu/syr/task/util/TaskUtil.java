package edu.syr.task.util;

import edu.syr.task.model.State;

import java.util.List;
import java.util.stream.Collectors;

public class TaskUtil {



    /**
     * Compares two strings for equality regardless of case.
     *
     * @param str1 The first string.
     * @param str2 The second string.
     * @return true if the strings are equal (case-insensitive), otherwise false.
     */
    public static boolean areStringsEqualIgnoreCase(String str1, String str2) {

        if (str1 == null && str2 == null) {
            return true;
        }

        if (str1 == null || str2 == null) {
            return false;
        }

        return str1.equalsIgnoreCase(str2);
    }


    /**
     * Compares two task states for equality.
     *
     * @param state1 The first state.
     * @param state2 The second state.
     * @return true if the states are equal, otherwise false.
     */

    public static boolean areTaskStates(State state1, State state2) {
        if (state1 == null && state2 == null) {
            return state1 == state2;
        }
        return state1.name().equalsIgnoreCase(state2.name());
    }

    /**
     * Compares two task descriptions for equality.
     *
     * @param description The first description.
     * @param description1 The second description.
     * @return true if the descriptions are equal, otherwise false.
     */
    public static boolean aredescription(String description, String description1) {

        if (description1 == null) {
            description1="";
        }


        return description.equalsIgnoreCase(description1);
    }

    public static boolean areListsEqualIgnoreCase(List<String> list1, List<String> list2) {
        if (list1 == null || list2 == null) {
            return list1 == list2;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        List<String> lowerCaseList1 = list1.stream().map(String::toLowerCase).collect(Collectors.toList());
        List<String> lowerCaseList2 = list2.stream().map(String::toLowerCase).collect(Collectors.toList());
        return lowerCaseList1.containsAll(lowerCaseList2);
    }


    /**
     * Compares two due dates for equality.
     *
     * @param dueDate The first due date.
     * @param dueDate1 The second due date.
     * @return true if the due dates are equal, otherwise false.
     */

    public static boolean areDueDatesSame(String dueDate, String dueDate1) {
        // If both are null, they are same
        if (dueDate == null && dueDate1 == null) {
            return true;
        }

        if (dueDate1 == null || dueDate1.isEmpty()) {
            return false;
        }

        if ((dueDate == null && dueDate1 != null) || (dueDate != null && dueDate1 == null)) {
            return false;
        }

        return dueDate.equals(dueDate1);

    }






}
