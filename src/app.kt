import java.io.File;

data class JsonDir(
        var name: String,
        var size: Long,
        var lastModified: Long,
        var files: MutableList<JsonFile> = mutableListOf<JsonFile>(),
        var directories: MutableList<JsonDir> = mutableListOf<JsonDir>()
)

data class JsonFile(
        val name: String,
        val size: Long,
        val lastModified: Long
)

fun main(args: Array<String>) {

    val file = File("C:\\Users\\Egidijus Lileika\\Desktop\\1\\");
    val fileWalker = file.walk();

    val root: JsonDir = JsonDir("", 0, 0);
    var currentDir: JsonDir = root;

    fileWalker.forEach {
        if(currentDir.name == "") {
            currentDir.name = it.name;
            currentDir.size = it.length();
            currentDir.lastModified = it.lastModified();
        }
        else if(it.isDirectory) {
            currentDir.directories.add(JsonDir(it.name, it.length(), it.lastModified()));
            currentDir = currentDir.directories.last();
        }
        else {
            currentDir.files.add(JsonFile(it.name, it.length(), it.lastModified()));
        }
    }

    println(root.toString());
}
