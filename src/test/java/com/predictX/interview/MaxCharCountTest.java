package com.predictX.interview;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaxCharCountTest {

    @Test
    public void test1() {
        MaxCharCounter maxCharCounter = new MaxCharCounter();
        String character = maxCharCounter.mix("A aaaa bb c", "& aaa bbb c d");
        assertEquals("1:aaaa/2:bbb", character);
    }

    @Test
    public void test2() {
        MaxCharCounter maxCharCounter = new MaxCharCounter();
        String charcter = maxCharCounter.mix("my&friend&Paul has heavy hats! &", "my friend John has many many friends &");
        assertEquals("2:nnnnn/1:aaaa/1:hhh/2:mmm/2:yyy/2:dd/2:ff/2:ii/2:rr/=:ee/=:ss", charcter);
    }

    @Test
    public void test3() {
        MaxCharCounter maxCharCounter = new MaxCharCounter();
        System.out.println(maxCharCounter.mix("mmmmm m nnnnn y&friend&Paul has heavy hats! &", "my frie n d Joh n has ma n y ma n y frie n ds n&"));
        String character = maxCharCounter.mix("mmmmm m nnnnn y&friend&Paul has heavy hats! &", "my frie n d Joh n has ma n y ma n y frie n ds n&");
        assertEquals("1:mmmmmm/=:nnnnnn/1:aaaa/1:hhh/2:yyy/2:dd/2:ff/2:ii/2:rr/=:ee/=:ss", character);

    }

    @Test
    public void test4() {
        MaxCharCounter maxCharCounter = new MaxCharCounter();
        String character = maxCharCounter.mix("Are the kids at home? aaaaa fffff", "Yes they are here! aaaaa fffff");
        assertEquals("=:aaaaaa/2:eeeee/=:fffff/2:rr/1:tt/=:hh", character);
    }
}
