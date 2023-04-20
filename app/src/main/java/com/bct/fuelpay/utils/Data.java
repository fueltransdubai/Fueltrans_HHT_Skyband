package com.bct.fuelpay.utils;

import com.google.gson.JsonArray;

public class Data {
    public static String cSTore = "{\n" +
            "    \"Lubricants\": [\n" +
            "      {\"name\": \"Rotella T6\", \"quantity\": \"1.5 LTR\", \"price\": \"512.00\"},\n" +
            "      {\"name\": \"Petron REV-X\", \"quantity\": \"1.5 LTR\", \"price\": \"950.00\"},\n" +
            "      {\n" +
            "        \"name\": \"Toyota Full Synthetic 5W40\",\n" +
            "        \"quantity\": \"1 LTR\",\n" +
            "        \"price\": \"330.00\"\n" +
            "      },\n" +
            "      {\"name\": \"PETRON REV-X RX800\", \"quantity\": \"1 LTR\", \"price\": \"555.00\"},\n" +
            "      {\n" +
            "        \"name\": \"Shell Advance AX7 10W-40\",\n" +
            "        \"quantity\": \"1 LTR\",\n" +
            "        \"price\": \"340.00\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"TOYOTA Engine Oil 5W-30\",\n" +
            "        \"quantity\": \"7.5 LTR\",\n" +
            "        \"price\": \"4600.00\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"SHELL ADVANCE 15W-40\",\n" +
            "        \"quantity\": \"7.5 LTR\",\n" +
            "        \"price\": \"250.00\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"PETRON Blaze Racing BR 830\",\n" +
            "        \"quantity\": \"1 LTR\",\n" +
            "        \"price\": \"679.00\"\n" +
            "      },\n" +
            "      {\"name\": \"Mobil Super 3000 5w30\", \"quantity\": \"1 LTR\", \"price\": \"652.00\"},\n" +
            "      {\"name\": \"Mitsubishi 4L 5W-40\", \"quantity\": \"1 LTR\", \"price\": \"1555.00\"},\n" +
            "      {\n" +
            "        \"name\": \"YAMALUBE MOTORCYCLE OIL 20W-40\",\n" +
            "        \"quantity\": \"800 Ml\",\n" +
            "        \"price\": \"250.00\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Caltex Havoline Synthetic 10W40\",\n" +
            "        \"quantity\": \"4 LTR\",\n" +
            "        \"price\": \"1963.00\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"MOTUL SCOOTER 4T 10W-40\",\n" +
            "        \"quantity\": \"800 Ml\",\n" +
            "        \"price\": \"330.00\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"Castrol Magnatec 10W-40\",\n" +
            "        \"quantity\": \"4 LTR\",\n" +
            "        \"price\": \"1750.00\"\n" +
            "      },\n" +
            "      {\"name\": \"Honda SN 0W-20\", \"quantity\": \"4 LTR\", \"price\": \"2450.00\"}\n" +
            "    ],\n" +
            "    \"Beverages\": [\n" +
            "      {\"name\": \"Mountain Dew Drink\", \"quantity\": \"1.5 LTR\", \"price\": \"66.00\"},\n" +
            "      {\"name\": \"Royal true orange\", \"quantity\": \"1.5 LTR\", \"price\": \"70.50\"},\n" +
            "      {\"name\": \"7UP\", \"quantity\": \"1.5 LTR\", \"price\": \"65.00\"},\n" +
            "      {\n" +
            "        \"name\": \"Green Cola Carbonated\",\n" +
            "        \"quantity\": \"6 CANS\",\n" +
            "        \"price\": \"185.00\"\n" +
            "      },\n" +
            "      {\"name\": \"Schweppes Tonic Water\", \"quantity\": \"320 ml\", \"price\": \"30.00\"},\n" +
            "      {\"name\": \"Schweppes Cream Soda\", \"quantity\": \"330 ml\", \"price\": \"60.00\"},\n" +
            "      {\"name\": \"Rite 'N Lite Cucumber\", \"quantity\": \"330 ml\", \"price\": \"24.50\"},\n" +
            "      {\"name\": \"Coca Cola\", \"quantity\": \"1.5 LTR\", \"price\": \"75.00\"},\n" +
            "      {\"name\": \"Taiwan Apple Sidra\", \"quantity\": \"330 ml\", \"price\": \"40.00\"},\n" +
            "      {\"name\": \"Mirinda Orange\", \"quantity\": \"2 LTR\", \"price\": \"73.00\"},\n" +
            "      {\"name\": \"IBC Cream Soda\", \"quantity\": \"330 ml\", \"price\": \"115.00\"},\n" +
            "      {\"name\": \"Lotte Milkis Original\", \"quantity\": \"250 ml\", \"price\": \"33.00\"} \n" +
            "      \n" +
            "    ],\n" +
            "    \"Snacks\": [\n" +
            "      {\"name\": \"Lotte Choco Biscuits\", \"quantity\": \"102 g\", \"price\": \"115.00\"},\n" +
            "      {\"name\": \"Magnolia Pancake\", \"quantity\": \"400 g\", \"price\": \"58.00\"},\n" +
            "      {\"name\": \"FERNA WAFFLE MIX\", \"quantity\": \"1 Kg\", \"price\": \"235.00\"},\n" +
            "      {\"name\": \"JIMS FUDGEE BARR\", \"quantity\": \"500 g\", \"price\": \"71.00\"},\n" +
            "      {\n" +
            "        \"name\": \"Quaker Instant Oatmeal\",\n" +
            "        \"quantity\": \"800 g\",\n" +
            "        \"price\": \"116.00\"\n" +
            "      },\n" +
            "      {\"name\": \"Kellogg's Special K\", \"quantity\": \"370 g\", \"price\": \"279.00\"},\n" +
            "      {\n" +
            "        \"name\": \"MILO Breakfast Cereals\",\n" +
            "        \"quantity\": \"330 g\",\n" +
            "        \"price\": \"190.00\"\n" +
            "      },\n" +
            "      {\"name\": \"PILI PEANUT BUTTER\", \"quantity\": \"330 g\", \"price\": \"150.00\"},\n" +
            "      {\"name\": \"Mixed Fruit Oatmeal\", \"quantity\": \"500 g\", \"price\": \"42.00\"},\n" +
            "      {\"name\": \"Lotus Biscoff Spread\", \"quantity\": \"380 g\", \"price\": \"369.00\"}\n" +
            "    ]\n" +
            "  }";
}
