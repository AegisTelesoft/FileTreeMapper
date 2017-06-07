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

fun scanDirectory(directory: String, treeNode: DirJson) {
    val file = File(directory)
    val fileWalker = file.walk().maxDepth(1)

    fileWalker.forEach {
        if(it.isDirectory) {
            if(directory != it.path) {
                treeNode.directories.add(DirJson(it.name, it.length(), it.lastModified()))
                scanDirectory(it.path, treeNode.directories.last())
            }
            else{
                treeNode.name = it.name;
                treeNode.size = it.length();
                treeNode.lastModified = it.lastModified();
            }
        }
        else {
            treeNode.files.add(FileJson(it.name, it.length(), it.lastModified()))
        }
    }
}

fun main(args: Array<String>) {

    val root: DirJson = DirJson("", 0, 0);

    scanDirectory(args[0], root);

    // Craeting `data Class to Json` object mapper
    val mapper = jacksonObjectMapper();

    // Mapping `root` object to Json as a string
    val json = mapper.writeValueAsString(root);

    print(json);
}
