import java.io.*;

/**
 * класс брутфорса текста, используется шифр Цезаря
 * стоит помнить, что общее кол-во ключей для этого шифра равно размеру криптоалфавита
 */

public class BruteForce {

    String inputFileName;       // имя входного файла
    String alphabet;            // алфавит (русский/английский/смешанный)

    public BruteForce(String inputFileName, String alphabet) {
        this.inputFileName = inputFileName;
        this.alphabet = alphabet;
    }

    public int makeBruteForce(){
        int[] encodedChars = readHundredChars();
        int[] decodedChars = new int[100];

        for (int brutedKey = 1; brutedKey < this.alphabet.length(); brutedKey++) {

            // в цикле ниже - формирование расшифрованного текста на очередном подбираемом ключе brutedKey
            for (int i = 0; i < encodedChars.length; i++) {
                int decodedChar = trySymbolDecrypt(encodedChars[i], brutedKey);
                decodedChars[i] = decodedChar;
            }

            // в цилке ниже - проверка встречаемости .(пробел) или ,(пробел)
            for(int i = 0; i < decodedChars.length - 2; i++){
                if(((char)decodedChars[i] == '.' || (char)decodedChars[i] == ',') && (char)decodedChars[i+1] == ' '){
                    return brutedKey;
                }
            }
        }

        return -1;
    }

    private int trySymbolDecrypt(int symbol, int key){
        // метод кодирования или расшифрования единичного символа

        int symbolUppercased = Character.toUpperCase(symbol);               // входной символ в верхнем регистре
        int alphabetSymbIndex = this.alphabet.indexOf(symbolUppercased);    // индекс входного символа в криптоалфавите

        if(alphabetSymbIndex == -1)
            return symbol;  // возврат неизменённого символа в случае, если он не обнаружен в криптоалфавите
        else {
            // индекс символа в криптоалфавите, который получится в результате пробной дешифровки
            int actionedSymbIndex = (alphabetSymbIndex - key) % this.alphabet.length();
            if(actionedSymbIndex < 0)                               // если остаток по модулю окажется < 0
                actionedSymbIndex += this.alphabet.length();

            return (int)(this.alphabet.charAt(actionedSymbIndex));    // возврат результирующего символа в криптоалфавите
        }
    }

    private int[] readHundredChars(){
        // метод считывания строки в 50 символов из файла
        int[] chars = new int[100];   // массив считываемых символов
        try (BufferedReader br = new BufferedReader (new FileReader(this.inputFileName)))
        {
            // чтение посимвольно
            int symbol; // очередной считываемый символ

            for (int i = 0; i <= chars.length - 1 ; i++) {
                if((symbol = br.read())!=-1){
                    chars[i] = symbol;
                } else
                    break;
            }
            // возврат считанного из файла массива в 50 символов (или меньше, зависит от файла)

        } catch (FileNotFoundException fnfExc) {
            System.out.println("Файл не найден! " + fnfExc);
        } catch (IOException exc) {
            System.out.println("Ошибка ввода-вывода: " + exc);
        }
        return chars;
    }


}
