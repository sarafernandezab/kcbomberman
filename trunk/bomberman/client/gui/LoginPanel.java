/*
 *  KC Bomberman
 *  Copyright 2008 Christian Lins <christian.lins@web.de>
 *  Copyright 2008 Kai Ritterbusch <kai.ritterbusch@googlemail.com>
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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 *
 * @author Christian Lins (christian.lins@web.de)
 */
public class LoginPanel extends javax.swing.JPanel
{
  
  /** Creates new form LoginPanel */
  public LoginPanel() 
  {
    initComponents();
    
    txtNickname.selectAll();
    
    txtNickname.addKeyListener(new KeyAdapter()     
    {
      @Override
      public void keyPressed(KeyEvent ke)
      {
        if(ke.getKeyCode() == KeyEvent.VK_ENTER)
          btnLoginActionPerformed(null);
      }
    });
  }
  
  private String getOwnIP() throws SocketException
  {
    Enumeration ifaces = NetworkInterface.getNetworkInterfaces();
  
    while (ifaces.hasMoreElements()) 
    {
      NetworkInterface ni = (NetworkInterface)ifaces.nextElement();
      if(ni.isLoopback())
        break;     

      Enumeration addrs = ni.getInetAddresses();
      while (addrs.hasMoreElements()) 
      {
        InetAddress ia = (InetAddress)addrs.nextElement();
        if(!ia.isLoopbackAddress())
        {
          System.out.println(ia.getHostAddress());
          return ia.getHostAddress();          
        }
      }
    }
    return "";
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

    txtNickname.setText("<bitte wählen>");

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

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(lblNickname)
          .addComponent(lblPassword))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addComponent(btnClear)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(btnLogin))
          .addComponent(txtNickname, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
          .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(lblNickname)
          .addComponent(txtNickname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lblPassword))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnLogin)
          .addComponent(btnClear))
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

  private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
    this.txtNickname.setText("");
    this.txtPassword.setText("");
  }//GEN-LAST:event_btnClearActionPerformed

  private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
    String nickname = this.txtNickname.getText();

    //InetAddress inet = InetAddress. getByName(InetAddress.getLocalHost());

    String password = this.txtPassword.getText();
    String ip = null;
    try
    { 
      
      // The Client request a login
      ClientThread.Server.login(nickname, password, ClientThread.ServerListener,  getOwnIP());

    }
    catch(Exception ex)
    {
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
  
  @Override
  public void paintComponent(Graphics g)
  {
    g.setColor(new Color(0, 0, 0, 150));
    g.fillRect(0, 0, getWidth(), getHeight());
  }
}
