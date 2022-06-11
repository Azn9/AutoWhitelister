package dev.azn9.autowhitelister.gui;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WaitingKeypressGui {

    private final MainGui mainGui;
    private final List<String> toWhitelist;
    private final String whitelistCommand;
    private final double delay;

    private JPanel contentPane;
    private JButton cancelButton;
    private JLabel instructionsLabel;

    private JDialog dialog;
    private TimerTask timerTask;
    private boolean waitingChatKeypress = false;
    private boolean whitelisting = false;

    public WaitingKeypressGui(MainGui mainGui, List<String> toWhitelist, String whitelistCommand, double delay) {
        this.mainGui = mainGui;
        this.toWhitelist = toWhitelist;
        this.whitelistCommand = whitelistCommand;
        this.delay = delay;
    }

    private void handleKeypress(NativeKeyEvent event) {
        if (event.getKeyCode() == NativeKeyEvent.VC_F7) {
            if (this.whitelisting) {
                if (this.timerTask != null) {
                    this.timerTask.cancel();
                    this.dialog.dispose();
                    this.mainGui.unhide();
                }
            } else {
                this.instructionsLabel.setText("Appuyez sur la touche utilisée pour ouvrir le chat");
                this.waitingChatKeypress = true;
            }
        } else if (this.waitingChatKeypress) {
            this.waitingChatKeypress = false;
            this.whitelisting = true;
            this.instructionsLabel.setText("Witelist en cours...\n\n0/" + this.toWhitelist.size() + "\n\nAppuyez sur F7 pour annuler");

            try {
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_ENTER);

                final int[] index = {0};
                this.timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (index[0] >= WaitingKeypressGui.this.toWhitelist.size()) {
                            WaitingKeypressGui.this.timerTask.cancel();
                            WaitingKeypressGui.this.dialog.dispose();
                            WaitingKeypressGui.this.mainGui.unhide();
                            return;
                        }

                        String toWhitelist = WaitingKeypressGui.this.toWhitelist.get(index[0]);
                        System.out.println("Whitelisting " + toWhitelist);

                        String whitelistCommand = WaitingKeypressGui.this.whitelistCommand.replace("{pseudo}", toWhitelist);
                        System.out.println("Whitelist command: " + whitelistCommand);

                        StringSelection stringSelection = new StringSelection(whitelistCommand);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, stringSelection);

                        try {
                            int chatKey = KeyEvent.getExtendedKeyCodeForChar(NativeKeyEvent.getKeyText(event.getKeyCode()).charAt(0));

                            robot.keyPress(chatKey);
                            robot.keyRelease(chatKey);

                            Thread.sleep(200);

                            robot.keyPress(KeyEvent.VK_CONTROL);
                            robot.keyPress(KeyEvent.VK_V);
                            Thread.sleep(100);

                            robot.keyRelease(KeyEvent.VK_V);
                            robot.keyRelease(KeyEvent.VK_CONTROL);

                            Thread.sleep(200);

                            robot.keyPress(KeyEvent.VK_ENTER);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        index[0]++;
                        WaitingKeypressGui.this.instructionsLabel.setText("Witelist en cours...\n\n" + index[0] + "/" + WaitingKeypressGui.this.toWhitelist.size() + "\n\nAppuyez sur F7 pour annuler");
                    }
                };
                new Timer().scheduleAtFixedRate(this.timerTask, (long) (this.delay * 1000), (long) (this.delay * 1000));
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void show() {
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
                @Override
                public void nativeKeyReleased(NativeKeyEvent event) {
                    WaitingKeypressGui.this.handleKeypress(event);
                }
            });
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }

        this.dialog = new JDialog();
        this.dialog.setContentPane(this.contentPane);
        this.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.dialog.setSize(600, 200);
        this.dialog.setResizable(false);
        this.dialog.setAlwaysOnTop(true);
        this.dialog.setVisible(true);

        this.cancelButton.addActionListener(e -> {
            this.dialog.dispose();
            this.mainGui.unhide();
        });

        this.dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                WaitingKeypressGui.this.dialog.dispose();
                WaitingKeypressGui.this.mainGui.unhide();
            }
        });
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
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        instructionsLabel = new JLabel();
        instructionsLabel.setAutoscrolls(false);
        instructionsLabel.setDoubleBuffered(false);
        instructionsLabel.setFocusCycleRoot(false);
        instructionsLabel.setText("Allez sur votre client et appuyez sur F7 (avec le chat fermé)");
        contentPane.add(instructionsLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Annuler");
        contentPane.add(cancelButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        contentPane.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        contentPane.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
