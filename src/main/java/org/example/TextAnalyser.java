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
    private static String letters = "abc";
    private static int length = 100_000;
    private static int numbtexts = 10_000;

    public static void main(String[] args) {


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
            countChar(queueA, "a", maxTextA);
        });

        // Создаем поток для анализа символа 'b'
        Thread threadB = new Thread(() -> {
            countChar(queueB, "b", maxTextB);
        });

        // Создаем поток для анализа символа 'c'
        Thread threadC = new Thread(() -> {
            countChar(queueC, "c", maxTextC);
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

    public static void countChar(ArrayBlockingQueue<String> queue, String ch, AtomicReference<String> maxText) {
        int maxCount = 0;
        for (int i = 0; i < numbtexts; i++) {
            try {
                String text = queue.take();
                int count = text.length() - text.replace(ch, "").length();
                if (count > maxCount) {
                    maxCount = count;
                    maxText.set(text);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
