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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Kai Ritterbusch
 */
public class GameListTableModel extends AbstractTableModel
{
  private String[]		columnNames = {"Spielname","Ersteller","Spielerzahl","Status"};      // Spaltennamen 
  private ArrayList< ArrayList<Object> > data = new ArrayList< ArrayList<Object> >(); // Daten
  private boolean[]		sortColumnDesc;   // Sortierungsstatus
  
  public GameListTableModel()
  {
    this.sortColumnDesc = new boolean[columnNames.length];
  }
  
  public void addRow(ArrayList<Object> row)
  {
    data.add(row);
    this.fireTableRowsInserted(data.size()-1, data.size()-1);
  }
  
  public void deleteRow(int y)
  {
    data.remove(y);
    this.fireTableRowsDeleted(y, y);
  }
    
  /** Anzahl der Zeilen zurueckgeben */
  public int getRowCount() 
  {
    return data.size();
  }    
  
  /** Gibt das Objekt in der angegebenen Zeile und Spalte zurueck */
  public Object getValueAt(int row, int col) 
  {     
    return data.get(row).get(col);
  }
  
  /**
   * Gibt die durch den Index spezifizierte Zeile zurueck.
   * @param row
   * @return
   */
  public Object getRow(int row) 
  {
    return data.get(row);
  }
  
  /** Name einer Spalte zurueckgeben */
  public String getColumnName(int col) 
  {  
    return columnNames[col];
  }
  
  /** Datentyp einer Spalte zurueckgeben */
  public Class<?> getColumnClass(int c) 
  {
    return getValueAt(0, c).getClass();
  }
  
  /** Anzahl der Spalten */
  public int getColumnCount() 
  { 
    return columnNames.length;
  }
  
  public void setData(ArrayList<ArrayList<Object>> data)
  {
    this.data = data;
    fireTableDataChanged();
  }
  
  /** Setzen eines speziellen Wertes */
  @Override
  public void setValueAt(Object value, int row, int col)
  {
    data.get(row).set(col, value);    
    fireTableCellUpdated(row, col);    
  }
  
  @Override
  public boolean isCellEditable(int row, int col) 
  {
    return false;
  }
  
  /**
   * Gibt alle Zeilen der Tabelle zurueck.
   * @return
   */
  public ArrayList getRows()
  {   
    return data; 
  }
  
    /**
   * Gibt an ob die angegebene Spalte sortiert ist oder nicht.
   * @param col
   * @return
   */
  public boolean getSortState(int col)
  {
    return sortColumnDesc[col];
  }
  
    /** Sortiericon erstellen */
  private Icon createAscendingIcon(int col)
  {
    sortColumnDesc[col] = false;
    return new Icon()
    {
      public int getIconHeight() 
      {
        return 3;
      }

      public int getIconWidth() 
      {
        return 5;
      }

      public void paintIcon(Component c, Graphics g, int x, int y) 
      {
        g.setColor( Color.BLACK );
        g.drawLine( x, y, x+4, y );
        g.drawLine( x+1, y+1, x+3, y+1 );
        g.drawLine( x+2, y+2, x+2, y+2 );
      }
    };
  }
  
  /**
   * Sortiericon erstellen.
   * @param col
   * @return
   */
  private Icon createDescendingIcon(int col)
  {
    sortColumnDesc[col] = true;
    return new Icon(){
      public int getIconHeight() {
        return 3;
      }

      public int getIconWidth() {
        return 5;
      }

      public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor( Color.BLACK);
        g.drawLine( x, y+2, x+4, y+2 );
        g.drawLine( x+1, y+1, x+3, y+1 );
        g.drawLine( x+2, y, x+2, y );
      }
    };
  } 
  
  /** Sortieren */
  public void sortByColumn(final int col)
  {
    if(data.size() == 0)
      return;
    Collections.sort(data, new Comparator<ArrayList>()
    {
      public int compare(ArrayList v1, ArrayList v2)
      {        
        int size1 = v1.size();
        if(col >= size1)
          throw new IllegalArgumentException("Out of Bounds");
        
        Comparable s1 = (Comparable<?>) v1.get(col);
        Comparable s2 = (Comparable<?>) v2.get(col);
        
       
        
        int cmp = s1.compareTo(s2);
        if (sortColumnDesc[col])
        {
          
          cmp *= -1;
        }
        return cmp;
      }     
     });
     sortColumnDesc[col] ^= true; 
     
  }
}

