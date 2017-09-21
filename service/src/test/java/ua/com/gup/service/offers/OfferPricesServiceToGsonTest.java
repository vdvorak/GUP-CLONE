package ua.com.gup.service.offers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.gup.service.offers.price.PriceOfRent;
import ua.com.gup.util.ConvertUtil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * @see http://docs.oracle.com/javase/6/docs/api/java/util/concurrent/LinkedBlockingDeque.html
 */
public class OfferPricesServiceToGsonTest {

    private final String PATH = "src/test/resources",
            RENTCALENDAR_FILE_NAME = "offerOctoberOfPrices.json",
            RESTORECALENDAR_FILE_NAME = "restoreMonthOfPrices.json",
            RESTORERENTS_FILE_NAME = "restoreRents2.json",
            RENT_FILE_NAME = "offerRents.json", //FIXME: file.properties
            jsonRestore = "{\n" +
                    "  'monthOfPrices': {\n" +
                    "    'weekday': {\n" +
                    "      'price': 10000\n" +
                    "      ,'days': ['1.11.2016','30.11.2016']\n" +
                    "    }\n" +
                    "    ,'weekend': {\n" +
                    "      'price': 15000\n" +
                    "      ,'days': ['12.11.2016','27.11.2016']\n" +
                    "    }\n" +
                    "    ,'specialdays': [\n" +
                    "      {\n" +
                    "        'price': 20000\n" +
                    "        ,'days': ['3.11.2016','7.11.2016']\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}",
            jsonRestore2 = "{\n" +
                    "  'rents': {\n" +
                    "    'availables': [\n" +
                    "      {\n" +
                    "        'day': '1478901600000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1478988000000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1479074400000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1479160800000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1479247200000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1479333600000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1479420000000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1479506400000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1479592800000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1479679200000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1479765600000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1479852000000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1479938400000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1480024800000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1480284000000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1480456800000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  ,'rented': [\n" +
                    "      {\n" +
                    "        'day': '1480111200000', 'user': {'id': '57e440464c8eda79f765532d', 'fullName': 'Петренко Юрий Владимирович', 'imgId': '57e440464c8eda79f765532d', 'rating': '10'}, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': '1478876805174', 'updateDate': '1478876916285', 'rentStatus': 'RENTED', 'orderStatus': 'SUCCESSFULLY_ORDER', 'salesRemained': '0', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1480197600000', 'user': {'id': '57e440464c8eda79f765532d', 'fullName': 'Петренко Юрий Владимирович', 'imgId': '57e440464c8eda79f765532d', 'rating': '10'}, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': '1478876805177', 'updateDate': '1478876916288', 'rentStatus': 'RENTED', 'orderStatus': 'SUCCESSFULLY_ORDER', 'salesRemained': '0', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1480370400000', 'user': {'id': '57e440464c8eda79f765532d', 'fullName': 'Петренко Юрий Владимирович', 'imgId': '57e440464c8eda79f765532d', 'rating': '10'}, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': '1478876805177', 'updateDate': '1478876916288', 'rentStatus': 'RENTED', 'orderStatus': 'SUCCESSFULLY_ORDER', 'salesRemained': '0', 'order': null\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  ,'expired': [\n" +
                    "      {\n" +
                    "        'day': '1477951200000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1478037600000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1478124000000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1478210400000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1478296800000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1478383200000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1478469600000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1478556000000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1478642400000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1478728800000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      },{\n" +
                    "        'day': '1478815200000', 'user': null, 'confirm': true, 'prepaid': true, 'dayPrepaid': null, 'orderDate': null, 'updateDate': null, 'rentStatus': 'AVAILABLE', 'orderStatus': 'NONE', 'salesRemained': '1', 'order': null\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}";

    private JsonObject jsonPriceRents,objJsonRestore,jsonRents;

    private Map<String, PriceOfRent> priceRents; //TODO: ПравилА будут хранится в базе (из низ потом будет строиться объект-календаря с ценой за все дни...)
    private Map<String, RentTest> rents;         //TODO: информация об состоянии аренды для клиентов...будут хранится в базе
    private Map<String, RentsRestore> restore2;
    private OfferPricesService service, service2;

    @Before
    public void setUp() {
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        try {
            jsonPriceRents = (JsonObject) parser.parse(new FileReader(PATH + "/" + RENTCALENDAR_FILE_NAME));
            jsonRents = (JsonObject) parser.parse(new FileReader(PATH + "/" + RENT_FILE_NAME));
            objJsonRestore = (JsonObject) parser.parse(new FileReader(PATH + "/" + RESTORECALENDAR_FILE_NAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        priceRents = gson.fromJson(jsonPriceRents, new TypeToken<Map<String, PriceOfRent>>(){}.getType());
        rents = gson.fromJson(jsonRents, new TypeToken<Map<String, RentTest>>(){}.getType());
        restore2 = gson.fromJson(objJsonRestore, new TypeToken<Map<String, RentsRestore>>(){}.getType());

        service = new OfferPricesServiceImpl(10000l,15000l); // Устанавливаем цену по умолчанию (на будни и выходные дни)
    }

    @After
    public void tearDown() {
        jsonPriceRents = null;
        jsonRents = null;
        priceRents = null;
        rents = null;
        service = null;
        service2 = null;
    }

//    @Test
//    public void testContains(){
//        System.out.println("--------------------[ testContains (Rent) ]");
//        service.addPrices(priceRents.get("scheme4").getPrice(), ConvertUtil.toDate(priceRents.get("scheme4").getDays()));
//        service.addRent(ConvertUtil.toDate(rents.get("rent51").getDays()), "57e440464c8eda79f765532d");
//        service.addRent(ConvertUtil.toDate(rents.get("rent52").getDays()), "57e440464c8eda79f765532d");
//
//        System.out.println(service.toJson());
//        System.out.println(service);
//        System.err.println(service.toRent());
//        System.out.println(service.jsonRent());
//
////        System.out.println( Arrays.toString( service.toArray() ) );
//    }
//
//    @Test
//    public void testAdd(){
//        System.out.println("--------------------[ testAdd (Rents) ]");
//        service.addPrices(priceRents.get("scheme4").getPrice(), ConvertUtil.toDate(priceRents.get("scheme4").getDays()));
//        service.addRent(ConvertUtil.toDate(rents.get("rent51").getDays()), "57e440464c8eda79f765532d");
//        service.addRent(ConvertUtil.toDate(rents.get("rent52").getDays()), "57e440464c8eda79f765532d");
//
//        System.out.println(service.toJson());
////        System.out.println(service.jsonRent());
//////        System.out.println(service.jsonOfferMonth());
//    }
//
//
//
//
//
//
//
//    @Test
//    public void testInit(){
//        System.out.println("--------------------[ testInit (Availables) ]");
//        service.addPrices(priceRents.get("scheme4").getPrice(), ConvertUtil.toDate(priceRents.get("scheme4").getDays()));
//
//        System.out.println(service.toRent());
//    }

//    /**
//     * Data to output use json-format
//     */
//    @Test
//    public void testRented(){
//        System.out.println("--------------------[ testRented (s) ]");
//        service.addPrices(priceRents.get("scheme4").getPrice(), ConvertUtil.toDate(priceRents.get("scheme4").getDays()));
//        service.addRent(ConvertUtil.toDate(rents.get("rent51").getDays()), "57e440464c8eda79f765532d");
//        service.addRent(ConvertUtil.toDate(rents.get("rent52").getDays()), "57e440464c8eda79f765532d");
//
////        System.out.println(service.toRent());
////        System.out.println();
//        System.out.println(service);
//        System.out.println();
////        System.out.println(service.toJson());
////        System.out.println();
//        System.out.println(service.jsonRent());
//    }

//    @Test
//    public void testIsRented(){
//        System.out.println("--------------------[ testIsRented ]");
//        service.addPrices(priceRents.get("scheme4").getPrice(), ConvertUtil.toDate(priceRents.get("scheme4").getDays()));
//        service.addRent(ConvertUtil.toDate(rents.get("rent51").getDays()), "57e440464c8eda79f765532d");
//        service.addRent(ConvertUtil.toDate(rents.get("rent52").getDays()), "57e440464c8eda79f765532d");
//
//        System.out.println("IsRented [05.11.2016] = " + service.isRent(ConvertUtil.toDate(rents.get("delete41").getDays())[0]) + " (false)");
//        System.out.println("IsRented [16.11.2016,31.11.2016] = " + service.isRent(ConvertUtil.toDate(rents.get("notfound").getDays())) + " (false)");
//        System.out.println("IsRented [26.11.2016] = " + service.isRent(ConvertUtil.toDate(rents.get("rent51").getDays())[0]) + " (true)");
//        System.out.println("IsRented [28.11.2016,30.11.2016] = " + service.isRent(ConvertUtil.toDate(rents.get("rent52").getDays())) + " (true)");
//        System.out.println(service.toRent());
//    }
//
//    @Test
//    public void testDel0(){
//        System.out.println("--------------------[ testDel (0) ]");
//        service.addPrices(priceRents.get("scheme4").getPrice(), ConvertUtil.toDate(priceRents.get("scheme4").getDays()));
//        service.addRent(ConvertUtil.toDate(rents.get("delete41").getDays()), "57e440464c8eda79f765532d");
//        service.addRent(ConvertUtil.toDate(rents.get("delete42").getDays()), "57e440464c8eda79f765532d");
//
//        System.out.println("Del [1.11.2016] = " + service.delRent(ConvertUtil.toDate(rents.get("rent41").getDays()))); //FIXME: проблеа конвертара с невалидными числами в месяцк...
//        System.out.println(service.toRent());
//    }
//
//    @Test
//    public void testDel1(){
//        System.out.println("--------------------[ testDel (1) ]");
//        service.addPrices(priceRents.get("scheme4").getPrice(), ConvertUtil.toDate(priceRents.get("scheme4").getDays()));
//        service.addRent(ConvertUtil.toDate(rents.get("rent51").getDays()), "57e440464c8eda79f765532d");
//        service.addRent(ConvertUtil.toDate(rents.get("delete42").getDays()), "57e440464c8eda79f765532d");
//
//        System.out.println("Del [26.11.2016] = " + service.delRent(ConvertUtil.toDate(rents.get("rent51").getDays())));
//        System.out.println(service.toRent());
//    }
//
//    @Test
//    public void testDel2(){
//        System.out.println("--------------------[ testDel (2) ]");
//        service.addPrices(priceRents.get("scheme4").getPrice(), ConvertUtil.toDate(priceRents.get("scheme4").getDays()));
//        service.addRent(ConvertUtil.toDate(rents.get("rent51").getDays()), "57e440464c8eda79f765532d");
//        service.addRent(ConvertUtil.toDate(rents.get("delete42").getDays()), "57e440464c8eda79f765532d");
//
//        System.out.println("Del [12.11.2016,14.11.2016] = " + service.delRent(ConvertUtil.toDate(rents.get("delete42").getDays())));
//        System.out.println(service.toRent());
//    }
//
//    @Test
//    public void testAddDelAdd(){
//        System.out.println("--------------------[ testAddDelAdd ]");
//        service.addPrices(priceRents.get("scheme4").getPrice(), ConvertUtil.toDate(priceRents.get("scheme4").getDays()));
//        service.addRent(ConvertUtil.toDate(rents.get("rent51").getDays()), "57e440464c8eda79f765532d");   //[+] 26.11.2016
//        service.addRent(ConvertUtil.toDate(rents.get("delete42").getDays()), "57e440464c8eda79f765532d"); //[+] 12.11.2016,14.11.2016
//
//        service.delRent(ConvertUtil.toDate(rents.get("delete42").getDays())); //[-] 12.11.2016,14.11.2016
//        service.addRent(ConvertUtil.toDate(rents.get("rent4").getDays()), "57e440464c8eda79f765532d");    //[+] 30.11.2016
//
//        System.out.println(service.toRent());                                 //[=] 26.11.2016,30.11.2016
//    }
//
//
//
//
//
//
//
//    /**
//     * @see https://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/ArrayUtils.html
//     */
//    @Test
//    public void testExpired(){
//        System.out.println("--------------------[ testExpired ]");
//        service.addPrices(priceRents.get("scheme4").getPrice(), ConvertUtil.toDate(priceRents.get("scheme4").getDays()));
//        service.addRent(ConvertUtil.toDate(rents.get("rent51").getDays()), "57e440464c8eda79f765532d"); //[+] 26.11.2016
//        service.addRent(ConvertUtil.toDate(rents.get("rent52").getDays()), "57e440464c8eda79f765532d"); //[+] 27.11.2016,29.11.2016
//        //
//        service.delRent(ConvertUtil.toDate(rents.get("rent52").getDays())); //[-] 27.11.2016,29.11.2016
//        service.addRent(ConvertUtil.toDate(rents.get("rent4").getDays()), "57e440464c8eda79f765532d");  //[+] 30.11.2016
//        // 21.11.2016
//        System.out.println( "Availables: " + service.getRents().getAvailables() ); //[-] 26.11.2016,30.11.2016
//        System.out.println( "    Rented: " + service.getRents().getRented() );     //[+] 26.11.2016,30.11.2016
//        System.err.println("   Expired: " + service.getRents().getExpired());      //[=] 01.11.2016,...,08.11.2016
//        System.err.println("   Expired: " + service.getRents().getExpired());      //[=] 01.11.2016,...,08.11.2016
////        System.err.println( "Availables: " + service.getRents().getAvailables() ); //[-] 26.11.2016,30.11.2016
////        System.err.println( "    Rented: " + service.getRents().getRented() );     //[+] 26.11.2016,30.11.2016
//        System.out.println();
//        System.out.println(service.toRent());                                      //[=] 30.11.2016
//        System.out.println();
//        System.err.println(service);
//    }
//
//    @Test
//    public void testExpired2(){
//        System.out.println("--------------------[ testExpired (2) ]");
//        service.addPrices(priceRents.get("scheme4").getPrice(), ConvertUtil.toDate(priceRents.get("scheme4").getDays()));
//        service.addRent(ConvertUtil.toDate(rents.get("rent51").getDays()), "57e440464c8eda79f765532d"); //[+] 26.11.2016
//        service.addRent(ConvertUtil.toDate(rents.get("rent52").getDays()), "57e440464c8eda79f765532d"); //[+] 28.11.2016,30.11.2016
//        //
//        service.delRent(ConvertUtil.toDate(rents.get("rent52").getDays())); //[-] 28.11.2016,30.11.2016
//        service.addRent(ConvertUtil.toDate(rents.get("rent4").getDays()), "57e440464c8eda79f765532d");  //[+] 31.11.2016
//        // 21.10.2016
//        System.out.println( "   Expired: " + service.getRents().getExpired() );
//        System.out.println( "Availables: " + service.getRents().getAvailables() );
//        System.out.println( "    Rented: " + service.getRents().getRented() );
//        System.out.println();
//        System.out.println(service.toJson());
//        System.out.println();
//        System.out.println(service.jsonRent());
//    }

//    @Test
//    public void testRestore(){
//        System.out.println("--------------------[ testRestore ]");
//        service2 = new OfferPricesServiceImpl(jsonRestore, jsonRestore2);
//        System.err.println(service2);
//        System.err.println();
////        System.err.println(service2.toRent());
////        System.err.println();
////        System.out.println(service2.toJson());
////        System.out.println();
//        System.out.println(service2.jsonRent());
//    }

//    @Test
//    public void testRestore2(){
//        System.err.println("--------------------[ testRestore (2) ]");
//        service2 = new OfferPricesServiceImpl(jsonRestore, jsonRestore2);
//
////        System.err.println(service2);
//        System.err.println(service2.toJson());
////        System.err.println();
////        System.err.println(service2.jsonRent());
//
//        service2.addRent(ConvertUtil.toDate(rents.get("delete42").getDays()), "57e440464c8eda79f765532d"); //[+] 12.11.2016,14.11.2016
////        System.err.println(service2.jsonRent());
////        System.err.println();
//        service2.delRent(ConvertUtil.toDate(rents.get("rent51").getDays()));                               //[-] 26.11.2016
//        System.err.println(service2.jsonRent());
//    }

    @Test
    public void testRestore3(){
        System.out.println("--------------------[ testRestore (3) ]");

        service2 = new OfferPricesServiceImpl(jsonRestore, jsonRestore2);
        service2.addRent(ConvertUtil.toDate(rents.get("delete42").getDays()), "57e440464c8eda79f765532d"); //[+] 12.11.2016,14.11.2016
        service2.delRent(ConvertUtil.toDate(rents.get("rent51").getDays()));                               //[-] 26.11.2016
        System.out.println(service2.toJsonFull());
    }
}