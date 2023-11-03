package org.example;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class TextAnalyser {
    // Создаем блокирующие очереди для символов 'a', 'b' и 'c'
    private static ArrayBlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    private static ArrayBlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    private static ArrayBlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {
        // Создаем поток для генерации текстов
        Thread textGeneratorThread = new Thread(() -> {
            String letters = "abc";
            int length = 100000;

            Random random = new Random();
            for (int i = 0; i < 10000; i++) {
                StringBuilder text = new StringBuilder();
                for (int j = 0; j < length; j++) {
                    text.append(letters.charAt(random.nextInt(letters.length())));
                }

                // Помещаем сгенерированный текст в соответствующую очередь
                try {
                    for (char c : text.toString().toCharArray()) {
                        if (c == 'a') {
                            queueA.put(text.toString());
                        } else if (c == 'b') {
                            queueB.put(text.toString());
                        } else if (c == 'c') {
                            queueC.put(text.toString());
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Создаем поток для анализа символа 'a'
        Thread threadA = new Thread(() -> {
            int maxCountA = 0;
            String maxTextA = "";

            while (true) {
                try {
                    String text = queueA.take();
                    int countA = text.length() - text.replace("a", "").length();
                    if (countA > maxCountA) {
                        maxCountA = countA;
                        maxTextA = text;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Создаем поток для анализа символа 'b'
        Thread threadB = new Thread(() -> {
            int maxCountB = 0;
            String maxTextB = "";

            while (true) {
                try {
                    String text = queueB.take();
                    int countB = text.length() - text.replace("b", "").length();
                    if (countB > maxCountB) {
                        maxCountB = countB;
                        maxTextB = text;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Создаем поток для анализа символа 'c'
        Thread threadC = new Thread(() -> {
            int maxCountC = 0;
            String maxTextC = "";

            while (true) {
                try {
                    String text = queueC.take();
                    int countC = text.length() - text.replace("c", "").length();
                    if (countC > maxCountC) {
                        maxCountC = countC;
                        maxTextC = text;
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
    }
}