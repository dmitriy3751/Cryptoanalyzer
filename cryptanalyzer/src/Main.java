import java.util.Scanner;
import java.io.File;

public class Main {

    private static final String russianAlphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ0123456789,.!?:;-\\/ \"";
    private static final String englishAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789,.!?:;-\\/ \"";
    private static final String rusEngAlphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                                                    "0123456789,.!?:;-\\/ \"";

    public static void main(String[] args) {
        System.out.println("Выберите режим работы программы:");
        System.out.println("1. - Шифрование");
        System.out.println("2. - Расшифрование");
        System.out.println("3. - Криптоанализ");

        Scanner console = new Scanner(System.in);

        if(console.hasNextInt()) {

            int workModeInt = console.nextInt();
            if (workModeInt == 1 || workModeInt == 2) {
                makeEncDecryption(workModeInt, console);
            } else if (workModeInt == 3) {
                makeCryptoAnalize(console);
            } else {
                System.out.println("Введены неправильный номер режима работы!");
            }
        } else {
            System.out.println("Введен неправильный формат номера выбора режима работы!");
        }

    }

    private static void makeEncDecryption(int encDecMode, Scanner console){

        int key;
        int cryptoAlphNumber;
        String inputFilename;
        String workMode;
        String outputFilename;

        if(encDecMode == 1) {
            workMode = "шифрования";
            outputFilename = "encoded.txt";
        } else {
            workMode = "расшифрования";
            outputFilename = "decoded.txt";
        }

        System.out.println("Выбран режим " + workMode + " текста. Введите путь к текстовому файлу с исходным текстом.");
        console.nextLine();
        inputFilename = console.nextLine();

        System.out.println("Введите ключ.");

        if (console.hasNextInt()) {
            key = console.nextInt();
        } else {
            System.out.println("Введён неверный ключ!");
            return;
        }

        System.out.println("Выберите криптоалфавит: 1 - русский, 2 - английский, 3 - смешанный");

        if (console.hasNextInt()) {
            cryptoAlphNumber = console.nextInt();
        } else {
            System.out.println("Введён неверный номер криптоалфавита!");
            return;
        }

        String alphabyte = chooseAlph(cryptoAlphNumber);
        boolean neededtoEncrypt = encDecMode == 1;

        if(checkFileExistence(inputFilename) && !alphabyte.equals("")){
            EncDecrypter actioner = new EncDecrypter(neededtoEncrypt, inputFilename, key, alphabyte);
            actioner.makeAction();
            System.out.println("Процедура " + workMode + " завершена! Итоговый файл находится в result/" + outputFilename + ".");
        } else {
            System.out.println("Введен неправильный путь к файлу и/или номер криптоалфавита!");
        }
    }

    private static void makeCryptoAnalize(Scanner console){
        System.out.println("Выберите режим криптоанализа: 1 - брутфорс, 2 - статистический анализ");
        if(console.hasNextInt()) {
            int cryptoAnalizeIntMode = console.nextInt();
            if(cryptoAnalizeIntMode == 1){                          // брутфорс
                makeBruteForce(console);
            } else if (cryptoAnalizeIntMode == 2){
                makeStatAnalysis(console);                          // статистический криптоанализ
            } else {
                System.out.println("Введён неверный параметр выбора режима криптоанализа!");
            }
        }
        else{
            System.out.println("Введён неверный параметр выбора режима криптоанализа!");
        }

    }

    private static void makeBruteForce(Scanner console){
        int cryptoAlphNumber;   // выбранный криптоалфавит

        System.out.println("Выбран режим брутфорса. Введите путь к текстовому файлу для дешифровки.");

        console.nextLine();
        String inputFilename = console.nextLine();

        System.out.println("Выберите криптоалфавит: 1 - русский, 2 - английский, 3 - смешанный");

        if (console.hasNextInt()) {
            cryptoAlphNumber = console.nextInt();
        } else {
            System.out.println("Введён неверный номер криптоалфавита!");
            return;
        }
        String alphabyte = chooseAlph(cryptoAlphNumber);

        if(checkFileExistence(inputFilename) && !alphabyte.equals("")){
            BruteForce bruteforcer = new BruteForce(inputFilename, alphabyte);
            int brutedKey = bruteforcer.makeBruteForce();                     // получен ключ шифра

            if(brutedKey != -1) {   // проверка, удалось ли сбрутить текст

                EncDecrypter actioner = new EncDecrypter(false, inputFilename, brutedKey, alphabyte);   // расшифровка
                actioner.makeAction();

                String brutedKeyVariants = brutedKey + " + x*" + alphabyte.length();

                System.out.println("Завершён процесс брутфорса файла! Ключ - (" + brutedKeyVariants + "), где x - " +
                        "любое целое число от 0 (для вариантов, когда ключ больше мощности криптоалфавита)." +
                        " Декодированный из " + inputFilename + " файл находится в " + "result/decoded.txt");
            } else {
                System.out.println("К сожалению, не удалось сбрутить ключ! Вероятно, в исходном незашифрованном тексте " +
                        "не содержатся пробелы.");
            }
        } else {
            System.out.println("Введен неправильный путь к файлу и/или номер криптоалфавита!");
        }
    }

    private static void makeStatAnalysis(Scanner console){
        int cryptoAlphNumber;   // выбранный криптоалфавит

        System.out.println("Выбран режим статистического криптоанализа. Введите путь к текстовому файлу для выборки статистики " +
                "на незашифрованном файле.");

        console.nextLine();
        String controlFileName = console.nextLine();

        System.out.println("Введите путь к зашифрованному текстовому файлу");

        String encodedFilename = console.nextLine();

        System.out.println("Выберите криптоалфавит: 1 - русский, 2 - английский, 3 - смешанный");

        if (console.hasNextInt()) {
            cryptoAlphNumber = console.nextInt();
        } else {
            System.out.println("Введён неверный номер криптоалфавита!");
            return;
        }
        String alphabyte = chooseAlph(cryptoAlphNumber);

        if(checkFileExistence(controlFileName) && checkFileExistence(encodedFilename) && !alphabyte.equals("")){
            StatAnalysis statanalyser = new StatAnalysis(controlFileName, encodedFilename, alphabyte);
            statanalyser.makeStatAnalysis();

            System.out.println("Завершён процесс стат.анализа файла! Декодированный из " +
                    encodedFilename + "файл находится в " + "result/stat analysised file.txt");
        } else {
            System.out.println("Введен неправильный путь к файлам и/или номер криптоалфавита!");
        }
    }

    private static boolean checkFileExistence(String inputFilename){
        // проверка существования входного файла
        if(inputFilename.equals("")){
            // System.out.println("Введена пустая строка!");
            return false;
        } else {
            File file = new File(inputFilename);
            try {
                return file.exists();
            } catch (SecurityException secExc){
                System.out.println("ОС не разрешила доступ к файлу " + inputFilename);
                return false;
            }
        }
    }

    private static String chooseAlph(int alphNumber){
        // конвертация номера алфавита из меню в строку криптоалфавита
        if(alphNumber == 1)
            return russianAlphabet;
        else if(alphNumber == 2)
            return  englishAlphabet;
        else if(alphNumber == 3)
            return rusEngAlphabet;
        else {
            System.out.println("Выбран неверный номер криптоалфита!");
            return "";
        }
    }
}
