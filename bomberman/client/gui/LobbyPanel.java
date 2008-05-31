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

import bomberman.server.Session;
import bomberman.server.Game;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;

/**
 *
 * @author Christian Lins
 */
public class LobbyPanel extends javax.swing.JPanel 
{
  private int        activeCol = 0;
          
  /** Creates new form LobbyPanel */
  public LobbyPanel() 
  {
    initComponents();
    
    txtChatInput.addKeyListener(new KeyAdapter()     {
           public void keyPressed(KeyEvent ke)
           {
             if(ke.getKeyCode() == KeyEvent.VK_ENTER)
                btnChatActionPerformed(null);
           }
      });
    
    tblGamelist.getTableHeader().addMouseListener(new MouseAdapter() 
    {
      public void mousePressed(MouseEvent evt) 
      {
         activeCol =  tblGamelist.columnAtPoint(evt.getPoint());
         ((GameListTableModel)tblGamelist.getModel()).sortByColumn(tblGamelist.columnAtPoint(evt.getPoint()));    

         ((GameListTableModel)tblGamelist.getModel()).fireTableDataChanged();
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

    lblLobbyTitle = new javax.swing.JLabel();
    lblLobbyInfo = new javax.swing.JLabel();
    lblGamelist = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    tblGamelist = new javax.swing.JTable();
    lblChatWindow = new javax.swing.JLabel();
    jScrollPane2 = new javax.swing.JScrollPane();
    txtChat = new javax.swing.JTextPane();
    txtChatInput = new javax.swing.JTextField();
    btnChat = new javax.swing.JButton();
    jScrollPane3 = new javax.swing.JScrollPane();
    lstUser = new javax.swing.JList();
    lblUserlist = new javax.swing.JLabel();
    btnCreateGame = new javax.swing.JButton();
    btnJoinGame = new javax.swing.JButton();

    lblLobbyTitle.setFont(new java.awt.Font("DejaVu Sans", 0, 24));
    lblLobbyTitle.setText("Lobby");

    lblLobbyInfo.setText("Hier kannst Du mit anderen Spielern chatten, ein Spiel erstellen oder einem Spiel beitreten");

    lblGamelist.setText("Spielliste");

    tblGamelist.setModel(new GameListTableModel());
    jScrollPane1.setViewportView(tblGamelist);

    lblChatWindow.setText("Chatfenster");

    txtChat.setEditable(false);
    jScrollPane2.setViewportView(txtChat);

    btnChat.setText("Chat");
    btnChat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnChatActionPerformed(evt);
      }
    });

    jScrollPane3.setViewportView(lstUser);

    lblUserlist.setText("Userliste");

    btnCreateGame.setText("Neues Spiel");
    btnCreateGame.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCreateGameActionPerformed(evt);
      }
    });

    btnJoinGame.setText("Beitreten");
    btnJoinGame.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnJoinGameActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
            .addComponent(lblLobbyTitle, javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblLobbyInfo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblGamelist, javax.swing.GroupLayout.Alignment.LEADING))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(txtChatInput, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnChat))
              .addComponent(lblChatWindow)
              .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(lblUserlist)
              .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(btnJoinGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(btnCreateGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(lblLobbyTitle)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(lblLobbyInfo)
        .addGap(18, 18, 18)
        .addComponent(lblGamelist)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(lblChatWindow)
              .addComponent(lblUserlist)))
          .addGroup(layout.createSequentialGroup()
            .addComponent(btnCreateGame)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnJoinGame)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(btnChat)
              .addComponent(txtChatInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

  private void btnChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChatActionPerformed
    String text = this.txtChatInput.getText();
    try
    {
      bomberman.client.Main.Server.sendChatMessage(bomberman.client.Main.Session, text);
      this.txtChatInput.setText("");
    }
    catch(RemoteException ex)
    {
      ex.printStackTrace();
    }
  }//GEN-LAST:event_btnChatActionPerformed

  private void btnCreateGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateGameActionPerformed
    try
    {
      String[] msg = {"Bitte geben Sie einen Namen f\u00FCr das Spiel an!"};
      String gameName = JOptionPane.showInputDialog(msg);
      Session session = bomberman.client.Main.Session;
      bomberman.client.Main.Server.createGame(session, gameName);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }//GEN-LAST:event_btnCreateGameActionPerformed

  private void btnJoinGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinGameActionPerformed
    //MainFrame.getInstance().setContentPane(new PlaygroundPanel(14, 16));
    try
    {
      int row = tblGamelist.getSelectedRow();
      String name = (String)((GameListTableModel)tblGamelist.getModel()).getValueAt(row, 0);
      bomberman.client.Main.Server.joinGame(bomberman.client.Main.Session, name);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }//GEN-LAST:event_btnJoinGameActionPerformed
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnChat;
  private javax.swing.JButton btnCreateGame;
  private javax.swing.JButton btnJoinGame;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JLabel lblChatWindow;
  private javax.swing.JLabel lblGamelist;
  private javax.swing.JLabel lblLobbyInfo;
  private javax.swing.JLabel lblLobbyTitle;
  private javax.swing.JLabel lblUserlist;
  private javax.swing.JList lstUser;
  private javax.swing.JTable tblGamelist;
  private javax.swing.JTextPane txtChat;
  private javax.swing.JTextField txtChatInput;
  // End of variables declaration//GEN-END:variables
  
  public void addChatMessage(String line)
  {
    try
    {
      Document doc = txtChat.getDocument();
      
      // Insert the line and a newline into the document
      doc.insertString(doc.getLength(), line + "\n", null);
      
      // Scroll to the end of the textpane
      txtChat.scrollRectToVisible(new Rectangle(0, txtChat.getHeight()));
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public void setUserList(ArrayList<String> users)
  {
    lstUser.setListData(new Vector<Object>(users));
  }
  
  public void addGameInfo(ArrayList<ArrayList<Object>> data)
  {
    ((GameListTableModel)tblGamelist.getModel()).setData(data);
  }
}
