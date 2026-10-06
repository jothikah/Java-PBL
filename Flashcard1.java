import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Flashcard Learning System
 * Demonstrates:
 * - OOP
 * - Encapsulation
 * - Abstraction
 * - Inheritance
 * - Polymorphism
 * - Interfaces
 * - Object Cloning
 * - Inner Classes
 * - ArrayList
 * - StringBuilder
 * - StringBuffer
 */

//interface
interface Printable {
    void printCard();
}
class InvalidAnswerException extends Exception {

    public InvalidAnswerException(String message) {
        super(message);
    }
}
//---------------------------------------------
//abstract class
abstract class Flashcard implements Printable, Cloneable {

    protected String question;
    protected String answer;
    protected String difficulty;
    protected String category;
//encaptulation
    private static int totalCards = 0;

    public Flashcard(String q,
                     String a,
                     String d,
                     String c) {

        question = q;
        answer = a;
        difficulty = d;
        category = c;

        totalCards++;
    }
//getter method
    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }

    public static int getTotalCards() {
        return totalCards;
    }

    public abstract void showType();

    public final void displayAnswer() {
        System.out.println("Answer : " + answer);
    }
//method overriding
    @Override
    //exception handling
    public Flashcard clone() {
        try {
            return (Flashcard) super.clone();
        }
        catch(CloneNotSupportedException e) {
            return null;
        }
    }
//method overriding
    @Override
    public String toString() {
        return question;
    }

}

//=============================================
// INTER-THREAD COMMUNICATION
//=============================================

class QuizCommunication {

    private boolean answerReady = false;

    public synchronized void waitForAnswer() {

        try {

            while (!answerReady) {

                System.out.println(
                        "Quiz processor waiting for answer..."
                );

                wait();
            }

            System.out.println(
                    "Quiz processor received the answer!"
            );
        }
        catch (InterruptedException e) {

            System.out.println(
                    "Communication interrupted."
            );
        }
    }

    public synchronized void submitAnswer() {

        answerReady = true;

        System.out.println(
                "User submitted the answer."
        );

        notify();
    }
}

//---------------------------------------------
//inheritance

class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/flashcard_db";

    private static final String USER =
            "root";

    private static final String PASSWORD =
            "your_password";

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}

class FlashcardDAO {

    // CREATE
    public void addFlashcard(
            String question,
            String answer,
            String difficulty) {

        String sql =
                "INSERT INTO flashcards "
                + "(question, answer, difficulty) "
                + "VALUES (?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, question);
            statement.setString(2, answer);
            statement.setString(3, difficulty);

            statement.executeUpdate();

            System.out.println(
                    "Flashcard added successfully."
            );

        }
        catch (SQLException e) {

            System.out.println(
                    "Error adding flashcard: "
                    + e.getMessage()
            );
        }
    }


    // READ
    public void displayFlashcards() {

        String sql =
                "SELECT * FROM flashcards";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet result =
                     statement.executeQuery()) {

            System.out.println();
            System.out.println(
                    "========= DATABASE FLASHCARDS ========="
            );

            while (result.next()) {

                System.out.println(
                        "ID : "
                                + result.getInt("id")
                );

                System.out.println(
                        "Question : "
                                + result.getString("question")
                );

                System.out.println(
                        "Answer : "
                                + result.getString("answer")
                );

                System.out.println(
                        "Difficulty : "
                                + result.getString("difficulty")
                );

                System.out.println(
                        "-------------------------------------"
                );
            }

        }
        catch (SQLException e) {

            System.out.println(
                    "Error reading flashcards: "
                            + e.getMessage()
            );
        }
    }


    // UPDATE
    public void updateFlashcard(
            int id,
            String newQuestion,
            String newAnswer,
            String newDifficulty) {

        String sql =
                "UPDATE flashcards "
                + "SET question = ?, "
                + "answer = ?, "
                + "difficulty = ? "
                + "WHERE id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, newQuestion);
            statement.setString(2, newAnswer);
            statement.setString(3, newDifficulty);
            statement.setInt(4, id);

            int rows =
                    statement.executeUpdate();

            if (rows > 0) {

                System.out.println(
                        "Flashcard updated successfully."
                );

            }
            else {

                System.out.println(
                        "Flashcard not found."
                );
            }

        }
        catch (SQLException e) {

            System.out.println(
                    "Error updating flashcard: "
                            + e.getMessage()
            );
        }
    }


    // DELETE
    public void deleteFlashcard(int id) {

        String sql =
                "DELETE FROM flashcards "
                + "WHERE id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rows =
                    statement.executeUpdate();

            if (rows > 0) {

                System.out.println(
                        "Flashcard deleted successfully."
                );

            }
            else {

                System.out.println(
                        "Flashcard not found."
                );
            }

        }
        catch (SQLException e) {

            System.out.println(
                    "Error deleting flashcard: "
                            + e.getMessage()
            );
        }
    }
}


class JavaFlashcard extends Flashcard {

    public JavaFlashcard(String q,
                         String a,
                         String d,
                         String c) {

        super(q,a,d,c);
    }

    @Override
    public void showType() {

        System.out.println("Category : " + category);
    }

    @Override
    public void printCard() {

        System.out.println("--------------------------------------");
        System.out.println("Difficulty : " + difficulty);
        System.out.println("Question : " + question);
    }

    // Inner Class

    class Hint {

        void showHint() {

            if(question.contains("JVM"))
                System.out.println("-->Hint : It executes Java Bytecode.");

            else if(question.contains("Inheritance"))
                System.out.println("-->Hint : extends keyword.");

            else
                System.out.println("-->Hint : Think about Java Fundamentals.");
        }

    }

}
//---------------------------------------------
//java21


static void demonstrateJDBC() {

    System.out.println();
    System.out.println(
            "========= JDBC DEMO ========="
    );

    FlashcardDAO dao =
            new FlashcardDAO();


    // CREATE
    dao.addFlashcard(
            "What is encapsulation?",
            "Wrapping data and methods together",
            "Medium"
    );


    // READ
    dao.displayFlashcards();
}

static void demonstrateCRUD() {

    System.out.println();
    System.out.println(
            "========= CRUD DEMO ========="
    );

    FlashcardDAO dao =
            new FlashcardDAO();


    // CREATE
    dao.addFlashcard(
            "What is an interface?",
            "A contract implemented by classes",
            "Medium"
    );


    // READ
    dao.displayFlashcards();


    // UPDATE
    dao.updateFlashcard(
            1,
            "What is Java?",
            "Java is a high level programming language",
            "Easy"
    );


    // READ after UPDATE
    dao.displayFlashcards();


    // DELETE
    dao.deleteFlashcard(2);


    // READ after DELETE
    dao.displayFlashcards();
}

//---------------------------------------------

//=============================================
// GENERIC CLASS
//=============================================

class FlashcardBox<T> {

    private ArrayList<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(item);
    }

    public T get(int index) {
        return items.get(index);
    }

    public int size() {
        return items.size();
    }

    public void displayItems() {

        for (T item : items) {
            System.out.println(item);
        }
    }
}
enum FlashcardCategory {
    JAVA,
    OOP,
    STRINGS,
    INHERITANCE
}

class CardInfo {
    
    private String title;
    private int totalCards;

    public CardInfo(String title, int totalCards) {
        this.title = title;
        this.totalCards = totalCards;
    }

    public String getTitle() {
        return title;
    }

    public int getTotalCards() {
        return totalCards;
    }
}

public class Flashcard1 {

    //=============================================
// GENERIC METHOD
//=============================================

static ArrayList<Flashcard> favoriteCards =
        new ArrayList<>();

static LinkedList<Flashcard> quizQueue = new LinkedList<>();

static HashSet<String> completedCategories = new HashSet<>();

static LinkedHashSet<String> earnedBadges = new LinkedHashSet<>();

static TreeSet<String> difficultyLevels = new TreeSet<>();

static EnumSet<FlashcardCategory> availableCategories =
    EnumSet.allOf(FlashcardCategory.class);

static HashMap<String, Integer> categoryXP = new HashMap<>();

static LinkedHashMap<Integer, String> quizHistory = new LinkedHashMap<>();

static TreeMap<String, Integer> categoryStats = new TreeMap<>();

static <T> void displayGeneric(T item) {

    System.out.println("Generic Item : " + item);
}

static void demonstrateCollections() {

    System.out.println();
    System.out.println("========= COLLECTIONS DEMO =========");

    // LinkedList
    quizQueue.clear();

    for (Flashcard card : cards) {
        quizQueue.add(card);
    }

    System.out.println(
            "LinkedList Quiz Queue Size : "
                    + quizQueue.size()
    );


    // HashSet
    completedCategories.clear();

    completedCategories.add("Java");
    completedCategories.add("OOP");
    completedCategories.add("Java");
    completedCategories.add("Strings");

    System.out.println(
            "HashSet Unique Categories : "
                    + completedCategories
    );


    // LinkedHashSet
    earnedBadges.clear();

    earnedBadges.add("First Correct Answer");
    earnedBadges.add("5 Question Streak");
    earnedBadges.add("10 Questions Completed");
    earnedBadges.add("First Correct Answer");

    System.out.println(
            "LinkedHashSet Badges : "
                    + earnedBadges
    );


    // TreeSet
    difficultyLevels.clear();

    difficultyLevels.add("Medium");
    difficultyLevels.add("Easy");
    difficultyLevels.add("Hard");

    System.out.println(
            "TreeSet Sorted Difficulty : "
                    + difficultyLevels
    );


    // EnumSet
    System.out.println(
            "EnumSet Categories : "
                    + availableCategories
    );


    // HashMap + Autoboxing
    categoryXP.clear();

    int javaXP = 50;
    int oopXP = 40;

    categoryXP.put("Java", javaXP);
    categoryXP.put("OOP", oopXP);

    System.out.println(
            "HashMap Category XP : "
                    + categoryXP
    );


    // LinkedHashMap
    quizHistory.clear();

    quizHistory.put(1, "Java - Correct");
    quizHistory.put(2, "OOP - Correct");
    quizHistory.put(3, "Strings - Wrong");

    System.out.println(
            "LinkedHashMap Quiz History : "
                    + quizHistory
    );


    // TreeMap
    categoryStats.clear();

    categoryStats.put("Strings", 3);
    categoryStats.put("Java", 5);
    categoryStats.put("OOP", 4);
    categoryStats.put("Inheritance", 2);

    System.out.println(
            "TreeMap Sorted Statistics : "
                    + categoryStats
    );
}

static void demonstrateIterator() {

    System.out.println();
    System.out.println("========= ITERATOR DEMO =========");

    Iterator<Flashcard> iterator =
            cards.iterator();

    int count = 1;

    while (iterator.hasNext()) {

        Flashcard card = iterator.next();

        System.out.println(
                count + ". "
                        + card.getQuestion()
        );

        count++;
    }
}

//=============================================
// COMMUNICATION DEMONSTRATION
//=============================================

static void demonstrateCommunication() {

    QuizCommunication communication =
            new QuizCommunication();

    Thread processor = new Thread(() -> {

        communication.waitForAnswer();

    });

    Thread user = new Thread(() -> {

        try {

            Thread.sleep(2000);

        }
        catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }

        communication.submitAnswer();

    });

    processor.start();
    user.start();
}

//=============================================
//  VIRTUAL THREAD
//=============================================

static void startNormalThread() {

    Thread thread = new Thread(() -> {

        System.out.println(
                "Normal Java Thread running..."
        );

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println(
                "Normal Thread completed."
        );
    });

    thread.start();

    try {
        thread.join();
    }
    catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}

//=============================================
// BOUNDED TYPE
//=============================================

static <T extends Flashcard> void
displayFlashcard(T card) {

    System.out.println("\nBounded Generic Flashcard");

    System.out.println("Question : "
            + card.getQuestion());

    System.out.println("Answer : "
            + card.getAnswer());

    System.out.println("Difficulty : "
            + card.getDifficulty());
}

//=============================================
// THREAD CREATION
//=============================================

static void startTimer() {

    QuizTimer timer = new QuizTimer(5);

    timer.start();
}

//=============================================
// THREAD PRIORITY
//=============================================

static void demonstratePriority() {

    Thread highPriority = new Thread(() -> {

        System.out.println(
                "High priority task running..."
        );

    });

    Thread lowPriority = new Thread(() -> {

        System.out.println(
                "Low priority task running..."
        );

    });

    highPriority.setPriority(Thread.MAX_PRIORITY);

    lowPriority.setPriority(Thread.MIN_PRIORITY);

    highPriority.start();
    lowPriority.start();
}

//=============================================
// SYNCHRONIZED XP MANAGER
//=============================================

class XPManager {

    private int xp = 0;

    public synchronized void addXP(int amount) {

        xp += amount;

        System.out.println(
                "XP Added : " + amount
        );

        System.out.println(
                "Total XP : " + xp
        );
    }

    public int getXP() {
        return xp;
    }
}

//=============================================
// GENERICS DEMONSTRATION
//=============================================

static void demonstrateGenerics() {

    System.out.println();
    System.out.println(
            "========= GENERICS DEMO ========="
    );

    FlashcardBox<Flashcard> box =
            new FlashcardBox<>();

    if (!cards.isEmpty()) {

        box.add(cards.get(0));

        System.out.println(
                "Flashcards stored using Generic Class: "
                        + box.size()
        );

        displayFlashcard(cards.get(0));
    }

    displayGeneric("Flashcard Learning System");

    displayGeneric(xp);
}

//=============================================
// MULTITHREADING DEMONSTRATION
//=============================================

static void demonstrateMultithreading() {

    System.out.println();
    System.out.println(
            "====== MULTITHREADING DEMO ======"
    );

    startTimer();

    demonstratePriority();

    demonstrateCommunication();
}

//--------------------------------------
// Exception Handling
//--------------------------------------

static void validateAnswer(String input)
        throws InvalidAnswerException {

    if (input == null || input.trim().isEmpty()) {

        throw new InvalidAnswerException(
                "Answer cannot be empty."
        );
    }

    if (!input.equalsIgnoreCase("yes")
            && !input.equalsIgnoreCase("no")) {

        throw new InvalidAnswerException(
                "Please enter only yes or no."
        );
    }
}

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Flashcard> cards =
            new ArrayList<>();

    static ArrayList<Flashcard> favouriteCards =
            new ArrayList<>();

    static int score = 0;

    static int xp = 0;

    static int streak = 0;

    static int highestStreak = 0;

    static String playerName;

    static String selectedCategory;

    static long totalTime = 0;

    //--------------------------------------

    static void welcome() {

        System.out.println();
        System.out.println("==========================================");
        System.out.println("      FLASHCARD LEARNING SYSTEM");
        System.out.println("==========================================");

        System.out.println();

        System.out.println("        (^_^)");
        System.out.println("      Coding Buddy");

        System.out.println();

        System.out.print("Enter your name : ");

        playerName = sc.nextLine();

        System.out.println();

        System.out.println(" Welcome " + playerName);

        System.out.println();

    }
        //--------------------------------------

    static void chooseCategory() {

        System.out.println("Choose Category");

        System.out.println("1.Java");

        System.out.println("2.OOP");

        System.out.println("3.Strings");

        System.out.println("4.Inheritance");

        System.out.print("Choice : ");

        int choice;

try {

    choice = sc.nextInt();
    sc.nextLine();

}
catch(InputMismatchException e) {

    System.out.println(
            "Invalid input! Please enter a number."
    );

    sc.nextLine();

    choice = 1;
}

        switch(choice) {

            case 1:
                selectedCategory = "Java";
                break;

            case 2:
                selectedCategory = "OOP";
                break;

            case 3:
                selectedCategory = "Strings";
                break;

            case 4:
                selectedCategory = "Inheritance";
                break;

            default:
                selectedCategory = "Java";
        }

        System.out.println();

    }
        //--------------------------------------
    // Load all flashcards

    static void loadFlashcards() {

        cards.add(new JavaFlashcard(
                "What is JVM?",
                "Java Virtual Machine",
                "*Easy*",
                "Java"));

        cards.add(new JavaFlashcard(
                "Which keyword is used for inheritance?",
                "extends",
                "*Easy*",
                "Inheritance"));

        cards.add(new JavaFlashcard(
                "Which keyword creates an object?",
                "new",
                "*Easy*",
                "Java"));

        cards.add(new JavaFlashcard(
                "What is OOP?",
                "Object Oriented Programming",
                "**Medium**",
                "OOP"));

        cards.add(new JavaFlashcard(
                "Which class is immutable?",
                "String",
                "**Medium**",
                "Strings"));

        cards.add(new JavaFlashcard(
                "Which class is mutable?",
                "StringBuilder",
                "**Medium**",
                "Strings"));

        cards.add(new JavaFlashcard(
                "Parent class is called?",
                "Superclass",
                "**Medium**",
                "Inheritance"));

        cards.add(new JavaFlashcard(
                "Child class is called?",
                "Subclass",
                "**Medium**",
                "Inheritance"));

        cards.add(new JavaFlashcard(
                "Method Overriding belongs to?",
                "Polymorphism",
                "***Hard***",
                "OOP"));

        cards.add(new JavaFlashcard(
                "Data hiding is called?",
                "Encapsulation",
                "***Hard***",
                "OOP"));

        cards.add(new JavaFlashcard(
                "Blueprint of objects?",
                "Class",
                "*Easy*",
                "Java"));

        cards.add(new JavaFlashcard(
                "Collection used here?",
                "ArrayList",
                "**Medium**",
                "Java"));

        cards.add(new JavaFlashcard(
                "Which keyword prevents inheritance?",
                "final",
                "***Hard***",
                "Inheritance"));

        cards.add(new JavaFlashcard(
                "Keyword for abstract class?",
                "abstract",
                "**Medium**",
                "OOP"));

        cards.add(new JavaFlashcard(
                "Method to compare strings?",
                "equals()",
                "**Medium**",
                "Strings"));
    }

    //--------------------------------------

    static void startQuiz() {

        Collections.shuffle(cards);

        int totalQuestions = 0;

        for(Flashcard card : cards){

            if(!card.getCategory().equalsIgnoreCase(selectedCategory))
                continue;

            totalQuestions++;
        }

        int current = 0;

        for(Flashcard card : cards){

            if(!card.getCategory().equalsIgnoreCase(selectedCategory))
                continue;

            current++;

            System.out.println("\n===================================");

            card.showType();

            card.printCard();

            JavaFlashcard.Hint hint =
                    ((JavaFlashcard)card).new Hint();

            hint.showHint();

            long start = System.currentTimeMillis();

            System.out.println("\nPress ENTER to view answer...");
            sc.nextLine();

            long end = System.currentTimeMillis();

            totalTime += (end-start);

            card.displayAnswer();

            System.out.print("\nDid you know this? (yes/no) : ");

String input = sc.nextLine();

try {

    validateAnswer(input);

    if(input.equalsIgnoreCase("yes")) {

        score++;
        xp += 10;
        streak++;

        if(streak > highestStreak)
            highestStreak = streak;

        System.out.println("\nGreat! That's Correct!");
        System.out.println("+10 XP");
        System.out.println("Streak : " + streak);
    }
    else {

        streak = 0;

        System.out.println("\nKeep Practicing!");
    }
}
catch(InvalidAnswerException e) {

    System.out.println("\nInvalid Input!");
    System.out.println("Error : " + e.getMessage());

    streak = 0;
}

            int progress=(current*100)/totalQuestions;

            System.out.println();

            System.out.print("Progress : ");

            int blocks=progress/10;

            for(int i=1;i<=10;i++){

                if(i<=blocks)
                    System.out.print("*");

                else
                    System.out.print("**");

            }

            System.out.println(" "+progress+"%");

            //--------------------------------

            System.out.print("\n <3 Add to favourites? (yes/no) : ");

            String fav=sc.nextLine();

            if(fav.equalsIgnoreCase("yes")){

                favouriteCards.add(card);

                System.out.println("Added Successfully.");

            }

        }

    }
        //--------------------------------------
    // Display favourite flashcards
    static void showFavourites() {
        String favouriteCards;
        System.out.println("\n=====================================");
        System.out.println(" <3 Favourite Flashcards");
        System.out.println("=====================================");

        if(favouriteCards.isEmpty()) {

            System.out.println("No favourite flashcards selected.");
            return;
        }

        for(Flashcard card : favoriteCards) {

            System.out.println("CARD " + card.getQuestion());

        }

    }

    //--------------------------------------
    // Display final report

    static void finalReport() {

    System.out.println(
            "\n====================================="
    );

    System.out.println(
            "           FINAL REPORT"
    );

    System.out.println(
            "====================================="
    );

    StringBuilder report = new StringBuilder();

    String playerName;
    String selectedCategory;
    int score;
    int xp;
    int highestStreak;
    long totalTime;

    report.append("Name : ")
          .append(playerName)
          .append("\n");

    report.append("Category : ")
          .append(selectedCategory)
          .append("\n");

    report.append("Score : ")
          .append(score)
          .append("\n");

    report.append("XP : ")
          .append(xp)
          .append("\n");

    report.append("Highest Streak : ")
          .append(highestStreak)
          .append("\n");

    report.append("Total Time : ")
          .append(totalTime / 1000)
          .append(" sec\n");

    System.out.println(report);
}

    //--------------------------------------
// CHARACTER STREAM - WRITE FILE
//--------------------------------------

static void saveReportToFile() {
    String playerName;
   String selectedCategory;
   int score;
   int xp;
   int highestStreak;
   long totalTime;

    try {

        FileWriter writer =
                new FileWriter("flashcard_report.txt");

        writer.write("=====================================\n");
        writer.write("       FLASHCARD LEARNING REPORT\n");
        writer.write("=====================================\n");

        writer.write("Name : " + playerName + "\n");
        writer.write("Category : " + selectedCategory + "\n");
        writer.write("Score : " + score + "\n");
        writer.write("XP : " + xp + "\n");
        writer.write("Highest Streak : "
                + highestStreak + "\n");

        writer.write("Total Time : "
                + totalTime / 1000
                + " sec\n");

        writer.write("=====================================\n");

        writer.close();

        System.out.println(
                "\nReport saved successfully!"
        );

    }
    catch(IOException e) {

        System.out.println(
                "Error while writing report: "
                + e.getMessage()
        );
    }
}
//--------------------------------------
// CHARACTER STREAM - READ FILE
//--------------------------------------

static void readPreviousReport() {

    try {

        FileReader reader =
                new FileReader("flashcard_report.txt");

        int character;

        System.out.println();
        System.out.println(
                "========= PREVIOUS REPORT ========="
        );

        while((character = reader.read()) != -1) {

            System.out.print((char) character);
        }

        reader.close();

    }
    catch(FileNotFoundException e) {

        System.out.println(
                "No previous report found."
        );
    }
    catch(IOException e) {

        System.out.println(
                "Error while reading report: "
                + e.getMessage()
        );
    }
}

//--------------------------------------
// BYTE STREAM - BACKUP FILE
//--------------------------------------

static void backupReport() {

    try {

        FileInputStream input =
                new FileInputStream(
                        "flashcard_report.txt"
                );

        FileOutputStream output =
                new FileOutputStream(
                        "flashcard_report_backup.txt"
                );

        int data;

        while((data = input.read()) != -1) {

            output.write(data);
        }

        input.close();
        output.close();

        System.out.println(
                "\nBackup created successfully!"
        );

    }
    catch(FileNotFoundException e) {

        System.out.println(
                "Report file does not exist."
        );
    }
    catch(IOException e) {

        System.out.println(
                "Error during backup: "
                + e.getMessage()
        );
    }
}

    //--------------------------------------
    // ASCII Celebration

    static void celebration() {

        System.out.println();

        System.out.println("************************************");
        System.out.println("WOW CONGRATULATIONS ");
        System.out.println("************************************");

        System.out.println();

        System.out.println("        HAPPY CODING!!!");

        System.out.println("      \\(^_^)/");

        System.out.println();

    }

    //--------------------------------------
    // Motivational Quote

    static void motivationalQuote() {

        String quotes[] = {

                "Practice makes perfect!",
                "Learning never stops!",
                "Code. Learn. Repeat.",
                "Every expert was once a beginner.",
                "Believe in yourself!"
        };

        Random r = new Random();

        System.out.println(" Quote : " +
                quotes[r.nextInt(quotes.length)]);

    }

    //=============================================
// THREAD CLASS - QUIZ TIMER
//=============================================

class QuizTimer extends Thread {

    private int seconds;

    public QuizTimer(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public void run() {

        for (int i = seconds; i > 0; i--) {

            System.out.println(
                    "Timer : " + i + " seconds"
            );

            try {

                Thread.sleep(1000);

            }
            catch (InterruptedException e) {

                System.out.println(
                        "Timer interrupted."
                );

                return;
            }
        }

        System.out.println("Time is up!");
    }
}

    //--------------------------------------
    // Main Method
 public static void main(String[] args) {

        welcome();

        chooseCategory();

        loadFlashcards();

        startQuiz();

        showFavourites();

        //----------------------------------
        // Object Cloning Demonstration

        Flashcard copy = cards.get(0).clone();

        System.out.println("\nClone Demonstration");

        System.out.println(copy);

        //----------------------------------
        // Record Demonstration

        CardInfo info =
                new CardInfo("Flashcard Quiz",
                        Flashcard.getTotalCards());

        System.out.println("\nRecord");

        System.out.println(info);

        //----------------------------------
       //method calls
        finalReport();

       // Save report using Character Stream
       saveReportToFile();

       // Create backup using Byte Streams
       backupReport();

       // Read the saved report
       readPreviousReport();

       celebration();

      motivationalQuote();

      System.out.println();
      System.out.println("====== ADVANCED JAVA FEATURES ======");

      // Generics
      demonstrateGenerics();

      // Multithreading
      demonstrateMultithreading();

      // Thread priority
      demonstratePriority();

      // Java 21 Virtual Thread
      startVirtualThread();
      System.out.println("\nProgram Finished.");

      System.out.println();
      System.out.println(
        "====== PART 5 : JDBC & DATABASE ======");

      demonstrateJDBC();
      demonstrateCRUD();

    }
}
