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

package bomberman.server.gui;

import bomberman.server.Database;
import bomberman.server.Game;
import bomberman.server.Player;
import bomberman.server.ServerThread;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.swing.DefaultListModel;
import javax.swing.JScrollBar;
import bomberman.server.gui.UserListTableModel;
import java.util.ArrayList;

/**
 * @author  Christian Lins
 * @author  Kai Ritterbusch
 */
public class ServerControlPanel extends javax.swing.JPanel 
{
  private ServerThread serverThread;
  
  private static ServerControlPanel instance = null;
  
  private String newUsername=null;
  private String newPW=null;
  
  public static ServerControlPanel getInstance()
  {
    return instance;
  }
  
  // New Log-Message
  public void addLogMessages(String txt)
  {
    // Creates actual Date
    Calendar cal = new GregorianCalendar( TimeZone.getTimeZone("ECT") );
    SimpleDateFormat formater = new SimpleDateFormat();
    String date = formater.format(cal.getTime());

    // Adds logsmessage to textarea
    txtLog.setText("("+ date + "): "+ txt +"\n" + txtLog.getText());    
    
    // Sets Scrollposition to 0
    JScrollBar vbar = scrPane.getVerticalScrollBar();
    vbar.setValue(vbar.getMinimum()); 
   // txtLog.updateUI();
  } 
  
  // Adds player to List
  public void addPlayer(Player pl)
  {
    //((DefaultListModel)liUser.getModel()).addElement(pl);    
  }
  
  /*
   * Adds game to List
   */ 
  public void addGame(Game game)
  {
    ((DefaultListModel)liGames.getModel()).addElement(game);    
  }
  
  /*
   * Removes Game from list
   */ 
  public void removeGame(Game game)
  {
    ((DefaultListModel)liGames.getModel()).removeElement(game);    
  }
  
  
  /** Creates new form ServerControlPanel */
  public ServerControlPanel(ServerThread thread) 
  {   
    initComponents();
    setThread(thread);
    instance = this;
    
    Database db = thread.getServer().getDatabase();
    for(String user : db.getUsers())
    {
      ArrayList<Object> data = new ArrayList<Object>();
      data.add(user);
      data.add("offline");
      ((UserListTableModel)getTblUserList().getModel()).addRow(data);
    }
  }
  
  private void setThread(ServerThread thread)
  {
    this.serverThread = thread;
    if(thread == null || !thread.isAlive())
    {
      btnStartServer.setEnabled(true);
      btnStopServer.setEnabled(false);
    }
    else
    {
      btnStartServer.setEnabled(false);
      btnStopServer.setEnabled(true);
    }
  }
  
  public void setUserNameandPw(String username, String password)
  {
    this.newUsername = username;
    this.newPW       = password;
  }
          
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    lblCaption = new javax.swing.JLabel();
    btnStartServer = new javax.swing.JButton();
    btnStopServer = new javax.swing.JButton();
    lblStartServer = new javax.swing.JLabel();
    lblStopServer = new javax.swing.JLabel();
    tabbedPane = new javax.swing.JTabbedPane();
    tabLog = new javax.swing.JPanel();
    scrPane = new javax.swing.JScrollPane();
    txtLog = new javax.swing.JTextArea();
    tabGames = new javax.swing.JPanel();
    jScrollPane2 = new javax.swing.JScrollPane();
    liGames = new javax.swing.JList();
    btnCloseGame = new javax.swing.JButton();
    tabUsers = new javax.swing.JPanel();
    btnKick = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    tblUserList = new javax.swing.JTable();
    btnCreateUser = new javax.swing.JButton();
    btnRemoveUser = new javax.swing.JButton();
    btnHighscoreExport = new javax.swing.JButton();
    lblExportHighscore = new javax.swing.JLabel();

    lblCaption.setText("Hier können Sie den KC Bomberman Server verwalten:");

    btnStartServer.setText("Server starten");
    btnStartServer.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnStartServerActionPerformed(evt);
      }
    });

    btnStopServer.setText("Server stoppen");
    btnStopServer.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnStopServerActionPerformed(evt);
      }
    });

    lblStartServer.setText("Der Server wird auf dem lokalen Host gestartet");

    lblStopServer.setText("Den laufenden Server und alle Spiele beenden");

    txtLog.setColumns(20);
    txtLog.setRows(5);
    scrPane.setViewportView(txtLog);

    org.jdesktop.layout.GroupLayout tabLogLayout = new org.jdesktop.layout.GroupLayout(tabLog);
    tabLog.setLayout(tabLogLayout);
    tabLogLayout.setHorizontalGroup(
      tabLogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(scrPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
    );
    tabLogLayout.setVerticalGroup(
      tabLogLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(scrPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
    );

    tabbedPane.addTab("Log", tabLog);

    liGames.setModel(new DefaultListModel());
    jScrollPane2.setViewportView(liGames);

    btnCloseGame.setText("Beenden");
    btnCloseGame.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCloseGameActionPerformed(evt);
      }
    });

    org.jdesktop.layout.GroupLayout tabGamesLayout = new org.jdesktop.layout.GroupLayout(tabGames);
    tabGames.setLayout(tabGamesLayout);
    tabGamesLayout.setHorizontalGroup(
      tabGamesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(org.jdesktop.layout.GroupLayout.TRAILING, tabGamesLayout.createSequentialGroup()
        .addContainerGap()
        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
        .add(18, 18, 18)
        .add(btnCloseGame)
        .addContainerGap())
    );
    tabGamesLayout.setVerticalGroup(
      tabGamesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(tabGamesLayout.createSequentialGroup()
        .addContainerGap()
        .add(tabGamesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
          .add(btnCloseGame))
        .addContainerGap())
    );

    tabbedPane.addTab("Spiele", tabGames);

    btnKick.setText("User kicken");
    btnKick.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnKickActionPerformed(evt);
      }
    });

    tblUserList.setModel(new UserListTableModel());
    jScrollPane1.setViewportView(tblUserList);

    btnCreateUser.setText("Neuer User");
    btnCreateUser.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCreateUserActionPerformed(evt);
      }
    });

    btnRemoveUser.setText("User entf.");
    btnRemoveUser.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnRemoveUserActionPerformed(evt);
      }
    });

    org.jdesktop.layout.GroupLayout tabUsersLayout = new org.jdesktop.layout.GroupLayout(tabUsers);
    tabUsers.setLayout(tabUsersLayout);
    tabUsersLayout.setHorizontalGroup(
      tabUsersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(org.jdesktop.layout.GroupLayout.TRAILING, tabUsersLayout.createSequentialGroup()
        .addContainerGap()
        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
        .add(tabUsersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
          .add(btnKick, 0, 0, Short.MAX_VALUE)
          .add(btnRemoveUser, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .add(btnCreateUser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );
    tabUsersLayout.setVerticalGroup(
      tabUsersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(tabUsersLayout.createSequentialGroup()
        .addContainerGap()
        .add(tabUsersLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
          .add(tabUsersLayout.createSequentialGroup()
            .add(btnCreateUser)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(btnRemoveUser)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(btnKick)))
        .addContainerGap())
    );

    tabbedPane.addTab("User", tabUsers);

    btnHighscoreExport.setText("Highs. Export");

    lblExportHighscore.setText("Aktuellen Highscore in Datei exportieren");

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(lblCaption)
          .add(layout.createSequentialGroup()
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
              .add(org.jdesktop.layout.GroupLayout.LEADING, btnHighscoreExport, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .add(org.jdesktop.layout.GroupLayout.LEADING, btnStartServer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .add(org.jdesktop.layout.GroupLayout.LEADING, btnStopServer, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
              .add(lblExportHighscore)
              .add(lblStopServer)
              .add(lblStartServer)))
          .add(org.jdesktop.layout.GroupLayout.TRAILING, tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(lblCaption)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(btnStartServer)
          .add(lblStartServer))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(btnStopServer)
          .add(lblStopServer))
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(btnHighscoreExport)
          .add(lblExportHighscore))
        .add(18, 18, 18)
        .add(tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
        .add(12, 12, 12))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void btnStartServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartServerActionPerformed
    if(this.getServerThread() != null)
    {
      this.getServerThread().stopThread();
    }
    
    try
    {
      this.serverThread = new ServerThread(true);
      this.getServerThread().start();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }

    setThread(this.getServerThread());
    ServerControlPanel.getInstance().addLogMessages("Bombermanserver bereit ...");
  }//GEN-LAST:event_btnStartServerActionPerformed

  private void btnStopServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopServerActionPerformed
    if(this.getServerThread() != null)
    {     
/*      for(int i = 0; i < ((DefaultListModel)liUser.getModel()).getSize(); i++)
      {
        try
        {
          serverThread.getServer().logout( ((DefaultListModel)liUser.getModel()).getElementAt(i).toString() );
          ((DefaultListModel)liUser.getModel()).removeElement(((DefaultListModel)liUser.getModel()).getElementAt(i));
        }
        catch(RemoteException e)
        {
          e.printStackTrace();
        }
      }
      this.serverThread.stopThread();
      setThread(null);
      ServerControlPanel.getInstance().addLogMessages("Bombermanserver gestoppt ...");
*/
    }
  }//GEN-LAST:event_btnStopServerActionPerformed

  private void btnKickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKickActionPerformed
    try
    {
      int i = tblUserList.getSelectedRow();
  
      String username = tblUserList.getModel().getValueAt(i, 0).toString();
      serverThread.getServer().logout(username);       
      ((UserListTableModel)tblUserList.getModel()).setValueAt("offline", i, 1);
      addLogMessages(username + " wurde gekickt");
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }//GEN-LAST:event_btnKickActionPerformed

  private void btnCloseGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseGameActionPerformed
   try
   {
    if(liGames.getSelectedValue() == null)
      return;
      getServerThread().getServer().stopGame(liGames.getSelectedValue().toString());
    ((DefaultListModel)liGames.getModel()).removeElement(liGames.getSelectedValue());    
   }
   catch(RemoteException e)
   {
    e.printStackTrace(); 
   }
    
  }//GEN-LAST:event_btnCloseGameActionPerformed

  private void btnCreateUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateUserActionPerformed
    UserFrame uf = new UserFrame(this);
    uf.setVisible(true);    
}//GEN-LAST:event_btnCreateUserActionPerformed

  private void btnRemoveUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveUserActionPerformed
    serverThread.getServer().getDatabase().removeUser(((UserListTableModel)tblUserList.getModel()).getValueAt(tblUserList.getSelectedRow(), 0).toString());
    ((UserListTableModel)tblUserList.getModel()).deleteRow(tblUserList.getSelectedRow());
  }//GEN-LAST:event_btnRemoveUserActionPerformed
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnCloseGame;
  private javax.swing.JButton btnCreateUser;
  private javax.swing.JButton btnHighscoreExport;
  private javax.swing.JButton btnKick;
  private javax.swing.JButton btnRemoveUser;
  private javax.swing.JButton btnStartServer;
  private javax.swing.JButton btnStopServer;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JLabel lblCaption;
  private javax.swing.JLabel lblExportHighscore;
  private javax.swing.JLabel lblStartServer;
  private javax.swing.JLabel lblStopServer;
  private javax.swing.JList liGames;
  private javax.swing.JScrollPane scrPane;
  private javax.swing.JPanel tabGames;
  private javax.swing.JPanel tabLog;
  private javax.swing.JPanel tabUsers;
  private javax.swing.JTabbedPane tabbedPane;
  private javax.swing.JTable tblUserList;
  private javax.swing.JTextArea txtLog;
  // End of variables declaration//GEN-END:variables

  public javax.swing.JTable getTblUserList() 
  {
    return tblUserList;
  }

  public ServerThread getServerThread() 
  {
    return serverThread;
  }
  
}
