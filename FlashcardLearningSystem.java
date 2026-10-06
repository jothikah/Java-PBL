import java.util.*;

// Interface
interface Printable {
    void printCard();
}

// Abstract Class
abstract class Flashcard implements Printable, Cloneable {

    protected String question;
    protected String answer;
    private static int totalCards = 0;

    public Flashcard(String q, String a) {
        question = q;
        answer = a;
        totalCards++;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public static int getTotalCards() {
        return totalCards;
    }

    public abstract void showType();

    public final void displayAnswer() {
        System.out.println("Answer : " + answer);
    }

    @Override
    public Flashcard clone() {
        try {
            return (Flashcard) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Question = " + question;
    }
}

// Inheritance
class JavaFlashcard extends Flashcard {

    public JavaFlashcard(String q, String a) {
        super(q, a);
    }

    @Override
    public void showType() {
        System.out.println("Category : Java");
    }

    @Override
    public void printCard() {
        System.out.println("Flashcard : " + question);
    }

    // Inner Class
    class Hint {
        void showHint() {
            System.out.println("Hint : Think about Java basics.");
        }
    }
}

// Record (Java 21)
record CardInfo(String title, int marks) {}

public class FlashcardLearningSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Flashcard> cards = new ArrayList<>();

        cards.add(new JavaFlashcard(
                "What is OOP?",
                "Object Oriented Programming"));

        cards.add(new JavaFlashcard(
                "Which keyword is used for inheritance?",
                "extends"));

        cards.add(new JavaFlashcard(
                "What is JVM?",
                "Java Virtual Machine"));

        System.out.println("===== FLASHCARD LEARNING SYSTEM =====");

        int score = 0;

        for (Flashcard card : cards) {

            card.showType();
            card.printCard();

            System.out.print("Press Enter to view answer...");
            sc.nextLine();

            card.displayAnswer();

            System.out.print("Did you know this? (yes/no): ");

            String input = sc.nextLine();

            if (input.equalsIgnoreCase("yes")) {
                score++;
            }

            System.out.println();
        }

        // Cloning
        Flashcard copy = cards.get(0).clone();

        System.out.println("Cloned Card : " + copy);

        // Inner Class
        JavaFlashcard.Hint hint =
                ((JavaFlashcard) cards.get(0)).new Hint();

        hint.showHint();

        // StringBuilder
        StringBuilder sb = new StringBuilder();
        sb.append("Score = ");
        sb.append(score);

        System.out.println(sb);

        // StringBuffer
        StringBuffer report = new StringBuffer();
        report.append("Total Cards : ");
        report.append(Flashcard.getTotalCards());

        System.out.println(report);

        // Array Example
        int[] marks = {10, 20, 30};

        System.out.print("Marks Array : ");

        for (int i : marks) {
            System.out.print(i + " ");
        }

        System.out.println();

        // Pattern Matching for switch (Java 21)
        Object obj = score;

        // String result = switch (obj) {
        //     case Integer i when i >= 2 -> "Excellent";
        //     case Integer i -> "Keep Practicing";
        //     default -> "Unknown";
        // };

        String result;

if (obj instanceof Integer i) {
    if (i >= 2) {
        result = "Excellent";
    } else {
        result = "Keep Practicing";
    }
} else {
    result = "Unknown";
}

System.out.println("Performance : " + result);

        // Record
        CardInfo info = new CardInfo("Java Quiz", 100);

        System.out.println(info);

        // Virtual Thread (Java 21)

        // Thread.startVirtualThread(() ->
        //         System.out.println("Virtual Thread Running..."));
        Thread t = new Thread(() -> {
    System.out.println("Thread Running...");
});
t.start();

        System.out.println("Program Finished.");

    }
}