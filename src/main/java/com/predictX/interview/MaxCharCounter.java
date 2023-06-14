package com.predictX.interview;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MaxCharCounter {
    public String mix(String s1, String s2) {

        Map<Character, Integer> str1CharToCountMap = charCountFunc(s1).entrySet().stream().filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<Character, Integer> str2CharToCountMap = charCountFunc(s2).entrySet().stream().filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<Integer, TreeSet<String>> charCountToTreeSetMap = populateHighestCountCharMap(str1CharToCountMap, str2CharToCountMap);

        StringBuilder resMixStrBuilder = formatResultantString(charCountToTreeSetMap);
        return resMixStrBuilder.deleteCharAt(resMixStrBuilder.length()-1).toString();
    }

    private StringBuilder formatResultantString(Map<Integer, TreeSet<String>> charCountToTreeSetMap) {
        StringBuilder resMixStrBuilder = new StringBuilder();
        for (Map.Entry<Integer, TreeSet<String>> countToTreeSetEntry : charCountToTreeSetMap.entrySet()) {
            Integer count = countToTreeSetEntry.getKey();
            TreeSet<String> charStrNumSet = countToTreeSetEntry.getValue();
            StringBuilder equalStrBuilder = new StringBuilder();
            for (String charStrNum : charStrNumSet) {
                char character = charStrNum.charAt(0);
                char num = charStrNum.charAt(1);
                if(num == '='){
                    formatter(equalStrBuilder, count, character, num);
                }else {
                    formatter(resMixStrBuilder, count, character, num);
                }
            }
            resMixStrBuilder.append(equalStrBuilder);
        }
        return resMixStrBuilder;
    }

    private void formatter(StringBuilder resMixStrBuilder, Integer count, char character, char num) {
        resMixStrBuilder.append(num).append(":");
        for (int i = 0; i < count; i++) {
            resMixStrBuilder.append(character);
            if (i == count - 1) {
                resMixStrBuilder.append("/");
            }
        }
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