package gra;
import java.util.Random;
public class Losuj {
    //Utworzenie randoma do generowania losowych numerów na przyciskach
    Random rand = new Random();
    void losuj(int tab[], int tab2[], int czyDodatnia, int rozmiar){
        //Pierwsza pętla jest dla pierwszego (najmniejszego) guzika
        //Do reszty dodaje się random w zakresie 1-8 -> aby numery się nie powtarzały
        //Tworzone są tu dwie takie same tablice
        for (int i=0;i<(rozmiar*rozmiar);i++){
            if (i==0){
                tab[i] = czyDodatnia*rand.nextInt(20);
                tab2[i] = tab[i];
            }
            else{
                tab[i] = tab[i-1] + rand.nextInt(7)+1;
                tab2[i] = tab[i];
            }
            
        }
        int temp, x, y;
        //Mieszamy drugą tablice, będzie ona wykorzystywana do przelosowania guzików
        for (int i=0;i<100;i++){
            x = rand.nextInt(rozmiar*rozmiar);
            do{
              y = rand.nextInt(rozmiar*rozmiar);
            }
            while(x==y);
            temp = tab2[x];
            tab2[x] = tab2[y];
            tab2[y] = temp;
        }
    }
}
