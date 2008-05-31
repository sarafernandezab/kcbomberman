/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bomberman.client.gui;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 *
 * @author Kai Ritterbusch
 * @author Christian Lins
 */
public class PlaygroundPanel extends JPanel
{
  private ElementPainter[][] elementPainter;
  
  public PlaygroundPanel(int rows, int cols)
  {
    setBackground(Color.BLACK);
    
    GridBagConstraints gbc = new GridBagConstraints();
    GridBagLayout gbl = new GridBagLayout(); 
    setLayout(gbl);
    gbc.fill = GridBagConstraints.BOTH;    

    gbc.gridwidth   = 1;
    gbc.gridheight  = 1;
    //gbc.anchor = GridBagConstraints.NORTH;
    gbc.gridx = 0;
    gbc.gridy = 0;
    //gbc.weightx = 1;
    //gbc.weighty = 1;
    gbc.insets = new Insets(1,1,1,1);  // Abstaende
    
    this.elementPainter = new ElementPainter[rows][cols]; 
    gbl.setConstraints(this, gbc);
    
    for(int y = 0; y < rows; y++)
    {
      gbc.gridy = y;
      for(int x = 0; x < cols; x++)
      {
        gbc.gridx = x;
        this.elementPainter[y][x] = new ElementPainter();
        add(this.elementPainter[y][x], gbc);        
      } 
    }
    
    MainFrame.getInstance().setSize(
        (cols + 2) * ElementPainter.DEFAULT_SIZE,
        (rows + 2) * ElementPainter.DEFAULT_SIZE);
  }
}
