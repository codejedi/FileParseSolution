import com.fileparse.service.FileParseService;

public class FileParseOperation {
    public static void main(String[] args) throws Exception {
        FileParseService service = new FileParseService();
        service.readContentsFromFile();
    }
}