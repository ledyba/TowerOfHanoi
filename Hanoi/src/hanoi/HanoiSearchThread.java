package hanoi;

import hanoi.game.Field;

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
public class HanoiSearchThread extends Thread {
    private final int MaxRings;
    HanoiPanel Hanoi;
    public HanoiSearchThread(Field field,HanoiPanel hanoi){
        Hanoi = hanoi;
        MaxRings = field.getMaxRings();
    }
    private boolean finished = false;
    public void run(){
        search(MaxRings,0,1,2);
        finished = true;
    }
    public boolean hasFinished(){
        return finished;
    }
    /**
     * A����C�Ɉړ�����B
     * @param n int
     * @param a int
     * @param b int
     * @param c int
     */
    private void search(int n,int a,int b,int c){
        if(n > 0){
            //a�́A�ړ���������̕�����b�Ɉړ����Ă����B
            search(n - 1, a, c, b);
            //move a to c
            Hanoi.moveRing(a,c,n);
            //b��C�ֈړ�����B
            search(n - 1, b, a, c);
        }
    }
}
