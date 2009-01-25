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
import bomberman.client.io.Resource;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import javax.swing.plaf.basic.BasicTextAreaUI;

/**
 * When a user has joined a game this panel is shown.
 * This panel shows information about the other players and the
 * game create has a button to start the game before four players
 * have joined.
 * @author Kai Ritterbusch (kai.ritterbusch@fh-osnabrueck.de)
 * @author Christian Lins (cli@openoffice.org)
 */
public class WaitingPanel extends javax.swing.JPanel 
{
  private Image background = null;
  private String gameName;
  
  /** Creates new form WaitingPanel */
  public WaitingPanel(String gameName) 
  {
    this.gameName = gameName;
    this.background = Resource.getImage("resource/gfx/waitscreen.jpg").getImage();
    initComponents();
    
    this.txtInfo.setUI(new BasicTextAreaUI()
    {
      @Override
      public void paintBackground(Graphics g)
      {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, txtInfo.getWidth(), txtInfo.getHeight());
      }
      
      @Override
      public void paintSafely(Graphics g)
      {
        paintBackground(g);
        super.paintSafely(g);
      }
    });
    
    btnStartGame.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnStartGameActionPerformed(evt);
      }
    });
  }
   
  /**
   * Shows if a user joins Game
   * @param text
   */
  public void addInfoText(String text)
  {
    this.txtInfo.setText(txtInfo.getText() + "\n" + text);
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    btnStartGame = new javax.swing.JButton();
    txtInfo = new javax.swing.JTextArea();

    btnStartGame.setText("Spiel starten");

    txtInfo.setColumns(20);
    txtInfo.setEditable(false);
    txtInfo.setForeground(new java.awt.Color(254, 254, 254));
    txtInfo.setRows(5);
    txtInfo.setAutoscrolls(false);
    txtInfo.setBorder(null);
    txtInfo.setDoubleBuffered(true);
    txtInfo.setOpaque(false);

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(layout.createSequentialGroup()
            .add(txtInfo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
            .addContainerGap())
          .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
            .add(btnStartGame)
            .add(127, 127, 127))))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
        .addContainerGap(170, Short.MAX_VALUE)
        .add(txtInfo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(btnStartGame)
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnStartGame;
  private javax.swing.JTextArea txtInfo;
  // End of variables declaration//GEN-END:variables
  
  
  private void btnStartGameActionPerformed(java.awt.event.ActionEvent evt)
  {     
    try
    {
      ClientThread.Server.startGame(bomberman.client.ClientThread.Session, this.gameName);     
    }
    catch(Exception rexc)
    {
      rexc.printStackTrace();
    }    
  }    
  
  /**
   * Shows the background of the Panel
   * @param g
   */
  @Override
  public void paintComponent(Graphics g)
  {
    g.drawImage(background, 0, 0, null);
  }
}
