/*
 * Created by JFormDesigner on Sat Feb 23 10:40:19 CST 2019
 */

package com.keer.mydb.GUI;


import com.keer.mydb.bigchaindb.KeyPairHolder;
import com.keer.mydb.config.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author keer
 */
public class CreateKeyDialog extends JDialog {
    private static Logger logger= LoggerFactory.getLogger(CreateKeyDialog.class);
    private static String keyPath= PropertyUtil.getProperties("keyPath");
    public CreateKeyDialog(Frame owner) {
        super(owner);
        initComponents();
    }

    public CreateKeyDialog(Dialog owner) {
        super(owner);
        initComponents();
    }

    private void button1ActionPerformed(ActionEvent e) {
        logger.info("开始创建密钥！！！！！");
        KeyPairHolder.SaveKeyPairToTXT(KeyPairHolder.getKeyPair());
        File file=new File(keyPath);
        if(file.exists()){
            logger.info("创建成功！！！");
            this.dispose();
        }else{
            logger.info("创建失败！！！");
            this.dispose();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        button1 = new JButton();
        button2 = new JButton();

        //======== this ========
        setType(Type.POPUP);
        setVisible(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- label1 ----
        label1.setText("\u662f\u5426\u521b\u5efa\u5bc6\u94a5\uff1f");
        label1.setFont(new Font("\u5b8b\u4f53", Font.BOLD, 16));
        contentPane.add(label1);
        label1.setBounds(130, 25, 120, 48);

        //---- button1 ----
        button1.setText("\u662f");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button1ActionPerformed(e);
            }
        });
        contentPane.add(button1);
        button1.setBounds(100, 85, 55, 40);

        //---- button2 ----
        button2.setText("\u5426");
        contentPane.add(button2);
        button2.setBounds(240, 85, 55, 40);

        contentPane.setPreferredSize(new Dimension(375, 185));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JButton button1;
    private JButton button2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
