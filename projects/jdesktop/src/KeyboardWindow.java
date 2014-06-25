/**
 * Created : July 27, 2012
 *
 * @author pquiring
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javaforce.*;
import javaforce.linux.*;

public class KeyboardWindow extends javax.swing.JWindow implements ActionListener, LayoutManager {

  /**
   * Creates new form Keyboard
   */
  public KeyboardWindow() {
    initComponents();
    setLayout(this);
    x11id = Linux.x11_get_id(this);
    JFLog.log("Keyboard.window=0x" + x11id);
    Linux.x11_set_dock(x11id);
    initTable();
    hide.setIcon(IconCache.loadIcon("jdesktop-keyboard-down"));
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        q = new javax.swing.JButton();
        w = new javax.swing.JButton();
        e = new javax.swing.JButton();
        r = new javax.swing.JButton();
        t = new javax.swing.JButton();
        y = new javax.swing.JButton();
        u = new javax.swing.JButton();
        i = new javax.swing.JButton();
        o = new javax.swing.JButton();
        p = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        a = new javax.swing.JButton();
        s = new javax.swing.JButton();
        d = new javax.swing.JButton();
        f = new javax.swing.JButton();
        g = new javax.swing.JButton();
        h = new javax.swing.JButton();
        j = new javax.swing.JButton();
        k = new javax.swing.JButton();
        l = new javax.swing.JButton();
        backspace = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        shift = new javax.swing.JToggleButton();
        z = new javax.swing.JButton();
        x = new javax.swing.JButton();
        c = new javax.swing.JButton();
        v = new javax.swing.JButton();
        b = new javax.swing.JButton();
        n = new javax.swing.JButton();
        m = new javax.swing.JButton();
        enter = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        mode = new javax.swing.JButton();
        comma = new javax.swing.JButton();
        space = new javax.swing.JButton();
        period = new javax.swing.JButton();
        hide = new javax.swing.JButton();

        setAlwaysOnTop(true);
        setName("keyboard");
        getContentPane().setLayout(new java.awt.GridLayout(4, 1));

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        q.setText("q");
        jPanel1.add(q);

        w.setText("w");
        jPanel1.add(w);

        e.setText("e");
        jPanel1.add(e);

        r.setText("r");
        jPanel1.add(r);

        t.setText("t");
        jPanel1.add(t);

        y.setText("y");
        jPanel1.add(y);

        u.setText("u");
        jPanel1.add(u);

        i.setText("i");
        jPanel1.add(i);

        o.setText("o");
        jPanel1.add(o);

        p.setText("p");
        jPanel1.add(p);

        getContentPane().add(jPanel1);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        a.setText("a");
        jPanel2.add(a);

        s.setText("s");
        jPanel2.add(s);

        d.setText("d");
        jPanel2.add(d);

        f.setText("f");
        jPanel2.add(f);

        g.setText("g");
        jPanel2.add(g);

        h.setText("h");
        jPanel2.add(h);

        j.setText("j");
        jPanel2.add(j);

        k.setText("k");
        jPanel2.add(k);

        l.setText("l");
        jPanel2.add(l);

        backspace.setText("<");
        backspace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backspaceActionPerformed(evt);
            }
        });
        jPanel2.add(backspace);

        getContentPane().add(jPanel2);

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        shift.setText("Shift");
        jPanel3.add(shift);

        z.setText("z");
        jPanel3.add(z);

        x.setText("x");
        jPanel3.add(x);

        c.setText("c");
        jPanel3.add(c);

        v.setText("v");
        jPanel3.add(v);

        b.setText("b");
        jPanel3.add(b);

        n.setText("n");
        jPanel3.add(n);

        m.setText("m");
        jPanel3.add(m);

        enter.setText("Enter");
        enter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterActionPerformed(evt);
            }
        });
        jPanel3.add(enter);

        getContentPane().add(jPanel3);

        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        mode.setText("?123");
        mode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modeActionPerformed(evt);
            }
        });
        jPanel4.add(mode);

        comma.setText(",");
        jPanel4.add(comma);

        space.setText("Space");
        space.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spaceActionPerformed(evt);
            }
        });
        jPanel4.add(space);

        period.setText(".");
        jPanel4.add(period);

        hide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideActionPerformed(evt);
            }
        });
        jPanel4.add(hide);

        getContentPane().add(jPanel4);

        pack();
    }// </editor-fold>//GEN-END:initComponents

  private void modeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modeActionPerformed
    symMode = !symMode;
    if (symMode) {
      mode.setText("ABC");
    } else {
      mode.setText("?123");
    }
    for(int idx=0;idx<table.length;idx++) {
      JButton button = (JButton)table[idx][0];
      button.setText((String)table[idx][symMode ? 2 : 1]);
    }
  }//GEN-LAST:event_modeActionPerformed

  private void spaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spaceActionPerformed
    int keycode = Linux.x11_keysym_to_keycode(' ');
    Linux.x11_send_event(keycode, true);
    JF.sleep(10);
    Linux.x11_send_event(keycode, false);
  }//GEN-LAST:event_spaceActionPerformed

  private void hideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideActionPerformed
    Dock.dock.hideKeyboard();
  }//GEN-LAST:event_hideActionPerformed

  private void backspaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backspaceActionPerformed
    Linux.x11_send_event(22, true);
    JF.sleep(10);
    Linux.x11_send_event(22, false);
  }//GEN-LAST:event_backspaceActionPerformed

  private void enterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterActionPerformed
    Linux.x11_send_event(36, true);
    JF.sleep(10);
    Linux.x11_send_event(36, false);
  }//GEN-LAST:event_enterActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton a;
    private javax.swing.JButton b;
    private javax.swing.JButton backspace;
    private javax.swing.JButton c;
    private javax.swing.JButton comma;
    private javax.swing.JButton d;
    private javax.swing.JButton e;
    private javax.swing.JButton enter;
    private javax.swing.JButton f;
    private javax.swing.JButton g;
    private javax.swing.JButton h;
    private javax.swing.JButton hide;
    private javax.swing.JButton i;
    private javax.swing.JButton j;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton k;
    private javax.swing.JButton l;
    private javax.swing.JButton m;
    private javax.swing.JButton mode;
    private javax.swing.JButton n;
    private javax.swing.JButton o;
    private javax.swing.JButton p;
    private javax.swing.JButton period;
    private javax.swing.JButton q;
    private javax.swing.JButton r;
    private javax.swing.JButton s;
    private javax.swing.JToggleButton shift;
    private javax.swing.JButton space;
    private javax.swing.JButton t;
    private javax.swing.JButton u;
    private javax.swing.JButton v;
    private javax.swing.JButton w;
    private javax.swing.JButton x;
    private javax.swing.JButton y;
    private javax.swing.JButton z;
    // End of variables declaration//GEN-END:variables

  private Object x11id;
  private boolean symMode;  //symbol mode
  private Object[][] table;

  private void initTable() {
    table = new Object[][] {
      {q, "q", "1"},
      {w, "w", "2"},
      {e, "e", "3"},
      {r, "r", "4"},
      {t, "t", "5"},
      {y, "y", "6"},
      {u, "u", "7"},
      {i, "i", "8"},
      {o, "o", "9"},
      {p, "p", "0"},
      {a, "a", "!"},
      {s, "s", "@"},
      {d, "d", "#"},
      {f, "f", "$"},
      {g, "g", "%"},
      {h, "h", "^"},
      {j, "j", "&"},
      {k, "k", "*"},
      {l, "l", "-"},
      {z, "z", "("},
      {x, "x", ")"},
      {c, "c", "="},
      {v, "v", "["},
      {b, "b", "]"},
      {n, "n", ":"},
      {m, "m", ";"},
      {comma, ",", "'"},
      {period, ".", "\""}
    };
    for(int idx=0;idx<table.length;idx++) {
      JButton button = (JButton)table[idx][0];
      button.addActionListener(this);
    }
  }

  public void actionPerformed(ActionEvent ae) {
    Linux.x11_set_dock(x11id);
    JButton button = (JButton)ae.getSource();
    String txt = button.getText();
    int keycode = Linux.x11_keysym_to_keycode(txt.charAt(0));
    if (!symMode && shift.isSelected()) {
      keycode += 0x100;  //X11_SHIFT
    }
    Linux.x11_send_event(keycode, true);
    JF.sleep(10);
    Linux.x11_send_event(keycode, false);
  }

  public void setPosition(int keyboadHeight, int dockHeight) {
    if (keyboadHeight == 0) keyboadHeight = 1;
    Rectangle s = JF.getMaximumBounds();
    setBounds(0, s.height - dockHeight - keyboadHeight, s.width, keyboadHeight);
  }

  public void addLayoutComponent(String string, Component cmpnt) {
  }

  public void removeLayoutComponent(Component cmpnt) {
  }

  public Dimension preferredLayoutSize(Container p) {
    return new Dimension(1,1);
  }

  public Dimension minimumLayoutSize(Container p) {
    return new Dimension(1,1);
  }

  public void layoutContainer(Container p) {
    int width = getWidth();
    jPanel1.setBounds(0, 0, width, 50);
    jPanel2.setBounds(0, 50, width, 50);
    jPanel3.setBounds(0, 100, width, 50);
    jPanel4.setBounds(0, 150, width, 50);
  }
}