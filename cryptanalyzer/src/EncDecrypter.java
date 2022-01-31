import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * класс шифрования и расшифрования текста, используется шифр Цезаря
 * стоит помнить, что общее кол-во ключей для этого шифра равно размеру криптоалфавита
 */

public class EncDecrypter {

    boolean neededToEncrypt;    // true - режим шифрования, false - режим расшифрования
    String inputFileName;       // имя входного файла
    int key;                    // ключ
    String alphabet;            // алфавит (русский/английский/смешанный)

    public EncDecrypter(boolean neededToEncrypt, String inputFileName, int key, String alphabet) {
        /* конструктор, на входе:
         * neededToEncrypt - выбор режима работа с текстом: true - шифрование, false - расшифрование
         * inputFileName - название входного файла
         * key - ключ
         * alphabet - русский или английский криптоалфавит */

        this.neededToEncrypt = neededToEncrypt;
        this.inputFileName = inputFileName;
        this.key = key;
        this.alphabet = alphabet;

    }

    public void makeAction(){
        /*  метод, выполняющий шифрование или расшифрование (указано в this.neededToEncrypt) */
        String outputFilename;

        File theDir = new File("result");
        theDir.mkdirs();

        if(java.util.Objects.equals(this.neededToEncrypt, true))
            outputFilename = theDir + File.separator + "encoded text.txt";
        else
            outputFilename = theDir + File.separator + "decoded text.txt";

        try (BufferedReader br = new BufferedReader (new FileReader(this.inputFileName));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilename)))
        {
            // чтение посимвольно
            int symbol;
            while((symbol=br.read())!=-1){
                int actionedSymbol = this.makeSymbolAction(symbol);
                bw.write(actionedSymbol);

            }
        } catch (FileNotFoundException fnfExc) {
            System.out.println("Файл не найден! " + fnfExc);
        } catch (IOException exc) {
            System.out.println("Ошибка ввода-вывода: " + exc);
        }
    }

    private int makeSymbolAction(int symbol){
        // метод кодирования или расшифрования единичного символа

        int symbolUppercased = Character.toUpperCase(symbol);               // входной символ в верхнем регистре
        int alphabetSymbIndex = this.alphabet.indexOf(symbolUppercased);    // индекс входного символа в криптоалфавите

        if(alphabetSymbIndex == -1)
            return symbol;  // возврат неизменённого символа в случае, если он не обнаружен в криптоалфавите
        else if(java.util.Objects.equals(this.neededToEncrypt, true)) {

            // индекс символа в криптоалфавите, который получится в результате шифрования
            int actionedSymbIndex = (alphabetSymbIndex + this.key) % this.alphabet.length();
            return (int)(this.alphabet.charAt(actionedSymbIndex));    // возврат результирующего символа в криптоалфавите

        } else {

            // индекс символа в криптоалфавите, который получится в результате расшифрования
            int actionedSymbIndex = (alphabetSymbIndex - this.key) % this.alphabet.length();
            if(actionedSymbIndex < 0)                               // если остаток по модулю окажется < 0
                actionedSymbIndex += this.alphabet.length();

            return (int)(this.alphabet.charAt(actionedSymbIndex));    // возврат результирующего символа в криптоалфавите
        }
    }
}
