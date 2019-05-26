package com.example.patryk.hairfire;

import java.util.Comparator;

public class DateComparator implements Comparator<Visit> {
    @Override
    public int compare(Visit o1, Visit o2) {
        return o1.getDecodedDate().compareTo(o2.getDecodedDate());
    }
}