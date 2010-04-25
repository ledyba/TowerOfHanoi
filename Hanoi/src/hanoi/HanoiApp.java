package hanoi;

import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Dimension;

/**
 * <p>�^�C�g��: �n�m�C�̓��@�����񓚃v���O����</p>
 *
 * <p>����: �Q�[�����Ԃɍ���Ȃ������̂ŁB�B�B�B</p>
 *
 * <p>���쌠: Copyright (c) 2007 PSI</p>
 *
 * <p>��Ж�: Pegasus</p>
 *
 * @author ������
 * @version 1.0
 */
public class HanoiApp {
    boolean packFrame = false;

    /**
     * �A�v���P�[�V�����̍\�z�ƕ\���B
     */
    public HanoiApp() {
        HanoiFrame frame = new HanoiFrame();
        // validate() �̓T�C�Y�𒲐�����
        // pack() �͗L���ȃT�C�Y�������C�A�E�g�Ȃǂ���擾����
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }

        // �E�B���h�E�𒆉��ɔz�u
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2,
                          (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }

    /**
     * �A�v���P�[�V�����G���g���|�C���g�B
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.
                                             getSystemLookAndFeelClassName());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                new HanoiApp();
            }
        });
    }
}