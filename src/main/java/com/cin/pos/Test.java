package com.cin.pos;

import com.cin.pos.util.JSONUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        List<Hero> _list = new ArrayList<>();
        _list.add(new Hero("hous", 28));
        _list.add(new Hero("czlyj", 25));
        _list.add(new Hero("fold", 22));
        Map<String, Hero> map = new HashMap<>();
//        map.put("1", _list.get(0));
//        map.put("2", _list.get(1));
//        map.put("3", _list.get(2));
        String json = JSONUtils.toJson(_list);
        List<Hero> heroes = JSONUtils.toList(json, Hero.class);
        System.out.println(heroes);
    }


    public static class Hero {
        private String name;

        private int age;

        public Hero(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
