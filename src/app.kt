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

    // args[0] directory as a command line argument
    val file = File(args[0]);

    // Creating File.walk instance so I could Iterate through all files in specified directory
    val fileWalker = file.walk();

    val root: DirJson = DirJson("", 0, 0);
    var currentDir: DirJson = root;

    // Iterating through specified directory
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

    // Craeting `data Class to Json` object mapper
    val mapper = jacksonObjectMapper();

    /*
    * Error:Kotlin: Supertypes of the following classes cannot be resolved. Please make sure you have the required
    * dependencies in the classpath:class com.fasterxml.jackson.databind.ObjectMapper, unresolved supertypes:
    * ObjectCodec, Versioned
    */

    // Mapping `root` object to Json as a string (this operation fails to compile with error mentioned above)
    val json = mapper.writeValueAsString(root);

    print(json);
}
