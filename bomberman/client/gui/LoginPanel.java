/*
 *  KC Bomberman
 *  Copyright (C) 2008,2009 Christian Lins <cli@openoffice.org>
 *  Copyright (C) 2008 Kai Ritterbusch <kai.ritterbusch@googlemail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package bomberman.client.gui;

import bomberman.client.ClientThread;
import bomberman.net.Event;
import bomberman.util.CHAP;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

/**
 * Shows the loginPanel when game has started
 * @author Kai Ritterbusch
 * @author Christian Lins
 */
public class LoginPanel extends javax.swing.JPanel
{
  
  private static volatile LoginPanel instance = null;
  
  public static LoginPanel getInstance()
  {
    return instance;
  }
  
  public LoginPanel() 
  {
    instance = this;
    initComponents();
    
    txtNickname.selectAll();
    
    txtPassword.addKeyListener(new KeyAdapter()     
    {
      @Override
      public void keyPressed(KeyEvent ke)
      {
        if(ke.getKeyCode() == KeyEvent.VK_ENTER)
          btnLoginActionPerformed(null);
      }
    });
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    lblNickname = new javax.swing.JLabel();
    txtNickname = new javax.swing.JTextField();
    btnLogin = new javax.swing.JButton();
    btnClear = new javax.swing.JButton();
    txtPassword = new javax.swing.JPasswordField();
    lblPassword = new javax.swing.JLabel();

    setNextFocusableComponent(txtNickname);

    lblNickname.setForeground(java.awt.Color.white);
    lblNickname.setText("Spielername:");

    txtNickname.setText("gast");

    btnLogin.setText("Login");
    btnLogin.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnLoginActionPerformed(evt);
      }
    });

    btnClear.setText("Clear");
    btnClear.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnClearActionPerformed(evt);
      }
    });

    lblPassword.setForeground(java.awt.Color.white);
    lblPassword.setText("Passwort:");

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(lblNickname)
          .add(lblPassword))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
            .add(btnClear)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(btnLogin))
          .add(txtNickname, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
          .add(txtPassword, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(lblNickname)
          .add(txtNickname, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(txtPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(lblPassword))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(btnLogin)
          .add(btnClear))
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

  private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
    this.txtNickname.setText("");
    this.txtPassword.setText("");
  }//GEN-LAST:event_btnClearActionPerformed

  private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
    String nickname = this.txtNickname.getText();
    try
    {
      ClientThread.Server.login1(new Event(new Object[]{nickname}));
    }
    catch(Exception ex)
    {
      JOptionPane.showMessageDialog( this, "Login fehlgeschlagen (Server laeuft nicht?)", "Fehler" ,JOptionPane.ERROR_MESSAGE );
      ex.printStackTrace();
    }
  }//GEN-LAST:event_btnLoginActionPerformed
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnClear;
  private javax.swing.JButton btnLogin;
  private javax.swing.JLabel lblNickname;
  private javax.swing.JLabel lblPassword;
  private javax.swing.JTextField txtNickname;
  private javax.swing.JPasswordField txtPassword;
  // End of variables declaration//GEN-END:variables
  
  /**
   * Paints the background
   * @param g
   */
  @Override
  public void paintComponent(Graphics g)
  {
    g.setColor(new Color(0, 0, 0, 150));
    g.fillRect(0, 0, getWidth(), getHeight());
  }
  
  public void continueLogin(long challenge)
  {
    String nickname = this.txtNickname.getText();
    String password = this.txtPassword.getText();
    long   hash     = CHAP.createChecksum(challenge, password);
      
    // The Client request a login
    ClientThread.Server.login2(new Event(new Object[]{nickname, hash}));
  }
}
