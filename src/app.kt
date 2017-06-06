import java.io.File;
import com.fasterxml.jackson.module.kotlin.*

data class DirJson(
        var name: String,
        var size: Long,
        var lastModified: Long,
        var files: MutableList<FileJson> = mutableListOf<FileJson>(),
        var directories: MutableList<DirJson> = mutableListOf<DirJson>()
)

data class FileJson(
        val name: String,
        val size: Long,
        val lastModified: Long
)

fun main(args: Array<String>) {

    val file = File(args[0]);
    val fileWalker = file.walk();

    val root: DirJson = DirJson("", 0, 0);
    var currentDir: DirJson = root;

    fileWalker.forEach {
        if(currentDir.name == "") {
            currentDir.name = it.name;
            currentDir.size = it.length();
            currentDir.lastModified = it.lastModified();
        }
        else if(it.isDirectory) {
            currentDir.directories.add(DirJson(it.name, it.length(), it.lastModified()));
            currentDir = currentDir.directories.last();
        }
        else {
            currentDir.files.add(FileJson(it.name, it.length(), it.lastModified()));
        }
    }

    val mapper = jacksonObjectMapper();
    val json = mapper.writeValueAsString(root);

    print(json);
}
