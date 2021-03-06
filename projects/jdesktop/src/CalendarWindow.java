/**
 * Created : Apr 4, 2012
 *
 * @author pquiring
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javaforce.JFLog;
import javaforce.linux.Linux;
import javax.swing.table.*;


public class CalendarWindow extends javax.swing.JWindow {

  /**
   * Creates new form CalendarWindow
   */
  public CalendarWindow(Point pos, int width) {
    super();
    initComponents();
    x11id = Linux.x11_get_id(this);
    JFLog.log("Calendar.window=0x" + x11id);
    try {
      Linux.x11_set_dock(x11id);
    } catch (Throwable t) {
      JFLog.log(t);
    }
    buildCalendar(true);
    Dimension d = getPreferredSize();
    setLocation(pos.x - d.width + width, pos.y - d.height - 5);
//    cal.setSelectionBackground(Color.green);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollTitle = new javax.swing.JScrollPane();
        cal = new javax.swing.JTable();
        title = new javax.swing.JLabel();
        prevYear = new javax.swing.JButton();
        prevMonth = new javax.swing.JButton();
        nextMonth = new javax.swing.JButton();
        nextYear = new javax.swing.JButton();
        today = new javax.swing.JButton();

        cal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S", "M", "T", "W", "T", "F", "S"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        cal.setCellSelectionEnabled(true);
        cal.getTableHeader().setReorderingAllowed(false);
        cal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                calMouseReleased(evt);
            }
        });
        scrollTitle.setViewportView(cal);
        cal.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        cal.getColumnModel().getColumn(0).setResizable(false);
        cal.getColumnModel().getColumn(1).setResizable(false);
        cal.getColumnModel().getColumn(2).setResizable(false);
        cal.getColumnModel().getColumn(3).setResizable(false);
        cal.getColumnModel().getColumn(4).setResizable(false);
        cal.getColumnModel().getColumn(5).setResizable(false);
        cal.getColumnModel().getColumn(6).setResizable(false);

        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("title");

        prevYear.setText("<<");
        prevYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevYearActionPerformed(evt);
            }
        });

        prevMonth.setText("<");
        prevMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevMonthActionPerformed(evt);
            }
        });

        nextMonth.setText(">");
        nextMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextMonthActionPerformed(evt);
            }
        });

        nextYear.setText(">>");
        nextYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextYearActionPerformed(evt);
            }
        });

        today.setText("Today");
        today.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                todayActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrollTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(prevYear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(prevMonth)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextMonth)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextYear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addComponent(today)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(prevYear)
                    .addComponent(prevMonth)
                    .addComponent(nextMonth)
                    .addComponent(nextYear)
                    .addComponent(today))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  private void todayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_todayActionPerformed
    year = month = day = -1;
    buildCalendar(true);
  }//GEN-LAST:event_todayActionPerformed

  private void prevYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevYearActionPerformed
    year--;
    buildCalendar(false);
  }//GEN-LAST:event_prevYearActionPerformed

  private void prevMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevMonthActionPerformed
    month--;
    if (month < 0) {month = 11; year--;}
    buildCalendar(false);
  }//GEN-LAST:event_prevMonthActionPerformed

  private void nextMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextMonthActionPerformed
    month++;
    if (month == 12) {month = 0; year++;}
    buildCalendar(false);
  }//GEN-LAST:event_nextMonthActionPerformed

  private void nextYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextYearActionPerformed
    year++;
    buildCalendar(false);
  }//GEN-LAST:event_nextYearActionPerformed

  private void calMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_calMouseReleased
    int row = cal.getSelectedRow();
    int col = cal.getSelectedColumn();
    Integer selday = (Integer)cal.getValueAt(row,col);
    if (selday == null) return;
    if (selday.intValue() <= 0) return;
    day = selday.intValue();
    title.setText(months[month] + " " + day + ", " + year);
  }//GEN-LAST:event_calMouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable cal;
    private javax.swing.JButton nextMonth;
    private javax.swing.JButton nextYear;
    private javax.swing.JButton prevMonth;
    private javax.swing.JButton prevYear;
    private javax.swing.JScrollPane scrollTitle;
    private javax.swing.JLabel title;
    private javax.swing.JButton today;
    // End of variables declaration//GEN-END:variables

  private int year = -1, month = -1, day = -1;
  private Object x11id;

  private void buildCalendar(boolean today) {
    DefaultTableModel model = (DefaultTableModel)cal.getModel();
    while (model.getRowCount() > 0) model.removeRow(0);
    Calendar c = Calendar.getInstance();
    if (month != -1) {
      c.set(Calendar.MONTH, month);
      c.set(Calendar.YEAR, year);
    } else {
      month = c.get(Calendar.MONTH);
      year = c.get(Calendar.YEAR);
      day = c.get(Calendar.DAY_OF_MONTH);
    }
    c.set(Calendar.DAY_OF_MONTH, 1);
    int firstDay = c.get(Calendar.DAY_OF_WEEK)-1;
    int daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
    if (day > daysInMonth) day = daysInMonth;
    c.set(Calendar.DAY_OF_MONTH, day);
    Integer daysInWeek[];
    int dayNumber = 1;
    int row=0, col=firstDay;
    int todayRow = -1, todayCol = -1;
    while (daysInMonth > 0) {
      daysInWeek = new Integer[7];
      for(int a=firstDay;a < 7 && daysInMonth > 0;a++) {
        if ((today) && (dayNumber == day)) {
          todayRow = row;
          todayCol = col;
        }
        daysInWeek[a] = dayNumber++;
        daysInMonth--;
        col++;
      }
      model.addRow(daysInWeek);
      firstDay = 0;
      row++;
      col = 0;
    }
    if (today) {
      cal.changeSelection(todayRow, todayCol, false, false);
    }
    title.setText(months[month] + " " + day + ", " + year);
  }

  private static String months[] = {"January", "Febuary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

  public void addMouseListener(MouseListener ml) {
    super.addMouseListener(ml);
    cal.addMouseListener(ml);
    cal.getParent().addMouseListener(ml);  //the JViewPort that is added automatically
//    cal.getParent().getParent().addMouseListener(ml);  //the JScrollPane that is added automatically
    cal.getTableHeader().addMouseListener(ml);
    prevYear.addMouseListener(ml);
    prevMonth.addMouseListener(ml);
    nextMonth.addMouseListener(ml);
    nextYear.addMouseListener(ml);
    today.addMouseListener(ml);
  }
}
