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

        Map<Map<Character, String>, Integer> charStrNumToCountMap = populateHighestCountCharMap(str1CharToCountMap, str2CharToCountMap);

        Map<Map<Character, String>, Integer> charStrNumToCountDescMap = entriesSortedByValues(charStrNumToCountMap);


        StringBuilder resMixStrBuilder = new StringBuilder();
        Iterator<Map.Entry<Map<Character, String>, Integer>> it = charStrNumToCountDescMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Map<Character, String>, Integer> charStrNumToCount = it.next();
            Map<Character, String> characterToStrNum = charStrNumToCount.getKey();
            Set<Character> set = characterToStrNum.keySet();
            Character character = set.iterator().next();
            String strNum = String.valueOf(characterToStrNum.get(character));
            Integer charToStrNumCount = charStrNumToCount.getValue();
            resMixStrBuilder.append(strNum);
            for (int i = 0; i < charToStrNumCount; i++) {
                resMixStrBuilder.append(character);
            }
            if (it.hasNext()) {
                resMixStrBuilder.append("/");
            }
        }
        return resMixStrBuilder.toString();
    }

    private Map<Map<Character, String>, Integer>
    populateHighestCountCharMap(Map<Character, Integer> str1CountMap, Map<Character, Integer> str2CountMap) {

        Iterator<Map.Entry<Character, Integer>> str1CountMapIter = str1CountMap.entrySet().iterator();

        Map<Map<Character, String>, Integer> charStrNumToCountMap = new HashMap<>();

        Set<Character> str2CharSet = str2CountMap.keySet();
        while (str1CountMapIter.hasNext()) {

            Map.Entry<Character, Integer> str1CountEntry = str1CountMapIter.next();
            Character str1Char = str1CountEntry.getKey();
            int str1Count = str1CountEntry.getValue();

            Map<Character, String> charToStrNumMap = new HashMap<>();
            if (str2CharSet.contains(str1Char)) {
                int str2CharCount = str2CountMap.get(str1Char);
                if (str1Count > str2CharCount) {
                    charToStrNumMap.put(str1Char, "1:");
                    charStrNumToCountMap.put(charToStrNumMap, str1Count);
                } else if (str1Count < str2CharCount) {
                    charToStrNumMap.put(str1Char, "2:");
                    charStrNumToCountMap.put(charToStrNumMap, str2CharCount);
                } else {
                    charToStrNumMap.put(str1Char, "=:");
                    charStrNumToCountMap.put(charToStrNumMap, str1Count);
                }
            } else {
                charToStrNumMap.put(str1Char, "1:");
                charStrNumToCountMap.put(charToStrNumMap, str1Count);
            }
        }

        Set<Character> str1CharSet = str1CountMap.keySet();
        Iterator<Map.Entry<Character, Integer>> str2CountMapIter = str2CountMap.entrySet().stream()
                .filter(entry -> !str1CharSet.contains(entry.getKey())).iterator();

        while (str2CountMapIter.hasNext()) {
            Map<Character, String> charToStrNumMap = new HashMap<>();
            Map.Entry<Character, Integer> str2CountEntry = str2CountMapIter.next();
            charToStrNumMap.put(str2CountEntry.getKey(), "2:");
            charStrNumToCountMap.put(charToStrNumMap, str2CountEntry.getValue());
        }
        return charStrNumToCountMap;
    }

    private <K, V extends Comparable<? super V>> Map<K, V> entriesSortedByValues(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        list.sort(new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
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