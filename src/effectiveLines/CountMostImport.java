package effectiveLines;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 曹 on 2017/5/14.
 * 不能处理换行的情况，学习模式匹配
 */

public class CountMostImport {
    class record {
        String[] allrecord;
        Map<String, Integer> importNumMax = new TreeMap<String, Integer>();
        public record() {

        }
    }
    String path = new String();
    List<File> allFiles = new ArrayList<File>();
    private static String CUSTOM_PATH = "./";
    private static String STATIC_PROCESS_STRING = "import";
    private static int INITIALIZE_NUM = 1;
    private static int IMPORT_MAXNUM = 1;
    private String maxImport = new String();
    List<String> content = new ArrayList<String>();
    String importNumMax = new String();
    public CountMostImport(String path) {
        this.path = path;
    }
    public CountMostImport() {
        this.path = CUSTOM_PATH;//默认当前文件夹
    }
    public  record myRecord = new record();
    //将文件内容放入TreeMap中
    public void processContent(List<String> waitForProcessContent) {
       // System.out.println(waitForProcessContent);
        String[] record = StringListToArray(waitForProcessContent);
        Pattern p = Pattern.compile("(?<= ).*(?=;)");
        for (int i = 0; i < record.length; i++) {
            //不能处理换行的情况
            if (record[i].startsWith(STATIC_PROCESS_STRING)) {
                Matcher m = p.matcher(record[i]);
                while (m.find()) {
                   // System.out.println(m.group());
                    if (myRecord.importNumMax.get(m.group()) == null) {
                        myRecord.importNumMax.put(m.group(), INITIALIZE_NUM);
                    } else {
                        int num = myRecord.importNumMax.get(m.group()) + 1;
                        if (num > IMPORT_MAXNUM) {
                            maxImport = m.group();
                        }
                        myRecord.importNumMax.put(m.group(), num);
                    }

                }
            }
        }
    }

    public void processContent() {
        processContent(this.content);

    }

    public void getContentFromFile(File... files) throws Exception {
        File singleFile;
        for (int i = 0; i < files.length; i++) {
            singleFile = files[i];
            getContentFromFile(singleFile);
        }
    }
    //根据allFiles返回所有文件内容到content
    public void getContentFromFile(File file) throws Exception {
        InputStream in = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        while ((line = br.readLine()) != null) {// 从文本中读取文件
            this.content.add(line);
        }

        if (br != null) {
            br.close();
        }

    }
    //根据allFiles返回所有文件内容到content
    public void getContentFromFile(List<File> listfiles) throws Exception {

        File singleFile;
        for (int i = 0; i < listfiles.size(); i++) {
           // System.out.println(listfiles.get(i));
            singleFile = listfiles.get(i);
            getContentFromFile(singleFile);
        }
    }
    //根据allFiles返回所有文件内容到content
    public void getContentFromFile() throws Exception {
        getContentFromFile(this.allFiles);
    }

    public File[] fileListToArray(List<File> listFiles) {
        File[] arrayFiles = new File[listFiles.size()];
        for (int i = 0; i < listFiles.size(); i++) {
            arrayFiles[i] = listFiles.get(i);
        }
        return arrayFiles;
    }

    public String[] StringListToArray(List<String> listStings) {
        String[] arrayFiles = new String[listStings.size()];
        for (int i = 0; i < listStings.size(); i++) {
            arrayFiles[i] = listStings.get(i);
        }
        return arrayFiles;
    }
    //获取路径下所有的文件
    public void getFilesFromPath(String path) {
        File f = new File(path);
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isDirectory()&&files[i].getName().endsWith(".java")) {
                this.allFiles.add(files[i]);
               // System.out.println(files[i]);
            } else if(files[i].isDirectory()){
                getFilesFromPath(files[i].toString());
            }
        }
    }
    //默认从path目录下返回所有文件到AllFiles
    public void getFilesFromPath() {
        getFilesFromPath(this.path);
    }
    public void sort(Map<String, Integer> importNumMax){
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(importNumMax.entrySet());
        //通过比较器实现排序
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            //降序排序
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int num = 1;
        //将结果写入文件
        for(Map.Entry<String, Integer> map : list) {
            if(num <= 10) {
               // System.out.println("出现次数第" + num + "的单词为：" + map.getKey() + "，出现频率为" + map.getValue() + "次");
                System.out.println(map.getKey() + ":" + map.getValue());
              //  System.out.println("--------------------");
                num++;
            }
            else break;
        }
    }

    public static void main(String[] args) throws Exception {
        CountMostImport T = new CountMostImport();
        T.getFilesFromPath();
        T.getContentFromFile();
        T.processContent();
        T.sort(T.myRecord.importNumMax);
    }
}
