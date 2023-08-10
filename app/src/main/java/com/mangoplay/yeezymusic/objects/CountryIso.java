package com.mangoplay.yeezymusic.objects;

import java.util.HashMap;
import java.util.Map;

public class CountryIso {

    static Map<String, String> map = new HashMap<String, String>();

    public static void setMap(){
        map.put("RO", "1279117071");
        map.put("US", "1313621735");
        map.put("BR", "1111141961");
        map.put("GB", "1111142221");
        map.put("DE", "1111143121");
        map.put("FR", "1109890291");
        map.put("CO", "1116188451");
        map.put("CA", "1652248171");
        map.put("MX", "1111142361");
        map.put("NL", "1266971851");
        map.put("ZA", "1362528775");
        map.put("JM", "1362508575");
        map.put("VE", "1362527605");
        map.put("UA", "1362526495");
        map.put("TN", "1362525375");
        map.put("TH", "1362524475");
        map.put("SV", "1362523615");
        map.put("SN", "1362523075");
        map.put("SI", "1362522355");
        map.put("SA", "1362521285");
        map.put("PY", "1362520135");
        map.put("PT", "1362519755");
        map.put("PH", "1362518895");
        map.put("PE", "1362518525");
        map.put("MY", "1362515675");
        map.put("MA", "1362512715");
        map.put("LB", "1362511155");
        map.put("KR", "1362510315");
        map.put("JP", "1362508955");
        map.put("JO", "1362508765");
        map.put("IL", "1362507345");
        map.put("HU", "1362506695");
        map.put("EG", "1362501615");
        map.put("EC", "1362501235");
        map.put("DZ", "1362501015");
        map.put("BO", "1362495515");
        map.put("BG", "1362494565");
        map.put("AE", "1362491345");
        map.put("SG", "1313620765");
        map.put("SE", "1313620305");
        map.put("ES", "1116190041");
        map.put("NO", "1313619885");
        map.put("IE", "1313619455");
        map.put("DK", "1313618905");
        map.put("CR", "1313618455");
        map.put("CH", "1313617925");
        map.put("BE", "1266968331");
        map.put("AU", "1313616925");
        map.put("TR", "1116189071");
        map.put("RU", "1116189381");
        map.put("NG", "1362516565");
        map.put("AT", "1313615765");
        map.put("AR", "1279119721");
        map.put("ID", "1116188761");
        map.put("IT", "1116187241");
        map.put("CL", "1279119121");
        map.put("GT", "1279118671");
        map.put("SK", "1266973701");
        map.put("RS", "1266972981");
        map.put("KE", "1362509215");
        map.put("PL", "1266972311");
        map.put("HR", "1266971131");
        map.put("CZ", "1266969571");
        map.put("LV", "1221037511");
        map.put("LT", "1221037371");
        map.put("EE", "1221037201");
        map.put("FI", "1221034071");
        map.put("HN", "1116190301");
        map.put("CI", "1362497945");
    }

    public static String getPlaylistId(String countryIso){
        if(map.size() == 0) setMap();
        String id = null;
        for (Map.Entry<String, String> element : map.entrySet()) {
            if(element.getKey().equals(countryIso)) {
                id = element.getValue();
                break;
            }
        }
        return id;
    }
}
