package dev.azn9.autowhitelister.gui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import dev.azn9.autowhitelister.Whitelister;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.JMarsDarkTheme;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainGui {

    private static final String URL_REGEX = "https://bots-api\\.azn9\\.dev/whitelist/\\w+/\\w+";

    private JPanel contentPane;

    private JPasswordField apiKeyField;
    private JTextField urlField;
    private JTextField whitelistCommandFiled;
    private JButton startButton;
    private JButton fileButton;

    private JDialog frame;

    public MainGui() {
        Whitelister whitelister = new Whitelister(this);

        this.fileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("TXT files", "txt"));

            int result = fileChooser.showOpenDialog(this.frame);

            if (result == JFileChooser.APPROVE_OPTION) {
                List<String> toWhitelist = new ArrayList<>();
                String whitelistCommand = this.whitelistCommandFiled.getText();

                File file = fileChooser.getSelectedFile();
                if (!file.exists()) {
                    JOptionPane.showMessageDialog(this.frame, "Le fichier n'existe pas", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.toURI().toURL().openStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.matches("[A-Za-z0-9_]{3,16}")) {
                            toWhitelist.add(line);
                        } else {
                            JOptionPane.showMessageDialog(this.frame, "Le fichier contient des pseudos invalides : " + line, "Erreur", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this.frame, "Une erreur est survenue : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (whitelistCommand.isEmpty()) {
                    JOptionPane.showMessageDialog(this.contentPane, "Veuillez entrer la commande de whitelisting.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                this.frame.setVisible(false);
                whitelister.start(toWhitelist, whitelistCommand);
            }
        });

        this.startButton.addActionListener(e -> {
            String url = this.urlField.getText();
            String apiKey = new String(this.apiKeyField.getPassword());
            String whitelistCommand = this.whitelistCommandFiled.getText();

            if (url.isEmpty()) {
                JOptionPane.showMessageDialog(this.contentPane, "Veuillez entrer une URL.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!url.matches(MainGui.URL_REGEX)) {
                JOptionPane.showMessageDialog(this.contentPane, "L'URL n'est pas valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (apiKey.isEmpty()) {
                JOptionPane.showMessageDialog(this.contentPane, "Veuillez entrer votre clé d'API.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (whitelistCommand.isEmpty()) {
                JOptionPane.showMessageDialog(this.contentPane, "Veuillez entrer la commande de whitelisting.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            this.frame.setVisible(false);
            whitelister.start(this.contentPane, url, apiKey, whitelistCommand);
        });
    }

    public void show() {
        this.frame = new JDialog((Dialog) null, "AutoWhitelister");
        this.frame.setUndecorated(true);
        this.frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icon.png")));
        this.frame.setSize(400, 500);
        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.setContentPane(this.contentPane);
        this.frame.setVisible(true);

        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void unhide() {
        this.frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(11, 3, new Insets(0, 0, 0, 0), -1, -1));
        startButton = new JButton();
        startButton.setText("Démarrer");
        contentPane.add(startButton, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("URL des pseudos");
        contentPane.add(label1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        urlField = new JTextField();
        urlField.setText("");
        contentPane.add(urlField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        contentPane.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        contentPane.add(spacer4, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Clé d'API");
        contentPane.add(label2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        apiKeyField = new JPasswordField();
        contentPane.add(apiKeyField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Commande de whitelist (avec le / si besoin)");
        contentPane.add(label3, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        whitelistCommandFiled = new JTextField();
        whitelistCommandFiled.setText("/whitelist add {pseudo}");
        contentPane.add(whitelistCommandFiled, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer5 = new Spacer();
        contentPane.add(spacer5, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        fileButton = new JButton();
        fileButton.setText("Lire depuis un fichier");
        contentPane.add(fileButton, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
