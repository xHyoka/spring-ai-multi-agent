package dev.tunahan.spring_ai;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
public class CityToolsTest {

    private final CityTools cityTools = new CityTools();

    @Test
    void bilinenSehrinNufusunuDoner() {
        String sonuc = cityTools.cityPopulation("izmir");
        assertThat(sonuc).isEqualTo("4400000");
    }

    @Test

    void turkceKarakterVeBuyukHarfiNormallestirir() {
        String sonuc = cityTools.cityPopulation("İSTANBUL");
        assertThat(sonuc).isEqualTo("15800000");
    }

    void basVeSondakiBosluklariTemizler() {
        String sonuc = cityTools.cityPopulation("  ankara  ");
        assertThat(sonuc).isEqualTo("5700000");
    }

    @Test
    void bilinmeyenSehirIcinVeriYokDoner(){
        String sonuc = cityTools.cityPopulation("Bursa");
        assertThat(sonuc).startsWith("Bu şehir için veri yok");
    }

    @Test
    void yuzolcumunuBirimiyleDoner() {
        String sonuc = cityTools.cityArea("Ankara");

        assertThat(sonuc).contains("25632").contains("km");
    }
}
