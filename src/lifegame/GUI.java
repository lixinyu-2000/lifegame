package lifegame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**GUIΪϸ���࣬�Ե���ϸ����ķ�����ʵ��������Ϸ���̵Ŀ��ӻ�.*/
public class GUI extends JFrame implements ActionListener {
    /**����.*/
    private static GUI frame;
    /**ϸ��.*/
    private Cell cell;
    /**���Ϳ�.*/
    private int maxLength, maxWidth;
    /**һ����ť��ʾһ��ϸ��.*/
    private JButton[][] nGrid;
    /**��ť�Ƿ�ѡ��.*/
    private boolean[][] isSelected;
    /**ȷ������ǰ��������������.*/
    private JButton ok, jbNowGeneration, randomInit, clearGeneration;
    /**��һ������ʼ���ܣ���ͣ���˳�.*/
    private JButton clearCell, nextGeneration, start, stop, exit;
    /**����ѡ��.*/
    private JComboBox lengthList, widthList;
    /**�߳�.*/
    private Thread thread;
    /**�߳��Ƿ�������.*/
    private boolean isRunning;
    /**ϸ���Ƿ�����.*/
    private boolean isDead;

    /**
     * �������.
     * @param args ���������в���
     */
    public static void main(final String[] args) {
        frame = new GUI("������Ϸ");
    }

    /**
     * ʵ�����ݷ�װ.
     * @return ���
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * ʵ�����ݷ�װ.
     * @param maxWid ���
     */
    public void setMaxWidth(final int maxWid) {
        this.maxWidth = maxWid;
    }

    /**
     * ʵ�����ݷ�װ.
     * @return ����
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * ʵ�����ݷ�װ.
     * @param maxLen ���
     */
    public void setMaxLength(final int maxLen) {
        this.maxLength = maxLen;
    }

    /**��ʼ������.*/
    public void initGUI() {
        final int wid = 20; //����Ĭ�ϵĿ��
        final int len = 35; //����Ĭ�ϵĳ���
        if (maxWidth == 0) {
            maxWidth = wid;
        }
        if (maxLength == 0) {
            maxLength = len;
        }

        cell = new Cell(maxWidth, maxLength);

        JPanel backPanel, centerPanel, bottomPanel;
        JLabel jWidth, jLength, jNowGeneration;
        backPanel = new JPanel(new BorderLayout());
        centerPanel = new JPanel(new GridLayout(maxWidth, maxLength));
        bottomPanel = new JPanel();
        this.setContentPane(backPanel);
        backPanel.add(centerPanel, "Center");
        backPanel.add(bottomPanel, "South");

        nGrid = new JButton[maxWidth][maxLength];
        isSelected = new boolean[maxWidth][maxLength];
        for (int i = 0; i < maxWidth; i++) {
            for (int j = 0; j < maxLength; j++) {
                nGrid[i][j] = new JButton(""); //��ť�����ÿ��Ա�ʾϸ��
                nGrid[i][j].setBackground(Color.WHITE); //��ʼʱ����ϸ����Ϊ��
                centerPanel.add(nGrid[i][j]);
            }
        }
        final int minNum = 3; //������Сֵ
        final int maxNum = 100; //�������ֵ
        jLength = new JLabel("���ȣ�");
        lengthList = new JComboBox();
        for (int i = minNum; i <= maxNum; i++) {
            lengthList.addItem(String.valueOf(i));
        }
        lengthList.setSelectedIndex(maxLength - minNum);

        jWidth = new JLabel("��ȣ�");
        widthList = new JComboBox();
        for (int i = minNum; i <= maxNum; i++) {
            widthList.addItem(String.valueOf(i));
        }
        widthList.setSelectedIndex(maxWidth - minNum);

        ok = new JButton("ȷ��");
        jNowGeneration = new JLabel(" ��ǰ������");
        //button��ť����ֱ�����int���ʲ��ô˷�ʽ
        jbNowGeneration = new JButton("" + cell.getNowGeneration());
        jbNowGeneration.setEnabled(false);
        clearGeneration = new JButton("��������");
        randomInit = new JButton("�����ʼ��");
        clearCell = new JButton("ϸ������");
        start = new JButton("��ʼ����");
        nextGeneration = new JButton("��һ��");
        stop = new JButton("��ͣ");
        exit = new JButton("�˳�");

        bottomPanel.add(jLength);
        bottomPanel.add(lengthList);
        bottomPanel.add(jWidth);
        bottomPanel.add(widthList);
        bottomPanel.add(ok);
        bottomPanel.add(jNowGeneration);
        bottomPanel.add(jbNowGeneration);
        bottomPanel.add(clearGeneration);
        bottomPanel.add(randomInit);
        bottomPanel.add(clearCell);
        bottomPanel.add(start);
        bottomPanel.add(nextGeneration);
        bottomPanel.add(stop);
        bottomPanel.add(exit);


        // ���ô���
        final int length = 1000; //���泤��
        final int height = 650; //������
        this.setSize(length, height);
        this.setResizable(true);
        this.setLocationRelativeTo(null); // �ô�������Ļ����
        this.setVisible(true); // ����������Ϊ�ɼ���

        // ע�������
        this.addWindowListener(new WindowAdapter() {
            public void windowClosed(final WindowEvent e) {
                System.exit(0);
            }
        });
        ok.addActionListener(this);
        clearGeneration.addActionListener(this);
        randomInit.addActionListener(this);
        clearCell.addActionListener(this);
        nextGeneration.addActionListener(this);
        start.addActionListener(this);
        stop.addActionListener(this);
        exit.addActionListener(this);
        for (int i = 0; i < maxWidth; i++) {
            for (int j = 0; j < maxLength; j++) {
                nGrid[i][j].addActionListener(this);
            }
        }
    }

    /**
     * �½�����.
     * @param name  �������
     */
    public GUI(final String name) {
        super(name);
        initGUI();
    }

    /**
     * ���ղ����¼�.
     * @param e �����¼�
     */
    public void actionPerformed(final ActionEvent e) {
        final int minNum = 3; //������Сֵ
        final int sleeptime = 500; //�߳�˯�ߵ�ʱ����
        if (e.getSource() == ok) { //ȷ��
            frame.setMaxLength(lengthList.getSelectedIndex() + minNum);
            frame.setMaxWidth(widthList.getSelectedIndex() + minNum);
            initGUI();

            cell = new Cell(getMaxWidth(), getMaxLength());

        } else if (e.getSource() == clearGeneration) { //��������
            cell.setNowGeneration(0);
            jbNowGeneration.setText("" + cell.getNowGeneration()); //ˢ�µ�ǰ����
            isRunning = false;
            thread = null;
        } else if (e.getSource() == randomInit) { //�����ʼ��
            cell.randomCell();
            showCell();
            isRunning = false;
            thread = null;
        } else if (e.getSource() == clearCell) { //ϸ������
            cell.deleteAllCell();
            showCell();
            isRunning = false;
            thread = null;
        } else if (e.getSource() == start) { //��ʼ
            isRunning = true;
            thread = new Thread(new Runnable() {
                public void run() {
                    while (isRunning) {
                        makeNextGeneration();
                        try {
                            Thread.sleep(sleeptime);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        isDead = true;
                        for (int row = 1; row <= maxWidth; row++) {
                            for (int col = 1; col <= maxLength; col++) {
                                if (cell.getGrid()[row][col] != 0) {
                                    isDead = false;
                                    break;
                                }
                            }
                            if (!isDead) {
                                break;
                            }
                        }
                        if (isDead) {
                            JOptionPane.showMessageDialog(null, "����ϸ��������");
                            isRunning = false;
                            thread = null;
                        }
                    }
                }
            });
            thread.start();
        } else if (e.getSource() == nextGeneration) { //��һ��
            makeNextGeneration();
            isRunning = false;
            thread = null;
        } else if (e.getSource() == stop) { //��ͣ
            isRunning = false;
            thread = null;
        } else if (e.getSource() == exit) { //�˳�
            frame.dispose();
            System.exit(0);
        } else {
            int[][] grid = cell.getGrid();
            for (int i = 0; i < maxWidth; i++) {
                for (int j = 0; j < maxLength; j++) {
                    if (e.getSource() == nGrid[i][j]) {
                        isSelected[i][j] = !isSelected[i][j];
                        if (isSelected[i][j]) {
                            nGrid[i][j].setBackground(Color.BLACK);
                            grid[i + 1][j + 1] = 1;
                        } else {
                            nGrid[i][j].setBackground(Color.WHITE);
                            grid[i + 1][j + 1] = 0;
                        }
                        break;
                    }
                }
            }
            cell.setGrid(grid);
        }
    }

    /**��һ��.*/
    private void makeNextGeneration() {
        cell.update();
        showCell();
        jbNowGeneration.setText("" + cell.getNowGeneration()); //ˢ�µ�ǰ����
    }

    /**��ϸ�����ص�������.*/
    public void showCell() {
        int[][] grid = cell.getGrid();
        for (int i = 0; i < maxWidth; i++) {
            for (int j = 0; j < maxLength; j++) {
                if (grid[i + 1][j + 1] == 1) {
                    nGrid[i][j].setBackground(Color.BLACK); //����ʾ��ɫ
                } else {
                    nGrid[i][j].setBackground(Color.WHITE); //������ʾ��ɫ
                }
            }
        }
    }

}

