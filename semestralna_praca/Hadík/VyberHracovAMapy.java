import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Image;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

/**
 * Trieda vytvára okno, na ktorom si hráč zvolí typ mapy, počet hráčov a typ hada.
 * Stlačením tlačidla Pokračovať si hráč zvolí meno, pod ktorým bude hrať.
 * Následne sa spustí trieda HraciaPlocha.
 * 
 * @author      Pavol Brišák
 * @version     1.0
 */
public class VyberHracovAMapy extends JFrame implements MouseListener {
    private JLabel oknoHrac1;    
    private JLabel oknoHrac2;
    private JLabel tlacitko;
    private JLabel mapa1;
    private JLabel mapa2;
    private JLabel mapa3;
    private JLabel vitajte;
    private JLabel pozadie;
    private JLabel farbaHada;
    private JLabel farbaHada2;
    private JLabel sipka;
    private JLabel sipka2;
    private int hrubkaOkraja;
    // atribút obrázok ktorý sa zobrazuje v paneloch
    private ImageIcon obrazok;
    private int ktoryObrazok;
    private int ktoryObrazok2;
    // arraylist stringov pre zobrazovanie obrázkov
    private ArrayList<String> obrazkyHada1;
    private ArrayList<String> typHada1;
    private ArrayList<String> obrazkyHada2;
    private ArrayList<String> typHada2;
    
    private HraciaPlocha hraciaPlocha;
    
    private int pocetHracov;
    private String typMapy;
    private String menoHraca1;
    private String menoHraca2;
    
    /**
      * Konštruktor triedy, v ktorom sa inicializujú atribúty.
      * Nastaví si plocha, pozadie a okná.
      */
    public VyberHracovAMapy() {
        // inicializácia atribútov
        this.pocetHracov = 0;
        this.typMapy = "";
        this.ktoryObrazok = 0;
        this.ktoryObrazok2 = 0;        
        this.hrubkaOkraja = 5;
        this.obrazkyHada1 = new ArrayList<String>(List.of("obrazky/zeleny.png", "obrazky/modry.png", "obrazky/ruzovy.png", "obrazky/farebny.png"));
        this.typHada1 = new ArrayList<String>(List.of("zeleny", "modry", "ruzovy", "farebny"));
        this.obrazkyHada2 = new ArrayList<String>(List.of("obrazky/oranzovy.png", "obrazky/fialovy.png", "obrazky/cierny.png", "obrazky/farebny2.png"));
        this.typHada2 = new ArrayList<String>(List.of("oranzovy", "fialovy", "cierny", "farebny2"));
        
        this.nastavPlochu();   
        this.nastavPozadie();
        this.vyberFarbyHad1();
        this.vyberFarbyHad2();
        //vytváranie panelov obsahujúcich text ktorý je v strede
        this.oknoHrac1 = new JLabel("1 HRÁČ", SwingConstants.CENTER);
        this.oknoHrac2 = new JLabel("2 HRÁČI", SwingConstants.CENTER);
        this.tlacitko = new JLabel("Pokračovať", SwingConstants.CENTER);
        this.mapa1 = new JLabel("ĽAHKÁ", SwingConstants.CENTER);
        this.mapa2 = new JLabel("KLASICKÁ", SwingConstants.CENTER);
        this.mapa3 = new JLabel("ŤAŽKÁ", SwingConstants.CENTER);        
        
        this.vitajte = new JLabel("<html>Zvoľte si počet hráčov a obtiažnosť hry. <br>Následne kliknite na tlačítko pokračovať.<br>Hráč 1 hrá šípkami. <br>Hráč 2 hrá klávesami WASD</html>");
        //každý panel sa vytvorí a nastaví sa mu text
        this.vitajte.setBounds(10, 10, 480, 100);
        this.vitajte = this.nastavText(this.vitajte);
                
        this.oknoHrac1.setBounds(35, 125, 100, 100);
        this.oknoHrac1 = this.nastavOkno(this.oknoHrac1);
        
        this.oknoHrac2.setBounds(150, 125, 100, 100);
        this.oknoHrac2 = this.nastavOkno(this.oknoHrac2);
        
        this.tlacitko.setBounds(150, 600, 150, 50);
        this.tlacitko = this.nastavOkno(this.tlacitko);
                
        this.mapa1.setBounds(35, 250, 100, 100);
        this.mapa1 = this.nastavOkno(this.mapa1);
                
        this.mapa2.setBounds(150, 250, 100, 100);
        this.mapa2 = this.nastavOkno(this.mapa2);
               
        this.mapa3.setBounds(265, 250, 100, 100);
        this.mapa3 = this.nastavOkno(this.mapa3);
        
        //pridávanie panelov na plochu
        this.add(this.oknoHrac1);
        this.add(this.oknoHrac2);
        this.add(this.tlacitko);
        this.add(this.mapa1);
        this.add(this.mapa2);
        this.add(this.mapa3);
        this.add(this.vitajte);
        this.add(this.pozadie);        
        this.setVisible(true);
    }
    
    /**
     * Metóda nastaví plochu.
     * Nastavia sa jej rozmery, názov a umiestnenie.
     */
    public void nastavPlochu() {
        this.pack();
        this.setSize(500, 700);
        this.setResizable(false);
        this.setTitle("Výber počtu hráčov a obtiažnosti");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);    
    }
    
    /**
     * Metóda nastaví okná, ktoré sa pridajú na plochu.
     * Nastaví sa farba a okraje.
     * Oknu sa pridá MouseListener.
     * 
     * @param okno     okno ktoré chceme nastaviť
     * @return         návratová hodnota vráti nastavené okno
     */
    public JLabel nastavOkno(JLabel okno) {
        okno.setBackground(Color.white);
        okno.setOpaque(true);
        okno.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja));
        okno.addMouseListener(this);        
        okno.setFont(new java.awt.Font("Verdana", 1, 15));
        return okno;
    }
    
    /**
     * Metóda nastaví oknu text, ktorý sa v ňom zobrazuje.
     * Nastaví sa font a umiestnenie textu.
     * 
     * @param okno     okno ktorému chceme nastaviť text
     * @return         návratová hodnota vráti nastavené okno
     */
    public JLabel nastavText(JLabel okno) {
        //text sa nastaví vľavo hore
        okno.setVerticalAlignment(JLabel.TOP);
        okno.setFont(new java.awt.Font("Verdana", 1, 15));
        return okno;
    }
    
    /**
     * Metóda nastaví pozadie.
     * Nastaví sa jeho obrázok a rozmery.
     */
    public void nastavPozadie() {
        this.obrazok = new ImageIcon("obrazky/obrazok.png");
        Image obraz = this.obrazok.getImage().getScaledInstance(500, 700, java.awt.Image.SCALE_DEFAULT);
        this.obrazok = new ImageIcon(obraz);
        this.pozadie = new JLabel(this.obrazok);
        this.zobraz(this.pozadie);
        this.pozadie.setSize(500, 700);
    }
    
    /**
     * Metóda nastaví okno, pomocou ktorého si hráč dokáže zvoliť typ hada.
     * Nastaví sa mu obrázok, okraje a rozmery.
     * Obdobne sa nastaví aj šípka, na ktorú hráč môže klikať.
     */
    public void vyberFarbyHad1() {        
        this.obrazok = new ImageIcon(this.obrazkyHada1.get(this.ktoryObrazok));
        Image obraz = this.obrazok.getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_DEFAULT);
        this.obrazok = new ImageIcon(obraz);
        this.farbaHada = new JLabel(this.obrazok);
        this.farbaHada.setBounds(150, 390, 150, 150);
        this.farbaHada = this.nastavOkno(this.farbaHada);        
        
        this.obrazok = new ImageIcon("obrazky/sipka1.png");
        Image obraz2 = this.obrazok.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_DEFAULT);
        this.obrazok = new ImageIcon(obraz2);
        this.sipka = new JLabel(this.obrazok);
        this.sipka.setBounds(100, 440, 50, 50);
        this.sipka.addMouseListener(this);
        
        //vytvorené okná zobrazíme a vložíme na plochu
        this.add(this.farbaHada);
        this.add(this.sipka);
        this.zobraz(this.farbaHada);
        this.zobraz(this.sipka);
    }
    
    /**
     * Metóda nastaví okno, pomocou ktorého si druhý hráč dokáže zvoliť typ druhého hada.
     * Nastaví sa mu obrázok, okraje a rozmery.
     * Obdobne sa nastaví aj druhá šípka, na ktorú hráč môže klikať.
     */
    public void vyberFarbyHad2() {
        this.obrazok = new ImageIcon(this.obrazkyHada2.get(this.ktoryObrazok));
        Image obraz = this.obrazok.getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_DEFAULT);
        this.obrazok = new ImageIcon(obraz);
        this.farbaHada2 = new JLabel(this.obrazok);
        this.farbaHada2.setBounds(250, 390, 150, 150);
        this.farbaHada2 = this.nastavOkno(this.farbaHada2);        
        
        this.obrazok = new ImageIcon("obrazky/sipka2.png");
        Image obraz2 = this.obrazok.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_DEFAULT);
        this.obrazok = new ImageIcon(obraz2);
        this.sipka2 = new JLabel(this.obrazok);
        this.sipka2.setBounds(400, 440, 50, 50);
        this.sipka2.addMouseListener(this);
        
        //vytvorené okná zostanú zatiaľ skryté a vložíme ich na plochu
        this.skry(this.farbaHada2);
        this.skry(this.sipka2);
        this.add(this.farbaHada2);
        this.add(this.sipka2);
    }
    
    /**
     * Metóda zmení polohu okna.
     * 
     * @param okno        okno ktorému chceme zmeniť polohu
     * @param xPoloha     poloha na osi x, na ktorú chceme okno posunúť 
     * @param yPoloha     poloha na osi y, na ktorú chceme okno posunúť
     */
    public void zmenPolohu(JLabel okno, int xPoloha, int yPoloha) {
        this.skry(okno);
        okno.setBounds(xPoloha, yPoloha, okno.getWidth(), okno.getWidth());
        this.zobraz(okno);
    }
    
    /**
     * Metóda zmení obrázok hada, ktorý si volíme.
     * 
     * @param ktoryObrazok     pozícia obrázka zo zoznamu 
     */
    public void zmenObrazokHada1(int ktoryObrazok) {
        String cestKObrazku = this.obrazkyHada1.get(ktoryObrazok);
        this.obrazok = new ImageIcon(cestKObrazku);
        Image obraz = this.obrazok.getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_DEFAULT);
        this.obrazok = new ImageIcon(obraz);
        this.farbaHada.setIcon(this.obrazok);
    }
    
    /**
     * Metóda zmení obrázok druhého hada, ktorý si volíme.
     * 
     * @param ktoryObrazok     pozícia obrázka zo zoznamu 
     */
    public void zmenObrazokHada2(int ktoryObrazok) {
        String cestKObrazku = this.obrazkyHada2.get(ktoryObrazok);
        this.obrazok = new ImageIcon(cestKObrazku);
        Image obraz = this.obrazok.getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_DEFAULT);
        this.obrazok = new ImageIcon(obraz);
        this.farbaHada2.setIcon(this.obrazok);
    }
    
    /**
     * Metóda zisťuje na ktoré okno sme klikli.
     * Následne sa mení farba okna na ktoré sme klikli.
     * Nastaví sa typ mapy a počet hráčov.
     * Podľa zvoleného počtu hráčov sa zobrazí výber typu hada.
     * Po kliknutí na tlačidlo Pokračovať si hráči zvolia meno, pod ktorým chcú hrať.
     * 
     * @param event     kliknutie na dané okno 
     */
    public void mouseClicked(MouseEvent event) {
        if ((event.getComponent() == this.oknoHrac1)) {
            this.oknoHrac1.setBorder(BorderFactory.createLineBorder(new Color(57, 165, 95), this.hrubkaOkraja));
            this.oknoHrac2.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja));
            this.setPocetHracov(1);
            this.zmenPolohu(this.farbaHada, 150, this.farbaHada.getY());
            this.zmenPolohu(this.sipka, 100, this.sipka.getY());
            this.skry(this.farbaHada2);
            this.skry(this.sipka2);
        } else if (event.getComponent() == this.oknoHrac2) {
            this.oknoHrac2.setBorder(BorderFactory.createLineBorder(new Color(57, 165, 95), this.hrubkaOkraja));
            this.oknoHrac1.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja));
            this.setPocetHracov(2);
            this.zmenPolohu(this.farbaHada, 70, this.farbaHada.getY());
            this.zmenPolohu(this.sipka, 20, this.sipka.getY());
            this.zobraz(this.farbaHada2);
            this.zobraz(this.sipka2);
        } else if (event.getComponent() == this.mapa1) {
            this.mapa1.setBorder(BorderFactory.createLineBorder(new Color(57, 165, 95), this.hrubkaOkraja));
            this.mapa2.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja));
            this.mapa3.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja));
            this.setMapa("ľahká"); 
        } else if (event.getComponent() == this.mapa2) {
            this.mapa2.setBorder(BorderFactory.createLineBorder(new Color(57, 165, 95), this.hrubkaOkraja));
            this.mapa1.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja));
            this.mapa3.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja));
            this.setMapa("klasická");
        } else if (event.getComponent() == this.mapa3) {
            this.mapa3.setBorder(BorderFactory.createLineBorder(new Color(57, 165, 95), this.hrubkaOkraja));
            this.mapa1.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja));
            this.mapa2.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja));
            this.setMapa("ťažká");
        } else if (event.getComponent() == this.sipka) {            
            this.ktoryObrazok++;
            if (this.ktoryObrazok == this.obrazkyHada1.size()) {
                this.ktoryObrazok = 0;           
            }
            this.zmenObrazokHada1(this.ktoryObrazok);
        } else if (event.getComponent() == this.sipka2) {            
            this.ktoryObrazok2++;
            if (this.ktoryObrazok2 == this.obrazkyHada2.size()) {
                this.ktoryObrazok2 = 0;           
            }
            this.zmenObrazokHada2(this.ktoryObrazok2);
        } else if (event.getComponent() == this.tlacitko) {
            //na tlačítko Pokračovať sa dá kliknúť iba ak sme si zvolili počet hráčov a typ mapy
            if ((this.getPocetHracov() != 0) && (!this.getMapa().equals(""))) {
                if (this.getPocetHracov() == 1) {
                    //z dialogového okna čítame mená hráčov
                    this.menoHraca1 = JOptionPane.showInputDialog(null, "Meno hráča 1");
                } else {
                    this.menoHraca1 = JOptionPane.showInputDialog(null, "Meno hráča 1");
                    this.menoHraca2 = JOptionPane.showInputDialog(null, "Meno hráča 2");
                }
                //otvorená plocha sa uzavrie a spustí sa trieda HraciaPlocha
                this.hraciaPlocha = new HraciaPlocha(this.getPocetHracov(), this.getMapa(), this.getTypHada(), this.getTypHada2(), this.getMenoHraca1(), this.getMenoHraca2());
                this.dispose();
            }
        }
    }
    
    /**
     * Metóda zisťuje do ktorého okna sme myškou vošli.
     * Následne sa mení farba daného okna.
     * 
     * @param event     vojdenie myšky do daného okna 
     */
    public void mouseEntered(MouseEvent event) {
        if ((event.getComponent() == this.oknoHrac1)) {
            if (!(this.pocetHracov == 1)) {
                this.oknoHrac1.setBorder(BorderFactory.createLineBorder(new Color(57, 165, 95), this.hrubkaOkraja)); 
            }
        } else if (event.getComponent() == this.oknoHrac2) {
            if (!(this.pocetHracov == 2)) {
                this.oknoHrac2.setBorder(BorderFactory.createLineBorder(new Color(57, 165, 95), this.hrubkaOkraja)); 
            }                
        } else if (event.getComponent() == this.mapa1) {
            if (!(this.typMapy.equals("ľahká"))) {
                this.mapa1.setBorder(BorderFactory.createLineBorder(new Color(57, 165, 95), this.hrubkaOkraja)); 
            } 
        } else if (event.getComponent() == this.mapa2) {
            if (!(this.typMapy.equals("klasická"))) {
                this.mapa2.setBorder(BorderFactory.createLineBorder(new Color(57, 165, 95), this.hrubkaOkraja)); 
            }
        } else if (event.getComponent() == this.mapa3) {
            if (!(this.typMapy.equals("ťažká"))) {
                this.mapa3.setBorder(BorderFactory.createLineBorder(new Color(57, 165, 95), this.hrubkaOkraja)); 
            }
        } else if (event.getComponent() == this.tlacitko) { 
            this.tlacitko.setBorder(BorderFactory.createLineBorder(new Color(57, 165, 95), this.hrubkaOkraja));
        } else if (event.getComponent() == this.sipka) { 
            this.farbaHada.setBorder(BorderFactory.createLineBorder(new Color(57, 165, 95), this.hrubkaOkraja));
        } else if (event.getComponent() == this.sipka2) { 
            this.farbaHada2.setBorder(BorderFactory.createLineBorder(new Color(57, 165, 95), this.hrubkaOkraja));
        }
    }
    
    /**
     * Metóda zisťuje z ktorého okna sme myškou vyšli.
     * Následne sa mení farba daného okna.
     * 
     * @param event     vystúpenie myšky z daného okna 
     */
    public void mouseExited(MouseEvent event) {
        if ((event.getComponent() == this.oknoHrac1)) {
            if (!(this.pocetHracov == 1)) {
                this.oknoHrac1.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja)); 
            }
        } else if (event.getComponent() == this.oknoHrac2) {
            if (!(this.pocetHracov == 2)) {
                this.oknoHrac2.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja)); 
            }                
        } else if (event.getComponent() == this.mapa1) {
            if (!(this.typMapy.equals("ľahká"))) {
                this.mapa1.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja)); 
            } 
        } else if (event.getComponent() == this.mapa2) {
            if (!(this.typMapy.equals("klasická"))) {
                this.mapa2.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja)); 
            }
        } else if (event.getComponent() == this.mapa3) {
            if (!(this.typMapy.equals("ťažká"))) {
                this.mapa3.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja)); 
            }
        } else if (event.getComponent() == this.tlacitko) { 
            this.tlacitko.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja));
        } else if (event.getComponent() == this.sipka) {
            this.farbaHada.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja));
        } else if (event.getComponent() == this.sipka2) { 
            this.farbaHada2.setBorder(BorderFactory.createLineBorder(Color.black, this.hrubkaOkraja));
        }        
    }
    
    /**
     * Metóda zobrazuje zvolené okno.
     * 
     * @param okno     okno, ktoré chceme zobraziť 
     */
    public void zobraz(JLabel okno) {
        okno.setVisible(true);
    }
    
    /**
     * Metóda skryje zvolené okno.
     * 
     * @param okno     okno, ktoré chceme skryť
     */
    public void skry(JLabel okno) {
        okno.setVisible(false);
    }
    
    /**
     * Getter typu hada.
     * 
     * @return         návratová hodnota vráti text ktorý vyjadruje typ hada
     */
    public String getTypHada() {
        return this.typHada1.get(this.ktoryObrazok);
    }
    
    /**
     * Getter typu druhého hada.
     * 
     * @return         návratová hodnota vráti text ktorý vyjadruje typ druhého hada
     */
    public String getTypHada2() {
        return this.typHada2.get(this.ktoryObrazok2);
    }
    
    /**
     * Getter počtu hráčov.
     * 
     * @return         návratová hodnota vráti počet hráčov
     */
    public int getPocetHracov() {
        return this.pocetHracov;
    }
    
    /**
     * Getter typu mapy.
     * 
     * @return         návratová hodnota vráti typ mapy
     */
    public String getMapa() {
        return this.typMapy;
    }
    
    /**
     * Getter mena hráča.
     * 
     * @return         návratová hodnota vráti meno hráča
     */
    public String getMenoHraca1() {
        return this.menoHraca1;
    }
    
    /**
     * Getter mena druhého hráča.
     * 
     * @return         návratová hodnota vráti meno druhého hráča
     */
    public String getMenoHraca2() {
        return this.menoHraca2;
    }
    
    /**
     * Setter počtu hráčov.
     * 
     * @param pocetHracov     počet hráčov ktorý chceme nastaviť
     */
    public void setPocetHracov(int pocetHracov) {
        this.pocetHracov = pocetHracov;
    }
    
    /**
     * Setter typu mapy.
     * 
     * @param mapa      typ mapy ktorý chceme nastaviť
     */
    public void setMapa(String mapa) {
        this.typMapy = mapa;
    }
    
    /**
     * Metóda zisťuje, ktoré okno stláčame.
     * 
     * @param event      stlačenie okna
     */
    public void mousePressed(MouseEvent event) {
       //túto metódu nevyužívam, ale MouseListener si ju vyžaduje 
    }
    
    /**
     * Metóda zisťuje, ktoré okno sme pustili.
     * 
     * @param event      pustenie okna
     */
    public void mouseReleased(MouseEvent event) {
        //túto metódu nevyužívam, ale MouseListener si ju vyžaduje
    }
}