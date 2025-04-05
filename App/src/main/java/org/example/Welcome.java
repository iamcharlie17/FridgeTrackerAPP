package org.example;

public class Welcome {
    public void printWelcome(){
        String bold = "\033[1m";  
        String reset = "\033[0m"; 

        System.out.println();
        System.out.println("==============================================================");
        System.out.println("||                                                          ||");
        System.out.println("||" + bold + "                   FRIDGE TRACKER APP!                   " + reset + " ||");
        System.out.println("||                                                          ||");
        System.out.println("==============================================================");
        System.out.println();
    }
}
