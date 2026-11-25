
import java.util.*;

public class JapaneseCharactergame {

    static Scanner sc = new Scanner(System.in);

    // =====================================
    // GAME DATA
    // =====================================

    static Map<String, String> hiragana = new LinkedHashMap<>();
    static Map<String, String> katakana = new LinkedHashMap<>();
    static Map<String, String> vocabulary = new LinkedHashMap<>();

    // =====================================
    // LOAD ALL GAME DATA
    // =====================================

    static void loadData() {

        // HIRAGANA
        String[] hChars = {
                "あ","い","う","え","お",
                "か","き","く","け","こ",
                "さ","し","す","せ","そ",
                "た","ち","つ","て","と",
                "な","に","ぬ","ね","の",
                "は","ひ","ふ","へ","ほ",
                "ま","み","む","め","も",
                "や","ゆ","よ",
                "ら","り","る","れ","ろ",
                "わ","を","ん"
        };

        String[] hSounds = {
                "a","i","u","e","o",
                "ka","ki","ku","ke","ko",
                "sa","shi","su","se","so",
                "ta","chi","tsu","te","to",
                "na","ni","nu","ne","no",
                "ha","hi","fu","he","ho",
                "ma","mi","mu","me","mo",
                "ya","yu","yo",
                "ra","ri","ru","re","ro",
                "wa","wo","n"
        };

        for (int i = 0; i < hChars.length; i++) {
            hiragana.put(hChars[i], hSounds[i]);
        }

        // KATAKANA
        String[] kChars = {
                "ア","イ","ウ","エ","オ",
                "カ","キ","ク","ケ","コ",
                "サ","シ","ス","セ","ソ",
                "タ","チ","ツ","テ","ト",
                "ナ","ニ","ヌ","ネ","ノ",
                "ハ","ヒ","フ","ヘ","ホ",
                "マ","ミ","ム","メ","モ",
                "ヤ","ユ","ヨ",
                "ラ","リ","ル","レ","ロ",
                "ワ","ヲ","ン"
        };

        String[] kSounds = {
                "a","i","u","e","o",
                "ka","ki","ku","ke","ko",
                "sa","shi","su","se","so",
                "ta","chi","tsu","te","to",
                "na","ni","nu","ne","no",
                "ha","hi","fu","he","ho",
                "ma","mi","mu","me","mo",
                "ya","yu","yo",
                "ra","ri","ru","re","ro",
                "wa","wo","n"
        };

        for (int i = 0; i < kChars.length; i++) {
            katakana.put(kChars[i], kSounds[i]);
        }

        // JAPANESE VOCABULARY
        vocabulary.put("ねこ", "cat");
        vocabulary.put("いぬ", "dog");
        vocabulary.put("みず", "water");
        vocabulary.put("ほん", "book");
        vocabulary.put("がっこう", "school");
        vocabulary.put("せんせい", "teacher");
        vocabulary.put("がくせい", "student");
        vocabulary.put("ともだち", "friend");
        vocabulary.put("くるま", "car");
        vocabulary.put("でんしゃ", "train");
        vocabulary.put("やま", "mountain");
        vocabulary.put("かわ", "river");
        vocabulary.put("はな", "flower");
        vocabulary.put("そら", "sky");
        vocabulary.put("つき", "moon");
        vocabulary.put("たいよう", "sun");
        vocabulary.put("あさ", "morning");
        vocabulary.put("よる", "night");
        vocabulary.put("たべもの", "food");
        vocabulary.put("のみもの", "drink");
        vocabulary.put("りんご", "apple");
        vocabulary.put("ねずみ", "mouse");
        vocabulary.put("とり", "bird");
        vocabulary.put("さかな", "fish");
        vocabulary.put("でんわ", "telephone");
        vocabulary.put("じかん", "time");
        vocabulary.put("おかね", "money");
        vocabulary.put("へや", "room");
        vocabulary.put("いえ", "house");
        vocabulary.put("くつ", "shoes");
    }

    // =====================================
    // LEVENSHTEIN DISTANCE
    // =====================================

    static int levenshteinDistance(String s1, String s2) {

        int m = s1.length();
        int n = s2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {

            for (int j = 1; j <= n; j++) {

                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {

                    dp[i][j] = dp[i - 1][j - 1];

                } else {

                    int insert = dp[i][j - 1];
                    int delete = dp[i - 1][j];
                    int replace = dp[i - 1][j - 1];

                    dp[i][j] = 1 + Math.min(
                            insert,
                            Math.min(delete, replace)
                    );
                }
            }
        }

        return dp[m][n];
    }

    // =====================================
    // ANSWER CHECKING
    // =====================================

    static int checkAnswer(String answer, String correctAnswer) {

        String userAnswer = answer.trim().toLowerCase();
        String expectedAnswer = correctAnswer.trim().toLowerCase();

        // Exact answer
        if (userAnswer.equals(expectedAnswer)) {

            System.out.println("Correct Answer!");
            return 1;
        }

        // Calculate spelling difference
        int distance = levenshteinDistance(
                userAnswer,
                expectedAnswer
        );

        // Close answer
        if (
                expectedAnswer.length() >= 3 &&
                        distance <= (expectedAnswer.length() > 5 ? 2 : 1)
        ) {

            System.out.println("Close to correct answer!");
            System.out.println("Your answer: " + answer);
            System.out.println("Correct answer: " + correctAnswer);

            return 0;
        }

        // Wrong answer
        System.out.println("Wrong Answer!");
        System.out.println("Correct answer: " + correctAnswer);

        return 0;
    }

    // =====================================
    // PLAY GAME
    // =====================================

    static void playGame(Map<String, String> data, int mode) {

        List<String> questions =
                new ArrayList<>(data.keySet());

        // Shuffle questions to avoid repetition
        Collections.shuffle(questions);

        System.out.print("\nEnter number of questions: ");

        int totalQuestions = sc.nextInt();

        if (totalQuestions <= 0) {

            System.out.println("Enter a valid number!");
            return;
        }

        if (totalQuestions > questions.size()) {

            totalQuestions = questions.size();

            System.out.println(
                    "Maximum available questions: " + totalQuestions
            );
        }

        int score = 0;
        int closeAnswers = 0;

        for (int i = 0; i < totalQuestions; i++) {

            String question = questions.get(i);

            String correctAnswer = data.get(question);

            System.out.println("\n----------------------------");
            System.out.println("Question " + (i + 1));

            if (mode == 3) {

                System.out.println(
                        "Japanese Word: " + question
                );

                System.out.print(
                        "Enter English Meaning: "
                );

            } else {

                System.out.println(
                        "Japanese Character: " + question
                );

                System.out.print(
                        "Enter Pronunciation: "
                );
            }

            String answer = sc.next();

            int result = checkAnswer(answer, correctAnswer);

            if (result == 1) {
                score++;
            } else if (
                    levenshteinDistance(
                            answer.trim().toLowerCase(),
                            correctAnswer.toLowerCase()
                    ) <= (correctAnswer.length() > 5 ? 2 : 1)
                            && correctAnswer.length() >= 3
            ) {
                closeAnswers++;
            }
        }

        // =====================================
        // FINAL RESULT
        // =====================================

        int wrong = totalQuestions - score - closeAnswers;

        double accuracy =
                (score * 100.0) / totalQuestions;

        System.out.println("\n================================");
        System.out.println("          GAME RESULT");
        System.out.println("================================");

        System.out.println("Total Questions: " + totalQuestions);
        System.out.println("Correct Answers: " + score);
        System.out.println("Close Answers: " + closeAnswers);
        System.out.println("Wrong Answers: " + wrong);

        System.out.println(
                "Final Score: " + score + "/" + totalQuestions
        );

        System.out.printf(
                "Accuracy: %.2f%%\n", accuracy
        );

        System.out.println("================================");
    }

    // =====================================
    // MAIN METHOD
    // =====================================

    public static void main(String[] args) {

        loadData();

        System.out.println("\n==================================");
        System.out.println("      JAPANESE LEARNING GAME");
        System.out.println("==================================");

        while (true) {

            System.out.println("\nSelect Game Mode:");

            System.out.println("1. Hiragana");
            System.out.println("2. Katakana");
            System.out.println("3. Japanese Vocabulary");
            System.out.println("4. Exit");

            System.out.print("\nEnter your choice: ");

            int choice = sc.nextInt();

            if (choice == 4) {

                System.out.println(
                        "\nThank you for playing!"
                );

                break;
            }

            Map<String, String> selectedMap;

            if (choice == 1) {

                selectedMap = hiragana;
                System.out.println("\nHIRAGANA MODE");

            } else if (choice == 2) {

                selectedMap = katakana;
                System.out.println("\nKATAKANA MODE");

            } else if (choice == 3) {

                selectedMap = vocabulary;
                System.out.println("\nVOCABULARY MODE");

            } else {

                System.out.println("Invalid choice!");
                continue;
            }

            playGame(selectedMap, choice);
        }

        sc.close();
    }
}
