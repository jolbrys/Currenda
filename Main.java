package com.company;

import jdk.internal.org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate)
    {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate) || calendar.getTime().equals(enddate))
        {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static String makeLink(String _currency_tag, String _start_date, String _end_date){
        String link_params;
        link_params = "http://api.nbp.pl/api/exchangerates/rates/a/" + _currency_tag + "/" + _start_date + "/" + _end_date + "/?format=xml";
        return link_params;
    }

    public static String makeLink(String _currency_tag, String _start_date){
        String link_params;
        link_params = "http://api.nbp.pl/api/exchangerates/rates/a/" + _currency_tag + "/" + _start_date + "/?format=xml";
        return link_params;
    }

    public static String calculateMidRate(String _api_link) throws IOException, org.xml.sax.SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(_api_link);  //("http://api.nbp.pl/api/exchangerates/rates/a/EUR/2017-10-11/2017-10-15/?format=xml");

        Element root = doc.getDocumentElement();
        String midrate = root.getElementsByTagName("Mid").item(0).getTextContent();
        return midrate;
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, org.xml.sax.SAXException, XPathExpressionException, ParseException {


        //dane
        String currency_tag;
        String start_date;
        String end_date;
        String api_link;
        String midrate;
        Scanner input = new Scanner(System.in);
        List<Double> rates = new ArrayList<Double>();
        Double std_var_result;


        //przygotowywanie zapytania dla api nbp

        System.out.println("Podaj kod waluty");
        currency_tag = input.next();
        System.out.println("Podaj datę początkową. Pamiętaj, że w weekendy NBP nie publikuje kursów walut.");
        start_date = input.next();
        System.out.println("Podaj datę końcową.");
        end_date = input.next();
        api_link = makeLink(currency_tag, start_date, end_date);

        //obliczanie kursu

        midrate = calculateMidRate(api_link);
        System.out.println("kurs: " + midrate);

        //odchylenie standardowe
        //arraylist z datami w formacie Date
        List<Date> dates;
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        dates = getDaysBetweenDates(df.parse(start_date), df.parse(end_date));

//
        //formatowanie daty do stringa i zapełnianie listy rates kursami walut w przedziale czasu zadanym w liscie dates

        for(int i=0;i<dates.size();i++) {
            String str_date = df.format(dates.get(i));
            //System.out.println(str_date);
            //Arrays.toString(dates.toArray());
            String tempratestr = calculateMidRate(makeLink(currency_tag, str_date));
            Double tempratedb = Double.parseDouble(tempratestr);

            //System.out.println(tempratedb);
            rates.add(tempratedb);
        }

//
        for(int i=0;i<rates.size();i++){
            //System.out.println(rates.get(i));
        }

        StandardDeviation std_var = new StandardDeviation((ArrayList<Double>) rates, rates.size());
        std_var_result = std_var.getStdDev();
        System.out.println("odchylenie standardowe: " + std_var_result);


    }

}
