package hanoi;

import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Font;
import hanoi.game.Field;
import java.awt.Color;
import hanoi.game.Ring;
import hanoi.game.Tower;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.Timer;
import java.util.TimerTask;


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
public class HanoiPanel extends JPanel {
    private Timer RepaintTimer;
    private Field Field;
    private HanoiSearchThread SearchThread;
    private boolean isSearchStart = false;
    private boolean isFinished = false;
    public HanoiPanel() {
        super();
    }

    private long StartTimeInMills;
    private long FinishTime;
    public void start(int max_rings) {
        if (!isSearchStart) {
            //フィールド初期化
            Field = new Field(max_rings);
            //探索スレッド
            SearchThread = new HanoiSearchThread(Field, this);
            SearchThread.start();
            //変数設定
            isSearchStart = true;
            isFinished = false;
            Steps = 0;
            StartTimeInMills = System.currentTimeMillis();
        }
        if(RepaintTimer == null){
            //描画用タイマータスク
            RepaintTimer = new Timer();
            RepaintTimer.scheduleAtFixedRate(new RepaintHandle(this), 0,
                                             1000 / 60);
        }
    }
    public void stop() {
        if(RepaintTimer != null){
            RepaintTimer.cancel();
            RepaintTimer = null;
        }
    }

    /**
     * 再描画
     */
    private int FPS = 0;
    private int FPS_count = 0;
    private long FPS_lastTime = System.currentTimeMillis();
    private int Steps = 0;
    private static final Font FPSFont = new Font(null, Font.PLAIN, 12);
    private static final Font FinishedFont = new Font(null, Font.BOLD, 32);
    private static final Color FinishColor = Color.RED;
    public synchronized void paint(Graphics g) {
        final int width = this.getWidth();
        final int height = this.getHeight();
        //初期化
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        //タワー
        moveRings();
        drawTower(g, width, height);
        //終わった？
        if (SearchThread != null && SearchThread.hasFinished()) {
            isSearchStart = false;
            SearchThread = null;
            isFinished = true;
            FinishTime = (System.currentTimeMillis() - StartTimeInMills) / 1000;
        }
        if (isFinished) {
            g.setFont(FinishedFont);
            g.setColor(FinishColor);
            String str = "全部移動できました。";
            g.drawString(str, (width >> 1) - (str.length() * 16) - 16,
                         (height >> 1) - 32);
            int h = (int) (FinishTime / 3600);
            int m = (int) ((FinishTime % 3600) / 60);
            int s = (int) ((FinishTime % 60));
            str = "掛かった時間："
                  + ((h == 0) ? "" : Integer.toString(h) + "時間")
                  + ((h == 0 && m == 0) ? "" : Integer.toString(m) + "分")
                  + Integer.toString(s) + "秒";
            g.drawString(str, (width >> 1) - (str.length() * 16) - 16,
                         (height >> 1));

            str = "総ステップ数："+Integer.toString(Steps);
            g.drawString(str, (width >> 1) - (str.length() * 16) - 16,
                         (height >> 1) + 32);
        }
        //FPS
        FPS_count++;
        long now = System.currentTimeMillis();
        if (now - FPS_lastTime > 1000) {
            FPS_lastTime += 1000;
            FPS = FPS_count;
            FPS_count = 0;
        }
        g.setColor(Color.black);
        g.setFont(this.FPSFont);

        g.drawString(Integer.toString(Steps)+" Step(s)", 12, 24);

        g.drawString(Integer.toString(FPS) + " FPS", width - 64, 24);
    }

    private void drawTower(Graphics g, final int width, final int height) {
        /*タワー*/
        final int tower_width = (width >> 6) & ~1;
        final int tower_height = (height << 1) / 3;
        final int tower_width_space = width / (Field.MAX_TOWER);
        final int tower_start_y = (height - tower_height);
        int tower_x = -(tower_width_space >> 1);
        for (int i = 0; i < Field.MAX_TOWER; i++) {
            //タワー描画
            tower_x += tower_width_space;
            g.setColor(Color.BLACK);
            g.fillRect
                    (tower_x - (tower_width >> 1),
                     tower_start_y, tower_width, tower_height);
        }
        if (Field == null) {
            return;
        }
        /*リング*/
        //こうしないと描画順がおかしい
        tower_x = -(tower_width_space >> 1);

        final int max_rings = Field.getMaxRings();
        final int ring_width = (tower_width_space / (max_rings + 1)) & ~1;
        final int ring_height =
                Math.min(tower_height / max_rings, (tower_height >> 3));
        final int ring_up_y = tower_start_y >> 1;

        final int tower_dst_x;
        if (MoveRing) {
            tower_dst_x = (tower_width_space >> 1) +
                          (MoveTowerDst) * tower_width_space;
        } else {
            tower_dst_x = 0;
        }

        for (int i = 0; i < Field.MAX_TOWER; i++) {
            tower_x += tower_width_space;
            //リング描画
            int y_ring = height - (ring_height >> 1);
            Tower tower = Field.getTower(i);
            tower.resetIterator();
            Ring ring;
            while ((ring = tower.nextRing()) != null) {
                int size = ring.getSize();
                if (MoveRing && size == MoveRingSize && i == MoveTowerSrc) { //移動中
                    switch (MoveStage) {
                    case UP: {
                        int y = ((ring_up_y * MoveCount) +
                                 (y_ring * (MOVE_MAX - MoveCount))) / MOVE_MAX;
                        drawRing(g, size, tower_x, y,
                                 ring_width * ring.getSize(), ring_height);
                    }
                    break;
                    case SLIDE: {
                        int x = (tower_dst_x * MoveCount +
                                 tower_x * (MOVE_MAX - MoveCount)) / MOVE_MAX;
                        drawRing(g, size,
                                 x, ring_up_y,
                                 ring_width * ring.getSize(), ring_height);
                    }
                    break;
                    case DOWN: {
                        int dst_rings = Field.getTower(MoveTowerDst).getRings();
                        int dst_top_y = height - (ring_height >> 1)
                                        - (dst_rings * ring_height);
                        int y = ((dst_top_y * MoveCount) +
                                 (ring_up_y * (MOVE_MAX - MoveCount)))
                                / MOVE_MAX;
                        drawRing(g, size, tower_dst_x, y,
                                 ring_width * ring.getSize(), ring_height);
                    }
                    break;
                    default:
                        break;
                    }
                } else { //それ以外
                    drawRing(g, size, tower_x, y_ring,
                             ring_width * ring.getSize(), ring_height);
                }
                y_ring -= ring_height;
            }
        }
    }

    private static final Color RingColor[] = {
                                             Color.BLUE.darker(),
                                             Color.CYAN.darker(),
                                             Color.green.darker(),
                                             Color.magenta.darker(),
                                             Color.ORANGE.darker(),
                                             Color.PINK.darker(),
                                             Color.RED.darker(),
                                             Color.WHITE.darker(),
                                             Color.YELLOW.darker()
    };
    private static final Color RingColorB[] = {
                           Color.BLUE.brighter(),
                           Color.CYAN.brighter(),
                           Color.green.brighter(),
                           Color.magenta.brighter(),
                           Color.ORANGE.brighter(),
                           Color.PINK.brighter(),
                           Color.RED.brighter(),
                           Color.WHITE.brighter(),
                           Color.YELLOW.brighter()
    };
    private void drawRing(Graphics g, int size, int x, int y, int width,
                          int height) {
        int c_index = size % RingColor.length;
        g.setColor(RingColor[c_index]);
        g.fill3DRect(x - (width >> 1), y - (height >> 1), width, height, true);
        g.setColor(RingColorB[c_index]);
        g.setFont(FPSFont);
        g.drawString(Integer.toString(size),x-3,y+6);
    }

    private Lock MoveRingLock = new ReentrantLock();
    private Condition MoveRingCond = MoveRingLock.newCondition();
    private boolean MoveRing = false;
    private int MoveCount = 0;
    enum MOVE_STAGE {
        STOP, UP, SLIDE, DOWN
    }


    private MOVE_STAGE MoveStage = MOVE_STAGE.STOP;
    private static final int MOVE_MAX = 10;

    private int MoveTowerSrc;
    private int MoveTowerDst;
    private int MoveRingSize;

    private void moveRings() {
        if (MoveRing) {
            switch (MoveStage) {
            case STOP:
                break;
            case UP:
                MoveCount++;
                if (MoveCount > MOVE_MAX) {
                    MoveCount = 0;
                    MoveStage = MOVE_STAGE.SLIDE;
                }
                break;
            case SLIDE:
                MoveCount++;
                if (MoveCount > MOVE_MAX) {
                    MoveCount = 0;
                    MoveStage = MOVE_STAGE.DOWN;
                }
                break;
            case DOWN:
                MoveCount++;
                if (MoveCount > MOVE_MAX) {
                    //実際に動かす
                    Field.moveRing(MoveTowerSrc, MoveTowerDst, MoveRingSize);
                    Steps++;
                    //おわり
                    MoveRingLock.lock();
                    MoveRing = false;
                    MoveRingCond.signalAll();
                    MoveRingLock.unlock();
                }
                break;
            default:
                break;
            }
        }
    }

    public void moveRing(int src, int dst, int size) {
        //移動終わりまで待つ
        waitMoveRingCond();
        //動かすための条件変数のセット
        setMoveState(src, dst, size);
        //コンディション待ち
        waitMoveRingCond();
    }

    private void setMoveState(int src, int dst, int size) {
        MoveRing = true;
        MoveCount = 0;
        MoveStage = MOVE_STAGE.UP;
        MoveTowerSrc = src;
        MoveTowerDst = dst;
        MoveRingSize = size;
    }

    private void waitMoveRingCond() {
        MoveRingLock.lock();
        if (!MoveRing) {
            MoveRingLock.unlock();
            return;
        }
        try {
            MoveRingCond.await();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            MoveRingLock.unlock();
        }
    }
}


class RepaintHandle extends TimerTask {
    private JPanel Panel;
    public RepaintHandle(JPanel panel) {
        Panel = panel;
    }

    public void run() {
        Panel.repaint();
    }
}
