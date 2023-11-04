package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class TextAnalyser {
    // Создаем блокирующие очереди для символов 'a', 'b' и 'c'
    private static ArrayBlockingQueue<String> queueA = new ArrayBlockingQueue<>(10);
    private static ArrayBlockingQueue<String> queueB = new ArrayBlockingQueue<>(10);
    private static ArrayBlockingQueue<String> queueC = new ArrayBlockingQueue<>(10);

    private static AtomicReference<String> maxTextA = new AtomicReference<>("");
    private static AtomicReference<String> maxTextB = new AtomicReference<>("");
    private static AtomicReference<String> maxTextC = new AtomicReference<>("");

    public static void main(String[] args) {

        String letters = "abc";
        int length = 100_000;
        int numbtexts = 10_000;

        // Создаем поток для генерации текстов
        Thread textGeneratorThread = new Thread(() -> {

            Random random = new Random();
            for (int i = 0; i < numbtexts; i++) {
                StringBuilder text = new StringBuilder();
                for (int j = 0; j < length; j++) {
                    text.append(letters.charAt(random.nextInt(letters.length())));
                }

                // Помещаем сгенерированный текст в соответствующую очередь
                try {
                    // Поменял добавление в очередь без проверки
                    queueA.put(String.valueOf(text));
                    queueB.put(String.valueOf(text));
                    queueC.put(String.valueOf(text));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Создаем поток для анализа символа 'a'
        Thread threadA = new Thread(() -> {
            int maxCountA = 0;

            for (int i = 0; i < numbtexts; i++) {
                try {
                    String text = queueA.take();
                    int countA = text.length() - text.replace("a", "").length();
                    if (countA > maxCountA) {
                        maxCountA = countA;
                        maxTextA.set(text);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Создаем поток для анализа символа 'b'
        Thread threadB = new Thread(() -> {
            int maxCountB = 0;

            for (int i = 0; i < numbtexts; i++) {
                try {
                    String text = queueB.take();
                    int countB = text.length() - text.replace("b", "").length();
                    if (countB > maxCountB) {
                        maxCountB = countB;
                        maxTextB.set(text);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Создаем поток для анализа символа 'c'
        Thread threadC = new Thread(() -> {
            int maxCountC = 0;

            for (int i = 0; i < numbtexts; i++) {
                try {
                    String text = queueC.take();
                    int countC = text.length() - text.replace("c", "").length();
                    if (countC > maxCountC) {
                        maxCountC = countC;
                        maxTextC.set(text);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Запускаем все потоки
        textGeneratorThread.start();
        threadA.start();
        threadB.start();
        threadC.start();

        try {
            textGeneratorThread.join();
            threadA.join();
            threadB.join();
            threadC.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Текст, в котором содержится максимальное количество символов 'a' : " + maxTextA);
        System.out.println("Текст, в котором содержится максимальное количество символов 'b' : " + maxTextB);
        System.out.println("Текст, в котором содержится максимальное количество символов 'c' : " + maxTextC);
    }
}