package com.keer.mydb;

import com.keer.mydb.GUI.bigchaindb;

import javax.swing.*;

public class StartMain extends JFrame {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        bigchaindb bigchaindb=new bigchaindb();
    }


}