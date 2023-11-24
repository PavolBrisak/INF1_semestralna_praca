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
 * Trieda predstavuje hru hadíka s dvomi hráčmi.  
 * 
 * @author      Pavol Brišák
 * @version     1.0
 */
public class Hadik2Hraci extends JPanel implements KeyListener, ActionListener {
    private Smer smerHada;
    private Smer smerHada2;
    private String typMapy;
    private String typHada;
    private String typHada2;
    private String menoHraca;
    private String menoHraca2;
    
    private int sirkaPlochy;
    private int vyskaPlochy;
    private int pocetPolicok;
    
    private int velkostTelaHada;
    private int teloHada;
    private int skoreHada;
      
    private int[] pozicieXHada; 
    private int[] pozicieYHada;
    private ImageIcon had;
    
    private int skoreHada2;
    private int teloHada2;
      
    private int[] pozicieXHada2; 
    private int[] pozicieYHada2;
    private ImageIcon had2;
    
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
    
    private int rychlost;
    
    private Timer casovac;
    private Random nahodne;
    private Random nahodnaZlateho;
    
    private boolean pohybujeSaHad;
    private boolean pohybujeSaHad2;
    private boolean restart;
    private boolean zlateJablko;
    
    /**
      * Konštruktor triedy, v ktorom sa inicializujú atribúty.
      * Následne sa nastaví plocha a spustí sa hra.
      * 
      * @param typMapy         typ mapy
      * @param typHada         typ hada
      * @param typHada2        typ druhého hada
      * @param menoHraca       meno hráča
      * @param menoHraca2      meno druhého hráča
      */
    public Hadik2Hraci(String typMapy, String typHada, String typHada2, String menoHraca, String menoHraca2) {
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
        this.menoHraca2 = menoHraca2;
        this.teloHada = 4;
        this.teloHada2 = 4;
        this.velkostTelaHada = 25;
        //vypočíta sa počet políčok danej mapy, na ktorých sa had môže vyskytovať
        this.pocetPolicok = (this.sirkaPlochy * this.vyskaPlochy) / (this.velkostTelaHada * this.velkostTelaHada);
        this.pocetPrekazok = 8;
        //počet pozícií kde sa hady môžu nachádzať podľa typu mapy
        if (this.typMapy.equals("ťažká")) {
            this.pozicieXHada = new int[this.pocetPolicok - this.pocetPrekazok];
            this.pozicieYHada = new int[this.pocetPolicok - this.pocetPrekazok];
            this.pozicieXHada2 = new int[this.pocetPolicok - this.pocetPrekazok];
            this.pozicieYHada2 = new int[this.pocetPolicok - this.pocetPrekazok];
        } else {
            this.pozicieXHada = new int[this.pocetPolicok];
            this.pozicieYHada = new int[this.pocetPolicok];
            this.pozicieXHada2 = new int[this.pocetPolicok];
            this.pozicieYHada2 = new int[this.pocetPolicok];
        }    
        this.typHada = typHada;
        this.typHada2 = typHada2;
        this.pocetJablk = 8;
        this.pozXJablk = new int[this.pocetJablk];
        this.pozYJablk = new int[this.pocetJablk];
        this.zlateJablka = new int[this.pocetJablk];
        this.pozXPrekazok = new int[this.pocetPrekazok];
        this.pozYPrekazok = new int[this.pocetPrekazok];
        this.rychlost = 160;
        //náhodný smer hadov
        this.smerHada = Smer.values()[new Random().nextInt(Smer.values().length)];
        this.smerHada2 = Smer.values()[new Random().nextInt(Smer.values().length)]; 
        this.restart = false;
        this.zlateJablko = false;       
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
     * Getter smeru druhého hada.
     * 
     * @return         návratová hodnota vráti smer druhého hada
     */
    public Smer getSmerHada2() {
        return this.smerHada2;
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
     * Setter smeru druhého hada.
     * 
     * @param smer     smer druhého hada, ktorý chceme nastaviť
     */
    public void setSmerHada2(Smer smer) {
        this.smerHada2 = smer;
    }
    
    /**
     * Setter pohybu hada.
     * 
     * @param pohybujeSa     nastavuje sa či sa had pohybuje
     */
    public void setPohybujeSa(boolean pohybujeSa) {
        this.pohybujeSaHad = pohybujeSa;
    }
    
    /**
     * Setter pohybu druhého hada.
     * 
     * @param pohybujeSa     nastavuje sa či sa druhý had pohybuje
     */
    public void setPohybujeSaHad2(boolean pohybujeSa) {
        this.pohybujeSaHad2 = pohybujeSa;
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
        //špeciálne nastavenie farby pre konkrétny typ hada
        if (this.typHada2.equals("cierny")) {
            this.setBackground(new Color(0, 0, 0));
        } else {
            this.setBackground(new Color(255, 255, 204));
        }        
        this.setFocusable(true);
        this.addKeyListener(this);
    } 
    
    /**
     * Metóda spustí hru.
     * Vytvoria sa oba hady a začnú sa pohybovať.
     * Podľa typu mapy sa vytvoria jablká a prekážky.
     * Následne sa vytvorí a zapne časovač.
     */
    public void spustiHru() {        
        this.vytvorHada();
        this.vytvorHada2();
        this.setPohybujeSa(true);
        this.setPohybujeSaHad2(true);
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
        if (this.pohybujeSaHad) {
            this.posunHadov();
            this.skontrolujCiNarazili();
            this.skontrolujCiZjedliJablko();
            this.skontrolujCiNiektoVyhral();
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
     * Metóda vytvorí pozície druhého hada.
     * Nastavia sa mu náhodné pozície v oddelenej časti mapy.
     */
    public void vytvorHada2() {
        this.nahodne = new Random();
        int nahoda = this.nahodne.nextInt(this.sirkaPlochy / this.velkostTelaHada);
        //kontrola, či sme vytvorili pozície hlavy v správnej časti mapy
        if ((nahoda >= this.teloHada2) && (nahoda <= ((this.sirkaPlochy / this.velkostTelaHada) - this.teloHada2))) {
            this.pozicieXHada2[0] = nahoda * this.velkostTelaHada;
        } else { 
            this.vytvorHada2();
        }
        int nahoda2 = this.nahodne.nextInt(this.vyskaPlochy / this.velkostTelaHada);
        if ((nahoda2 >= this.teloHada2) && (nahoda2 <= ((this.vyskaPlochy / this.velkostTelaHada) - this.teloHada2))) {
            this.pozicieYHada2[0] = nahoda2 * this.velkostTelaHada;
        } else {
            this.vytvorHada2();
        }
        //vytváranie pozícií tela podľa smeru hada
        for (int i = 1; i < this.teloHada2; i++) {
            switch (this.getSmerHada2()) {
                case VPRAVO:
                    this.pozicieXHada2[i] = this.pozicieXHada2[i - 1] - this.velkostTelaHada;
                    this.pozicieYHada2[i] = this.pozicieYHada2[i - 1];
                    break;
                case VLAVO:
                    this.pozicieXHada2[i] = this.pozicieXHada2[i - 1] + this.velkostTelaHada;
                    this.pozicieYHada2[i] = this.pozicieYHada2[i - 1];
                    break;
                case HORE:
                    this.pozicieYHada2[i] = this.pozicieYHada2[i - 1] + this.velkostTelaHada;
                    this.pozicieXHada2[i] = this.pozicieXHada2[i - 1];
                    break;
                case DOLE:
                    this.pozicieYHada2[i] = this.pozicieYHada2[i - 1] - this.velkostTelaHada;
                    this.pozicieXHada2[i] = this.pozicieXHada2[i - 1];
                    break;
            }
        }
        //skontrolujeme, či dané pozície druhého hada sa vytvorili správne
        if (!(this.skontrolujPozicieHada2())) {
            this.vytvorHada2();
        }
    }
    
    /**
     * Metóda skontroluje pozície druhého hada.
     * 
     * @return          návratová hodnota vráti, či bola kontrola úspešná
     */
    public boolean skontrolujPozicieHada2() {
        boolean kontrola = true;
        //kontrola, či pozície druhého hada sa nevytvorili na nejakej pozícii prvého už vytvoreného hada
        for (int i = 0; i < this.teloHada2; i++) {
            for (int j = 0; j < this.teloHada; j++) {
                if ((this.pozicieXHada2[i] == this.pozicieXHada[j]) && (this.pozicieYHada2[i] == this.pozicieYHada[j])) {
                    kontrola = false;
                    break;
                }
            }
        }
        //kontrola, či pozície druhého hada nie sú vzdialené od pozícií prvého hada menej než je dvoj-násobok velkosti tela hada
        for (int i = 0; i < this.teloHada; i++) {
            if ((this.pozicieXHada2[0] < (this.pozicieXHada[i] + (2 * this.velkostTelaHada))) && (this.pozicieXHada2[0] > (this.pozicieXHada[i] - (2 * this.velkostTelaHada)))) {
                kontrola = false;
                break;
            }
            if ((this.pozicieYHada2[0] < (this.pozicieYHada[i] + (2 * this.velkostTelaHada))) && (this.pozicieYHada2[0] > (this.pozicieYHada[i] - (2 * this.velkostTelaHada)))) {
                kontrola = false;
                break;
            }
        }
        return kontrola;
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
        //kontrola, či sa prekážka nenachádza na nejakej pozícii druhého hada
        for (int i = 0; i < this.teloHada; i++) {
            if ((pozXPrekazky == this.pozicieXHada2[i]) && (pozYPrekazky == this.pozicieYHada2[i])) {
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
        if (!(this.skontrolujCiNiektoVyhral())) {
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
        if (!(this.skontrolujCiNiektoVyhral())) {
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
        if (!(this.skontrolujCiNiektoVyhral())) {
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
        //kontrola, či sa jablko nevytvorilo na nejakej pozícii druhého hada
        for (int i = 0; i < this.teloHada2; i++) {
            if ((xPoziciaJablka == this.pozicieXHada2[i]) && (yPoziciaJablka == this.pozicieYHada2[i])) {
                kontrola = false;
                break;
            }
        }
        //kontrola, či sa jablko nevytvorilo na nejakej pozícii prekážky
        if (this.typMapy.equals("tazka")) {
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
                            case HORE:
                                this.had = new ImageIcon("obrazky/hlava hore.png");
                                Image obraz1 = this.had.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had = new ImageIcon(obraz1);
                                break;
                            case DOLE:
                                this.had = new ImageIcon("obrazky/hlava dole.png");
                                Image obraz2 = this.had.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had = new ImageIcon(obraz2);
                                break;
                            case VLAVO:
                                this.had = new ImageIcon("obrazky/hlava vlavo.png");
                                Image obraz3 = this.had.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had = new ImageIcon(obraz3);
                                break;
                            case VPRAVO:
                                this.had = new ImageIcon("obrazky/hlava vpravo.png");
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
                                Image obraz4 = this.had.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had = new ImageIcon(obraz4);
                            } else {
                                this.had = new ImageIcon("obrazky/chvost hore.png");
                                Image obraz4 = this.had.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had = new ImageIcon(obraz4);
                            }
                        } else if (this.pozicieYHada[i] == this.pozicieYHada[i - 1]) {
                            if (this.pozicieXHada[i] < this.pozicieXHada[i - 1]) {
                                this.had = new ImageIcon("obrazky/chvost vpravo.png");
                                Image obraz4 = this.had.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had = new ImageIcon(obraz4);
                            } else {
                                this.had = new ImageIcon("obrazky/chvost vlavo.png");
                                Image obraz4 = this.had.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had = new ImageIcon(obraz4);
                            }
                        }
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
     * Metóda kreslí druhého hada na jeho pozíciách.
     * Skontroluje typ hada a nakreslí druhého hada.
     * 
     * @param grafika        využitie grafiky
     * @param typHada        typ hada, ktorý sme si zvolili
     */
    public void nakresliHada2(Graphics grafika, String typHada) {
        switch (typHada) {
            //na základe typu druhého hada sa mení farba a nakreslí sa útvar na danej pozícii
            case "fialovy":
                for (int i = 0; i < this.teloHada2; i++) {
                    if (i == 0) {
                        //nastaví sa farba
                        grafika.setColor(new Color(186, 85, 211));
                    } else {
                        grafika.setColor(new Color(153, 50, 204));
                    }
                    //nakreslí sa obdĺžnik a vyplní sa zvolenou farbou
                    grafika.drawRect(this.pozicieXHada2[i], this.pozicieYHada2[i], this.velkostTelaHada, this.velkostTelaHada);
                    grafika.fillRect(this.pozicieXHada2[i], this.pozicieYHada2[i], this.velkostTelaHada, this.velkostTelaHada);
                }
                break;
            case "oranzovy" :
                for (int i = 0; i < this.teloHada2; i++) {
                    if (i == 0) {
                        grafika.setColor(new Color(255, 102, 0));
                    } else {
                        grafika.setColor(new Color(255, 153, 0));
                    }
                    grafika.drawRect(this.pozicieXHada2[i], this.pozicieYHada2[i], this.velkostTelaHada, this.velkostTelaHada);
                    grafika.fillRect(this.pozicieXHada2[i], this.pozicieYHada2[i], this.velkostTelaHada, this.velkostTelaHada);
                }
                break;
            //špeciálny čierny had sa kreslí pomocou obrázkov zo súboru
            case "cierny" :
                for (int i = 0; i < this.teloHada2; i++) {
                    //kreslí sa hlava čierneho hada podľa jeho smeru
                    if (i == 0) {
                        switch (this.getSmerHada2()) {
                            //načíta sa obrázok a upravia sa jeho rozmery podľa tela hada
                            case VPRAVO:
                                this.had2 = new ImageIcon("obrazky/ciernahlava vpravo.png");
                                Image obraz1 = this.had2.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had2 = new ImageIcon(obraz1);
                                break;
                            case VLAVO:
                                this.had2 = new ImageIcon("obrazky/ciernahlava vlavo.png");
                                Image obraz2 = this.had2.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had2 = new ImageIcon(obraz2);
                                break;
                            case HORE:
                                this.had2 = new ImageIcon("obrazky/ciernahlava hore.png");
                                Image obraz3 = this.had2.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had2 = new ImageIcon(obraz3);
                                break;
                            case DOLE:
                                this.had2 = new ImageIcon("obrazky/ciernahlava dole.png");
                                Image obraz4 = this.had2.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had2 = new ImageIcon(obraz4);
                                break;
                        }
                        this.had2.paintIcon(this, grafika, this.pozicieXHada2[i], this.pozicieYHada2[i]);
                        //kreslí sa chvost čierneho hada
                    } else if (i == (this.teloHada2 - 1)) {
                        //kontrola pozícií chvosta a tela pred chvostom čiernho hada
                        if (this.pozicieXHada2[i] == this.pozicieXHada2[i - 1]) {
                            if (this.pozicieYHada2[i] < this.pozicieYHada2[i - 1]) {
                                this.had2 = new ImageIcon("obrazky/ciernahlava hore.png");
                            } else {
                                this.had2 = new ImageIcon("obrazky/ciernahlava dole.png");
                            }
                        } else if (this.pozicieYHada2[i] == this.pozicieYHada2[i - 1]) {
                            if (this.pozicieXHada2[i] < this.pozicieXHada2[i - 1]) {
                                this.had2 = new ImageIcon("obrazky/ciernahlava vlavo.png");
                            } else {
                                this.had2 = new ImageIcon("obrazky/ciernahlava vpravo.png");
                            }
                        }
                        //kontroly chvosta a tela pred chvostom čierneho hada, ak had prešiel cez stenu
                        if ((this.pozicieXHada2[i] == 0) && (this.pozicieXHada2[i - 1] == (this.sirkaPlochy - this.velkostTelaHada))) {
                            this.had2 = new ImageIcon("obrazky/ciernahlava vpravo.png"); 
                        }
                        if ((this.pozicieXHada2[i] == (this.sirkaPlochy - this.velkostTelaHada)) && (this.pozicieXHada2[i - 1] == 0)) {
                            this.had2 = new ImageIcon("obrazky/ciernahlava vlavo.png");
                        }
                        if ((this.pozicieYHada2[i] == 0) && (this.pozicieYHada2[i - 1] == (this.vyskaPlochy - this.velkostTelaHada))) {
                            this.had2 = new ImageIcon("obrazky/ciernahlava dole.png"); 
                        }
                        if ((this.pozicieYHada2[i] == (this.vyskaPlochy - this.velkostTelaHada)) && (this.pozicieYHada2[i - 1] == 0)) {
                            this.had2 = new ImageIcon("obrazky/ciernahlava hore.png");
                        }
                        Image obraz = this.had2.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                        this.had2 = new ImageIcon(obraz);
                        this.had2.paintIcon(this, grafika, this.pozicieXHada2[i], this.pozicieYHada2[i]);
                    } else {
                        //kreslí sa telo hada a podľa pozícií tela pred ním a za ním sa upravuje ohyb tela
                        if (this.pozicieXHada2[i] == this.pozicieXHada2[i - 1]) {
                            if (this.pozicieXHada2[i] == this.pozicieXHada2[i + 1]) {
                                this.had2 = new ImageIcon("obrazky/ciernetelo zvisle.png");
                            } else if (this.pozicieXHada2[i] < this.pozicieXHada2[i + 1]) {
                                if (this.pozicieYHada2[i] < this.pozicieYHada2[i - 1]) {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb3.png");
                                } else {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb2.png");
                                }
                            } else {
                                if (this.pozicieYHada2[i] < this.pozicieYHada2[i - 1]) {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb4.png");
                                } else {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb1.png");
                                }
                            }
                        } else if (this.pozicieYHada2[i] == this.pozicieYHada2[i - 1]) {
                            if (this.pozicieYHada2[i] == this.pozicieYHada2[i + 1]) {
                                this.had2 = new ImageIcon("obrazky/ciernetelo vodorovne.png");
                            } else if (this.pozicieYHada2[i] < this.pozicieYHada2[i + 1]) {
                                if (this.pozicieXHada2[i] < this.pozicieXHada2[i - 1]) {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb3.png");
                                } else {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb4.png");
                                }
                            } else {
                                if (this.pozicieXHada2[i] < this.pozicieXHada2[i - 1]) {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb2.png");
                                } else {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb1.png");                                   
                                }
                            }
                        }
                        //kontrola, či sa telo hada, ktoré kreslíme nenachádza na ľavej strane plochy
                        //pričom telo pred ním musí byť na opačnej strane plochy a telo za ním musí byť na rovnakej pozícií
                        if ((this.pozicieXHada2[i] == 0) && (this.pozicieXHada2[i - 1] == (this.sirkaPlochy - this.velkostTelaHada))) {
                            if (this.pozicieXHada2[i + 1] == 0) {
                                if (this.pozicieYHada2[i] < this.pozicieYHada2[i + 1]) {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb4.png");
                                } else {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb1.png");
                                }                                
                            }
                        }
                        //kontrola, či sa telo hada, ktoré kreslíme nenachádza na pravej strane plochy
                        //pričom telo pred ním musí byť na opačnej strane plochy a telo za ním musí byť na rovnakej pozícií
                        if ((this.pozicieXHada2[i] == (this.sirkaPlochy - this.velkostTelaHada)) && (this.pozicieXHada2[i - 1] == 0)) {
                            if (this.pozicieXHada2[i + 1] == (this.sirkaPlochy - this.velkostTelaHada)) {
                                if (this.pozicieYHada2[i] < this.pozicieYHada2[i + 1]) {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb3.png");
                                } else {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb2.png");
                                }                                
                            }
                        }
                        //kontrola, či sa telo hada, ktoré kreslíme nenachádza na hornej strane plochy
                        //pričom telo pred ním musí byť na opačnej strane plochy a telo za ním musí byť na rovnakej pozícií
                        if ((this.pozicieYHada2[i] == 0) && (this.pozicieYHada2[i - 1] == (this.vyskaPlochy - this.velkostTelaHada))) {
                            if (this.pozicieYHada2[i + 1] == 0) {
                                if (this.pozicieXHada2[i] < this.pozicieXHada2[i + 1]) {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb2.png");
                                } else {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb1.png");
                                }                                
                            }
                        }
                        //kontrola, či sa telo hada, ktoré kreslíme nenachádza na dolnej strane plochy
                        //pričom telo pred ním musí byť na opačnej strane plochy a telo za ním musí byť na rovnakej pozícií
                        if ((this.pozicieYHada2[i] == (this.vyskaPlochy - this.velkostTelaHada)) && (this.pozicieYHada2[i - 1] == 0)) {
                            if (this.pozicieYHada2[i + 1] == (this.vyskaPlochy - this.velkostTelaHada)) {
                                if (this.pozicieXHada2[i] < this.pozicieXHada2[i + 1]) {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb3.png");
                                } else {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb4.png");
                                }                                
                            }
                        }
                        //kontrola, či sa telo hada, ktoré kreslíme nenachádza na ľavej strane plochy
                        //pričom telo pred za musí byť na opačnej strane plochy a telo pred ním musí byť na rovnakej pozícií
                        if ((this.pozicieXHada2[i] == 0) && (this.pozicieXHada2[i + 1] == (this.sirkaPlochy - this.velkostTelaHada))) {
                            if (this.pozicieXHada2[i - 1] == 0) {
                                if (this.pozicieYHada2[i] < this.pozicieYHada2[i - 1]) {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb4.png");
                                } else {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb1.png");
                                }                                
                            }
                        }
                        //kontrola, či sa telo hada, ktoré kreslíme nenachádza na pravej strane plochy
                        //pričom telo pred za musí byť na opačnej strane plochy a telo pred ním musí byť na rovnakej pozícií
                        if ((this.pozicieXHada2[i] == (this.sirkaPlochy - this.velkostTelaHada)) && (this.pozicieXHada2[i + 1] == 0)) {
                            if (this.pozicieXHada2[i - 1] == (this.sirkaPlochy - this.velkostTelaHada)) {
                                if (this.pozicieYHada2[i] < this.pozicieYHada2[i - 1]) {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb3.png");
                                } else {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb2.png");
                                }                                
                            }
                        }
                        //kontrola, či sa telo hada, ktoré kreslíme nenachádza na hornej strane plochy
                        //pričom telo pred za musí byť na opačnej strane plochy a telo pred ním musí byť na rovnakej pozícií
                        if ((this.pozicieYHada2[i] == 0) && (this.pozicieYHada2[i + 1] == (this.vyskaPlochy - this.velkostTelaHada))) {
                            if (this.pozicieYHada2[i - 1] == 0) {
                                if (this.pozicieXHada2[i] < this.pozicieXHada2[i - 1]) {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb2.png");
                                } else {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb1.png");
                                }                                
                            }
                        }
                        //kontrola, či sa telo hada, ktoré kreslíme nenachádza na dolnej strane plochy
                        //pričom telo pred za musí byť na opačnej strane plochy a telo pred ním musí byť na rovnakej pozícií
                        if ((this.pozicieYHada2[i] == (this.vyskaPlochy - this.velkostTelaHada)) && (this.pozicieYHada2[i + 1] == 0)) {
                            if (this.pozicieYHada2[i - 1] == (this.vyskaPlochy - this.velkostTelaHada)) {
                                if (this.pozicieXHada2[i] < this.pozicieXHada2[i - 1]) {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb3.png");
                                } else {
                                    this.had2 = new ImageIcon("obrazky/ciernyohyb4.png");
                                }                                
                            }
                        }
                        Image obraz = this.had2.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                        this.had2 = new ImageIcon(obraz);
                        this.had2.paintIcon(this, grafika, this.pozicieXHada2[i], this.pozicieYHada2[i]);                        
                    }           
                }  
                break;
            //farebný had sa kreslí pomocou obrázkov zo súboru
            case "farebny2" :
                for (int i = 0; i < this.teloHada2; i++) {
                    if (i == 0) {
                        switch (this.getSmerHada2()) {
                            //podľa smeru farebného hada sa nakreslí jeho hlava
                            case VPRAVO:
                                //načíta sa obrázok a upravia sa jeho rozmery podľa tela hada
                                this.had2 = new ImageIcon("obrazky/hlava vpravo2.png");
                                Image obraz1 = this.had2.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had2 = new ImageIcon(obraz1);
                                break;
                            case VLAVO:
                                this.had2 = new ImageIcon("obrazky/hlava vlavo2.png");
                                Image obraz2 = this.had2.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had2 = new ImageIcon(obraz2);
                                break;
                            case HORE:
                                this.had2 = new ImageIcon("obrazky/hlava hore2.png");
                                Image obraz3 = this.had2.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had2 = new ImageIcon(obraz3);
                                break;
                            case DOLE:
                                this.had2 = new ImageIcon("obrazky/hlava dole2.png");
                                Image obraz4 = this.had2.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                                this.had2 = new ImageIcon(obraz4);
                                break;
                        }
                        this.had2.paintIcon(this, grafika, this.pozicieXHada2[i], this.pozicieYHada2[i]);
                    } else {
                        //kreslenie tela farebného hada
                        this.had2 = new ImageIcon("obrazky/telo2.png");
                        Image obraz = this.had2.getImage().getScaledInstance(this.velkostTelaHada, this.velkostTelaHada, java.awt.Image.SCALE_DEFAULT);
                        this.had2 = new ImageIcon(obraz);
                        this.had2.paintIcon(this, grafika, this.pozicieXHada2[i], this.pozicieYHada2[i]);
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
            //zmena farby prekážok na bielu ak je typ druhého hada čierny
            if (this.typHada2.equals("cierny")) {
                //nastaví si farbu, ktorou vyplní vytvorený obdĺžnik
                grafika.setColor(new Color(192, 192, 192));
            } else {
                grafika.setColor(Color.black);
            }
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
        if (this.pohybujeSaHad) {
            if (this.typMapy.equals("ťažká")) {
                this.nakresliPrekazky(grafika);
            }
            this.nakresliJablko(grafika);
            this.nakresliHada(grafika, this.typHada);
            this.nakresliHada2(grafika, this.typHada2);
        } else {
            //ak nejaký hráč vyhral, hra sa končí a spustí sa daná metóda
            if (this.skontrolujCiNiektoVyhral()) {
                this.vyhral(grafika);
            } else {
                this.prehral(grafika);  
            }            
        }
    }
    
    /**
     * Metóda posúva hadov.
     * Každému hadovi sa posunie pozícia hlavy podľa jeho smeru.
     * Telo každého hada sa posúva podľa pozície predošlého tela konkrétneho hada.
     */
    public void posunHadov() {
        //posun prvého hada
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
        //posun druhého hada
        for (int i = this.teloHada2; i >= 0; i--) {
            if (!(i == 0)) {
                //zmena pozície tela druhého hada na pozíciu tela predchádzajúceho
                this.pozicieXHada2[i] = this.pozicieXHada2[i - 1]; 
                this.pozicieYHada2[i] = this.pozicieYHada2[i - 1];
            } else {
                //podľa smeru druhého hada sa posunú pozície hlavy druhého hada
                switch (this.getSmerHada2()) {
                    case VPRAVO:
                        this.pozicieXHada2[i] = this.pozicieXHada2[i] + this.velkostTelaHada;
                        break;
                    case VLAVO:
                        this.pozicieXHada2[i] = this.pozicieXHada2[i] - this.velkostTelaHada;
                        break;    
                    case HORE:
                        this.pozicieYHada2[i] = this.pozicieYHada2[i] - this.velkostTelaHada;
                        break;
                    case DOLE:
                        this.pozicieYHada2[i] = this.pozicieYHada2[i] + this.velkostTelaHada;
                        break;
                }   
            }          
        }
    }
    
    /**
     * Metóda kontroluje, či nejaký had nezjedol jablko.
     * Podľa typu mapy sa kontroluje, či sa hlava konkrétneho hada nenachádza na pozícií jablka.
     * Následne sa danému hráčovi zvýši skóre a telo konkrétneho hada sa predĺži.
     * Zjedené jablko sa znovu vytvorí.
     */
    public void skontrolujCiZjedliJablko() {
        //v ľahkej mape sa kontrolujú všetky jablká
        if (this.typMapy.equals("ľahká")) {
            //kontrola, či sa hlava hada nenachádza na pozícií jablka z poľa jabĺk
            for (int i = 0; i < this.pocetJablk; i++) {
                if ((this.pozicieXHada[0] == this.pozXJablk[i]) && (this.pozicieYHada[0] == this.pozYJablk[i])) {
                    //ak je zlaté, skóre sa zvýši o 5
                    if (this.zlateJablka[i] == 1) {
                        this.skoreHada = this.skoreHada + 5;
                    } else {
                        this.skoreHada++;
                    }
                    this.teloHada++;
                    //vytvorí sa zjedené jablko a zmení sa hodnota zlatého jablka
                    this.zlateJablka[i] = 0;
                    this.vytvorJablko(i, i);
                }
            }
            //kontrola, či sa hlava druhého hada nenachádza na pozícií jablka z poľa jabĺk
            for (int i = 0; i < this.pocetJablk; i++) {
                if ((this.pozicieXHada2[0] == this.pozXJablk[i]) && (this.pozicieYHada2[0] == this.pozYJablk[i])) {
                    //ak je zlaté, skóre sa zvýši o 5
                    if (this.zlateJablka[i] == 1) {
                        this.skoreHada2 = this.skoreHada2 + 5;
                    } else {
                        this.skoreHada2++;
                    }
                    this.teloHada2++;
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
                    this.skoreHada = this.skoreHada + 5;
                } else {
                    this.skoreHada++;
                }
                this.teloHada++;
                //vytvorí sa zjedené jablko a zmení sa hodnota zlatého jablka
                this.zlateJablko = false; 
                this.vytvorJablko();
            }
            //kontrola, či sa hlava druhého hada nenachádza na pozícií jablka
            if ((this.pozicieXHada2[0] == this.pozXJablka) && (this.pozicieYHada2[0] == this.pozYJablka)) {
                //ak je zlaté, skóre sa zvýši o 5
                if (this.zlateJablko) {
                    this.skoreHada2 = this.skoreHada2 + 5;
                } else {
                    this.skoreHada2++;
                }
                this.teloHada2++;
                //vytvorí sa zjedené jablko a zmení sa hodnota zlatého jablka
                this.zlateJablko = false; 
                this.vytvorJablko();
            }
        }
    }
    
    /**
     * Metóda kontroluje, či nejaký had nenarazil.
     * Podľa typu mapy sa kontroluje, či sa hlava daného hada nenachádza na neprípustnej pozícii.
     * Následne sa nastaví pohyb daného hada.
     */
    public void skontrolujCiNarazili() {
        //kontrola, či sa hlava hada nenachádza na nejakej pozícii svojho tela
        for (int i = this.teloHada; i > 0; i--) {
            if ((this.pozicieXHada[0] == this.pozicieXHada[i]) && (this.pozicieYHada[0] == this.pozicieYHada[i])) {
                this.setPohybujeSa(false);
                this.setPohybujeSaHad2(false);
            }
        }
        //kontrola, či sa hlava hada nenachádza na nejakej pozícii prekážky
        if (this.typMapy.equals("ťažká")) {
            for (int i = 0; i < this.pocetPrekazok; i++) {
                if ((this.pozicieXHada[0] == this.pozXPrekazok[i]) && (this.pozicieYHada[0] == this.pozYPrekazok[i])) {
                    this.setPohybujeSa(false);
                    this.setPohybujeSaHad2(false);
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
            //pokiaľ had narazil, oba hady sa prestanú pohybovať
            if (this.pozicieXHada[0] < 0) {
                this.setPohybujeSa(false);
                this.setPohybujeSaHad2(false);
            }   
            if (this.pozicieXHada[0] == this.sirkaPlochy) {
                this.setPohybujeSa(false);
                this.setPohybujeSaHad2(false);
            }
            if (this.pozicieYHada[0] < 0) {
                this.setPohybujeSa(false);
                this.setPohybujeSaHad2(false);
            }
            if (this.pozicieYHada[0] == this.vyskaPlochy) {
                this.setPohybujeSa(false);
                this.setPohybujeSaHad2(false);
            }
        }
        //kontrola, či sa hlava druhého hada nenachádza na nejakej pozícii svojho tela
        for (int i = this.teloHada2; i > 0; i--) {
            if ((this.pozicieXHada2[0] == this.pozicieXHada2[i]) && (this.pozicieYHada2[0] == this.pozicieYHada2[i])) {
                this.setPohybujeSa(false);
                this.setPohybujeSaHad2(false);
            }
        }
        //kontrola, či sa hlava druhého hada nenachádza na nejakej pozícii prekážky
        if (this.typMapy.equals("ťažká")) {
            for (int i = 0; i < this.pocetPrekazok; i++) {
                if ((this.pozicieXHada2[0] == this.pozXPrekazok[i]) && (this.pozicieYHada2[0] == this.pozYPrekazok[i])) {
                    this.setPohybujeSa(false);
                    this.setPohybujeSaHad2(false);
                }
            } 
        }
        //posunutie pozície hlavy druhého hada, ak druhý had prešiel cez stenu
        if (this.typMapy.equals("ľahká")) {
            if (this.pozicieXHada2[0] < 0) {
                this.pozicieXHada2[0] = this.sirkaPlochy - this.velkostTelaHada;
            }
            if (this.pozicieXHada2[0] == this.sirkaPlochy) {
                this.pozicieXHada2[0] = 0;
            }
            if (this.pozicieYHada2[0] < 0) {
                this.pozicieYHada2[0] = this.vyskaPlochy - this.velkostTelaHada;
            }
            if (this.pozicieYHada2[0] == this.vyskaPlochy) {
                this.pozicieYHada2[0] = 0;
            }
        } else {
            //pokiaľ druhý had narazil, oba hady sa prestanú pohybovať
            if (this.pozicieXHada2[0] < 0) {
                this.setPohybujeSa(false);
                this.setPohybujeSaHad2(false);
            }
            if (this.pozicieXHada2[0] == this.sirkaPlochy) {
                this.setPohybujeSa(false);
                this.setPohybujeSaHad2(false);
            }
            if (this.pozicieYHada2[0] < 0) {
                this.setPohybujeSa(false);
                this.setPohybujeSaHad2(false);
            }
            if (this.pozicieYHada2[0] == this.vyskaPlochy) {
                this.setPohybujeSa(false);
                this.setPohybujeSaHad2(false);
            }
        }
        //kontrola, či nejaký hráč nevyhral
        if (this.skontrolujCiNiektoVyhral()) {
            this.setPohybujeSa(false);
            this.setPohybujeSaHad2(false);
        }
        //kontrola, či sa hlava hada neanchádza na pozícii druhého hada
        for (int i = 0; i < this.teloHada; i++) {
            for (int j = 0; j < this.teloHada2; j++) {
                if ((this.pozicieXHada[i] == this.pozicieXHada2[j]) && (this.pozicieYHada[i] == this.pozicieYHada2[j])) {
                    this.setPohybujeSa(false);
                    this.setPohybujeSaHad2(false);
                    break;
                }
            }
        }
        //kontrola, či sa hlava druhého hada neanchádza na pozícii prvého hada
        for (int i = 0; i < this.teloHada2; i++) {
            for (int j = 0; j < this.teloHada; j++) {
                if ((this.pozicieXHada2[i] == this.pozicieXHada[j]) && (this.pozicieYHada2[i] == this.pozicieYHada[j])) {
                    this.setPohybujeSa(false);
                    this.setPohybujeSaHad2(false);
                    break;
                }
            }
        }
        //ak sa had prestal pohybovať, časovač sa zastaví
        if ((!this.pohybujeSaHad) || (!this.pohybujeSaHad2)) {
            this.casovac.stop();
        }
    }
    
    /**
     * Metóda kreslí údaje o hre, ak nejaký hráč prehral.
     * Zapne sa reštart a nakreslí sa text na vhodnej pozícii.
     * Následne sa údaje uložia do súboru.
     * 
     * @param grafika        využitie grafiky
     */
    public void prehral(Graphics grafika) {
        this.restart = true;
        //nastaví sa font a nakreslí sa text
        Font mojFont = new Font("TimesRoman", 10, this.velkostTelaHada);
        //zmení sa farba textu, ak typ druhého hada je čierny
        if (this.typHada2.equals("cierny")) {
            grafika.setColor(Color.white);
        } else {
            grafika.setColor(Color.black);
        }       
        grafika.setFont(mojFont);
        grafika.drawString("Prehrali ste :(", this.sirkaPlochy / 4, this.vyskaPlochy / 3);
        grafika.drawString("Skóre: ", this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 30);
        grafika.drawString(this.menoHraca + " " + this.skoreHada, this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 60);
        grafika.drawString(this.menoHraca2 + " " + this.skoreHada2, this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 90);
        grafika.drawString("Vyššie skóre mal: " + this.zistiKtoVyhral(), this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 120);
        grafika.drawString("Vaše údaje boli uložené do súboru.", this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 150);
        grafika.drawString("Pre reštart stlačte medzerník!", this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 180);
        //údaje sa zapíšu do súboru
        try {
            this.zapisDoSuboru("skore.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Metóda kreslí údaje o hre, ak nejaký hráč vyhral.
     * Zapne sa reštart a nakreslí sa text na vhodnej pozícii.
     * Následne sa údaje uložia do súboru.
     * 
     * @param grafika        využitie grafiky
     */
    public void vyhral(Graphics grafika) {
        this.restart = true;
        //nastaví sa font a nakreslí sa text
        Font mojFont = new Font("TimesRoman", 10, this.velkostTelaHada);
        //zmení sa farba textu, ak typ druhého hada je čierny
        if (this.typHada2.equals("cierny")) {
            grafika.setColor(Color.white);
        } else {
            grafika.setColor(Color.black);
        }
        grafika.setFont(mojFont);
        grafika.drawString("Gratulujeme!!! Vyhrali ste!!!", this.sirkaPlochy / 4, this.vyskaPlochy / 3);
        grafika.drawString("Skóre: ", this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 30);
        grafika.drawString(this.menoHraca + " " + this.skoreHada, this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 60);
        grafika.drawString(this.menoHraca2 + " " + this.skoreHada2, this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 90);
        grafika.drawString("Vyhral: " + this.zistiKtoVyhral(), this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 120);
        grafika.drawString("Vaše údaje boli uložené do súboru.", this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 150);
        grafika.drawString("Pre reštart stlačte medzerník!", this.sirkaPlochy / 4, (this.vyskaPlochy / 3) + 180);
        //údaje sa zapíšu do súboru
        try {
            this.zapisDoSuboru("skore.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Metóda zistí, ktorý hráč vyhral.
     * Kontroluje sa skóre obidvoch hadov.
     * 
     * @return         návratová hodnota vráti, ktorý hráč vyhral
     */
    public String zistiKtoVyhral() {
        String vyhral = this.menoHraca;
        if (this.skoreHada2 > this.skoreHada) {
            vyhral = this.menoHraca2;
        } else if (this.skoreHada2 == this.skoreHada) {
            vyhral = "remíza";
        }
        return vyhral;
    }   
    
    /**
     * Metóda zistí, či niektorý hráč vyhral.
     * Kontroluje sa, či sa telo niektorého hada nepredĺžilo najviac ako sa mu dá.
     * 
     * @return         návratová hodnota vráti, či niektorý hráč vyhral
     */
    public boolean skontrolujCiNiektoVyhral() {
        boolean kontrola = false;
        if (this.typMapy.equals("ťažká")) {
            if (this.teloHada == (this.pocetPolicok - this.pocetPrekazok - this.teloHada2)) {
                kontrola = true;
            }
            if (this.teloHada2 == (this.pocetPolicok - this.pocetPrekazok - this.teloHada)) {
                kontrola = true;
            }
        } else {
            if (this.teloHada == (this.pocetPolicok - this.teloHada2)) {
                kontrola = true;
            }
            if (this.teloHada2 == (this.pocetPolicok - this.teloHada)) {
                kontrola = true;
            }
        }
        return kontrola;
    }
    
    /**
     * Metóda zisťuje, ktorú klávesu sme stlačili.
     * Podľa stlačenej klávesy sa danému hadovi nastaví vhodný smer.
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
            case KeyEvent.VK_W:
                if (this.getSmerHada2() != Smer.DOLE) {
                    this.setSmerHada2(Smer.HORE);
                }
                break;
            case KeyEvent.VK_A:
                if (this.getSmerHada2() != Smer.VPRAVO) {
                    this.setSmerHada2(Smer.VLAVO);
                }
                break;
            case KeyEvent.VK_S:
                if (this.getSmerHada2() != Smer.HORE) {
                    this.setSmerHada2(Smer.DOLE);
                }
                break;
            case KeyEvent.VK_D:
                if (this.getSmerHada2() != Smer.VLAVO) {
                    this.setSmerHada2(Smer.VPRAVO);
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
        this.teloHada2 = 4;
        this.skoreHada = 0;
        this.skoreHada2 = 0;
        this.smerHada = Smer.values()[new Random().nextInt(Smer.values().length)];
        this.smerHada2 = Smer.values()[new Random().nextInt(Smer.values().length)]; 
        this.restart = false;
        this.zlateJablko = false;
        //znovu spustenie hry
        this.spustiHru();
    }
    
    /**
     * Metóda zapisuje údaje do súboru.
     * Otvorí sa súbor a vloží sa doň nový text.
     * Zapíše sa deň, kedy sme hru hrali, typ mapy, skóre a meno hráčov.
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
        zapisovac.println(this.menoHraca + " " + this.skoreHada);
        zapisovac.println(this.menoHraca2 + " " + this.skoreHada2);
        zapisovac.println("Vyhral: " + this.zistiKtoVyhral());
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