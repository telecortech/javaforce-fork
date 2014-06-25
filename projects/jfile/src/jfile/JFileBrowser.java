package jfile;

/**
 * Created : Aug 11, 2012
 *
 * @author pquiring
 */

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import javaforce.*;
import javaforce.jbus.*;
import javaforce.linux.*;
import javaforce.utils.*;

public class JFileBrowser extends javax.swing.JComponent implements MouseListener, MouseMotionListener, monitordir.Listener {

  /**
   * Creates new form JFileBrowser
   * @param view - any of the VIEW_... types
   * @param path - initial path to view (local system only)
   * @param desktopMenu - popup menu for unused areas of panel
   * @param fileMenu - popup menu for files
   * @param wallPaperFile - PNG file to wallpaper panel (optional)
   * @param wallPaperView - WALLPAPER_VIEW_... types
   * @param saveConfig - saves layout to .desktop file
   * @param openFolder - program to open folders with (else just navigates to them) (optional)
   * @param openFile - program to open files
   * @param backClr - background color (selections) and (if no wallpaper)
   * @param foreClr - text color
   * @param useScrolling - use scroll bars?
   * @param arrangeIconsVertical - arrange icons vertical (else horizontal)
   * @param showHidden - show hidden files (files that begin with a period)
   * @param autoArrange - auto arrange by name
   * @param jbusClient - a JBusClient client
   * @param desktopMode - desktopMode (always use openFile for files)
   */

  public JFileBrowser(int view, String path, JPopupMenu desktopMenu, JPopupMenu fileMenu
    , String wallPaperFile, int wallPaperView, boolean saveConfig
    , String openFolder, String openFile, Color backClr, Color foreClr
    , boolean useScrolling, boolean arrangeIconsVertical, boolean showHidden, boolean autoArrange
    , JBusClient jbusClient, boolean desktopMode)
  {
    setView(view);
    this.desktopMenu = desktopMenu;
    this.fileMenu = fileMenu;
    this.wallPaperFile = wallPaperFile;
    this.wallPaperView = wallPaperView;
    this.saveConfig = saveConfig;
    this.openFolder = openFolder;
    this.openFile = openFile;
    this.backClr = backClr;
    this.foreClr = foreClr;
    this.useScrolling = useScrolling;
    this.arrangeIconsVertical = arrangeIconsVertical;
    this.showHidden = showHidden;
    this.autoArrange = autoArrange;
    this.jbusClient = jbusClient;
    this.desktopMode = desktopMode;
    loadWallPaper();
    setLayout(null);
    setFocusable(true);
    setPath(path);  //must do last since it calls refresh()
  }

  public static final int VIEW_ICONS = 1;
  public static final int VIEW_LIST = 2;
  public static final int VIEW_DETAILS = 3;

  public static final int WALLPAPER_VIEW_CENTER = 0;
  public static final int WALLPAPER_VIEW_TILE = 1;
  public static final int WALLPAPER_VIEW_FULL = 2;
  public static final int WALLPAPER_VIEW_FILL = 3;

  private int view;
  private String path;
  private String mount;
  private JPopupMenu desktopMenu, fileMenu;
  private String wallPaperFile;
  private int wallPaperView;
  private boolean saveConfig;
  private String openFolder, openFile;
  public Color backClr, foreClr;
  private boolean useScrolling, arrangeIconsVertical;
  private boolean showHidden;
  private boolean autoArrange;
  private boolean desktopMode;
  private JFileBrowserListener listener;
  private JBusClient jbusClient;

  private JFImage wallPaper;

  //icon view dimensions
  public static int ix, iy;  //icon image size
  public static int bx, by;  //icon size

  private int nx, ny;  //next position
  private int mx, my;  //last mouse pressed

  private ArrayList<FileEntry> entries = new ArrayList<FileEntry>();

  private JScrollPane scroll;
  private JPanel panel;

  private boolean iconsVisible = true;

  public void setBounds(int x,int y,int w,int h) {
    super.setBounds(x,y,w,h);
//    JFLog.log("setBounds:" + getWidth() + "," + getHeight());
    if (scroll != null) {
      scroll.setBounds(0,0,getWidth(),getHeight());
      calcPanelSize();
    } else if (panel != null) {
      panel.setBounds(0,0,getWidth(),getHeight());
    }
    if (listener != null) listener.browserResized(this);
    update();
  }

  public void setListener(JFileBrowserListener listener) {
    this.listener = listener;
  }

  private void initGUI() {
//    JFLog.log("initGUI:" + getWidth() + "," + getHeight());
    panel = new JPanel() {
      public Dimension getPreferredSize() {
        return getSize();
      }
      public void paintChildren(Graphics g) {
        if (!iconsVisible) return;
        super.paintChildren(g);
      }
      public void paintComponent(Graphics g) {
//        JFLog.log("JFB:paint");
        g.setColor(backClr);
        int px = getWidth();
        int py = getHeight();
        g.fillRect(0,0,px,py);
        if (wallPaper != null) {
          //paint wallpaper
          int wx = wallPaper.getWidth();
          int wy = wallPaper.getHeight();
          switch (wallPaperView) {
            case WALLPAPER_VIEW_CENTER:
              g.drawImage(wallPaper.getImage(), (px - wx) / 2, (py - wy) / 2, null);
              break;
            case WALLPAPER_VIEW_TILE:
              Image img = wallPaper.getImage();
              for(int x=0;x<px;x+=wx) {
                for(int y=0;y<py;y+=wy) {
                  g.drawImage(img, x, y, null);
                }
              }
              break;
            case WALLPAPER_VIEW_FULL:
              //maintains aspect ratio
              int dx = px - wx;
              int dy = py - wy;
              if (dx > dy) {
                //image touches top/bottom (bars left/right)
                g.drawImage(wallPaper.getImage(), dx/2, 0, null);
              } else {
                //image touches left/right (bars top/bottom)
                g.drawImage(wallPaper.getImage(), 0, dy/2, null);
              }
              break;
            case WALLPAPER_VIEW_FILL:
              //ignore aspect ration : can distort image
              g.drawImage(wallPaper.getImage(), 0, 0, px, py, null);
              break;
          }
        }
        if (dragselection) {
          g.setColor(new Color(0x77003377, true));
          int x1, y1, x2, y2;
          if (dragX < selX) {
            x1 = dragX;
            x2 = selX;
          } else {
            x2 = dragX;
            x1 = selX;
          }
          if (dragY < selY) {
            y1 = dragY;
            y2 = selY;
          } else {
            y2 = dragY;
            y1 = selY;
          }
          g.fillRect(x1,y1,x2-x1,y2-y1);
        }
      }
    };
    panel.addMouseListener(this);
    panel.addMouseMotionListener(this);
    panel.setLayout(null);
    panel.setBounds(0,0,getWidth(),getHeight());
    if (useScrolling) {
      scroll = new JScrollPane(panel);
      scroll.setBounds(0,0,getWidth(),getHeight());
      add(scroll);
    } else {
      add(panel);
    }
  }

  private void listFiles() {
//    JFLog.log("path=" + path);
    File file = new File(path);
    File files[] = file.listFiles();
    if (files == null) return;
    Arrays.sort(files);
    if (view != VIEW_ICONS) {
      bx = 0;
      by = 18;
      JFImage tmp = new JFImage(1,1);
      for(int a=0;a<files.length;a++) {
        int w = ix + tmp.getGraphics().getFontMetrics().stringWidth(files[a].getName());
        if (w > bx) bx = w;
      }
    }
    for(int a=0;a<files.length;a++) {
      String fullfile = files[a].getAbsolutePath();
      if (fullfile.endsWith("/.desktop")) continue;
      int idx = fullfile.lastIndexOf(File.separatorChar);
      if (fullfile.substring(idx+1).startsWith(".")) {
        if (!showHidden) continue;
      }
      if (files[a].isDirectory()) {
        addFolder(fullfile);
      } else {
        if (fullfile.endsWith(".desktop")) {
          addDesktopFile(fullfile);
        } else {
          addFile(fullfile);
        }
      }
    }
  }

  private void nextPosition(FileEntry entry, boolean check) {
    switch (view) {
      case VIEW_ICONS:
        nextPositionIcons(entry, check);
        break;
      case VIEW_LIST:
        nextPositionList(entry);
        break;
      case VIEW_DETAILS:
        nextPositionDetails(entry);
        break;
    }
  }

  private void nextPositionIcons(FileEntry entry, boolean check) {
    boolean noRoom = false;
    boolean tooClose;
    int px = getWidth();
    int py = getHeight();
    do {
      entry.x = nx;
      entry.y = ny;
      if (arrangeIconsVertical) {
        ny += by + 5;
        if (ny + by > py) {
          nx += bx + 5;
          ny = 5;
        }
        if (nx + bx > px && !useScrolling) {
          if (noRoom) {
            entry.x = 5;
            entry.y = 5;
            return;
          }
          nx = 5;
          noRoom = true;
        }
      } else {
        nx += bx + 5;
        if (nx + bx > px) {
          ny += by + 5;
          nx = 5;
        }
        if (ny + by > py && !useScrolling) {
          if (noRoom) {
            entry.x = 5;
            entry.y = 5;
            return;
          }
          ny = 5;
          noRoom = true;
        }
      }
      tooClose = false;
      if (check) {
        tooClose = checkProximity(entry);
      }
    } while(tooClose);
  }

  private void nextPositionList(FileEntry entry) {
    entry.x = nx;
    entry.y = ny;
    int px = getWidth();
    int py = getHeight();
    if ((arrangeIconsVertical) || (true)) {
      ny += by;
      if (ny + by > py) {
        nx += bx;
        ny = 0;
      }
    } else {
      //doesn't make sense to use this ever
      nx += bx;
      if (nx + bx > px) {
        ny += by;
        nx = 0;
      }
    }
  }

  private void nextPositionDetails(FileEntry entry) {
    entry.x = nx;
    entry.y = ny;
    ny += by;
  }

  public boolean checkProximity(FileEntry entry) {
    for(int a=0;a<entries.size();a++) {
      FileEntry otherEntry = entries.get(a);
      if (otherEntry == entry) continue;
      int dx = entry.x - otherEntry.x;
      int dy = entry.y - otherEntry.y;
      int len = (int)Math.sqrt(dx * dx + dy * dy);
      if (len < bx) return true;
    }
    return false;
  }

  private void addDetails(FileEntry entry) {
    File file = new File(entry.file);
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(file.lastModified());
    String date = String.format("%d-%d-%d", c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
    String time = String.format("%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
    String size = "" + file.length();

    entry.details_date = new JLabel(date);
    entry.details_date.setForeground(foreClr);
    panel.add(entry.details_date);
    Dimension dim = entry.details_date.getPreferredSize();
    entry.details_date.setBounds(entry.x + bx, entry.y, dim.width, dim.height);

    entry.details_time = new JLabel(time);
    entry.details_time.setForeground(foreClr);
    panel.add(entry.details_time);
    dim = entry.details_time.getPreferredSize();
    entry.details_time.setBounds(entry.x + bx + 96, entry.y, dim.width, dim.height);

    entry.details_size = new JLabel(size);
    entry.details_size.setForeground(foreClr);
    panel.add(entry.details_size);
    dim = entry.details_size.getPreferredSize();
    entry.details_size.setBounds(entry.x + bx + 96 + 64, entry.y, dim.width, dim.height);
  }

  private JFileIcon addIcon(FileEntry entry) {
    for(int a=0;a<entries.size();a++) {
      if (entries.get(a).file.equals(entry.file)) {
        return entries.get(a).button;
      }
    }
    if (!new File(entry.file).exists()) {
      JFLog.log("Error:File not found:" + entry.file);
      return null;
    }
    int px = getWidth();
    int py = getHeight();
    JFImage buttonImage = IconCache.loadIcon(entry.icon);
    buttonImage = IconCache.scaleIcon(buttonImage, ix, iy);
    entry.button = new JFileIcon(this, buttonImage, entry, view == VIEW_ICONS);
    entry.button.setToolTipText(entry.name);
    entry.button.addMouseListener(this);
    entry.button.addMouseMotionListener(this);
    if ((!useScrolling) && (view == VIEW_ICONS)) {
      if ((entry.x + bx > px) || (entry.y + by > py)) {
        nextPosition(entry, true);
      }
    }
    panel.add(entry.button);
    entry.button.setLocation(entry.x, entry.y);
    entries.add(entry);
    if (view == VIEW_DETAILS) {
      addDetails(entry);
    }
    return entry.button;
  }

  private JFileIcon addNewIcon(String icon, String name, String file) {
    FileEntry entry = new FileEntry();
    nextPosition(entry, false);
    entry.icon = icon;
    entry.name = name;
    entry.file = file;
    return addIcon(entry);
  }

  private JFileIcon addNewIconDesktop(String fn) {
    String name = null, icon = null;
    try {
      FileInputStream fis = new FileInputStream(fn);
      byte data[] = JF.readAll(fis);
      fis.close();
      String str = new String(data);
      String lns[] = str.split("\n");
      boolean desktopEntry = false;
      for(int a=0;a<lns.length;a++) {
        if (lns[a].startsWith("[Desktop Entry]")) {
          desktopEntry = true;
          continue;
        }
        if (lns[a].startsWith("[")) desktopEntry = false;
        if (!desktopEntry) continue;
        if (lns[a].startsWith("Name=")) {
          name = lns[a].substring(5);
        }
        if (lns[a].startsWith("Icon=")) {
          icon = lns[a].substring(5);
        }
      }
      if (name == null) JFLog.log("Warning:No NAME field for icon:" + fn);
      if (icon == null) icon = "jfile-404";
      int i1 = fn.lastIndexOf(File.separatorChar);
      name = fn.substring(i1+1, fn.length() - 8);  //.desktop
      return addNewIcon(icon, name, fn);
    } catch (FileNotFoundException e) {
      JFLog.log("File not found:" + fn);
      return null;
    } catch (Exception e) {
      JFLog.log(e);
      return null;
    }
  }

  private String getFileName(String fn) {
    int idx;
    idx = fn.lastIndexOf(File.separatorChar);
    if (idx != -1) fn = fn.substring(idx+1);
    if (!fn.endsWith(".desktop")) return fn;
    //remove .desktop extension
    idx = fn.lastIndexOf(".");
    if (idx == -1) return fn;
    return fn.substring(0, idx);
  }

  private String getFolderName(String fn) {
    int idx = fn.lastIndexOf(File.separatorChar);
    if (idx == -1) return fn;
    return fn.substring(idx+1);
  }

  private void addFile(String fn) {
    switch (view) {
      case VIEW_ICONS:
      case VIEW_LIST:
      case VIEW_DETAILS:
        addNewIcon(IconCache.findIcon(fn), getFileName(fn), fn);
        break;
    }
  }

  private void addDesktopFile(String fn) {
    switch (view) {
      case VIEW_ICONS:
      case VIEW_LIST:
      case VIEW_DETAILS:
        addNewIconDesktop(fn);
        break;
    }
  }

  private void addFolder(String fn) {
    JFileIcon fi = null;
    String icon = NetworkShares.isShared(fn) ? "jfile-folder-shared" : "jfile-folder";
    switch (view) {
      case VIEW_ICONS:
      case VIEW_LIST:
      case VIEW_DETAILS:
        fi = addNewIcon(icon, getFolderName(fn), fn);
        break;
    }
    if (fi == null) return;
    fi.entry.isDir = true;
  }

  private void loadWallPaper() {
    if (wallPaperFile == null) return;
    wallPaper = new JFImage();
    wallPaper.load(wallPaperFile);
  }

  public void setWallPaper(String file, int mode) {
    wallPaperFile = file;
    wallPaperView = mode;
    loadWallPaper();
    repaint();
  }

  public class Config {
    public FileEntry file[];
  }

  private void loadConfig() {
    if (!saveConfig) return;
    try {
      String fn = path + "/.desktop";
      Config config = new Config();
      if (!new File(fn).exists()) {
        JFLog.log("Error:File not found:" + fn);
        return;
      }
      FileInputStream fis = new FileInputStream(fn);
      XML xml = new XML();
      xml.read(fis);
      xml.writeClass(config);
      fis.close();
      for(int a=0;a<config.file.length;a++) {
        addIcon(config.file[a]);
      }
    } catch (Exception e) {
      JFLog.log(e);
    }
  }

  private void saveConfig() {
    if (!saveConfig) return;
    //save Entries to .desktop file
    try {
      Config config = new Config();
      config.file = entries.toArray(new FileEntry[0]);
      FileOutputStream fos = new FileOutputStream(path + "/.desktop");
      XML xml = new XML();
      xml.readClass("desktop", config);
      xml.write(fos);
      fos.close();
    } catch (Exception e) {
      JFLog.log(e);
    }
  }

  @SuppressWarnings("unchecked")
  public void arrangeByName() {
    if (view != VIEW_ICONS) return;
    try {
      nx = 5;
      ny = 5;
      //place Home first
      FileEntry entry;
      Object list[] = panel.getComponents();
      Arrays.sort(list, new Comparator() {
        public int compare(Object t1, Object t2) {
          if (!(t1 instanceof JFileIcon)) return 1;
          if (!(t2 instanceof JFileIcon)) return -1;
          JFileIcon b1 = (JFileIcon)t1;
          JFileIcon b2 = (JFileIcon)t2;
          String s1 = b1.getText();
          if (s1.equals("Home")) return -1;
          String s2 = b2.getText();
          if (s2.equals("Home")) return 1;
          return s1.compareTo(s2);
        }
      });
      for(int a=0;a<list.length;a++) {
        if (!(list[a] instanceof JFileIcon)) continue;
        JFileIcon button = (JFileIcon)list[a];
        entry = button.entry;
        if (entry == null) continue;
        nextPosition(entry, false);
        button.setLocation(entry.x, entry.y);
      }
      saveConfig();
      update();
    } catch (Exception e) {
      JFLog.log(e);
    }
  }

  public void arrangeByGrid() {
    if (view != VIEW_ICONS) return;
    try {
      FileEntry entry;
      Object list[] = panel.getComponents();
      int xx = 5 + bx;
      int yy = 5 + by;
      for(int a=0;a<list.length;a++) {
        if (!(list[a] instanceof JFileIcon)) continue;
        JFileIcon button = (JFileIcon)list[a];
        entry = button.entry;
        if (entry == null) continue;
        entry.x = ((((entry.x-5) + (xx/2)) / xx) * xx) + 5;
        entry.y = ((((entry.y-5) + (yy/2)) / yy) * yy) + 5;
        if (checkProximity(entry)) {
          nx = entry.x;
          ny = entry.y;
          nextPosition(entry, true);
        }
        button.setLocation(entry.x, entry.y);
      }
      saveConfig();
      update();
    } catch (Exception e) {
      JFLog.log(e);
    }
  }

  private void calcPanelSize() {
    int px = 0, py = 0;
    int w = bx;
    if (view == VIEW_DETAILS) {
      w += 256;  //details columns
    }
    for(int a=0;a<entries.size();a++) {
      FileEntry entry = entries.get(a);
      int x = entry.x + w;
      if (x > px) px = x;
      int y = entry.y + by;
      if (y > py) py = y;
    }
    panel.setSize(px, py);
  }

  public void refresh() {
    if (path == null) return;
    try {
      stopFolderListener();
      removeAll();
      entries.clear();
      switch (view) {
        case VIEW_ICONS:
          nx = 5;
          ny = 5;
          break;
        case VIEW_LIST:
          nx = 0;
          ny = 0;
          break;
        case VIEW_DETAILS:
          nx = 0;
          ny = 0;
          break;
      }
      initGUI();
      initDND();
      loadConfig();
      listFiles();
      if (useScrolling) calcPanelSize();
      update();
      startFolderListener();
    } catch (Exception e) {
      JFLog.log(e);
    }
  }

  private void clearSelection() {
    int cnt = panel.getComponentCount();
    for(int a=0;a<cnt;a++) {
      JComponent c = (JComponent)panel.getComponent(a);
      if (!(c instanceof JFileIcon)) continue;
      JFileIcon b = (JFileIcon)c;
      b.setSelected(false);
    }
  }

  private void selectIcons() {
    //select icons from dragX/Y to selX/Y
    int x1, y1, x2, y2;
    if (dragX < selX) {
      x1 = dragX;
      x2 = selX;
    } else {
      x2 = dragX;
      x1 = selX;
    }
    if (dragY < selY) {
      y1 = dragY;
      y2 = selY;
    } else {
      y2 = dragY;
      y1 = selY;
    }
    int cnt = panel.getComponentCount();
    for(int a=0;a<cnt;a++) {
      JComponent c = (JComponent)panel.getComponent(a);
      if (!(c instanceof JFileIcon)) continue;
      JFileIcon b = (JFileIcon)c;
      Point p = b.getLocation();
      p.x += b.getWidth()/2;
      p.y += b.getHeight()/2;
      if ((p.x >= x1) && (p.x <= x2) && (p.y >= y1) && (p.y <= y2)) {
        b.setSelected(true);
      } else {
        b.setSelected(false);
      }
    }
  }

  public void setPath(String newpath) {
    if (newpath == null) return;
    try {
      if (mount != null) {
        if (!newpath.startsWith(mount)) {
          closeFile();
        }
      }
      if (new File(newpath).isDirectory()) {
        path = newpath;
      } else {
        openFile(newpath);
      }
      refresh();
      if (listener != null) listener.browserChangedPath(this, newpath);
    } catch (Exception e) {
      JFLog.log(e);
    }
  }

  public String getPath() {
    return path;
  }

  private void execute(String cmd[], boolean wait) {
    try {
      if (wait) {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(cmd);
        Process p = pb.start();
        p.waitFor();
      } else {
        Runtime.getRuntime().exec(cmd);
      }
    } catch (Exception e) {
      JFLog.log(e);
    }
  }

  private void openEntry(FileEntry entry) {
    File file = new File(entry.file);
    if (!file.exists()) return;
    if (file.isDirectory()) {
      if (openFolder == null) {
        setPath(entry.file);
      } else {
        execute(new String[] {openFolder, entry.file}, false);
      }
    } else {
      if (entry.file.endsWith(".desktop")) {
        //execute desktop file
        try {
          FileInputStream fis = new FileInputStream(entry.file);
          byte data[] = JF.readAll(fis);
          fis.close();
          String str = new String(data);
          String lns[] = str.split("\n");
          for(int a=0;a<lns.length;a++) {
            if (lns[a].startsWith("Exec=")) {
              String cmd[] = Linux.expandDesktopExec(lns[a].substring(5).trim(), "");
              Runtime.getRuntime().exec(cmd);
              return;
            }
          }
        } catch (Exception e) {
          JFLog.log(e);
        }
      } else if (!desktopMode && entry.file.toLowerCase().endsWith(".iso")) {
        openFile(entry.file);
      } else if (!desktopMode && entry.file.toLowerCase().endsWith(".zip")) {
        openFile(entry.file);
      } else {
        if (openFile == null) return;
        execute(new String[] {openFile, entry.file}, false);
      }
    }
  }

  public void invoke(JFileIcon b) {
    openEntry(b.entry);
  }

  private java.util.Timer refreshTimer;
  private Vector<String> newFiles = new Vector<String>();
  private Vector<String> removeFiles = new Vector<String>();
  private final Object lock = new Object();

  public void folderChangeEvent(String event, String file) {
//    JFLog.log("event:" + event + ":" + file);
    try {
      if (event.equals("CREATED")) {
        if ((file.startsWith(".")) && (!showHidden)) return;
        newFiles.add(file);
        java.awt.EventQueue.invokeLater(new Runnable() {
          public void run() {
            while (newFiles.size() > 0) {
              String fn = path + "/" + newFiles.remove(0);
              File file = new File(fn);
              if (file.isDirectory()) {
                addFolder(fn);
              } else {
                if (fn.endsWith(".desktop")) {
                  addDesktopFile(fn);
                } else {
                  addFile(fn);
                }
              }
            }
            saveConfig();
            update();
          }
        });
      } else if (event.equals("DELETED")) {
        if (/*view == VIEW_LIST ||*/ view == VIEW_DETAILS) {
          //need to do a refresh in 100ms (just removing the icon would look bad)
          synchronized(lock) {
            if (refreshTimer == null) {
              refreshTimer = new java.util.Timer();
              refreshTimer.schedule(new TimerTask() {
                public void run() {
                  synchronized(lock) {
                    java.awt.EventQueue.invokeLater(new Runnable() {
                      public void run() {
                        refreshTimer = null;
                        refresh();
                      }
                    });
                  }
                }
              }, 100);
            }
          }
        } else {
          //just remove the icon
          removeFiles.add(file);
          java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
              Component list[] = panel.getComponents();
              while (removeFiles.size() > 0) {
                String fn = removeFiles.remove(0);
                for(int a=0;a<list.length;a++) {
                  if (!(list[a] instanceof JFileIcon)) continue;
                  JFileIcon icon = (JFileIcon)list[a];
                  if (icon.entry.name.equals(fn)) {
                    panel.remove(list[a]);
                    break;
                  }
                }
                for(int a=0;a<entries.size();a++) {
                  if (entries.get(a).name.equals(fn)) {
                    entries.remove(a);
                    break;
                  }
                }
              }
              saveConfig();
              update();
            }
          });
        }
      }
    } catch (Exception e) {
      JFLog.log(e);
    }
  }

  private int monitor = -1;

  private void startFolderListener() {
    monitor = monitordir.add(path);
    monitordir.setListener(monitor, this);
  }

  private void stopFolderListener() {
    if (monitor == -1) return;
    monitordir.remove(monitor);
    monitor = -1;
  }

  public void setIconsVisible(boolean state) {
    iconsVisible = state;
    repaint();
  }

  public FileEntry createIcon(String name, String exec, String icon, String fn, boolean nextPos) {
    try {
      FileOutputStream fos = new FileOutputStream(fn);
      fos.write("[Desktop Entry]\n".getBytes());
      fos.write(("Name=" + name + "\n").getBytes());
      fos.write(("Exec=" + exec + "\n").getBytes());
      fos.write(("Icon=" + icon + "\n").getBytes());
      fos.close();
      FileEntry entry = new FileEntry();
      if (nextPos) {
        nextPosition(entry, true);
      } else {
        entry.x = mx;
        entry.y = my;
      }
      entry.file = fn;
      entry.name = name;
      entry.icon = icon;
      addIcon(entry);
      saveConfig();
      update();
      return entry;
    } catch (Exception e) {
      JFLog.log(e);
    }
    return null;
  }

  public static void runCmd(String cmd[], int numFiles) {
    JFTask task = new JFTask() {
      private String cmd[];
      private int numFiles, filesDone = 0;
      public boolean work() {
        cmd = (String[])this.getProperty("cmd");
        numFiles = cmd.length - 3;
        setTitle("File Operation");
        if (cmd[0].equals("cp")) {
          setLabel("Copying files...");
        } else {
          setLabel("Moving files...");
        }
        ShellProcess sp = new ShellProcess();
        sp.addListener(this);
        sp.run(cmd, true);
        return true;
      }
      public void shellProcessOutput(String str) {
        int lines = str.split("\n").length;
        filesDone += (lines-1);
        setProgress(filesDone * 100 / numFiles);
      }
    };
    task.setProperty("cmd", cmd);
    ProgressDialog dialog = new ProgressDialog(null, true, task);
    dialog.setAutoClose(true);
    dialog.setVisible(true);
  }

  public static int cntFiles(String fn) {
    File file = new File(fn);
    int cnt = 1;
    if (file.isDirectory()) {
      File files[] = file.listFiles();
      for(int a=0;a<files.length;a++) {
        if (files[a].isDirectory()) {
          cnt += cntFiles(files[a].getAbsolutePath());
        } else {
          cnt++;
        }
      }
    }
    return cnt;
  }

  private JFileIcon dragTarget;

  private void initDND() {
    panel.setTransferHandler(new TransferHandler() {
      public boolean canImport(TransferHandler.TransferSupport info) {
        // we only import Files
        if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
          return false;
        }

        DropLocation dl = (DropLocation) info.getDropLocation();
        Point pt = dl.getDropPoint();
        JComponent c = (JComponent)panel.getComponentAt(pt);
        if (dragTarget != null) {
          dragTarget.setSelected(false);
          dragTarget = null;
        }
        if (c != null) {
          if (c instanceof JFileIcon) {
            JFileIcon fi = (JFileIcon)c;
            if (!fi.isTransparent()) {
              fi.setSelected(true);
              dragTarget = fi;
            }
          }
        }
        return true;
      }

      public boolean importData(TransferHandler.TransferSupport info) {
        if (!info.isDrop()) {
          return false;
        }

        // Check for file flavor
        if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
          return false;
        }

        DropLocation dl = info.getDropLocation();
        Point pt = dl.getDropPoint();
        JComponent c = (JComponent)panel.getComponentAt(pt);
        if (dragTarget != null) {
          dragTarget.setSelected(false);
          dragTarget = null;
        }
        if (c != null) {
          if (c instanceof JFileIcon) {
            JFileIcon fi = (JFileIcon)c;
            if (!fi.isTransparent()) {
              fi.setSelected(true);
              dragTarget = fi;
            }
          }
        }
        String folder = path;
        if (dragTarget != null) {
          FileEntry entry = dragTarget.entry;
          if (entry.isLink) return false;
          if (entry.isDir) {
            folder += File.separatorChar;
            folder += entry.name;
          }
        }

        // Get the file(s) that are being dropped.
        Transferable t = info.getTransferable();
        java.util.List<File> data;
        try {
          data = (java.util.List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
        } catch (Exception e) {
          return false;
        }

        // Perform the actual import.
        ArrayList<String> cmd = new ArrayList<String>();
        boolean move = false;
        boolean copy = false;
        int cnt = 0;
        String fn;
        for(int a=0;a<data.size();a++) {
          switch (info.getDropAction()) {
            case COPY:
              if (move) return false;  //Can that happen?
              copy = true;
              fn = ((File)data.get(a)).getAbsolutePath();
              cmd.add(fn);
              cnt += cntFiles(fn);
              break;
            case MOVE:
              if (copy) return false;  //Can that happen?
              move = true;
              cmd.add(((File)data.get(a)).getAbsolutePath());
              cnt++;
              break;
            case LINK:
              return false;  //BUG : not supported : ???
          }
        }
        if (cmd.isEmpty()) return false;
        cmd.add(0, "-v");
        cmd.add(0, "-n");
        if (copy) {
          cmd.add(0, "-r");
          cmd.add(0, "cp");
        } else if (move) {
          cmd.add(0, "mv");
        } else {
          return false;
        }
        cmd.add(folder);
        runCmd(cmd.toArray(new String[0]), cnt);
        refresh();
        return true;
      }

      public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
      }

      protected Transferable createTransferable(JComponent c) {
        FileEntry fefiles[] = getSelected();
        ArrayList<File> files = new ArrayList<File>();

        for (int i = 0; i < fefiles.length; i++) {
          files.add(new File(fefiles[i].file));
        }
        return new Transferable() {
          private java.util.List<File> files;

          public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] {DataFlavor.javaFileListFlavor};
          }

          public boolean isDataFlavorSupported(DataFlavor df) {
            return (df == DataFlavor.javaFileListFlavor);
          }

          public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
            return files;
          }

          public Transferable init(java.util.List<File> files) {
            this.files = files;
            return this;
          }
        }.init(files);
      }

      protected void exportDone(JComponent source, Transferable data, int action) {
        refresh();
      }
    });
  }

  public void deleteFile(String filename) {
    for(int a=0;a<entries.size();a++) {
      if (entries.get(a).file.equals(filename)) {
        FileEntry entry = entries.get(a);
        File file = new File(filename);
        file.delete();
        if (file.exists()) {
          //access denied
          if (file.isDirectory()) {
            JF.showError("Error", "Can not delete folder");
          } else {
            JF.showError("Error", "Can not delete file");
          }
          return;
        }
        panel.remove(entry.button);
        if (view == VIEW_DETAILS) {
          panel.remove(entry.details_date);
          panel.remove(entry.details_time);
          panel.remove(entry.details_size);
        }
        entries.remove(a);
        saveConfig();
        update();
        return;
      }
    }
  }

  public FileEntry[] getSelected() {
    ArrayList<FileEntry> list = new ArrayList<FileEntry>();
    int cnt = panel.getComponentCount();
    for(int a=0;a<cnt;a++) {
      JComponent c = (JComponent)panel.getComponent(a);
      if (!(c instanceof JFileIcon)) continue;
      JFileIcon b = (JFileIcon)c;
      if (b.isSelected()) {
        list.add(b.entry);
      }
    }
    return list.toArray(new FileEntry[0]);
  }

  public FileEntry[] getFolders() {
    ArrayList<FileEntry> list = new ArrayList<FileEntry>();
    int cnt = panel.getComponentCount();
    for(int a=0;a<cnt;a++) {
      JComponent c = (JComponent)panel.getComponent(a);
      if (!(c instanceof JFileIcon)) continue;
      JFileIcon b = (JFileIcon)c;
      if (b.entry.isDir) {
        list.add(b.entry);
      }
    }
    return list.toArray(new FileEntry[0]);
  }

  //also returns to org position from drag
  private void setSelectedTransparent(boolean undrag) {
    int cnt = panel.getComponentCount();
    for(int a=0;a<cnt;a++) {
      JComponent c = (JComponent)panel.getComponent(a);
      if (!(c instanceof JFileIcon)) continue;
      JFileIcon b = (JFileIcon)c;
      if (b.isSelected()) {
        b.setTransparent(true);
        if (undrag) b.setLocation(b.dragX, b.dragY);
      }
    }
  }

  public void setSelectedTransparent() {
    setSelectedTransparent(false);
  }

  public void trash() {
    if (JF.isWindows()) {
      //TODO
      return;
    } else {
      FileEntry list[] = getSelected();
      if ((list == null) || (list.length == 0)) return;
      ArrayList<String> cmd = new ArrayList<String>();
      cmd.add("mv");
      cmd.add("-v");
      cmd.add("-n");
      for(int a=0;a<list.length;a++) cmd.add(list[a].file);
      cmd.add(JF.getUserPath() + "/.local/share/Trash");
      runCmd(cmd.toArray(new String[0]), list.length);
    }
  }

  public void delete() {
    FileEntry list[] = getSelected();
    if ((list == null) || (list.length == 0)) return;
    ArrayList<String> cmd = new ArrayList<String>();
    cmd.add("rm");
    cmd.add("-v");
    cmd.add("-r");
    for(int a=0;a<list.length;a++) cmd.add(list[a].file);
    runCmd(cmd.toArray(new String[0]), list.length);
  }

  public int getCount() {
    return entries.size();
  }

  public int getSelectedCount() {
    return getSelected().length;
  }

  public FileEntry[] getAll() {
    return entries.toArray(new FileEntry[0]);
  }

  public void selectFile(FileEntry entry) {
    clearSelection();
    entry.button.setSelected(true);
    entry.button.repaint();
  }

  public void setView(int newview) {
    view = newview;
    switch (view) {
      case VIEW_ICONS:
        ix = 48;
        iy = 48;
        bx = ix + 32;
        by = iy + 32;
        break;
      case VIEW_LIST:
      case VIEW_DETAILS:
        ix = 18;
        iy = 18;
        //bx,by calc later in listFiles()
        break;
    }
  }

  public void setShowHidden(boolean state) {
    showHidden = state;
  }

  public void setAutoArrange(boolean state) {
    autoArrange = state;
  }

  private boolean dragselection, dragicon;
  private int dragX, dragY;
  private int selX, selY;
  private boolean iconMoved;
//  private JComponent dragOverlay;

  public void mouseClicked(MouseEvent me) {
    JComponent c = (JComponent)me.getSource();
    if (c == panel) {
      if ((me.getButton() == me.BUTTON3) && (me.getClickCount() == 1)) {
        if (desktopMenu == null) return;
        mx = me.getX();
        my = me.getY();
        desktopMenu.show(panel, me.getX(), me.getY());
      }
      clearSelection();
      return;
    }
    if (c instanceof JFileIcon) {
      JFileIcon b = (JFileIcon)c;
      switch (me.getClickCount()) {
        case 1:
          clearSelection();
          b.setSelected(true);
          break;
        case 2:
          clearSelection();
          openEntry(b.entry);
          break;
      }
    }
  }

  public void mousePressed(MouseEvent me) {
    requestFocus();
    dragicon = false;
    dragselection = false;
    JComponent c = (JComponent)me.getSource();
    if (c instanceof JFileIcon) {
      JFileIcon b = (JFileIcon)c;
      if (!b.isSelected()) {
        clearSelection();
        b.setSelected(true);
      }
      if (me.getButton() == MouseEvent.BUTTON3) {
        if (fileMenu != null) {
          mx = me.getX();
          my = me.getY();
          fileMenu.show(b, me.getX(), me.getY());
        }
        return;
      }
      if (me.getButton() != MouseEvent.BUTTON1) return;
      dragicon = true;
      dragX = me.getXOnScreen();
      dragY = me.getYOnScreen();
      int cnt = panel.getComponentCount();
      for(int a=0;a<cnt;a++) {
        c = (JComponent)panel.getComponent(a);
        if (!(c instanceof JFileIcon)) continue;
        b = (JFileIcon)c;
        if (!b.isSelected()) continue;
        b.dragX = b.getX();
        b.dragY = b.getY();
      }
    } else {
      if (me.getButton() != MouseEvent.BUTTON1) return;
      dragX = me.getX();
      dragY = me.getY();
      selX = dragX;
      selY = dragY;
      dragselection = true;
    }
  }

  public void mouseReleased(MouseEvent me) {
    if (iconMoved) {
      if (autoArrange) {
        arrangeByGrid();
      } else {
        if (useScrolling) calcPanelSize();
      }
      saveConfig();
      iconMoved = false;
/*      if (dragOverlay != null) {
        panel.remove(dragOverlay);
        dragOverlay = null;
      }*/
    }
    dragselection = false;
    dragicon = false;
    repaint();
  }

  public void mouseEntered(MouseEvent me) {
  }

  public void mouseExited(MouseEvent me) {
    if (dragicon) {
      panel.getTransferHandler().exportAsDrag(panel, me, TransferHandler.MOVE);
      setSelectedTransparent(true);
      dragicon = false;
      repaint();
      jbusClient.call("org.jflinux.jdesktop." + System.getenv("JID"), "show", "");
/*      if (dragOverlay != null) {
        panel.remove(dragOverlay);
        dragOverlay = null;
      }*/
    }
  }

  public void mouseDragged(MouseEvent me) {
    if (dragicon) {
      iconMoved = true;
      int x = me.getXOnScreen();
      int y = me.getYOnScreen();
      Point pt1 = null;
      if (useScrolling) {
        pt1 = scroll.getLocationOnScreen();
      } else {
        pt1 = panel.getLocationOnScreen();
      }
      Point pt2 = new Point();
      pt2.x = pt1.x + getWidth();
      pt2.y = pt1.y + getHeight();
      if ((x < pt1.x) || (y < pt1.y) || (x > pt2.x) || (y > pt2.y) || (view != VIEW_ICONS)) {
        panel.getTransferHandler().exportAsDrag(panel, me, TransferHandler.MOVE);
        setSelectedTransparent(true);
        dragicon = false;
        repaint();
        jbusClient.call("org.jflinux.jdesktop." + System.getenv("JID"), "show", "");
/*        if (dragOverlay != null) {
          panel.remove(dragOverlay);
          dragOverlay = null;
        }*/
        return;
      }
      int dx = x - dragX;
      int dy = y - dragY;
      int cnt = panel.getComponentCount();
      for(int a=0;a<cnt;a++) {
        JComponent c = (JComponent)panel.getComponent(a);
        if (!(c instanceof JFileIcon)) continue;
        JFileIcon b = (JFileIcon)c;
        if (!b.isSelected()) continue;
        FileEntry entry = b.entry;
        entry.x = b.dragX + dx;
        entry.y = b.dragY + dy;
        b.setLocation(entry.x, entry.y);
        panel.setComponentZOrder(b, 0);  //or getComponentCount()
      }
/*      dragOverlay = new JComponent() {};  //
      panel.add(dragOverlay);
      panel.setComponentZOrder(dragOverlay, 0);
      dragOverlay.setBounds(0,0,getWidth(),getHeight());*/
    } else if (dragselection) {
      selX = me.getX();
      selY = me.getY();
      selectIcons();
      repaint();
    }
  }

  public void mouseMoved(MouseEvent me) {
  }

  jfuseiso iso;
  jfusezip zip;

  private void openFile(String file) {
    //file = .iso or .zip
    if (mount != null) return;  //can not open a file in a file (!recursive)
    String name;
    int idx = file.lastIndexOf("/");
    if (idx == -1) {
      name = file;
    } else {
      name = file.substring(idx+1);
    }
    String mount = JF.getUserPath() + "/.fuse/" + name;
    String fullmount = mount;
    int cnt = 1;
    while (new File(fullmount).exists()) {
      fullmount = mount + "(" + cnt++ + ")";
    }
    new File(fullmount).mkdirs();
    final String args[] = new String[] {file, fullmount, "-f"};
    if (file.toLowerCase().endsWith(".iso")) {
      iso = new jfuseiso();
      if (!iso.auth(args, null)) {
        JF.showError("Error", "Failed to open file");
        return;
      }
      new Thread() {
        public void run() {
          iso.start(args);
        }
      }.start();
    } else if (file.toLowerCase().endsWith(".zip")) {
      zip = new jfusezip();
      if (!zip.auth(args, null)) {
        JF.showError("Error", "Failed to open file");
        return;
      }
      new Thread() {
        public void run() {
          zip.start(args);
        }
      }.start();
    } else {
      JFLog.log("Error:Can not open:" + file);
      setPath(JF.getUserPath());
      return;
    }
    this.mount = fullmount;
    JF.sleep(100);  //wait for thread to start
    setPath(fullmount);
  }

  public void closeFile() {
    if (mount == null) return;
    execute(new String[] {"fusermount", "-u", mount}, true);
    new File(mount).delete();
    mount = null;
  }

  private void update() {
    repaint();
    panel.repaint();
  }
}
