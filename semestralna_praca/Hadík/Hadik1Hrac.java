import java.util.Random;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.Timer;

/**
 * Trieda predstavuje hru hadíka s jedným hráčom.  
 * 
 * @author      Pavol Brišák
 * @version     1.0
 */
public class Hadik1Hrac extends JPanel implements KeyListener, ActionListener {
    private Smer smerHada;
    private String typMapy;
    private String typHada;
    private String menoHraca;
    
    private int velkostTelaHada;
    private int teloHada;
    private int skore;
    
    private int[] pozicieXHada; 
    private int[] pozicieYHada;
    private ImageIcon had;
    
    private int pozXJablka;
    private int pozYJablka;
    private int[] pozXJablk;
    private int[] pozYJablk;
    private int pocetJablk;
    private ImageIcon jablko;
    private int[] zlateJablka;
    
    private int[] pozXPrekazok;
    private int[] pozYPrekazok;
    private int pocetPrekazok;
    
    private int sirkaPlochy;
    private int vyskaPlochy;
    private int pocetPolicok;
    
    private int rychlost;
    
    private Timer casovac;
    private Random nahodne;
    private Random nahodnaZlateho;
    
    private boolean pohybujeSa;
    private boolean restart;
    private boolean zlateJablko;
   
    /**
      * Konštruktor triedy, v ktorom sa inicializujú atribúty.
      * Následne sa nastaví plocha a spustí sa hra.
      * 
      * @param typMapy         typ mapy
      * @param typHada         typ hada
      * @param menoHraca       meno hráča
      */
    public Hadik1Hrac(String typMapy, String typHada, String menoHraca) {
        //nastavenie rozmerov na základe vybranej mapy
        this.typMapy = typMapy;        
        if (this.typMapy.equals("ľahká")) {
            this.sirkaPlochy = 1000;
            this.vyskaPlochy = 700;
        }
        if (this.typMapy.equals("klasická")) {
            this.sirkaPlochy = 700;
            this.vyskaPlochy = 700;
        }
        if (this.typMapy.equals("ťažká")) {
            this.sirkaPlochy = 800;
            this.vyskaPlochy = 700;
        }
        this.menoHraca = menoHraca;
        this.velkostTelaHada = 25;
        this.pocetPrekazok = 8;
        //vypočíta sa počet políčok danej mapy, na ktorých sa had môže vyskytovať
        this.pocetPolicok = (this.sirkaPlochy * this.vyskaPlochy) / (this.velkostTelaHada * this.velkostTelaHada);
        //počet pozícií kde sa had môže nachádzať podľa typu mapy
        if (this.typMapy.equals("ťažká")) {
            this.pozicieXHada = new int[this.pocetPolicok - this.pocetPrekazok];
            this.pozicieYHada = new int[this.pocetPolicok - this.pocetPrekazok];
        } else {
            this.pozicieXHada = new int[this.pocetPolicok];
            this.pozicieYHada = new int[this.pocetPolicok];
        }        
        this.teloHada = 4; 
        this.typHada = typHada;
        this.nahodne = new Random();
        //náhodný smer hada
        this.smerHada = Smer.values()[this.nahodne.nextInt(Smer.values().length)];        
        this.pocetJablk = 8;
        this.pozXJablk = new int[this.pocetJablk];
        this.pozYJablk = new int[this.pocetJablk];
        this.zlateJablka = new int[this.pocetJablk];
        this.zlateJablko = false;        
        this.pozXPrekazok = new int[this.pocetPrekazok];
        this.pozYPrekazok = new int[this.pocetPrekazok];        
        this.rychlost = 160;                    
        this.restart = false;       
        this.nastavPlochu();
        this.spustiHru();
    }      
    
    /**
     * Getter smeru hada.
     * 
     * @return         návratová hodnota vráti smer hada
     */
    public Smer getSmerHada() {
        return this.smerHada;
    }
    
    /**
     * Setter smeru hada.
     * 
     * @param smer     smer hada, ktorý chceme nastaviť
     */
    public void setSmerHada(Smer smer) {
        this.smerHada = smer;
    }
    
    /**
     * Setter pohybu hada.
     * 
     * @param pohybujeSa     nastavuje sa či sa had pohybuje
     */
    public void setPohybujeSa(boolean pohybujeSa) {
        this.pohybujeSa = pohybujeSa;
    }
    
    /**
     * Metóda nastaví plochu.
     * Nastavia sa jej rozmery, farba a umiestnenie.
     * Pridá sa aj KeyListener, pomocou ktorého môžeme čitať z klávesnice.
     */
    public void nastavPlochu() {
        //vytvoria sa rozmery a nastaví sa veľkosť plochy
        Dimension rozmery = new Dimension(this.sirkaPlochy, this.vyskaPlochy);
        this.setPreferredSize(rozmery);
        //farba plochy
        this.setBackground(new Color(255, 255, 204));
        this.setFocusable(true);
        this.addKeyListener(this);
    } 
    
    /**
     * Metóda spustí hru.
     * Vytvorí sa had a začne sa pohybovať.
     * Podľa typu mapy sa vytvoria jablká a prekážky.
     * Následne sa vytvorí a zapne časovač.
     */
    public void spustiHru() {        
        this.vytvorHada();
        this.setPohybujeSa(true);        
        switch (this.typMapy) {
            case "ľahká":
                this.vytvorJablka();
                break;
            case "klasická":
                this.vytvorJablko();
                break;
            case "ťažká":
                this.vytvorPrekazky();
                this.vytvorJablko();
                break;
        }
        //vytvorenie a spustenie časovača mám požičané zo stránky
        //https://stackoverflow.com/questions/4044726/how-to-set-a-timer-in-java
        this.casovac = new Timer(this.rychlost, this);
        this.casovac.start();
    }
    
    /**
     * Metóda sa spúšťa podľa časovača.
     * Volajú sa metódy na základe toho, či sa had pohybuje.
     * Následne sa znova nakreslí celá plocha.
     * 
     * @param event       akcia ktorá sa vykoná
     */
    public void actionPerformed(ActionEvent event) {        
        if (this.pohybujeSa) {
            this.posunHada();
            this.skontrolujCiNarazil();
            this.skontrolujCiZjedolJablko();
            this.skontrolujCiVyhral();
        }
        //znova sa kreslí celá plocha 
        this.repaint(); 
    } 
    
    /**
     * Metóda vytvorí pozície hada.
     * Nastavia sa mu náhodné pozície v oddelenej časti mapy.
     */
    public void vytvorHada() {
        this.nahodne = new Random();
        int nahoda = this.nahodne.nextInt(this.sirkaPlochy / this.velkostTelaHada);
        int nahoda2 = this.nahodne.nextInt(this.vyskaPlochy / this.velkostTelaHada);
        //kontrola, či sme vytvorili pozície hlavy v správnej časti mapy
        if ((nahoda >= this.teloHada) && (nahoda <= ((this.sirkaPlochy / this.velkostTelaHada) - this.teloHada))) {
            this.pozicieXHada[0] = nahoda * this.velkostTelaHada;
        } else {
            this.vytvorHada();
        }        
        if ((nahoda2 >= this.teloHada) && (nahoda2 <= ((this.vyskaPlochy / this.velkostTelaHada) - this.teloHada))) {
            this.pozicieYHada[0] = nahoda2 * this.velkostTelaHada;
        } else {
            this.vytvorHada();
        }
        //vytváranie pozícií tela podľa smeru hada
        for (int i = 1; i < this.teloHada; i++) {
            switch (this.getSmerHada()) {
                case VPRAVO:
                    this.pozicieXHada[i] = this.pozicieXHada[i - 1] - this.velkostTelaHada;
                    this.pozicieYHada[i] = this.pozicieYHada[i - 1];
                    break;
                case VLAVO:
                    this.pozicieXHada[i] = this.pozicieXHada[i - 1] + this.velkostTelaHada;
                    this.pozicieYHada[i] = this.pozicieYHada[i - 1];
                    break;
                case HORE:
                    this.pozicieYHada[i] = this.pozicieYHada[i - 1] + this.velkostTelaHada;
                    this.pozicieXHada[i] = this.pozicieXHada[i - 1];
                    break;
                case DOLE:
                    this.pozicieYHada[i] = this.pozicieYHada[i - 1] - this.velkostTelaHada;
                    this.pozicieXHada[i] = this.pozicieXHada[i - 1];
                    break;
            }
        }  
    }
    
    /**
     * Metóda vytvorí pozície prekážky.
     * Nastavia sa jej náhodné pozície v oddelenej časti mapy.
     * Skontroluje sa jej vytvorená pozícia.
     */
    public void vytvorPrekazky() {
        for (int i = 0; i < this.pocetPrekazok; i++) {
            this.nahodne = new Random();
            int nahoda = this.nahodne.nextInt(this.sirkaPlochy / this.velkostTelaHada);
            int nahoda2 = this.nahodne.nextInt(this.vyskaPlochy / this.velkostTelaHada);
            //náahodne vytvorené pozície prekážky v oddelenej časti mapy
            if ((nahoda < this.teloHada) || (nahoda > ((this.sirkaPlochy / this.velkostTelaHada) - this.teloHada))) {
                this.pozXPrekazok[i] = nahoda * this.velkostTelaHada;
                this.pozYPrekazok[i] = nahoda2 * this.velkostTelaHada;
            } else if ((nahoda2 < this.teloHada) || (nahoda2 > ((this.vyskaPlochy / this.velkostTelaHada) - this.teloHada))) {
                this.pozYPrekazok[i] = nahoda2 * this.velkostTelaHada;
                this.pozXPrekazok[i] = nahoda * this.velkostTelaHada;
            }
            //kontrola, či sa vytvorená prekážka nenachádza na nesprávnej pozícii
            if (!(this.skontrolujPoziciuPrekazky(this.pozXPrekazok[i], this.pozYPrekazok[i]))) {
                this.vytvorPrekazky();
            }
        }
        //kontrola, či sa nevytvorili prekážky s rovnakými pozíciami 
        for (int i = 0; i < this.pocetPrekazok; i++) { 
            for (int j = i + 1 ; j < this.pocetPrekazok; j++) { 
                if ((this.pozXPrekazok[i] == (this.pozXPrekazok[j])) && (this.pozYPrekazok[i] == (this.pozYPrekazok[j]))) {
                    this.vytvorPrekazky();
                }
            } 
        }        
    }
    
    /**
     * Metóda skontroluje pozíciu prekážky.
     * 
     * @param pozXPrekazky       pozícia prekážky na osi x
     * @param pozYPrekazky       pozícia prekážky na osi y
     * @return                   návratová hodnota vráti, či kontrola bola úspešná
     */
    public boolean skontrolujPoziciuPrekazky(int pozXPrekazky, int pozYPrekazky) {
        boolean kontrola = true;
        //kontrola, či sa prekážka nenachádza na nejakej pozícii hada
        for (int i = 0; i < this.teloHada; i++) {
            if ((pozXPrekazky == this.pozicieXHada[i]) && (pozYPrekazky == this.pozicieYHada[i])) {
                kontrola = false;
            }
        }
        return kontrola;
    } 
    
    /**
     * Metóda vytvorí pozície jablka.
     * Nastavia sa mu náhodné pozície.
     * Skontroluje sa jej vytvorená pozícia.
     * Následne sa zistí, či dané vytvorené jablko je zlaté.
     */ 
    public void vytvorJablko() {
        if (!(this.skontrolujCiVyhral())) {
            this.nahodne = new Random();
            this.pozXJablka = this.nahodne.nextInt(this.sirkaPlochy / this.velkostTelaHada) * this.velkostTelaHada;
            this.pozYJablka = this.nahodne.nextInt(this.vyskaPlochy / this.velkostTelaHada) * this.velkostTelaHada;
            if (!(this.skontrolujPoziciuJablka(this.pozXJablka, this.pozYJablka))) {
                this.vytvorJablko();
            }
            //zistí sa, či vytvorené jablko nie je zlaté
            if (this.nahodaZlatehoJablka() < 0.1) {
                this.zlateJablko = true;
            }
        }      
    } 
    
    /**
     * Metóda vytvára konkrétne jablko z poľa jabĺk.
     * Skontroluje sa jeho pozícia.
     * Následne sa zistí, či dané vytvorené jablko je zlaté.
     * 
     * @param xJablko       poloha pozície x jablka v poli
     * @param yJablko       poloha pozície y jablka v poli
     */
    public void vytvorJablko(int xJablko, int yJablko) {
        if (!(this.skontrolujCiVyhral())) {
            //pokiaľ pre jablko už nie je miesto, vytvorí sa mimo mapu
            if ((this.pocetPolicok - this.teloHada) < this.pocetJablk) {
                this.pozXJablk[xJablko] = -1;
                this.pozYJablk[yJablko] = -1;
            } else {
                this.nahodne = new Random();
                this.pozXJablk[xJablko] = this.nahodne.nextInt(this.sirkaPlochy / this.velkostTelaHada) * this.velkostTelaHada;
                this.pozYJablk[yJablko] = this.nahodne.nextInt(this.vyskaPlochy / this.velkostTelaHada) * this.velkostTelaHada;
                if (!(this.skontrolujPoziciuJablka(this.pozXJablk[xJablko], this.pozYJablk[yJablko]))) {
                    this.vytvorJablko(xJablko, yJablko);
                }
                //zistí sa, či vytvorené jablko nie je zlaté
                if (this.nahodaZlatehoJablka() < 0.1) {
                    this.zlateJablka[xJablko] = 1;
                }
            }
        }      
    }
    
    /**
     * Metóda vytvorí pozície jabĺk.
     * Nastavia sa im náhodné pozície.
     * Skontroluje sa ich vytvorená pozícia.
     * Následne sa zistí, či dané vytvorené jablko je zlaté.
     */
    public void vytvorJablka() {
        if (!(this.skontrolujCiVyhral())) {
            //vytvoria sa náhodné pozície jabĺk a skontrolujú sa ich pozície
            for (int i = 0; i < this.pocetJablk; i++) {
                this.nahodne = new Random();
                this.pozXJablk[i] = this.nahodne.nextInt(this.sirkaPlochy / this.velkostTelaHada) * this.velkostTelaHada;
                this.pozYJablk[i] = this.nahodne.nextInt(this.vyskaPlochy / this.velkostTelaHada) * this.velkostTelaHada;
                if (!(this.skontrolujPoziciuJablka(this.pozXJablk[i], this.pozYJablk[i]))) {
                    this.vytvorJablka();
                }
                //kontrola, či vytvorené jablko nie je zlaté
                if (this.nahodaZlatehoJablka() < 0.1) {
                    this.zlateJablka[i] = 1;
                }
            }
            //kontrola, či sa nevytvorili jablká s rovnakými pozíciami
            for (int i = 0; i < this.pocetJablk; i++) { 
                for (int j = i + 1 ; j < this.pocetJablk; j++) { 
                    if ((this.pozXJablk[i] == (this.pozXJablk[j])) && (this.pozYJablk[i] == (this.pozYJablk[j]))) {
                        this.vytvorJablka();
                        break;
                    }
                } 
            }
        } 
    }
    
    /**
     * Metóda skontroluje pozíciu jablka.
     * 
     * @param xPoziciaJablka       pozícia jablka na osi x
     * @param yPoziciaJablka       pozícia jablka na osi y
     * @return                     návratová hodnota vráti, či kontrola bola úspešná
     */
    public boolean skontrolujPoziciuJablka(int xPoziciaJablka, int yPoziciaJablka) {
        boolean kontrola = true;
        //kontrola, či sa jablko nevytvorilo na nejakej pozícii hada 
        for (int i = 0; i < this.teloHada; i++) {
            if ((xPoziciaJablka == this.pozicieXHada[i]) && (yPoziciaJablka == this.pozicieYHada[i])) {
                kontrola = false;
                break;
            }
        }
        //kontrola, či sa jablko nevytvorilo na nejakej pozícii prekážky
        if (this.typMapy.equals("ťažká")) {
            for (int i = 0; i < this.pocetPrekazok; i++) {
                if ((xPoziciaJablka == this.pozXPrekazok[i]) && (yPoziciaJablka == this.pozYPrekazok[i])) {
                    kontrola = false;
                    break;
                }
            }
        }
        //kontrola, či sa jablko nevytvorilo mimo mapy
        if (xPoziciaJablka < 0) {
            kontrola = false;
        }
        if (xPoziciaJablka > this.sirkaPlochy) {
            kontrola = false;
        }
        if (yPoziciaJablka < 0) {
            kontrola = false;
        }
        if (yPoziciaJablka > this.vyskaPlochy) {
            kontrola = false;
        }
        return kontrola;
    }
    
    /**
     * Metóda vytvorí náhodné desatinné číslo.
     * 
     * @return         návratová hodnota vráti vytvorené číslo
     */
    public double nahodaZlatehoJablka() {
        this.nahodnaZlateho = new Random();
        return this.nahodnaZlateho.nextDouble();
    }
    
    /**
     * Metóda kreslí hada na jeho pozíciách.
     * Skontroluje typ hada a nakreslí hada.
     * 
     * @param grafika        využitie grafiky
     * @param typHada        typ hada, ktorý sme si zvolili
     */
    public void nakresliHada(Graphics grafika, String typHada) {
        switch (typHada) {
            //na základe typu hada sa mení farba a nakreslí sa útvar na danej pozícii
            case "modry":
                for (int i = 0; i < this.teloHada; i++) {
                    if (i == 0) {
                        //nastaví sa farba
                        grafika.setColor(new Color(0, 191, 255));
                    } else {                    
                        grafika.setColor(new Color(135, 206, 235));
                    }
                    //nakreslí sa obdĺžnik a vyplní sa zvolenou farbou
                    grafika.drawRect(this.pozicieXHada[i], this.pozicieYHada[i], this.velkostTelaHada, this.velkostTelaHada);
                    grafika.fillRect(this.pozicieXHada[i], this.pozicieYHada[i], this.velkostTelaHada, this.velkostTelaHada);
                }
                break;                
            case "zeleny":
                for (int i = 0; i < this.teloHada; i++) {
                    if (i == 0) {
                        grafika.setColor(new Color(0, 255, 1));
                    } else {
                        grafika.setColor(new Color(0, 102, 0));
                    }
                    grafika.drawRect(this.pozicieXHada[i], this.pozicieYHada[i], this.velkostTelaHada, this.velkostTelaHada);
                    grafika.fillRect(this.pozicieXHada[i], this.pozicieYHada[i], this.velkostTelaHada, this.velkostTelaHada);
                }
                break;
            case "ruzovy":
                for (int i = 0; i < this.teloHada; i++) {
                    if (i == 0) {
                        grafika.setColor(new Color(255, 20, 147));
                    } else {
                        grafika.setColor(new Color(255, 105, 180));
                    }
                    grafika.drawRect(this.pozicieXHada[i], this.pozicieYHada[i], this.velkostTelaHada, this.velkostTelaHada);
                    grafika.fillRect(this.pozicieXHada[i], this.pozicieYHada[i], this.velkostTelaHada, this.velkostTelaHada);
                }
                break;
            //farebný had sa kreslí pomocou obrázkov zo súboru
            case "farebny":
                for (int i = 0; i < this.teloHada; i++) {
                    if (i == 0) {
                        switch (this.getSmerHada()) {
                            //podľa smeru farebného hada sa nakreslí jeho hlava
                            case VPRAVO:
                                //načíta sa obrázok a upravia sa jeho rozmery podľa tela hada
                                this.had = new ImageIcon("obrazky/hlava vpravo.png");
                                Image obraz1 = this.had.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had = new ImageIcon(obraz1);
                                break;
                            case VLAVO:
                                this.had = new ImageIcon("obrazky/hlava vlavo.png");
                                Image obraz2 = this.had.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had = new ImageIcon(obraz2);
                                break;
                            case HORE:
                                this.had = new ImageIcon("obrazky/hlava hore.png");
                                Image obraz3 = this.had.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had = new ImageIcon(obraz3);
                                break;
                            case DOLE:
                                this.had = new ImageIcon("obrazky/hlava dole.png");
                                Image obraz4 = this.had.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had = new ImageIcon(obraz4);
                                break;
                        }
                        this.had.paintIcon(this, grafika, this.pozicieXHada[i], this.pozicieYHada[i]);
                    } else if (i == (this.teloHada - 1)) {
                        //na základe pozície tela pred chvostom, sa kreslí chvost farebného hada
                        if (this.pozicieXHada[i] == this.pozicieXHada[i - 1]) {
                            if (this.pozicieYHada[i] < this.pozicieYHada[i - 1]) {
                                this.had = new ImageIcon("obrazky/chvost dole.png");
                            } else {
                                this.had = new ImageIcon("obrazky/chvost hore.png");
                            }
                        } else if (this.pozicieYHada[i] == this.pozicieYHada[i - 1]) {
                            if (this.pozicieXHada[i] < this.pozicieXHada[i - 1]) {
                                this.had = new ImageIcon("obrazky/chvost vpravo.png");
                            } else {
                                this.had = new ImageIcon("obrazky/chvost vlavo.png");
                            }
                        }
                        Image obraz4 = this.had.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                        this.had = new ImageIcon(obraz4);
                        this.had.paintIcon(this, grafika, this.pozicieXHada[i], this.pozicieYHada[i]);
                    } else {
                        //kreslenie tela farebného hada
                        this.had = new ImageIcon("obrazky/telo.png");
                        Image obraz = this.had.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                        this.had = new ImageIcon(obraz);
                        this.had.paintIcon(this, grafika, this.pozicieXHada[i], this.pozicieYHada[i]);
                    }           
                }
                break;            
        }        
    }
    
    /**
     * Metóda kreslí jablko na jeho pozíciách.
     * Skontroluje typ mapy a nakreslí vhodný počet jabĺk.
     * 
     * @param grafika        využitie grafiky
     */
    public void nakresliJablko(Graphics grafika) {
        //v ľahkej mape sa vytvára daný počet jabĺk, v ostaných typoch sa vytvára iba jedno jablko
        if (this.typMapy.equals("ľahká")) {
            for (int i = 0; i < this.pocetJablk; i++) {
                if ((this.pozXJablk[i] >= 0) && (this.pozYJablk[i] >= 0)) {
                    //kontrola, či dané jablko je zlaté
                    if (this.zlateJablka[i] == 1) {
                        //načíta sa obrázok a upravia sa jeho rozmery podľa tela hada
                        this.jablko = new ImageIcon("obrazky/zlate jablko.png");
                    } else {
                        this.jablko = new ImageIcon("obrazky/jablko.png");
                    }
                } 
                Image obraz = this.jablko.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                this.jablko = new ImageIcon(obraz);
                this.jablko.paintIcon(this, grafika, this.pozXJablk[i], this.pozYJablk[i]);
            }
        } else {
            //kontrola, či dané jablko je zlaté
            if (this.zlateJablko) {
                //načíta sa obrázok a upravia sa jeho rozmery podľa tela hada
                this.jablko = new ImageIcon("obrazky/zlate jablko.png");
            } else {
                this.jablko = new ImageIcon("obrazky/jablko.png");
            }
            Image obraz = this.jablko.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
            this.jablko = new ImageIcon(obraz);
            this.jablko.paintIcon(this, grafika, this.pozXJablka, this.pozYJablka);
        }
    }
    
    /**
     * Metóda kreslí prekážky na ich pozíciách.
     * 
     * @param grafika        využitie grafiky
     */
    public void nakresliPrekazky(Graphics grafika) {
        for (int i = 0; i < this.pocetPrekazok; i++) {
            //nastaví si farbu, ktorou vyplní vytvorený obdĺžnik
            grafika.setColor(Color.black);
            grafika.drawRect(this.pozXPrekazok[i], this.pozYPrekazok[i], this.velkostTelaHada, this.velkostTelaHada);
            grafika.fillRect(this.pozXPrekazok[i], this.pozYPrekazok[i], this.velkostTelaHada, this.velkostTelaHada);
        }
    } 
    
    /**
     * Metóda kreslí celú plochu.
     * Volá metódy kreslenia a kontroluje typ mapy a či sa had pohybuje.
     * Taktiež skontroluje, či hráč vyhral alebo prehral.
     * 
     * @param grafika        využitie grafiky
     */
    public void paintComponent(Graphics grafika) {
        //túto správu mám požičanú zo stránky 
        //https://docs.oracle.com/javase/tutorial/uiswing/painting/problems.html
        super.paintComponent(grafika);
        //kreslí, ak sa had pohybuje a podľa typu mapy kreslí dané útvary
        if (this.pohybujeSa) {
            if (this.typMapy.equals("ťažká")) {
                this.nakresliPrekazky(grafika);
            }
            this.nakresliJablko(grafika);
            this.nakresliHada(grafika, this.typHada);                        
        } else {
            //ak hráč vyhral, hra sa končí a spustí sa daná metóda
            if (this.skontrolujCiVyhral()) {
                this.vyhral(grafika);
            } else {
                this.prehral(grafika);  
            }            
        }        
    }
    
    /**
     * Metóda posúva hada.
     * Podľa smeru hada sa posunú pozície hlavy hada.
     * Telo hada sa posúva podľa pozície predošlého tela.
     */
    public void posunHada() {
        for (int i = this.teloHada; i >= 0; i--) {
            if (!(i == 0)) {
                //zmena pozície tela hada na pozíciu tela predchádzajúceho
                this.pozicieXHada[i] = this.pozicieXHada[i - 1]; 
                this.pozicieYHada[i] = this.pozicieYHada[i - 1];
            } else {
                //podľa smeru hada sa posunú pozície hlavy hada
                switch (this.getSmerHada()) {
                    case VPRAVO:
                        this.pozicieXHada[i] = this.pozicieXHada[i] + this.velkostTelaHada;
                        break;
                    case VLAVO:
                        this.pozicieXHada[i] = this.pozicieXHada[i] - this.velkostTelaHada;
                        break;
                    case HORE:
                        this.pozicieYHada[i] = this.pozicieYHada[i] - this.velkostTelaHada;
                        break;
                    case DOLE:
                        this.pozicieYHada[i] = this.pozicieYHada[i] + this.velkostTelaHada;
                        break;
                }   
            }          
        }        
    }
    
    /**
     * Metóda kontroluje, či had nezjedol jablko.
     * Podľa typu mapy sa kontroluje, či sa hlava hada nenachádza na pozícií jablka.
     * Následne sa hráčovi zvýši skóre a telo hada sa predĺži.
     * Zjedené jablko sa znovu vytvorí.
     */
    public void skontrolujCiZjedolJablko() {
        //v ľahkej mape sa kontrolujú všetky jablká
        if (this.typMapy.equals("ľahká")) {
            //kontrola, či sa hlava hada nenachádza na pozícií jablka z poľa jabĺk
            for (int i = 0; i < this.pocetJablk; i++) {
                if ((this.pozicieXHada[0] == this.pozXJablk[i]) && (this.pozicieYHada[0] == this.pozYJablk[i])) {
                    //ak je zlaté, skóre sa zvýši o 5
                    if (this.zlateJablka[i] == 1) {
                        this.skore = this.skore + 5;
                    } else {
                        this.skore++;
                    }
                    this.teloHada++;
                    //vytvorí sa zjedené jablko a zmení sa hodnota zlatého jablka
                    this.zlateJablka[i] = 0; 
                    this.vytvorJablko(i, i);
                }
            }
        } else {
            //kontrola, či sa hlava hada nenachádza na pozícií jablka
            if ((this.pozicieXHada[0] == this.pozXJablka) && (this.pozicieYHada[0] == this.pozYJablka)) {
                //ak je zlaté, skóre sa zvýši o 5
                if (this.zlateJablko) {
                    this.skore = this.skore + 5;
                } else {
                    this.skore++;
                }
                this.teloHada++;
                //vytvorí sa zjedené jablko a zmení sa hodnota zlatého jablka
                this.zlateJablko = false; 
                this.vytvorJablko();
            }
        }
    }
    
    /**
     * Metóda kontroluje, či had nenarazil.
     * Podľa typu mapy sa kontroluje, či sa hlava hada nenachádza na neprípustnej pozícii.
     * Následne sa nastaví pohyb hada.
     */
    public void skontrolujCiNarazil() {
        //kontrola, či sa hlava hada nenachádza na nejakej pozícii tela hada 
        for (int i = this.teloHada; i > 0; i--) {
            if ((this.pozicieXHada[0] == this.pozicieXHada[i]) && (this.pozicieYHada[0] == this.pozicieYHada[i])) {
                this.setPohybujeSa(false);
            }
        }
        //kontrola, či sa hlava hada nenachádza na nejakej pozícii prekážky
        if (this.typMapy.equals("ťažká")) {
            for (int i = 0; i < this.pocetPrekazok; i++) {
                if ((this.pozicieXHada[0] == this.pozXPrekazok[i]) && (this.pozicieYHada[0] == this.pozYPrekazok[i])) {
                    this.setPohybujeSa(false);
                }
            }
        }
        //posunutie pozície hlavy hada, ak had prešiel cez stenu
        if (this.typMapy.equals("ľahká")) {
            if (this.pozicieXHada[0] < 0) {
                this.pozicieXHada[0] = this.sirkaPlochy - this.velkostTelaHada;
            }
            if (this.pozicieXHada[0] == this.sirkaPlochy) {
                this.pozicieXHada[0] = 0;
            }
            if (this.pozicieYHada[0] < 0) {
                this.pozicieYHada[0] = this.vyskaPlochy - this.velkostTelaHada;
            }
            if (this.pozicieYHada[0] == this.vyskaPlochy) {
                this.pozicieYHada[0] = 0;
            }
        } else {
            //pokiaľ had narazil, prestane sa pohybovať
            if (this.pozicieXHada[0] < 0) {
                this.setPohybujeSa(false);
            }
            if (this.pozicieXHada[0] == this.sirkaPlochy) {
                this.setPohybujeSa(false);
            }
            if (this.pozicieYHada[0] < 0) {
                this.setPohybujeSa(false);
            }
            if (this.pozicieYHada[0] == this.vyskaPlochy) {
                this.setPohybujeSa(false);
            }
        }
        //kontrola, či hráč nevyhral
        if (this.skontrolujCiVyhral()) {
            this.setPohybujeSa(false);
        }
        //ak sa had prestal pohybovať, časovač sa zastaví
        if (!this.pohybujeSa) {
            this.casovac.stop();
        } 
    }
    
    /**
     * Metóda kreslí údaje o hre, ak hráč prehral.
     * Zapne sa reštart a nakreslí sa text na vhodnej pozícii.
     * Následne sa údaje uložia do súboru.
     * 
     * @param grafika        využitie grafiky
     */
    public void prehral(Graphics grafika) {
        this.restart = true;
        //nastaví sa font a nakreslí sa text
        Font mojFont = new Font("TimesRoman", 10, this.velkostTelaHada);
        grafika.setColor(Color.black);
        grafika.setFont(mojFont);
        grafika.drawString("Prehrali ste :(", this.sirkaPlochy / 4, this.vyskaPlochy / 3);
        grafika.drawString("Skóre: " + this.skore, this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 30);
        grafika.drawString("Vaše údaje boli uložené do súboru.", this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 60);
        grafika.drawString("Pre reštart stlačte medzerník!", this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 90);
        //údaje sa zapíšu do súboru
        try {
            this.zapisDoSuboru("skore.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Metóda kreslí údaje o hre, ak hráč vyhral.
     * Zapne sa reštart a nakreslí sa text na vhodnej pozícii.
     * Následne sa údaje uložia do súboru.
     * 
     * @param grafika        využitie grafiky
     */
    public void vyhral(Graphics grafika) {
        this.restart = true;
        //nastaví sa font a nakreslí sa text
        Font mojFont = new Font("TimesRoman", 10, this.velkostTelaHada);
        grafika.setColor(Color.black);
        grafika.setFont(mojFont);
        grafika.drawString("Gratulujeme!!! Vyhrali ste!!!", this.sirkaPlochy / 4, this.vyskaPlochy / 3);
        grafika.drawString("Skóre: " + this.skore, this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 30);
        grafika.drawString("Vaše údaje boli uložené do súboru.", this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 60);
        grafika.drawString("Pre reštart stlačte medzerník!", this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 90);
        //údaje sa zapíšu do súboru
        try {
            this.zapisDoSuboru("skore.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Metóda zistí, či hráč vyhral.
     * Kontroluje sa, či sa telo hada nepredĺžilo najviac ako sa mu dá.
     * 
     * @return         návratová hodnota vráti, či hráč vyhral
     */
    public boolean skontrolujCiVyhral() {
        boolean kontrola = false;
        if (this.typMapy.equals("ťažká")) {
            if (this.teloHada == (this.pocetPolicok - this.pocetPrekazok)) {
                kontrola = true;
            }
        } else {
            if (this.teloHada == this.pocetPolicok) {
                kontrola = true;
            }
        }        
        return kontrola;
    }
    
    /**
     * Metóda zisťuje, ktorú klávesu sme stlačili.
     * Podľa smeru hada sa mu nastaví vhodný smer.
     * 
     * @param event      stlačenie klávesy
     */
    public void keyPressed(KeyEvent klavesa) {
        switch (klavesa.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                //kontrola, či nový smer je možné nastaviť
                if (this.getSmerHada() != Smer.VLAVO) {
                    this.setSmerHada(Smer.VPRAVO);
                }
                break;
            case KeyEvent.VK_LEFT:
                if (this.getSmerHada() != Smer.VPRAVO) {
                    this.setSmerHada(Smer.VLAVO);
                }
                break;            
            case KeyEvent.VK_UP:
                if (this.getSmerHada() != Smer.DOLE) {
                    this.setSmerHada(Smer.HORE);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (this.getSmerHada() != Smer.HORE) {
                    this.setSmerHada(Smer.DOLE);
                }
                break;
            case KeyEvent.VK_SPACE:
                //ak je reštart zapnutý, spustí sa hra odznova
                if (this.restart) {                                        
                    this.restartHry();                    
                } 
                break;
        }
    }
    
    /**
     * Metóda spustí hru odznova.
     * Atribúty sa nastavia na pôvodnú hodnotu.
     * Následne sa spustí hra odznova.
     */
    public void restartHry() {
        //atribúty sa nastavia na pôvodnú hodnotu
        this.teloHada = 4;
        this.skore = 0;
        this.smerHada = Smer.values()[new Random().nextInt(Smer.values().length)];
        this.zlateJablko = false;
        this.zlateJablka = new int[this.pocetJablk];
        this.restart = false;
        //znovu spustenie hry
        this.spustiHru();
    }
    
    /**
     * Metóda zapisuje údaje do súboru.
     * Otvorí sa súbor a vloží sa doň nový text.
     * Zapíše sa deň, kedy sme hru hrali, typ mapy, skóre a meno hráča.
     * 
     * @param nazovSuboru      názov súboru, do ktorého chceme text vložiť
     */
    public void zapisDoSuboru(String nazovSuboru) throws IOException {
        PrintWriter zapisovac = new PrintWriter(new FileWriter(nazovSuboru, true));
        zapisovac.println("********************");
        //zápis času mám požičaný zo stránky
        //https://www.javatpoint.com/java-get-current-date
        zapisovac.println("Čas: " + java.time.LocalDate.now());
        zapisovac.println("Mapa: " + this.typMapy);
        zapisovac.println("Skóre: ");
        zapisovac.println(this.menoHraca + " " + this.skore);
        zapisovac.close();
    }
    
    /**
     * Metóda zisťuje, ktorú klávesu sme pustili.
     * 
     * @param event      pustenie klávesy
     */
    public void keyReleased(KeyEvent klavesa) {
        //túto metódu nevyužívam, ale KeyListener si ju vyžaduje
    }
    
    /**
     * Metóda zisťuje, ktorú klávesu sme napísali.
     * 
     * @param event      napísanie klávesy
     */
    public void keyTyped(KeyEvent klavesa) {
        //túto metódu nevyužívam, ale KeyListener si ju vyžaduje
    } 
}