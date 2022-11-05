import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Main {

    private final static int SIZE = 100_000;
    static AtomicLong _3LengthStrings;
    static AtomicLong _4LengthStrings;
    static AtomicLong _5LengthStrings;
    public static String generateText(String letters, int length){
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    static void addAndGet(int type){
        switch (type){
            case (3):{
                _3LengthStrings.addAndGet(1);
            }
            case (4):{
                _4LengthStrings.addAndGet(1);
            }
            case (5):{
                _5LengthStrings.addAndGet(1);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[SIZE];
        _3LengthStrings = new AtomicLong(0);
        _4LengthStrings = new AtomicLong(0);
        _5LengthStrings = new AtomicLong(0);

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread palindromeThread = new Thread( () -> {
            for (int i = 0; i < SIZE; i++) {

                if(texts[i].equalsIgnoreCase(new StringBuilder(texts[i]).reverse().toString())){
                    addAndGet(texts[i].length());
                }
            }
        });

        Thread sameLetterThread = new Thread(() -> {
            for (int i = 0; i < SIZE; i++) {
                boolean isSame = true;
                for (int j = 0; j < texts[i].length() - 1; j++) {
                    if (texts[i].charAt(j) != texts[i].charAt(j + 1)){
                        isSame = false;
                        break;
                    }

                    if (isSame){
                        // Не имеет смысла подсчитывать, т.к. сгенерированное слово, состоящее из одной и той же буквы уже подсчитано в подсчете палиндрома
                        //addAndGet(texts[i].length());
                    }
                }
            }
        });

        Thread oneWayLettersThread = new Thread(() -> {
            for (int i = 0; i < SIZE; i++) {
                boolean isOneWay = true;
                for (int j = 0; j < texts[i].length() - 1; j++) {
                    if (texts[i].charAt(j) > texts[i].charAt(j + 1)){
                        isOneWay = false;
                        break;
                    }
                }
                if (isOneWay){
                    addAndGet(texts[i].length());
                }
            }
        });

        palindromeThread.start();
        sameLetterThread.start();
        oneWayLettersThread.start();
        palindromeThread.join();
        sameLetterThread.join();
        oneWayLettersThread.join();
        System.out.println("Красивых слов с длиной 3: " + _3LengthStrings + " шт");
        System.out.println("Красивых слов с длиной 4: " + _4LengthStrings + " шт");
        System.out.println("Красивых слов с длиной 5: " + _5LengthStrings + " шт");
    }
}
