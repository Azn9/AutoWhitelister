package dev.azn9.autowhitelister;

import dev.azn9.autowhitelister.gui.MainGui;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.JMarsDarkTheme;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame.setDefaultLookAndFeelDecorated(false);
        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel(new JMarsDarkTheme()));
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        new MainGui().show();
    }

}
