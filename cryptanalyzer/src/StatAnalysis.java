import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.HashMap;

/**
 * класс статистического анализа текста, используется шифр Цезаря
 * стоит помнить, что общее кол-во ключей для этого шифра равно размеру криптоалфавита
 */

public class StatAnalysis {
    String controlFilename;     // имя файла, на основе которого собирается статистика вхождения символов
    String encodedFilename;     // имя зашифрованного файла
    String alphabet;            // алфавит (русский/английский/смешанный)

    public StatAnalysis(String controlFilename, String inputFileName, String alphabet) {
        this.controlFilename = controlFilename;
        this.encodedFilename = inputFileName;
        this.alphabet = alphabet;
    }

    public void makeStatAnalysis(){
        // метод запуска статистического анализа

        // мапа частотной встречаемости символов в контрольном входном файле
        Map<Character, Double> controlFileMap = this.getStatisticsMap(this.controlFilename);
        // мапа встречаемости символов в закодированном файле
        Map<Character, Double> encodedFileMap = this.getStatisticsMap(this.encodedFilename);
        // инициализация мапы сопоставления символов по частоте из двух мап выше
        Map<Character, Character> comparisonMap = new HashMap<>();

        Iterator<Map.Entry<Character, Double>> itControlFileMap = controlFileMap.entrySet().iterator();
        Iterator<Map.Entry<Character, Double>> itEncodedFileMap = encodedFileMap.entrySet().iterator();
        while(itControlFileMap.hasNext() && itEncodedFileMap.hasNext()){
            Map.Entry<Character, Double> controlFilePair = itControlFileMap.next();
            Map.Entry<Character, Double> encodedFilePair = itEncodedFileMap.next();
            comparisonMap.put(encodedFilePair.getKey(), controlFilePair.getKey());  // создание сопоставленной мапы
        }

        File theDir = new File("result");
        theDir.mkdirs();    // проверка и создание при необходимости директории для результрующего файла

        try (BufferedReader br = new BufferedReader (new FileReader(this.encodedFilename));
             BufferedWriter bw = new BufferedWriter(new FileWriter(theDir + File.separator + "stat analysised file.txt")))
        {
            // чтение посимвольно
            int symbol;
            while((symbol=br.read())!=-1){
                if(comparisonMap.containsKey((char)symbol)){    //проверка на наличие символа в нашем криптоалфавите
                    bw.write(comparisonMap.get((char)symbol));  // пишем сопоставленный в мапе раскодированный символ
                } else {
                    bw.write(symbol);       // пишем в файл символы, которых нет в нашем криптоалфавите
                }
            }
        } catch (FileNotFoundException fnfExc) {
            System.out.println("Файл не найден! " + fnfExc);
        } catch (IOException exc) {
            System.out.println("Ошибка ввода-вывода: " + exc);
        }
    }

    private Map<Character, Double> getStatisticsMap(String filename){
        // метод сбора статистики вхождения символов в предоставляемом файле

        Map<Character, Double> statMap = new HashMap<>();
        for (int i = 0; i < this.alphabet.length(); i++) {
            statMap.put(this.alphabet.charAt(i), (double)0);         // проинициализировали мапу, всем символам алфавита присвоили 0
        }

        int charsAmount = 0;    // общее количество символов в тексте

        try (BufferedReader br = new BufferedReader (new FileReader(filename)))
        {
            // чтение посимвольно
            int symbol;
            while((symbol=br.read())!=-1){
                charsAmount += 1;
                int symbolUppercased = Character.toUpperCase(symbol);
                if(statMap.containsKey((char)(symbolUppercased)))     // проверка наличия встреченного в файле символа в алфавите
                    statMap.put((char)(symbolUppercased), statMap.get((char)(symbolUppercased)) + 1);   // инкремент количества встречаемости символа
            }
        } catch (FileNotFoundException fnfExc) {
            System.out.println("Файл не найден! " + fnfExc);
        } catch (IOException exc) {
            System.out.println("Ошибка ввода-вывода: " + exc);
        }

        // вычисляем частотность встречаемости каждого символа мапы в тексте filename
        for(Map.Entry<Character, Double> entry: statMap.entrySet()){
            double frequency = entry.getValue() / charsAmount;
            statMap.put(entry.getKey(), frequency);     // теперь вместо количества встреченных в тексте символов будет их частота
        }

        Map<Character, Double> sortedMap = statMap.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> { throw new AssertionError(); },
                        LinkedHashMap::new
                ));                                     // отсортировали мапу по частоте встречаемости символов

        return sortedMap;
    }

}
