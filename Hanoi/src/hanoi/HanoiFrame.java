package hanoi;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>タイトル: ハノイの塔　自動回答プログラム</p>
 *
 * <p>説明: ゲームが間に合わなかったので。。。。</p>
 *
 * <p>著作権: Copyright (c) 2007 PSI</p>
 *
 * <p>会社名: Pegasus</p>
 *
 * @author 未入力
 * @version 1.0
 */
public class HanoiFrame extends JFrame {
    public static final Image WinIcon = Toolkit.getDefaultToolkit().createImage(
            hanoi.HanoiFrame.class.getResource("icon.png"));
    JPanel contentPane;
    BorderLayout borderLayout1 = new BorderLayout();
    HanoiPanel DrawnPanel = new HanoiPanel();
    JPanel StatusPanel = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JButton StartButton = new JButton();
    JButton StopButton = new JButton();
    JTextField RingsField = new JTextField();

    public HanoiFrame() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * コンポーネントの初期化。
     *
     * @throws java.lang.Exception
     */
    private void jbInit() throws Exception {
        setIconImage(WinIcon);
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(borderLayout1);
        setSize(new Dimension(640, 480));
        setTitle("ハノイの塔");
        StatusPanel.setLayout(gridBagLayout1);
        StartButton.setText("Start");
        StartButton.addActionListener(new HanoiFrame_StartButton_actionAdapter(this));
        StopButton.setText("Stop");
        StopButton.addActionListener(new HanoiFrame_StopButton_actionAdapter(this));
        RingsField.setText("");
        contentPane.add(DrawnPanel, java.awt.BorderLayout.CENTER);
        contentPane.add(StatusPanel, java.awt.BorderLayout.SOUTH);
        StatusPanel.add(StartButton,
                        new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.NONE,
                                               new Insets(0, 0, 0, 0), 0, 0));
        StatusPanel.add(StopButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        StatusPanel.add(RingsField,
                        new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.HORIZONTAL,
                                               new Insets(0, 0, 0, 0), 0, 0));
    }

    public void StartButton_actionPerformed(ActionEvent e) {
        DrawnPanel.start(Integer.parseInt(RingsField.getText()));
    }

    public void StopButton_actionPerformed(ActionEvent e) {
        DrawnPanel.stop();
    }
}


class HanoiFrame_StopButton_actionAdapter implements ActionListener {
    private HanoiFrame adaptee;
    HanoiFrame_StopButton_actionAdapter(HanoiFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.StopButton_actionPerformed(e);
    }
}


class HanoiFrame_StartButton_actionAdapter implements ActionListener {
    private HanoiFrame adaptee;
    HanoiFrame_StartButton_actionAdapter(HanoiFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.StartButton_actionPerformed(e);
    }
}
