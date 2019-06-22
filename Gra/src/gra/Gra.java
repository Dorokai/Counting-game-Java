package gra;
import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;
public class Gra extends JFrame{
    //Tworzenie obu tablic i guzików na maksymalnym zakresie
    int[] tablica = new int[36];
    int[] tablica2 = new int[36];
    JButton przycisk[] = new JButton[36];
    JPanel panel = new JPanel();
    JMenuBar menuBar = new JMenuBar();
    //Tworzenie nazw Menu
    JMenu menuTryb = new JMenu("Tryb gry");
    JMenu menuAutor = new JMenu("O autorze");
    JMenu menuWielkosc = new JMenu("Trudność");
    //Tworzenie nazw podmenu trybu
    JCheckBoxMenuItem ujemne = new JCheckBoxMenuItem("Ujemne liczby");
    JCheckBoxMenuItem zapamietaj = new JCheckBoxMenuItem("Tryb zapamiętywania");
    //Tworzenie nazw podmenu autora
    JMenuItem autor = new JMenuItem("Autor");
    //Tworzenie nazw podmenu trudności
    JMenuItem latwy = new JMenuItem("Łatwy (3x3)");
    JMenuItem sredni = new JMenuItem("Średni (4x4)");
    JMenuItem trudny = new JMenuItem("Trudny (5x5)");
    JMenuItem btrudny = new JMenuItem("Bardzo trudny (6x6)");
    //Grupa przycisków po to by zablokować ujemne liczby przy trybie zapamiętywania
    ButtonGroup grupa = new ButtonGroup();
    //Utworzenie i zmiana czcionki czasu na dole
    JLabel czas = new JLabel("<html><span style='font-size: 12px;'>Czas: </span></html>",SwingConstants.CENTER);
    //Pierwsze losowanie tablicy
    Losuj losowanie = new Losuj();
    //Opcje podczas wygrania rozgrywki
    Object[] opcje = { "Tak","Nie"};
    int liczba, m=0 ,i=0 ,licz=0 ,j=0, x=0,u=-1, blad=0, rozmiar=6, prev=0;
    double czasGry=0;
    boolean dziala = true, zapamietywanie = false, rozpoczeta = true;
    class NasluchPrzycisków implements ActionListener{
        public void actionPerformed(ActionEvent e){
            liczba = Integer.parseInt((((JButton) e.getSource()).getText()));
            for(x=0;x<(rozmiar*rozmiar);x++)
                //Gdy numer tablicy pierwszej zgadza się z numerem tablicy drugiej to przycisk znika, a my szukamy następnego
                if (e.getSource()==przycisk[x]&&tablica[j]==liczba){
                    przycisk[x].setVisible(false);
                    j++;
                }
                //Gdy się nie zgadza to nalicza nam błąd
                else if(e.getSource()==przycisk[x]&&tablica[j]!=liczba) blad++; 
                //Gdy liczba wyszukanych przycisków jest równa z rozmiarem to zaczyna się zakończenie gry
                if(j==(rozmiar*rozmiar)){
                    //Stop zegara
                    dziala=false;
                    //Wiadomość o wynikach
                    JLabel wiadomosc = new JLabel("<html>Wygrałeś! <br />Ilość błędów: "+blad+".<br />Czas gry: "+String.format("%.1f", czasGry)+"sek.<br />Czy chcesz zagrać jeszcze raz?</html>");
                    int wynik = JOptionPane.showOptionDialog(null, wiadomosc, "Wygrana", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, opcje, null);       
                    //Wyjście z gry
                    if(wynik == JOptionPane.NO_OPTION)System.exit(0);
                    //Jeśli chcemy kontynuować to losuje nam od nowa planszę
                    else if(wynik == JOptionPane.YES_OPTION){
                        losowanie.losuj(tablica,tablica2,u,rozmiar);
                        for(i=0; i<(rozmiar*rozmiar);i++){
                            przycisk[i].setText(""+tablica2[i]);
                            przycisk[i].setVisible(true);
                            j=0;
                            blad=0;
                            czasGry=0;
                            m=0;
                            licz=0;
                            dziala = true;
                        }
                        if(zapamietywanie) pokazywacz.start();
                    }
                }
        }
    }
    //Nasłuchiwacz liczb ujemnych
    //Jeśli włączymy liczby ujemne, to zakres zmienia się na -20 do nieskończoności
    class NasluchMinusowych implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(ujemne.isSelected()) {
                //Resetujemy grę i zmieniamy "u" na -1
                //"u" to mnożnik w funkcji
                u=-1;
                j=0;
                blad=0;
                czasGry=0;
                losowanie.losuj(tablica,tablica2,u,rozmiar);
                for(i=0; i<(rozmiar*rozmiar);i++) {
                    przycisk[i].setText(""+tablica2[i]);
                    przycisk[i].setVisible(true);
                }
            } 
            //Jeśli jest wyłączony to wtedy zakres jest tylko dodatni
            //"u" jest dodatnie i mnożymy *1
            //Resetujemy grę
            else {
                u=1;
                losowanie.losuj(tablica,tablica2,u,rozmiar);
                for(i=0; i<(rozmiar*rozmiar);i++){
                    przycisk[i].setText(""+tablica2[i]);
                    przycisk[i].setVisible(true);
                    j=0;
                    blad=0;
                    czasGry=0;
                }
            }
        }
    }
    //Przycisk autora wyświetla nam okno z autorem
    class NasluchAutora implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(e.getSource() == autor) JOptionPane.showMessageDialog(rootPane, "Mateusz Cyra 17661", "Autor",1);
        }
    }
    //Przyciski wybierające trudność
    class NasluchTrudnosci implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //Zatrzymujemy czas
            pokazywacz.stop();
            //Usuwamy przyciski
            for(i=0;i<(rozmiar*rozmiar);i++) panel.remove(przycisk[i]);
            //Resetujemy grę
            j=0;
            blad=0;
            czasGry=0;
            m=0;
            licz=0;
            //Ustawiamy trudność
            if(e.getSource() == latwy) rozmiar = 3;
            if(e.getSource() == sredni) rozmiar = 4;
            if(e.getSource() == trudny) rozmiar = 5;
            if(e.getSource() == btrudny) rozmiar = 6;
            //Zmieniamy panel
            panel.setLayout(new GridLayout(rozmiar,rozmiar));
            //Losujemy numery na przyciskach
            losowanie.losuj(tablica,tablica2,u,rozmiar);
            //Jeśli tryb zapamiętywania jest włączony, 
            //zmieniamy kolor i tekst przycisków na biały
            //I uruchamiamy pokazywacz, który na sekundę zmienia przyciski
            //Na czerwone, a potem zmienia je znowu na białe
            if(zapamietywanie){
                for(i=0; i<(rozmiar*rozmiar);i++) {
                    panel.add(przycisk[i]);
                    przycisk[i].setText(""+tablica2[i]);
                    przycisk[i].setBackground(Color.WHITE);
                    przycisk[i].setForeground(Color.WHITE);
                    przycisk[i].setVisible(true);
                }
            pokazywacz.start();
            }
            //Jeśli tryb normalny, to zmieniamy kolor przycisków na czarny
            else{
                for(i=0; i<(rozmiar*rozmiar);i++) {
                    panel.add(przycisk[i]);
                    przycisk[i].setText(""+tablica2[i]);
                    przycisk[i].setForeground(Color.BLACK);
                    przycisk[i].setVisible(true);
                }
            }
        }
    }
    //Nasłuchiwacz Trybu gry - normalny lub zapamiętywanie
    class NasluchTrybu implements ActionListener{
        public void actionPerformed(ActionEvent e){
            //Jeśli tryb zapamiętywania jest uruchomiony to 
            //Zmieniamy kolor czcionki na biały, a rozmiar czcionki na 0, by nie było jej widać
            //Przywracamy wszystkie przyciski jako widoczne, uruchamiamy czas
            if(zapamietaj.isSelected()){
                ujemne.setEnabled(false);
                zapamietywanie = true;
                losowanie.losuj(tablica,tablica2,u,rozmiar);
                for(i=0; i<(rozmiar*rozmiar);i++) {
                    panel.add(przycisk[i]);
                    przycisk[i].setText(""+tablica2[i]);
                    przycisk[i].setForeground(Color.WHITE);
                    przycisk[i].setFont(new Font("Chiller", Font.PLAIN, 0));
                    przycisk[i].setVisible(true);
                }
                pokazywacz.start();
            }
            else{
                ujemne.setEnabled(true);
                zapamietywanie = false;
                pokazywacz.stop();
                //Uruchomienie guzików trudności
                latwy.setEnabled(true);
                sredni.setEnabled(true);
                trudny.setEnabled(true);
                btrudny.setEnabled(true);
                licz=0;
                //Poprzedni przycisk zmienia się na biały
                przycisk[prev].setBackground(Color.WHITE);
                u=1;
                losowanie.losuj(tablica,tablica2,u,rozmiar);
                for(i=0; i<(rozmiar*rozmiar);i++){
                    przycisk[i].setText(""+tablica2[i]);
                    //Przywracamy wszystkie guziki, kolor i rozmiar czcionki
                    przycisk[i].setForeground(Color.BLACK);
                    przycisk[i].setFont(new Font("Chiller", Font.PLAIN, 45));
                    przycisk[i].setVisible(true);
                    for(i=0; i<(rozmiar*rozmiar);i++) {
                        przycisk[i].setEnabled(true);
                    }
                    //Resetujemy grę
                    j=0;
                    blad=0;
                    czasGry=0;
                }
            }
        }
    }
    //  Pierwsze uruchomienie gry jest na trybie normalnym, ale ujemnym
    //  Po to by zainicjować wszystkie klawisze
    public Gra(){
        super("Gra");
        ujemne.setSelected(true);
        menuBar.add(menuTryb);
        menuBar.add(menuWielkosc);
        menuBar.add(menuAutor);
        menuTryb.add(ujemne);
        menuTryb.add(zapamietaj);
        menuAutor.add(autor);
        grupa.add(latwy);
        grupa.add(sredni);
        grupa.add(trudny);
        grupa.add(btrudny);
        menuWielkosc.add(latwy);
        menuWielkosc.add(sredni);
        menuWielkosc.add(trudny);
        menuWielkosc.add(btrudny);
        setLayout(new BorderLayout());
        panel.setLayout(new GridLayout(rozmiar,rozmiar));
        add(menuBar,"North");
        //Tworzenie nasłuchów
        NasluchPrzycisków przyciski = new NasluchPrzycisków();
        NasluchMinusowych minusowe = new NasluchMinusowych();
        NasluchTrybu wybranyTryb = new NasluchTrybu();
        NasluchAutora przyciskAutora = new NasluchAutora();
        NasluchTrudnosci przyciskiTrudnosci = new NasluchTrudnosci();
        //Przywiązanie nasłuchów do guzików
        ujemne.addActionListener(minusowe);
        zapamietaj.addActionListener(wybranyTryb);
        autor.addActionListener(przyciskAutora);
        latwy.addActionListener(przyciskiTrudnosci);
        sredni.addActionListener(przyciskiTrudnosci);
        trudny.addActionListener(przyciskiTrudnosci);
        btrudny.addActionListener(przyciskiTrudnosci);
        //Pierwsze losowanie
        losowanie.losuj(tablica,tablica2,u,rozmiar);
        //Utworzenie guzików
        for(i=0;i<(rozmiar*rozmiar);i++) przycisk[i] = new JButton(""+tablica2[i]);
        for(i=0;i<(rozmiar*rozmiar);i++){
            //Dodanie przycisku na planszę
            add(przycisk[i]);
            //Dodanie nasłuchów
            przycisk[i].addActionListener(przyciski);
            //Zmiana czcionki i koloru
            przycisk[i].setFont(new Font("Chiller", Font.PLAIN, 45));
            //Usunięcie podświetlenia obramowań czcionki na stałe
            przycisk[i].setFocusPainted(false);
            //Zmiana tła przycisku na biały
            przycisk[i].setBackground(Color.WHITE);
            //Dodanie przycisku do panelu
            panel.add(przycisk[i]);
        }
        //Dodanie panelu z przyciskami na środek
        add(panel,"Center");
        //Dodanie panelu z czasem na dół
        add(czas,"South");
        setSize(550,550);
        //Zablokowanie zmiany rozmiaru
        setResizable(false);
        //Wyśrodkowanie okienka na monitorze
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        //Uruchomienie pierwszego czasomierza
        czasomierz.start();
    }
    //Czasomierz odpowiada za odliczanie czasu co sto milisekund
    Timer czasomierz = new Timer(100, new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            if(dziala&&rozpoczeta) {
                czasGry = czasGry+0.1;
                //Odświeżenie labelu z czasem co sto milisekund
                czas.setText("<html><span style='font-size: 12px;'>Czas: " + String.format("%.1f", czasGry) + "</span></html>");
            }
        }
    });
    //Timer pokazywacz do drugiego trybu gry.
    //Ma on za zadanie zmieniać kolory klocków z białego na czerwony co 200 milisekund
    //A następnie zmienić je na biały kolor i wskazać następny
    Timer pokazywacz = new Timer(200, new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            if(licz<(rozmiar*rozmiar)){
                //Wyłączenie guzików trudności, by nie zepsuć gry
                latwy.setEnabled(false);
                sredni.setEnabled(false);
                trudny.setEnabled(false);
                btrudny.setEnabled(false);
                //Wyłączenie przycisków na chwilę, by nie można było
                //Zacząć gry
                for(i=0; i<(rozmiar*rozmiar);i++) {
                    przycisk[i].setEnabled(false);
                }
                if(licz==0){
                    //Wyzerowanie czasu gry i zablokowanie na czas pokazywania guzików
                    czasGry=0;
                    czas.setText("<html><span style='font-size: 12px;'>Czas: " + String.format("%.1f", czasGry) + "</span></html>");
                    rozpoczeta=false;
                    for(m=0;m<(rozmiar*rozmiar);m++){
                        if(tablica[licz]==tablica2[m]){
                            //Pokazywanie guzika pierwszego
                            przycisk[m].setBackground(Color.RED);
                            przycisk[m].setForeground(Color.RED);
                            prev=m;
                            break;
                        }
                    }
                }
                //Dla kolejnych guzików
                else{
                    //Wyszukanie kolejnego guzika
                    for(m=0;m<(rozmiar*rozmiar);m++){
                        if(tablica[licz]==tablica2[m]){
                            //Zmiana koloru na czerwony
                            przycisk[m].setBackground(Color.RED);
                            przycisk[m].setForeground(Color.RED);
                            //Zmiana koloru poprzedniego guzika na biały
                            przycisk[prev].setBackground(Color.WHITE);
                            przycisk[prev].setForeground(Color.WHITE);
                            prev=m;
                            //Przerwanie pętli dla skrócenia czasu
                            break;
                        }
                    }  
                }
            }
            m=0;
            licz++;
            //Gdy licz osiągnie liczbę większą o jeden niż rozmiar
            //To ostatni czerwony przycisk zmieni się na biały
            //To wymagany if, ponieważ nie wystąpi pobranie guzika nieistniejącego
            if(licz==((rozmiar*rozmiar)+1)){
                licz++;
                przycisk[prev].setBackground(Color.WHITE);
                przycisk[prev].setForeground(Color.WHITE);
                rozpoczeta = true;
                licz=0;
                for(i=0; i<(rozmiar*rozmiar);i++) {
                    przycisk[i].setEnabled(true);
                }
                //Uruchomienie guzików trudności
                latwy.setEnabled(true);
                sredni.setEnabled(true);
                trudny.setEnabled(true);
                btrudny.setEnabled(true);
                //Zatrzymanie pokazywacza
                pokazywacz.stop();
            }
        }
    });

    public static void main(String[] args) { 
    new Gra();
    }
}
