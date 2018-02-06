package org.jsxnh.view.test;

public class TestObject {


    public void setName(){
        user u = new user();
        u.setName("jsxnh");
        System.out.println(u);
        change(u);
        System.out.println(u);
    }

    public void change(user u){
        u.setName("jds");
    }

    public static void main(String[] args){

        new TestObject().setName();
    }



    private class user{
        private String name;
        public void setName(String s){
            name = s;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
