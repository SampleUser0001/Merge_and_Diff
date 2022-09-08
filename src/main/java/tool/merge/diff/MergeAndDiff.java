package tool.merge.diff;


import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.StringBuilder;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.file.StandardOpenOption;

import java.io.BufferedWriter;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class MergeAndDiff {
    public static void main( String[] args ) throws IOException {
        int argsIndex = 0;
        List<String> fileList = Files.readAllLines(Paths.get(args[argsIndex++]));
        Map<String, List<String>> fileWordsMap = new LinkedHashMap<String, List<String>>();
        
        for(String filePath : fileList) {
            fileWordsMap.put(
                filePath,
                Files.readAllLines(
                    Paths.get(filePath)
                )
            );
        }

        List<String> wordList 
            = fileWordsMap.values()
                          .parallelStream()
                          .flatMap(Collection::stream)
                          .distinct()
                          .collect(Collectors.toList());

        StringBuilder builder = new StringBuilder();

        // ヘッダ行
        builder.append("\t");
        builder.append(String.join("\t",fileList)).append(System.getProperty("line.separator"));
        
        // データ部
        for(String word : wordList){
            builder.append(word).append("\t");
            for(List<String> list : fileWordsMap.values()){
                builder.append(list.contains(word) ? "○" : "×").append("\t");
            }       
            builder.append(System.getProperty("line.separator"));
        }
    
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(args[argsIndex++]), Charset.forName("UTF-8"), StandardOpenOption.CREATE)) {
            writer.write(builder.toString());
        }
        // wordList.stream().forEach(System.out::println);
        
    }
}
