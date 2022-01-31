import java.io.*;

/**
 * ЗАМЕНЁН УНИВЕРСАЛЬНЫМ КЛАССОМ EncDecrypter
 * класс шифрования текста, используется шифр Цезаря
 * стоит помнить, что общее кол-во ключей для этого шифра равно размеру криптоалфавита
 */
public class Encrypter {

    String inputFileName;
    int key;
    String alphabet;

    public Encrypter(String inputFileName, int key, String alphabet) {
        /* конструктор, на входе:
        * inputFileName - название входного файла
        * key - ключ
        * alphabet - русский или английский криптоалфавит */

        this.inputFileName = inputFileName;
        this.key = key;
        this.alphabet = alphabet;

    }

    public void makeEncryption(){
        /*  метод, выполняющий шифрование */
        // int symbol;

        try (BufferedReader br = new BufferedReader (new FileReader(this.inputFileName));
             BufferedWriter bw = new BufferedWriter(new FileWriter("encoded text.txt")))
        {
            // чтение посимвольно
            int symbol;
            while((symbol=br.read())!=-1){
                int encryptedSymbol = this.encryptSymbol(symbol);
                bw.write(encryptedSymbol);
                // System.out.print((char)c);
            }
        } catch (FileNotFoundException fnfExc) {
            System.out.println("Файл не найден! " + fnfExc);
        } catch (IOException exc) {
            System.out.println("Ошибка ввода-вывода: " + exc);
        }
    }

    private int encryptSymbol(int symbol){
        // кодировка каждого символа
        char testSymbolUppercase = Character.toUpperCase((char)(symbol));
        System.out.println(testSymbolUppercase);
        int symbolUppercased = Character.toUpperCase(symbol);

        int alphabetSymbIndex = this.alphabet.indexOf(symbolUppercased);
        if(alphabetSymbIndex == -1)
            return symbol;  // возврат неизменённого символа в случае, если он не обнаружен в криптоалфавите
        else{
            int encryptedSymbIndex = (alphabetSymbIndex + this.key) % this.alphabet.length();
            int encryptedSymbol = (int)(this.alphabet.charAt(encryptedSymbIndex));
            return encryptedSymbol;
        }

    }
}
