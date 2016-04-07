package com.denis.concurrency.mastering_concurrency.chapter_2_socket_server.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WDIDAO {

    private List<WdiData> dataSet = new ArrayList<>();

    public String query(String codCountry, String codIndicator)
    {
        return dataSet.parallelStream()
                .filter(data -> data.countryCode.equals(codCountry) && data.indicatorCode.equals(codIndicator))
                .map(data -> "Key[" + data.countryCode + ":" + data.indicatorCode + "] " + data.line)
                .collect(Collectors.joining(";"));
    }

    public String query(String codCountry, String codIndicator, short year)
    {
        return dataSet.parallelStream()
                .filter(data -> data.countryCode.equals(codCountry) && data.indicatorCode.equals(codIndicator))
                .map(data -> "Key[" + data.countryCode + ":" + data.indicatorCode + "] " + data.line)
                .collect(Collectors.joining(";"));
    }

    public String report(String codIndicator) {
        return dataSet.parallelStream()
                .filter(data -> data.indicatorCode.equals(codIndicator))
                .map(data -> "Key[" + data.countryCode + ":" + data.indicatorCode + "] " + data.line)
                .collect(Collectors.joining(";"));
    }

    private static class Holder {
        private static final WDIDAO instance = new WDIDAO();
    }

    public static WDIDAO getDAO() {
        return Holder.instance;
    }

    private WDIDAO() {
        Path path = Paths.get("C:\\Users\\denis.shuvalov\\Desktop\\WDI", "WDI_Data.csv");
        try
        {
            Files.readAllLines(path).forEach(line -> {
                String[] splits = line.split(",");

                if (splits[2].startsWith("\"")) {
                    splits[2] = String.join(", ", splits[2].substring(1), splits[3].substring(0, splits[3].length() - 1));
                    splits[3] = splits[4];
                }

                WdiData data = new WdiData();
                data.countryName = splits[0];
                data.countryCode = splits[1];
                data.indicatorName = splits[2];
                data.indicatorCode = splits[3];
                data.line = line;
                dataSet.add(data);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class WdiData {
        String countryName;
        String countryCode;
        String indicatorName;
        String indicatorCode;
        String line;
    }
}
