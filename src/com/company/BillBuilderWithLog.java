package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.*;

public class BillBuilderWithLog {

    public static void main(String[] args) throws IOException {

        Logger logger = Logger.getLogger(BillBuilderWithLog.class.getName());
        FileHandler fh;

        try {

            fh = new FileHandler("common.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Подготовка структур в памяти");

        ArrayList<String> productNames = new ArrayList<String>();
        ArrayList<Float> productQuantity = new ArrayList<Float>();
        ArrayList<Float> productPrice = new ArrayList<Float>();

        String inputLine;

        BufferedReader reader;
        // Читаем исходный файл
        logger.info("Чтение исходного файла");

        try {
            reader = new BufferedReader(new FileReader(
                    "./products.txt"
            ));

            logger.log(Level.FINE, "Файл найден, осуществляем построчное чтение");

            // Контроль целостности данных (порядка следования строк) не производится
            do {
                // Читаем блоками по три строки (согласно ТЗ)
                inputLine = reader.readLine();
                // Первая строка - ожидаем название товара
                if (inputLine != null) {
                    //System.out.println(inputLine);
                    productNames.add(inputLine);
                    logger.log(Level.INFO, "Название товара = " + inputLine);
                } else break;

                // Вторая строка - ожидаем получить кол-во товара
                inputLine = reader.readLine();
                if (inputLine != null) {
                    productQuantity.add(Float.parseFloat(inputLine));
                    logger.log(Level.INFO, "  кол-во = " + inputLine);
                }  else {
                    logger.log(Level.WARNING, "Не найдена строка кол-ва товара");
                }

                // Третья строка - ожидаем получить цену товара
                inputLine = reader.readLine();
                if (inputLine != null) {
                    productPrice.add(Float.parseFloat(inputLine));
                    logger.log(Level.INFO, "  цена товара = " + inputLine);
                }  else {
                    logger.log(Level.WARNING, "Не найдена строка цены товара");
                }

            } while(inputLine != null);

            reader.close();

            logger.log(Level.INFO, "Осуществляем расчет чека");

            System.out.println("Наименование        Цена   Кол-во    Стоимость\n" +
                    "===============================================");
            // Открываем цикл по импортированным из файла записям
            Iterator<String> pName = productNames.iterator();
            Iterator<Float> pQuantity = productQuantity.iterator();
            Iterator<Float> pPrice = productPrice.iterator();

            Double total = 0.0;
            while (pName.hasNext()) {
                Float price = pPrice.next();
                Float quant = pQuantity.next();
                Float summ = price * quant;
                total += summ;

                System.out.printf("%-18s %6.2f x %6.3f  =%8.2f\n", pName.next(), price, quant, summ);
            }

            System.out.println("===============================================");
            System.out.printf("Итого:                              =%8.2f\n", total);

        } catch (IOException e) {
            logger.log(Level.SEVERE,"Ошибка чтения файла продуктов: " + e.toString());

            e.printStackTrace();
        }

    }
}