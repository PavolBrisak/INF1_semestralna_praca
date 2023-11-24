import javax.swing.JFrame;

/**
 * Trieda vytvára okno, na ktoré sa vloži hra hadíka.
 * 
 * @author      Pavol Brišák
 * @version     1.0
 */
public class HraciaPlocha extends JFrame {
    private JFrame okno;    
    /**
      * Konštruktor triedy, v ktorom sa inicializujú atribúty.
      * Následne sa zapne hra.
      * 
      * @param pocetHracov     počet hráčov
      * @param typMapy         typ mapy
      * @param typHada         typ hada
      * @param typHada2        typ druhého hada
      * @param menoHraca       meno hráča
      * @param menoHraca2      meno druhého hráča
      */
    public HraciaPlocha(int pocetHracov, String typMapy, String typHada, String typHada2, String menoHraca, String menoHraca2) {
        this.okno = new JFrame();
        this.zapniHru(pocetHracov, typMapy, typHada, typHada2, menoHraca, menoHraca2);       
    }
    
    /**
      * Metóda spustí triedu hry hadíka podľa zvoleného počtu hráčov.
      * 
      * @param pocetHracov     počet hráčov
      * @param typMapy         typ mapy
      * @param typHada         typ hada
      * @param typHada2        typ druhého hada
      * @param menoHraca       meno hráča
      * @param menoHraca2      meno druhého hráča
      */
    public void zapniHru(int pocetHracov, String typMapy, String typHada, String typHada2, String menoHraca, String menoHraca2) {
        switch (pocetHracov) {
            case 1:                   
                this.okno.add(new Hadik1Hrac(typMapy, typHada, menoHraca));
                this.nastavVlastnostiOkna();
                break;
            case 2:
                this.okno.add(new Hadik2Hraci(typMapy, typHada, typHada2, menoHraca, menoHraca2));
                this.nastavVlastnostiOkna();
                break;               
        }
    }  
    
    /**
      * Metóda nastaví vlatnosti okna na ktoré sa vloži hra hadíka.
      * Nastaví sa mu názov, lokáciu, rozmery a viditeľnosť.
      */
    public void nastavVlastnostiOkna() {
        this.okno.setTitle("Hadík");
        this.okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.okno.setResizable(false);
        this.okno.pack();        
        this.okno.setLocationRelativeTo(null);
        this.okno.setVisible(true);
    } 
}