package com.predictX.interview;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MaxCharCounter {

    // take count of only lowercase characters
    // if count is 1 of character ignore it
    // s1 = "A aaaa bb c"
    // s2 = "& aaa bbb c d"
    // res = "1:aaaa/2:bbb"

    //   "=:"
    // res should be in decreasing order of their length
    // alphabetically; the different groups will be separated by '/'.

    /*
    s1 = "my&friend&Paul has heavy hats! &"
    s2 = "my friend John has many many friends &"
    mix(s1, s2) --> "2:nnnnn/1:aaaa/1:hhh/2:mmm/2:yyy/2:dd/2:ff/2:ii/2:rr/=:ee/=:ss"

    s1 = "mmmmm m nnnnn y&friend&Paul has heavy hats! &"
    s2 = "my frie n d Joh n has ma n y ma n y frie n ds n&"
    mix(s1, s2) --> "1:mmmmmm/=:nnnnnn/1:aaaa/1:hhh/2:yyy/2:dd/2:ff/2:ii/2:rr/=:ee/=:ss"

    s1="Are the kids at home? aaaaa fffff"
    s2="Yes they are here! aaaaa fffff"
    mix(s1, s2) --> "=:aaaaaa/2:eeeee/=:fffff/1:tt/2:rr/=:hh"

     */
    public String mix(String s1, String s2) {

        Map<Character, Integer> str1CharToCountMap = charCountFunc(s1).entrySet().stream().filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<Character, Integer> str2CharToCountMap = charCountFunc(s2).entrySet().stream().filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<Integer, TreeSet<String>> charCountToTreeSetMap = populateHighestCountCharMap(str1CharToCountMap, str2CharToCountMap);
        System.out.println(charCountToTreeSetMap);

        StringBuilder resMixStrBuilder = new StringBuilder();
        Iterator<Map.Entry<Integer, TreeSet<String>>> charCountToTreeSetIt = charCountToTreeSetMap.entrySet().iterator();
        while (charCountToTreeSetIt.hasNext()) {
            Map.Entry<Integer, TreeSet<String>> countToTreeSetEntry = charCountToTreeSetIt.next();
            Integer count = countToTreeSetEntry.getKey();
            TreeSet<String> charStrNumSet = countToTreeSetEntry.getValue();
            Iterator<String> charStrNumSetIt = charStrNumSet.iterator();
            for (String charStrNum : charStrNumSet) {
                char character = charStrNum.charAt(0);
                char num = charStrNum.charAt(1);
                resMixStrBuilder.append(num).append(":");
                for (int i = 0; i < count; i++) {
                    resMixStrBuilder.append(character);
                    if(i == count-1){
                        resMixStrBuilder.append("/");
                    }
                }
            }
        }
        return resMixStrBuilder.toString();
    }

    private Map<Integer, TreeSet<String>>
    populateHighestCountCharMap(Map<Character, Integer> str1CountMap, Map<Character, Integer> str2CountMap) {

        Iterator<Map.Entry<Character, Integer>> str1CountMapIter = str1CountMap.entrySet().iterator();

        Map<Integer, TreeSet<String>> countToCharSetMap = new TreeMap<>(Collections.reverseOrder());

        Set<Character> str2CharSet = str2CountMap.keySet();
        while (str1CountMapIter.hasNext()) {

            Map.Entry<Character, Integer> str1CountEntry = str1CountMapIter.next();
            Character str1Char = str1CountEntry.getKey();
            int str1Count = str1CountEntry.getValue();

            TreeSet<String> orderedSet = new TreeSet<>();
            if (str2CharSet.contains(str1Char)) {
                int str2Count = str2CountMap.get(str1Char);
                if (str1Count > str2Count) {
                    orderedSet.add(str1Char + "1:");
                    if (countToCharSetMap.get(str1Count) != null) {
                        countToCharSetMap.get(str1Count).add(str1Char + "1:");
                    } else {
                        countToCharSetMap.put(str1Count, orderedSet);
                    }
                } else if (str1Count < str2Count) {
                    orderedSet.add(str1Char + "2:");
                    if (countToCharSetMap.get(str2Count) != null) {
                        countToCharSetMap.get(str2Count).add(str1Char + "2:");
                    } else {
                        countToCharSetMap.put(str2Count, orderedSet);
                    }
                } else {

                    orderedSet.add(str1Char + "=:");
                    if (countToCharSetMap.get(str1Count) != null) {
                        countToCharSetMap.get(str1Count).add(str1Char + "=:");
                    } else {
                        countToCharSetMap.put(str1Count, orderedSet);
                    }
                }
            } else {
                orderedSet.add(str1Char + "1:");
                if (countToCharSetMap.get(str1Count) != null) {
                    countToCharSetMap.get(str1Count).add(str1Char + "1:");
                } else {
                    countToCharSetMap.put(str1Count, orderedSet);
                }
            }
        }

        Set<Character> str1CharSet = str1CountMap.keySet();
        Iterator<Map.Entry<Character, Integer>> str2CountMapIter = str2CountMap.entrySet().stream()
                .filter(entry -> !str1CharSet.contains(entry.getKey())).iterator();

        while (str2CountMapIter.hasNext()) {
            TreeSet<String> orderedSet = new TreeSet<>();
            Map.Entry<Character, Integer> str2CountEntry = str2CountMapIter.next();
            char str2Char = str2CountEntry.getKey();
            int str2Count = str2CountEntry.getValue();
            orderedSet.add(str2Char + "2:");
            if (countToCharSetMap.get(str2Count) != null) {
                countToCharSetMap.get(str2Count).add(str2Char + "2:");
            } else {
                countToCharSetMap.put(str2Count, orderedSet);
            }

        }
        return countToCharSetMap;
    }


    private Map<Character, Integer> charCountFunc(String str) {
        Map<Character, Integer> charCount = new TreeMap<>();
        for (char ch : str.toCharArray()) {
            if (Character.isAlphabetic(ch) && Character.isLowerCase(ch)) {
                charCount.computeIfPresent(ch, (key, val) -> val + 1);
                charCount.putIfAbsent(ch, 1);
            }
        }
        return charCount;
    }
}