/*
 * Created by JFormDesigner on Tue Feb 19 11:51:25 CST 2019
 */

package com.keer.mydb.GUI;


import java.awt.event.*;
import javax.swing.event.*;
import com.bigchaindb.model.Assets;
import com.keer.mydb.BDQLParser.BDQLUtil;
import com.keer.mydb.bigchaindb.BigchainDBRunner;
import com.keer.mydb.bigchaindb.BigchainDBUtil;
import com.keer.mydb.bigchaindb.KeyPairHolder;
import com.keer.mydb.config.PropertyUtil;
import com.keer.mydb.domain.MetaData;
import com.keer.mydb.domain.ParserResult;
import com.keer.mydb.domain.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author keer
 */
public class bigchaindb extends JFrame {
    private static Logger logger = LoggerFactory.getLogger(bigchaindb.class);
    private static String keyPath = PropertyUtil.getProperties("keyPath");

    public bigchaindb() {
        initComponents();
    }


    /**
     * 获取秘钥按钮事件
     *
     * @param e
     */
    private void btKeyActionPerformed(ActionEvent e) {
        File file = new File(keyPath);
        if (file.exists()) {
            logger.info("密钥文件存在，开始获取秘钥");
            String key = null;
            try {
                FileInputStream in = new FileInputStream(file);
                byte[] buffer = new byte[in.available()];
                in.read(buffer);
                key = new String(buffer);
            } catch (Exception e1) {
                logger.error("密钥文件损坏");
            }
            textKey.setText(key);
        } else {
            logger.warn("秘钥文件不存在！！！！");
            CreateKeyDialog myDialog = new CreateKeyDialog(this);
            myDialog.setVisible(true);


        }
    }

    /**
     * 连接按钮事件
     *
     * @param e
     */
    private void btConnActionPerformed(ActionEvent e) {
        // TODO add your code here
        String url = textConn.getText();
        if (BigchainDBRunner.StartConn(url)) {
            logger.info("连接成功");
            label4.setOpaque(true);
            label4.setBackground(Color.green);

        } else {
            logger.info("连接失败");
            label4.setOpaque(true);
            label4.setBackground(Color.green);
        }
        addTree();


    }

    /**
     * 初始化tree
     *
     * @return
     */
    private DefaultMutableTreeNode initTree() {
        DefaultMutableTreeNode bigchaindb = new DefaultMutableTreeNode("bigchaingDB");
        return bigchaindb;
    }

    /**
     * 运行按钮的监听事件
     *
     * @param e
     */
    private void btRUNActionPerformed(ActionEvent e)  {
        String BDQL = tpBDQL.getText();
        if (BDQL != null) {
            ParserResult resultre = BDQLUtil.work(BDQL);
            if (resultre.getMessage().equals("select")) {
                Table table= (Table) resultre.getData();
                this.table.setModel(buildTableData(table));
                panel4.repaint();
                this.repaint();
            } else {
                tpBDQL.setText(resultre.getData().toString());

                addTree();
                this.repaint();
            }
        }


    }

    /**
     * 获得全部的数据表名
     */
    private void addTree() {
        logger.info(">>>>>>>>>>");
        DefaultMutableTreeNode assertTable = new DefaultMutableTreeNode("asset");
        DefaultMutableTreeNode metadataTable = new DefaultMutableTreeNode("metadata");
        DefaultMutableTreeNode bigchaindb = new DefaultMutableTreeNode("bigchaingDB");

        bigchaindb.add(assertTable);
        bigchaindb.add(metadataTable);

        Map<String, Table> result = null;

        try {
            Thread.sleep(2000);
            result = BDQLUtil.getAlltablesByPubKey(KeyPairHolder.pubKeyToString(KeyPairHolder.getPublic()));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        for (String key : result.keySet()) {
            if (result.get(key).getType().equals("CREATE")) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(key);
                assertTable.add(node);
            } else {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(key);
                metadataTable.add(node);
            }
        }

        DefaultTreeModel model = new DefaultTreeModel(bigchaindb);
        tree1.setModel(model);
        panel3.repaint();
        this.repaint();

        logger.info("finish");

    }

    /**
     * 叶子节点触发事件
     *
     * @param e
     */

    private void tree1ValueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode note = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent(); //返回最后选中的结点
        if(note.isLeaf()) {
            String tablename = note.toString();//获得这个结点的名称
            TreeNode[] nodes = note.getPath();
            String tableType = nodes[nodes.length - 2].toString();

            Table tabledata = new Table();
            tabledata.setTableName(tablename);
            if (tableType.equals("asset")) {
                tabledata.setType("CREATE");
                Assets assets = BigchainDBUtil.getAssetByKey(tablename);
                tabledata.setTableData(assets);
            } else {
                tabledata.setType("TRANSFER");
                List<MetaData> metaDatas = BigchainDBUtil.getMetaDatasByKey(tablename);
                tabledata.setTableData(metaDatas);
            }


            table.setModel(buildTableData(tabledata));
            panel4.repaint();
        }
        this.repaint();
    }

    /**
     * 构建table数据模型
     * @param table
     * @return
     */
    private DefaultTableModel buildTableData(Table table){
        DefaultTableModel model = new DefaultTableModel();
        Object[] columnNames = table.getColumnName().toArray();
        List<Map> data = table.getData();
        List<Object[]> objects = new ArrayList<Object[]>();
        for (Map map : data) {
            Object[] a = new Object[columnNames.length];
            for (int i = 0; i < columnNames.length; i++) {
                a[i] = map.get(columnNames[i]);
            }
            objects.add(a);
        }

        Object[][] b = (Object[][]) objects.toArray(new Object[data.size()][columnNames.length]);
        model.setDataVector(b, columnNames);
        return model;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        btConn = new JButton();
        btKey = new JButton();
        textKey = new JTextField();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        textConn = new JTextField();
        label4 = new JLabel();
        panel3 = new JPanel();
        scrollPane1 = new JScrollPane();
        tree1 = new JTree();
        panel5 = new JPanel();
        btRUN = new JButton();
        scrollPane4 = new JScrollPane();
        tpBDQL = new JTextPane();
        panel4 = new JPanel();
        scrollPane2 = new JScrollPane();
        table = new JTable();

        //======== this ========
        setTitle("bigchaindb");
        setForeground(Color.white);
        setBackground(Color.lightGray);
        setVisible(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel1 ========
        {
            panel1.setLayout(null);

            //---- btConn ----
            btConn.setText("\u8fde\u63a5");
            btConn.setBackground(UIManager.getColor("ArrowButton.background"));
            btConn.setForeground(Color.black);
            btConn.setAutoscrolls(true);
            btConn.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 14));
            btConn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btConnActionPerformed(e);
                }
            });
            panel1.add(btConn);
            btConn.setBounds(400, 70, 95, 40);

            //---- btKey ----
            btKey.setText("\u83b7\u53d6\u5bc6\u94a5");
            btKey.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 14));
            btKey.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btKeyActionPerformed(e);
                }
            });
            panel1.add(btKey);
            btKey.setBounds(395, 10, 100, 40);

            //---- textKey ----
            textKey.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            panel1.add(textKey);
            textKey.setBounds(155, 10, 165, 40);

            //---- label1 ----
            label1.setText("\u8f93\u5165\u79d8\u94a5");
            label1.setFont(new Font("\u5e7c\u5706", Font.BOLD, 21));
            label1.setHorizontalAlignment(SwingConstants.CENTER);
            panel1.add(label1);
            label1.setBounds(35, 15, 100, 35);

            //---- label2 ----
            label2.setText("\u8f93\u5165\u79d8\u94a5");
            panel1.add(label2);
            label2.setBounds(0, 110, 65, 35);

            //---- label3 ----
            label3.setText("\u8282\u70b9HOST");
            label3.setFont(new Font("\u5e7c\u5706", Font.BOLD, 18));
            label3.setHorizontalAlignment(SwingConstants.CENTER);
            panel1.add(label3);
            label3.setBounds(40, 70, 95, 30);

            //---- textConn ----
            textConn.setText("http://127.0.0.1:9984");
            textConn.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            panel1.add(textConn);
            textConn.setBounds(155, 70, 170, 35);

            //---- label4 ----
            label4.setBackground(Color.red);
            label4.setForeground(Color.red);
            label4.setText(" ");
            panel1.add(label4);
            label4.setBounds(350, 75, 20, 20);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel1.getComponentCount(); i++) {
                    Rectangle bounds = panel1.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel1.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel1.setMinimumSize(preferredSize);
                panel1.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel1);
        panel1.setBounds(5, 10, 745, 110);

        //======== panel3 ========
        {
            panel3.setLayout(null);

            //======== scrollPane1 ========
            {

                //---- tree1 ----
                tree1.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 12));
                tree1.addTreeSelectionListener(new TreeSelectionListener() {
                    public void valueChanged(TreeSelectionEvent e) {
                        tree1ValueChanged(e);
                    }
                });
                scrollPane1.setViewportView(tree1);
            }
            panel3.add(scrollPane1);
            scrollPane1.setBounds(10, 0, 185, 510);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel3.getComponentCount(); i++) {
                    Rectangle bounds = panel3.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel3.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel3.setMinimumSize(preferredSize);
                panel3.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel3);
        panel3.setBounds(-5, 125, 190, 510);

        //======== panel5 ========
        {
            panel5.setLayout(null);

            //---- btRUN ----
            btRUN.setText("\u8fd0\u884c");
            btRUN.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 12));
            btRUN.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btRUNActionPerformed(e);
                }
            });
            panel5.add(btRUN);
            btRUN.setBounds(445, 150, 68, 30);

            //======== scrollPane4 ========
            {
                scrollPane4.setViewportView(tpBDQL);
            }
            panel5.add(scrollPane4);
            scrollPane4.setBounds(0, 0, 550, 150);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel5.getComponentCount(); i++) {
                    Rectangle bounds = panel5.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel5.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel5.setMinimumSize(preferredSize);
                panel5.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel5);
        panel5.setBounds(190, 445, 550, 185);

        //======== panel4 ========
        {
            panel4.setLayout(null);

            //======== scrollPane2 ========
            {

                //---- table ----
                table.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.BOLD, 12));
                scrollPane2.setViewportView(table);
            }
            panel4.add(scrollPane2);
            scrollPane2.setBounds(5, 5, 540, 310);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel4.getComponentCount(); i++) {
                    Rectangle bounds = panel4.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel4.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel4.setMinimumSize(preferredSize);
                panel4.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel4);
        panel4.setBounds(190, 125, 545, 320);

        { // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            ((JComponent)contentPane).setMinimumSize(preferredSize);
            ((JComponent)contentPane).setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JButton btConn;
    private JButton btKey;
    private JTextField textKey;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JTextField textConn;
    private JLabel label4;
    private JPanel panel3;
    private JScrollPane scrollPane1;
    private JTree tree1;
    private JPanel panel5;
    private JButton btRUN;
    private JScrollPane scrollPane4;
    private JTextPane tpBDQL;
    private JPanel panel4;
    private JScrollPane scrollPane2;
    private JTable table;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
