/*
 *  KC Bomberman
 *  Copyright (C) 2008,2009 Christian Lins <christian.lins@web.de>
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

package bomberman.server.gui;

import bomberman.server.Server;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Frame that allows the creation of a new user.
 * @author  Kai Ritterbusch
 */
public class UserFrame extends javax.swing.JFrame 
{
  private ServerControlPanel parent;
  
  /** Creates new form UserFrame */
  public UserFrame() 
  {
    initComponents();
  }
  
  /** Creates new form UserFrame */
  public UserFrame(ServerControlPanel parent) 
  {
    initComponents();
    this.parent = parent;
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    btnOk = new javax.swing.JButton();
    btnCancel = new javax.swing.JButton();
    txtUsername = new javax.swing.JTextField();
    txtPw1 = new javax.swing.JPasswordField();
    txtPw2 = new javax.swing.JPasswordField();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();

    setTitle("Neuen User erstellen");

    btnOk.setText("Erstellen");
    btnOk.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnOkActionPerformed(evt);
      }
    });

    btnCancel.setText("Abbrechen");
    btnCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCancelActionPerformed(evt);
      }
    });

    txtPw2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtPw2ActionPerformed(evt);
      }
    });

    jLabel1.setText("Username:");

    jLabel2.setText("PW:");

    jLabel3.setText("PW (best.)");

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(layout.createSequentialGroup()
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
              .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
              .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
              .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
              .add(txtPw1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
              .add(txtPw2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
              .add(org.jdesktop.layout.GroupLayout.TRAILING, txtUsername, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)))
          .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
            .add(btnOk)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(btnCancel)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .add(30, 30, 30)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(jLabel1)
          .add(txtUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(txtPw1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(jLabel2))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(txtPw2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(jLabel3))
        .add(18, 18, 18)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(btnCancel)
          .add(btnOk))
        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  /**
   * Called on hit on the cancel button.
   * @param evt
   */
  private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
    this.setVisible(false);
}//GEN-LAST:event_btnCancelActionPerformed

  /**
   * Called on hit on the create button.
   * @param evt
   */
  private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
    if(txtPw1.getText().equals(txtPw2.getText()))
    {
      ArrayList<Object> user = new ArrayList<Object>();
      user.add(txtUsername.getText());
      user.add("offline");
      Server.getInstance().getDatabase().addUser(txtUsername.getText(), txtPw1.getText());
      ((UserListTableModel)this.parent.getTblUserList().getModel()).addRow(user);
      
      this.setVisible(false);
    }
    else
      JOptionPane.showMessageDialog( this, "Das Passwort und die Bestaetigung stimmen nicht ueberein!!" );
  }//GEN-LAST:event_btnOkActionPerformed

  private void txtPw2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPw2ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_txtPw2ActionPerformed
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnCancel;
  private javax.swing.JButton btnOk;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JPasswordField txtPw1;
  private javax.swing.JPasswordField txtPw2;
  private javax.swing.JTextField txtUsername;
  // End of variables declaration//GEN-END:variables

}
