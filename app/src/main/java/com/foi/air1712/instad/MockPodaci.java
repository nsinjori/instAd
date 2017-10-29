package com.foi.air1712.instad;

import com.foi.air1712.database.Dogadaji;

/**
 * Created by Nikola on 29.10.2017..
 */

public class MockPodaci {
    public static void writeAll(){
        Dogadaji dogadaj1 = new Dogadaji();
        dogadaj1.setAjdi(5);
        dogadaj1.setAdresa("Zadar");
        dogadaj1.setObjekt("orgulje");
        dogadaj1.save();

        Dogadaji dogadaj2 = new Dogadaji();
        dogadaj2.setAjdi(8);
        dogadaj2.setAdresa("Varazdin");
        dogadaj2.setObjekt("foi");
        dogadaj2.save();

        Dogadaji dogadaj3 = new Dogadaji();
        dogadaj3.setAjdi(8);
        dogadaj3.setAdresa("Zagreb");
        dogadaj3.setObjekt("studio smijeha");
        dogadaj3.save();

    }
}
