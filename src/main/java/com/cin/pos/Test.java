package com.cin.pos;

public class Test {

    public static void main(String[] args) {
//        List<Hero> _list = new ArrayList<>();
//        _list.add(new Hero("hous", 28));
//        _list.add(new Hero("czlyj", 25));
//        _list.add(new Hero("fold", 22));
//        Map<String, Hero> map = new HashMap<>();
//        map.put("1", _list.get(0));
//        map.put("2", _list.get(1));
//        map.put("3", _list.get(2));
//        String json = JSONUtils.toJson(map);
//        Map<String, Hero> heroMap = JSONUtils.toMap(json, Hero.class);
//        System.out.println(heroMap);

        String oldVersion1 = "1.1.1";

        String newVersion2 = "2.2.12";

        System.out.println(oldVersion1.compareTo(newVersion2) < 0 ? "需要更新" : "不需要更新");
    }


    public static class Hero {
        private String name;

        private int age;

        public Hero() {
        }

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
