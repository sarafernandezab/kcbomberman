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

/**
 * @author  Christian Lins
 * @author  Kai Ritterbusch
 */
public class ServerControlPanel extends javax.swing.JPanel 
{
  private ServerThread serverThread;
  
  private static ServerControlPanel instance = null;
  
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
    txtLog.updateUI();
  } 
  
  // Adds player to List
  public void addPlayer(Player pl)
  {
    ((DefaultListModel)liUser.getModel()).addElement(pl);    
  }
  
  // Adds game to List
  public void addGame(Game game)
  {
    ((DefaultListModel)liGames.getModel()).addElement(game);    
  }
  
  /** Creates new form ServerControlPanel */
  public ServerControlPanel(ServerThread thread) 
  {   
    initComponents();
    setThread(thread);
    instance = this;
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
    jScrollPane3 = new javax.swing.JScrollPane();
    liUser = new javax.swing.JList();
    btnKick = new javax.swing.JButton();

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

    javax.swing.GroupLayout tabLogLayout = new javax.swing.GroupLayout(tabLog);
    tabLog.setLayout(tabLogLayout);
    tabLogLayout.setHorizontalGroup(
      tabLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(scrPane, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
    );
    tabLogLayout.setVerticalGroup(
      tabLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(scrPane, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
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

    javax.swing.GroupLayout tabGamesLayout = new javax.swing.GroupLayout(tabGames);
    tabGames.setLayout(tabGamesLayout);
    tabGamesLayout.setHorizontalGroup(
      tabGamesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabGamesLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
        .addGap(18, 18, 18)
        .addComponent(btnCloseGame)
        .addContainerGap())
    );
    tabGamesLayout.setVerticalGroup(
      tabGamesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabGamesLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(tabGamesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
          .addComponent(btnCloseGame))
        .addContainerGap())
    );

    tabbedPane.addTab("Spiele", tabGames);

    liUser.setModel(new DefaultListModel());
    jScrollPane3.setViewportView(liUser);

    btnKick.setText("Kicken");
    btnKick.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnKickActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout tabUsersLayout = new javax.swing.GroupLayout(tabUsers);
    tabUsers.setLayout(tabUsersLayout);
    tabUsersLayout.setHorizontalGroup(
      tabUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabUsersLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        .addGap(18, 18, 18)
        .addComponent(btnKick)
        .addContainerGap())
    );
    tabUsersLayout.setVerticalGroup(
      tabUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(tabUsersLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(tabUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
          .addComponent(btnKick))
        .addContainerGap())
    );

    tabbedPane.addTab("User", tabUsers);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
          .addComponent(lblCaption)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(btnStartServer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(btnStopServer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(lblStopServer)
              .addComponent(lblStartServer))))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(lblCaption)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnStartServer)
          .addComponent(lblStartServer))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnStopServer)
          .addComponent(lblStopServer))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

  private void btnStartServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartServerActionPerformed
    if(this.serverThread != null)
    {
      this.serverThread.stopThread();
    }
    
    this.serverThread = new ServerThread(true);
    this.serverThread.start();
    setThread(this.serverThread);
    ServerControlPanel.getInstance().addLogMessages("Bombermanserver bereit ...");
  }//GEN-LAST:event_btnStartServerActionPerformed

  private void btnStopServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopServerActionPerformed
    if(this.serverThread != null)
    {     
      for(int i = 0; i < ((DefaultListModel)liUser.getModel()).getSize(); i++)
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

    }
  }//GEN-LAST:event_btnStopServerActionPerformed

  private void btnKickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKickActionPerformed
    try
    {
      Player pl = (Player)liUser.getSelectedValue();
      serverThread.getServer().logout(pl.toString());         
      ((DefaultListModel)liUser.getModel()).removeElement(pl);
      addLogMessages(pl.getNickname() + " wurde gekickt");
    }
    catch(RemoteException e)
    {
      e.printStackTrace();
    }
  }//GEN-LAST:event_btnKickActionPerformed

  private void btnCloseGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseGameActionPerformed
   try
   {
    if(liGames.getSelectedValue() == null)
      return;
    serverThread.getServer().closeGame(liGames.getSelectedValue().toString());
    ((DefaultListModel)liGames.getModel()).removeElement(liGames.getSelectedValue());
   }
   catch(RemoteException e)
   {
    e.printStackTrace(); 
   }
    
  }//GEN-LAST:event_btnCloseGameActionPerformed
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnCloseGame;
  private javax.swing.JButton btnKick;
  private javax.swing.JButton btnStartServer;
  private javax.swing.JButton btnStopServer;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JLabel lblCaption;
  private javax.swing.JLabel lblStartServer;
  private javax.swing.JLabel lblStopServer;
  private javax.swing.JList liGames;
  private javax.swing.JList liUser;
  private javax.swing.JScrollPane scrPane;
  private javax.swing.JPanel tabGames;
  private javax.swing.JPanel tabLog;
  private javax.swing.JPanel tabUsers;
  private javax.swing.JTabbedPane tabbedPane;
  private javax.swing.JTextArea txtLog;
  // End of variables declaration//GEN-END:variables
  
}
