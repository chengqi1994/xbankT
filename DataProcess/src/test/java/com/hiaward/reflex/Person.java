package com.hiaward.reflex;

public class Person extends Parent{  
    private int personId;  
    public String personName;  
      
    public Person(){  
          
    }  
      
    public Person(String personName){  
        this.parentName=personName;  
    }  
      
    private Person(int personId){  
        this.personId=personId;  
    }  
      
    private int mul(int a,int b){  
        return a*b;  
    }  
      
    protected int del(int a,int b){  
        return a/b;  
    }  
      
    public int add(int a,int b){  
        return a+b;  
    }  
}  