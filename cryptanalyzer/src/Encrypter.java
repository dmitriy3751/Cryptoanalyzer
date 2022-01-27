import java.util.HashMap;
import java.util.Random;

/**
 * класс шифрования текста, используется шифр Цезаря
 * стоит помнить, что общее кол-во ключей для этого шифра равно размеру криптоалфавита
 */
public class Encrypter {

    private static final String russianAlphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ0123456789,.!?:;-\\/ \"";
    private static final String englishAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789,.!?:;-\\/ \"";

    String inputFileName;
    int key;
    String alphabet;

    public static void main(String[] args) {
        String text = "Допустим, мы устанавливаем сдвиг на 3. В таком случае А заменится на Г, Б станет Д,\n" +
                "и так далее.";
        int key = 67;

        String russianAlphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
        String englishAlphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String symbols = ",.!?:;-\\/ ";
        String digits = "0123456789";

        String cryptoAlphabet = russianAlphabet + englishAlphabet + symbols + digits;

        String encr = "";
        // for (int i = 0; i < text.length(); i++) {
        //    encr += cryptoAlphabet.charAt(new Random().nextInt(cryptoAlphabet.length() - 1));
        //}

        System.out.println(encr);
    }

    public Encrypter(String inputFileName, int key, int alphabet) {
        /* конструктор, на входе:
        * inputFileName - название входного файла
        * key - ключ
        * alphabet - выбор русского (0) или английского (1) алфавита */

        this.inputFileName = inputFileName;
        this.key = key;

        if(alphabet == 0)
            this.alphabet = Encrypter.russianAlphabet;
        else
            this.alphabet = Encrypter.englishAlphabet;

    }

    public void make_encryption(){
        /*  метод, выполняющий шифрование */

    }

    private HashMap<Character, Integer> read_file(){
        /* метод чтения текстового файла и возврата мапы частоты встречаемости символов */
        HashMap<Character, Integer> alphabetMap = new HashMap<>();

        for (int i = 0; i < this.alphabet.length(); i++) {
            alphabetMap.put(this.alphabet.charAt(i), 0);
        }

        return alphabetMap;
    }
}
