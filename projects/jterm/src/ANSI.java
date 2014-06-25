/*
 * ANSI.java
 *
 * Created on August 3, 2007, 8:37 PM
 *
 * @author pquiring
 */

import java.awt.event.KeyEvent;
import java.awt.Color;

public class ANSI {

  public ANSI(Buffer buffer, boolean telnet) {
    orgForeColor = buffer.getForeColor();
    orgBackColor = buffer.getBackColor();
  }

  public final static char ESC = 0x1b;

  public final static int clrs[][] = {
    //Black  ,Red     ,Green   ,Yellow  ,Blue    ,Magenta ,Cyan    ,White
    {0x000000,0x880000,0x008800,0x888800,0x000088,0x880088,0x008888,0x888888},  //low
    {0x444444,0xff0000,0x00ff00,0xffff00,0x0000ff,0xff00ff,0x00ffff,0xffffff}  //high
  };

  public boolean altfnt = false;  //use alternate font
  public char altcode = '[';  //prefix some key codes with this
  public int high = 0;   //high intensity color (0/1)
  private int savedx = -1, savedy = -1;

  private Color orgForeColor, orgBackColor;
  private boolean telnet;

  public int numc;
  public int nums[] = new int[16];

  public char encodeChar(char ch) {
    if (!altfnt) return ch;
    char ret = altfntchars[ch];
    if (ret == 0) ret = ch;
    return ret;
  }

  public char[] encodeString(char buf[], int buflen) {
    char ret[] = new char[buflen];
    for(int a=0;a<buflen;a++) ret[a] = encodeChar(buf[a]);
    return ret;
  }

  public void keyPressed(int keyCode, int keyMods, Buffer buffer) {
    //an easy way to find codes is to run "xterm sh" and press keys - "sh" doesn't understand ANSI and echo'es them back
    String str = null;
    if (keyMods == KeyEvent.CTRL_MASK) {
      if ((keyCode >= KeyEvent.VK_A) && (keyCode <= KeyEvent.VK_Z)) {
        str = "" + (char)(keyCode - KeyEvent.VK_A + 1);
      }
      switch (keyCode) {
        case KeyEvent.VK_UP:   str = "" + ESC + "" + altcode + "1;5A"; break;
        case KeyEvent.VK_DOWN: str = "" + ESC + "" + altcode + "1;5B"; break;
        case KeyEvent.VK_RIGHT:str = "" + ESC + "" + altcode + "1;5C"; break;
        case KeyEvent.VK_LEFT: str = "" + ESC + "" + altcode + "1;5D"; break;
      }
    }
    if (keyMods == KeyEvent.ALT_MASK) {
      switch (keyCode) {
        case KeyEvent.VK_UP:   str = "" + ESC + "" + altcode + "1;3A"; break;
        case KeyEvent.VK_DOWN: str = "" + ESC + "" + altcode + "1;3B"; break;
        case KeyEvent.VK_RIGHT:str = "" + ESC + "" + altcode + "1;3C"; break;
        case KeyEvent.VK_LEFT: str = "" + ESC + "" + altcode + "1;3D"; break;
      }
    }
    if (keyMods == KeyEvent.SHIFT_MASK) {
      switch (keyCode) {
        case KeyEvent.VK_UP:   str = "" + ESC + "" + altcode + "1;2A"; break;
        case KeyEvent.VK_DOWN: str = "" + ESC + "" + altcode + "1;2B"; break;
        case KeyEvent.VK_RIGHT:str = "" + ESC + "" + altcode + "1;2C"; break;
        case KeyEvent.VK_LEFT: str = "" + ESC + "" + altcode + "1;2D"; break;

        case KeyEvent.VK_F1: str = "" + ESC + "[25~"; break;  //KEY_F13
        case KeyEvent.VK_F2: str = "" + ESC + "[26~"; break;  //KEY_F14
        case KeyEvent.VK_F3: str = "" + ESC + "[28~"; break;  //KEY_F15
        case KeyEvent.VK_F4: str = "" + ESC + "[29~"; break;  //KEY_F16
        case KeyEvent.VK_F5: str = "" + ESC + "[31~"; break;  //KEY_F17
        case KeyEvent.VK_F6: str = "" + ESC + "[32~"; break;  //KEY_F18
        case KeyEvent.VK_F7: str = "" + ESC + "[33~"; break;  //KEY_F19
        case KeyEvent.VK_F8: str = "" + ESC + "[34~"; break;  //KEY_F20
      }
    }
    if (str != null) buffer.output(str.toCharArray());
    if (keyMods != 0) return;
    switch (keyCode) {
      case KeyEvent.VK_UP:   str = "" + ESC + "" + altcode + "A"; break;
      case KeyEvent.VK_DOWN: str = "" + ESC + "" + altcode + "B"; break;
      case KeyEvent.VK_RIGHT:str = "" + ESC + "" + altcode + "C"; break;
      case KeyEvent.VK_LEFT: str = "" + ESC + "" + altcode + "D"; break;

      case KeyEvent.VK_HOME: str = "" + ESC + "[H"; break;
      case KeyEvent.VK_END: str = "" + ESC + "[F"; break;

      case KeyEvent.VK_F1:   str = "" + ESC + altcode + "P"; break;
      case KeyEvent.VK_F2:   str = "" + ESC + altcode + "Q"; break;
      case KeyEvent.VK_F3:   str = "" + ESC + altcode + "R"; break;
      case KeyEvent.VK_F4:   str = "" + ESC + altcode + "S"; break;
      case KeyEvent.VK_F5:
        if (telnet)
          str = "" + Telnet.IAC + Telnet.BRK;  //BREAK
        else
          str = "" + ESC + "[15~";
        break;
      case KeyEvent.VK_F6:   str = "" + ESC + "[17~"; break;
      case KeyEvent.VK_F7:   str = "" + ESC + "[18~"; break;
      case KeyEvent.VK_F8:   str = "" + ESC + "[19~"; break;
      case KeyEvent.VK_F9:   str = "" + ESC + "[20~"; break;
      case KeyEvent.VK_F10:  str = "" + ESC + "[21~"; break;
      case KeyEvent.VK_F11:  str = "" + ESC + "[23~"; break;
      case KeyEvent.VK_F12:  str = "" + ESC + "[24~"; break;

      case KeyEvent.VK_DELETE:     str = "" + ESC + "[3~"; break;
      case KeyEvent.VK_PAGE_UP:    str = "" + ESC + "[5~"; break;  //PREV
      case KeyEvent.VK_PAGE_DOWN:  str = "" + ESC + "[6~"; break;  //NEXT

      case KeyEvent.VK_PAUSE: str = "" + Telnet.IAC + Telnet.BRK; break;  //BREAK
    }
    if (str != null) buffer.output(str.toCharArray());
  }

  public boolean decode(char code[], int codelen, Buffer buffer) {
    int x,y;
    if (codelen < 2) return false;
    switch (code[1]) {
      case 'H':
        //??? (unix vi)
        return true;
      case 'M':
        buffer.scrollDown(1);
        return true;
      case '7':
        //save cursor pos
        savedx = buffer.getx();
        savedy = buffer.gety();
        return true;
      case '8':
        //restore cursor pos
        if (savedx == -1) return true;
        buffer.gotoPos(savedx, savedy);
        return true;
      case '#':
      case '(':
      case '[': break;
      default: return true;  //ignore unknown code
    }
    if (codelen < 3) return false;
    switch (code[1]) {
      case '#':
/*
        switch (code[2]) {
          case '3':  //large font?
          case '4':  //small font?
        }
*/
        break;
      case '(':
        switch (code[2]) {
          case '0': altfnt = true; break;
          case 'B': altfnt = false; break;
        }
        break;
      case '[':
        if (!(
          ((code[codelen-1]) >= 'A' && (code[codelen-1] <= 'Z')) ||
          ((code[codelen-1]) >= 'a' && (code[codelen-1] <= 'z')) ||
          ((code[codelen-1]) == '~') ||
          ((code[codelen-1]) == '@'))) return false;
        x = buffer.getx();
        y = buffer.gety();
        if (code[2] == '?') {
          decodeNums(code, codelen, 3);
          switch (code[codelen-1]) {
            case 'h':
              if (numc == 1 && nums[0] == 1) altcode = 'O';
              break;
            case 'l':
              if (numc == 1 && nums[0] == 1) altcode = '[';
              break;
          }
          return true;
        }
        decodeNums(code, codelen, 2);
        switch (code[codelen-1]) {
          case 'J':
            if (numc==0) nums[0]=0;
            switch (nums[0]) {
              case 0:
                //from cursor to end of screen
                for(int a=x;a<=buffer.sx;a++) buffer.setChar(a, y, ' ');  //erase partial line
                for(int b=y+1;b<=buffer.sy;b++) for(int a=1;a<=buffer.sx;a++) buffer.setChar(a, b, ' ');
                break;
              case 1:
                //from begining to cursor
                for(int a=1;a<x;a++) buffer.setChar(a, y, ' ');  //erase partial line
                for(int b=1;b<y;b++) for(int a=1;a<=buffer.sx;a++) buffer.setChar(a, b, ' ');
                break;
              case 2:
                //entire screen (reposition cursor to (1,1) too)
                buffer.clrscr();
                break;
            }
            break;
          case 'K':
            if (numc==0) nums[0]=0;
            switch (nums[0]) {
              case 0:
                //erase from cursor to end of line
                for(int a=x;a<=buffer.sx;a++) buffer.setChar(a, y, ' ');
                break;
              case 1:
                //from beginning of line to cursor
                for(int a=1;a<x;a++) buffer.setChar(a, y, ' ');
                break;
              case 2:
                //whole line
                for(int a=1;a<=buffer.sx;a++) buffer.setChar(a, y, ' ');
                break;
            }
            break;
          case 'H':
          case 'f':
            if (numc == 2 && nums[1] == 0) numc = 1;
            if (numc == 1 && nums[0] == 0) numc = 0;
            switch (numc) {
              case 2:
                buffer.gotoPos(min(buffer.sx,nums[1]),min(buffer.sy,nums[0]));
                break;
              case 1:
                buffer.gotoPos(1, min(buffer.sy,nums[0]));
                break;
              case 0:
                buffer.gotoPos(1,1);
                break;
            }
            break;
          case 'A': if (numc==0) nums[0]=1; if (y-nums[0]>1 ) buffer.gotoPos(x,y-nums[0]); else buffer.gotoPos(x,1); break;
          case 'B': if (numc==0) nums[0]=1; if (y+nums[0]<buffer.sy) buffer.gotoPos(x,y+nums[0]); else buffer.gotoPos(x,buffer.sy); break;
          case 'C': if (numc==0) nums[0]=1; if (x+nums[0]<buffer.sx) buffer.gotoPos(x+nums[0],y); else buffer.gotoPos(buffer.sx,y); break;
          case 'D': if (numc==0) nums[0]=1; if (x-nums[0]>1 ) buffer.gotoPos(x-nums[0],y); else buffer.gotoPos(1,y); break;
          case 'L': if (numc==0) nums[0]=1; buffer.scrollDown(nums[0]); break;
          case 'r':
            //define rows that scroll
            if (numc != 2) break;
            buffer.y1 = nums[0]-1;
            buffer.y2 = nums[1]-1;
            break;
          case 'm':
            //colour
            if (numc == 0) {numc = 1; nums[0] = 0;}
            for(int a=0;a<numc;a++) {
              if (nums[a] == 1) {high = 1; continue;}  //bold
              if (nums[a] == 2) {high = 0; continue;}
            }
            for(int a=0;a<numc;a++) {
              if (nums[a] == 0) {
                //normal
                high = 0;
                buffer.setBlinker(false);
                buffer.setReverse(false);
                buffer.setForeColor(orgForeColor);
                buffer.setBackColor(orgBackColor);
                continue;
              }
//              if (nums[a] == 4) {continue;}  //underline (not implemented)
              if (nums[a] == 5) {buffer.setBlinker(true); continue;}
              if (nums[a] == 7) {buffer.setReverse(true); continue;}  //reverse
//              if (nums[a] == 8) {continue;}  //invisible (not implemented) [vt300]
//              if (nums[a] == 24) {continue;}  //not underline (not implemented)
              if (nums[a] == 25) {buffer.setBlinker(false); continue;}
              if (nums[a] == 27) {buffer.setReverse(false); continue;}  //Positive (not inverse)
//              if (nums[a] == 28) {continue;}  //visible (not implemented) [vt300]
              if ((nums[a] >= 30) && (nums[a] <= 37)) {buffer.setForeColor(clrs[high][nums[a]-30]); continue;}
              if (nums[a] == 39) {buffer.setForeColor(orgForeColor); continue;}  //default (org)
              if ((nums[a] >= 40) && (nums[a] <= 47)) {buffer.setBackColor(clrs[1][nums[a]-40]); continue;}
              if (nums[a] == 49) {buffer.setBackColor(orgBackColor); continue;}  //default (org)
            }
            break;
          case 'n':
            //query cursor position "ESC[row;colR"
            if ((numc != 1) || (nums[0] != 6)) break;
            String str = "" + ESC + '[' + buffer.gety() + ';' + buffer.getx() + 'R';
            buffer.output(str.toCharArray());
            break;
          case 'P':
            //delete nums[0] chars at cursor
            if (numc == 0) {numc = 1; nums[0] = 1;}
            for(int cnt=0;cnt<nums[0];cnt++) buffer.delete();
            break;
          case '@':
            //insert nums[0] chars at cursor
            if (numc == 0) {numc = 1; nums[0] = 1;}
            for(int cnt=0;cnt<nums[0];cnt++) buffer.insert();
            break;
          case 's':
            //save cursor pos
            savedx = buffer.getx();
            savedy = buffer.gety();
            return true;
          case 'u':
            //restore cursor pos
            if (savedx == -1) return true;
            buffer.gotoPos(savedx, savedy);
            return true;
        }
    }
    return true;
  }
  private void decodeNums(char code[], int codelen, int start) {
    //decode #s encoded into code[]
    int c=start,s,e,t,m;
    numc = 0;
    code[codelen] = 'x';
    while (c < codelen) {
      while ((code[c]==';')||(code[c]==',')) {
        nums[numc++] = 0;
        c++;
      }
      if (!((code[c]>='0') && (code[c]<='9'))) break;
      s=c;
      while ((code[c]>='0') && (code[c]<='9')) {
        c++;
      }
      e=c;
      t=0;
      m=1;
      while (c!=s) {
        c--;
        t+=(code[c]-'0') * m;
        m*=10;
      }
      nums[numc++]=t;
      c=e;
      if ((code[c] == ';') || (code[c] == ',')) c++;
    }
  }
  private int min(int v1, int v2) {
    if (v1 < v2) return v1;
    return v2;
  }
  private int max(int v1, int v2) {
    if (v1 > v2) return v1;
    return v2;
  }
  private static char altfntchars[] = {
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,  //0
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,  //16
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,  //32
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,  //48
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,  //64
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,  //80
    0,0,0,0,0,0,0,0,0,0,217,191,218,192,0,0,  //96
    0,196,0,0,195,180,193,194,179,0,0,0,0,0,0,0,  //112
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,  //128
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
    0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
  };
}

